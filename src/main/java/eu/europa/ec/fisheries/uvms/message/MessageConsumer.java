package eu.europa.ec.fisheries.uvms.message;

public interface MessageConsumer {

    <T> T getMessage(final String correlationId, final Class type) throws MessageException;
}
