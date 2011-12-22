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

package gov.hhs.fha.nhinc.docquery.passthru.deferred.request;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 * Unsecured Nhin proxy for DocQueryDeferredRequest service
 *
 * @author patlollav
 */
@WebService(serviceName = "NhincProxyDocQueryDeferredRequest", portName = "NhincProxyDocQueryDeferredRequestPortSoap", endpointInterface = "gov.hhs.fha.nhinc.nhincproxydocquerydeferredrequest.NhincProxyDocQueryDeferredRequestPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxydocquerydeferredrequest", wsdlLocation = "WEB-INF/wsdl/PassthruDocQueryDeferredRequestUnsecured/NhincProxyDocQueryDeferredRequest.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled=true)
public class PassthruDocQueryDeferredRequestUnsecured {

    @Resource
    private WebServiceContext context;


    /**
     * Delegates method call to Implementation class in the core library
     *
     * @param crossGatewayQueryRequest
     * @return
     */
    public gov.hhs.healthit.nhin.DocQueryAcknowledgementType crossGatewayQueryRequest(
            gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQueryRequestType crossGatewayQueryRequest) {
        PassthruDocQueryDeferredRequestUnsecuredImpl oImpl = new PassthruDocQueryDeferredRequestUnsecuredImpl();
        return oImpl.crossGatewayQueryRequest(crossGatewayQueryRequest, context);
    }

}
