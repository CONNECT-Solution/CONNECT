/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.hiem.nhin.subscription;

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.eventcommon.SubscribeEventType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.dte.WebServiceContextHelper;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import java.io.ByteArrayOutputStream;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import javax.xml.ws.handler.MessageContext;
import javax.xml.soap.SOAPMessage;
import gov.hhs.fha.nhinc.hiem.processor.nhin.NhinSubscribeProcessor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault;
import org.oasis_open.docs.wsn.bw_2.NotifyMessageNotSupportedFault;
import org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault;
import org.w3c.dom.Element;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import org.oasis_open.docs.wsn.b_2.SubscribeCreationFailedFaultType;

/**
 *
 * @author jhoppesc
 */
public class HiemSubscriptionImpl {

    private static Log log = LogFactory.getLog(HiemSubscriptionImpl.class);

    public SubscribeResponse subscribe(Subscribe subscribeRequest, WebServiceContext context) throws NotifyMessageNotSupportedFault, SubscribeCreationFailedFault, TopicNotSupportedFault, InvalidTopicExpressionFault, ResourceUnknownFault  {
        log.debug("Entering HiemSubscriptionImpl.subscribe");

        WebServiceContextHelper contextHelper = new WebServiceContextHelper();
        Element soapMessage = contextHelper.extractSoapMessage(context);

        NhinSubscribeProcessor subscribeProcessor = new NhinSubscribeProcessor();
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

        // Audit the input message
        auditInputMessage(subscribeRequest, assertion);

        SubscribeResponse response = null;
        if(checkPolicy(subscribeRequest, assertion))
        {
            response = subscribeProcessor.processNhinSubscribe(soapMessage, assertion);
        }
        else
        {
            SubscribeCreationFailedFaultType faultInfo = null;
            throw new SubscribeCreationFailedFault("Policy check failed", faultInfo);
        }

        // Audit the response message
        auditResponseMessage(response, assertion);

        log.debug("Exiting HiemSubscriptionImpl.subscribe");
        return response;
    }

    private void auditInputMessage(Subscribe subscribe, AssertionType assertion) {
        log.debug("In HiemSubscriptionImpl.auditInputMessage");
        AcknowledgementType ack = null;
        try
        {
            AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

            gov.hhs.fha.nhinc.common.nhinccommoninternalorch.SubscribeRequestType message = new gov.hhs.fha.nhinc.common.nhinccommoninternalorch.SubscribeRequestType();
            message.setAssertion(assertion);
            message.setSubscribe(subscribe);

            LogEventRequestType auditLogMsg = auditLogger.logNhinSubscribeRequest(message, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

            if(auditLogMsg != null)
            {
                AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
                AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
                ack = proxy.auditLog(auditLogMsg, assertion);
            }
        }
        catch(Throwable t)
        {
            log.error("Error logging subscribe message: " + t.getMessage(), t);
        }
    }

    private void auditResponseMessage(SubscribeResponse response, AssertionType assertion) {
        log.debug("In HiemSubscriptionImpl.auditResponseMessage");
        AcknowledgementType ack = null;
        try
        {
            AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

            gov.hhs.fha.nhinc.common.hiemauditlog.SubscribeResponseMessageType message = new gov.hhs.fha.nhinc.common.hiemauditlog.SubscribeResponseMessageType();
            message.setAssertion(assertion);
            message.setSubscribeResponse(response);

            LogEventRequestType auditLogMsg = auditLogger.logSubscribeResponse(message, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

            if(auditLogMsg != null)
            {
                AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
                AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
                ack = proxy.auditLog(auditLogMsg, assertion);
            }
        }
        catch(Throwable t)
        {
            log.error("Error loging subscription response: " + t.getMessage(), t);
        }
    }

    private boolean checkPolicy(Subscribe subscribe, AssertionType assertion) {
        log.debug("In HiemSubscriptionImpl.checkPolicy");
        boolean policyIsValid = false;

        SubscribeEventType policyCheckReq = new SubscribeEventType();
        policyCheckReq.setDirection(NhincConstants.POLICYENGINE_INBOUND_DIRECTION);
        gov.hhs.fha.nhinc.common.eventcommon.SubscribeMessageType request = new gov.hhs.fha.nhinc.common.eventcommon.SubscribeMessageType();
        request.setAssertion(assertion);
        request.setSubscribe(subscribe);
        policyCheckReq.setMessage(request);

        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
        CheckPolicyRequestType policyReq = policyChecker.checkPolicySubscribe(policyCheckReq);
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq, assertion);

        if (policyResp.getResponse() != null &&
                NullChecker.isNotNullish(policyResp.getResponse().getResult()) &&
                policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT) {
            policyIsValid = true;
        }

        log.debug("Finished HiemSubscriptionImpl.checkPolicy - valid: " + policyIsValid);
        return policyIsValid;
    }

    private static String extractSoapMessage(WebServiceContext context)
    {
        String extractedMessage = null;
        @SuppressWarnings("unchecked")
        MessageContext msgContext = context.getMessageContext();
        if(msgContext != null)
        {
            javax.servlet.http.HttpServletRequest servletRequest = (javax.servlet.http.HttpServletRequest)msgContext.get(MessageContext.SERVLET_REQUEST);
            SOAPMessage soapMessage = (SOAPMessage)servletRequest.getAttribute(NhincConstants.HIEM_SUBSCRIBE_SOAP_HDR_ATTR_TAG);
            if(soapMessage != null)
            {
                log.debug("******** Attempting to write out SOAP message *************");
                try
                {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    soapMessage.writeTo(bos);
                    extractedMessage = new String(bos.toByteArray());
                    log.debug("Extracted soap message: " + extractedMessage);
                }
                catch (Throwable t)
                {
                    log.debug("Exception writing out the message");
                    t.printStackTrace();
                }
            }
            else
            {
                log.debug("SOAPMessage was null");
            }
        }
        return extractedMessage;
    }
}
