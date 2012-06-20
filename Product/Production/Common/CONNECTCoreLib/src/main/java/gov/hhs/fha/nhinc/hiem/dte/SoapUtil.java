/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.hiem.dte;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.Headers;
import com.sun.xml.ws.developer.WSBindingProvider;

import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersElements;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;

/**
 *
 *
 * @author Neil Webb
 */
public class SoapUtil {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(SoapUtil.class);

    /**
     * @deprecated ("just need to rename this to extractSoapHeader")
     * @param context
     * @param attributeName
     */
    @Deprecated
    public void extractReferenceParameters(SOAPMessageContext context, String attributeName) {
        log.debug("******** In handleMessage() *************");
        SOAPMessage soapMessage = null;
        String soapMessageText = null;
        try {
            if (context != null) {
                log.debug("******** Context was not null *************");
                soapMessage = context.getMessage();
                log.debug("******** After getMessage *************");

                if (soapMessage != null) {
                    log.debug("******** Attempting to write out SOAP message *************");
                    try {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        soapMessage.writeTo(bos);
                        soapMessageText = bos.toString();
                        log.debug("Captured soap message: " + soapMessageText);
                    } catch (Throwable t) {
                        log.debug("Exception writing out the message");
                        t.printStackTrace();
                    }
                } else {
                    log.debug("SOAPMessage was null");
                }
            } else {
                log.debug("SOAPMessageContext was null.");
            }
        } catch (Throwable t) {
            log.debug("Error logging the SOAP message: " + t.getMessage());
            t.printStackTrace();
        }
        if (soapMessage != null) {
            @SuppressWarnings("unchecked")
            javax.servlet.http.HttpServletRequest servletRequest = (javax.servlet.http.HttpServletRequest) context
                    .get(MessageContext.SERVLET_REQUEST);
            servletRequest.setAttribute(attributeName, soapMessage);
        }
    }

    private SOAPMessage extractSoapMessageObject(WebServiceContext context, String attributeName) {
        SOAPMessage soapMessage = null;
        if (context != null) {
            MessageContext msgContext = context.getMessageContext();
            if (msgContext != null) {
                javax.servlet.http.HttpServletRequest servletRequest = (javax.servlet.http.HttpServletRequest) msgContext
                        .get(MessageContext.SERVLET_REQUEST);
                soapMessage = (SOAPMessage) servletRequest.getAttribute(attributeName);
            }
        }
        return soapMessage;
    }

    public String extractSoapMessage(WebServiceContext context, String attributeName) {
        String extractedMessage = null;

        SOAPMessage soapMessage = extractSoapMessageObject(context, attributeName);
        if (soapMessage != null) {
            log.debug("******** Attempting to write out SOAP message *************");
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                soapMessage.writeTo(bos);
                extractedMessage = new String(bos.toByteArray());
                log.debug("Extracted soap message: " + extractedMessage);
            } catch (Throwable t) {
                log.debug("Exception writing out the message");
                t.printStackTrace();
            }
        } else {
            log.debug("SOAPMessage was null");
        }
        return extractedMessage;
    }

    public Element extractSoapMessageElement(WebServiceContext context, String attributeName) {
        String extractedMessage = extractSoapMessage(context, attributeName);

        Element messageElement = null;
        try {
            messageElement = XmlUtility.convertXmlToElement(extractedMessage);
        } catch (Exception ex) {
            log.error("failed to convert soap xml to element", ex);
        }
        return messageElement;
    }

    private Element findElement(NodeList nodes, String elementName) {
        Element e = null;
        if ((nodes != null) && (elementName != null)) {
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if ((node != null) && (node instanceof Element)) {
                    Element tmpElement = (Element) node;
                    String tmpElementName = tmpElement.getLocalName();
                    if (elementName.equalsIgnoreCase(tmpElementName)) {
                        e = tmpElement;
                    } else if ((node.getChildNodes() != null) && (node.getChildNodes().getLength() > 0)) {
                        e = findElement(node.getChildNodes(), elementName);
                    }
                    if (e != null) {
                        break;
                    }
                }
            }
        }
        return e;
    }

    public Element extractFirstElement(WebServiceContext context, String attributeName, String elementName) {
        Element firstElement = null;
        SOAPMessage soapMessage = extractSoapMessageObject(context, attributeName);
        if (soapMessage != null) {
            try {
                // Dump body
                SOAPBody soapBody = soapMessage.getSOAPBody();
                // Dump namespaces
                Iterator namespaceIter = soapBody.getVisibleNamespacePrefixes();
                if (namespaceIter != null) {
                    while (namespaceIter.hasNext()) {
                        String namespacePrefix = (String) namespaceIter.next();
                        String namespace = soapBody.getNamespaceURI(namespacePrefix);
                        log.debug("Namespace - " + namespacePrefix + ":" + namespace);
                    }
                }

                firstElement = findElement(soapBody.getChildNodes(), elementName);

            } catch (SOAPException ex) {
                log.error("SOAPException reading soap body: " + ex.getMessage(), ex);
            }

        }
        return firstElement;
    }

    /**
     * @param WSBindingProvider port
     * @param ReferenceParametersElements referenceParametersElements
     */
    public SOAPHeader extractSoapHeader(WebServiceContext context, String attributeName) throws SOAPException {
        SOAPHeader header = null;
        SOAPMessage message = extractSoapMessageObject(context, attributeName);
        if (message != null) {
            header = message.getSOAPHeader();
        }
        return header;
    }

    public void attachReferenceParameterElements(WSBindingProvider port,
            ReferenceParametersElements referenceParametersElements) {
        attachReferenceParameterElements(port, referenceParametersElements, null);
    }

    /**
     * @param WSBindingProvider port
     * @param ReferenceParametersElements referenceParametersElements
     * @param List<Header> headers
     */
    public void attachReferenceParameterElements(WSBindingProvider port,
            ReferenceParametersElements referenceParametersElements, List<Header> headers) {
        List<Header> newHeaders = new ArrayList<Header>();
        if (referenceParametersElements != null) {
            for (Element referenceParametersElement : referenceParametersElements.getElements()) {

                if (validateHeader(referenceParametersElement.getNodeName())) {
                    log.debug("attaching header " + referenceParametersElement.getNodeName());
                    Header header = Headers.create(referenceParametersElement);
                    newHeaders.add(header);
                }
            }
        }
        
        if (headers != null) {
            // This introduces a chance of duplicate elements
            newHeaders.addAll(headers);
            // Casting to LinkedHashSet to remove duplicates and retain order
            // then cast back to List<Headers>
            Set<Header> oSet = new LinkedHashSet<Header>(newHeaders);
            newHeaders.clear();
            newHeaders.addAll(oSet);
        }
        port.setOutboundHeaders(newHeaders);
        
    }

    private boolean validateHeader(String headerName) {
        boolean result = true;
        String testCondition = headerName.toLowerCase();

        if ((testCondition.equals("to")) || testCondition.equals("replyto") || testCondition.equals("action")
                || testCondition.equals("messageid")) {
            result = false;
            log.warn("Invalid header: " + headerName);
        }
        return result;
    }
}
