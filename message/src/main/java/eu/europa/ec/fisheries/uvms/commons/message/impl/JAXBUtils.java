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
package eu.europa.ec.fisheries.uvms.commons.message.impl;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JAXBUtils {

    private static Logger LOG = LoggerFactory.getLogger(JAXBUtils.class);

    private static Map<String, JAXBContext> contexts = new HashMap<>();

    private JAXBUtils() {

    }

    /**
     * Marshalls a JAXB Object to a XML String representation.
     *
     * @param <T>
     * @param data @return @throws
     */
    public static <T> String marshallJaxBObjectToString(final T data) throws JAXBException {
        JAXBContext jaxbContext = contexts.get(data.getClass().getName());
        if (jaxbContext == null) {
            long before = System.currentTimeMillis();
            jaxbContext = JAXBContext.newInstance(data.getClass());
            contexts.put(data.getClass().getName(), jaxbContext);
            LOG.debug("Stored contexts: {}", contexts.size());
            LOG.debug("JAXBContext creation time: {}", (System.currentTimeMillis() - before));
        }
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        StringWriter sw = new StringWriter();
        marshaller.marshal(data, sw);
        long before = System.currentTimeMillis();
        String marshalled = sw.toString();
        LOG.debug("StringWriter time: {}", (System.currentTimeMillis() - before));
        return marshalled;
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
        JAXBContext jc = contexts.get(clazz.getName());
        if (jc == null) {
            long before = System.currentTimeMillis();
            jc = JAXBContext.newInstance(clazz);
            contexts.put(clazz.getName(), jc);
            LOG.debug("Stored contexts: {}", contexts.size());
            LOG.debug("JAXBContext creation time: {}", (System.currentTimeMillis() - before));
        }
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        if (schema != null){
            unmarshaller.setSchema(schema);
        }
        StringReader sr = new StringReader(textMessage);
        StreamSource source = new StreamSource(sr);
        long before = System.currentTimeMillis();
        R object = (R) unmarshaller.unmarshal(source);
        LOG.debug("Unmarshalling time: {}", (System.currentTimeMillis() - before));
        return object;
    }
}