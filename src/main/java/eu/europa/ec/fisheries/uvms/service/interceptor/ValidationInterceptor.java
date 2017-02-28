package eu.europa.ec.fisheries.uvms.service.interceptor;

import java.util.List;
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

import eu.europa.ec.fisheries.uvms.rest.constants.ErrorCodes;
import eu.europa.ec.fisheries.uvms.rest.dto.ResponseDto;

@Interceptor
public class ValidationInterceptor {
	
	private static ValidatorFactory factory;
	
	private static Validator validator;
	

	static {
		factory = Validation.buildDefaultValidatorFactory();
    	validator = factory.getValidator();
	}
	
	@AroundInvoke
	public Object validateInputDto(final InvocationContext ic) throws Exception {
		Object[] params = ic.getParameters();
		for (Object param : params) {
			if(param instanceof List) {
				List paramList = (List)param;
				for(Object obj : paramList) {
					Set<ConstraintViolation<Object>> constraintViolations = validator.validate(obj);
					if (!constraintViolations.isEmpty()) {
			    		return badRequest();
			    	}
				}
			} else {
				Set<ConstraintViolation<Object>> constraintViolations = validator.validate(param);    	
		    	if (!constraintViolations.isEmpty()) {
		    		return badRequest();
		    	}
			}	    	
		}
		return ic.proceed();
	}
	
	public Response badRequest() {
		ResponseDto dto = new ResponseDto(HttpServletResponse.SC_BAD_REQUEST, ErrorCodes.INPUT_VALIDATION_FAILED);
		Response response = Response.status(HttpServletResponse.SC_BAD_REQUEST).entity(dto).build();
		return response;
	}
}
