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
    public void sendEventBusMessage(String text, String serviceName) throws MessageException {
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
            producer.send(message);
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