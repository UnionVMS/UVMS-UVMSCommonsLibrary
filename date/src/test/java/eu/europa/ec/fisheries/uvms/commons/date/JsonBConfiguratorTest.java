package eu.europa.ec.fisheries.uvms.commons.date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import java.io.StringReader;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;
import javax.json.bind.Jsonb;
import org.junit.Test;

public class JsonBConfiguratorTest {

    @Test
    public void serializingDateTest() {
        Jsonb jsonb = new JsonBConfigurator().getContext(null);
        Date date = new Date();
        Instant instant = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        Duration duration = Duration.ofMillis(1000000000);
        String json = jsonb.toJson(new JsonbSerializationTestClass(date, instant, duration));
        
        JsonbSerializationTestClass fromJson = jsonb.fromJson(json, JsonbSerializationTestClass.class);
        assertThat(fromJson.getDate(), is(date));
        assertThat(fromJson.getInstant(), is(instant));
        assertThat(fromJson.getDuration(), is(duration));
    }
    
    @Test
    public void numberDateTest() {
        Jsonb jsonb = new JsonBConfigurator().getContext(null);
        Date date = new Date();
        Instant instant = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        Duration duration = Duration.ofMillis(1000000000);
        String json = jsonb.toJson(new JsonbSerializationTestClass(date, instant, duration));
        
        JsonReader jsonReader = Json.createReader(new StringReader(json));
        JsonObject object = jsonReader.readObject();
        JsonValue dateJson = object.get("date");
        assertThat(dateJson.getValueType(), is(ValueType.NUMBER));
        jsonReader.close();
    }
    
    @Test
    public void numberInstantTest() {
        Jsonb jsonb = new JsonBConfigurator().getContext(null);
        Date date = new Date();
        Instant instant = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        Duration duration = Duration.ofMillis(1000000000);
        String json = jsonb.toJson(new JsonbSerializationTestClass(date, instant, duration));
        
        JsonReader jsonReader = Json.createReader(new StringReader(json));
        JsonObject object = jsonReader.readObject();
        JsonValue dateJson = object.get("instant");
        assertThat(dateJson.getValueType(), is(ValueType.NUMBER));
        jsonReader.close();
    }
    
    @Test
    public void numberDurationTest() {
        Jsonb jsonb = new JsonBConfigurator().getContext(null);
        Date date = new Date();
        Instant instant = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        Duration duration = Duration.ofMillis(1000000000);
        String json = jsonb.toJson(new JsonbSerializationTestClass(date, instant, duration));
        JsonReader jsonReader = Json.createReader(new StringReader(json));
        JsonObject object = jsonReader.readObject();
        JsonValue dateJson = object.get("duration");
        assertThat(dateJson.getValueType(), is(ValueType.NUMBER));
        jsonReader.close();
    }
    
    @Test
    public void instantStringTest() {
        Jsonb jsonb = new JsonBConfigurator().getContext(null);
        Instant instant = Instant.now().truncatedTo(ChronoUnit.SECONDS);

        String json = "{\"instant\": \"" + instant.atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern(DateFormats.Strings.DATE_TIME_PATTERN_WITH_T)) + "\" }";
        
        JsonbSerializationTestClass fromJson = jsonb.fromJson(json, JsonbSerializationTestClass.class);
        
        assertThat(fromJson.getInstant(), is(instant));
    }
}