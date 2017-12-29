/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.commons.message.impl;

import eu.europa.ec.fisheries.uvms.commons.message.api.Producer;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SimpleAbstractProducer implements Producer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleAbstractProducer.class);

    @Override
    public void sendMessage(Queue destination, Queue replyTo, String messageToSend) {
        sendMessage(null, null, destination, replyTo, messageToSend);
    }

    @Override
    public void sendMessage(String messageID, String msgCoorelationId, Queue destination, Queue replyTo, String messageToSend) {

        try (Connection connection = getConnectionFactory().createConnection();
             Session session = JMSUtils.connectToQueue(connection);
             MessageProducer producer = session.createProducer(destination)) {
            LOGGER.debug("Sending message with correlationId {} on queue: {}", messageID, destination);
            final TextMessage message = session.createTextMessage();
            if (StringUtils.isNotEmpty(messageID)) {
                message.setJMSMessageID(messageID);
            }
            if (StringUtils.isNotEmpty(msgCoorelationId)) {
                message.setJMSCorrelationID(msgCoorelationId);
            }
            message.setJMSReplyTo(replyTo);
            message.setText(messageToSend);
            producer.send(message);

        } catch (Exception e) {
            LOGGER.error("[ Error when returning request. ] {} {}", e.getMessage(), e.getStackTrace());
        }
    }

    protected abstract ConnectionFactory getConnectionFactory();
}
