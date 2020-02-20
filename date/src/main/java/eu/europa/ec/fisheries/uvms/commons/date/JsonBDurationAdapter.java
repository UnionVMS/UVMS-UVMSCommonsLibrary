package eu.europa.ec.fisheries.uvms.commons.date;

import javax.json.Json;
import javax.json.JsonNumber;
import javax.json.JsonValue;
import javax.json.bind.adapter.JsonbAdapter;
import java.time.Duration;

public class JsonBDurationAdapter implements JsonbAdapter<Duration, JsonValue> {

    @Override
    public JsonValue adaptToJson(Duration duration) {
        return Json.createValue(duration.toMillis());
    }

    @Override
    public Duration adaptFromJson(JsonValue json) {
        return Duration.ofMillis(((JsonNumber)json).longValue());
    }
}
