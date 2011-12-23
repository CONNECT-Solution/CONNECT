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

package gov.hhs.fha.nhinc.docretrieve.adapter.deferred.response;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterDocRetrieveDeferredResponseSecured", portName = "AdapterDocRetrieveDeferredResponseSecuredPortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterdocretrievedeferredrespsecured.AdapterDocRetrieveDeferredResponseSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterdocretrievedeferredrespsecured", wsdlLocation = "WEB-INF/wsdl/AdapterDocRetrieveDeferredResponseSecured/AdapterDocRetrieveDeferredRespSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterDocRetrieveDeferredResponseSecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredResponseType body) {
        return new AdapterDocRetrieveDeferredResponseImpl().crossGatewayRetrieveResponse(body, context);
    }

}
