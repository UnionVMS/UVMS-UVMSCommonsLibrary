/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
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