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

package gov.hhs.fha.nhinc.docquery.passthru.deferred.response;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;
/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "NhincProxyDocQueryDeferredResponse", portName = "NhincProxyDocQueryDeferredResponsePortSoap", endpointInterface = "gov.hhs.fha.nhinc.nhincproxydocquerydeferredresponse.NhincProxyDocQueryDeferredResponsePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxydocquerydeferredresponse", wsdlLocation = "WEB-INF/wsdl/PassthruDocQueryDeferredResponseUnsecured/NhincProxyDocQueryDeferredResponse.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
@Addressing(enabled=true)
public class PassthruDocQueryDeferredResponseUnsecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQueryResponseType respondingGatewayCrossGatewayQueryRequest) {
        return new PassthruDocQueryDeferredResponseUnsecuredImpl().respondingGatewayCrossGatewayQuery(respondingGatewayCrossGatewayQueryRequest, context);
    }

}
