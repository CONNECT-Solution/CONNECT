/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.docretrieve.adapter.deferred.request.queue;

import gov.hhs.fha.nhinc.gateway.adapterdocretrievereqqueueprocess.DocRetrieveDeferredReqQueueProcessRequestType;
import gov.hhs.fha.nhinc.gateway.adapterdocretrievereqqueueprocess.DocRetrieveDeferredReqQueueProcessResponseType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author richard.ettema
 */
@WebService(serviceName = "AdapterDocRetrieveDeferredReqQueueProcess", portName = "AdapterDocRetrieveDeferredReqQueueProcessPort", endpointInterface = "gov.hhs.fha.nhinc.adapterdocretrievereqqueueprocess.AdapterDocRetrieveDeferredReqQueueProcessPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterdocretrievereqqueueprocess", wsdlLocation = "WEB-INF/wsdl/AdapterDocRetrieveDeferredReqQueueProcess/AdapterDocRetrieveDeferredReqQueueProcess.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterDocRetrieveDeferredReqQueueProcess {

    @Resource
    private WebServiceContext context;

    protected AdapterDocRetrieveDeferredReqQueueProcessImpl getAdapterDocRetrieveDeferredReqQueueProcessImpl() {
        return new AdapterDocRetrieveDeferredReqQueueProcessImpl();
    }

    /**
     * processDocRetrieveDeferredReqQueue WebMethod for processing request queues on reponding gateway
     * @param request
     * @return response
     */
    public DocRetrieveDeferredReqQueueProcessResponseType processDocRetrieveDeferredReqQueue(DocRetrieveDeferredReqQueueProcessRequestType request) {
        return getAdapterDocRetrieveDeferredReqQueueProcessImpl().processDocRetrieveDeferredReqQueue(request, context);
    }
}
