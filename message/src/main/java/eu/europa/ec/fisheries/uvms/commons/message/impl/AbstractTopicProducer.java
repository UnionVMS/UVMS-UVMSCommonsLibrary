/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.uvms.commons.message.impl;

import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import javax.annotation.PostConstruct;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.DeliveryMode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTopicProducer {

    public static final String SERVICE_NAME = "ServiceName";
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractProducer.class);
    private ConnectionFactory connectionFactory;
    private Destination destination;

    @PostConstruct
    public void initializeConnectionFactory() {
        connectionFactory = JMSUtils.lookupConnectionFactory();
        destination = getDestination();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendEventBusMessage(String text, String serviceName, int jmsDeliveryMode, long timeToLiveInMillis) throws MessageException {
        Connection connection = null;
        Session session = null;
        javax.jms.MessageProducer producer = null;
        try {
            connection = getConnection();
            session = JMSUtils.connectToQueue(connection);
            LOGGER.info("Sending message to EventBus...");
            TextMessage message = session.createTextMessage(text);
            message.setStringProperty(SERVICE_NAME, serviceName);
            producer = session.createProducer(destination);
            producer.setDeliveryMode(jmsDeliveryMode);
            producer.setTimeToLive(timeToLiveInMillis);
            producer.send(message);
            LOGGER.info("Message with {} has been successfully sent.", message.getJMSMessageID());
            return message.getJMSMessageID();
        } catch (JMSException e) {
            LOGGER.error("[ Error when sending message. ] {}", e.getMessage());
            throw new MessageException("[ Error when sending message. ]", e);
        } finally {
            JMSUtils.disconnectQueue(connection, session, producer);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendEventBusMessage(String text, String serviceName) throws MessageException {
        return sendEventBusMessage(text, serviceName,DeliveryMode.PERSISTENT, 0L);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendEventBusMessage(String text, String serviceName, Destination replyToDestination) throws MessageException {
        Connection connection = null;
        Session session = null;
        javax.jms.MessageProducer producer = null;
        try {
            connection = getConnection();
            session = JMSUtils.connectToQueue(connection);
            LOGGER.info("Sending message to EventBus...");
            TextMessage message = session.createTextMessage(text);
            message.setStringProperty(SERVICE_NAME, serviceName);
            message.setJMSReplyTo(replyToDestination);
            producer = session.createProducer(destination);
            producer.send(message);
            return message.getJMSMessageID();
        } catch (JMSException e) {
            throw new MessageException("Error while trying to send EventBus Message..");
        } finally {
            JMSUtils.disconnectQueue(connection, session, producer);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendEventBusMessageWithSpecificIds(String text, String serviceName, Destination replyToDestination, String messageId, String messageCorrelationId) throws MessageException {
        Connection connection = null;
        Session session = null;
        javax.jms.MessageProducer producer = null;
        try {
            connection = getConnection();
            session = JMSUtils.connectToQueue(connection);
            LOGGER.info("Sending message to EventBus...");
            TextMessage message = session.createTextMessage(text);
            message.setStringProperty(SERVICE_NAME, serviceName);
            message.setJMSReplyTo(replyToDestination);
            if(StringUtils.isNotEmpty(messageId)){
                message.setJMSMessageID(messageId);
            }
            if(StringUtils.isNotEmpty(messageCorrelationId)){
                message.setJMSCorrelationID(messageCorrelationId);
            }
            producer = session.createProducer(destination);
            producer.send(message);
            return message.getJMSMessageID();
        } catch (JMSException e) {
            throw new MessageException("Error while trying to send EventBus Message..");
        } finally {
            JMSUtils.disconnectQueue(connection, session, producer);
        }
    }


    protected final ConnectionFactory getConnectionFactory() {
        if (connectionFactory == null) {
            connectionFactory = JMSUtils.lookupConnectionFactory();
        }
        return connectionFactory;
    }

    public final Destination getDestination() {
        if (destination == null && StringUtils.isNotEmpty(getDestinationName())) {
            destination = JMSUtils.lookupTopic(getDestinationName());
        }
        return destination;
    }

    protected final Connection getConnection() throws JMSException {
        return getConnectionFactory().createConnection();
    }

    protected abstract String getDestinationName();
}
