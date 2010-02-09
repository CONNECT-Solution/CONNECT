/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.xmlCommon;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.io.ByteArrayInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.apache.commons.logging.Log;
import org.w3c.dom.*;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSException;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSParser;

/**
 *
 * @author rayj
 */
public class XmlUtility {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(XmlUtility.class);

    public static String getNodeValue(Node node) {
        String value = null;
        if (node != null) {
            value = node.getNodeValue();
            if (value == null) {
                value = node.getTextContent();
            }
        }
        return value;
    }

    public static String serializeElement(Element element) throws LSException, IllegalAccessException, DOMException, InstantiationException, ClassNotFoundException, ClassCastException {
        String serializedElement = null;
        if (element != null) {
            System.setProperty(DOMImplementationRegistry.PROPERTY, "org.apache.xerces.dom.DOMImplementationSourceImpl");
            DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
            DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
            LSSerializer writer = impl.createLSSerializer();
            serializedElement = writer.writeToString(element);
        }
        return serializedElement;
    }

    public static String formatElementForLogging(String elementName, Element element) {
        String elementNamePart = null;
        String elementValuePart = null;

        elementValuePart = serializeElementIgnoreFaults(element);

        if (NullChecker.isNotNullish(elementName)) {
            elementNamePart = elementName;
        } else if (element != null) {
            elementNamePart = element.getLocalName();
        } else {
            elementNamePart = "element";
        }

        String message = "{" + elementNamePart + "}=[" + elementValuePart + "]";
        return message;
    }

    public static String serializeElementIgnoreFaults(Element element) {
        String serializedElement = null;
        try {
            serializedElement = serializeElement(element);
        } catch (Exception ex) {
            Logger.getLogger(XmlUtility.class.getName()).log(Level.SEVERE, null, ex);
            serializedElement = "???";
        }
        return serializedElement;

    }

    public static String serializeNode(Node node) throws LSException, IllegalAccessException, DOMException, InstantiationException, ClassNotFoundException, ClassCastException {
        String serializedElement = null;
        if (node != null) {
            System.setProperty(DOMImplementationRegistry.PROPERTY, "org.apache.xerces.dom.DOMImplementationSourceImpl");
            DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
            DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
            LSSerializer writer = impl.createLSSerializer();
            serializedElement = writer.writeToString(node);
        }
        return serializedElement;
    }

    public static String serializeNodeIgnoreFaults(Node node) {
        String serializedNode = null;
        try {
            serializedNode = serializeNode(node);
        } catch (Exception ex) {
            Logger.getLogger(XmlUtility.class.getName()).log(Level.SEVERE, null, ex);
            serializedNode = "???";
        }
        return serializedNode;

    }

    /**
     * @deprecated Use {@link XpathHelper} instead
     * @param sourceXml
     * @param xpathQuery
     * @return
     * @throws javax.xml.xpath.XPathExpressionException
     */
    public static Node performXpathQuery(String sourceXml, String xpathQuery) throws XPathExpressionException {
        return XpathHelper.performXpathQuery(sourceXml, xpathQuery);
    }

    /**
     * @deprecated Use {@link XpathHelper} instead
     * @param sourceXml
     * @param xpathQuery
     * @param namespaceContext
     * @return
     * @throws javax.xml.xpath.XPathExpressionException
     */
    @Deprecated
    public static Node performXpathQuery(String sourceXml, String xpathQuery, NamespaceContext namespaceContext) throws XPathExpressionException {
        return XpathHelper.performXpathQuery(sourceXml, xpathQuery, namespaceContext);
    }

    /**
     * @deprecated Use {@link XpathHelper} instead
     * @param sourceElement
     * @param xpathQuery
     * @return
     * @throws javax.xml.xpath.XPathExpressionException
     */
    public static Node performXpathQuery(Element sourceElement, String xpathQuery) throws XPathExpressionException {
        return XpathHelper.performXpathQuery(sourceElement, xpathQuery);
    }

    /**
     * @deprecated Use {@link XpathHelper} instead
     * @param sourceElement
     * @param xpathQuery
     * @param namespaceContext
     * @return
     * @throws javax.xml.xpath.XPathExpressionException
     */
    public static Node performXpathQuery(Element sourceElement, String xpathQuery, NamespaceContext namespaceContext) throws XPathExpressionException {
        return XpathHelper.performXpathQuery(sourceElement, xpathQuery, namespaceContext);
    }

    public static Element convertXmlToElement(String xml) throws Exception {
        Element element = null;

        if (NullChecker.isNotNullish(xml)) {
            if (XmlUtfHelper.isUtf16(xml)) {
                try {
                    element = convertXmlToElementWorker(xml);
                } catch (Exception ex) {
                    // Exception may be due to the encoding of the message being incorrect.
                    // retry using UTF-8
                    log.warn("failed to perform xml to element - retrying with UTF-8");
                    xml = XmlUtfHelper.convertToUtf8(xml);
                    element = convertXmlToElementWorker(xml);
                }
            } else {
                element = convertXmlToElementWorker(xml);
            }
        }
        return element;
    }

    private static Element convertXmlToElementWorker(String xml) throws Exception {
        Element element = null;

        System.setProperty(DOMImplementationRegistry.PROPERTY, "org.apache.xerces.dom.DOMImplementationSourceImpl");
        DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
        DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
        LSParser parser = impl.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, "http://www.w3.org/2001/XMLSchema");
        LSInput lsInput = impl.createLSInput();
        lsInput.setStringData(xml);
        Document doc = parser.parse(lsInput);
        if (doc != null) {
            element = doc.getDocumentElement();
        }
        return element;
    }

    public static Element getSingleChildElement(Element element, String namespaceURI, String localName) {
        Element childElement = null;
        if ((element != null) && (NullChecker.isNotNullish(namespaceURI) && (NullChecker.isNotNullish(localName)))) {
            NodeList result = element.getElementsByTagNameNS(namespaceURI, localName);
            if ((result != null) && (result.getLength() >= 1)) {
                childElement = (Element) result.item(0);
            }
        }
        return childElement;
    }
}
