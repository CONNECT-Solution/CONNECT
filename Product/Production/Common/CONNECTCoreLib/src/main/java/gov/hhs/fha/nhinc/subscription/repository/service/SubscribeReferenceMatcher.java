/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.subscription.repository.service;

import gov.hhs.fha.nhinc.hiem.dte.SubscriptionReferenceHelper;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.EndpointReferenceMarshaller;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.SubscriptionReferenceMarshaller;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3._2005._08.addressing.AttributedURIType;
import org.w3._2005._08.addressing.ReferenceParametersType;
import org.w3c.dom.Element;
import org.w3._2005._08.addressing.EndpointReferenceType;

/**
 *
 * @author rayj
 */
public class SubscribeReferenceMatcher {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(SubscribeReferenceMatcher.class);

    public boolean isSubscriptionReferenceMatch(String prototypeSubscriptionReferenceXml, String possibleMatchSubscriptionReferenceXml) {
        boolean result = false;
        log.debug("isSubscriptionReferenceMatch (xml)");
        Element element1 = null;
        Element element2 = null;
        try {
            log.debug("parsing prototypeSubscriptionReferenceXml: [" + prototypeSubscriptionReferenceXml + "]");
            element1 = XmlUtility.convertXmlToElement(prototypeSubscriptionReferenceXml);
            log.debug("parsing possibleMatchSubscriptionReferenceXml: [" + possibleMatchSubscriptionReferenceXml + "]");
            element2 = XmlUtility.convertXmlToElement(possibleMatchSubscriptionReferenceXml);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Malformed xml while processing isSubscriptionReferenceMatch", ex);
        }

        result = isSubscriptionReferenceMatch(element1, element2);
        return result;
    }

    public boolean isSubscriptionReferenceMatch(Element prototypeSubscriptionReferenceElement, Element possibleMatchSubscriptionReferenceElement) {
        log.debug("isSubscriptionReferenceMatch");
        log.debug("prototypeSubscriptionReferenceElement: [" + XmlUtility.serializeElementIgnoreFaults(prototypeSubscriptionReferenceElement) + "]");
        log.debug("possibleMatchSubscriptionReferenceElement: [" + XmlUtility.serializeElementIgnoreFaults(possibleMatchSubscriptionReferenceElement) + "]");

        SubscriptionReferenceMarshaller marshaller = new SubscriptionReferenceMarshaller();
        EndpointReferenceType subscribeSubscriptionReference = marshaller.unmarshal(prototypeSubscriptionReferenceElement);
        EndpointReferenceType possibleMatchSubscriptionReference = marshaller.unmarshal(possibleMatchSubscriptionReferenceElement);

        return isSubscriptionReferenceMatch(subscribeSubscriptionReference, possibleMatchSubscriptionReference);
    }

    public boolean isSubscriptionReferenceMatch(EndpointReferenceType prototypeSubscriptionReference, EndpointReferenceType possibleMatchSubscriptionReference) {
        boolean match = true;
        log.debug("isSubscriptionReferenceMatch");

        try {
            EndpointReferenceMarshaller marshaller = new EndpointReferenceMarshaller();
            log.debug(XmlUtility.formatElementForLogging("prototypeSubscriptionReference", marshaller.marshal(prototypeSubscriptionReference)));
            log.debug(XmlUtility.formatElementForLogging("possibleMatchSubscriptionReference", marshaller.marshal(possibleMatchSubscriptionReference)));
        } catch (Exception ex) {
            log.debug("failed to output endpoint references");
        }

        if (prototypeSubscriptionReference == null) {
            log.warn("comparing subscription references, input 1 is null");
            match = false;
        } else if (possibleMatchSubscriptionReference == null) {
            log.warn("comparing subscription references, input 2 is null");
            match = false;
        } else {
            log.debug("check address");
            match = match && isMatch(prototypeSubscriptionReference.getAddress(), possibleMatchSubscriptionReference.getAddress());
            log.debug("match=" + match);
            log.debug("check reference parameters");
            match = match && isMatch(prototypeSubscriptionReference.getReferenceParameters(), possibleMatchSubscriptionReference.getReferenceParameters());
            log.debug("match=" + match);
        }
        return match;
    }

    private boolean isMatch(ReferenceParametersType prototype, ReferenceParametersType possibleMatch) {
        log.debug("begin isMatch on reference parameters");
        boolean isMatch = true;
        for (Object prototypeReferenceParameter : prototype.getAny()) {
            log.debug("checking reference parameter [" + prototypeReferenceParameter.toString() + "]");
            if (prototypeReferenceParameter instanceof Element) {
                Element prototypeReferenceParameterElement = (Element) prototypeReferenceParameter;
                log.debug("reference parameter element [" + XmlUtility.serializeElementIgnoreFaults(prototypeReferenceParameterElement) + "]");
                boolean foundMatchingReferenceParameter = false;
                for (Object possibleMatchReferenceParameter : possibleMatch.getAny()) {
                    Element possibleMatchReferenceParameterElement = (Element) possibleMatchReferenceParameter;
                    log.debug("trying to find matching reference parameter [[" + XmlUtility.serializeElementIgnoreFaults(prototypeReferenceParameterElement) + "]==[" + XmlUtility.serializeElementIgnoreFaults(possibleMatchReferenceParameterElement) + "]");

                    log.debug("check node name");
                    boolean matchOnNodeName = isMatch(prototypeReferenceParameterElement.getLocalName(), possibleMatchReferenceParameterElement.getLocalName());

                    log.debug("check node namespace");
                    boolean matchOnNamespaceUri = isMatch(prototypeReferenceParameterElement.getNamespaceURI(), possibleMatchReferenceParameterElement.getNamespaceURI());

                    log.debug("check node value");
                    boolean matchOnNodeValue = isMatch(XmlUtility.getNodeValue(prototypeReferenceParameterElement), XmlUtility.getNodeValue(possibleMatchReferenceParameterElement));

                    if (matchOnNodeName && matchOnNodeValue && matchOnNamespaceUri) {
                        log.debug("found match");
                        foundMatchingReferenceParameter = true;
                        break;
                    }
                }
                log.debug("found match=" + foundMatchingReferenceParameter);
                if (!foundMatchingReferenceParameter) {
                    isMatch = false;
                }
            }
        }
        log.debug("isMatch=" + isMatch);
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

        log.debug("[" + prototype + "]==[" + possibleMatch + "] = [" + match + "]");
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
            log.debug("[" + prototype.getValue() + "]==[" + possibleMatch.getValue() + "] = [" + match + "]");
        }
        log.debug("[" + prototype + "]==[" + possibleMatch + "] = [" + match + "]");

        return match;
    }
}
