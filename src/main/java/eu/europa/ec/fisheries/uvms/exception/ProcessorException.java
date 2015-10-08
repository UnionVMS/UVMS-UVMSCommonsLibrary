package eu.europa.ec.fisheries.uvms.exception;

public class ProcessorException extends Exception {
    private static final long serialVersionUID = 1L;

    public ProcessorException(String message) {
        super(message);
    }

    public ProcessorException(String message, Throwable cause) {
        super(message, cause);
    }
}
