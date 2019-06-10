/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.uvms.commons.message.impl;

import eu.europa.ec.fisheries.uvms.commons.message.context.MappedDiagnosticContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.jms.*;

public abstract class AbstractTopicProducer {

    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    private static final String SERVICE_NAME = "ServiceName";

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTopicProducer.class);



    public String sendEventBusMessage(String text, String serviceName, int jmsDeliveryMode, long timeToLiveInMillis) throws JMSException {
        try (
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(getDestination());
        ){
            TextMessage message = session.createTextMessage(text);
            message.setStringProperty(SERVICE_NAME, serviceName);
            MappedDiagnosticContext.addThreadMappedDiagnosticContextToMessageProperties(message);
            producer.setDeliveryMode(jmsDeliveryMode);
            producer.setTimeToLive(timeToLiveInMillis);
            producer.send(message);
            return message.getJMSMessageID();
        } catch (JMSException e) {
            LOGGER.error("[ Error when sending message. ] {}", e.getMessage());
            throw  e;
        }
    }

    public String sendEventBusMessage(String text, String serviceName) throws JMSException {
        return sendEventBusMessage(text, serviceName,DeliveryMode.PERSISTENT, 0L);
    }

    public String sendEventBusMessage(String text, String serviceName, Destination replyToDestination)  throws JMSException {
        try (
                Connection connection = connectionFactory.createConnection();
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                MessageProducer producer = session.createProducer(getDestination());
        )
        {
            TextMessage message = session.createTextMessage(text);
            message.setStringProperty(SERVICE_NAME, serviceName);
            message.setJMSReplyTo(replyToDestination);
            MappedDiagnosticContext.addThreadMappedDiagnosticContextToMessageProperties(message);
            producer.send(message);
            return message.getJMSMessageID();
        }
    }


    public String sendEventBusMessageWithSpecificIds(String text, String serviceName, Destination replyToDestination, String messageId, String messageCorrelationId) throws JMSException {
        return sendEventBusMessageWithSpecificIds(text, serviceName, replyToDestination, messageId, messageCorrelationId, 0, DeliveryMode.PERSISTENT);
    }

    public String sendEventBusMessageWithSpecificIds(String text, String serviceName, Destination replyToDestination, String messageId, String messageCorrelationId, int timeToLive, int deliveryMode) throws JMSException {
        try (
                Connection connection = connectionFactory.createConnection();
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                MessageProducer producer = session.createProducer(getDestination());
        ){
            TextMessage message = session.createTextMessage(text);
            message.setStringProperty(SERVICE_NAME, serviceName);
            message.setJMSReplyTo(replyToDestination);

            if (messageId != null && messageId.length() > 0) {
                message.setJMSMessageID(messageId);
            }
            if (messageCorrelationId != null && messageCorrelationId.length() > 0) {
                message.setJMSCorrelationID(messageCorrelationId);
            }
            MappedDiagnosticContext.addThreadMappedDiagnosticContextToMessageProperties(message);
            producer.setTimeToLive(timeToLive);
            producer.setDeliveryMode(deliveryMode);
            producer.send(message);
            return message.getJMSMessageID();
        }
    }




    public abstract Destination getDestination();
}
