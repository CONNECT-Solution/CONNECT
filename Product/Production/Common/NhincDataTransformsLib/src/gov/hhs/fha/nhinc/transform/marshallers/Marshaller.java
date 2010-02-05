/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.transform.marshallers;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import org.w3c.dom.Element;

/**
 *
 * @author rayj
 */
public class Marshaller {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(Marshaller.class);

    public Element marshal(Object object, String contextPath) {
        Element element = null;
        log.debug("begin marshal");
        if (object == null) {
            log.warn("object to marshall is null");
        } else if (NullChecker.isNullish(contextPath)) {
            log.warn("no contextPath supplied");
        } else {
            try {
                log.debug("get instance of JAXBContext [contextPath='" + contextPath + "']");
                JAXBContextHandler oHandler = new JAXBContextHandler();
                JAXBContext jc = oHandler.getJAXBContext(contextPath);
                log.debug("get instance of marshaller");
                javax.xml.bind.Marshaller marshaller = jc.createMarshaller();
                StringWriter stringWriter = new StringWriter();
                log.debug("Calling marshal");
                marshaller.marshal(object, stringWriter);
                log.debug("string writer writing to string");
                String xml = stringWriter.toString();
                log.debug("Marshaled xml=[" + xml + "]");
                if (NullChecker.isNotNullish(xml)) {
                    element = XmlUtility.convertXmlToElement(xml);
                }
            } catch (Exception e) {
                //"java.security.PrivilegedActionException: java.lang.ClassNotFoundException: com.sun.xml.bind.v2.ContextFactory"
                log.error("Failed to marshall: " + e.getMessage(), e);
                element = null;
            }
        }
        return element;
    }

    public Object unmarshallJaxbElement(Element element, String contextPath) {
        Object unmarshalledObject = unmarshal(element, contextPath);
        if (unmarshalledObject instanceof JAXBElement) {
            JAXBElement jaxb = (JAXBElement) unmarshalledObject;
            unmarshalledObject = jaxb.getValue();
        }
        return unmarshalledObject;
    }

    public Object unmarshal(Element element, String contextPath) {
        Object unmarshalledObject = null;
        log.debug("begin unmarshal");

        if (element == null) {
            log.warn("element to unmarshal is null");
        } else if (contextPath == null) {
            log.warn("no contextPath supplied");
        } else {
            try {
                log.debug("desializing element");
                String serializedElement = XmlUtility.serializeElement(element);
                log.debug("serializedElement=[" + serializedElement + "]");
                log.debug("get instance of JAXBContext [contextPath='" + contextPath + "']");
                JAXBContextHandler oHandler = new JAXBContextHandler();
                JAXBContext jc = oHandler.getJAXBContext(contextPath);
                log.debug("get instance of unmarshaller");
                javax.xml.bind.Unmarshaller unmarshaller = jc.createUnmarshaller();
                log.debug("init stringReader");
                StringReader stringReader = new StringReader(serializedElement);
                log.debug("Calling unmarshal");
                unmarshalledObject = unmarshaller.unmarshal(stringReader);
                log.debug("end unmarshal");
            } catch (Exception e) {
                //"java.security.PrivilegedActionException: java.lang.ClassNotFoundException: com.sun.xml.bind.v2.ContextFactory"
                //use jaxb element
                log.error("Failed to unmarshall: " + e.getMessage(), e);
                unmarshalledObject = null;
            }
        }

        return unmarshalledObject;
    }

    public Object unmarshal(String xml, String contextPath) {
        Element element = null;
        try {
            element = XmlUtility.convertXmlToElement(xml);
        } catch (Exception ex) {
            log.warn("failed to parse xml", ex);
        }
        return unmarshal(element, contextPath);
    }
}
