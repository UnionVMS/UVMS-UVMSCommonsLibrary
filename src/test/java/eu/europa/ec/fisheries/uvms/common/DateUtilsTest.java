package eu.europa.ec.fisheries.uvms.common;

import java.util.Date;

import junit.framework.TestCase;

public class DateUtilsTest extends TestCase {

    public void testDateConversion() {
        Date date = new Date(1441065600000l);

        Date result = DateUtils.stringToDate(DateUtils.dateToString(date));
        assertEquals(date, result);
    }

}
