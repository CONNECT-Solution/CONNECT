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
package gov.hhs.fha.nhinc.hiemadapter.proxy.unsubscribe;

import com.sun.xml.ws.developer.WSBindingProvider;

import gov.hhs.fha.nhinc.adaptersubscriptionmanagementsecured.AdapterSubscriptionManagerPortSecuredType;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.UnsubscribeRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersElements;
import gov.hhs.fha.nhinc.hiem.dte.SoapUtil;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.WsntUnsubscribeMarshaller;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.WsntUnsubscribeResponseMarshaller;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Unsubscribe;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;
import org.w3c.dom.Element;

/**
 * 
 * @author rayj
 */
public class HiemUnsubscribeAdapterWebServiceProxySecured implements HiemUnsubscribeAdapterProxy {

    private static Log log = LogFactory.getLog(HiemUnsubscribeAdapterWebServiceProxy.class);
    // static AdapterSubscriptionManagerSecured service = new AdapterSubscriptionManagerSecured();

    private static Service cachedService = null;
    private static WebServiceProxyHelper oProxyHelper = null;

    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:adaptersubscriptionmanagementsecured";
    private static final String SERVICE_LOCAL_PART = "AdapterSubscriptionManagerSecured";
    private static final String PORT_LOCAL_PART = "AdapterSubscriptionManagerPortSoap";
    private static final String WSDL_FILE = "AdapterSubscriptionManagementSecured.wsdl";

    public Element unsubscribe(Element unsubscribeElement, ReferenceParametersElements referenceParametersElements,
            AssertionType assertion, NhinTargetSystemType target) {
        Element responseElement = null;

        log.debug("start secured unsubscribe");
        String url = getUrl(target, NhincConstants.HIEM_UNSUBSCRIBE_ADAPTER_SERVICE_SECURED_NAME);

        AdapterSubscriptionManagerPortSecuredType port = getPort(url, assertion);

        if (port != null) {
            log.debug("attaching reference parameter headers");
            SoapUtil soapUtil = new SoapUtil();
            soapUtil.attachReferenceParameterElements((WSBindingProvider) port, referenceParametersElements);

            log.debug("unmarshalling unsubscribe element");
            WsntUnsubscribeMarshaller unsubscribeMarshaller = new WsntUnsubscribeMarshaller();
            Unsubscribe subscribe = unsubscribeMarshaller.unmarshal(unsubscribeElement);

            log.debug("building unsubscribe message");
            UnsubscribeRequestType adapterUnsubscribeRequest = new UnsubscribeRequestType();
            adapterUnsubscribeRequest.setUnsubscribe(subscribe);
            adapterUnsubscribeRequest.setAssertion(assertion);

            log.debug("invoking unsubscribe port");
            // The proxyhelper invocation casts exceptions to generic Exception, trying to use the default method
            // invocation
            UnsubscribeResponse response = port.unsubscribe(subscribe);

            log.debug("building response");
            WsntUnsubscribeResponseMarshaller unsubscribeResponseMarshaller = new WsntUnsubscribeResponseMarshaller();
            responseElement = unsubscribeResponseMarshaller.marshal(response);
        } else {
            throw new RuntimeException("Unable to create adapter port");
        }

        log.debug("end secured unsubscribe");
        return responseElement;
    }

    private String getUrl(NhinTargetSystemType target, String serviceName) {
        String url = null;
        try {
            url = ConnectionManagerCache.getInstance().getEndpontURLFromNhinTarget(target, serviceName);
        } catch (ConnectionManagerException ex) {
            log.warn("exception occurred accessing url from connection manager (getEndpontURLFromNhinTarget)", ex);
        }
        if (NullChecker.isNullish(url)) {
            try {
                url = ConnectionManagerCache.getInstance().getLocalEndpointURLByServiceName(serviceName);
            } catch (ConnectionManagerException ex) {
                log.warn("exception occurred accessing url from connection manager (getLocalEndpointURLByServiceName)",
                        ex);
            }
        }
        return url;
    }

    private AdapterSubscriptionManagerPortSecuredType getPort(String url, AssertionType assertIn) {
        AdapterSubscriptionManagerPortSecuredType oPort = null;
        try {
            Service oService = getService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);

            if (oService != null) {
                log.debug("subscribe() Obtained service - creating port.");
                oPort = oService.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART),
                        AdapterSubscriptionManagerPortSecuredType.class);

                // Initialize secured port
                getWebServiceProxyHelper().initializeSecurePort((BindingProvider) oPort, url,
                        NhincConstants.HIEM_NOTIFY_ENTITY_SERVICE_NAME_SECURED, null, assertIn);
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
