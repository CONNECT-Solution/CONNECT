/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.hiemadapter.proxy.notify;

import com.sun.xml.ws.developer.WSBindingProvider;
import gov.hhs.fha.nhinc.adapternotificationconsumer.AdapterNotificationConsumer;
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
import gov.hhs.fha.nhinc.hiem.dte.marshallers.SubscribeResponseMarshaller;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.WsntSubscribeMarshaller;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Notify;
import org.w3c.dom.*;

/**
 *
 * @author Jon Hoppesch
 */
public class HiemNotifyAdapterWebServiceProxy implements HiemNotifyAdapterProxy {

    private static Log log = LogFactory.getLog(HiemNotifyAdapterWebServiceProxy.class);
    static AdapterNotificationConsumer adapterNotifyService = new AdapterNotificationConsumer();

    public Element notify(Element notifyElement, ReferenceParametersElements referenceParametersElements, AssertionType assertion, NhinTargetSystemType target) throws Exception {
        Element responseElement = null;
        AcknowledgementType response = null;
        AdapterNotificationConsumerPortType port = getPort();

        WsntSubscribeMarshaller subscribeMarshaller = new WsntSubscribeMarshaller();
        Notify notify = subscribeMarshaller.unmarshalNotifyRequest(notifyElement);

        NotifyRequestType adapternotifyRequest = new NotifyRequestType();
        adapternotifyRequest.setNotify(notify);
        adapternotifyRequest.setAssertion(assertion);

        log.debug("attaching reference parameter headers");
        SoapUtil soapUtil = new SoapUtil();
        soapUtil.attachReferenceParameterElements((WSBindingProvider) port, referenceParametersElements);

        response = port.notify(adapternotifyRequest);

        NhincCommonAcknowledgementMarshaller acknowledgementMarshaller = new NhincCommonAcknowledgementMarshaller();
        responseElement = acknowledgementMarshaller.marshal(response);

        return responseElement;
    }

    public Element notifySubscribersOfDocument(Element docNotify, AssertionType assertion, NhinTargetSystemType target) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Element notifySubscribersOfCdcBioPackage(Element cdcNotify, AssertionType assertion, NhinTargetSystemType target) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private AdapterNotificationConsumerPortType getPort() {
        AdapterNotificationConsumerPortType port = adapterNotifyService.getAdapterNotificationConsumerPortSoap();
        String url = null;

        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.HIEM_NOTIFY_ADAPTER_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.HIEM_NOTIFY_ADAPTER_SERVICE_NAME + " for local home community");
            log.error(ex.getMessage());
        }

        log.info("Setting endpoint address to Adapter Hiem Notify Service to " + url);
        ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        return port;
    }
}
