/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.docquery.entity.deferred.request.queue;

import gov.hhs.fha.nhinc.gateway.entitydocqueryreqqueueprocess.DocQueryDeferredReqQueueProcessRequestType;
import gov.hhs.fha.nhinc.gateway.entitydocqueryreqqueueprocess.DocQueryDeferredReqQueueProcessResponseType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author richard.ettema
 */
@WebService(serviceName = "EntityDocQueryDeferredReqQueueProcess", portName = "EntityDocQueryDeferredReqQueueProcessPort", endpointInterface = "gov.hhs.fha.nhinc.entitydocqueryreqqueueprocess.EntityDocQueryDeferredReqQueueProcessPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitydocqueryreqqueueprocess", wsdlLocation = "WEB-INF/wsdl/EntityDocQueryDeferredReqQueueProcess/EntityDocQueryDeferredReqQueueProcess.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class EntityDocQueryDeferredReqQueueProcess {

    @Resource
    private WebServiceContext context;

    /**
     * processDocQueryDeferredReqQueue WebMethod for processing request queues on reponding gateway
     * @param request
     * @return response
     */
    public DocQueryDeferredReqQueueProcessResponseType processDocQueryDeferredReqQueue(DocQueryDeferredReqQueueProcessRequestType request) {
        return new EntityDocQueryDeferredReqQueueProcessImpl().processDocQueryDeferredReqQueue(request, context);
    }
}
