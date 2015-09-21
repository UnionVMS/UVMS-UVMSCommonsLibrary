package eu.europa.ec.fisheries.uvms.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.*;

/**
 * //TODO create test
 */
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
            try {
                connection.stop();
                connection.close();
            } catch (JMSException e) {
                LOG.error("[ Error when closing JMS connection ] {}", e.getStackTrace());
                throw new MessageException("[ Error when sending message. ]", e);
            }
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
            try {
                connection.stop();
                connection.close();
            } catch (JMSException e) {
                LOG.error("[ Error when stopping or closing JMS queue. ] {} {}", e.getMessage(), e.getStackTrace());
            }
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
            connection.stop();
            connection.close();
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
