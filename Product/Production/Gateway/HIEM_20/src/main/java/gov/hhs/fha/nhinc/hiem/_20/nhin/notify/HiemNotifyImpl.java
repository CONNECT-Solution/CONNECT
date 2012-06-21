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
package gov.hhs.fha.nhinc.hiem._20.nhin.notify;

import java.io.ByteArrayOutputStream;

import javax.xml.soap.SOAPMessage;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Notify;
import org.w3c.dom.Element;

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.eventcommon.NotifyEventType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.hiem.dte.SoapUtil;
import gov.hhs.fha.nhinc.hiem.processor.nhin.NhinNotifyProcessor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;

/**
 *
 * @author jhoppesc
 */
public class HiemNotifyImpl {

    private static Log log = LogFactory.getLog(HiemNotifyImpl.class);

    public static void notify(Notify notifyRequest, WebServiceContext context) {
        log.debug("Entering HiemNotifyImpl.notify");
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
            
        auditInputMessage(notifyRequest, assertion,
                NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        
        // Log the start of the nhin performance record
        PerformanceManager.getPerformanceManagerInstance().logPerformanceStart(NhincConstants.HIEM_NOTIFY_SERVICE_NAME,
                NhincConstants.AUDIT_LOG_NHIN_INTERFACE, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                HomeCommunityMap.getLocalHomeCommunityId());

        SoapUtil contextHelper = new SoapUtil();
        Element soapMessage = contextHelper.extractSoapMessageElement(context,
                NhincConstants.HIEM_NOTIFY_SOAP_HDR_ATTR_TAG);

        try {
            // String rawSoapMessage = extractSoapMessage(context, "notifySoapMessage");
            NhinNotifyProcessor notifyProcessor = new NhinNotifyProcessor();
            auditInputMessage(notifyRequest, assertion,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);
            if (checkPolicy(notifyRequest, assertion)) {
                notifyProcessor.processNhinNotify(soapMessage, assertion);
            } else {
                log.error("Failed policy check on notify message");
            }
        } catch (Throwable t) {
            log.debug("Exception encountered processing a notify message: " + t.getMessage(), t);
            // TODO: Add specific catch statements and throw the appropriate fault

        }
        // Log the end of the nhin performance record
        PerformanceManager.getPerformanceManagerInstance().logPerformanceStop(NhincConstants.HIEM_NOTIFY_SERVICE_NAME,
                NhincConstants.AUDIT_LOG_NHIN_INTERFACE, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                HomeCommunityMap.getLocalHomeCommunityId());
        log.debug("Exiting HiemNotifyImpl.notify");
    }

    private static void auditInputMessage(Notify notifyRequest, AssertionType assertion,
            String direction, String logInterface) {
        log.debug("In HiemNotifyImpl.auditInputMessage");
        try {
            AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

            gov.hhs.fha.nhinc.common.nhinccommoninternalorch.NotifyRequestType message = new gov.hhs.fha.nhinc.common.nhinccommoninternalorch.NotifyRequestType();
            message.setAssertion(assertion);
            message.setNotify(notifyRequest);

            LogEventRequestType auditLogMsg = auditLogger.logNhinNotifyRequest(message,
                    direction, logInterface);

            if (auditLogMsg != null) {
                AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
                AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
                proxy.auditLog(auditLogMsg, assertion);
            }
        } catch (Throwable t) {
            log.error("Failed to log notify message: " + t.getMessage(), t);
        }
    }

    private static boolean checkPolicy(Notify notifyRequest, AssertionType assertion) {
        log.debug("In HiemNotifyImpl.checkPolicy");
        boolean policyIsValid = false;

        NotifyEventType policyCheckReq = new NotifyEventType();
        policyCheckReq.setDirection(NhincConstants.POLICYENGINE_INBOUND_DIRECTION);
        gov.hhs.fha.nhinc.common.eventcommon.NotifyMessageType request = new gov.hhs.fha.nhinc.common.eventcommon.NotifyMessageType();
        request.setAssertion(assertion);
        request.setNotify(notifyRequest);
        policyCheckReq.setMessage(request);

        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
        CheckPolicyRequestType policyReq = policyChecker.checkPolicyNotify(policyCheckReq);
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq, assertion);

        if (policyResp.getResponse() != null && NullChecker.isNotNullish(policyResp.getResponse().getResult())
                && policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT) {
            policyIsValid = true;
        }

        log.debug("Finished HiemNotifyImpl.checkPolicy - valid: " + policyIsValid);
        return policyIsValid;
    }

    private static String extractSoapMessage(WebServiceContext context, String attributeName) {
        String extractedMessage = null;
        @SuppressWarnings("unchecked")
        MessageContext msgContext = context.getMessageContext();
        if (msgContext != null) {
            javax.servlet.http.HttpServletRequest servletRequest = (javax.servlet.http.HttpServletRequest) msgContext
                    .get(MessageContext.SERVLET_REQUEST);
            SOAPMessage soapMessage = (SOAPMessage) servletRequest.getAttribute(attributeName);
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
        }
        return extractedMessage;
    }
}
