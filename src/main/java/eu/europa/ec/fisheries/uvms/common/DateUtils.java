package eu.europa.ec.fisheries.uvms.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.LoggerFactory;

public class DateUtils {

    final static org.slf4j.Logger LOG = LoggerFactory.getLogger(DateUtils.class);

    public static final String DATE_TIME_UI_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_TIME_FILTER_FORMAT = "yyyy-MM-dd HH:mm:ss";
    final static String FORMAT = "yyyy-MM-dd HH:mm:ss Z";

    // thread safe formatter
    public static DateTimeFormatter UI_FORMATTER = DateTimeFormat.forPattern(DATE_TIME_UI_FORMAT);
    public static DateTimeFormatter FILTER_FORMATTER = DateTimeFormat.forPattern(DATE_TIME_FILTER_FORMAT);

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
        XMLGregorianCalendar now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);

        return now;
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
        return new DateTime(DateTimeZone.UTC);
    }

    public static DateTime nowUTCMinusHours(final DateTime now, final int hours) throws IllegalArgumentException {
        return now.minusHours(hours);
    }


}
