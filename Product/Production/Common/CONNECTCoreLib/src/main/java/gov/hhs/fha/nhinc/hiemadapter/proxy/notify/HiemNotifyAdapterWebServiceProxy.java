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
package gov.hhs.fha.nhinc.hiemadapter.proxy.notify;

import com.sun.xml.ws.developer.WSBindingProvider;
import gov.hhs.fha.nhinc.adapternotificationconsumer.AdapterNotificationConsumerPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.NotifyRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersElements;
import gov.hhs.fha.nhinc.hiem.dte.SoapUtil;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.NhincCommonAcknowledgementMarshaller;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.WsntSubscribeMarshaller;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Notify;
import org.w3c.dom.*;
import javax.xml.ws.Service;

/**
 * 
 * @author Jon Hoppesch
 */
public class HiemNotifyAdapterWebServiceProxy implements HiemNotifyAdapterProxy {

    private static Log log = LogFactory.getLog(HiemNotifyAdapterWebServiceProxy.class);
    private static Service cachedService = null;
    private static WebServiceProxyHelper oProxyHelper = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:adapternotificationconsumer";
    private static final String SERVICE_LOCAL_PART = "AdapterNotificationConsumer";
    private static final String PORT_LOCAL_PART = "AdapterNotificationConsumerPortSoap";
    private static final String WSDL_FILE = "AdapterNotificationConsumer.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:Notify";

    public Element notify(Element notifyElement, ReferenceParametersElements referenceParametersElements,
            AssertionType assertion, NhinTargetSystemType target) throws Exception {
        Element responseElement = null;
        AcknowledgementType response = null;
        String url = null;

        try {
            url = ConnectionManagerCache.getInstance().getInternalEndpointURLByServiceName(
                    NhincConstants.HIEM_NOTIFY_ADAPTER_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.HIEM_NOTIFY_ADAPTER_SERVICE_NAME
                    + " for local home community");
            log.error(ex.getMessage());
        }

        AdapterNotificationConsumerPortType port = getPort(url, assertion);

        WsntSubscribeMarshaller subscribeMarshaller = new WsntSubscribeMarshaller();
        Notify notify = subscribeMarshaller.unmarshalNotifyRequest(notifyElement);

        NotifyRequestType adapternotifyRequest = new NotifyRequestType();
        adapternotifyRequest.setNotify(notify);
        adapternotifyRequest.setAssertion(assertion);

        log.debug("attaching reference parameter headers");
        SoapUtil soapUtil = new SoapUtil();
        soapUtil.attachReferenceParameterElements((WSBindingProvider) port, referenceParametersElements);

        // The proxyhelper invocation casts exceptions to generic Exception, trying to use the default method invocation
        response = port.notify(adapternotifyRequest);

        NhincCommonAcknowledgementMarshaller acknowledgementMarshaller = new NhincCommonAcknowledgementMarshaller();
        responseElement = acknowledgementMarshaller.marshal(response);

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

    private AdapterNotificationConsumerPortType getPort(String url, AssertionType assertIn) {
        AdapterNotificationConsumerPortType oPort = null;
        try {
            Service oService = getService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);

            if (oService != null) {
                log.debug("subscribe() Obtained service - creating port.");
                oPort = oService.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART),
                        AdapterNotificationConsumerPortType.class);

                // Initialize unsecured port
                getWebServiceProxyHelper().initializeUnsecurePort((BindingProvider) oPort, url, WS_ADDRESSING_ACTION,
                        assertIn);
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
