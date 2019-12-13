package eu.europa.ec.fisheries.uvms.commons.date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;

public class UVMSInstantDeserializer extends JsonDeserializer<Instant> {
    @Override
    public Instant deserialize(JsonParser arg0, DeserializationContext arg1) throws IOException {
        return DateUtils.stringToDate(arg0.getValueAsString());
    }
}
