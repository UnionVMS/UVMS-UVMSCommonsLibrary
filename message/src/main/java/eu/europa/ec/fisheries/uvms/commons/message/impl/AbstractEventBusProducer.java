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

import eu.europa.ec.fisheries.uvms.commons.message.api.EventBusProducer;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.*;
import java.util.Map;

public abstract class AbstractEventBusProducer implements EventBusProducer {

    private ConnectionFactory connectionFactory;

    private Destination destination;

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractProducer.class);

    @PostConstruct
    public void initializeConnectionFactory() {
        connectionFactory = JMSUtils.lookupConnectionFactory();
        destination = JMSUtils.lookupTopic(getDestinationName());
    }

    protected final ConnectionFactory getConnectionFactory() {
        if (connectionFactory == null) {
            connectionFactory = JMSUtils.lookupConnectionFactory();
        }
        return connectionFactory;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendEventBusMessage(final String text, final Map<String, String> props, final int jmsDeliveryMode, final long timeToLiveInMillis) throws MessageException {

        try (Connection connection = getConnectionFactory().createConnection();
             Session session = JMSUtils.connectToQueue(connection);
             javax.jms.MessageProducer producer = session.createProducer(getDestination())) {

            TextMessage message = session.createTextMessage();
            message.setText(text);

            if (MapUtils.isNotEmpty(props)) {
                for (Object o : props.entrySet()) {
                    Map.Entry<String, String> entry = (Map.Entry) o;
                    message.setStringProperty(entry.getKey(), entry.getValue());
                }
            }

            producer.setDeliveryMode(jmsDeliveryMode);
            producer.setTimeToLive(timeToLiveInMillis);
            producer.send(message);
            LOGGER.info("Message with {} has been successfully sent.", message.getJMSMessageID());
            return message.getJMSMessageID();

        } catch (Exception e) {
            LOGGER.error("[ Error when sending message. ] {}", e.getMessage());
            throw new MessageException("[ Error when sending message. ]", e);
        }
    }

    protected final Destination getDestination() {
        if (destination == null) {
            destination = JMSUtils.lookupTopic(getDestinationName());
        }
        return destination;
    }
}
