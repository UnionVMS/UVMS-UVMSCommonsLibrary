package eu.europa.ec.fisheries.uvms.message;

import javax.jms.Destination;

public interface MessageProducer {

    String sendModuleMessage(String text, Destination replyTo) throws MessageException;

}
