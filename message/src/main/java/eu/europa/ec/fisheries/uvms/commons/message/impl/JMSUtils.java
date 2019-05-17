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
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by osdjup on 2016-12-02.
 */

public class JMSUtils {

    private JMSUtils() {}

    private final static Logger LOG = LoggerFactory.getLogger(JMSUtils.class);

    private static final int RETRIES = 100;

    // ConnectionFactory object is a JMS administered object and supports concurrent use! @ConnectionFactory.
    static ConnectionFactory CACHED_CONNECTION_FACTORY = lookupConnectionFactory();

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


    public static Session createSessionAndStartConnection(Connection connection) throws JMSException {
        Session session = null;
        try {
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (JMSException ex){ // Probably the connection was already closed by the broker..!
            if(session != null){
                session.close();
            }
            ConnectionFactory connectionFactory = lookupConnectionFactory();
            CACHED_CONNECTION_FACTORY = connectionFactory;
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        }
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

    static Connection getConnectionWithRetry(int retries) throws MessageException {
        try {
            Connection conn = JMSUtils.CACHED_CONNECTION_FACTORY.createConnection();
            conn.start();
            return conn;
        } catch (JMSException e) {
            if(retries > 0){
                LOG.warn("Couldn't create connection.. Going to retry for the [-"+(RETRIES-retries)+"-] time now [After sleeping for 1.5 Seconds]..");
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException ignored1) {
                }
                int newRetries = retries - 1;
                return getConnectionWithRetry(newRetries);
            }
            throw new MessageException("Couldn't create connection", e);
        }
    }

    /**
     * Tries to create the {connection, sesssion, producer} objcects.
     * If the can succesfully be created it means the broker accepts connections..
     *
     * It tries each second for @param secondsToWait times..
     *
     * @param secondsToWait
     * @param destination
     * @return
     */
    protected static boolean waitForConnection(int secondsToWait, Destination destination){
        Connection connection = null;
        Session session = null;
        javax.jms.MessageProducer producer = null;
        boolean couldConnect;
        while(secondsToWait > 0){
            try {
                Thread.sleep(1000);
                connection = JMSUtils.getConnection();
                session = JMSUtils.createSessionAndStartConnection(connection);
                producer = session.createProducer(destination);
                couldConnect = true;
            } catch (InterruptedException | JMSException ignored1) {
                couldConnect = false;
            } finally {
                JMSUtils.disconnectQueue(connection, session, producer);
            }
            if(couldConnect){
                return true;
            }
            secondsToWait--;
        }
        return false;
    }

    public static Connection getConnectionV2() throws JMSException {
        AtomicReference<Connection> connection = new AtomicReference<>(lookupConnectionFactory().createConnection());
        connection.get().setExceptionListener(exception -> {
            LOG.error("ExceptionListener triggered -> " + exception.getMessage(), exception);
            try {
                Thread.sleep(5000); // Wait 5 seconds (JMS server restarted?)
                connection.set(lookupConnectionFactory().createConnection());
            } catch (InterruptedException | JMSException e) {
                LOG.error("Error pausing thread or retrying to create connection! -> {}", e.getMessage());
                throw new RuntimeException("[FATAL] Unrecoverable error during Connection creation...");
            }
        });
        return connection.get();
    }

    static Connection getConnection() throws JMSException {
        return JMSUtils.CACHED_CONNECTION_FACTORY.createConnection();
    }
}
