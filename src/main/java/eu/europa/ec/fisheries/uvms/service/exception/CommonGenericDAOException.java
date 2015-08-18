package eu.europa.ec.fisheries.uvms.service.exception;

public class CommonGenericDAOException extends Exception{

    public CommonGenericDAOException() {
    }

    public CommonGenericDAOException(String message) {
        super(message);
    }

    public CommonGenericDAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
