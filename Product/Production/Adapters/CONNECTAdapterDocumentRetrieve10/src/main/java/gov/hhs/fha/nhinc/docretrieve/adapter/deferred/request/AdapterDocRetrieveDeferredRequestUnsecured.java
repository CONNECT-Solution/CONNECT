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

package gov.hhs.fha.nhinc.docretrieve.adapter.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveRequestType;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterDocRetrieveDeferredRequest", portName = "AdapterDocRetrieveDeferredRequestPortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterdocretrievedeferredreq.AdapterDocRetrieveDeferredRequestPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterdocretrievedeferredreq", wsdlLocation = "WEB-INF/wsdl/AdapterDocRetrieveDeferredRequestUnsecured/AdapterDocRetrieveDeferredReq.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterDocRetrieveDeferredRequestUnsecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest) {
        return new AdapterDocRetrieveDeferredRequestImpl().crossGatewayRetrieveRequest(crossGatewayRetrieveRequest, context);
    }

}
