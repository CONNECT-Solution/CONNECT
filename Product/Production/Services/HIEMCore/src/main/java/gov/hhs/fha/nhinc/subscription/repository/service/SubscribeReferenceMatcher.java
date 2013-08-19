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
package gov.hhs.fha.nhinc.subscription.repository.service;

import gov.hhs.fha.nhinc.hiem.dte.marshallers.EndpointReferenceMarshaller;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.SubscriptionReferenceMarshaller;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;

import org.w3._2005._08.addressing.AttributedURIType;
import org.w3._2005._08.addressing.EndpointReferenceType;
import org.w3._2005._08.addressing.ReferenceParametersType;
import org.w3c.dom.Element;
import org.apache.log4j.Logger;

/**
 * 
 * @author rayj
 */
public class SubscribeReferenceMatcher {

    private static final Logger LOG = Logger.getLogger(SubscribeReferenceMatcher.class);

    public boolean isSubscriptionReferenceMatch(String prototypeSubscriptionReferenceXml,
            String possibleMatchSubscriptionReferenceXml) {
        boolean result = false;
        LOG.debug("isSubscriptionReferenceMatch (xml)");
        Element element1 = null;
        Element element2 = null;
        try {
            LOG.debug("parsing prototypeSubscriptionReferenceXml: [" + prototypeSubscriptionReferenceXml + "]");
            element1 = XmlUtility.convertXmlToElement(prototypeSubscriptionReferenceXml);
            LOG.debug("parsing possibleMatchSubscriptionReferenceXml: [" + possibleMatchSubscriptionReferenceXml + "]");
            element2 = XmlUtility.convertXmlToElement(possibleMatchSubscriptionReferenceXml);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Malformed xml while processing isSubscriptionReferenceMatch", ex);
        }

        result = isSubscriptionReferenceMatch(element1, element2);
        return result;
    }

    public boolean isSubscriptionReferenceMatch(Element prototypeSubscriptionReferenceElement,
            Element possibleMatchSubscriptionReferenceElement) {
        LOG.debug("isSubscriptionReferenceMatch");
        LOG.debug("prototypeSubscriptionReferenceElement: ["
                + XmlUtility.serializeElementIgnoreFaults(prototypeSubscriptionReferenceElement) + "]");
        LOG.debug("possibleMatchSubscriptionReferenceElement: ["
                + XmlUtility.serializeElementIgnoreFaults(possibleMatchSubscriptionReferenceElement) + "]");

        SubscriptionReferenceMarshaller marshaller = new SubscriptionReferenceMarshaller();
        EndpointReferenceType subscribeSubscriptionReference = marshaller
                .unmarshal(prototypeSubscriptionReferenceElement);
        EndpointReferenceType possibleMatchSubscriptionReference = marshaller
                .unmarshal(possibleMatchSubscriptionReferenceElement);

        return isSubscriptionReferenceMatch(subscribeSubscriptionReference, possibleMatchSubscriptionReference);
    }

    public boolean isSubscriptionReferenceMatch(EndpointReferenceType prototypeSubscriptionReference,
            EndpointReferenceType possibleMatchSubscriptionReference) {
        boolean match = true;
        LOG.debug("isSubscriptionReferenceMatch");

        try {
            EndpointReferenceMarshaller marshaller = new EndpointReferenceMarshaller();
            LOG.debug(XmlUtility.formatElementForLogging("prototypeSubscriptionReference",
                    marshaller.marshal(prototypeSubscriptionReference)));
            LOG.debug(XmlUtility.formatElementForLogging("possibleMatchSubscriptionReference",
                    marshaller.marshal(possibleMatchSubscriptionReference)));
        } catch (Exception ex) {
            LOG.debug("failed to output endpoint references");
        }

        if (prototypeSubscriptionReference == null) {
            LOG.warn("comparing subscription references, input 1 is null");
            match = false;
        } else if (possibleMatchSubscriptionReference == null) {
            LOG.warn("comparing subscription references, input 2 is null");
            match = false;
        } else {
            LOG.debug("check address");
            match = match
                    && isMatch(prototypeSubscriptionReference.getAddress(),
                            possibleMatchSubscriptionReference.getAddress());
            LOG.debug("match=" + match);
            LOG.debug("check reference parameters");
            match = match
                    && isMatch(prototypeSubscriptionReference.getReferenceParameters(),
                            possibleMatchSubscriptionReference.getReferenceParameters());
            LOG.debug("match=" + match);
        }
        return match;
    }

    private boolean isMatch(ReferenceParametersType prototype, ReferenceParametersType possibleMatch) {
        LOG.debug("begin isMatch on reference parameters");
        boolean isMatch = true;
        for (Object prototypeReferenceParameter : prototype.getAny()) {
            LOG.debug("checking reference parameter [" + prototypeReferenceParameter.toString() + "]");
            if (prototypeReferenceParameter instanceof Element) {
                Element prototypeReferenceParameterElement = (Element) prototypeReferenceParameter;
                LOG.debug("reference parameter element ["
                        + XmlUtility.serializeElementIgnoreFaults(prototypeReferenceParameterElement) + "]");
                boolean foundMatchingReferenceParameter = false;
                for (Object possibleMatchReferenceParameter : possibleMatch.getAny()) {
                    Element possibleMatchReferenceParameterElement = (Element) possibleMatchReferenceParameter;
                    LOG.debug("trying to find matching reference parameter [["
                            + XmlUtility.serializeElementIgnoreFaults(prototypeReferenceParameterElement) + "]==["
                            + XmlUtility.serializeElementIgnoreFaults(possibleMatchReferenceParameterElement) + "]");

                    LOG.debug("check node name");
                    boolean matchOnNodeName = isMatch(prototypeReferenceParameterElement.getLocalName(),
                            possibleMatchReferenceParameterElement.getLocalName());

                    LOG.debug("check node namespace");
                    boolean matchOnNamespaceUri = isMatch(prototypeReferenceParameterElement.getNamespaceURI(),
                            possibleMatchReferenceParameterElement.getNamespaceURI());

                    LOG.debug("check node value");
                    boolean matchOnNodeValue = isMatch(XmlUtility.getNodeValue(prototypeReferenceParameterElement),
                            XmlUtility.getNodeValue(possibleMatchReferenceParameterElement));

                    if (matchOnNodeName && matchOnNodeValue && matchOnNamespaceUri) {
                        LOG.debug("found match");
                        foundMatchingReferenceParameter = true;
                        break;
                    }
                }
                LOG.debug("found match=" + foundMatchingReferenceParameter);
                if (!foundMatchingReferenceParameter) {
                    isMatch = false;
                }
            }
        }
        LOG.debug("isMatch=" + isMatch);
        return isMatch;
    }

    private boolean isMatch(String prototype, String possibleMatch) {
        boolean match;

        if (prototype == null) {
            match = false;
        } else if (possibleMatch == null) {
            match = false;
        } else {
            match = prototype.trim().contentEquals(possibleMatch.trim());
        }

        LOG.debug("[" + prototype + "]==[" + possibleMatch + "] = [" + match + "]");
        return match;
    }

    private boolean isMatch(AttributedURIType prototype, AttributedURIType possibleMatch) {
        boolean match;
        if (prototype == null) {
            match = true;
        } else if (possibleMatch == null) {
            match = false;
        } else {
            match = isMatch(prototype.getValue(), possibleMatch.getValue());
            LOG.debug("[" + prototype.getValue() + "]==[" + possibleMatch.getValue() + "] = [" + match + "]");
        }
        LOG.debug("[" + prototype + "]==[" + possibleMatch + "] = [" + match + "]");

        return match;
    }
}
