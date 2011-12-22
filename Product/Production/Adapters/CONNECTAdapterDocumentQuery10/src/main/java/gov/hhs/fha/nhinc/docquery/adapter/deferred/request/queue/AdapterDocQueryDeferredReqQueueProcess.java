/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.docquery.adapter.deferred.request.queue;

import gov.hhs.fha.nhinc.gateway.adapterdocqueryreqqueueprocess.DocQueryDeferredReqQueueProcessRequestType;
import gov.hhs.fha.nhinc.gateway.adapterdocqueryreqqueueprocess.DocQueryDeferredReqQueueProcessResponseType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author richard.ettema
 */
@WebService(serviceName = "AdapterDocQueryDeferredReqQueueProcess", portName = "AdapterDocQueryDeferredReqQueueProcessPort", endpointInterface = "gov.hhs.fha.nhinc.adapterdocqueryreqqueueprocess.AdapterDocQueryDeferredReqQueueProcessPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterdocqueryreqqueueprocess", wsdlLocation = "WEB-INF/wsdl/AdapterDocQueryDeferredReqQueueProcess/AdapterDocQueryDeferredReqQueueProcess.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterDocQueryDeferredReqQueueProcess {

    @Resource
    private WebServiceContext context;

    protected AdapterDocQueryDeferredReqQueueProcessImpl getAdapterDocQueryDeferredReqQueueProcessImpl() {
        return new AdapterDocQueryDeferredReqQueueProcessImpl();
    }
    
    /**
     * processDocQueryDeferredReqQueue WebMethod for processing request queues on reponding gateway
     * @param request
     * @return response
     */
    public DocQueryDeferredReqQueueProcessResponseType processDocQueryDeferredReqQueue(DocQueryDeferredReqQueueProcessRequestType request) {
        return getAdapterDocQueryDeferredReqQueueProcessImpl().processDocQueryDeferredReqQueue(request, context);
    }
}
