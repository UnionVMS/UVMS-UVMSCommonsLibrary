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
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.*;
import java.util.Map;

public abstract class AbstractProducer implements MessageProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractProducer.class);

    private ConnectionFactory connectionFactory;

    private Destination destination;

    @PostConstruct
    public void initializeConnectionFactory() {
        connectionFactory = JMSUtils.lookupConnectionFactory();
        destination = JMSUtils.lookupQueue(getDestinationName());
    }

    protected final ConnectionFactory getConnectionFactory() {
        if (connectionFactory == null) {
            connectionFactory = JMSUtils.lookupConnectionFactory();
        }
        return connectionFactory;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendModuleMessageWithProps(final String text, final Destination replyTo, Map<String, String> props, final int jmsDeliveryMode, final long timeToLiveInMillis) throws MessageException {

        try (Connection connection = getConnectionFactory().createConnection();
             Session session = JMSUtils.connectToQueue(connection);
             javax.jms.MessageProducer producer = session.createProducer(getDestination())) {

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

            producer.setDeliveryMode(jmsDeliveryMode);
            producer.setTimeToLive(timeToLiveInMillis);
            producer.send(message);
            LOGGER.info("Message with {} has been successfully sent.", message.getJMSMessageID());
            return message.getJMSMessageID();

        } catch (final Exception e) {
            LOGGER.error("[ Error when sending message. ] {}", e.getMessage());
            throw new MessageException("[ Error when sending message. ]", e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendModuleMessageWithProps(final String text, final Destination replyTo, Map<String, String> props) throws MessageException {
        return sendModuleMessageWithProps(text, replyTo, props, DeliveryMode.PERSISTENT, 0L);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendModuleMessage(final String text, final Destination replyTo) throws MessageException {
        return sendModuleMessageWithProps(text, replyTo, null, DeliveryMode.PERSISTENT, 0L);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendModuleMessageNonPersistent(final String text, final Destination replyTo, final long timeToLiveInMillis) throws MessageException {
        return sendModuleMessageWithProps(text, replyTo, null, DeliveryMode.NON_PERSISTENT, timeToLiveInMillis);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendModuleResponseMessage(final TextMessage message, final String text, final String moduleName) {
        try (Connection connection = getConnectionFactory().createConnection();
             Session session = JMSUtils.connectToQueue(connection);
             javax.jms.MessageProducer producer = session.createProducer(message.getJMSReplyTo())) {

            LOGGER.info("Sending message back to recipient from " + moduleName + " with correlationId {} on queue: {}",
                    message.getJMSMessageID(), message.getJMSReplyTo());

            final TextMessage response = session.createTextMessage(text);
            response.setJMSCorrelationID(message.getJMSMessageID());
            producer.send(response);
        } catch (final Exception e) {
            LOGGER.error("[ Error when returning" + moduleName + "request. ] {} {}", e.getMessage(), e.getStackTrace());
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendModuleResponseMessage(final TextMessage message, final String text) {
        sendModuleResponseMessage(message, text, StringUtils.EMPTY);
    }

    protected final Destination getDestination() {
        if (destination == null) {
            destination = JMSUtils.lookupQueue(getDestinationName());
        }
        return destination;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendFault(final TextMessage message, Fault fault) {
        try (Connection connection = getConnectionFactory().createConnection();
             Session session = JMSUtils.connectToQueue(connection);
             javax.jms.MessageProducer producer = session.createProducer(message.getJMSReplyTo())) {

            String text = JAXBUtils.marshallJaxBObjectToString(fault);

            LOGGER.debug(
                    "Sending message back to recipient from  with correlationId {} on queue: {}",
                    message.getJMSMessageID(), message.getJMSReplyTo());

            final TextMessage response = session.createTextMessage();
            response.setText(text);
            producer.send(response);
        } catch (Exception e) {
            LOGGER.error("[ Error when returning request. ] {} {}", e.getMessage(),
                    e.getStackTrace());
        }
    }
}
