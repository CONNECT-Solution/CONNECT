/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.adapter.component.deferred.request;

import gov.hhs.fha.nhinc.async.AsyncMessageProcessHelper;
import gov.hhs.fha.nhinc.asyncmsgs.dao.AsyncMsgRecordDao;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.transform.document.DocRetrieveAckTranforms;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *
 * @author Ralph Saunders
 */
public class AdapterComponentDocRetrieveDeferredReqOrchImpl {

    private Log log = null;

    public AdapterComponentDocRetrieveDeferredReqOrchImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    protected AsyncMessageProcessHelper createAsyncProcesser() {
        return new AsyncMessageProcessHelper();
    }

    public DocRetrieveAcknowledgementType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetRequestType body, AssertionType assertion) {
        log.debug("Begin AdapterComponentDocRetrieveDeferredReqOrchImpl.respondingGatewayCrossGatewayRetrieve()");

        DocRetrieveAcknowledgementType ack = new DocRetrieveAcknowledgementType();

        RespondingGatewayCrossGatewayRetrieveRequestType respondingGatewayCrossGatewayRetrieveRequest = new RespondingGatewayCrossGatewayRetrieveRequestType();
        respondingGatewayCrossGatewayRetrieveRequest.setRetrieveDocumentSetRequest(body);
        respondingGatewayCrossGatewayRetrieveRequest.setAssertion(assertion);

        AsyncMessageProcessHelper asyncProcess = createAsyncProcesser();

        String requestCommunityID = HomeCommunityMap.getCommunityIdFromAssertion(assertion);

        RespondingGatewayCrossGatewayRetrieveRequestType requestType = new RespondingGatewayCrossGatewayRetrieveRequestType();
        requestType.setRetrieveDocumentSetRequest(body);
        requestType.setAssertion(assertion);

        // Add a new inbound RD request entry to the local deferred queue
        boolean bIsQueueOk = asyncProcess.addRetrieveDocumentsRequest(requestType, AsyncMsgRecordDao.QUEUE_DIRECTION_INBOUND, requestCommunityID);

        // check for valid queue entry
        if (bIsQueueOk) {
            bIsQueueOk = asyncProcess.processMessageStatus(assertion.getMessageId(), AsyncMsgRecordDao.QUEUE_STATUS_REQRCVD);
        }

        // check for valid queue entry/update
        if (bIsQueueOk) {
            // Set the ack success status of the deferred queue entry
            ack = DocRetrieveAckTranforms.createAckMessage(NhincConstants.DOC_RETRIEVE_DEFERRED_REQ_ACK_STATUS_MSG, null, null);
            asyncProcess.processAck(assertion.getMessageId(), AsyncMsgRecordDao.QUEUE_STATUS_REQRCVDACK, AsyncMsgRecordDao.QUEUE_STATUS_REQRCVDERR, ack);
        } else {
            String ackMsg = "Deferred Retrieve Documents processing halted; deferred queue repository error encountered";

            // Set the error acknowledgement status
            // fatal error with deferred queue repository
            ack = DocRetrieveAckTranforms.createAckMessage(NhincConstants.DOC_RETRIEVE_DEFERRED_RESP_ACK_FAILURE_STATUS_MSG, NhincConstants.DOC_RETRIEVE_DEFERRED_ACK_ERROR_INVALID, ackMsg);
        }

        log.debug("End AdapterComponentDocRetrieveDeferredReqOrchImpl.respondingGatewayCrossGatewayRetrieve()");

        return ack;
    }
}
