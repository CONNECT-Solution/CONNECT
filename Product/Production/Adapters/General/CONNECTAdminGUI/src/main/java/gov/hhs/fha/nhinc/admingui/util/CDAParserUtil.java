
package gov.hhs.fha.nhinc.admingui.util;

import javax.xml.bind.JAXBIntrospector;

import java.io.InputStream;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.hl7.v3.ObjectFactory;
import org.hl7.v3.POCDMT000040ClinicalDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mpnguyen
 *
 */
public class CDAParserUtil {
    private final static Logger logger = LoggerFactory.getLogger(CDAParserUtil.class);

    /**
     * Convert xml into object
     *
     * @param <T>
     *
     * @param cdaInputStream
     * @return
     * @return
     */
    public static <T> T convertXMLToCDA(InputStream cdaInputStream, Class<T> responseClass) {
        try {
            logger.debug("Convert CDA XML into Java Obj");
            JAXBContext jContext = JAXBContext.newInstance(responseClass);
            Unmarshaller unmarshaller = jContext.createUnmarshaller();
            return (T) JAXBIntrospector.getValue(unmarshaller.unmarshal(cdaInputStream));
        } catch (JAXBException e) {
            logger.error("Unable to parsing xml into object due to {}", e);
            return null;
        }

    }
    /**
     * @param ccDAInputStream
     * @return
     */
    public static String convertCDAToXML(POCDMT000040ClinicalDocument cdaDoc) {
        try {
            logger.debug("Convert CDA Java Obj to XML");
            ObjectFactory factory = new ObjectFactory();
            JAXBContext jContext = JAXBContext.newInstance(POCDMT000040ClinicalDocument.class);
            Marshaller marshaller = jContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

            StringWriter strWrite = new StringWriter();
            marshaller.marshal(factory.createClinicalDocument(cdaDoc), strWrite);
            return strWrite.toString();

        } catch (JAXBException e) {
            logger.error("Unable to convert cdaObj to xml due to {}", e);
        }
        return null;
    }

}