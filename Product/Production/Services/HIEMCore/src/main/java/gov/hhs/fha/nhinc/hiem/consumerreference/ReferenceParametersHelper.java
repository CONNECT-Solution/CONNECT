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
package gov.hhs.fha.nhinc.hiem.consumerreference;

import gov.hhs.fha.nhinc.xmlCommon.XpathHelper;

import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * @author rayj
 */
public class ReferenceParametersHelper {

    private static Log log = LogFactory.getLog(ReferenceParametersHelper.class);

    public SoapMessageElements createReferenceParameterElementsFromSubscriptionReference(String subscriptionReferenceXml)
            throws XPathExpressionException {
        log.debug("extracting reference parameters from subscription reference [" + subscriptionReferenceXml + "]");
        String xpathQuery = "//*[local-name()='ReferenceParameters']";
        return createReferenceParameterElementsFromEndpointReference(subscriptionReferenceXml, xpathQuery);
    }

    public SoapMessageElements createReferenceParameterElementsFromConsumerReference(String subscribeXml)
            throws XPathExpressionException {
        log.debug("extracting reference parameters from subscribe [" + subscribeXml + "]");
        String xpathQuery = "//*[local-name()='ReferenceParameters']";
        return createReferenceParameterElementsFromEndpointReference(subscribeXml, xpathQuery);
    }
    
    public static String getWsAddressingTo(SoapMessageElements referenceParametersElements) {
        
        String wsAddressingTo = null;
        for (Element referenceParametersElement : referenceParametersElements.getElements()) {

            String nodeName = referenceParametersElement.getLocalName().toLowerCase();
                   
            if (nodeName.equals("to")) {
                String nodeValue = referenceParametersElement.getNodeValue();
                if (nodeValue == null && referenceParametersElement.getFirstChild() != null) {
                    nodeValue = referenceParametersElement.getFirstChild().getNodeValue();
                }
                                
                wsAddressingTo = nodeValue;
                break;
            }
        }
        
        return wsAddressingTo;
        
    }
    
    public static String getSubscriptionId(SoapMessageElements referenceParametersElements) {
        String SubscriptionId = null;
        for (Element referenceParametersElement : referenceParametersElements.getElements()) {

            String nodeName = referenceParametersElement.getLocalName().toLowerCase();
                   
            if (nodeName.equals("subscriptionid")) {
                String nodeValue = referenceParametersElement.getNodeValue();
                log.debug("nodeValue SubscriptionId :"+nodeValue);
                if (nodeValue == null && referenceParametersElement.getFirstChild() != null) {
                    nodeValue = referenceParametersElement.getFirstChild().getNodeValue(); 
                }
           SubscriptionId = nodeValue;
           log.debug("SubscriptionId: "+SubscriptionId);
           break;
        }
    }
    return SubscriptionId;
    }

    private SoapMessageElements createReferenceParameterElementsFromEndpointReference(String xml, String xpathQuery)
            throws XPathExpressionException {
        log.debug("extracting reference parameters from xml [" + xml + "]");
        log.debug("get endpoint reference using xpath:" + xpathQuery);
        Element endpointReference = (Element) XpathHelper.performXpathQuery(xml, xpathQuery);
        return createReferenceParameterElementsFromEndpointReference(endpointReference);
    }

    private SoapMessageElements createReferenceParameterElementsFromEndpointReference(Element endpointReference)
            throws XPathExpressionException {
        SoapMessageElements referenceParametersElements = new SoapMessageElements();
        if (endpointReference != null) {
            for (int i = 0; i < endpointReference.getChildNodes().getLength(); i++) {
                Node childNode = endpointReference.getChildNodes().item(i);
                log.debug("processing child node [" + childNode.getNodeName() + "]");
                if (childNode instanceof Element) {
                    log.debug("adding to reference parameters");
                    Element childElement = (Element) childNode;
                    referenceParametersElements.getElements().add(childElement);
                } else {
                    log.debug("not an element - skipping");
                }
            }
        }
        log.debug("referenceParametersElements.getElements().size() = "
                + referenceParametersElements.getElements().size());
        return referenceParametersElements;
    }
}
