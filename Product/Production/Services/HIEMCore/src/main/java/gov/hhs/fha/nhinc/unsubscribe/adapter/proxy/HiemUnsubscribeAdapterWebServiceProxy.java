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
package gov.hhs.fha.nhinc.unsubscribe.adapter.proxy;

import gov.hhs.fha.nhinc.adaptersubscriptionmanagement.AdapterSubscriptionManagerPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.UnsubscribeRequestType;
import gov.hhs.fha.nhinc.hiem.consumerreference.SoapMessageElements;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.WsntUnsubscribeMarshaller;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.WsntUnsubscribeResponseMarshaller;
import gov.hhs.fha.nhinc.messaging.client.CONNECTCXFClientFactory;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.unsubscribe.adapter.proxy.service.HiemUnsubscribeAdapterServicePortDescriptor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Unsubscribe;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;
import org.w3c.dom.Element;

/**
 * 
 * @author rayj
 */
public class HiemUnsubscribeAdapterWebServiceProxy implements HiemUnsubscribeAdapterProxy {

    private static Log log = LogFactory.getLog(HiemUnsubscribeAdapterWebServiceProxy.class);

    private static WebServiceProxyHelper oProxyHelper = null;

    protected CONNECTClient<AdapterSubscriptionManagerPortType> getCONNECTClientUnsecured(
            ServicePortDescriptor<AdapterSubscriptionManagerPortType> portDescriptor, String url,
            AssertionType assertion) {

        return CONNECTCXFClientFactory.getInstance().getCONNECTClientUnsecured(portDescriptor, url, assertion);
    }

    public Element unsubscribe(Element unsubscribeElement, SoapMessageElements referenceParametersElements,
            AssertionType assertion, NhinTargetSystemType target) {
        Element responseElement = null;

        try {
            String url = getWebServiceProxyHelper().getAdapterEndPointFromConnectionManager(
                    NhincConstants.HIEM_UNSUBSCRIBE_ADAPTER_SERVICE_NAME);

            if (NullChecker.isNotNullish(url)) {
                // SoapUtil soapUtil = new SoapUtil();
                // soapUtil.attachReferenceParameterElements((WSBindingProvider) port, referenceParametersElements);

                WsntUnsubscribeMarshaller unsubscribeMarshaller = new WsntUnsubscribeMarshaller();
                Unsubscribe subscribe = unsubscribeMarshaller.unmarshal(unsubscribeElement);

                UnsubscribeRequestType adapterUnsubscribeRequest = new UnsubscribeRequestType();
                adapterUnsubscribeRequest.setUnsubscribe(subscribe);
                adapterUnsubscribeRequest.setAssertion(assertion);

                ServicePortDescriptor<AdapterSubscriptionManagerPortType> portDescriptor = new HiemUnsubscribeAdapterServicePortDescriptor();

                CONNECTClient<AdapterSubscriptionManagerPortType> client = getCONNECTClientUnsecured(portDescriptor,
                        url, assertion);

                UnsubscribeResponse response = (UnsubscribeResponse) client.invokePort(
                        AdapterSubscriptionManagerPortType.class, "unsubscribe", adapterUnsubscribeRequest);

                WsntUnsubscribeResponseMarshaller unsubscribeResponseMarshaller = new WsntUnsubscribeResponseMarshaller();
                responseElement = unsubscribeResponseMarshaller.marshal(response);

            } else {
                log.error("Error getting url for: " + NhincConstants.HIEM_UNSUBSCRIBE_ADAPTER_SERVICE_NAME);
            }
        } catch (Exception e) {
            log.error("Failed to send unsubscribe message to adapter", e);
        }

        return responseElement;
    }

    private WebServiceProxyHelper getWebServiceProxyHelper() {
        if (oProxyHelper == null) {
            oProxyHelper = new WebServiceProxyHelper();
        }
        return oProxyHelper;
    }
}
