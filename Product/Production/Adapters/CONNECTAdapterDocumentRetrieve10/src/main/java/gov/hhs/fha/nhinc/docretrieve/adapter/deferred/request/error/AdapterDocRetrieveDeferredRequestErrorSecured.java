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

package gov.hhs.fha.nhinc.docretrieve.adapter.deferred.request.error;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterDocRetrieveDeferredRequestErrorSecuredService", portName = "AdapterDocRetrieveDeferredRequestErrorSecuredPortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterdocretrievedeferredreqerrorsecured.AdapterDocRetrieveDeferredRequestErrorSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterdocretrievedeferredreqerrorsecured", wsdlLocation = "WEB-INF/wsdl/AdapterDocRetrieveDeferredRequestErrorSecured/AdapterDocRetrieveDeferredReqErrorSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterDocRetrieveDeferredRequestErrorSecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType crossGatewayRetrieveRequestError(gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterDocumentRetrieveDeferredRequestErrorSecuredType body) {
        return new AdapterDocRetrieveDeferredRequestErrorImpl().crossGatewayRetrieveRequestError(body, context);
    }

}
