package eu.europa.ec.fisheries.uvms.common;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtils {

    private static final String DATE_TIME_UI_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    // thread safe formatter
    public static DateTimeFormatter UI_FORMATTER = DateTimeFormat.forPattern(DATE_TIME_UI_FORMAT);

    private DateUtils(){

    }

}
