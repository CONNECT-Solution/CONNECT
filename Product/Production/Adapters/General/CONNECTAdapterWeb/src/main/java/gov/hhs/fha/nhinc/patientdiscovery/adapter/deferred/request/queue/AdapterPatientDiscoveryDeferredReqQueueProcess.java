/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.queue;

import gov.hhs.fha.nhinc.gateway.adapterpatientdiscoveryreqqueueprocess.PatientDiscoveryDeferredReqQueueProcessRequestType;
import gov.hhs.fha.nhinc.gateway.adapterpatientdiscoveryreqqueueprocess.PatientDiscoveryDeferredReqQueueProcessResponseType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author richard.ettema
 */
@WebService(serviceName = "AdapterPatientDiscoveryDeferredReqQueueProcess", portName = "AdapterPatientDiscoveryDeferredReqQueueProcessPort", endpointInterface = "gov.hhs.fha.nhinc.adapterpatientdiscoveryreqqueueprocess.AdapterPatientDiscoveryDeferredReqQueueProcessPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpatientdiscoveryreqqueueprocess", wsdlLocation = "WEB-INF/wsdl/AdapterPatientDiscoveryDeferredReqQueueProcess/AdapterPatientDiscoveryDeferredReqQueueProcess.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterPatientDiscoveryDeferredReqQueueProcess {

    @Resource
    private WebServiceContext context;

    protected AdapterPatientDiscoveryDeferredReqQueueProcessImpl getAdapterPatientDiscoveryDeferredReqQueueProcessImpl() {
        return new AdapterPatientDiscoveryDeferredReqQueueProcessImpl();
    }

    /**
     * processPatientDiscoveryDeferredReqQueue WebMethod for processing request queues on reponding gateway
     * @param request
     * @return response
     */
    public PatientDiscoveryDeferredReqQueueProcessResponseType processPatientDiscoveryDeferredReqQueue(PatientDiscoveryDeferredReqQueueProcessRequestType request) {
        return getAdapterPatientDiscoveryDeferredReqQueueProcessImpl().processPatientDiscoveryDeferredReqQueue(request, context);
    }

}
