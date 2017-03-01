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

import javax.annotation.PostConstruct;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public abstract class AbstractConsumer implements MessageConsumer {

    protected final static Logger LOG = LoggerFactory.getLogger( MessageConsumer.class);

    private ConnectionFactory connectionFactory;
    private Destination destination;

    private Connection connection = null;
    private Session session = null;

    private static long MILLISECONDS = 600000;

	
	@PostConstruct
    private void connectConnectionFactory() {
        LOG.debug("Open connection to JMS broker");
        InitialContext ctx;
        try {
            ctx = new InitialContext();
        } catch (Exception e) {
            LOG.error("Failed to get InitialContext",e);
            throw new RuntimeException(e);
        }
        try {
            connectionFactory = (QueueConnectionFactory) ctx.lookup(MessageConstants.CONNECTION_FACTORY);
        } catch (NamingException ne) {
            //if we did not find the connection factory we might need to add java:/ at the start
            LOG.debug("Connection Factory lookup failed for " + MessageConstants.CONNECTION_FACTORY);
            String wfName = "java:/" + MessageConstants.CONNECTION_FACTORY;
            try {
                LOG.debug("trying " + wfName);
                connectionFactory = (QueueConnectionFactory) ctx.lookup(wfName);
            } catch (Exception e) {
                LOG.error("Connection Factory lookup failed for both " + MessageConstants.CONNECTION_FACTORY  + " and " + wfName);
                throw new RuntimeException(e);
            }
        }
		
		destination = JMSUtils.lookupQueue(ctx, getDestinationName());

    }
	
	
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @SuppressWarnings(value = "unchecked")
    public <T> T getMessage(final String correlationId, final Class type, final Long timeoutInMillis) throws MessageException {
        try {
            LOG.debug("Trying to receive message with correlationId:[{}], class type:[{}], timeout: {}", correlationId, type, timeoutInMillis);
            if (correlationId == null || correlationId.isEmpty()) {
                throw new MessageException("No CorrelationID provided!");
            }

            connectToQueue();

            T recievedMessage = (T) session.createConsumer(getDestination(), "JMSCorrelationID='" + correlationId + "'").receive(timeoutInMillis);

            if (recievedMessage == null) {
                throw new MessageException("Message either null or timeout occured. Timeout was set to: " + timeoutInMillis);
            } else {
                LOG.debug("JMS message received: {} \n Content: {}", recievedMessage, ((TextMessage)recievedMessage).getText());
            }

            return recievedMessage;

        } catch (Exception e) {
            LOG.error("[ Error when retrieving message. ] {}", e.getMessage());
            throw new MessageException("Error when retrieving message: " + e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.stop();
                    connection.close();
                }
            } catch (JMSException e) {
                LOG.error("[ Error when stopping or closing JMS queue. ] {} {}", e.getMessage(), e.getStackTrace());
            }
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @SuppressWarnings(value = "unchecked")
    public <T> T getMessage(final String correlationId, final Class type) throws MessageException {
        return getMessage(correlationId, type, getMilliseconds());
    }

    private void connectToQueue() throws JMSException {
        connection = getConnectionFactory().createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        connection.start();
        LOG.debug("Connected to {}", getDestination());
    }

    protected ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    protected long getMilliseconds() {
        return MILLISECONDS;
    }
	
	public Destination getDestination() {
		return destination;
	}
}
