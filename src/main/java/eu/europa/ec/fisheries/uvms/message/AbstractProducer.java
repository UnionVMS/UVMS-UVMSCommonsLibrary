/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.message;

import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;

@Slf4j
public abstract class AbstractProducer implements MessageProducer {

    private ConnectionFactory connectionFactory;
    private Destination destination;

    private Connection connection = null;
    private Session session = null;

    @PostConstruct
    protected void initializeConnectionFactory() {
        log.debug("Open connection to JMS broker");
        InitialContext ctx;
        try {
            ctx = new InitialContext();
        } catch (Exception e) {
            log.error("Failed to get InitialContext", e);
            throw new RuntimeException(e);
        }
        try {
            connectionFactory = (QueueConnectionFactory) ctx.lookup(MessageConstants.CONNECTION_FACTORY);
        } catch (NamingException ne) {
            //if we did not find the connection factory we might need to add java:/ at the start
            log.debug("Connection Factory lookup failed for " + MessageConstants.CONNECTION_FACTORY);
            String wfName = "java:/" + MessageConstants.CONNECTION_FACTORY;
            try {
                log.debug("trying " + wfName);
                connectionFactory = (QueueConnectionFactory) ctx.lookup(wfName);
            } catch (Exception e) {
                log.error("Connection Factory lookup failed for both " + MessageConstants.CONNECTION_FACTORY + " and " + wfName);
                throw new RuntimeException(e);
            }
        }

        destination = JMSUtils.lookupQueue(ctx, getDestinationName());

    }


    public String sendModuleMessage(String text, Destination replyTo, Map<String, String> messageProperties) throws MessageException {
        try {
            connectToQueue();

            log.debug("Sending message with replyTo: [{}]", replyTo);
            log.trace("Message content : [{}]", text);

            if (connection == null || session == null) {
                throw new MessageException("[ Connection or session is null, cannot send message ] ");
            }

            TextMessage message = session.createTextMessage();
            if(MapUtils.isNotEmpty(messageProperties)){
                for(Map.Entry<String, String> entry : messageProperties.entrySet()){
                    message.setStringProperty(entry.getKey(), entry.getValue());
                }
            }
            message.setJMSReplyTo(replyTo);
            message.setText(text);
            session.createProducer(getDestination()).send(message);
            log.debug("Message with {} has been successfully sent.", message.getJMSMessageID());
            return message.getJMSMessageID();

        } catch (JMSException e) {
            log.error("[ Error when sending message. ] {}", e.getMessage());
            throw new MessageException("[ Error when sending message. ]", e);
        } finally {
            disconnectQueue();
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendModuleMessage(String text, Destination replyTo) throws MessageException {
        try {
            connectToQueue();

            log.debug("Sending message with replyTo: [{}]", replyTo);
            log.trace("Message content : [{}]", text);

            if (connection == null || session == null) {
                throw new MessageException("[ Connection or session is null, cannot send message ] ");
            }

            TextMessage message = session.createTextMessage();
            message.setJMSReplyTo(replyTo);
            message.setText(text);
            session.createProducer(getDestination()).send(message);
            log.debug("Message with {} has been successfully sent.", message.getJMSMessageID());
            return message.getJMSMessageID();

        } catch (JMSException e) {
            log.error("[ Error when sending message. ] {}", e.getMessage());
            throw new MessageException("[ Error when sending message. ]", e);
        } finally {
            disconnectQueue();
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendModuleResponseMessage(TextMessage message, String text, String moduleName) {
        try {
            log.debug("Sending message back to recipient from" + moduleName + " with correlationId {} on queue: {}", message.getJMSMessageID(),
                    message.getJMSReplyTo());
            connectToQueue();
            TextMessage response = session.createTextMessage(text);
            response.setJMSCorrelationID(message.getJMSMessageID());
            session.createProducer(message.getJMSReplyTo()).send(response);
        } catch (JMSException e) {
            log.error("[ Error when returning" + moduleName + "request. ] {} {}", e.getMessage(), e.getStackTrace());
        } finally {
            disconnectQueue();
        }
    }

    protected void connectToQueue() throws JMSException {
        connection = connectionFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        connection.start();
        log.debug("Connecting to {}", getDestination());
    }

    protected void disconnectQueue() {
        try {
            if (connection != null) {
                connection.stop();
                connection.close();
            }
        } catch (JMSException e) {
            log.error("[ Error when stopping or closing JMS queue. ] {} {}", e.getMessage(), e.getStackTrace());
        }
    }

    public Session getSession() {
        return session;
    }


    protected Destination getDestination() {
        return destination;
    }

    public Connection getConnection() {
        return connection;
    }
}
