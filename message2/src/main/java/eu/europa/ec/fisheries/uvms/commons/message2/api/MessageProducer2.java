package eu.europa.ec.fisheries.uvms.commons.message2.api;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;
import java.util.Map;

public interface MessageProducer2 {

    String sendModuleMessageWithProps(String text, Destination replyTo, Map<String, String> props) throws JMSException;

    String sendModuleMessageWithProps(String text, Destination replyTo, Map<String, String> props, int jmsDeliveryMode, long timeToLiveInMillis) throws JMSException;

    String sendModuleMessage(String text, Destination replyTo) throws JMSException;

    String sendModuleMessageNonPersistent(String text, Destination replyTo, long timeToLiveInMillis) throws JMSException;

    void sendResponseMessageToSender(TextMessage message, String text) throws JMSException;

    void sendResponseMessageToSender(TextMessage message, String text, long timeToLive) throws JMSException;

    void sendResponseMessageToSender(TextMessage message, String text, long timeToLive, int deliveryMode) throws JMSException;

    void sendResponseMessageToSender(TextMessage message, String text, String moduleName) throws JMSException;

    String sendMessageToSpecificQueueWithFunction(String messageToSend, Destination destination, Destination replyTo, String function, String grouping) throws JMSException;

    void sendFault(TextMessage textMessage, Fault fault) throws JMSException, JAXBException;

    String sendMessageWithSpecificIds(String messageToSend, Destination destination, Destination replyTo, String jmsMessageID, String jmsCorrelationID) throws JMSException;

    String sendMessageToSpecificQueue(String messageToSend, Destination destination, Destination replyTo) throws JMSException;

    String sendMessageToSpecificQueue(String messageToSend, Destination destination, Destination replyTo, long timeToLiveInMillis) throws JMSException;

    String sendMessageToSpecificQueue(String messageToSend, Destination destination, Destination replyTo, long timeToLiveInMillis, int deliveryMode) throws JMSException;

    Destination  getDestination();


}
