/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package gov.hhs.fha.nhinc.xmlCommon;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.io.StringWriter;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSException;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSParser;
import org.w3c.dom.ls.LSSerializer;

/**
 *
 * @author rayj
 */
public class XmlUtility {

    private static final Logger LOG = LoggerFactory.getLogger(XmlUtility.class);

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

    protected static DOMImplementationLS getDOMImplementationLS(Node node) {
        Document document = node.getOwnerDocument();
        return (DOMImplementationLS) document.getImplementation();
    }

    public static String serializeElement(Element element)
        throws TransformerFactoryConfigurationError, TransformerException {
        String serializedElement = null;
        if (element != null) {
            StringWriter output = new StringWriter();

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(element), new StreamResult(output));

            serializedElement = output.toString();
        }
        return serializedElement;
    }

    public static String serializeNode(Node node) throws LSException, IllegalAccessException, DOMException,
        InstantiationException, ClassNotFoundException, ClassCastException {
        String serializedElement = null;
        if (node != null) {
            DOMImplementationLS impl = getDOMImplementationLS(node);
            LSSerializer writer = impl.createLSSerializer();
            serializedElement = writer.writeToString(node);
        }
        return serializedElement;
    }

    /**
     * @deprecated Use {@link XpathHelper} instead
     * @param sourceXml
     * @param xpathQuery
     * @return
     * @throws javax.xml.xpath.XPathExpressionException
     */
    @Deprecated
    public static Node performXpathQuery(String sourceXml, String xpathQuery) throws XPathExpressionException {
        return XpathHelper.performXpathQuery(sourceXml, xpathQuery);
    }

    /**
     * @deprecated Use {@link XpathHelper} instead
     * @param sourceElement
     * @param xpathQuery
     * @return
     * @throws javax.xml.xpath.XPathExpressionException
     */
    @Deprecated
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
    @Deprecated
    public static Node performXpathQuery(Element sourceElement, String xpathQuery, NamespaceContext namespaceContext)
        throws XPathExpressionException {
        return XpathHelper.performXpathQuery(sourceElement, xpathQuery, namespaceContext);
    }

    public static Element convertXmlToElement(String xml) throws Exception {
        Element element = null;

        if (NullChecker.isNotNullish(xml)) {
            if (XmlUtfHelper.isUtf16(xml)) {
                try {
                    element = convertXmlToElementWorker(xml);
                } catch (Exception e) {
                    // Exception may be due to the encoding of the message being incorrect.
                    // retry using UTF-8
                    LOG.warn("Failed to convert xml to element, retrying with UTF-8: {}", e.getLocalizedMessage());
                    LOG.trace("Failed to convert xml to element, retrying with UTF-8: {}", e.getLocalizedMessage(), e);
                    xml = XmlUtfHelper.convertToUtf8(xml);
                    element = convertXmlToElementWorker(xml);
                }
            } else {
                element = convertXmlToElementWorker(xml);
            }
        }
        return element;
    }

    private static Element initializeElement() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document;
        Element docElement = null;

        try {
            builder = factory.newDocumentBuilder();
            document = builder.newDocument();
            docElement = document.createElement("init");
        } catch (ParserConfigurationException e) {
            LOG.error("Error creating dom document builder", e);
        }

        return docElement;
    }

    private static Element convertXmlToElementWorker(String xml) throws Exception {
        Element element = initializeElement();
        if (element != null) {
            DOMImplementationLS impl = getDOMImplementationLS(element);

            LSParser parser = impl.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS,
                "http://www.w3.org/2001/XMLSchema");
            LSInput lsInput = impl.createLSInput();
            lsInput.setStringData(xml);
            Document doc = parser.parse(lsInput);
            if (doc != null) {
                element = doc.getDocumentElement();
            }
        }
        return element;
    }

    public static Element getSingleChildElement(Element element, String namespaceURI, String localName) {
        Element childElement = null;
        if (element != null && NullChecker.isNotNullish(namespaceURI) && NullChecker.isNotNullish(localName)) {
            NodeList result = element.getElementsByTagNameNS(namespaceURI, localName);
            if (result != null && result.getLength() >= 1) {
                childElement = (Element) result.item(0);
            }
        }
        return childElement;
    }
}
