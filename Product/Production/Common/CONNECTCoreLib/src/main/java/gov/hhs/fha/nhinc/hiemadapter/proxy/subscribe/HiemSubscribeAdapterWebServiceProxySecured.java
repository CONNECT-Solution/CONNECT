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

import com.sun.xml.ws.api.message.Headers;
import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.developer.WSBindingProvider;
import javax.xml.namespace.QName;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.hiemadapter.proxy.subscribe.HiemSubscribeAdapterProxy;

import gov.hhs.fha.nhinc.adaptersubscriptionmanagementsecured.AdapterNotificationProducerSecured;
import gov.hhs.fha.nhinc.adaptersubscriptionmanagementsecured.AdapterNotificationProducerPortSecuredType;

import gov.hhs.fha.nhinc.adaptersubscriptionmanagement.AdapterSubscriptionManager;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.SubscribeResponseMarshaller;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.w3c.dom.Element;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.WsntSubscribeMarshaller;

import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;
import javax.xml.ws.BindingProvider;


/**
 *
 * @author Jon Hoppesch
 */
public class HiemSubscribeAdapterWebServiceProxySecured implements HiemSubscribeAdapterProxy {
    //TODO: consider how to handle the exceptions.  Is there a better choice than relying on "runtime exception"

    private static Log log = LogFactory.getLog(HiemSubscribeAdapterWebServiceProxy.class);
    static AdapterNotificationProducerSecured adapterSubscribeService = new AdapterNotificationProducerSecured();
    static AdapterSubscriptionManager adapterUnsubscribeService = new AdapterSubscriptionManager();

    public Element subscribe(Element subscribeElement, AssertionType assertion, NhinTargetSystemType target) throws Exception {
        Element responseElement = null;
        SubscribeResponse response = null;

        log.debug("start secured subscribe");

        String url = getUrl(target, NhincConstants.HIEM_SUBSCRIBE_ADAPTER_SECURED_SERVICE_NAME);
        AdapterNotificationProducerPortSecuredType port = getPort(url);

        WsntSubscribeMarshaller subscribeMarshaller = new WsntSubscribeMarshaller();
        Subscribe subscribe = subscribeMarshaller.unmarshalUnsubscribeRequest(subscribeElement);


        SamlTokenCreator tokenCreator = new SamlTokenCreator();
        Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.HIEM_NOTIFY_ENTITY_SERVICE_NAME_SECURED);
        ((BindingProvider) port).getRequestContext().putAll(requestContext);

        response = port.subscribe(subscribe);

        SubscribeResponseMarshaller subscribeResponseMarshaller = new SubscribeResponseMarshaller();
        responseElement = subscribeResponseMarshaller.marshal(response);

        log.debug("end secured subscribe");

        return responseElement;
    }

    private AdapterNotificationProducerPortSecuredType getPort(NhinTargetSystemType target) throws ConnectionManagerException {
        String serviceName = NhincConstants.HIEM_SUBSCRIBE_ADAPTER_SECURED_SERVICE_NAME;
        String url = getUrl(target, serviceName);
        return getPort(url);
    }

    private AdapterNotificationProducerPortSecuredType getPort(String url) {
        AdapterNotificationProducerPortSecuredType port = adapterSubscribeService.getAdapterNotificationProducerPortSecuredSoap();

        log.info("Setting endpoint address to Nhin Hiem Subscribe Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        WSBindingProvider bp = (WSBindingProvider) port;

//        bp.setOutboundHeaders(Headers.create(new QName("urn:test", "testheader", "myprefix"), "sampleValue"));

        return port;
    }

    private String getUrl(NhinTargetSystemType target, String serviceName) throws ConnectionManagerException {
        String url = null;
        url = ConnectionManagerCache.getInstance().getEndpontURLFromNhinTarget(target, serviceName);
        if (NullChecker.isNullish(url)) {
            url = ConnectionManagerCache.getInstance().getLocalEndpointURLByServiceName(serviceName);
        }
        return url;
    }
}
