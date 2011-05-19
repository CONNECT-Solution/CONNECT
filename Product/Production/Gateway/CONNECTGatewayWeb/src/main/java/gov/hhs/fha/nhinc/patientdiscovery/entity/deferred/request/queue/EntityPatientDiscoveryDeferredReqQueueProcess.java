/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.queue;

import gov.hhs.fha.nhinc.gateway.entitypatientdiscoveryreqqueueprocess.PatientDiscoveryDeferredReqQueueProcessRequestType;
import gov.hhs.fha.nhinc.gateway.entitypatientdiscoveryreqqueueprocess.PatientDiscoveryDeferredReqQueueProcessResponseType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author richard.ettema
 */
@WebService(serviceName = "EntityPatientDiscoveryDeferredReqQueueProcess", portName = "EntityPatientDiscoveryDeferredReqQueueProcessPort", endpointInterface = "gov.hhs.fha.nhinc.entitypatientdiscoveryreqqueueprocess.EntityPatientDiscoveryDeferredReqQueueProcessPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitypatientdiscoveryreqqueueprocess", wsdlLocation = "WEB-INF/wsdl/EntityPatientDiscoveryDeferredReqQueueProcess/EntityPatientDiscoveryDeferredReqQueueProcess.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class EntityPatientDiscoveryDeferredReqQueueProcess {

    @Resource
    private WebServiceContext context;

    /**
     * processPatientDiscoveryDeferredReqQueue WebMethod for processing request queues on reponding gateway
     * @param request
     * @return response
     */
    public PatientDiscoveryDeferredReqQueueProcessResponseType processPatientDiscoveryDeferredReqQueue(PatientDiscoveryDeferredReqQueueProcessRequestType request) {
        return new EntityPatientDiscoveryDeferredReqQueueProcessImpl().processPatientDiscoveryDeferredReqQueue(request, context);
    }

}
