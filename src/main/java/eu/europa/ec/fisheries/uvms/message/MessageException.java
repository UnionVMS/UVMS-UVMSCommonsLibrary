package eu.europa.ec.fisheries.uvms.message;

public class MessageException extends Exception {

    public MessageException() {
    }

    public MessageException(String message) {
        super(message);
    }

    public MessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
