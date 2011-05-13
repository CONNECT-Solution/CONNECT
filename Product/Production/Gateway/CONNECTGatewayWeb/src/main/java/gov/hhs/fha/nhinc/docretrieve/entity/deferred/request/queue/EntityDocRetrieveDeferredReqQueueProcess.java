/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docretrieve.entity.deferred.request.queue;

import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 *
 * @author narendra.reddy
 */
@WebService(serviceName = "EntityDocRetrieveDeferredReqQueueProcess")
public class EntityDocRetrieveDeferredReqQueueProcess {

    /**
     * processDocRetreiveDeferredReqQueue WebMethod for processing request queues on responding gateway
     * @param messageId
     * @return String
     */
    @WebMethod(operationName = "processDocRetreiveDeferredReqQueue")
    public String processDocRetreiveDeferredReqQueue(@WebParam(name = "messageId") String messageId) {
        String result = "Failed";

        DocRetrieveAcknowledgementType docRetrieveAck = new DocRetrieveAcknowledgementType();
        EntityDocRetrieveDeferredReqQueueProcessImpl entityDocRetrieveDeferredReqQueueProcessImpl = new EntityDocRetrieveDeferredReqQueueProcessImpl();
        docRetrieveAck = entityDocRetrieveDeferredReqQueueProcessImpl.processDocRetreiveDeferredReqQueue(messageId);
        if (docRetrieveAck != null) {
            result = "Success";
        }
        return result;

    }
}
