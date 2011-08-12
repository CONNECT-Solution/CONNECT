/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request;

import gov.hhs.fha.nhinc.async.AsyncMessageProcessHelper;
import gov.hhs.fha.nhinc.asyncmsgs.dao.AsyncMsgRecordDao;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;

/**
 *
 * @author jhoppesc
 */
public class AdapterPatientDiscoveryDeferredReqOrchImpl {
    private Log log = null;

    public AdapterPatientDiscoveryDeferredReqOrchImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected AsyncMessageProcessHelper createAsyncProcesser() {
        return new AsyncMessageProcessHelper();
    }

    public MCCIIN000002UV01 processPatientDiscoveryAsyncReq(PRPAIN201305UV02 request, AssertionType assertion) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        String ackMsg = "Success";

        AsyncMessageProcessHelper asyncProcess = createAsyncProcesser();

        // Add a new inbound PD request entry to the local deferred queue
        boolean bIsQueueOk = asyncProcess.addPatientDiscoveryRequest(request, assertion, AsyncMsgRecordDao.QUEUE_DIRECTION_INBOUND);

        // check for valid queue entry
        if (bIsQueueOk) {
            bIsQueueOk = asyncProcess.processMessageStatus(assertion.getMessageId(), AsyncMsgRecordDao.QUEUE_STATUS_REQRCVD);
        }

        // check for valid queue entry/update
        if (bIsQueueOk) {
            // Set the ack success status of the deferred queue entry
            ack = HL7AckTransforms.createAckFrom201305(request, ackMsg);
            asyncProcess.processAck(assertion.getMessageId(), AsyncMsgRecordDao.QUEUE_STATUS_REQRCVDACK, AsyncMsgRecordDao.QUEUE_STATUS_REQRCVDERR, ack);
        } else {
            ackMsg = "Deferred Patient Discovery processing halted; deferred queue repository error encountered";

            // Set the error acknowledgement status
            // fatal error with deferred queue repository
            ack = HL7AckTransforms.createAckErrorFrom201305(request, ackMsg);
            asyncProcess.processAck(assertion.getMessageId(), AsyncMsgRecordDao.QUEUE_STATUS_REQRCVDERR, AsyncMsgRecordDao.QUEUE_STATUS_REQRCVDERR, ack);
        }

        return ack;
    }


}
