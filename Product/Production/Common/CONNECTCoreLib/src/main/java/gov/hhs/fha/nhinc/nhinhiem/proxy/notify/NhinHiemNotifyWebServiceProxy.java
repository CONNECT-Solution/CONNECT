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
package gov.hhs.fha.nhinc.nhinhiem.proxy.notify;


import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;

import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Notify;
import org.oasis_open.docs.wsn.bw_2.NotificationConsumer;
import org.w3c.dom.Element;

import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.developer.WSBindingProvider;

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.eventcommon.NotifyEventType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersElements;
import gov.hhs.fha.nhinc.hiem.dte.SoapUtil;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.WsntSubscribeMarshaller;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;

/**
 *
 * @author Jon Hoppesch
 */
public class NhinHiemNotifyWebServiceProxy implements NhinHiemNotifyProxy {

    private static Log log = LogFactory.getLog(NhinHiemNotifyWebServiceProxy.class);

    private static Service cachedService = null;
    private static WebServiceProxyHelper oProxyHelper = null;
    private static final String NAMESPACE_URI = "http://docs.oasis-open.org/wsn/bw-2";
    private static final String SERVICE_LOCAL_PART = "NotificationConsumerService";
    private static final String PORT_LOCAL_PART = "NotificationConsumerPort";
    private static final String WSDL_FILE = "NhinSubscription.wsdl";
    private static final String NOTIFY_SERVICE = "notify";
    private static final String WS_ADDRESSING_ACTION = "http://docs.oasis-open.org/wsn/bw-2/NotificationConsumer/Notify";

    @Override
    public void notify(Element notifyElement, ReferenceParametersElements referenceParametersElements,
            AssertionType assertion, NhinTargetSystemType target) {
        String url = null;

        log.debug("Notify element received in NhinHiemNotifyWebServiceProxy: "
                + XmlUtility.serializeElementIgnoreFaults(notifyElement));

        log.debug("unmarshaling notify message");
        WsntSubscribeMarshaller notifyMarshaller = new WsntSubscribeMarshaller();
        Notify notify = notifyMarshaller.unmarshalNotifyRequest(notifyElement);

        if (target != null) {
            try {
                url = ConnectionManagerCache.getInstance().getEndpointURLFromNhinTarget(target,
                        NhincConstants.HIEM_NOTIFY_SERVICE_NAME);
            } catch (ConnectionManagerException ex) {
                log.error("Error: Failed to retrieve url for service: " + NhincConstants.HIEM_NOTIFY_SERVICE_NAME);
                log.error(ex.getMessage());
            }
        } else {
            log.error("Target system passed into the proxy is null");
        }

        try {
            if (NullChecker.isNotNullish(url)) {

                // Policy check
                log.debug("Calling checkPolicy");
                if (checkPolicy(notify, assertion)) {
                    NotificationConsumer port = getPort(url, assertion);
                    WebServiceProxyHelper wsHelper = new WebServiceProxyHelper();
                    wsHelper.addTargetCommunity(((BindingProvider)port), target);

                    log.debug("attaching reference parameter headers");
                    List<Header> headers = oProxyHelper.createWSAddressingHeaders((WSBindingProvider) port,
                            WS_ADDRESSING_ACTION, assertion);
                    SoapUtil soapUtil = new SoapUtil();
                    soapUtil.attachReferenceParameterElements((WSBindingProvider) port, referenceParametersElements,
                            headers);

                    try {
                        auditInputMessage(notify, assertion,
                            NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

                        log.debug("Calling notification consumer port in NhinHiemWebServiceProxy.");
                        // The proxyhelper invocation casts exceptions to generic Exception, trying to use the default
                        // method invocation
                        oProxyHelper.invokePort(port, port.getClass(), NOTIFY_SERVICE, notify);
                    } catch (Exception ex) {
                        log.error("Error occurred while trying to invoke notify", ex);
                    }
                } else {
                    log.error("Failed policy check on send NHIN notify message");
                }
            } else {
                log.error("The URL for service: " + NhincConstants.HIEM_NOTIFY_SERVICE_NAME + " is null");
            }
        } catch (Throwable t) {
            log.error("Error sending notify to remote gateway: " + t.getMessage(), t);
        }
    }

    protected NotificationConsumer getPort(String url, AssertionType assertion) {
        NotificationConsumer oPort = null;
        try {
            Service oService = getService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);
            if (oService != null) {
                log.debug("subscribe() Obtained service - creating port.");
                oPort = oService.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), NotificationConsumer.class);

                // Initialize secured port
                getWebServiceProxyHelper().initializeSecurePort((BindingProvider) oPort, url,
                        NhincConstants.SUBSCRIBE_ACTION, null, assertion);
            } else {
                log.error("Unable to obtain service - no port created.");
            }
        } catch (Throwable t) {
            log.error("Error creating service: " + t.getMessage(), t);
        }
        return oPort;
    }

    private boolean checkPolicy(Notify notify, AssertionType assertion) {
        log.debug("In NhinHiemNotifyWebServiceProxy.checkPolicy");
        boolean policyIsValid = false;

        NotifyEventType policyCheckReq = new NotifyEventType();
        policyCheckReq.setDirection(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION);
        gov.hhs.fha.nhinc.common.eventcommon.NotifyMessageType request = new gov.hhs.fha.nhinc.common.eventcommon.NotifyMessageType();
        request.setAssertion(assertion);
        request.setNotify(notify);
        policyCheckReq.setMessage(request);

        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
        CheckPolicyRequestType policyReq = policyChecker.checkPolicyNotify(policyCheckReq);
        policyReq.setAssertion(assertion);
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();

        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq, assertion);

        if (policyResp.getResponse() != null && NullChecker.isNotNullish(policyResp.getResponse().getResult())
                && policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT) {
            policyIsValid = true;
        }

        log.debug("Finished NhinHiemNotifyWebServiceProxy.checkPolicy - valid: " + policyIsValid);
        return policyIsValid;
    }

    /**
     * Create a generic log for Input messages.
     * @param notify The notify message to be audited
     * @param assertion The assertion element to be audited
     * @param direction The direction of the log to be audited (Inbound or Outbound)
     * @param logInterface The interface of the log to be audited (NHIN or Adapter)
     */
    private void auditInputMessage(Notify notify, AssertionType assertion, String direction,
            String logInterface) {
        log.debug("In NhinHiemNotifyWebServiceProxy.auditInputMessage");
        try {
            AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

            gov.hhs.fha.nhinc.common.nhinccommoninternalorch.NotifyRequestType message = new gov.hhs.fha.nhinc.common.nhinccommoninternalorch.NotifyRequestType();
            message.setAssertion(assertion);
            message.setNotify(notify);

            LogEventRequestType auditLogMsg = auditLogger.logNhinNotifyRequest(message,
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
