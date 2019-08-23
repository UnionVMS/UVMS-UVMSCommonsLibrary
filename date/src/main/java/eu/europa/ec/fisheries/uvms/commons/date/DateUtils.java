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

import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateUtils extends XMLDateUtils {

    final static org.slf4j.Logger LOG = LoggerFactory.getLogger(DateUtils.class);


    public static final String DATE_TIME_UI_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public static final String FORMAT = "yyyy-MM-dd HH:mm:ss Z";
    public final static String DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    // thread safe formatter

    private DateUtils() {

    }

    public static String dateToString(Instant date) {
        String dateString = null;
        if (date != null) {
            dateString = date.atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern(FORMAT));
        }
        return dateString;
    }

    public static String parseUTCDateToString(Instant date) {
        return dateToString(date);
    }

    public static Instant nowUTC() throws IllegalArgumentException {
        return Instant.now();
    }

    public static Instant nowUTCMinusHours(final Instant now, final int hours) throws IllegalArgumentException {
        return now.minus(hours, ChronoUnit.HOURS );
    }

    public static Instant nowUTCMinusSeconds(final Instant now, final Float hours) throws IllegalArgumentException {
        return now.minus((int) (hours * 3600), ChronoUnit.HOURS);
    }

    public static Instant stringToDate(String dateString) throws IllegalArgumentException {
        if (dateString != null) {
            return ZonedDateTime.parse(dateString, DateTimeFormatter.ofPattern(FORMAT)).toInstant();
        } else {
            return null;
        }
    }

    public static String stringToUTC(String dateTime){
        Instant date = DateUtils.stringToDate(dateTime);
        DateFormat df = new SimpleDateFormat(DATE_TIME);
        String dateString = df.format(date);
        return dateString;
    }
	
	public static Instant getNowDateUTC(){
        return Instant.now();
    }

    public static Instant parseToUTCDate(String dateString, String format) throws IllegalArgumentException {
        try {
            if (dateString != null) {
                return ZonedDateTime.parse(dateString, DateTimeFormatter.ofPattern(format)).toInstant();
            } else {
                return null;
            }
        } catch (IllegalArgumentException e) {
            LOG.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

}
