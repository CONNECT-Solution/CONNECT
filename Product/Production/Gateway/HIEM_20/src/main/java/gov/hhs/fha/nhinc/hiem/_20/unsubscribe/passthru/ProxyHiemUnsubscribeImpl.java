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
package gov.hhs.fha.nhinc.hiem._20.unsubscribe.passthru;

import javax.xml.ws.WebServiceContext;

import org.apache.log4j.Logger;
import org.oasis_open.docs.wsn.b_2.Unsubscribe;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;
import org.w3c.dom.Element;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.cxf.extraction.SAML2AssertionExtractor;
import gov.hhs.fha.nhinc.hiem.consumerreference.SoapHeaderHelper;
import gov.hhs.fha.nhinc.hiem.consumerreference.SoapMessageElements;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.UnableToDestroySubscriptionFault;
import gov.hhs.fha.nhinc.unsubscribe.nhin.proxy.NhinHiemUnsubscribeProxy;
import gov.hhs.fha.nhinc.unsubscribe.nhin.proxy.NhinHiemUnsubscribeProxyObjectFactory;

public class ProxyHiemUnsubscribeImpl {

    private static final Logger LOG = Logger.getLogger(ProxyHiemUnsubscribeImpl.class);

    public UnsubscribeResponse unsubscribe(
            gov.hhs.fha.nhinc.common.nhinccommonproxy.UnsubscribeRequestType unsubscribeRequest,
            WebServiceContext context) throws Exception {
        UnsubscribeResponse response = null;
        LOG.debug("Entering ProxyHiemUnsubscribeImpl.unsubscribe...");

        Unsubscribe unsubscribe = unsubscribeRequest.getUnsubscribe();
        NhinTargetSystemType target = unsubscribeRequest.getNhinTargetSystem();
        AssertionType assertion = unsubscribeRequest.getAssertion();

        SoapMessageElements soapHeaderElements = new SoapHeaderHelper().getSoapHeaderElements(context);

        NhinHiemUnsubscribeProxyObjectFactory factory = new NhinHiemUnsubscribeProxyObjectFactory();
        NhinHiemUnsubscribeProxy proxy = factory.getNhinHiemUnsubscribeProxy();
        try {
            response = proxy
                    .unsubscribe(unsubscribe, soapHeaderElements, assertion, target, getSubscriptionId(context));
        } catch (UnableToDestroySubscriptionFault ex) {
            LOG.error("error occurred", ex);
            response = new UnsubscribeResponse();
            response.getAny().add(ex);
        }
        LOG.debug("Exiting ProxyHiemUnsubscribeImpl.unsubscribe...");
        return response;
    }

    public UnsubscribeResponse unsubscribe(
            gov.hhs.fha.nhinc.common.nhinccommonproxy.UnsubscribeRequestSecuredType unsubscribeRequest,
            WebServiceContext context) throws UnableToDestroySubscriptionFault {
        UnsubscribeResponse response = null;
        LOG.debug("Entering ProxyHiemUnsubscribeImpl.unsubscribe...");
        Unsubscribe unsubscribe = unsubscribeRequest.getUnsubscribe();
        NhinTargetSystemType target = unsubscribeRequest.getNhinTargetSystem();
        AssertionType assertion = SAML2AssertionExtractor.getInstance().extractSamlAssertion(context);

        SoapMessageElements soapHeaderElements = new SoapHeaderHelper().getSoapHeaderElements(context);

        NhinHiemUnsubscribeProxyObjectFactory factory = new NhinHiemUnsubscribeProxyObjectFactory();
        NhinHiemUnsubscribeProxy proxy = factory.getNhinHiemUnsubscribeProxy();
        try {
            response = proxy
                    .unsubscribe(unsubscribe, soapHeaderElements, assertion, target, getSubscriptionId(context));
        } catch (UnableToDestroySubscriptionFault e) {
            LOG.error("error occurred", e);
            response = new UnsubscribeResponse();
            response.getAny().add(e);
        } catch (Exception e) {
            LOG.error("exception occured: " + e.getMessage());
            response.getAny().add(e);
        }
        LOG.debug("Exiting ProxyHiemUnsubscribeImpl.unsubscribe...");
        return response;
    }

    private String getSubscriptionId(WebServiceContext context) {
        SoapMessageElements soapHeaderElements = new SoapHeaderHelper().getSoapHeaderElements(context);

        String subscriptionId = null;
        for (Element soapHeaderElement : soapHeaderElements.getElements()) {
            String nodeName = soapHeaderElement.getLocalName();
            if (nodeName.equals("SubscriptionId")) {
                String nodeValue = soapHeaderElement.getNodeValue();
                if (NullChecker.isNullish(nodeValue) && soapHeaderElement.getFirstChild() != null) {
                    nodeValue = soapHeaderElement.getFirstChild().getNodeValue();
                }
                return nodeValue;
            }
        }

        return subscriptionId;
    }

}
