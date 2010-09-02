/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.notify;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import javax.jws.HandlerChain;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "NotificationConsumerService", portName = "NotificationConsumerPort", endpointInterface = "org.oasis_open.docs.wsn.bw_2.NotificationConsumer", targetNamespace = "http://docs.oasis-open.org/wsn/bw-2", wsdlLocation = "WEB-INF/wsdl/HiemNotify/NhinSubscription.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "SoapHeaderHandler.xml")
public class HiemNotify
{
    @Resource
    private WebServiceContext context;

    public void notify(org.oasis_open.docs.wsn.b_2.Notify notify)
    {
        HiemNotifyImpl.notify(notify, context);
    }

}
