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
package gov.hhs.fha.nhinc.notify.adapter.proxy;

import gov.hhs.fha.nhinc.adapternotificationconsumer.AdapterNotificationConsumerPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.NotifyRequestType;
import gov.hhs.fha.nhinc.hiem.consumerreference.SoapMessageElements;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.NhincCommonAcknowledgementMarshaller;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.WsntSubscribeMarshaller;
import gov.hhs.fha.nhinc.messaging.client.CONNECTCXFClientFactory;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.notify.adapter.proxy.service.HiemNotifyAdapterServicePortDescriptor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;

import org.apache.log4j.Logger;
import org.oasis_open.docs.wsn.b_2.Notify;
import org.w3c.dom.Element;

/**
 * 
 * @author Jon Hoppesch
 */
public class HiemNotifyAdapterWebServiceProxy implements HiemNotifyAdapterProxy {

    private static final Logger LOG = Logger.getLogger(HiemNotifyAdapterWebServiceProxy.class);

    private static WebServiceProxyHelper oProxyHelper = null;

    protected CONNECTClient<AdapterNotificationConsumerPortType> getCONNECTClientUnsecured(
            ServicePortDescriptor<AdapterNotificationConsumerPortType> portDescriptor, String url,
            AssertionType assertion) {

        return CONNECTCXFClientFactory.getInstance().getCONNECTClientUnsecured(portDescriptor, url, assertion);
    }

    public Element notify(Element notifyElement, SoapMessageElements referenceParametersElements,
            AssertionType assertion, NhinTargetSystemType target) throws Exception {
        Element responseElement = null;

        String url = getWebServiceProxyHelper().getAdapterEndPointFromConnectionManager(
                NhincConstants.HIEM_NOTIFY_ADAPTER_SERVICE_NAME);

        if (NullChecker.isNotNullish(url)) {

            WsntSubscribeMarshaller subscribeMarshaller = new WsntSubscribeMarshaller();
            Notify notify = subscribeMarshaller.unmarshalNotifyRequest(notifyElement);

            NotifyRequestType adapternotifyRequest = new NotifyRequestType();
            adapternotifyRequest.setNotify(notify);
            adapternotifyRequest.setAssertion(assertion);

            // SoapUtil soapUtil = new SoapUtil();
            // soapUtil.attachReferenceParameterElements((WSBindingProvider) port, referenceParametersElements);

            ServicePortDescriptor<AdapterNotificationConsumerPortType> portDescriptor = new HiemNotifyAdapterServicePortDescriptor();

            CONNECTClient<AdapterNotificationConsumerPortType> client = getCONNECTClientUnsecured(portDescriptor, url,
                    assertion);

            AcknowledgementType response = (AcknowledgementType) client.invokePort(
                    AdapterNotificationConsumerPortType.class, "notify", adapternotifyRequest);

            NhincCommonAcknowledgementMarshaller acknowledgementMarshaller = new NhincCommonAcknowledgementMarshaller();
            responseElement = acknowledgementMarshaller.marshal(response);
        } else {
            LOG.error("Failed to call the web service (" + NhincConstants.HIEM_NOTIFY_ADAPTER_SERVICE_NAME
                    + ").  The URL is null.");
        }

        return responseElement;
    }

    public Element notifySubscribersOfDocument(Element docNotify, AssertionType assertion, NhinTargetSystemType target)
            throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Element notifySubscribersOfCdcBioPackage(Element cdcNotify, AssertionType assertion,
            NhinTargetSystemType target) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private WebServiceProxyHelper getWebServiceProxyHelper() {
        if (oProxyHelper == null) {
            oProxyHelper = new WebServiceProxyHelper();
        }
        return oProxyHelper;
    }
}
