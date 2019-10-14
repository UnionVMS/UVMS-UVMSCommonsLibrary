package eu.europa.ec.fisheries.uvms.commons.rest.filter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.util.Collections;


@Provider
public class EncodingResponseFilter implements ContainerResponseFilter {

	private final String CONTENT_TYPE_HEADER = "content-type";

	@Override
	public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) {

		MultivaluedMap<String, Object> headers = containerResponseContext.getHeaders();

		headers.forEach((k,v)->{

			if(k.equalsIgnoreCase(CONTENT_TYPE_HEADER) && !v.stream().anyMatch(obj -> String.valueOf(obj).contains(";charset"))) {
				String extendHeaderWithEncoding = String.valueOf(v.get(v.size() - 1)) + ";charset=utf-8";
				headers.put(CONTENT_TYPE_HEADER, Collections.singletonList(extendHeaderWithEncoding));
			}
		});

	}

}
