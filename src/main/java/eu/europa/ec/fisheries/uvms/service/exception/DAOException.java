package eu.europa.ec.fisheries.uvms.service.exception;

/**
 * //TODO create test
 */
public class DAOException extends RuntimeException {
    private int errorCode;
    private String errorMessage;
    private Exception originalException;

    public DAOException(int errorCode, String errorMessage, Exception originalException){
        this.errorCode=errorCode;
        this.errorMessage=errorMessage;
        this.originalException=originalException;
    }
}
