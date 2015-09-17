package eu.europa.ec.fisheries.uvms.rest.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public abstract class AbstractJSONMarshaller {

    /**
     * Convert Json string back to Java object.
     *
     * @param json The Json string to convert from
     * @param clazz The java class to convert to
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    protected <T> T marshallJsonStringToObject(String json, final Class clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return (T) mapper.readValue(json, clazz);
    }
}
