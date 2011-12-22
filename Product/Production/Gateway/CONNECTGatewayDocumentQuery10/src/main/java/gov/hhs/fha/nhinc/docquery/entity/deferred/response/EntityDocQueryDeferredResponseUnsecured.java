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

package gov.hhs.fha.nhinc.docquery.entity.deferred.response;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "EntityDocQueryDeferredResponse", portName = "EntityDocQueryDeferredResponsePortSoap", endpointInterface = "gov.hhs.fha.nhinc.entitydocquerydeferredresponse.EntityDocQueryDeferredResponsePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitydocquerydeferredresponse", wsdlLocation = "WEB-INF/wsdl/EntityDocQueryDeferredResponseUnsecured/EntityDocQueryDeferredResponse.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
@Addressing(enabled=true)
public class EntityDocQueryDeferredResponseUnsecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.DocQueryAcknowledgementType crossGatewayQueryResponse(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryResponseType crossGatewayDocQueryDeferredResponse) {
        return new EntityDocQueryDeferredResponseUnsecuredImpl().crossGatewayQueryResponse(crossGatewayDocQueryDeferredResponse, context);
    }

}
