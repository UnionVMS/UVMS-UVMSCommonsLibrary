package eu.europa.ec.fisheries.uvms.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

public abstract class AbstractJAXBMarshaller {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractJAXBMarshaller.class);

    /**
     * Marshalls a JAXB Object to a XML String representation.
     *
     * @param <T>
     * @param data
     * @return
     * @throws
     */
    protected static <T> String marshallJaxBObjectToString(final T data) throws JAXBException {

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
    protected static <R> R unmarshallTextMessage(final TextMessage textMessage, final Class clazz) throws JAXBException, JMSException {
        JAXBContext jc = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        StringReader sr = new StringReader(textMessage.getText());
        return (R) unmarshaller.unmarshal(sr);
    }

}

