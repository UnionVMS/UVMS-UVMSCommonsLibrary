/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.message;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.*;

@Slf4j
public abstract class AbstractConsumer implements MessageConsumer {

    @Resource(lookup = MessageConstants.CONNECTION_FACTORY)
    private ConnectionFactory connectionFactory;

    private Connection connection = null;
    private Session session = null;

    private static long MILLISECONDS = 600000;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @SuppressWarnings(value = "unchecked")
    public <T> T getMessage(final String correlationId, final Class type, final Long timeoutInMillis) throws MessageException {
        try {
            log.debug("Trying to receive message with correlationId:[{}], class type:[{}], timeout: {}", correlationId, type, timeoutInMillis);
            if (correlationId == null || correlationId.isEmpty()) {
                throw new MessageException("No CorrelationID provided!");
            }

            connectToQueue();

            T recievedMessage = (T) session.createConsumer(getDestination(), "JMSCorrelationID='" + correlationId + "'").receive(timeoutInMillis);

            if (recievedMessage == null) {
                throw new MessageException("Message either null or timeout occured. Timeout was set to: " + timeoutInMillis);
            } else {
                log.debug("JMS message received: {} \n Content: {}", recievedMessage, ((TextMessage)recievedMessage).getText());
            }

            return recievedMessage;

        } catch (Exception e) {
            log.error("[ Error when retrieving message. ] {}", e.getMessage());
            throw new MessageException("Error when retrieving message: " + e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.stop();
                    connection.close();
                }
            } catch (JMSException e) {
                log.error("[ Error when stopping or closing JMS queue. ] {} {}", e.getMessage(), e.getStackTrace());
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
        log.debug("Connected to {}", getDestination());
    }

    protected ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    protected long getMilliseconds() {
        return MILLISECONDS;
    }
}
