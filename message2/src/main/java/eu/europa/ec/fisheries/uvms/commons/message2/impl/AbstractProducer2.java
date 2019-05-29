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

package eu.europa.ec.fisheries.uvms.commons.message2.impl;

import eu.europa.ec.fisheries.uvms.commons.message2.api.Fault;
import eu.europa.ec.fisheries.uvms.commons.message2.api.MessageConstants;
import eu.europa.ec.fisheries.uvms.commons.message2.api.MessageProducer2;
import eu.europa.ec.fisheries.uvms.commons.message2.context.MappedDiagnosticContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.jms.*;
import javax.xml.bind.JAXBException;
import java.util.Map;

public abstract class AbstractProducer2 implements MessageProducer2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractProducer2.class);

    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    public abstract Destination getDestination();

    public String sendModuleMessage(final String text, final Destination replyTo) throws JMSException {
        return sendModuleMessageWithProps(text, replyTo, null, DeliveryMode.PERSISTENT, 0L);
    }

    public String sendModuleMessageWithProps(final String text, final Destination replyTo, Map<String, String> props) throws JMSException {
        return sendModuleMessageWithProps(text, replyTo, props, DeliveryMode.PERSISTENT, 0L);
    }

    public String sendModuleMessageNonPersistent(final String text, final Destination replyTo, final long timeToLiveInMillis) throws JMSException {
        return sendModuleMessageWithProps(text, replyTo, null, DeliveryMode.NON_PERSISTENT, timeToLiveInMillis);
    }

    public String sendModuleMessageWithProps(final String text, final Destination replyTo, Map<String, String> props, final int jmsDeliveryMode, final long timeToLiveInMillis) throws JMSException {
        try (Connection connection = connectionFactory.createConnection();
             Session session = connection.createSession(false, 1);
             MessageProducer producer = session.createProducer(getDestination());
        ) {

            TextMessage message = session.createTextMessage();
            if (props != null && props.size() > 0) {
                for (Map.Entry<String, String> entry : props.entrySet()) {
                    message.setStringProperty(entry.getKey(), entry.getValue());
                }
            }
            MappedDiagnosticContext.addThreadMappedDiagnosticContextToMessageProperties(message);
            message.setJMSReplyTo(replyTo);
            message.setText(text);
            producer.setDeliveryMode(jmsDeliveryMode);
            producer.setTimeToLive(timeToLiveInMillis);
            producer.send(message);
            return message.getJMSMessageID();
        }
    }

    public void sendResponseMessageToSender(final TextMessage message, final String text) throws JMSException {
        sendResponseMessageToSender(message, text, Message.DEFAULT_TIME_TO_LIVE);
    }

    public void sendResponseMessageToSender(final TextMessage message, final String text, long timeToLive) throws JMSException {
        sendResponseMessageToSender(message, text, timeToLive, DeliveryMode.PERSISTENT);
    }

    public void sendResponseMessageToSender(final TextMessage message, final String text, final String moduleName) throws JMSException {
        sendResponseMessageToSender(message, text, Message.DEFAULT_TIME_TO_LIVE);
    }

    public void sendResponseMessageToSender(final TextMessage message, final String text, long timeToLive, int deliveryMode) throws JMSException {
        try (Connection connection = connectionFactory.createConnection();
             Session session = connection.createSession(false, 1);
             MessageProducer producer = session.createProducer(message.getJMSReplyTo());
        ) {
            TextMessage response = session.createTextMessage(text);
            response.setJMSCorrelationID(message.getJMSMessageID());
            MappedDiagnosticContext.addThreadMappedDiagnosticContextToMessageProperties(response);
            producer.setTimeToLive(timeToLive);
            producer.setDeliveryMode(deliveryMode);
            producer.send(response);
        }
    }

    public void sendFault(final TextMessage message, Fault fault) throws JMSException, JAXBException {
        try (Connection connection = connectionFactory.createConnection();
             Session session = connection.createSession(false, 1);
             MessageProducer producer = session.createProducer(message.getJMSReplyTo());
        ) {
            String text = JAXBUtils.marshallJaxBObjectToString(fault);
            final TextMessage response = session.createTextMessage();
            response.setText(text);
            MappedDiagnosticContext.addThreadMappedDiagnosticContextToMessageProperties(response);
            producer.send(message);
        }
    }

    public String sendMessageWithSpecificIds(String messageToSend, Destination destination, Destination replyTo, String jmsMessageID, String jmsCorrelationID) throws JMSException {
        if (destination == null) {
            throw new RuntimeException("Destination cannot be null!");
        }
        try (Connection connection = connectionFactory.createConnection();
             Session session = connection.createSession(false, 1);
             MessageProducer producer = session.createProducer(destination);
        ) {
            final TextMessage message = session.createTextMessage(messageToSend);
            if (jmsMessageID != null && jmsMessageID.length() > 0) {
                message.setJMSMessageID(jmsMessageID);
            }
            if (jmsCorrelationID != null && jmsCorrelationID.length() > 0) {
                message.setJMSCorrelationID(jmsCorrelationID);
            }
            message.setJMSReplyTo(replyTo);
            MappedDiagnosticContext.addThreadMappedDiagnosticContextToMessageProperties(message);
            producer.send(message);
            return message.getJMSMessageID();
        }
    }

    public String sendMessageToSpecificQueue(String messageToSend, Destination destination, Destination replyTo) throws JMSException {
        return sendMessageToSpecificQueue(messageToSend, destination, replyTo, Message.DEFAULT_TIME_TO_LIVE);
    }

    public String sendMessageToSpecificQueue(String messageToSend, Destination destination, Destination replyTo, long timeToLiveInMillis) throws JMSException {
        return sendMessageToSpecificQueue(messageToSend, destination, replyTo, timeToLiveInMillis, DeliveryMode.PERSISTENT);
    }


    public String sendMessageToSpecificQueue(String messageToSend, Destination destination, Destination replyTo, long timeToLiveInMillis, int deliveryMode) throws JMSException {

        try (Connection connection = connectionFactory.createConnection();
             Session session = connection.createSession(false, 1);
             MessageProducer producer = session.createProducer(destination);
        ) {
            final TextMessage message = session.createTextMessage(messageToSend);
            message.setJMSReplyTo(replyTo);
            producer.setTimeToLive(timeToLiveInMillis);
            producer.setDeliveryMode(deliveryMode);
            MappedDiagnosticContext.addThreadMappedDiagnosticContextToMessageProperties(message);
            producer.send(message);
            return message.getJMSMessageID();
        }
    }

    public String sendMessageToSpecificQueueWithFunction(String messageToSend, Destination destination, Destination replyTo, String function, String grouping) throws JMSException {

        try (Connection connection = connectionFactory.createConnection();
             Session session = connection.createSession(false, 1);
             MessageProducer producer = session.createProducer(destination);
        ) {
            TextMessage message = session.createTextMessage(messageToSend);
            message.setJMSReplyTo(replyTo);
            message.setStringProperty(MessageConstants.JMS_FUNCTION_PROPERTY, function);
            message.setStringProperty(MessageConstants.JMS_MESSAGE_GROUP, grouping);
            producer.setTimeToLive(Message.DEFAULT_TIME_TO_LIVE);
            MappedDiagnosticContext.addThreadMappedDiagnosticContextToMessageProperties(message);
            producer.send(message);
            return message.getJMSMessageID();
        }
    }
}
