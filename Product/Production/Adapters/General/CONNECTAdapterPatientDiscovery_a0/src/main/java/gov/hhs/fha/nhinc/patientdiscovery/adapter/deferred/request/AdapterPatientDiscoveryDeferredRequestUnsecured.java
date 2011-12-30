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

package gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterPatientDiscoveryAsyncReq", portName = "AdapterPatientDiscoveryAsyncReqPortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterpatientdiscoveryasyncreq.AdapterPatientDiscoveryAsyncReqPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpatientdiscoveryasyncreq", wsdlLocation = "WEB-INF/wsdl/AdapterPatientDiscoveryDeferredRequestUnsecured/AdapterPatientDiscoveryAsyncReq.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class AdapterPatientDiscoveryDeferredRequestUnsecured {
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.MCCIIN000002UV01 processPatientDiscoveryAsyncReq(org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType processPatientDiscoveryAsyncReqAsyncRequest) {
        return new AdapterPatientDiscoveryDeferredRequestImpl().processPatientDiscoveryAsyncReq(processPatientDiscoveryAsyncReqAsyncRequest.getPRPAIN201305UV02(), processPatientDiscoveryAsyncReqAsyncRequest.getAssertion(), context);
    }

}
