/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.hiem.entity.unsubscribe;

import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.ResourceUnknownFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.UnableToDestroySubscriptionFault;
import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import javax.jws.HandlerChain;
import javax.xml.ws.BindingType;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "EntitySubscriptionManagerSecured", portName = "EntitySubscriptionManagerSecuredPortSoap", endpointInterface = "gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.EntitySubscriptionManagerSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitysubscriptionmanagementsecured", wsdlLocation = "WEB-INF/wsdl/EntitySubscribeSecuredService/EntitySubscriptionManagementSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "EntityUnsubscribeSoapHeaderHandler.xml")
public class EntityUnsubscribeSecuredService
{
    @Resource
    private WebServiceContext context;

    public org.oasis_open.docs.wsn.b_2.UnsubscribeResponse unsubscribe(org.oasis_open.docs.wsn.b_2.Unsubscribe unsubscribeRequestSecured) throws ResourceUnknownFault, UnableToDestroySubscriptionFault
    {
        return new EntityUnsubscribeServiceImpl().unsubscribe(unsubscribeRequestSecured, context);
    }

}
