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

import eu.europa.ec.fisheries.uvms.commons.message.api.MessageConsumer;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.commons.message.context.MappedDiagnosticContext;
import javax.annotation.PostConstruct;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractConsumer implements MessageConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractConsumer.class);

	private static long MILLISECONDS = 600000;
	private ConnectionFactory connectionFactory;
	private Destination destination;

	@PostConstruct
	public void initializeConnectionFactory() {
		connectionFactory = JMSUtils.lookupConnectionFactory();
		destination = JMSUtils.lookupQueue(getDestinationName());
	}


	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@SuppressWarnings(value = "unchecked")
	public <T> T getMessage(final String correlationId, final Class type, final Long timeoutInMillis) throws MessageException {
		Connection connection = null;
		Session session = null;
		javax.jms.MessageConsumer consumer = null;
		try {
			connection = getConnectionFactory().createConnection();
			session = JMSUtils.connectToQueue(connection);
			LOGGER.debug("Trying to receive message with correlationId:[{}], class type:[{}], timeout: {}", correlationId, type, timeoutInMillis);
			if (correlationId == null || correlationId.isEmpty()) {
				throw new MessageException("No CorrelationID provided!");
			}
			consumer = session.createConsumer(getDestination(), "JMSCorrelationID='" + correlationId + "'");
			final T receivedMessage = (T) consumer.receive(timeoutInMillis);
			if (receivedMessage == null) {
				throw new MessageException("Message either null or timeout occurred. Timeout was set to: " + timeoutInMillis);
			} else {
				LOGGER.debug("Message with {} has been successfully received.", correlationId);
				LOGGER.debug("JMS message received: {} \n Content: {}", receivedMessage, ((TextMessage) receivedMessage).getText());
			}
			MappedDiagnosticContext.addMessagePropertiesToThreadMappedDiagnosticContext((TextMessage) receivedMessage);
			return receivedMessage;
		} catch (final Exception e) {
			LOGGER.error("[ Error when retrieving message. ] {}", e.getMessage());
			throw new MessageException("Error when retrieving message: " + e.getMessage());
		} finally {
			JMSUtils.disconnectQueue(connection, session, consumer);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@SuppressWarnings(value = "unchecked")
	public <T> T getMessage(final String correlationId, final Class type) throws MessageException {
		return getMessage(correlationId, type, getMilliseconds());
	}

	protected long getMilliseconds() {
		return MILLISECONDS;
	}

	@Override
	public Destination getDestination() {
		if (destination == null) {
			destination = JMSUtils.lookupQueue(getDestinationName());
		}		
		return destination;
	}

	protected Connection getConnection() throws JMSException {
		return getConnectionFactory().createConnection();
	}

	protected ConnectionFactory getConnectionFactory() {
		if (connectionFactory == null) {
			connectionFactory = JMSUtils.lookupConnectionFactory();
		}
		return connectionFactory;
	}
}
