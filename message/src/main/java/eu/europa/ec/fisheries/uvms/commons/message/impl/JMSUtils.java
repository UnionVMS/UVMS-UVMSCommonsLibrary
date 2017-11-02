/*
 Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
 © European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
 redistribute it and/or modify it under the terms of the GNU General Public License as published by the
 Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
 the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
 copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.commons.message.impl;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants;

/**
 * Created by osdjup on 2016-12-02.
 */

public class JMSUtils {

	private final static Logger LOG = LoggerFactory.getLogger(JMSUtils.class);

	private static InitialContext CACHED_INITIAL_CONTEXT;

	public static ConnectionFactory lookupConnectionFactory() {
		ConnectionFactory connectionFactory = null;
		LOG.debug("Open connection to JMS broker");
		try {
			final InitialContext ctx = getInitialContext();
			connectionFactory = (QueueConnectionFactory) ctx.lookup(MessageConstants.CONNECTION_FACTORY);
		} catch (final NamingException ne) {
			// if we did not find the connection factory we might need to add java:/ at the
			// start
			LOG.debug("Connection Factory lookup failed for " + MessageConstants.CONNECTION_FACTORY);
			final String wfName = "java:/" + MessageConstants.CONNECTION_FACTORY;
			try {
				LOG.debug("trying " + wfName);
				connectionFactory = (QueueConnectionFactory) CACHED_INITIAL_CONTEXT.lookup(wfName);
			} catch (final Exception e) {
				LOG.error("Connection Factory lookup failed for both " + MessageConstants.CONNECTION_FACTORY + " and "
						+ wfName);
				throw new RuntimeException(e);
			}
		}

		return connectionFactory;
	}

	private static InitialContext getInitialContext() throws NamingException {
		if (CACHED_INITIAL_CONTEXT == null) {
			CACHED_INITIAL_CONTEXT = new InitialContext();
		}
		return CACHED_INITIAL_CONTEXT;
	}

	public static Queue lookupQueue(final String queue) {
		InitialContext ctx;
		try {
			ctx = getInitialContext();
			return lookupQueue(ctx, queue);
		} catch (final NamingException e) {
			throw new RuntimeException(e);
		}
	}

	public static Topic lookupTopic(final String topic) {
		InitialContext ctx;
		try {
			ctx = getInitialContext();
			return lookupTopic(ctx, topic);
		} catch (final NamingException e) {
			throw new RuntimeException(e);
		}
	}

	public static Queue lookupQueue(final InitialContext ctx, final String queue) {
		try {
			return (Queue) ctx.lookup(queue);
		} catch (final NamingException e) {
			// if we did not find the queue we might need to add java:/ at the start
			LOG.debug("Queue lookup failed for " + queue);
			final String wfQueueName = "java:/" + queue;
			try {
				LOG.debug("trying " + wfQueueName);
				return (Queue) ctx.lookup(wfQueueName);
			} catch (final Exception e2) {
				LOG.error("Queue lookup failed for both " + queue + " and " + wfQueueName);
				throw new RuntimeException(e);
			}
		}
	}

	public static Topic lookupTopic(final InitialContext ctx, final String topic) {
		try {
			return (Topic) ctx.lookup(topic);
		} catch (final NamingException e) {
			// if we did not find the queue we might need to add java:/ at the start
			LOG.debug("Queue lookup failed for " + topic);
			final String wfTopicName = "java:/" + topic;
			try {
				LOG.debug("trying " + wfTopicName);
				return (Topic) ctx.lookup(wfTopicName);
			} catch (final Exception e2) {
				LOG.error("Topic lookup failed for both " + topic + " and " + wfTopicName);
				throw new RuntimeException(e);
			}
		}
	}

	public static MessageProducer getProducer(final Session session, final Destination destination)
			throws JMSException {
		final javax.jms.MessageProducer producer = session.createProducer(destination);
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		producer.setTimeToLive(60000L);
		return producer;
	}

	public static Session connectToQueue(final Connection connection) throws JMSException {
		final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		connection.start();
		return session;
	}

	public static void disconnectQueue(final Connection connection) {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (final JMSException e) {
			LOG.error("[ Error when closing JMS connection ] {}", e.getMessage());
		}
	}
}
