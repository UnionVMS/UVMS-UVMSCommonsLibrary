package eu.europa.ec.fisheries.uvms.message;

/**
 * //TODO create test
 */
public interface MessageConsumer {

    public <T> T getMessage(final String correlationId, final Class type) throws MessageException;
}