/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docquery.adapter.component.deferred.request;

import gov.hhs.fha.nhinc.async.AsyncMessageProcessHelper;
import gov.hhs.fha.nhinc.asyncmsgs.dao.AsyncMsgRecordDao;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.transform.document.DocQueryAckTranforms;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class AdapterComponentDocQueryDeferredRequestOrchImpl {

    private Log log = null;

    public AdapterComponentDocQueryDeferredRequestOrchImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected AsyncMessageProcessHelper createAsyncProcesser() {
        return new AsyncMessageProcessHelper();
    }

    /**
     *
     * @param msg
     * @param assertion
     * @return <code>DocQueryAcknowledgementType</code>
     */
    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdhocQueryRequest msg, AssertionType assertion) {
        DocQueryAcknowledgementType ack = new DocQueryAcknowledgementType();

        RespondingGatewayCrossGatewayQueryRequestType respondingGatewayCrossGatewayQueryRequest = new RespondingGatewayCrossGatewayQueryRequestType();
        respondingGatewayCrossGatewayQueryRequest.setAdhocQueryRequest(msg);
        respondingGatewayCrossGatewayQueryRequest.setAssertion(assertion);

        AsyncMessageProcessHelper asyncProcess = createAsyncProcesser();

        String requestCommunityID = null;
        if (msg != null && msg.getAdhocQuery() != null) {
            requestCommunityID = msg.getAdhocQuery().getHome();
        }
        // Add a new inbound QD request entry to the local deferred queue
        boolean bIsQueueOk = asyncProcess.addQueryForDocumentsRequest(respondingGatewayCrossGatewayQueryRequest, AsyncMsgRecordDao.QUEUE_DIRECTION_INBOUND, requestCommunityID);

        // check for valid queue entry
        if (bIsQueueOk) {
            bIsQueueOk = asyncProcess.processMessageStatus(assertion.getMessageId(), AsyncMsgRecordDao.QUEUE_STATUS_REQRCVD);
        }

        // check for valid queue entry/update
        if (bIsQueueOk) {
            // Set the ack success status of the deferred queue entry
            ack = DocQueryAckTranforms.createAckMessage(NhincConstants.DOC_QUERY_DEFERRED_REQ_ACK_STATUS_MSG, null, null);
            asyncProcess.processAck(assertion.getMessageId(), AsyncMsgRecordDao.QUEUE_STATUS_REQRCVDACK, AsyncMsgRecordDao.QUEUE_STATUS_REQRCVDERR, ack);
        } else {
            String ackMsg = "Deferred Query For Documents processing halted; deferred queue repository error encountered";

            // Set the error acknowledgement status
            // fatal error with deferred queue repository
            ack = DocQueryAckTranforms.createAckMessage(NhincConstants.DOC_QUERY_DEFERRED_RESP_ACK_FAILURE_STATUS_MSG, NhincConstants.DOC_QUERY_DEFERRED_ACK_ERROR_INVALID, ackMsg);
        }

        return ack;
    }

}
