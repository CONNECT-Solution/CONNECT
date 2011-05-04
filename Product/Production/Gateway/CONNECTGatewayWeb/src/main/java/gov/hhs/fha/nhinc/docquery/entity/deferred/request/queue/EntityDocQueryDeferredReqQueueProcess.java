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
package gov.hhs.fha.nhinc.docquery.entity.deferred.request.queue;

import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 *
 * @author narendra.reddy
 */
@WebService(serviceName = "EntityDocQueryDeferredReqQueueProcess")
public class EntityDocQueryDeferredReqQueueProcess {

    /**
     * processDocQueryDeferredReqQueue WebMethod for processing request queues on responding gateway
     * @param messageId
     * @return String
     */
    @WebMethod(operationName = "processDocQueryDeferredReqQueue")
    public String processDocQueryDeferredReqQueue(@WebParam(name = "messageId") String messageId) {
        String result = "Failed";
        DocQueryAcknowledgementType docQueryAck = new DocQueryAcknowledgementType();
        EntityDocQueryDeferredReqQueueProcessImpl entityDocQueryDeferredReqQueueProcessImpl = new EntityDocQueryDeferredReqQueueProcessImpl();
        docQueryAck = entityDocQueryDeferredReqQueueProcessImpl.processDocQueryDeferredReqQueue(messageId);
        if (docQueryAck != null) {
            result = "Success";
        }
        return result;

    }
}
