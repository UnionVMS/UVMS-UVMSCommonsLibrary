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

package eu.europa.ec.fisheries.uvms.commons.message2.impl;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class JAXBUtils {

    private static Map<String, JAXBContext> contexts = new HashMap<>();

    private JAXBUtils() {}

    public static <T> String marshallJaxBObjectToString(final T data, String encoding, boolean formatted, NamespacePrefixMapper prefixMapper) throws JAXBException {
        JAXBContext jaxbContext = contexts.get(data.getClass().getName());
        if (jaxbContext == null) {
            jaxbContext = JAXBContext.newInstance(data.getClass());
            contexts.put(data.getClass().getName(), jaxbContext);
        }

        Marshaller marshaller = jaxbContext.createMarshaller();
        if(encoding != null && encoding.length() > 0){
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
        }
        if (formatted){
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        }
        if (prefixMapper != null){
            marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", prefixMapper);
        }
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        StringWriter sw = new StringWriter();
        marshaller.marshal(data, sw);
        return sw.toString();
    }
        /**
         * Marshalls a JAXB Object to a XML String representation.
         *
         * @param <T>
         * @param encoding
         * @param formatted
         * @param data @return @throws
         */
    public static <T> String marshallJaxBObjectToString(final T data, String encoding, boolean formatted) throws JAXBException {
        return marshallJaxBObjectToString(data, encoding, formatted, null);
    }

    /**
     * Marshalls a JAXB Object to a XML String representation.
     *
     * @param <T>
     * @param data @return @throws
     */
    public static <T> String marshallJaxBObjectToString(final T data) throws JAXBException {
        return marshallJaxBObjectToString(data, null, true);
    }

    /**
     * Unmarshalls a textMessage to the desired Object. The object must be the root
     * object of the unmarshalled message!
     *
     * @param <R>
     * @param textMessage @param clazz @return @throws
     */
    public static <R> R unMarshallMessage(String textMessage, Class clazz) throws JAXBException {
        return unMarshallMessage(textMessage, clazz, null);
    }

    /**
     * Unmarshalls a textMessage to the desired Object. The object must be the root
     * object of the unmarshalled message and validate against an xsd schema!
     *
     * @param <R>
     * @param textMessage @param clazz @return @throws
     */
    public static <R> R unMarshallMessage(String textMessage, Class clazz, Schema schema) throws JAXBException {
        if (textMessage != null){
            textMessage = textMessage.trim();
        }
        JAXBContext jc = contexts.get(clazz.getName());
        if (jc == null) {
            jc = JAXBContext.newInstance(clazz);
            contexts.put(clazz.getName(), jc);
        }
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        if (schema != null){
            unmarshaller.setSchema(schema);
        }
        StringReader sr = new StringReader(textMessage);
        StreamSource source = new StreamSource(sr);
        return  (R) unmarshaller.unmarshal(source);
    }

}