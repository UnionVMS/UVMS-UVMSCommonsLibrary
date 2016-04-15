package eu.europa.ec.fisheries.uvms.message;

import javax.jms.Destination;

public interface MessageConsumer {

    <T> T getMessage(final String correlationId, final Class type, final Long timeoutInMillis) throws MessageException;
    
    
    <T> T getMessage(final String correlationId, final Class type) throws MessageException;

    Destination getDestination();
}