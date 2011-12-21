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

package gov.hhs.fha.nhinc.docquery.adapter.deferred.response;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.service.WebServiceHelper;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterDocQueryDeferredResponse", portName = "AdapterDocQueryDeferredResponsePortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterdocquerydeferredresponse.AdapterDocQueryDeferredResponsePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterdocquerydeferredresponse", wsdlLocation = "WEB-INF/wsdl/AdapterDocQueryDeferredResponseUnsecured/AdapterDocQueryDeferredResponse.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterDocQueryDeferredResponseUnsecured {
    @Resource
    private WebServiceContext context;


    public gov.hhs.healthit.nhin.DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryResponseType respondingGatewayCrossGatewayQueryRequest) {
        return new AdapterDocQueryDeferredResponseUnsecuredImpl().respondingGatewayCrossGatewayQuery(respondingGatewayCrossGatewayQueryRequest, context);
    }

}
