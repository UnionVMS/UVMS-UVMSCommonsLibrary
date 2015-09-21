package eu.europa.ec.fisheries.uvms.message;

import javax.jms.TextMessage;

public interface MessageService {

    <T> T getMessage(final String correlationId, final Class type) throws MessageException;

    String sendModuleMessage(String text) throws MessageException;

    void sendModuleResponseMessage(TextMessage message, String text);

}
