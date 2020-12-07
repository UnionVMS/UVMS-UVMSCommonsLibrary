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
import eu.europa.ec.fisheries.uvms.commons.message.context.FluxEnvelopeHolder;
import eu.europa.ec.fisheries.uvms.commons.message.context.JmsFluxEnvelopeHelper;
import eu.europa.ec.fisheries.uvms.commons.message.context.MappedDiagnosticContext;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.jms.*;
import javax.xml.bind.JAXBException;
import java.util.Map;

public abstract class AbstractProducer implements MessageProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractProducer.class);

    private static final String ERROR_WHEN_SENDING_MESSAGE = "Error when sending message. ";

    @Inject
    private FluxEnvelopeHolder fluxEnvelopeHolder;

    @Inject
    private JmsFluxEnvelopeHelper fluxEnvelopeHelper;

    private volatile Destination destination;

    private volatile Connection connection;

    private volatile Session session;

    private volatile javax.jms.MessageProducer producer;

    private static final int RETRIES = 100;

    private static final long TIME_TO_LIVE_FOR_NON_PERSISTENT_MESSAGES = 1800000; // 30 MINUTES

    private static final String FAILED_AFTER_RETRY = "[FAILED-TO-SEND] After +" + RETRIES + "+ of retries still failed sending message!";

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendModuleMessage(final String text, final Destination replyTo) throws MessageException {
        return sendModuleMessageWithProps(text, replyTo, null, DeliveryMode.PERSISTENT, 0L);
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendModuleMessageInGroup(final String text, final Destination replyTo, final String group) throws MessageException {
        return sendModuleMessageInGroup(text, replyTo, null, DeliveryMode.PERSISTENT, 0L, group);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendModuleMessageInGroup(final String text, final Destination replyTo, Map<String, String> props, final int jmsDeliveryMode, final long timeToLiveInMillis, String group) throws MessageException {
        return sendMessageWithRetry(text, null, replyTo, props, jmsDeliveryMode, timeToLiveInMillis, null, null, group, RETRIES);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendModuleMessageWithProps(final String text, final Destination replyTo, Map<String, String> props) throws MessageException {
        return sendModuleMessageWithProps(text, replyTo, props, DeliveryMode.PERSISTENT, 0L);
    }

    @Override
    public void sendModuleMessageWithPropsSameTx(final String text, Map<String, String> props) throws MessageException {
        sendMessageWithRetry(text, null, null, props, DeliveryMode.PERSISTENT, 0L, null, null,null, RETRIES);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendModuleMessageNonPersistent(final String text, final Destination replyTo, final long timeToLiveInMillis) throws MessageException {
        return sendModuleMessageWithProps(text, replyTo, null, DeliveryMode.NON_PERSISTENT, timeToLiveInMillis);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendModuleMessageWithProps(final String text, final Destination replyTo, Map<String, String> props, final int jmsDeliveryMode, final long timeToLiveInMillis) throws MessageException {
        return sendMessageWithRetry(text, null, replyTo, props, jmsDeliveryMode, timeToLiveInMillis, null, null,null, RETRIES);
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
        Destination jmsReplyTo;
        String messageId;
        try {
            jmsReplyTo = message.getJMSReplyTo();
            messageId = message.getJMSMessageID();
        } catch (JMSException e) {
           throw new MessageException("Could Not get the sender destination to send the response to!!", e);
        }
        sendMessageWithRetry(text, jmsReplyTo, null, null,deliveryMode, timeToLive, messageId, null, null, RETRIES);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendFault(final TextMessage message, Fault fault) throws MessageException {
        String faultMsgText;
        Destination jmsReplyTo;
        try {
            faultMsgText = JAXBUtils.marshallJaxBObjectToString(fault);
            jmsReplyTo = message.getJMSReplyTo();
        } catch (JAXBException | JMSException e) {
            throw new MessageException("Error while marshalling Fault message or getting JMSReplyTo!",e);
        }
        sendMessageWithRetry(faultMsgText, jmsReplyTo, null, null, DeliveryMode.NON_PERSISTENT, TIME_TO_LIVE_FOR_NON_PERSISTENT_MESSAGES, null, null, null, RETRIES);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendMessageWithSpecificIds(String messageToSend, Destination destination, Destination replyTo, String jmsMessageID, String jmsCorrelationID) throws MessageException {
        return sendMessageWithSpecificIdsWithRetry(messageToSend, destination, replyTo, jmsMessageID, jmsCorrelationID, RETRIES);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendMessageToSpecificQueue(String messageToSend, Destination destination, Destination replyTo) throws MessageException {
        return sendMessageToSpecificQueue(messageToSend, destination, replyTo, Message.DEFAULT_TIME_TO_LIVE);
    }

    @Override
    public String sendMessageToSpecificQueueSameTx(String messageToSend, Destination destination, Destination replyTo) throws MessageException {
        return sendMessageToSpecificQueue(messageToSend, destination, replyTo, Message.DEFAULT_TIME_TO_LIVE);
    }

    @Override
    public String sendMessageToSpecificQueueSameTx(String messageToSend, Destination destination, Destination replyTo, Map<String, String> props) throws MessageException {
        return sendMessageWithRetry(messageToSend, destination, replyTo, props, DeliveryMode.PERSISTENT, TIME_TO_LIVE_FOR_NON_PERSISTENT_MESSAGES, null, null, null, RETRIES);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendMessageToSpecificQueue(String messageToSend, Destination destination, Destination replyTo, Map<String, String> props) throws MessageException {
        return sendMessageWithRetry(messageToSend, destination, replyTo, props, DeliveryMode.PERSISTENT, TIME_TO_LIVE_FOR_NON_PERSISTENT_MESSAGES, null, null, null, RETRIES);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendMessageToSpecificQueue(String messageToSend, Destination destination, Destination replyTo, long timeToLiveInMillis) throws MessageException {
        return sendMessageToSpecificQueue(messageToSend, destination, replyTo, timeToLiveInMillis, DeliveryMode.PERSISTENT);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendMessageToSpecificQueue(String messageToSend, Destination destination, Destination replyTo, long timeToLiveInMillis, int deliveryMode) throws MessageException {
        return sendMessageWithRetry(messageToSend, destination, replyTo, null, deliveryMode, TIME_TO_LIVE_FOR_NON_PERSISTENT_MESSAGES, null, null, null, RETRIES);
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendMessageToSpecificQueueWithFunction(String messageToSend, Destination destination, Destination replyTo, String function, String grouping) throws MessageException {
        return sendMessageWithRetry(messageToSend, destination, replyTo, null, DeliveryMode.NON_PERSISTENT, TIME_TO_LIVE_FOR_NON_PERSISTENT_MESSAGES, null, function, grouping, RETRIES);
    }

    private String sendMessageWithRetry(String messageToSend, Destination destination, Destination replyTo, Map<String, String> props, int jmsDeliveryMode, long timeToLiveInMillis,
                                        String jmsCorrIdForResponseMessage, String function, String grouping, int retries) throws MessageException {
        try {
            initializeConnectionAndDestination(destination != null ? destination : getDestination());
            TextMessage message = session.createTextMessage();
            if (MapUtils.isNotEmpty(props)) {
                for (Map.Entry<String, String> entry : props.entrySet()) {
                    message.setStringProperty(entry.getKey(), entry.getValue());
                }
            }
            if(StringUtils.isNotEmpty(function)){
                message.setStringProperty(MessageConstants.JMS_FUNCTION_PROPERTY, function);
            }
            if(StringUtils.isNotEmpty(grouping)){
                message.setStringProperty(MessageConstants.JMS_MESSAGE_GROUP, grouping);
            }
            fluxEnvelopeHelper.setHeaders(fluxEnvelopeHolder.get(), message);
            if(StringUtils.isNotEmpty(jmsCorrIdForResponseMessage)){
                message.setJMSCorrelationID(jmsCorrIdForResponseMessage);
            }
            MappedDiagnosticContext.addThreadMappedDiagnosticContextToMessageProperties(message);
            message.setJMSReplyTo(replyTo);
            message.setText(messageToSend);
            producer.setDeliveryMode(jmsDeliveryMode);
            producer.setTimeToLive(timeToLiveInMillis);
            producer.send(message);
            LOGGER.debug("Message with {} has been successfully sent.", message.getJMSMessageID());
            return message.getJMSMessageID();
        } catch (final JMSException e) {
            closeResources();
            if (retries > 0) {
                LOGGER.warn("Couldn't send message.. Going to retry for the [-" + (RETRIES - retries) + "-] time now [After sleeping for 1.5 Seconds]..");
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException ignored1) {
                    LOGGER.warn("Couldn't send message thread woke");
                }
                int newRetries = retries - 1;
                return sendMessageWithRetry(messageToSend, destination, replyTo, props, jmsDeliveryMode, timeToLiveInMillis, jmsCorrIdForResponseMessage, function, grouping, newRetries);
            } else {
                throw new MessageException(ERROR_WHEN_SENDING_MESSAGE + FAILED_AFTER_RETRY, e);
            }
        } finally {
            closeResources();
        }
    }

    private String sendMessageWithSpecificIdsWithRetry(String messageToSend, Destination destination, Destination replyTo, String jmsMessageID, String jmsCorrelationID, int retries) throws MessageException {
        try {
            initializeConnectionAndDestination(destination);
            final TextMessage message = session.createTextMessage(messageToSend);
            fluxEnvelopeHelper.setHeaders(fluxEnvelopeHolder.get(), message);
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
            return message.getJMSMessageID();
        } catch (JMSException e) {
            closeResources();
            if (retries > 0) {
                LOGGER.warn("Couldn't return response message.. Going to retry for the [-" + (RETRIES - retries) + "-] time now [After sleeping for 1.5 Seconds]..");
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException ignored1) {
                }
                int newRetries = retries - 1;
                return sendMessageWithSpecificIdsWithRetry(messageToSend, destination, replyTo, jmsMessageID, jmsCorrelationID, newRetries);
            } else {
                throw new MessageException(FAILED_AFTER_RETRY + " Error send message with specific IDs!", e);
            }
        } finally {
            closeResources();
        }
    }


    private void initializeConnectionAndDestination(Destination destin) {
        try {
            connection = JMSUtils.getConnectionV2();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            producer = session.createProducer(destin);
        } catch (JMSException e) {
            LOGGER.error("[INIT-ERROR] JMS Connection could not be estabelished!",e);
        }
    }

    @PreDestroy
    public void preDistroy(){
        LOGGER.info("[DESTROYING-PRODUCER] Predestroy producer...");
        closeResources();
    }


    private void closeResources() {
        try {
            if (producer != null) {
                producer.close();
                producer = null;
            }
            if (session != null) {
                session.close();
                session = null;
            }
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } catch (JMSException e) {
            LOGGER.error("[CLOSE-ERROR] JMS Connection could not be closed! + " + e.getMessage(), e);
        }
    }

    public Destination getDestination() {
        if (destination == null && StringUtils.isNotEmpty(getDestinationName())) {
            destination = JMSUtils.lookupQueue(getDestinationName());
        }
        return destination;
    }

}
