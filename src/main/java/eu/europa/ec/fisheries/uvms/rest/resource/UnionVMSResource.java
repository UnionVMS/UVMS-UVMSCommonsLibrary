/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.rest.resource;

import eu.europa.ec.fisheries.uvms.rest.dto.PaginatedResponse;
import eu.europa.ec.fisheries.uvms.rest.dto.ResponseDto;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.util.List;


public abstract class UnionVMSResource {

	public Response createSuccessResponse() {
		ResponseDto dto = new ResponseDto(HttpServletResponse.SC_OK);
		Response response = Response.status(HttpServletResponse.SC_OK).entity(dto).build();
		return response;
	}

	public <T> Response createSuccessResponse(T data) {
		ResponseDto<T> dto = new ResponseDto<T>(data, HttpServletResponse.SC_OK);
		Response response = Response.status(HttpServletResponse.SC_OK).entity(dto).build();
		return response;
	}

	public <T> Response createSuccessPaginatedResponse(List<T> data, int totalItemsCount) {
		PaginatedResponse<T> dto = new PaginatedResponse<T>();
		dto.setResultList(data).setTotalItemsCount(totalItemsCount).setCode(HttpServletResponse.SC_OK);
		Response response = Response.status(HttpServletResponse.SC_OK).entity(dto).build();
		return response;
	}
	
	public Response createErrorResponse() {
		ResponseDto dto = new ResponseDto(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		Response response = Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity(dto).build();
		return response;
	}
	
	public Response createErrorResponse(String errorMsgCode) {
		ResponseDto dto = new ResponseDto(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errorMsgCode);
		Response response = Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity(dto).build();
		return response;
	}

	public Response createScNotFoundErrorResponse(String errorMsgCode) {
		ResponseDto dto = new ResponseDto(HttpServletResponse.SC_NOT_FOUND, errorMsgCode);
		Response response = Response.status(HttpServletResponse.SC_NOT_FOUND).entity(dto).build();
		return response;
	}

	public Response createAccessForbiddenResponse() {
		ResponseDto dto = new ResponseDto(HttpServletResponse.SC_FORBIDDEN);
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity(dto)
				.build();
	}


	public Response createAccessForbiddenResponse(String errorMsgCode) {
		ResponseDto dto = new ResponseDto(HttpServletResponse.SC_FORBIDDEN, errorMsgCode);
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity(dto)
				.build();
	}
}
