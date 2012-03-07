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
package gov.hhs.fha.nhinc.hiemadapter.proxy.subscribe;

import gov.hhs.fha.nhinc.adaptersubscriptionmanagementsecured.AdapterNotificationProducerPortSecuredType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.SubscribeResponseMarshaller;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.WsntSubscribeMarshaller;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.w3c.dom.Element;

/**
 * 
 * @author Jon Hoppesch
 */
public class HiemSubscribeAdapterWebServiceProxySecured implements HiemSubscribeAdapterProxy {

    private static Log log = LogFactory.getLog(HiemSubscribeAdapterWebServiceProxy.class);

    private static Service cachedService = null;
    private static WebServiceProxyHelper oProxyHelper = null;

    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:adaptersubscriptionmanagementsecured";
    private static final String SERVICE_LOCAL_PART = "AdapterNotificationProducerSecured";
    private static final String PORT_LOCAL_PART = "AdapterNotificationProducerPortSecuredSoap";
    private static final String WSDL_FILE = "AdapterSubscriptionManagementSecured.wsdl";

    public Element subscribe(Element subscribeElement, AssertionType assertion, NhinTargetSystemType target)
            throws Exception {
        Element responseElement = null;
        SubscribeResponse response = null;

        log.debug("start secured subscribe");

        String url = getWebServiceProxyHelper().getAdapterEndPointFromConnectionManager(NhincConstants.HIEM_SUBSCRIBE_ADAPTER_SECURED_SERVICE_NAME);
        if (NullChecker.isNotNullish(url)) {
            AdapterNotificationProducerPortSecuredType port = getPort(url, assertion);

            WsntSubscribeMarshaller subscribeMarshaller = new WsntSubscribeMarshaller();
            Subscribe subscribe = subscribeMarshaller.unmarshalUnsubscribeRequest(subscribeElement);

            // The proxyhelper invocation casts exceptions to generic Exception, trying to use the default method
            // invocation
            response = port.subscribe(subscribe);

            SubscribeResponseMarshaller subscribeResponseMarshaller = new SubscribeResponseMarshaller();
            responseElement = subscribeResponseMarshaller.marshal(response);
        } else {
            log.error("Failed to call the web service (" + NhincConstants.HIEM_SUBSCRIBE_ADAPTER_SECURED_SERVICE_NAME
                    + ").  The URL is null.");
        }

        log.debug("end secured subscribe");

        return responseElement;
    }

    protected AdapterNotificationProducerPortSecuredType getPort(String url, AssertionType assertIn) {
        AdapterNotificationProducerPortSecuredType oPort = null;
        try {
            Service oService = getService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);

            if (oService != null) {
                log.debug("subscribe() Obtained service - creating port.");
                oPort = oService.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART),
                        AdapterNotificationProducerPortSecuredType.class);

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
