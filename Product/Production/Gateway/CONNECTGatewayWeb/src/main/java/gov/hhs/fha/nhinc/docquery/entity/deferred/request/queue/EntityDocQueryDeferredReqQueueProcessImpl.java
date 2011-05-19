/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.docquery.entity.deferred.request.queue;

import gov.hhs.fha.nhinc.gateway.entitydocqueryreqqueueprocess.DocQueryDeferredReqQueueProcessRequestType;
import gov.hhs.fha.nhinc.gateway.entitydocqueryreqqueueprocess.DocQueryDeferredReqQueueProcessResponseType;
import gov.hhs.fha.nhinc.gateway.entitydocqueryreqqueueprocess.SuccessOrFailType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author richard.ettema
 */
public class EntityDocQueryDeferredReqQueueProcessImpl {

    private static Log log = LogFactory.getLog(EntityDocQueryDeferredReqQueueProcessImpl.class);

    protected EntityDocQueryDeferredReqQueueProcessOrchImpl getEntityDocQueryDeferredReqQueueProcessOrchImpl() {
        return new EntityDocQueryDeferredReqQueueProcessOrchImpl();
    }

    /**
     * processDocQueryAsyncReqQueue Implementation method for processing request queues on reponding gateway
     * @param request
     * @param context
     * @return DocQueryDeferredReqQueueProcessResponseType
     */
    public DocQueryDeferredReqQueueProcessResponseType processDocQueryDeferredReqQueue(DocQueryDeferredReqQueueProcessRequestType request, WebServiceContext context) {

        DocQueryDeferredReqQueueProcessResponseType response = new DocQueryDeferredReqQueueProcessResponseType();
        SuccessOrFailType sof = new SuccessOrFailType();
        sof.setSuccess(Boolean.FALSE);
        response.setSuccessOrFail(sof);

        DocQueryAcknowledgementType docQueryAck = new DocQueryAcknowledgementType();
        EntityDocQueryDeferredReqQueueProcessOrchImpl entityDocQueryDeferredReqQueueProcessOrchImpl = new EntityDocQueryDeferredReqQueueProcessOrchImpl();
        docQueryAck = entityDocQueryDeferredReqQueueProcessOrchImpl.processDocQueryAsyncReqQueue(request.getMessageId());

        if (docQueryAck != null &&
                docQueryAck.getMessage() != null &&
                docQueryAck.getMessage().getStatus() != null &&
                docQueryAck.getMessage().getStatus().equals(NhincConstants.DOC_QUERY_DEFERRED_REQ_ACK_STATUS_MSG)) {
            sof.setSuccess(Boolean.TRUE);
        }

        return response;
    }
}
