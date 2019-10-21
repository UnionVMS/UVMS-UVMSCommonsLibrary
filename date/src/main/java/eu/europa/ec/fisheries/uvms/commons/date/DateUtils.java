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
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class DateUtils extends XMLDateUtils {

    final static org.slf4j.Logger LOG = LoggerFactory.getLogger(DateUtils.class);


    public static final String DATE_TIME_UI_FORMAT = DateFormats.Strings.DATE_TIME_PATTERN_UTC_WITH_T;

    // thread safe formatter

    private DateUtils() {

    }

    public static String dateToString(Instant date) {
        String dateString = null;
        if (date != null) {
            dateString = date.atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern(DateFormats.DATE_TIME_PATTERN.getFormat()));
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
            return convertDateTimeInUTC(dateString);
        } else {
            return null;
        }
    }

    public static Instant convertDateTimeInUTC(String dateTimeInUTC){
        if(dateTimeInUTC == null){
            return null;
        }
        if(dateTimeInUTC.length() < 20){    //if there is no offset info, assume UTC and add it
            dateTimeInUTC = dateTimeInUTC.concat(" Z");
        }
        for (DateFormats format : DateFormats.values()) {
            Instant date = convertDateTimeInUTC(dateTimeInUTC, format.getFormat());
            if (date != null) {
                return date;
            }
        }
        LOG.error("Could not parse dateTimeInUTC: " + dateTimeInUTC + " with pattern any defined pattern.");
        return null;
    }

    public static Instant convertDateTimeInUTC(String dateTimeInUTC, String pattern){
        if (dateTimeInUTC != null) {
            try {
                return ZonedDateTime.parse(dateTimeInUTC, DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH)).toInstant();   //goes via ZonedDateTime to make sure that it can handle formats other then ISO_INSTANT, for example formats other then 2011-12-03T10:15:30Z and does not cry in pain from a zone
            } catch (DateTimeParseException e) {
                LOG.info("Could not parse dateTimeInUTC: " + dateTimeInUTC + " with pattern: " + pattern + ". Error: " + e.getMessage() + ". Trying next pattern");
            }
        }
        return null;
    }

    public static String stringToUTC(String dateTime){
        Instant date = DateUtils.stringToDate(dateTime);
        DateFormat df = new SimpleDateFormat(DateFormats.DATE_TIME_PATTERN_UTC.getFormat());
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
