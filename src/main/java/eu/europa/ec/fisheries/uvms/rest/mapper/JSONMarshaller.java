package eu.europa.ec.fisheries.uvms.rest.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.europa.ec.fisheries.uvms.exception.ModelMapperException;
import eu.europa.ec.fisheries.uvms.exception.ModelMarshallException;

import java.io.IOException;

public class JSONMarshaller {

    /**
     * Convert Json string back to Java object.
     *
     * @param json The Json string to convert from
     * @param clazz The java class to convert to
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    protected <T> T marshallJsonStringToObject(final String json, final Class clazz) throws ModelMarshallException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return (T) mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new ModelMarshallException("Error when converting " + json , e);
        }
    }
}
