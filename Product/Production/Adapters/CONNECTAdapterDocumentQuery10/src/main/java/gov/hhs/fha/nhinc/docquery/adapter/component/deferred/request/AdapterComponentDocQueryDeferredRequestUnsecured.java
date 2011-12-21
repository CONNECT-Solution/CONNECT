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

package gov.hhs.fha.nhinc.docquery.adapter.component.deferred.request;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "AdapterComponentDocQueryDeferredRequest", portName = "AdapterComponentDocQueryDeferredRequestPortSoap", endpointInterface = "gov.hhs.fha.nhinc.adaptercomponentdocquerydeferredrequest.AdapterComponentDocQueryDeferredRequestPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adaptercomponentdocquerydeferredrequest", wsdlLocation = "WEB-INF/wsdl/AdapterComponentDocQueryDeferredRequestUnsecured/AdapterComponentDocQueryDeferredRequest.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterComponentDocQueryDeferredRequestUnsecured {

    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequestType respondingGatewayCrossGatewayQueryRequest) {
        return new AdapterComponentDocQueryDeferredRequestUnsecuredImpl().respondingGatewayCrossGatewayQuery(respondingGatewayCrossGatewayQueryRequest, context);
    }

}
