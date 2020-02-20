package eu.europa.ec.fisheries.uvms.commons.date;

import javax.json.Json;
import javax.json.JsonNumber;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;
import javax.json.bind.adapter.JsonbAdapter;
import java.time.Instant;

public class JsonBInstantAdapter implements JsonbAdapter<Instant, JsonValue> {

        @Override
        public JsonValue adaptToJson(Instant date) {
            return Json.createValue(date.toEpochMilli());
        }

        @Override
        public Instant adaptFromJson(JsonValue json) {
            if (json.getValueType().equals(ValueType.NUMBER)) {
                return Instant.ofEpochMilli(((JsonNumber)json).longValue());
            } else {
                return DateUtils.stringToDate(((JsonString)json).getString());
            }
        }
}
