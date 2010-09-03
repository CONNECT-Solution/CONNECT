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
package gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.queue;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author westberg
 */
@WebService(serviceName = "AdapterPatientDiscoveryAsyncReqQueue", portName = "AdapterPatientDiscoveryAsyncReqQueuePortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterpatientdiscoveryasyncreqqueue.AdapterPatientDiscoveryAsyncReqQueuePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpatientdiscoveryasyncreqqueue", wsdlLocation = "WEB-INF/wsdl/AdapterPatientDiscoveryUnsecuredAsyncReqQueue/AdapterPatientDiscoveryAsyncReqQueue.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class AdapterPatientDiscoveryUnsecuredAsyncReqQueue
{

    @Resource
    private WebServiceContext context;

    public org.hl7.v3.MCCIIN000002UV01 addPatientDiscoveryAsyncReq(org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType addPatientDiscoveryAsyncReqAsyncRequest)
    {
        return new AdapterPatientDiscoverySecuredAsyncReqQueueImpl().addPatientDiscoveryAsyncReqUnsecured(addPatientDiscoveryAsyncReqAsyncRequest, context);
    }
}
