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
package gov.hhs.fha.nhinc.hiem._20.nhin.subscribe;

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.eventcommon.SubscribeEventType;
import gov.hhs.fha.nhinc.common.eventcommon.SubscribeMessageType;
import gov.hhs.fha.nhinc.common.hiemauditlog.SubscribeResponseMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.common.nhinccommoninternalorch.SubscribeRequestType;
import gov.hhs.fha.nhinc.cxf.extraction.SAML2AssertionExtractor;
import gov.hhs.fha.nhinc.hiem.dte.SoapUtil;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import gov.hhs.fha.nhinc.subscribe.nhin.NhinSubscribeProcessor;

import javax.xml.ws.WebServiceContext;

import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.SubscribeCreationFailedFaultType;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault;
import org.oasis_open.docs.wsn.bw_2.NotifyMessageNotSupportedFault;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault;
import org.w3c.dom.Element;

/**
 * 
 * @author jhoppesc
 */
public class HiemSubscribeImpl {

    private static Log log = LogFactory.getLog(HiemSubscribeImpl.class);

    public SubscribeResponse subscribe(Subscribe subscribeRequest, WebServiceContext context)
            throws NotifyMessageNotSupportedFault, SubscribeCreationFailedFault, TopicNotSupportedFault,
            InvalidTopicExpressionFault {
        log.debug("Entering HiemSubscriptionImpl.subscribe");

        Element soapMessage = extractSoapMessage(context);

        NhinSubscribeProcessor subscribeProcessor = new NhinSubscribeProcessor();
        AssertionType assertion = SAML2AssertionExtractor.getInstance().extractSamlAssertion(context);

        // Audit the input message
        auditInputMessage(subscribeRequest, assertion,
                NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        SubscribeResponse response = null;
        if (checkPolicy(subscribeRequest, assertion)) {
            // Audit the input message
            auditInputMessage(subscribeRequest, assertion,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);

            response = subscribeProcessor.processNhinSubscribe(soapMessage, assertion);

            // Audit the response message
            auditResponseMessage(response, assertion,
                NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);
        } else {
            SubscribeCreationFailedFaultType faultInfo = null;
            throw new SubscribeCreationFailedFault("Policy check failed", faultInfo);
        }

        // Audit the response message
        // Audit the response message
        auditResponseMessage(response, assertion,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);


        log.debug("Exiting HiemSubscriptionImpl.subscribe");
        return response;
    }

    private void auditInputMessage(Subscribe subscribe, AssertionType assertion,
            String direction, String logInterface) {
        log.debug("In HiemSubscriptionImpl.auditInputMessage");
        try {
            AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

            SubscribeRequestType message = new SubscribeRequestType();
            message.setAssertion(assertion);
            message.setSubscribe(subscribe);

            LogEventRequestType auditLogMsg = auditLogger.logNhinSubscribeRequest(message,
                    direction, logInterface);

            if (auditLogMsg != null) {
                AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
                AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
                proxy.auditLog(auditLogMsg, assertion);
            }
        } catch (Throwable t) {
            log.error("Error logging subscribe message: " + t.getMessage(), t);
        }
    }

    private void auditResponseMessage(SubscribeResponse response, AssertionType assertion,
            String direction, String logInterface) {
        log.debug("In HiemSubscriptionImpl.auditResponseMessage");

        try {
            AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

            SubscribeResponseMessageType message = new SubscribeResponseMessageType();
            message.setAssertion(assertion);
            message.setSubscribeResponse(response);

            LogEventRequestType auditLogMsg = auditLogger.logSubscribeResponse(message,
                    direction, logInterface);

            if (auditLogMsg != null) {
                AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
                AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
                proxy.auditLog(auditLogMsg, assertion);
            }
        } catch (Throwable t) {
            log.error("Error loging subscription response: " + t.getMessage(), t);
        }
    }

    private boolean checkPolicy(Subscribe subscribe, AssertionType assertion) {
        log.debug("In HiemSubscriptionImpl.checkPolicy");
        boolean policyIsValid = false;

        SubscribeEventType policyCheckReq = new SubscribeEventType();
        policyCheckReq.setDirection(NhincConstants.POLICYENGINE_INBOUND_DIRECTION);
        SubscribeMessageType request = new SubscribeMessageType();
        request.setAssertion(assertion);
        request.setSubscribe(subscribe);
        policyCheckReq.setMessage(request);

        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
        CheckPolicyRequestType policyReq = policyChecker.checkPolicySubscribe(policyCheckReq);
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq, assertion);

        if (policyResp.getResponse() != null && NullChecker.isNotNullish(policyResp.getResponse().getResult())
                && policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT) {
            policyIsValid = true;
        }

        log.debug("Finished HiemSubscriptionImpl.checkPolicy - valid: " + policyIsValid);
        return policyIsValid;
    }

    private Element extractSoapMessage(WebServiceContext context) {
        return new SoapUtil().extractSoapMessageElement(context, NhincConstants.HTTP_REQUEST_ATTRIBUTE_SOAPMESSAGE);
    }
}
