package eu.europa.ec.fisheries.uvms.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * //TODO create temp
 */
public abstract class AbstractProducer implements MessageProducer {

    final static Logger LOG = LoggerFactory.getLogger(MessageProducer.class);

    @Resource(lookup = MessageConstants.CONNECTION_FACTORY)
    private ConnectionFactory connectionFactory;

    private Connection connection = null;
    private Session session = null;

    @Override
    public String sendModuleMessage(String text, Destination replyTo) throws MessageException {

        try {

            connectToQueue();
            TextMessage message = session.createTextMessage();
            message.setJMSReplyTo(replyTo);
            message.setText(text);
            session.createProducer(getDestination()).send(message);
            return message.getJMSMessageID();

        } catch (JMSException e) {
            LOG.error("[ Error when sending message. ] {}", e.getMessage());
            throw new MessageException("[ Error when sending message. ]", e);
        } finally {
            try {
                connection.stop();
                connection.close();
            } catch (JMSException e) {
                LOG.error("[ Error when closing JMS connection ] {}", e.getStackTrace());
                throw new MessageException("[ Error when sending message. ]", e);
            }
        }
    }

    private void connectToQueue() throws JMSException {
        connection = connectionFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        connection.start();
    }

    @Override
    public void sendModuleResponseMessage(TextMessage message, String text){
        try {
            LOG.info("Sending message back to recipient from" + getModuleName() + " with correlationId {} on queue: {}", message.getJMSMessageID(),
                    message.getJMSReplyTo());
            connectQueue();
            TextMessage response = session.createTextMessage(text);
            response.setJMSCorrelationID(message.getJMSMessageID());
            session.createProducer(message.getJMSReplyTo()).send(response);
        } catch (JMSException e) {
            LOG.error("[ Error when returning" + getModuleName() + "request. ] {} {}", e.getMessage(), e.getStackTrace());
        } finally {
            disconnectQueue();
        }
    }

    protected abstract String getModuleName();

    private void connectQueue() throws JMSException {
        connection = connectionFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        connection.start();
    }

    private void disconnectQueue() {
        try {
            connection.stop();
            connection.close();
        } catch (JMSException e) {
            LOG.error("[ Error when stopping or closing JMS queue. ] {} {}", e.getMessage(), e.getStackTrace());
        }
    }

    protected abstract Destination getDestination();
}
