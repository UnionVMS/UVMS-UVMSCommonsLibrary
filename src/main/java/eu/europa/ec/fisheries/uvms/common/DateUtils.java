package eu.europa.ec.fisheries.uvms.common;

import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtils {

    public static final String DATE_TIME_UI_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_TIME_FILTER_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // thread safe formatter
    public static DateTimeFormatter UI_FORMATTER = DateTimeFormat.forPattern(DATE_TIME_UI_FORMAT);
    public static DateTimeFormatter FILTER_FORMATTER = DateTimeFormat.forPattern(DATE_TIME_FILTER_FORMAT);

    private DateUtils(){

    }
    
    /**
     * Get current timestamp in XMLGreorianCalender format
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

}
