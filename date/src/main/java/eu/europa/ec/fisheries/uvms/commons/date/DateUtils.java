/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */


package eu.europa.ec.fisheries.uvms.commons.date;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.LoggerFactory;

public class DateUtils {

    final static org.slf4j.Logger LOG = LoggerFactory.getLogger(DateUtils.class);

    static final public DateTime START_OF_TIME = new DateTime( 0000, 1, 1, 0, 0, 0, DateTimeZone.UTC );
    static final public DateTime END_OF_TIME = new DateTime( 9999, 1, 1, 0, 0, 0, DateTimeZone.UTC );

    public static final String DATE_TIME_UI_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_TIME_XML_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    public static final String FORMAT = "yyyy-MM-dd HH:mm:ss Z";
    public final static String DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    // thread safe formatter
    public static DateTimeFormatter UI_FORMATTER = DateTimeFormat.forPattern(DATE_TIME_UI_FORMAT);
    public static DateTimeFormatter XML_FORMATTER = DateTimeFormat.forPattern(DATE_TIME_XML_FORMAT);

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
        return new DateTime(DateTimeZone.UTC).toDateTime();
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

    public static String stringToUTC(String dateTime){
        Date date = DateUtils.stringToDate(dateTime);
        DateFormat df = new SimpleDateFormat(DATE_TIME);
        String dateString = df.format(date);
        return dateString;
    }
	
	public static Date getNowDateUTC(){
        DateTimeZone zone = DateTimeZone.getDefault();
        long utc = zone.convertLocalToUTC(new Date().getTime(), false);
        return new Date(utc);
    }

    public static Date parseToUTCDate(String dateString, String format) throws IllegalArgumentException {
        try {
            if (dateString != null) {
                DateTimeFormatter formatter = DateTimeFormat.forPattern(format).withOffsetParsed();
                DateTime dateTime = formatter.withZoneUTC().parseDateTime(dateString);
                GregorianCalendar cal = dateTime.toGregorianCalendar();
                return cal.getTime();
            } else {
                return null;
            }
        } catch (IllegalArgumentException e) {
            LOG.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

}
