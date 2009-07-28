package nhinc.wsn.jaxb;

import org.oasis_open.docs.wsn.b_2.Subscribe;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Perform marshaling and unmarshaling using JAXB
 * 
 * @author Neil Webb
 */
public class Marshaler 
{
    private static Log log = LogFactory.getLog(Marshaler.class);
    
    /**
     * Uses JXB to marshal a message to an XML string.
     * 
     * This operation will remove unused namespaces so if namespaces must be 
     * retained, the message must be captured from the soap message.
     * 
     * @param subscribe
     * @return
     */
    public String marshalSubscribe(Subscribe subscribe)
    {
        log.debug("Begin marshal subscribe");
        String sXML = "";
        
        if (subscribe != null)
        {
            try
            {
                JAXBContext jc = JAXBContext.newInstance("org.oasis_open.docs.wsn.b_2");
                Marshaller marshaller = jc.createMarshaller();
                StringWriter swXML = new StringWriter();
                log.debug("Calling marshal");
                marshaller.marshal(subscribe, swXML);
                sXML = swXML.toString();
                log.debug("Marshaled subscribe: " + sXML);
            }
            catch (Exception e)
            {
                log.error("Failed to marshall AdhocQueryResponse to XML: " + e.getMessage());
            }
        }
        
        log.debug("End marshal subscribe");
        return sXML;
    }
    
    public Subscribe unmarshalSubscribe(String subscribeXml)
    {
        log.debug("Begin unmarshal subscribe");
        Subscribe subscribe = null;
        
        if (subscribeXml != null)
        {
            try
            {
                JAXBContext jc = JAXBContext.newInstance("org.oasis_open.docs.wsn.b_2");
                Unmarshaller unmarshaller = jc.createUnmarshaller();
                StringReader srSubscribeXML = new StringReader(subscribeXml);
                log.debug("Calling unmarshal");
                subscribe = (Subscribe)unmarshaller.unmarshal(srSubscribeXML);
            }
            catch (Exception e)
            {
                log.error("Failed to marshall Subscribe to XML: " + e.getMessage());
            }
        }
        log.debug("End unmarshal subscribe");
        return subscribe;
    }
}
