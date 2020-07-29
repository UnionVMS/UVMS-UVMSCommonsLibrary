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

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static eu.europa.ec.fisheries.uvms.commons.message.impl.ThrowingFunction.sneaky;

public abstract class AbstractJAXBMarshaller {

    private static ConcurrentHashMap<Class<?>, JAXBContext> contexts = new ConcurrentHashMap<>();

    /**
     * Marshalls a JAXB Object to a XML String representation
     *
     * @param data       The object to be marshalled
     * @param properties key/value pair properties
     * @return The marshalled object in string
     * @throws javax.xml.bind.JAXBException if JAXBContext/Marshaller creation or marshalling fails
     */
    protected static String marshallToString(Object data, Map<String,Object> properties) throws JAXBException, JAXBRuntimeException {
        JAXBContext jaxbContext = contexts.computeIfAbsent(data.getClass(), sneaky(JAXBContext::newInstance));
        Marshaller marshaller = jaxbContext.createMarshaller();
        if(properties != null) {
            for(Map.Entry<String,Object> en : properties.entrySet()){
                marshaller.setProperty(en.getKey(), en.getValue());
            }
        }
        StringWriter sw = new StringWriter();
        marshaller.marshal(data, sw);
        return sw.toString();
    }

    /**
     * Unmarshalls A textMessage to the desired Object. The object must be the
     * root object of the unmarchalled message!
     *
     * @param <T>         The type to be unmarshalled to
     * @param textMessage the jms message
     * @param clazz       The type class to be unmarshalled
     * @return the unmarshalled object
     * @throws javax.xml.bind.JAXBException if JAXBContext/UnMarshaller creation or text unmarshalling fails
     */
    protected static <T> T unmarshallTo(TextMessage textMessage, Class<T> clazz) throws JAXBException, JMSException, JAXBRuntimeException {
        return unmarshallTo(textMessage.getText(), clazz);
    }

    /**
     * Unmarshalls A textMessage to the desired Object. The object must be the
     * root object of the unmarchalled message!
     *
     * @param <T>   The type to be unmarshalled to
     * @param text  the jms message
     * @param clazz The type class to be unmarshalled to
     * @return the unmarshalled object
     * @throws javax.xml.bind.JAXBException if JAXBContext/UnMarshaller creation or text unmarshalling fails
     */
    protected static <T> T unmarshallTo(String text, Class<T> clazz) throws JAXBException, JAXBRuntimeException {
        return unmarshallTo(text, clazz, null);
    }

    /**
     * Unmarshalls A textMessage to the desired Object. The object must be the
     * root object of the unmarchalled message!
     *
     * @param <T>    The type to be unmarshalled to
     * @param text   the jms message
     * @param clazz  The type class to be unmarshalled to
     * @param schema Unmarshaller schema
     * @return the unmarshalled object
     * @throws javax.xml.bind.JAXBException if JAXBContext/UnMarshaller creation or text unmarshalling fails
     */
    protected static <T> T unmarshallTo(String text, Class<T> clazz, Schema schema) throws JAXBException, JAXBRuntimeException {
        JAXBContext jc = contexts.computeIfAbsent(clazz, sneaky(JAXBContext::newInstance));
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        if (schema != null) {
            unmarshaller.setSchema(schema);
        }
        StringReader sr = new StringReader(text);
        StreamSource source = new StreamSource(sr);
        return clazz.cast(unmarshaller.unmarshal(source));
    }
}
