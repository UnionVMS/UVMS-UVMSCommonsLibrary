/*
 Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
 © European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
 redistribute it and/or modify it under the terms of the GNU General Public License as published by the
 Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
 the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
 copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.commons.date;

/**
 * Created by osdjup on 2016-10-06.
 */
public enum DateFormats {

    FORMAT(Strings.FORMAT),
    FORMAT_2(Strings.FORMAT_2),
    DATE_TIME_PATTERN(Strings.DATE_TIME_PATTERN),
    DATE_TIME_PATTERN_WITH_T(Strings.DATE_TIME_PATTERN_WITH_T),
    DATE_TIME_PATTERN_UTC(Strings.DATE_TIME_PATTERN_UTC),
    DATE_TIME_PATTERN_UTC_WITH_T(Strings.DATE_TIME_PATTERN_UTC_WITH_T);

    String format;

    DateFormats(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    /**
     * These constants can be used as values in annotations, the {@link #getFormat()} can not
     * <p>
     * Example: @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = {@value eu.europa.ec.fisheries.uvms.commons.date.DateFormats.Strings#DATE_TIME_PATTERN_UTC})
     */
    public static class Strings {
        public final static String FORMAT = "yyyy-MM-dd HH:mm:ss Z";
        public final static String FORMAT_2 = "EEE MMM dd HH:mm:ss z yyyy";
        public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss X";
        public final static String DATE_TIME_PATTERN_WITH_T = "yyyy-MM-dd'T'HH:mm:ss X";
        public final static String DATE_TIME_PATTERN_UTC = "yyyy-MM-dd HH:mm:ss";
        public final static String DATE_TIME_PATTERN_UTC_WITH_T = "yyyy-MM-dd'T'HH:mm:ss";

    }
}
