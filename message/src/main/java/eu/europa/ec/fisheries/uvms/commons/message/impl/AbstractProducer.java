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
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageProducer;
import eu.europa.ec.fisheries.uvms.commons.message.context.MappedDiagnosticContext;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.*;
import javax.xml.bind.JAXBException;
import java.util.Map;

public abstract class AbstractProducer implements MessageProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractProducer.class);

    private Destination destination;

    @PostConstruct
    public void initializeDestination() {
        destination = getDestination();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendModuleMessage(final String text, final Destination replyTo) throws MessageException {
        return sendModuleMessageWithProps(text, replyTo, null, DeliveryMode.PERSISTENT, 0L);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendModuleMessageWithProps(final String text, final Destination replyTo, Map<String, String> props) throws MessageException {
        return sendModuleMessageWithProps(text, replyTo, props, DeliveryMode.PERSISTENT, 0L);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendModuleMessageNonPersistent(final String text, final Destination replyTo, final long timeToLiveInMillis) throws MessageException {
        return sendModuleMessageWithProps(text, replyTo, null, DeliveryMode.NON_PERSISTENT, timeToLiveInMillis);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendModuleMessageWithProps(final String text, final Destination replyTo, Map<String, String> props, final int jmsDeliveryMode,
                                             final long timeToLiveInMillis) throws MessageException {
        Connection connection = null;
        Session session = null;
        javax.jms.MessageProducer producer = null;
        try {
            connection = getConnection();
            session = JMSUtils.createSessionAndStartConnection(connection);
            LOGGER.debug("Sending message with replyTo: [{}]", replyTo);
            TextMessage message = session.createTextMessage();
            if (MapUtils.isNotEmpty(props)) {
                for (Object o : props.entrySet()) {
                    Map.Entry<String, String> entry = (Map.Entry) o;
                    message.setStringProperty(entry.getKey(), entry.getValue());
                }
            }
            MappedDiagnosticContext.addThreadMappedDiagnosticContextToMessageProperties(message);
            message.setJMSReplyTo(replyTo);
            message.setText(text);
            producer = session.createProducer(getDestination());
            producer.setDeliveryMode(jmsDeliveryMode);
            producer.setTimeToLive(timeToLiveInMillis);
            producer.send(message);
            LOGGER.debug("Message with {} has been successfully sent.", message.getJMSMessageID());
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
    public void sendResponseMessageToSender(final TextMessage message, final String text) throws MessageException {
        sendResponseMessageToSender(message, text, Message.DEFAULT_TIME_TO_LIVE);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendResponseMessageToSender(final TextMessage message, final String text, long timeToLive) throws MessageException {
        sendResponseMessageToSender(message, text, timeToLive, DeliveryMode.PERSISTENT);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendResponseMessageToSender(final TextMessage message, final String text, final String moduleName) throws MessageException {
        sendResponseMessageToSender(message, text, Message.DEFAULT_TIME_TO_LIVE);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendResponseMessageToSender(final TextMessage message, final String text, long timeToLive, int deliveryMode) throws MessageException {
        Connection connection = null;
        Session session = null;
        javax.jms.MessageProducer producer = null;
        try {
            connection = getConnection();
            session = JMSUtils.createSessionAndStartConnection(connection);
            LOGGER.debug("Sending message back to recipient from  with correlationId {} on queue: {}", message.getJMSMessageID(), message.getJMSReplyTo());
            TextMessage response = session.createTextMessage(text);
            response.setJMSCorrelationID(message.getJMSMessageID());
            MappedDiagnosticContext.addThreadMappedDiagnosticContextToMessageProperties(response);
            producer = session.createProducer(message.getJMSReplyTo());
            producer.setTimeToLive(timeToLive);
            producer.setDeliveryMode(deliveryMode);
            producer.send(response);
        } catch (final JMSException e) {
            LOGGER.error("[ Error when returning request. ] {} {}", e.getMessage(), e.getStackTrace());
            throw new MessageException("[ Error when sending response message. ]", e);
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
            session = JMSUtils.createSessionAndStartConnection(connection);
            LOGGER.debug("Sending message back to recipient from  with correlationId {} on queue: {}", message.getJMSMessageID(), message.getJMSReplyTo());
            final TextMessage response = session.createTextMessage();
            response.setText(text);
            MappedDiagnosticContext.addThreadMappedDiagnosticContextToMessageProperties(response);
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
    public String sendMessageWithSpecificIds(String messageToSend, Destination destination, Destination replyTo, String jmsMessageID, String jmsCorrelationID) throws MessageException {
        if(destination == null){
            throw new MessageException("Destination cannot be null!");
        }
        Connection connection = null;
        Session session = null;
        javax.jms.MessageProducer producer = null;
        String corrId;
        try {
            connection = getConnection();
            session = JMSUtils.createSessionAndStartConnection(connection);
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
            MappedDiagnosticContext.addThreadMappedDiagnosticContextToMessageProperties(message);
            producer.send(message);
            corrId = message.getJMSMessageID();
        } catch (JMSException e) {
            LOGGER.error("Error when returning request!", e);
            throw new MessageException("Error when returning request!", e);
        } finally {
            JMSUtils.disconnectQueue(connection, session, producer);
        }
        return corrId;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendMessageToSpecificQueue(String messageToSend, Destination destination, Destination replyTo) throws MessageException {
        return sendMessageToSpecificQueue(messageToSend, destination, replyTo, Message.DEFAULT_TIME_TO_LIVE);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendMessageToSpecificQueue(String messageToSend, Destination destination, Destination replyTo, long timeToLiveInMillis) throws MessageException {
        return sendMessageToSpecificQueue(messageToSend, destination, replyTo, timeToLiveInMillis, DeliveryMode.PERSISTENT);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendMessageToSpecificQueue(String messageToSend, Destination destination, Destination replyTo, long timeToLiveInMillis, int deliveryMode) throws MessageException {
        Connection connection = null;
        Session session = null;
        javax.jms.MessageProducer producer = null;
        try {
            connection = getConnection();
            session = JMSUtils.createSessionAndStartConnection(connection);
            producer = session.createProducer(destination);
            LOGGER.debug("Sending message on queue: {}", destination);
            final TextMessage message = session.createTextMessage(messageToSend);
            message.setJMSReplyTo(replyTo);
            producer.setTimeToLive(timeToLiveInMillis);
            producer.setDeliveryMode(deliveryMode);
            MappedDiagnosticContext.addThreadMappedDiagnosticContextToMessageProperties(message);
            producer.send(message);
            return message.getJMSMessageID();
        } catch (JMSException e) {
            throw new MessageException("[ Error when sending message. ]", e);
        } finally {
            JMSUtils.disconnectQueue(connection, session, producer);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendMessageToSpecificQueueWithFunction(String messageToSend, Destination destination, Destination replyTo, String function, String grouping) throws MessageException {
        Connection connection = null;
        Session session = null;
        javax.jms.MessageProducer producer = null;
        try {
            connection = getConnection();
            session = JMSUtils.createSessionAndStartConnection(connection);
            producer = session.createProducer(destination);
            LOGGER.debug("Sending message with correlationId {} on queue: {}", destination);
            final TextMessage message = session.createTextMessage(messageToSend);
            message.setJMSReplyTo(replyTo);
            message.setStringProperty(MessageConstants.JMS_FUNCTION_PROPERTY, function);
            message.setStringProperty(MessageConstants.JMS_MESSAGE_GROUP, grouping);
            producer.setTimeToLive(Message.DEFAULT_TIME_TO_LIVE);
            MappedDiagnosticContext.addThreadMappedDiagnosticContextToMessageProperties(message);
            producer.send(message);
            return message.getJMSMessageID();
        } catch (JMSException e) {
            throw new MessageException("[ Error when sending message. ]", e);
        } finally {
            JMSUtils.disconnectQueue(connection, session, producer);
        }
    }

    public Destination getDestination() {
        if (destination == null && StringUtils.isNotEmpty(getDestinationName())) {
            destination = JMSUtils.lookupQueue(getDestinationName());
        }
        return destination;
    }

    protected Connection getConnection() throws JMSException {
        return JMSUtils.CACHED_CONNECTION_FACTORY.createConnection();
    }
}
