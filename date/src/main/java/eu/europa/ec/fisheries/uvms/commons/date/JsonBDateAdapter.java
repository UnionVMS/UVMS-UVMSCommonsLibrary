package eu.europa.ec.fisheries.uvms.commons.date;

import java.util.Date;
import javax.json.Json;
import javax.json.JsonNumber;
import javax.json.JsonValue;
import javax.json.bind.adapter.JsonbAdapter;

public class JsonBDateAdapter implements JsonbAdapter<Date, JsonValue> {

    @Override
    public JsonValue adaptToJson(Date date) {
        return Json.createValue(date.getTime());
    }

    @Override
    public Date adaptFromJson(JsonValue json) {
        return new Date(((JsonNumber)json).longValue());
    }
}
