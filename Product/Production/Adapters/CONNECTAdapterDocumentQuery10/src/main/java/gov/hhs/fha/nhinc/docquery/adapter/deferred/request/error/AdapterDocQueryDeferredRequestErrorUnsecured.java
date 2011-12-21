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

package gov.hhs.fha.nhinc.docquery.adapter.deferred.request.error;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "AdapterDocQueryDeferredRequestError", portName = "AdapterDocQueryDeferredRequestErrorPortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterdocquerydeferredrequesterror.AdapterDocQueryDeferredRequestErrorPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterdocquerydeferredrequesterror", wsdlLocation = "WEB-INF/wsdl/AdapterDocQueryDeferredRequestErrorUnsecured/AdapterDocQueryDeferredRequestError.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterDocQueryDeferredRequestErrorUnsecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterDocumentQueryDeferredRequestErrorType respondingGatewayCrossGatewayQueryRequest) {
        return new AdapterDocQueryDeferredRequestErrorUnsecuredImpl().respondingGatewayCrossGatewayQuery(respondingGatewayCrossGatewayQueryRequest, context);
    }

}
