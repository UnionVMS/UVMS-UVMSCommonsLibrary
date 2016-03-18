package eu.europa.ec.fisheries.uvms.common;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {

    final static org.slf4j.Logger LOG = LoggerFactory.getLogger(DateUtils.class);

    public static final String DATE_TIME_UI_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    final static String FORMAT = "yyyy-MM-dd HH:mm:ss Z";

    // thread safe formatter
    public static DateTimeFormatter UI_FORMATTER = DateTimeFormat.forPattern(DATE_TIME_UI_FORMAT);

    private DateUtils() {

    }

    /**
     * Get current timestamp in XMLGreorianCalender format
     *
     * @see {@link XMLGregorianCalendar}
     *
     * @return current timestamp
     * @throws DatatypeConfigurationException
     */
    public static XMLGregorianCalendar getCurrentDate() throws DatatypeConfigurationException {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();

        return datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
    }

    public static String dateToString(Date date) {
        String dateString = null;
        if (date != null) {
            DateFormat df = new SimpleDateFormat(FORMAT);
            dateString = df.format(date);
        }
        return dateString;
    }

    public static String parseUTCDateToString(Date date) {
        return dateToString(date);
    }

    public static DateTime nowUTC() throws IllegalArgumentException {
        return new DateTime(DateTimeZone.UTC).toLocalDateTime().toDateTime();
    }

    public static DateTime nowUTCMinusHours(final DateTime now, final int hours) throws IllegalArgumentException {
        return now.minusHours(hours);
    }

    public static DateTime nowUTCMinusSeconds(final DateTime now, final Float hours) throws IllegalArgumentException {
        return now.minusSeconds((int) (hours * 3600));
    }

    public static Date stringToDate(String dateString) throws IllegalArgumentException {
        if (dateString != null) {
            DateTimeFormatter formatter = DateTimeFormat.forPattern(FORMAT).withOffsetParsed();
            DateTime dateTime = formatter.withZoneUTC().parseDateTime(dateString);
            GregorianCalendar cal = dateTime.toGregorianCalendar();
            return cal.getTime();
        } else {
            return null;
        }
    }

    public static XMLGregorianCalendar dateToXmlGregorian(Date timestamp) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(timestamp);
        XMLGregorianCalendar xmlCalendar = null;
        try {
            xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (DatatypeConfigurationException ex) {
        }
        return xmlCalendar;
    }

}
