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
package gov.hhs.fha.nhinc.hiem._20.entity.proxy;

import javax.xml.ws.WebServiceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Unsubscribe;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;
import org.w3c.dom.Element;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersElements;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersHelper;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.WsntUnsubscribeMarshaller;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.WsntUnsubscribeResponseMarshaller;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.ResourceUnknownFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.UnableToDestroySubscriptionFault;
import gov.hhs.fha.nhinc.nhinhiem.proxy.unsubscribe.NhinHiemUnsubscribeProxy;
import gov.hhs.fha.nhinc.nhinhiem.proxy.unsubscribe.NhinHiemUnsubscribeProxyObjectFactory;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;

/**
 *
 * @author rayj
 */
public class ProxyHiemUnsubscribeImpl {

    private static Log log = LogFactory.getLog(ProxyHiemUnsubscribeImpl.class);

    public org.oasis_open.docs.wsn.b_2.UnsubscribeResponse unsubscribe(
            gov.hhs.fha.nhinc.common.nhinccommonproxy.UnsubscribeRequestType unsubscribeRequest,
            WebServiceContext context) throws Exception {
        log.debug("Entering ProxyHiemUnsubscribeImpl.unsubscribe...");

        // Log the start of the nhin performance record
        PerformanceManager.getPerformanceManagerInstance().logPerformanceStart(NhincConstants.HIEM_UNSUBSCRIBE_PROXY_SERVICE_NAME_SECURED, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, HomeCommunityMap.getLocalHomeCommunityId());

        log.debug("extracting unsubscribe");
        Unsubscribe unsubscribe = unsubscribeRequest.getUnsubscribe();
        WsntUnsubscribeMarshaller wsntUnsubscribeMarshaller = new WsntUnsubscribeMarshaller();
        Element unsubscribeElement = wsntUnsubscribeMarshaller.marshal(unsubscribe);
        // Element unsubscribeElement = soaputil.extractFirstElement(context, "unsubscribeSoapMessage", "Unsubscribe");
        log.debug(XmlUtility.formatElementForLogging("unsubscribe", unsubscribeElement));

        log.debug("extracting target");
        NhinTargetSystemType target = unsubscribeRequest.getNhinTargetSystem();
        log.debug("extracted target");

        log.debug("extracting assertion");
        AssertionType assertion = unsubscribeRequest.getAssertion();
        log.debug("extracted assertion");

        log.debug("extracting consumer reference elements");
        ReferenceParametersHelper consumerReferenceHelper = new ReferenceParametersHelper();
        ReferenceParametersElements consumerReferenceElements = consumerReferenceHelper
                .createReferenceParameterElements(context, "unsubscribeSoapMessage");
        log.debug("extracted consumer reference elements");

        UnsubscribeResponse response = null;
        NhinHiemUnsubscribeProxyObjectFactory factory = new NhinHiemUnsubscribeProxyObjectFactory();
        NhinHiemUnsubscribeProxy proxy = factory.getNhinHiemSubscribeProxy();
        try {
            log.debug("invoke unsubscribe nhin component proxy");
            Element responseElement = proxy.unsubscribe(unsubscribeElement, consumerReferenceElements, assertion,
                    target);
            log.debug("invoked unsubscribe nhin component proxy");
            log.debug("unmarshall unsubscribe response to object");
            WsntUnsubscribeResponseMarshaller marshaller = new WsntUnsubscribeResponseMarshaller();
            response = marshaller.unmarshal(responseElement);
            log.debug("unmarshalled unsubscribe response to object");
        } catch (org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault ex) {
            log.error("error occurred", ex);
            // todo: throw proper exception
            response = new UnsubscribeResponse();
            response.getAny().add(ex);
        } catch (org.oasis_open.docs.wsn.bw_2.UnableToDestroySubscriptionFault ex) {
            log.error("error occurred", ex);
            // todo: throw proper exception
            response = new UnsubscribeResponse();
            response.getAny().add(ex);
        }
        // Log the end of the nhin performance record
        PerformanceManager.getPerformanceManagerInstance().logPerformanceStop(NhincConstants.HIEM_UNSUBSCRIBE_PROXY_SERVICE_NAME_SECURED, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, HomeCommunityMap.getLocalHomeCommunityId());
        log.debug("Exiting ProxyHiemUnsubscribeImpl.unsubscribe...");
        return response;
    }

    public org.oasis_open.docs.wsn.b_2.UnsubscribeResponse unsubscribe(
            gov.hhs.fha.nhinc.common.nhinccommonproxy.UnsubscribeRequestSecuredType unsubscribeRequest,
            WebServiceContext context) throws UnableToDestroySubscriptionFault, ResourceUnknownFault {
        log.debug("Entering ProxyHiemUnsubscribeImpl.unsubscribe...");

        // Log the start of the nhin performance record
        PerformanceManager.getPerformanceManagerInstance().logPerformanceStart(NhincConstants.HIEM_UNSUBSCRIBE_PROXY_SERVICE_NAME_SECURED, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, HomeCommunityMap.getLocalHomeCommunityId());

        log.debug("extracting unsubscribe");
        Unsubscribe unsubscribe = unsubscribeRequest.getUnsubscribe();
        WsntUnsubscribeMarshaller wsntUnsubscribeMarshaller = new WsntUnsubscribeMarshaller();
        Element unsubscribeElement = wsntUnsubscribeMarshaller.marshal(unsubscribe);
        // Element unsubscribeElement = soaputil.extractFirstElement(context, "unsubscribeSoapMessage", "Unsubscribe");
        log.debug(XmlUtility.formatElementForLogging("unsubscribe", unsubscribeElement));

        log.debug("extracting target");
        NhinTargetSystemType target = unsubscribeRequest.getNhinTargetSystem();
        log.debug("extracted target");

        log.debug("extracting assertion");
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
        log.debug("extracted assertion");

        log.debug("extracting consumer reference elements");
        ReferenceParametersHelper consumerReferenceHelper = new ReferenceParametersHelper();
        ReferenceParametersElements consumerReferenceElements = consumerReferenceHelper
                .createReferenceParameterElements(context, "unsubscribeSoapMessage");
        log.debug("extracted consumer reference elements");

        UnsubscribeResponse response = null;
        NhinHiemUnsubscribeProxyObjectFactory factory = new NhinHiemUnsubscribeProxyObjectFactory();
        NhinHiemUnsubscribeProxy proxy = factory.getNhinHiemSubscribeProxy();
        try {
            log.debug("invoke unsubscribe nhin component proxy");
            Element responseElement = proxy.unsubscribe(unsubscribeElement, consumerReferenceElements, assertion,
                    target);
            log.debug("invoked unsubscribe nhin component proxy");
            log.debug("unmarshall unsubscribe response to object");
            WsntUnsubscribeResponseMarshaller marshaller = new WsntUnsubscribeResponseMarshaller();
            response = marshaller.unmarshal(responseElement);
            log.debug("unmarshalled unsubscribe response to object");
        } catch (org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault e) {
            log.error("error occurred", e);
            // todo: throw proper exception
            response = new UnsubscribeResponse();
            response.getAny().add(e);
        } catch (org.oasis_open.docs.wsn.bw_2.UnableToDestroySubscriptionFault e) {
            log.error("error occurred", e);
            // todo: throw proper exception
            response = new UnsubscribeResponse();
            response.getAny().add(e);
        } catch (Exception e) {
            log.error("exception occured: " + e.getMessage());
            response.getAny().add(e);
        }
        // Log the end of the nhin performance record
        PerformanceManager.getPerformanceManagerInstance().logPerformanceStop(NhincConstants.HIEM_UNSUBSCRIBE_PROXY_SERVICE_NAME_SECURED, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, HomeCommunityMap.getLocalHomeCommunityId());
        log.debug("Exiting ProxyHiemUnsubscribeImpl.unsubscribe...");
        return response;
    }
}
