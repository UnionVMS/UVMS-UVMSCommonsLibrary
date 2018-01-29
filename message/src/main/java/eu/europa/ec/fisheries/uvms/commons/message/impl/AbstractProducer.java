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

package eu.europa.ec.fisheries.uvms.commons.message.impl;

import eu.europa.ec.fisheries.uvms.commons.message.api.Fault;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageProducer;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractProducer implements MessageProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractProducer.class);

    private ConnectionFactory connectionFactory;

    private Destination destination;

    @PostConstruct
    public void initializeConnectionFactory() {
        connectionFactory = JMSUtils.lookupConnectionFactory();
        destination = getDestination();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendModuleMessage(final String text, final Destination replyTo) throws MessageException {
        return sendModuleMessageWithProps(text, replyTo, null);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendModuleMessage(final String text, final String replyToQueueName) throws MessageException {
        final Queue replyQueue = JMSUtils.lookupQueue(replyToQueueName);
        return sendModuleMessageWithProps(text, replyQueue, null);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendModuleMessageWithProps(final String text, final Destination replyTo, Map<String, String> props) throws MessageException {
        Connection connection = null;
        Session session = null;
        javax.jms.MessageProducer producer = null;
        try {
            connection = getConnection();
            session = JMSUtils.connectToQueue(connection);
            LOGGER.info("Sending message with replyTo: [{}]", replyTo);
            LOGGER.debug("Message content : [{}]", text);
            if (connection == null || session == null) {
                throw new MessageException("[ Connection or session is null, cannot send message ] ");
            }
            TextMessage message = session.createTextMessage();
            if (MapUtils.isNotEmpty(props)) {
                for (Object o : props.entrySet()) {
                    Map.Entry<String, String> entry = (Map.Entry) o;
                    message.setStringProperty(entry.getKey(), entry.getValue());
                }
            }
            message.setJMSReplyTo(replyTo);
            message.setText(text);
            producer = session.createProducer(getDestination());
            producer.send(message);
            LOGGER.info("Message with {} has been successfully sent.", message.getJMSMessageID());
            return message.getJMSMessageID();
        } catch (final JMSException e) {
            LOGGER.error("[ Error when sending message. ] {}", e.getMessage());
            throw new MessageException("[ Error when sending message. ]", e);
        } finally {
            JMSUtils.disconnectQueue(connection, session, producer);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendModuleResponseMessage(final TextMessage message, final String text, final String moduleName) {
        Connection connection = null;
        Session session = null;
        javax.jms.MessageProducer producer = null;
        try {
            connection = getConnection();
            session = JMSUtils.connectToQueue(connection);
            LOGGER.info("Sending message back to recipient from" + moduleName + " with correlationId {} on queue: {}", message.getJMSMessageID(), message.getJMSReplyTo());
            TextMessage response = session.createTextMessage(text);
            response.setJMSCorrelationID(message.getJMSMessageID());
            producer = session.createProducer(message.getJMSReplyTo());
            producer.send(response);
            session.close();
        } catch (final JMSException e) {
            LOGGER.error("[ Error when returning" + moduleName + "request. ] {} {}", e.getMessage(), e.getStackTrace());
        } finally {
            JMSUtils.disconnectQueue(connection, session, producer);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendModuleResponseMessage(final TextMessage message, final String text) {
        Connection connection = null;
        Session session = null;
        javax.jms.MessageProducer producer = null;
        try {
            connection = getConnection();
            session = JMSUtils.connectToQueue(connection);
            LOGGER.info("Sending message back to recipient from  with correlationId {} on queue: {}", message.getJMSMessageID(), message.getJMSReplyTo());
            TextMessage response = session.createTextMessage(text);
            response.setJMSCorrelationID(message.getJMSMessageID());
            producer = session.createProducer(message.getJMSReplyTo());
            producer.send(response);
        } catch (final JMSException e) {
            LOGGER.error("[ Error when returning request. ] {} {}", e.getMessage(), e.getStackTrace());
        } finally {
            JMSUtils.disconnectQueue(connection, session, producer);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendFault(final TextMessage message, Fault fault) {
        Connection connection = null;
        Session session = null;
        javax.jms.MessageProducer producer = null;
        try {
            String text = JAXBUtils.marshallJaxBObjectToString(fault);
            connection = getConnection();
            session = JMSUtils.connectToQueue(connection);
            LOGGER.debug("Sending message back to recipient from  with correlationId {} on queue: {}", message.getJMSMessageID(), message.getJMSReplyTo());
            final TextMessage response = session.createTextMessage();
            response.setText(text);
            producer = session.createProducer(message.getJMSReplyTo());
            producer.send(response);
        } catch (JMSException | JAXBException e) {
            LOGGER.error("[ Error when returning request. ] {} {}", e.getMessage(),
                    e.getStackTrace());
        } finally {
            JMSUtils.disconnectQueue(connection, session, producer);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendMessageWithSpecificIds(String messageToSend, Destination destination, Destination replyTo, String jmsMessageID, String jmsCorrelationID) throws JMSException {
        if(destination == null){
            throw new JMSException("Destination cannot be null!");
        }
        Connection connection = null;
        Session session = null;
        javax.jms.MessageProducer producer = null;
        String corrId = null;
        try {
            connection = getConnectionFactory().createConnection();
            session = JMSUtils.connectToQueue(connection);
            producer = session.createProducer(destination);
            LOGGER.debug("Sending message with correlationId {} on queue: {}", jmsCorrelationID, destination);
            final TextMessage message = session.createTextMessage(messageToSend);
            if (StringUtils.isNotEmpty(jmsMessageID)) {
                message.setJMSMessageID(jmsMessageID);
            }
            if (StringUtils.isNotEmpty(jmsCorrelationID)) {
                message.setJMSCorrelationID(jmsCorrelationID);
            } else {
                message.setJMSCorrelationID(message.getJMSMessageID());
            }
            message.setJMSReplyTo(replyTo);
            producer.send(message);
            corrId = message.getJMSMessageID();
        } catch (JMSException e) {
            LOGGER.error("[ Error when returning request. ] {} {}", e.getMessage(), e.getStackTrace());
        } finally {
            JMSUtils.disconnectQueue(connection, session, producer);
        }
        return corrId;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendMessageToSpecificQueue(Destination destination, Destination replyTo, String messageToSend) {
        Connection connection = null;
        Session session = null;
        javax.jms.MessageProducer producer = null;
        String corrId = null;
        try {
            connection = getConnectionFactory().createConnection();
            session = JMSUtils.connectToQueue(connection);
            producer = session.createProducer(destination);
            LOGGER.debug("Sending message with correlationId {} on queue: {}", destination);
            final TextMessage message = session.createTextMessage(messageToSend);
            message.setJMSReplyTo(replyTo);
            producer.send(message);
            corrId = message.getJMSMessageID();
        } catch (JMSException e) {
            LOGGER.error("[ Error when returning request. ] {} {}", e.getMessage(), e.getStackTrace());
        } finally {
            JMSUtils.disconnectQueue(connection, session, producer);
        }
        return corrId;
    }

    protected final ConnectionFactory getConnectionFactory() {
        if (connectionFactory == null) {
            connectionFactory = JMSUtils.lookupConnectionFactory();
        }
        return connectionFactory;
    }

    public final Destination getDestination() {
        if (destination == null && StringUtils.isNotEmpty(getDestinationName())) {
            destination = JMSUtils.lookupQueue(getDestinationName());
        }
        return destination;
    }

    protected final Connection getConnection() throws JMSException {
        return getConnectionFactory().createConnection();
    }
}
