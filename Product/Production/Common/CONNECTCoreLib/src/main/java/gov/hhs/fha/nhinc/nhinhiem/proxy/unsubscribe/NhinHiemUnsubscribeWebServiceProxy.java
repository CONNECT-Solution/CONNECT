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
package gov.hhs.fha.nhinc.nhinhiem.proxy.unsubscribe;

import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;

import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Unsubscribe;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;
import org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault;
import org.oasis_open.docs.wsn.bw_2.SubscriptionManager;
import org.oasis_open.docs.wsn.bw_2.UnableToDestroySubscriptionFault;
import org.w3c.dom.Element;

import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.developer.WSBindingProvider;

import gov.hhs.fha.nhinc.common.eventcommon.UnsubscribeEventType;
import gov.hhs.fha.nhinc.common.eventcommon.UnsubscribeMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersElements;
import gov.hhs.fha.nhinc.hiem.dte.SoapUtil;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.WsntUnsubscribeMarshaller;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.WsntUnsubscribeResponseMarshaller;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;

/**
 *
 * @author rayj
 */
public class NhinHiemUnsubscribeWebServiceProxy implements NhinHiemUnsubscribeProxy {

    private static Log log = LogFactory.getLog(NhinHiemUnsubscribeWebServiceProxy.class);

    private static Service cachedService = null;
    private static WebServiceProxyHelper oProxyHelper = null;
    private static final String NAMESPACE_URI = "http://docs.oasis-open.org/wsn/bw-2";
    private static final String SERVICE_LOCAL_PART = "SubscriptionManagerService";
    private static final String PORT_LOCAL_PART = "SubscriptionManagerPort";
    private static final String WS_ADDRESSING_ACTION = "http://docs.oasis-open.org/wsn/bw-2/SubscriptionManager/UnsubscribeRequest";
    private static final String WSDL_FILE = "NhinSubscription.wsdl";
    private static final String UNSUBSCRIBE_SERVICE = "unsubscribe";

    @Override
    public Element unsubscribe(Element unsubscribeElement, ReferenceParametersElements referenceParametersElements,
            AssertionType assertion, NhinTargetSystemType target) throws ResourceUnknownFault,
            UnableToDestroySubscriptionFault, Exception {
        SubscriptionManager port = getPort(target, assertion);
        WebServiceProxyHelper wsHelper = new WebServiceProxyHelper();
        wsHelper.addTargetCommunity(((BindingProvider)port), target);

        Element responseElement = null;
        UnsubscribeResponse response = null;

        if (port != null) {
            log.debug("attaching reference parameter headers");
            SoapUtil soapUtil = new SoapUtil();
            List<Header> headers = oProxyHelper.createWSAddressingHeaders((WSBindingProvider) port,
                    WS_ADDRESSING_ACTION, assertion);
            soapUtil.attachReferenceParameterElements((WSBindingProvider) port, referenceParametersElements, headers);

            log.debug("unmarshalling unsubscribe element");
            WsntUnsubscribeMarshaller marshaller = new WsntUnsubscribeMarshaller();
            Unsubscribe unsubscribe = marshaller.unmarshal(unsubscribeElement);

            log.debug("invoking unsubscribe invokePort");
            if (checkPolicy(unsubscribe, assertion)) {
                try {
                    response = (UnsubscribeResponse) oProxyHelper.invokePort(port, port.getClass(),
                            UNSUBSCRIBE_SERVICE, unsubscribe);
                } catch (Exception e) {
                    log.error("Exception: " + e.getMessage());
                    throw e;
                }

                log.debug("marshalling unsubscribe response");
                WsntUnsubscribeResponseMarshaller responseMarshaller = new WsntUnsubscribeResponseMarshaller();
                responseElement = responseMarshaller.marshal(response);
            }
        }
        return responseElement;
    }

    /**
     * @param unsubscribe
     * @param assertion
     * @return
     */
    private boolean checkPolicy(Unsubscribe unsubscribe, AssertionType assertion) {
        log.debug("In NhinHiemSubscribeWebServiceProxy.checkPolicy");
        boolean policyIsValid = false;

        UnsubscribeEventType policyCheckReq = new UnsubscribeEventType();
        policyCheckReq.setDirection(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION);
        UnsubscribeMessageType request = new UnsubscribeMessageType();
        request.setAssertion(assertion);
        request.setUnsubscribe(unsubscribe);
        policyCheckReq.setMessage(request);

        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
        CheckPolicyRequestType policyReq = policyChecker.checkPolicyUnsubscribe(policyCheckReq);
        policyReq.setAssertion(assertion);
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq, assertion);

        if (policyResp.getResponse() != null && NullChecker.isNotNullish(policyResp.getResponse().getResult())
                && policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT) {
            policyIsValid = true;
        }

        log.debug("Finished NhinHiemUnsubscribeWebServiceProxy.checkPolicy - valid: " + policyIsValid);
        return policyIsValid;
    }

    private SubscriptionManager getPort(NhinTargetSystemType target, AssertionType assertion) {
        String url = null;
        if (target != null) {
            try {
                url = ConnectionManagerCache.getInstance().getEndpointURLFromNhinTarget(target,
                        NhincConstants.HIEM_SUBSCRIPTION_MANAGER_SERVICE_NAME);
            } catch (ConnectionManagerException ex) {
                log.error("Error: Failed to retrieve url for service: "
                        + NhincConstants.HIEM_SUBSCRIPTION_MANAGER_SERVICE_NAME);
                log.error(ex.getMessage());
            }
        } else {
            log.error("Target system passed into the proxy is null");
        }
        return getPort(url, assertion);
    }

    protected SubscriptionManager getPort(String url, AssertionType assertIn) {
        SubscriptionManager oPort = null;
        try {
            Service oService = getService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);

            if (oService != null) {
                log.debug("subscribe() Obtained service - creating port.");
                oPort = oService.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), SubscriptionManager.class);

                // Initialize secured port
                getWebServiceProxyHelper().initializeSecurePort((BindingProvider) oPort, url,
                        NhincConstants.UNSUBSCRIBE_ACTION, WS_ADDRESSING_ACTION, assertIn);
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
