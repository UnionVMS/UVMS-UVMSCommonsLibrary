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

import eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Created by osdjup on 2016-12-02.
 */

public class JMSUtils {

    private JMSUtils() {}

    private final static Logger LOG = LoggerFactory.getLogger(JMSUtils.class);

    // ConnectionFactory object is a JMS administered object and supports concurrent use.
    static final ConnectionFactory CACHED_CONNECTION_FACTORY = lookupConnectionFactory();

    private static ConnectionFactory lookupConnectionFactory() {
        ConnectionFactory connectionFactory;
        InitialContext ctx = null;
        LOG.debug("Open connection to JMS broker");
        try {
            ctx = getInitialContext();
            connectionFactory = (QueueConnectionFactory) ctx.lookup(MessageConstants.CONNECTION_FACTORY);
        } catch (final NamingException ne) {
            // if we did not find the connection factory we might need to add java:/ at the start
            LOG.debug("Connection Factory lookup failed for " + MessageConstants.CONNECTION_FACTORY);
            final String wfName = "java:/" + MessageConstants.CONNECTION_FACTORY;
            try {
                connectionFactory = (QueueConnectionFactory) ctx.lookup(wfName);
            } catch (final Exception e) {
                LOG.error("[ERROR] Connection Factory lookup failed for both " + MessageConstants.CONNECTION_FACTORY + " and " + wfName);
                throw new RuntimeException(e);
            }
        } finally {
            closeInitialContext(ctx);
        }
        return connectionFactory;
    }

    public static Queue lookupQueue(final String queueName) {
        try {
            InitialContext ctx = getInitialContext();
            Queue queueObj;
            try {
                queueObj = (Queue) ctx.lookup(queueName);
            } catch (final NamingException e) {
                // if we did not find the queue we might need to add java:/ at the start
                final String wfQueueName = "java:/" + queueName;
                try {
                    queueObj = (Queue) ctx.lookup(wfQueueName);
                } catch (final Exception e2) {
                    LOG.error("Queue lookup failed for both " + queueName + " and " + wfQueueName);
                    throw new RuntimeException(e);
                }
            } finally {
                ctx.close();
            }
            return queueObj;
        } catch (final NamingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Topic lookupTopic(final String topicName) {
        InitialContext ctx = getInitialContext();
        Topic topicObj;
        try {
            topicObj = (Topic) ctx.lookup(topicName);
        } catch (final NamingException e) {
            // if we did not find the queue we might need to add java:/ at the start
            final String wfTopicName = "java:/" + topicName;
            try {
                topicObj = (Topic) ctx.lookup(wfTopicName);
            } catch (final Exception e2) {
                LOG.error("Topic lookup failed for both " + topicName + " and " + wfTopicName);
                throw new RuntimeException(e);
            }
        }
        return topicObj;
    }


    static Session createSessionAndStartConnection(final Connection connection) throws JMSException {
        final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        connection.start();
        return session;
    }


    public static void disconnectQueue(final Connection connection, Session session, MessageProducer producer) {
        try {
            if (producer != null) {
                producer.close();
            }
        } catch (final JMSException e) {
            LOG.error("[ Error when closing producer ] {}", e);
        }
        try {
            if (session != null) {
                session.close();
            }
        } catch (final JMSException e) {
            LOG.error("[ Error when closing session ] {}", e);
        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (final JMSException e) {
            LOG.error("[ Error when closing JMS connection ] {}", e);
        }
    }

    public static void disconnectQueue(final Connection connection, Session session, MessageConsumer consumer) {
        try {
            if (consumer != null) {
                consumer.close();
            }
        } catch (final JMSException e) {
            LOG.error("[ Error when closing consumer ] {}", e);
        }
        try {
            if (session != null) {
                session.close();
            }
        } catch (final JMSException e) {
            LOG.error("[ Error when closing session ] {}", e);
        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (final JMSException e) {
            LOG.error("[ Error when closing JMS connection ] {}", e);
        }
    }

    private static void closeInitialContext(InitialContext ctx) {
        if(ctx != null){
            try {
                ctx.close();
            } catch (NamingException e) {
                LOG.error("[ERROR] Error while trying to close context!");
            }
        }
    }

    private static InitialContext getInitialContext() {
        try {
            return new InitialContext();
        } catch (NamingException e) {
            throw new RuntimeException("Couldn't initialize the IntialContext!");
        }
    }
}
