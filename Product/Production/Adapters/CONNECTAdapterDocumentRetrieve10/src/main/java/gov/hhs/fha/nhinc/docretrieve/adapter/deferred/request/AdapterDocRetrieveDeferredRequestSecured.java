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

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterDocRetrieveDeferredRequestSecured", portName = "AdapterDocRetrieveDeferredRequestSecuredPortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterdocretrievedeferredreqsecured.AdapterDocRetrieveDeferredRequestSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterdocretrievedeferredreqsecured", wsdlLocation = "WEB-INF/wsdl/AdapterDocRetrieveDeferredRequestSecured/AdapterDocRetrieveDeferredReqSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterDocRetrieveDeferredRequestSecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredRequestType body) {
        return new AdapterDocRetrieveDeferredRequestImpl().crossGatewayRetrieveRequest(body, context);
    }

}
