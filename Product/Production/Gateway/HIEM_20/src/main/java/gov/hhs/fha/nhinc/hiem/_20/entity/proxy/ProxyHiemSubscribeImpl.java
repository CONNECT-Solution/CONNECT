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
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.w3c.dom.Element;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.SubscribeRequestSecuredType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.SubscribeRequestType;
import gov.hhs.fha.nhinc.hiem.dte.SoapUtil;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.SubscribeResponseMarshaller;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinhiem.proxy.subscribe.NhinHiemSubscribeProxy;
import gov.hhs.fha.nhinc.nhinhiem.proxy.subscribe.NhinHiemSubscribeProxyObjectFactory;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;

/**
 *
 * @author Jon Hoppesch
 */
public class ProxyHiemSubscribeImpl {

    private static Log log = LogFactory.getLog(ProxyHiemSubscribeImpl.class);

    public SubscribeResponse subscribe(SubscribeRequestType subscribeRequest, WebServiceContext context)
            throws Exception {
        log.debug("Entering ProxyHiemSubscribeImpl.subscribe...");

        // Log the start of the nhin performance record
        PerformanceManager.getPerformanceManagerInstance().logPerformanceStart(
                NhincConstants.HIEM_SUBSCRIBE_PROXY_SERVICE_NAME_SECURED, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, HomeCommunityMap.getLocalHomeCommunityId());
        SubscribeResponse resp = null;

        Element subscribeElement = new SoapUtil().extractFirstElement(context, "subscribeSoapMessage", "Subscribe");

        // Audit the Audit Log Query Request Message sent on the Nhin Interface
        audit(subscribeRequest);

        NhinHiemSubscribeProxyObjectFactory hiemSubscribeFactory = new NhinHiemSubscribeProxyObjectFactory();
        NhinHiemSubscribeProxy proxy = hiemSubscribeFactory.getNhinHiemSubscribeProxy();

        Element responseElement = proxy.subscribe(subscribeElement, subscribeRequest.getAssertion(),
                subscribeRequest.getNhinTargetSystem());

        SubscribeResponseMarshaller responseMarshaller = new SubscribeResponseMarshaller();
        resp = responseMarshaller.unmarshal(responseElement);

        // Log the end of the nhin performance record
        PerformanceManager.getPerformanceManagerInstance().logPerformanceStop(
                NhincConstants.HIEM_SUBSCRIBE_PROXY_SERVICE_NAME_SECURED, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, HomeCommunityMap.getLocalHomeCommunityId());

        log.debug("Exiting ProxyHiemSubscribeImpl.subscribe...");
        return resp;
    }

    private AcknowledgementType audit(SubscribeRequestType subscribeRequest) {
        AcknowledgementType ack = null;
        // ConfigurationManager config = new ConfigurationManager();
        // if (config.isAuditEnabled()) {
        //
        // AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        //
        // // Fix namespace issue
        // gov.hhs.fha.nhinc.common.nhinccommoninternalorch.SubscribeRequestType message = new
        // gov.hhs.fha.nhinc.common.nhinccommoninternalorch.SubscribeRequestType();
        // message.setAssertion(subscribeRequest.getAssertion());
        // message.setSubscribe(subscribeRequest.getSubscribe());
        //
        // LogEventRequestType auditLogMsg = auditLogger.logNhinSubscribeRequest(message,
        // NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        //
        // AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
        // AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
        // ack = proxy.auditLog(auditLogMsg);
        // }

        return ack;
    }

    public SubscribeResponse subscribe(SubscribeRequestSecuredType subscribeRequest, WebServiceContext context)
            throws Exception {
        log.debug("Entering Secured ProxyHiemSubscribeImpl.subscribe...");

        // Log the start of the nhin performance record
        PerformanceManager.getPerformanceManagerInstance().logPerformanceStart(
                NhincConstants.HIEM_SUBSCRIBE_PROXY_SERVICE_NAME_SECURED, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, HomeCommunityMap.getLocalHomeCommunityId());
        SubscribeResponse resp = null;

        Element subscribeElement = new SoapUtil().extractFirstElement(context, "subscribeSoapMessage", "Subscribe");

        // Audit the Audit Log Query Request Message sent on the Nhin Interface
        audit(subscribeRequest);

        NhinHiemSubscribeProxyObjectFactory hiemSubscribeFactory = new NhinHiemSubscribeProxyObjectFactory();
        NhinHiemSubscribeProxy proxy = hiemSubscribeFactory.getNhinHiemSubscribeProxy();

        Element responseElement = proxy.subscribe(subscribeElement, SamlTokenExtractor.GetAssertion(context),
                subscribeRequest.getNhinTargetSystem());

        SubscribeResponseMarshaller responseMarshaller = new SubscribeResponseMarshaller();
        resp = responseMarshaller.unmarshal(responseElement);

        // Log the end of the nhin performance record
        PerformanceManager.getPerformanceManagerInstance().logPerformanceStop(
                NhincConstants.HIEM_SUBSCRIBE_PROXY_SERVICE_NAME_SECURED, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, HomeCommunityMap.getLocalHomeCommunityId());

        log.debug("Response = " + XmlUtility.serializeElementIgnoreFaults(responseElement));

        log.debug("Exiting Secured ProxyHiemSubscribeImpl.subscribe...");
        return resp;
    }

    private AcknowledgementType audit(SubscribeRequestSecuredType subscribeRequest) {
        AcknowledgementType ack = null;
        // ConfigurationManager config = new ConfigurationManager();
        // if (config.isAuditEnabled()) {
        //
        // AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        //
        // // Fix namespace issue
        // gov.hhs.fha.nhinc.common.nhinccommoninternalorch.SubscribeRequestType message = new
        // gov.hhs.fha.nhinc.common.nhinccommoninternalorch.SubscribeRequestType();
        // message.setAssertion(subscribeRequest.getAssertion());
        // message.setSubscribe(subscribeRequest.getSubscribe());
        //
        // LogEventRequestType auditLogMsg = auditLogger.logNhinSubscribeRequest(message,
        // NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        //
        // AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
        // AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
        // ack = proxy.auditLog(auditLogMsg);
        // }

        return ack;
    }
}
