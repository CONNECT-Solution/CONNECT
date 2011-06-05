/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.queue;

import gov.hhs.fha.nhinc.async.AsyncMessageProcessHelper;
import gov.hhs.fha.nhinc.asyncmsgs.dao.AsyncMsgRecordDao;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;

/**
 * This class contains the business logic for the AdapterPatientDiscoveryAsyncReqQueue.
 * Currently it is assumed that this will be overridden by the adapter implementation.
 * All this does is send back an ACK.
 *
 * @author westberg
 */
public class AdapterPatientDiscoveryAsyncReqQueueProxyOrchImpl {

    private Log log = null;

    public AdapterPatientDiscoveryAsyncReqQueueProxyOrchImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected AsyncMessageProcessHelper createAsyncProcesser() {
        return new AsyncMessageProcessHelper();
    }

    /**
     * This simulates the patient discovery async request to being added to a queue.
     * @param request The request message.
     * @return The ACK.
     */
    public MCCIIN000002UV01 addPatientDiscoveryAsyncReq(RespondingGatewayPRPAIN201305UV02RequestType request) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        String ackMsg = "Success";

        AsyncMessageProcessHelper asyncProcess = createAsyncProcesser();

        // Add a new inbound PD request entry to the local deferred queue
        boolean bIsQueueOk = asyncProcess.addPatientDiscoveryRequest(request, AsyncMsgRecordDao.QUEUE_DIRECTION_INBOUND);

        // check for valid queue entry
        if (bIsQueueOk) {
            bIsQueueOk = asyncProcess.processMessageStatus(request.getAssertion().getMessageId(), AsyncMsgRecordDao.QUEUE_STATUS_REQRCVD);
        }

        // check for valid queue entry/update
        if (bIsQueueOk) {
            // Set the ack success status of the deferred queue entry
            ack = HL7AckTransforms.createAckFrom201305(request.getPRPAIN201305UV02(), ackMsg);
            asyncProcess.processAck(request.getAssertion().getMessageId(), AsyncMsgRecordDao.QUEUE_STATUS_REQRCVDACK, AsyncMsgRecordDao.QUEUE_STATUS_REQRCVDERR, ack);
        } else {
            ackMsg = "Deferred Patient Discovery processing halted; deferred queue repository error encountered";

            // Set the error acknowledgement status
            // fatal error with deferred queue repository
            ack = HL7AckTransforms.createAckErrorFrom201305(request.getPRPAIN201305UV02(), ackMsg);
            asyncProcess.processAck(request.getAssertion().getMessageId(), AsyncMsgRecordDao.QUEUE_STATUS_REQRCVDERR, AsyncMsgRecordDao.QUEUE_STATUS_REQRCVDERR, ack);
        }

        return ack;
    }

}
