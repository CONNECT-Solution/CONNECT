/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.docretrieve.entity.deferred.request.queue;

import gov.hhs.fha.nhinc.gateway.entitydocqueryreqqueueprocess.DocQueryDeferredReqQueueProcessRequestType;
import gov.hhs.fha.nhinc.gateway.entitydocqueryreqqueueprocess.DocQueryDeferredReqQueueProcessResponseType;
import gov.hhs.fha.nhinc.gateway.entitydocqueryreqqueueprocess.SuccessOrFailType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author richard.ettema
 */
public class EntityDocRetrieveDeferredReqQueueProcessImpl {

    private static Log log = LogFactory.getLog(EntityDocRetrieveDeferredReqQueueProcessImpl.class);

    protected EntityDocRetrieveDeferredReqQueueProcessOrchImpl getEntityDocRetrieveDeferredReqQueueProcessOrchImpl() {
        return new EntityDocRetrieveDeferredReqQueueProcessOrchImpl();
    }

    /**
     * processDocRetrieveDeferredReqQueue Implementation method for processing request queues on reponding gateway
     * @param request
     * @param context
     * @return DocQueryDeferredReqQueueProcessResponseType
     */
    public DocQueryDeferredReqQueueProcessResponseType processDocRetrieveDeferredReqQueue(DocQueryDeferredReqQueueProcessRequestType request, WebServiceContext context) {

        DocQueryDeferredReqQueueProcessResponseType response = new DocQueryDeferredReqQueueProcessResponseType();
        SuccessOrFailType sof = new SuccessOrFailType();
        sof.setSuccess(Boolean.FALSE);
        response.setSuccessOrFail(sof);

        DocRetrieveAcknowledgementType docRetrieveAck = new DocRetrieveAcknowledgementType();
        EntityDocRetrieveDeferredReqQueueProcessOrchImpl entityDocQueryDeferredReqQueueProcessOrchImpl = new EntityDocRetrieveDeferredReqQueueProcessOrchImpl();
        docRetrieveAck = entityDocQueryDeferredReqQueueProcessOrchImpl.processDocRetrieveDeferredReqQueue(request.getMessageId());

        if (docRetrieveAck != null &&
                docRetrieveAck.getMessage() != null &&
                docRetrieveAck.getMessage().getStatus() != null &&
                docRetrieveAck.getMessage().getStatus().equals(NhincConstants.DOC_RETRIEVE_DEFERRED_REQ_ACK_STATUS_MSG)) {
            sof.setSuccess(Boolean.TRUE);
        }

        return response;
    }
}
