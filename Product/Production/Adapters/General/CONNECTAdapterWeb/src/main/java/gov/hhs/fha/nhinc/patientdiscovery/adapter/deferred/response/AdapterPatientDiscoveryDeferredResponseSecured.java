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

package gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.response;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterPatientDiscoverySecuredAsyncResp", portName = "AdapterPatientDiscoverySecuredAsyncRespPortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterpatientdiscoverysecuredasyncresp.AdapterPatientDiscoverySecuredAsyncRespPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpatientdiscoverysecuredasyncresp", wsdlLocation = "WEB-INF/wsdl/AdapterPatientDiscoveryDeferredResponseSecured/AdapterPatientDiscoverySecuredAsyncResp.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class AdapterPatientDiscoveryDeferredResponseSecured {
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.MCCIIN000002UV01 processPatientDiscoveryAsyncResp(org.hl7.v3.RespondingGatewayPRPAIN201306UV02SecuredRequestType processPatientDiscoveryAsyncRespAsyncRequest) {
        return new AdapterPatientDiscoverySecuredDeferredResponseImpl().processPatientDiscoveryAsyncResp(processPatientDiscoveryAsyncRespAsyncRequest.getPRPAIN201306UV02(), context);
    }

}
