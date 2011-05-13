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

/**
 *
 * @author narendra.reddy
 */
public class EntityDocRetrieveDeferredReqQueueProcessImpl {

    public EntityDocRetrieveDeferredReqQueueProcessImpl() {
    }

    /**
     * processDocRetreiveDeferredReqQueue Implementation method for processing request queues on reponding gateway
     * @param messageId
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType processDocRetreiveDeferredReqQueue(String messageId) {
        // RetrieveDocumentSetResponseType docRetrieveAck = new RetrieveDocumentSetResponseType();
        DocRetrieveAcknowledgementType docRetrieveAck = new DocRetrieveAcknowledgementType();
        EntityDocRetrieveDeferredReqQueueProcessOrchImpl entityDocQueryDeferredReqQueueProcessOrchImpl = new EntityDocRetrieveDeferredReqQueueProcessOrchImpl();
        docRetrieveAck = entityDocQueryDeferredReqQueueProcessOrchImpl.processDocRetrieveDeferredReqQueue(messageId);
        return docRetrieveAck;
    }
}
