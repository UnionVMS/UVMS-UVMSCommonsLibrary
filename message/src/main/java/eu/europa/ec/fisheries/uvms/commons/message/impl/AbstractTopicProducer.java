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
import eu.europa.ec.fisheries.uvms.commons.message.context.MappedDiagnosticContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.*;

public abstract class AbstractTopicProducer {

    private static final String SERVICE_NAME = "ServiceName";

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractProducer.class);

    private Destination destination;

    private static final int RETRIES = 100;

    @PostConstruct
    public void initializeConnectionFactory() {
        destination = getDestination();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendEventBusMessage(String text, String serviceName, int jmsDeliveryMode, long timeToLiveInMillis) throws MessageException {
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        try {
            connection = JMSUtils.getConnectionWithRetry(RETRIES);
            session = JMSUtils.createSessionAndStartConnection(connection);
            LOGGER.debug("Sending message to EventBus...");
            TextMessage message = session.createTextMessage(text);
            message.setStringProperty(SERVICE_NAME, serviceName);
            MappedDiagnosticContext.addThreadMappedDiagnosticContextToMessageProperties(message);
            producer = session.createProducer(destination);
            producer.setDeliveryMode(jmsDeliveryMode);
            producer.setTimeToLive(timeToLiveInMillis);
            producer.send(message);
            LOGGER.info("Message with {} has been successfully sent.", message.getJMSMessageID());
            return message.getJMSMessageID();
        } catch (JMSException e) {
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
        MessageProducer producer = null;
        try {
            connection = JMSUtils.getConnectionWithRetry(RETRIES);
            session = JMSUtils.createSessionAndStartConnection(connection);
            LOGGER.debug("Sending message to EventBus...");
            TextMessage message = session.createTextMessage(text);
            message.setStringProperty(SERVICE_NAME, serviceName);
            message.setJMSReplyTo(replyToDestination);
            MappedDiagnosticContext.addThreadMappedDiagnosticContextToMessageProperties(message);
            producer = session.createProducer(destination);
            producer.send(message);
            return message.getJMSMessageID();
        } catch (JMSException e) {
            throw new MessageException("Error while trying to send EventBus Message..",e);
        } finally {
            JMSUtils.disconnectQueue(connection, session, producer);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendEventBusMessageWithSpecificIds(String text, String serviceName, Destination replyToDestination, String messageId, String messageCorrelationId) throws MessageException {
        return sendEventBusMessageWithSpecificIds(text, serviceName, replyToDestination, messageId, messageCorrelationId, 0, DeliveryMode.PERSISTENT);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendEventBusMessageWithSpecificIds(String text, String serviceName, Destination replyToDestination, String messageId, String messageCorrelationId, int timeToLive, int deliveryMode) throws MessageException {
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        try {
            connection = JMSUtils.getConnectionWithRetry(RETRIES);
            session = JMSUtils.createSessionAndStartConnection(connection);
            LOGGER.debug("Sending message to EventBus...");
            TextMessage message = session.createTextMessage(text);
            message.setStringProperty(SERVICE_NAME, serviceName);
            message.setJMSReplyTo(replyToDestination);
            if(StringUtils.isNotEmpty(messageId)){
                message.setJMSMessageID(messageId);
            }
            if(StringUtils.isNotEmpty(messageCorrelationId)){
                message.setJMSCorrelationID(messageCorrelationId);
            }
            MappedDiagnosticContext.addThreadMappedDiagnosticContextToMessageProperties(message);
            producer = session.createProducer(destination);
            producer.setTimeToLive(timeToLive);
            producer.setDeliveryMode(deliveryMode);
            producer.send(message);
            return message.getJMSMessageID();
        } catch (JMSException e) {
            throw new MessageException("Error while trying to send EventBus Message..",e);
        } finally {
            JMSUtils.disconnectQueue(connection, session, producer);
        }
    }

    private Destination getDestination() {
        if (destination == null && StringUtils.isNotEmpty(getDestinationName())) {
            destination = JMSUtils.lookupTopic(getDestinationName());
        }
        return destination;
    }

    public abstract String getDestinationName();
}
