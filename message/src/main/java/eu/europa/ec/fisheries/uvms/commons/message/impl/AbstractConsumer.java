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
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageRuntimeException;
import eu.europa.ec.fisheries.uvms.commons.message.context.MappedDiagnosticContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import javax.jms.*;

public abstract class AbstractConsumer implements MessageConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractConsumer.class);

	private static final long DEFAULT_TIME_TO_CONSUME = 120000;

	private static final long FIVE_SECONDS_TO_CONSUME = 5000L;

	private Destination destination;

	private volatile Connection connection;

	private volatile Session session;

	private static final int RETRIES = 100;


	@Override
	@SuppressWarnings(value = "unchecked")
	public <T> T getMessage(final String correlationId, final Class type) throws MessageException {
		return getMessage(correlationId, type, DEFAULT_TIME_TO_CONSUME);
	}

	/**
	 *  When the broker has many sync consumers that are slow to receive (when system is overloaded happens a lot..) it blocks..
	 *  "Restarting" the connection of the slow consumers each 5 seconds makes the broker system more reliable and costs almost nothing!
	 *
	 * @param correlationId
	 * @param type
	 * @param timeoutInMillis
	 * @param <T>
	 * @return
	 * @throws MessageException
	 */
	@Override
	@SuppressWarnings(value = "unchecked")
	public <T> T getMessage(final String correlationId, final Class type, final Long timeoutInMillis) throws MessageException {
		Long newTimeout = timeoutInMillis;
		while (newTimeout > 0){
			T message = getMessageInFiveSeconds(correlationId);
			if(message != null){
				return message;
			}
			newTimeout -= FIVE_SECONDS_TO_CONSUME;
		}
		throw new MessageException("TimeOut occurred while trying to consume message!");
	}

	@SuppressWarnings(value = "unchecked")
	private <T> T getMessageInFiveSeconds(final String correlationId) throws MessageException {
		javax.jms.MessageConsumer consumer = null;
		try {
			LOGGER.debug("Trying to receive message with correlationId:[{}], timeout: {}", correlationId, FIVE_SECONDS_TO_CONSUME);
			if (correlationId == null || correlationId.isEmpty()) {
				throw new MessageException("No CorrelationID provided! Cannot consume synchronously without a CorrelationID!");
			}
			closeResources(consumer);
			initializeConnectionAndDestination();
			consumer = session.createConsumer(getDestination(), "JMSCorrelationID='" + correlationId + "'");
			final T receivedMessage = (T) consumer.receive(FIVE_SECONDS_TO_CONSUME);
			if (receivedMessage == null) {
				return null;
			} else {
				LOGGER.debug("Message with {} has been successfully received.", correlationId);
				LOGGER.debug("JMS message received: {} \n Content: {}", receivedMessage, ((TextMessage) receivedMessage).getText());
			}
			MappedDiagnosticContext.addMessagePropertiesToThreadMappedDiagnosticContext((TextMessage) receivedMessage);
			return receivedMessage;
		} catch (final Exception e) {
			LOGGER.error("Error when retrieving message. " + e.getMessage(),e);
			return null;
		} finally {
			closeResources(consumer);
		}
	}

	private void initializeConnectionAndDestination() {
		try {
			connection = JMSUtils.getConnectionV2();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		} catch (MessageRuntimeException | JMSException e) {
			LOGGER.error("[INIT-ERROR] JMS Connection could not be estabelished!", e);
		}
		destination = getDestination();
	}

	private void closeResources(javax.jms.MessageConsumer consumer) {
		try {
			if(consumer != null){
				consumer.close();
			}
			if(session != null){
				session.close();
				session = null;
			}
			if(connection != null){
				LOGGER.debug("END : [CLOSING-CONNECTION] ID : {}", connection.hashCode());
				connection.close();
				connection = null;
			}
		} catch (JMSException e) {
			LOGGER.error("[CLOSE-ERROR] JMS Connection could not be closed!" + e.getMessage(), e);
		}
	}

	@PreDestroy
	public void preDestroy(){
		LOGGER.info("[DESTROYING-CONSUMER] Consumer has been destroyed..");
		closeResources(null);
	}

	@Override
	public Destination getDestination() {
		if (destination == null) {
			destination = JMSUtils.lookupQueue(getDestinationName());
		}
		return destination;
	}

}
