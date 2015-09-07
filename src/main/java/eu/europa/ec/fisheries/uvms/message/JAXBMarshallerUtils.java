package eu.europa.ec.fisheries.uvms.message;

import java.io.StringReader;
import java.io.StringWriter;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JAXBMarshallerUtils {

    private static Logger LOG = LoggerFactory.getLogger(JAXBMarshallerUtils.class);

    /**
     * Marshalls a JAXB Object to a XML String representation
     *
     * @param <T>
     * @param data
     * @return
     * @throws
     */
    public static <T> String marshallJaxBObjectToString(final T data) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(data.getClass());
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        StringWriter sw = new StringWriter();
        marshaller.marshal(data, sw);
        return sw.toString();
    }

    /**
     * Unmarshalls A textMessage to the desired Object. The object must be the
     * root object of the unmarshalled message!
     *
     * @param <R>
     * @param textMessage
     * @param clazz
     * @return
     * @throws
     */
    public static <R> R unmarshallTextMessage(final TextMessage textMessage, final Class clazz) throws JAXBException, JMSException {
        JAXBContext jc = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        StringReader sr = new StringReader(textMessage.getText());
        return (R) unmarshaller.unmarshal(sr);
    }

}

