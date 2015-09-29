package eu.europa.ec.fisheries.uvms.exception;

public class ServiceException extends ModelMarshallException {

    private static final long serialVersionUID = 7582161942682172612L;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
