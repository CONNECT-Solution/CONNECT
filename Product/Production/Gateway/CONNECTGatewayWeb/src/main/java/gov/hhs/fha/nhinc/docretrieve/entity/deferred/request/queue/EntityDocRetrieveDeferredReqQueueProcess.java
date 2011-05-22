/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.docretrieve.entity.deferred.request.queue;

import gov.hhs.fha.nhinc.gateway.entitydocretrievereqqueueprocess.DocRetrieveDeferredReqQueueProcessRequestType;
import gov.hhs.fha.nhinc.gateway.entitydocretrievereqqueueprocess.DocRetrieveDeferredReqQueueProcessResponseType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author richard.ettema
 */
@WebService(serviceName = "EntityDocRetrieveDeferredReqQueueProcess", portName = "EntityDocRetrieveDeferredReqQueueProcessPort", endpointInterface = "gov.hhs.fha.nhinc.entitydocretrievereqqueueprocess.EntityDocRetrieveDeferredReqQueueProcessPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitydocretrievereqqueueprocess", wsdlLocation = "WEB-INF/wsdl/EntityDocRetrieveDeferredReqQueueProcess/EntityDocRetrieveDeferredReqQueueProcess.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class EntityDocRetrieveDeferredReqQueueProcess {

    @Resource
    private WebServiceContext context;

    /**
     * processDocRetrieveDeferredReqQueue WebMethod for processing request queues on reponding gateway
     * @param request
     * @return response
     */
    public DocRetrieveDeferredReqQueueProcessResponseType processDocRetrieveDeferredReqQueue(DocRetrieveDeferredReqQueueProcessRequestType request) {
        return new EntityDocRetrieveDeferredReqQueueProcessImpl().processDocRetrieveDeferredReqQueue(request, context);
    }
}
