package eu.europa.ec.fisheries.uvms.commons.message2.api;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;
import java.util.Map;

public interface MessageProducer2 {

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    String sendModuleMessageWithProps(String text, Destination replyTo, Map<String, String> props) throws JMSException;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    String sendModuleMessageWithProps(String text, Destination replyTo, Map<String, String> props, int jmsDeliveryMode, long timeToLiveInMillis) throws JMSException;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    String sendModuleMessage(String text, Destination replyTo) throws JMSException;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    String sendModuleMessageNonPersistent(String text, Destination replyTo, long timeToLiveInMillis) throws JMSException;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    void sendResponseMessageToSender(TextMessage message, String text) throws JMSException;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    void sendResponseMessageToSender(TextMessage message, String text, long timeToLive) throws JMSException;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    void sendResponseMessageToSender(TextMessage message, String text, long timeToLive, int deliveryMode) throws JMSException;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    void sendResponseMessageToSender(TextMessage message, String text, String moduleName) throws JMSException;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    String sendMessageToSpecificQueueWithFunction(String messageToSend, Destination destination, Destination replyTo, String function, String grouping) throws JMSException;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    void sendFault(TextMessage textMessage, Fault fault) throws JMSException, JAXBException;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    String sendMessageWithSpecificIds(String messageToSend, Destination destination, Destination replyTo, String jmsMessageID, String jmsCorrelationID) throws JMSException;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    String sendMessageToSpecificQueue(String messageToSend, Destination destination, Destination replyTo) throws JMSException;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    String sendMessageToSpecificQueue(String messageToSend, Destination destination, Destination replyTo, long timeToLiveInMillis) throws JMSException;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    String sendMessageToSpecificQueue(String messageToSend, Destination destination, Destination replyTo, long timeToLiveInMillis, int deliveryMode) throws JMSException;

    Destination  getDestination();


}
