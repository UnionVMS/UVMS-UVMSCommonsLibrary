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

import javax.annotation.PostConstruct;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.uvms.commons.message.api.Fault;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageProducer;

public abstract class AbstractProducer implements MessageProducer {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractProducer.class);

	private ConnectionFactory connectionFactory;

	private Destination destination;

	@PostConstruct
	public void initializeConnectionFactory() {
		connectionFactory = JMSUtils.lookupConnectionFactory();
		destination = JMSUtils.lookupQueue(getDestinationName());
	}

	protected final ConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public final String sendModuleMessage(final String text, final Destination replyTo) throws MessageException {

		Connection connection = null;

		try {
			connection = connectionFactory.createConnection();
			final Session session = JMSUtils.connectToQueue(connection);

			LOGGER.info("Sending message with replyTo: [{}]", replyTo);
			LOGGER.debug("Message content : [{}]", text);

			if (connection == null || session == null) {
				throw new MessageException("[ Connection or session is null, cannot send message ] ");
			}

			final TextMessage message = session.createTextMessage();
			message.setJMSReplyTo(replyTo);
			message.setText(text);
			session.createProducer(getDestination()).send(message);
			LOGGER.info("Message with {} has been successfully sent.", message.getJMSMessageID());
			return message.getJMSMessageID();

		} catch (final JMSException e) {
			LOGGER.error("[ Error when sending message. ] {}", e.getMessage());
			throw new MessageException("[ Error when sending message. ]", e);
		} finally {
			JMSUtils.disconnectQueue(connection);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public final void sendModuleResponseMessage(final TextMessage message, final String text, final String moduleName) {
		Connection connection = null;
		try {
			connection = connectionFactory.createConnection();
			final Session session = JMSUtils.connectToQueue(connection);

			LOGGER.info("Sending message back to recipient from" + moduleName + " with correlationId {} on queue: {}",
					message.getJMSMessageID(), message.getJMSReplyTo());

			final TextMessage response = session.createTextMessage(text);
			response.setJMSCorrelationID(message.getJMSMessageID());
			session.createProducer(message.getJMSReplyTo()).send(response);
		} catch (final JMSException e) {
			LOGGER.error("[ Error when returning" + moduleName + "request. ] {} {}", e.getMessage(), e.getStackTrace());
		} finally {
			JMSUtils.disconnectQueue(connection);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public final void sendModuleResponseMessage(final TextMessage message, final String text) {
		Connection connection = null;
		try {
			connection = connectionFactory.createConnection();
			final Session session = JMSUtils.connectToQueue(connection);

			LOGGER.info("Sending message back to recipient from  with correlationId {} on queue: {}",
					message.getJMSMessageID(), message.getJMSReplyTo());

			final TextMessage response = session.createTextMessage(text);
			response.setJMSCorrelationID(message.getJMSMessageID());
			session.createProducer(message.getJMSReplyTo()).send(response);
		} catch (final JMSException e) {
			LOGGER.error("[ Error when returning request. ] {} {}", e.getMessage(), e.getStackTrace());
		} finally {
			JMSUtils.disconnectQueue(connection);
		}
	}

	protected final Destination getDestination() {
		if (destination == null) {
			destination = JMSUtils.lookupQueue(getDestinationName());
		}
		return destination;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void sendFault(final TextMessage message, Fault fault) {
		Connection connection = null;
		try {
			String text = JAXBUtils.marshallJaxBObjectToString(fault);
			connection = connectionFactory.createConnection();
			final Session session = JMSUtils.connectToQueue(connection);

			LOGGER.debug(
					"Sending message back to recipient from  with correlationId {} on queue: {}",
					message.getJMSMessageID(), message.getJMSReplyTo());

			final TextMessage response = session.createTextMessage();
			response.setText(text);
			session.createProducer(message.getJMSReplyTo()).send(response);
		} catch (JMSException | JAXBException e) {
			LOGGER.error("[ Error when returning request. ] {} {}", e.getMessage(),
					e.getStackTrace());
		} finally {
			JMSUtils.disconnectQueue(connection);
		}
	}
}
