/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.subscription.repository.util;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "HiemSubscriptionRepositoryUtilService", portName = "HiemSubscriptionRepositoryUtilPortTypeBindingPort", endpointInterface = "gov.hhs.fha.nhinc.hiemsubscriptionrepositoryutil.HiemSubscriptionRepositoryUtilPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:hiemsubscriptionrepositoryutil", wsdlLocation = "WEB-INF/wsdl/HiemSubscriptionRepositoryUtilService/HiemSubscriptionRepositoryUtil.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class HiemSubscriptionRepositoryUtilService
{

    public gov.hhs.fha.nhinc.gateway.hiemsubscriptionrepositoryutil.GetSubscriptionCountResponseType getSubscriptionCount(gov.hhs.fha.nhinc.gateway.hiemsubscriptionrepositoryutil.GetSubscriptionCountRequestType getSubscriptionCountRequest)
    {
        return new HiemSubscriptionRepositoryUtilServiceImpl().getSubscriptionCount(getSubscriptionCountRequest);
    }

    public gov.hhs.fha.nhinc.gateway.hiemsubscriptionrepositoryutil.EmptySubscriptionRepositoryResponseType emptySubscriptionRepository(gov.hhs.fha.nhinc.gateway.hiemsubscriptionrepositoryutil.EmptySubscriptionRepositoryRequestType emptySubscriptionRepositoryRequest)
    {
        return new HiemSubscriptionRepositoryUtilServiceImpl().emptySubscriptionRepository(emptySubscriptionRepositoryRequest);
    }

}
