package eu.europa.ec.fisheries.uvms.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * //TODO create test
 */
public abstract class AbstractProducer implements MessageProducer {

    protected final static Logger LOG = LoggerFactory.getLogger(MessageProducer.class);

    @Resource(lookup = MessageConstants.CONNECTION_FACTORY)
    private ConnectionFactory connectionFactory;

    private Connection connection = null;
    private Session session = null;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendModuleMessage(String text, Destination replyTo) throws MessageException {

        try {
            connectToQueue();

            LOG.debug("Sending message:[{}], with replyTo: [{}]", text, replyTo);
            if (connection == null || session == null) {
                throw new MessageException("[ Connection or session is null, cannot send message ] ");
            }

            TextMessage message = session.createTextMessage();
            message.setJMSReplyTo(replyTo);
            message.setText(text);
            session.createProducer(getDestination()).send(message);
            LOG.debug("Message with ID: {} has been successfully sent.", message.getJMSMessageID());
            return message.getJMSMessageID();

        } catch (JMSException e) {
            LOG.error("[ Error when sending message. ] {}", e.getMessage());
            throw new MessageException("[ Error when sending message. ]", e);
        } finally {
            try {
                if (connection != null) {
                    connection.stop();
                    connection.close();
                }
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
        LOG.debug("Connecting to queue: {}", getDestination());
    }

    protected abstract Destination getDestination();
}
