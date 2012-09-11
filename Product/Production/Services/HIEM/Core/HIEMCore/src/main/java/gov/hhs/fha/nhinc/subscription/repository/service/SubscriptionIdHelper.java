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

import gov.hhs.fha.nhinc.hiem.consumerreference.SoapMessageElements;
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

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
            .getLog(SubscriptionIdHelper.class);

    public static String extractSubscriptionIdFromSubscriptionReferenceXml(String subscriptionReferenceXml) {
        log.debug("Begin attemptSubscriptionIdExtract");
        String subscriptionId = null;
        if (subscriptionReferenceXml != null) {
            try {
                log.debug("Attempting  to extract subscription id from subscription reference xml: "
                        + subscriptionReferenceXml);

                Element subscriptionIdElement = (Element) XpathHelper.performXpathQuery(subscriptionReferenceXml,
                        "//*[local-name()='ReferenceParameters']/*[local-name()='SubscriptionId']");
                subscriptionId = XmlUtility.getNodeValue(subscriptionIdElement);

                if (NullChecker.isNullish(subscriptionId)) {
                    subscriptionIdElement = (Element) XpathHelper
                            .performXpathQuery(subscriptionReferenceXml,
                                    "/*[local-name()='EndpointReference']/*[local-name()='ReferenceParameters']/*[local-name()='SubscriptionId']");
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

    // todo: check namespace
    // todo: move "subscriptionid" magic definition somewhere central - allow for the implementation of subscription id
    // to change without having to trace changes
    public static String extractSubscriptionIdFromReferenceParametersElements(
            SoapMessageElements referenceParametersElements) {
        log.debug("Begin extractSubscriptionIdFromReferenceParametersElements");
        String subscriptionId = null;
        if (referenceParametersElements != null) {
            log.debug("looking for subscription id");
            for (Element consumerReferenceElement : referenceParametersElements.getElements()) {
                if (consumerReferenceElement.getLocalName().contentEquals("SubscriptionId")) {
                    log.debug("subscriptionId element: "
                            + XmlUtility.formatElementForLogging(null, consumerReferenceElement));
                    subscriptionId = XmlUtility.getNodeValue(consumerReferenceElement);
                    break;
                }
            }
        }
        log.debug("End extractSubscriptionIdFromReferenceParametersElements - Subscription id='" + subscriptionId + "'");

        return subscriptionId;
    }
}
