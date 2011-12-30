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

package gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.error;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterPatientDiscoveryAsyncReqError", portName = "AdapterPatientDiscoveryAsyncReqErrorPortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterpatientdiscoveryasyncreqerror.AdapterPatientDiscoveryAsyncReqErrorPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpatientdiscoveryasyncreqerror", wsdlLocation = "WEB-INF/wsdl/AdapterPatientDiscoveryDeferredRequestErrorUnsecured/AdapterPatientDiscoveryAsyncReqError.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class AdapterPatientDiscoveryDeferredRequestErrorUnsecured {
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.MCCIIN000002UV01 processPatientDiscoveryAsyncReqError(org.hl7.v3.AsyncAdapterPatientDiscoveryErrorRequestType processPatientDiscoveryAsyncReqErrorRequest) {
        return new AdapterPatientDiscoverySecuredDeferredRequestErrorImpl().processPatientDiscoveryAsyncReqError(processPatientDiscoveryAsyncReqErrorRequest.getPRPAIN201305UV02(), processPatientDiscoveryAsyncReqErrorRequest.getPRPAIN201306UV02(), processPatientDiscoveryAsyncReqErrorRequest.getAssertion(), processPatientDiscoveryAsyncReqErrorRequest.getErrorMsg(), context);
    }

}
