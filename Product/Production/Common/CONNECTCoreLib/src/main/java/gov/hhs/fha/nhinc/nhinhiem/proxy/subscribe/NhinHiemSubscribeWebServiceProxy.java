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
package gov.hhs.fha.nhinc.nhinhiem.proxy.subscribe;

//import java.lang.reflect.InvocationTargetException;

import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;

import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.SubscribeCreationFailedFaultType;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.oasis_open.docs.wsn.bw_2.NotificationProducer;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.w3c.dom.Element;

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.eventcommon.SubscribeEventType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.SubscribeResponseMarshaller;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.WsntSubscribeMarshaller;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;

/**
 *
 * @author Jon Hoppesch
 */
public class NhinHiemSubscribeWebServiceProxy implements NhinHiemSubscribeProxy {

    private static Log log = LogFactory.getLog(NhinHiemSubscribeWebServiceProxy.class);

    private static Service cachedService = null;
    private static WebServiceProxyHelper oProxyHelper = null;
    private static final String NAMESPACE_URI = "http://docs.oasis-open.org/wsn/bw-2";
    private static final String SERVICE_LOCAL_PART = "NotificationProducerService";
    private static final String PORT_LOCAL_PART = "NotificationProducerPort";
    private static final String WSDL_FILE = "NhinSubscription.wsdl";
    private static final String WS_ADDRESSING_ACTION = "http://docs.oasis-open.org/wsn/bw-2/NotificationProducer/SubscribeRequest";
    private static final String SUBSCRIBE_SERVICE = "subscribe";


    @Override
    public Element subscribe(Element subscribeElement, AssertionType assertion, NhinTargetSystemType target)
            throws Exception {
        Element responseElement = null;
        SubscribeResponse response = null;
        String url = null;

        log.debug("In NhinSubscribeWebserviceProxy.subscribe()");

        WsntSubscribeMarshaller subscribeMarshaller = new WsntSubscribeMarshaller();
        Subscribe subscribe = subscribeMarshaller.unmarshalUnsubscribeRequest(subscribeElement);


        if (target != null) {
            try {
                url = ConnectionManagerCache.getInstance().getEndpointURLFromNhinTarget(target,
                        NhincConstants.HIEM_SUBSCRIBE_SERVICE_NAME);
            } catch (ConnectionManagerException ex) {
                log.error("Error: Failed to retrieve url for service: " + NhincConstants.HIEM_SUBSCRIBE_SERVICE_NAME);
                log.error(ex.getMessage());
            }
        } else {
            log.error("Target system passed into the proxy is null");
        }

        if (NullChecker.isNotNullish(url)) {
            NotificationProducer port = getPort(url, assertion);
            WebServiceProxyHelper wsHelper = new WebServiceProxyHelper();
            wsHelper.addTargetCommunity(((BindingProvider)port), target);

            if (checkPolicy(subscribe, assertion)) {
                    auditInputMessage(subscribe, assertion,
                        NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
                    try {
                        response = (SubscribeResponse) oProxyHelper.invokePort(port, NotificationProducer.class,
                                SUBSCRIBE_SERVICE, subscribe);
                    } catch (Exception e) {
                        log.error("Exception: " + e.getMessage());
                        throw e;
                    }
                auditResponseMessage(response, assertion,
                        NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
            } else {
                SubscribeCreationFailedFaultType faultInfo = null;
                throw new SubscribeCreationFailedFault("Policy check failed", faultInfo);
            }


            SubscribeResponseMarshaller responseMarshaller = new SubscribeResponseMarshaller();
            responseElement = responseMarshaller.marshal(response);
            log.debug(XmlUtility.serializeElementIgnoreFaults(responseElement));
        } else {
            log.error("The URL for service: " + NhincConstants.HIEM_SUBSCRIBE_SERVICE_NAME + " is null");
        }

        log.debug("Exit NhinSubscribeWebserviceProxy.subscribe()");
        return responseElement;
    }

    private boolean checkPolicy(Subscribe subscribe, AssertionType assertion) {
        log.debug("In NhinHiemSubscribeWebServiceProxy.checkPolicy");
        boolean policyIsValid = false;

        SubscribeEventType policyCheckReq = new SubscribeEventType();
        policyCheckReq.setDirection(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION);
        gov.hhs.fha.nhinc.common.eventcommon.SubscribeMessageType request = new gov.hhs.fha.nhinc.common.eventcommon.SubscribeMessageType();
        request.setAssertion(assertion);
        request.setSubscribe(subscribe);
        policyCheckReq.setMessage(request);

        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
        CheckPolicyRequestType policyReq = policyChecker.checkPolicySubscribe(policyCheckReq);
        policyReq.setAssertion(assertion);
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq, assertion);

        if (policyResp.getResponse() != null && NullChecker.isNotNullish(policyResp.getResponse().getResult())
                && policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT) {
            policyIsValid = true;
        }

        log.debug("Finished NhinHiemSubscribeWebServiceProxy.checkPolicy - valid: " + policyIsValid);
        return policyIsValid;
    }

    /**
     * Create generic audit log for Input requests.
     * @param subscribe The subscribe message to be audited
     * @param assertion The assertion to be audited
     * @param logDirection The direction of the log being audited (Inbound or Outbound)
     * @param logInterface The interface of the log being audited (NHIN or Adapter)
     */
    private void auditInputMessage(Subscribe subscribe, AssertionType assertion, 
            String logDirection, String logInterface ) {
        log.debug("In NhinHiemSubscribeWebServiceProxy.auditInputMessage");
        AcknowledgementType ack = null;
        try {
            AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

            gov.hhs.fha.nhinc.common.nhinccommoninternalorch.SubscribeRequestType message = new gov.hhs.fha.nhinc.common.nhinccommoninternalorch.SubscribeRequestType();
            message.setAssertion(assertion);
            message.setSubscribe(subscribe);

            LogEventRequestType auditLogMsg = auditLogger.logNhinSubscribeRequest(message,
                    logDirection, logInterface);

            if (auditLogMsg != null) {
                AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
                AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
                ack = proxy.auditLog(auditLogMsg, assertion);
            }
        } catch (Throwable t) {
            log.error("Error logging subscribe message: " + t.getMessage(), t);
        }
    }

    /**
     * Create generic audit log for response messages.
     * @param response the response to be audited
     * @param assertion the assertion to be audited
     * @param logDirection the direction of the log to be audited (Inbound or Outbound)
     * @param logInterface the interface of the log being audited (NHIN or Adapter)
     */
    private void auditResponseMessage(SubscribeResponse response, AssertionType assertion, String logDirection,
            String logInterface ) {
        log.debug("In NhinHiemSubscribeWebServiceProxy.auditResponseMessage");
        try {
            AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

            gov.hhs.fha.nhinc.common.hiemauditlog.SubscribeResponseMessageType message = new gov.hhs.fha.nhinc.common.hiemauditlog.SubscribeResponseMessageType();
            message.setAssertion(assertion);
            message.setSubscribeResponse(response);

            LogEventRequestType auditLogMsg = auditLogger.logSubscribeResponse(message,
                    logDirection, logInterface);

            if (auditLogMsg != null) {
                AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
                AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
                proxy.auditLog(auditLogMsg, assertion);
            }
        } catch (Throwable t) {
            log.error("Error logging subscribe response message: " + t.getMessage(), t);
        }
    }

    protected NotificationProducer getPort(String url, AssertionType assertIn) {
        NotificationProducer oPort = null;
        try {
            Service oService = getService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);

            if (oService != null) {
                log.debug("subscribe() Obtained service - creating port.");
                oPort = oService.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), NotificationProducer.class);

                // Initialize secured port
                getWebServiceProxyHelper().initializeSecurePort((BindingProvider) oPort, url,
                        NhincConstants.HIEM_SUBSCRIBE_SERVICE_NAME, WS_ADDRESSING_ACTION, assertIn);
                
                ((BindingProvider)oPort).getRequestContext().put(NhincConstants.TARGET_API_LEVEL, GATEWAY_API_LEVEL.LEVEL_g0);
            } else {
                log.error("Unable to obtain service - no port created.");
            }
        } catch (Throwable t) {
            log.error("Error creating service: " + t.getMessage(), t);
        }
        return oPort;
    }

    private WebServiceProxyHelper getWebServiceProxyHelper() {
        if (oProxyHelper == null) {
            oProxyHelper = new WebServiceProxyHelper();
        }
        return oProxyHelper;
    }

    private Service getService(String wsdl, String uri, String service) {
        if (cachedService == null) {
            try {
                cachedService = getWebServiceProxyHelper().createService(wsdl, uri, service);
            } catch (Throwable t) {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }
}
