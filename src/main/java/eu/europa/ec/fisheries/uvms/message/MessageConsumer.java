package eu.europa.ec.fisheries.uvms.message;

import javax.jms.Destination;

/**
 * //TODO create test
 */
public interface MessageConsumer {

    <T> T getMessage(final String correlationId, final Class type, final Long timeoutInMillis) throws MessageException;

    Destination getDestination();
}