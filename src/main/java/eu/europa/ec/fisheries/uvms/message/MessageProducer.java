package eu.europa.ec.fisheries.uvms.message;

import javax.jms.Destination;
import javax.jms.TextMessage;

public interface MessageProducer {

    String sendModuleMessage(String text, Destination replyTo) throws MessageException;

    void sendModuleResponseMessage(TextMessage message, String text);

}
