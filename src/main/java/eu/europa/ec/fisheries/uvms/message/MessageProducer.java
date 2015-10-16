package eu.europa.ec.fisheries.uvms.message;

import javax.jms.Destination;

/**
 * //TODO create test
 */
public interface MessageProducer {

    public String sendModuleMessage(String text, Destination replyTo) throws MessageException;

    }
