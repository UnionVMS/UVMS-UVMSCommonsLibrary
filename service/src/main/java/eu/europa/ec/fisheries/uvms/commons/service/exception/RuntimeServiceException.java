package eu.europa.ec.fisheries.uvms.commons.service.exception;

/**
 * Runtime exception encapsulating a {@link ServiceException} for throwing from lambdas.
 */
public class RuntimeServiceException extends RuntimeException {
	public RuntimeServiceException(String message, ServiceException cause) {
		super(message, cause);
	}
}
