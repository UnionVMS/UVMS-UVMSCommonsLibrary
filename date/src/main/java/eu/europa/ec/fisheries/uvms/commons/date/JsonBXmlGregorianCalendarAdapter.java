package eu.europa.ec.fisheries.uvms.commons.date;

import javax.json.Json;
import javax.json.JsonNumber;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.bind.adapter.JsonbAdapter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.Instant;
import java.util.GregorianCalendar;

public class JsonBXmlGregorianCalendarAdapter implements JsonbAdapter<XMLGregorianCalendar, JsonValue> {

    @Override
    public JsonValue adaptToJson(XMLGregorianCalendar xmlGregorianCalendar) {
        return Json.createValue(xmlGregorianCalendar.toGregorianCalendar().getTimeInMillis());
    }

    @Override
    public XMLGregorianCalendar adaptFromJson(JsonValue jsonValue) throws Exception {
        if (jsonValue.getValueType().equals(JsonValue.ValueType.NUMBER)) {
            JsonNumber jsonNumber = (JsonNumber) jsonValue;
            Instant instant = Instant.ofEpochMilli(jsonNumber.longValue());

            return instantToXmlGregorianCalendar(instant);
        } else {
            Instant instant = DateUtils.stringToDate(((JsonString) jsonValue).getString());
            return instantToXmlGregorianCalendar(instant);
        }
    }

    private XMLGregorianCalendar instantToXmlGregorianCalendar(Instant instant) throws DatatypeConfigurationException {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(instant.toEpochMilli());

        return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
    }
}
