package eu.europa.ec.fisheries.uvms.service.interceptor;

import java.util.Set;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.core.Response;

import eu.europa.ec.fisheries.uvms.rest.dto.ResponseDto;

@Interceptor
public class ValidationInterceptor {
	
	public static String INPUT_VALIDATION_FAILED = "INPUT_VALIDATION_FAILED";
	
	@AroundInvoke
	public Object validateInputDto(final InvocationContext ic) throws Exception {
		Object[] params = ic.getParameters();
		for (Object param : params) {
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	    	Validator validator = factory.getValidator();
	    	Set<ConstraintViolation<Object>> constraintViolations = validator.validate(param);    	
	    	if (!constraintViolations.isEmpty()) {
	    		ResponseDto dto = new ResponseDto(HttpServletResponse.SC_BAD_REQUEST, INPUT_VALIDATION_FAILED);
	    		Response response = Response.status(HttpServletResponse.SC_BAD_REQUEST).entity(dto).build();
	    		return response;
	    	}
		}
		return ic.proceed();
	}
}
