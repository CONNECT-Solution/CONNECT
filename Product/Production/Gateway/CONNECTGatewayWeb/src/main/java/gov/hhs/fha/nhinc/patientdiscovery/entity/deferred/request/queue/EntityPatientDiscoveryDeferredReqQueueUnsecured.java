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

package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.queue;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;
/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "EntityPatientDiscoveryAsyncReqQueue", portName = "EntityPatientDiscoveryAsyncReqQueuePortSoap", endpointInterface = "gov.hhs.fha.nhinc.entitypatientdiscoveryasyncreqqueue.EntityPatientDiscoveryAsyncReqQueuePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitypatientdiscoveryasyncreqqueue", wsdlLocation = "WEB-INF/wsdl/EntityPatientDiscoveryDeferredReqQueueUnsecured/EntityPatientDiscoveryAsyncReqQueue.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled=true)
public class EntityPatientDiscoveryDeferredReqQueueUnsecured {
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.MCCIIN000002UV01 addPatientDiscoveryAsyncReq(org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType addPatientDiscoveryAsyncReqAsyncRequest) {
        return new EntityPatientDiscoverySecuredDeferredReqQueueImpl().addPatientDiscoveryAsyncReq(addPatientDiscoveryAsyncReqAsyncRequest, context);
    }

}
