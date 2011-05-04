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

/**
 *
 * @author narendra.reddy
 */
public class EntityDocQueryDeferredReqQueueProcessImpl {

    public EntityDocQueryDeferredReqQueueProcessImpl() {
    }

    /**
     * processDocQueryAsyncReqQueue Implementation method for processing request queues on reponding gateway
     * @param messageId
     * @return DocQueryAcknowledgementType
     */
    public DocQueryAcknowledgementType processDocQueryDeferredReqQueue(String messageId) {
        DocQueryAcknowledgementType docQueryAck = new DocQueryAcknowledgementType();
        EntityDocQueryDeferredReqQueueProcessOrchImpl entityDocQueryDeferredReqQueueProcessOrchImpl = new EntityDocQueryDeferredReqQueueProcessOrchImpl();
        docQueryAck = entityDocQueryDeferredReqQueueProcessOrchImpl.processDocQueryAsyncReqQueue(messageId);
        return docQueryAck;
    }
}
