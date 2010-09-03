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

import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersElements;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import gov.hhs.fha.nhinc.xmlCommon.XpathHelper;
import org.xml.sax.InputSource;
import java.io.ByteArrayInputStream;
import org.w3c.dom.Element;

/**
 *
 * @author rayj
 */
public class SubscriptionIdHelper {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(SubscriptionIdHelper.class);

    public static String extractSubscriptionIdFromSubscriptionReferenceXml(String subscriptionReferenceXml) {
        log.debug("Begin attemptSubscriptionIdExtract");
        String subscriptionId = null;
        if (subscriptionReferenceXml != null) {
            try {
                log.debug("Attempting  to extract subscription id from subscription reference xml: " + subscriptionReferenceXml);

                Element subscriptionIdElement = (Element) XpathHelper.performXpathQuery(subscriptionReferenceXml, "//*[local-name()='ReferenceParameters']/*[local-name()='SubscriptionId']");
                subscriptionId = XmlUtility.getNodeValue(subscriptionIdElement);


                if (NullChecker.isNullish(subscriptionId)) {
                    subscriptionIdElement = (Element) XpathHelper.performXpathQuery(subscriptionReferenceXml, "/*[local-name()='EndpointReference']/*[local-name()='ReferenceParameters']/*[local-name()='SubscriptionId']");
                }

                if (subscriptionId != null) {
                    subscriptionId = subscriptionId.trim();
                }
                log.debug("The value for subscription id was: " + ((subscriptionId == null) ? "null" : subscriptionId));
            } catch (Throwable t) {
                log.error("Error looking up subscription id: " + t.getMessage(), t);
            }
        }
        log.debug("End attemptSubscriptionIdExtractXpath - Subscription id='" + subscriptionId + "'");

        return subscriptionId;
    }

    //todo: check namespace
    //todo: move "subscriptionid" magic definition somewhere central - allow for the implementation of subscription id to change without having to trace changes
    public static String extractSubscriptionIdFromReferenceParametersElements(ReferenceParametersElements referenceParametersElements) {
        log.debug("Begin extractSubscriptionIdFromReferenceParametersElements");
        String subscriptionId = null;
        if (referenceParametersElements != null) {
            log.debug("looking for subscription id");
            for (Element consumerReferenceElement : referenceParametersElements.getElements()) {
                if (consumerReferenceElement.getLocalName().contentEquals("SubscriptionId")) {
                    log.debug("subscriptionId element: " + XmlUtility.formatElementForLogging(null, consumerReferenceElement));
                    subscriptionId = XmlUtility.getNodeValue(consumerReferenceElement);
                    break;
                }
            }
        }
        log.debug("End extractSubscriptionIdFromReferenceParametersElements - Subscription id='" + subscriptionId + "'");

        return subscriptionId;
    }
}
