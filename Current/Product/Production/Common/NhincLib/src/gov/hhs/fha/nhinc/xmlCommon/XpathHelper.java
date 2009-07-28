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
public class XpathHelper {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(XpathHelper.class);

    public static Node performXpathQuery(String sourceXml, String xpathQuery) throws XPathExpressionException {
        return performXpathQuery(sourceXml, xpathQuery, null);
    }

    public static Node performXpathQuery(String sourceXml, String xpathQuery, NamespaceContext namespaceContext) throws XPathExpressionException {
        javax.xml.xpath.XPathFactory factory = javax.xml.xpath.XPathFactory.newInstance();
        javax.xml.xpath.XPath xpath = factory.newXPath();

        if (namespaceContext != null) {
            xpath.setNamespaceContext(namespaceContext);
        }

        InputSource inputSource = new InputSource(new ByteArrayInputStream(sourceXml.getBytes()));

        log.debug("perform xpath query (query='" + xpathQuery + "'");
        Node result = null;
        if (XmlUtfHelper.isUtf16(sourceXml)) {
            try {
                result = (Node) xpath.evaluate(xpathQuery, inputSource, XPathConstants.NODE);
            } catch (Exception ex) {
                // Exception may be due to the encoding of the message being incorrect.
                // retry using UTF-8
                log.warn("failed to perform xpath query - retrying with UTF-8");
                sourceXml = XmlUtfHelper.convertToUtf8(sourceXml);
                result = performXpathQuery(sourceXml, xpathQuery, namespaceContext);
            }
        } else {
            result = (Node) xpath.evaluate(xpathQuery, inputSource, XPathConstants.NODE);
        }
        log.debug("xpath query complete [result?=" + result + "]");
        return result;
    }

    public static Node performXpathQuery(Element sourceElement, String xpathQuery) throws XPathExpressionException {
        return performXpathQuery(sourceElement, xpathQuery, null);
    }

    public static Node performXpathQuery(Element sourceElement, String xpathQuery, NamespaceContext namespaceContext) throws XPathExpressionException {
        javax.xml.xpath.XPathFactory factory = javax.xml.xpath.XPathFactory.newInstance();
        javax.xml.xpath.XPath xpath = factory.newXPath();

        if (namespaceContext != null) {
            xpath.setNamespaceContext(namespaceContext);
        }

        log.debug("About to perform xpath query (query='" + xpathQuery + "'");
        Node result = (Node) xpath.evaluate(xpathQuery, sourceElement, XPathConstants.NODE);
        return result;
    }
}
