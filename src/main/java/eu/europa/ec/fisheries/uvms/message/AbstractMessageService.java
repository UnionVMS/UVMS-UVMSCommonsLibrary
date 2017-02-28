/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

public abstract class AbstractMessageService implements MessageService {

    final static Logger LOG = LoggerFactory.getLogger(AbstractMessageService.class);

    private Connection connection = null;
    private Session session = null;

    private static final long MILLISECONDS = 60000;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendModuleMessage(String text) throws MessageException {

        try {

            connectToQueue();
            TextMessage message = session.createTextMessage();
            message.setJMSReplyTo(getResponseDestination());
            message.setText(text);
            session.createProducer(getEventDestination()).send(message);
            return message.getJMSMessageID();

        } catch (JMSException e) {
            LOG.error("[ Error when sending message. ] {}", e.getMessage());
            throw new MessageException("[ Error when sending message. ]", e);
        } finally {
            disconnectQueue();
        }
    }

    protected abstract ConnectionFactory getConnectionFactory();

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendModuleResponseMessage(TextMessage message, String text) {
        try {
            LOG.info("Sending message back to recipient from" + getModuleName() + " with correlationId {} on queue: {}", message.getJMSMessageID(),
                    message.getJMSReplyTo());
            connectToQueue();
            TextMessage response = session.createTextMessage(text);
            response.setJMSCorrelationID(message.getJMSMessageID());
            session.createProducer(message.getJMSReplyTo()).send(response);
        } catch (JMSException e) {
            LOG.error("[ Error when returning" + getModuleName() + "request. ] {} {}", e.getMessage(), e.getStackTrace());
        } finally {
            disconnectQueue();
        }
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @SuppressWarnings(value = "unchecked")
    public <T> T getMessage(final String correlationId, final Class type) throws MessageException {
        try {

            if (correlationId == null || correlationId.isEmpty()) {
                throw new MessageException("No CorrelationID provided!");
            }

            connectToQueue();

            return (T) session.createConsumer(getResponseDestination(), "JMSCorrelationID='" + correlationId + "'").receive(getMilliseconds());

        } catch (Exception e) {
            LOG.error("[ Error when retrieving message. ] {}", e.getMessage());
            throw new MessageException("Error when retrieving message: " + e.getMessage());
        } finally {
            disconnectQueue();
        }
    }


    protected Session connectToQueue() throws JMSException {
        connection = getConnectionFactory().createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        connection.start();
        return session;
    }

    protected void disconnectQueue() {
        try {
            if (connection != null) {
                connection.stop();
                connection.close();
            }
        } catch (JMSException e) {
            LOG.error("[ Error when stopping or closing JMS queue. ] {} {}", e.getMessage(), e.getStackTrace());
        }
    }

    protected abstract Destination getEventDestination();

    protected abstract Destination getResponseDestination();

    protected abstract String getModuleName();

    protected long getMilliseconds() {
        return MILLISECONDS;
    }
}
