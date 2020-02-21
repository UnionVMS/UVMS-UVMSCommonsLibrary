package eu.europa.ec.fisheries.uvms.commons.date;

import org.junit.Test;

import javax.json.Json;
import javax.json.JsonNumber;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.Instant;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

public class JsonBXmlGregorianCalendarAdapterTest {

    private JsonBXmlGregorianCalendarAdapter adapter = new JsonBXmlGregorianCalendarAdapter();

    @Test
    public void calendarToJson() throws Exception {
        // Given
        Instant now = Instant.parse("2020-01-02T15:15:00.000Z");

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(now.toEpochMilli());

        XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);

        // When
        JsonValue jsonValue = adapter.adaptToJson(xmlGregorianCalendar);

        // Then
        assertEquals(1577978100000L, ((JsonNumber) jsonValue).longValue());
    }

    @Test
    public void jsonToCalendar() throws Exception {
        // Given
        JsonNumber jsonNumber = Json.createValue(1577978100000L);

        // When
        XMLGregorianCalendar xmlGregorianCalendar = adapter.adaptFromJson(jsonNumber);

        // Then
        assertEquals(1577978100000L, xmlGregorianCalendar.toGregorianCalendar().getTimeInMillis());
    }

    @Test
    public void jsonToCalendar_string() throws Exception {
        // Given
        JsonString value = Json.createValue("2020-01-02T15:15:00.000Z");

        // When
        XMLGregorianCalendar xmlGregorianCalendar = adapter.adaptFromJson(value);

        // Then
        assertEquals(1577978100000L, xmlGregorianCalendar.toGregorianCalendar().getTimeInMillis());
    }
}
