/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.commons.date;

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
import java.util.TimeZone;

public class XMLDateUtils {

    final static org.slf4j.Logger LOG = LoggerFactory.getLogger(XMLDateUtils.class);

    private static final String UTC = "UTC";
    public static final String DATE_TIME_XML_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static DateTimeFormatter XML_FORMATTER = DateTimeFormat.forPattern(DATE_TIME_XML_FORMAT);

    protected XMLDateUtils() {

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

    public static Date xmlGregorianCalendarToDate(XMLGregorianCalendar xmlGregorianCalendar) {
        if (xmlGregorianCalendar != null){
            TimeZone.setDefault(TimeZone.getTimeZone(UTC));
            return new Date(xmlGregorianCalendar.toGregorianCalendar().getTimeInMillis());
        }
        return null;

    }

    public static XMLGregorianCalendar dateToXmlGregorian(Date date) {
        XMLGregorianCalendar gregCalendar = null;
        if (date != null){
            DateFormat format = new SimpleDateFormat(DATE_TIME_XML_FORMAT);
            try {
                gregCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(format.format(date));
            } catch (DatatypeConfigurationException e) {
                LOG.error(e.getMessage(), e);
            }
        }
        return gregCalendar;
    }
}
