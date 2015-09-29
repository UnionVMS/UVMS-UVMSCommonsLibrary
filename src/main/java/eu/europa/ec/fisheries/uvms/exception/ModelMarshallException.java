package eu.europa.ec.fisheries.uvms.exception;

public class ModelMarshallException extends ModelMapperException {

    private static final long serialVersionUID = 7582161942682172612L;

    public ModelMarshallException(String message) {
        super(message);
    }

    public ModelMarshallException(String message, Throwable cause) {
        super(message, cause);
    }
}
