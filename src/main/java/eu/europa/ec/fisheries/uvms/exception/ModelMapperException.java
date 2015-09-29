package eu.europa.ec.fisheries.uvms.exception;

public class ModelMapperException extends ModelException {
    private static final long serialVersionUID = 1L;

    public ModelMapperException(String message) {
        super(message);
    }

    public ModelMapperException(String message, Throwable cause) {
        super(message, cause);
    }
}
