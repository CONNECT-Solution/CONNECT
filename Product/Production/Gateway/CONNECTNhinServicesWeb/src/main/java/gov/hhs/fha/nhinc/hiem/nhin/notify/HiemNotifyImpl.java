/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.hiem.nhin.notify;

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.eventcommon.NotifyEventType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.hiem.dte.SoapUtil;
import gov.hhs.fha.nhinc.hiem.processor.nhin.NhinNotifyProcessor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import java.io.ByteArrayOutputStream;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Notify;
import org.w3c.dom.Element;

/**
 *
 * @author jhoppesc
 */
public class HiemNotifyImpl {

    private static Log log = LogFactory.getLog(HiemNotifyImpl.class);

    public static void notify(Notify notifyRequest, WebServiceContext context) {
        log.debug("Entering HiemNotifyImpl.notify");

        SoapUtil contextHelper = new SoapUtil();
        Element soapMessage = contextHelper.extractSoapMessageElement(context, NhincConstants.HIEM_NOTIFY_SOAP_HDR_ATTR_TAG);

        try {
            //String rawSoapMessage = extractSoapMessage(context, "notifySoapMessage");
            NhinNotifyProcessor notifyProcessor = new NhinNotifyProcessor();
            AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
            auditInputMessage(notifyRequest, assertion);
            if(checkPolicy(notifyRequest, assertion))
            {
                notifyProcessor.processNhinNotify(soapMessage, assertion);
            }
            else
            {
                log.error("Failed policy check on notify message");
            }
        } catch (Throwable t) {
            log.debug("Exception encountered processing a notify message: " + t.getMessage(), t);
            // TODO: Add specific catch statements and throw the appropriate fault

        }

        log.debug("Exiting HiemNotifyImpl.notify");
    }

    private static void auditInputMessage(Notify notifyRequest, AssertionType assertion) {
        log.debug("In HiemNotifyImpl.auditInputMessage");
        AcknowledgementType ack = null;
        try
        {
            AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

            gov.hhs.fha.nhinc.common.nhinccommoninternalorch.NotifyRequestType message = new gov.hhs.fha.nhinc.common.nhinccommoninternalorch.NotifyRequestType();
            message.setAssertion(assertion);
            message.setNotify(notifyRequest);

            LogEventRequestType auditLogMsg = auditLogger.logNhinNotifyRequest(message, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

            if(auditLogMsg != null)
            {
                AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
                AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
                ack = proxy.auditLog(auditLogMsg, assertion);
            }
        }
        catch(Throwable t)
        {
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

        if (policyResp.getResponse() != null &&
                NullChecker.isNotNullish(policyResp.getResponse().getResult()) &&
                policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT) {
            policyIsValid = true;
        }

        log.debug("Finished HiemNotifyImpl.checkPolicy - valid: " + policyIsValid);
        return policyIsValid;
    }

    private static String extractSoapMessage(WebServiceContext context, String attributeName)
    {
        String extractedMessage = null;
        @SuppressWarnings("unchecked")
        MessageContext msgContext = context.getMessageContext();
        if(msgContext != null)
        {
            javax.servlet.http.HttpServletRequest servletRequest = (javax.servlet.http.HttpServletRequest)msgContext.get(MessageContext.SERVLET_REQUEST);
            SOAPMessage soapMessage = (SOAPMessage)servletRequest.getAttribute(attributeName);
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
