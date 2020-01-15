package eu.europa.ec.fisheries.uvms.commons.date;

import javax.json.Json;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.bind.adapter.JsonbAdapter;
import java.time.Instant;

public class JsonBInstantAdapter implements JsonbAdapter<Instant, JsonValue>

    {

        @Override
        public JsonValue adaptToJson(Instant date) {
            return Json.createValue(DateUtils.dateToEpochMilliseconds(date));
        }

        @Override
        public Instant adaptFromJson(JsonValue json) {
            return DateUtils.stringToDate(((JsonString)json).getString());
        }
}
