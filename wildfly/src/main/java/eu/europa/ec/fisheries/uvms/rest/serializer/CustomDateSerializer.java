package eu.europa.ec.fisheries.uvms.rest.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.Date;

public class CustomDateSerializer extends JsonSerializer<Date> {

    @Override
    public void serialize (Date value, JsonGenerator gen, SerializerProvider arg2)
      throws IOException {
        gen.writeString(DateUtils.UI_FORMATTER.print(new DateTime(value)));
    }
}
