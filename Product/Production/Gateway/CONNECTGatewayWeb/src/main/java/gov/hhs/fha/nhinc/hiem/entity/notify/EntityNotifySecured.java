/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.hiem.entity.notify;

import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import javax.jws.HandlerChain;
import javax.xml.ws.BindingType;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "EntityNotificationConsumerSecured", portName = "EntityNotificationConsumerSecuredPortSoap", endpointInterface = "gov.hhs.fha.nhinc.entitynotificationconsumersecured.EntityNotificationConsumerSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitynotificationconsumersecured", wsdlLocation = "WEB-INF/wsdl/EntityNotifySecured/EntityNotificationConsumerSecured.wsdl")
@HandlerChain(file = "EntityNotifySoapHeaderHandler.xml")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class EntityNotifySecured
{
    @Resource
    private WebServiceContext context;

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType notifySubscribersOfDocument(gov.hhs.fha.nhinc.common.nhinccommonentity.NotifySubscribersOfDocumentRequestSecuredType notifySubscribersOfDocumentRequestSecured)
    {
        throw new UnsupportedOperationException("Not implemented.");
    }

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType notify(org.oasis_open.docs.wsn.b_2.Notify notifyRequestSecured)
    {
        return new EntityNotifyServiceImpl().notify(notifyRequestSecured, context);
    }

}
