package eu.europa.ec.fisheries.uvms.commons.date;

import javax.json.Json;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.bind.adapter.JsonbAdapter;
import java.time.Duration;

public class JsonBDurationAdapter implements JsonbAdapter<Duration, JsonValue>

{

    @Override
    public JsonValue adaptToJson(Duration date) {
        return Json.createValue(date.toMillis());
    }

    @Override
    public Duration adaptFromJson(JsonValue json) {
        return Duration.ofMillis(Long.valueOf(((JsonString)json).getString()));
    }
}
