/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.uvms.commons.message.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.Enumeration;
import java.util.Map;

public class MappedDiagnosticContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(MappedDiagnosticContext.class);

    public static final String MESSAGE_PROPERTY_PREFIX_MDC = "MDC_";

    public static void addThreadMappedDiagnosticContextToMessageProperties(TextMessage messageToAddMessageProperties) {
        Map<String, String> mappedDiagnosticContextEntries = MDC.getCopyOfContextMap();
        if (mappedDiagnosticContextEntries == null) {
            return;
        }
        for (Map.Entry<String, String> mappedDiagnosticContextEntry : mappedDiagnosticContextEntries.entrySet()) {
            String key = MESSAGE_PROPERTY_PREFIX_MDC + mappedDiagnosticContextEntry.getKey();
            String traceId = mappedDiagnosticContextEntry.getValue();
            try {
                messageToAddMessageProperties.setStringProperty(key, traceId);
            } catch (Exception e) {
                LOGGER.warn("Unable to set mapped diagnostic context property key: " + key + " value: " + traceId + "  as message property. Reason: " + e.getMessage(),e);
            }
        }
    }

    public static void addMessagePropertiesToThreadMappedDiagnosticContext(Message message) {
        try {
            Enumeration<String> enumeration = (Enumeration<String>) message.getPropertyNames();
            if (enumeration == null) {
                return;
            }
            while (enumeration.hasMoreElements()) {
                String prefixedPropertyKeyName = enumeration.nextElement();
                if (prefixedPropertyKeyName.startsWith(MESSAGE_PROPERTY_PREFIX_MDC)) {
                    String propertyKeyName = prefixedPropertyKeyName.substring(4);
                    String propertyKeyValue = message.getStringProperty(prefixedPropertyKeyName);
                    MDC.put(propertyKeyName, propertyKeyValue);
                }
            }
        } catch (Exception e) {
            LOGGER.warn("Unable to add message properties to thread mapped diagnostic context. Reason: " + e.getMessage(),e);
        }
    }
}
