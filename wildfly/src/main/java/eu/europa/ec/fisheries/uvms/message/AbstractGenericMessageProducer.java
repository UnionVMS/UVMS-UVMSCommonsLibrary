/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.uvms.message;

import eu.europa.ec.fisheries.uvms.exception.JmsMessageException;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.jms.*;


/**
 * Created by kovian on 02/12/2016.
 */
@Slf4j
public class AbstractGenericMessageProducer {

    @Resource(lookup = MessageConstants.CONNECTION_FACTORY)
    private ConnectionFactory connectionFactory;

    private Connection connection;

    private Session session;

    /**
     * Send a "fire and forget" message to a recipient
     *
     * @param toQueue        The destinsation of the response
     * @param textMessag     The actual message as a String representation of an XML
     * @param deliveryMode   The delivery mode to use
     * @param defultPriority The priority for this message
     * @param timeToLive     The message's lifetime (in milliseconds)
     * @return
     * @throws JmsMessageException
     */
    protected String sendMessage(Destination toQueue, Destination responseQueue, String textMessag, int deliveryMode, int defultPriority, long timeToLive) throws JmsMessageException {
        return sendMessage(toQueue, responseQueue, textMessag, null, deliveryMode, defultPriority, timeToLive);
    }

    /**
     * Sends a message to the toQueue;
     *
     * @param toQueue
     * @param textMessag
     * @return
     * @throws JmsMessageException
     */
    protected String sendMessage(Destination toQueue, String textMessag) throws JmsMessageException {
        return sendMessage(toQueue, null, textMessag, null, null, null, 6000L);
    }

    /**
     * Sends a response message to a reciever. The corralationId is the
     * JMSMessage id provided in the message this metod responds to.
     *
     * @param responseQueue The destinsation of the response
     * @param textMessage   The actual message as a String representation of an
     *                      XML
     * @param correlationId The correlationId to set on the message that is
     *                      returned
     * @return The JMSMessage id of the sent message
     * @throws JmsMessageException
     */
    protected String sendMessage(Destination responseQueue, String textMessage, String correlationId) throws JmsMessageException {
        return sendMessage(responseQueue, null, textMessage, correlationId, null, null, null);
    }

    /**
     * Sends a JS message to a recipient and sets teh expected reponse queue
     *
     * @param toQueue     The destinsation of the message
     * @param replyQueue  The destination that shis message should respond to
     *                    when arriving at the toQueue
     * @param textMessage The actual message as a String representation of an
     *                    XML
     * @return The JMSMessage id of the sent message
     * @throws JmsMessageException
     */
    protected String sendMessage(Destination toQueue, Destination replyQueue, String textMessage) throws JmsMessageException {
        return sendMessage(toQueue, replyQueue, textMessage, null, null, null, null);
    }

    /**
     * Sends a message to a JMS destination
     *
     * @param toQueue         The destinsation of the message
     * @param replyQueue      The destination that shis message should respond to
     *                        when arriving at the toQueue
     * @param textMessage     The actual message as a String representation of an
     *                        XML
     * @param correlationId   The correlationId to set on the message that is
     *                        returned
     * @param deliveryMode    The delivery mode to use
     * @param defaultPriority The priority for this message
     * @param timetoLive      The message's lifetime (in milliseconds)
     * @return The JMSMessage id of the sent message
     * @throws JmsMessageException
     */
    private String sendMessage(Destination toQueue, Destination replyQueue, String textMessage, String correlationId, Integer deliveryMode, Integer defaultPriority, Long timetoLive) throws JmsMessageException {

        try {
            log.info("[ Sending message to recipient on queue ] {}", toQueue);
            session = getSession();
            TextMessage message = session.createTextMessage();
            message.setText(textMessage);
            message.setJMSReplyTo(replyQueue);
            message.setJMSDestination(toQueue);
            message.setJMSCorrelationID(correlationId);
            if (deliveryMode != null && defaultPriority != null && timetoLive != null) {
                getProducer(session, toQueue).send(message, deliveryMode, defaultPriority, timetoLive);
            } else {
                getProducer(session, toQueue).send(message);
            }
            disconnectQueue();
            return message.getJMSMessageID();
        } catch (JMSException ex) {
            throw new JmsMessageException("Error when sending message or closing JMS queue", ex);
        } finally {
            disconnectQueue();
        }
    }


    /**
     * Creates a new JMS Session and returns it;
     *
     * @return Session
     * @throws JMSException
     */
    private Session getSession() throws JMSException {
        if (connection == null) {
            log.debug("Open connection to JMS broker");
            try {
                connection = connectionFactory.createConnection();
                connection.start();
                return connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            } catch (JMSException | NullPointerException ex) {
                log.error("Error when opening connection to JMS broker", ex);
                throw new JMSException(ex.getMessage());
            }
        }
        return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }


    private javax.jms.MessageProducer getProducer(Session session, Destination destination) throws JMSException {
        javax.jms.MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        producer.setTimeToLive(60000L);
        return producer;
    }

    /**
     * Disconnects from the actual connection if it is still active;
     */
    protected void disconnectQueue() {
        try {
            if(session != null){
                session.close();
            }
            if (connection != null) {
                connection.stop();
                connection.close();
            }
            log.debug("Succesfully disconnected from FLUX BRIDGE Remote queue.");
        } catch (JMSException e) {
            log.error("[ Error when stopping or closing JMS queue ] {}", e);
        }
    }
}
