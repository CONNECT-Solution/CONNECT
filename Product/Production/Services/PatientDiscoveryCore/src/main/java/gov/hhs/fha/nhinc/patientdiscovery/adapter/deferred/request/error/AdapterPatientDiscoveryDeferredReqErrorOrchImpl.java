/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.error;

import gov.hhs.fha.nhinc.async.AsyncMessageProcessHelper;
import gov.hhs.fha.nhinc.asyncmsgs.dao.AsyncMsgRecordDao;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;

import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;

/**
 * 
 * @author jhoppesc
 */
public class AdapterPatientDiscoveryDeferredReqErrorOrchImpl {

    protected AsyncMessageProcessHelper createAsyncProcesser() {
        return new AsyncMessageProcessHelper();
    }

    public MCCIIN000002UV01 processPatientDiscoveryAsyncReqError(PRPAIN201305UV02 request, PRPAIN201306UV02 response,
            AssertionType assertion, String errMsg) {
        MCCIIN000002UV01 ack = HL7AckTransforms.createAckErrorFrom201305(request, errMsg);

        AsyncMessageProcessHelper asyncProcess = createAsyncProcesser();

        // Add a new inbound PD request entry to the local deferred queue
        boolean bIsQueueOk = asyncProcess.addPatientDiscoveryRequest(request, assertion,
                AsyncMsgRecordDao.QUEUE_DIRECTION_INBOUND);

        // check for valid queue entry
        if (bIsQueueOk) {
            bIsQueueOk = asyncProcess.processAck(assertion.getMessageId(), AsyncMsgRecordDao.QUEUE_STATUS_REQRCVDERR,
                    AsyncMsgRecordDao.QUEUE_STATUS_REQRCVDERR, ack);
        } else {
            String ackMsg = "Deferred Patient Discovery processing halted; deferred queue repository error encountered; "
                    + errMsg;

            // Set the error acknowledgement status
            // fatal error with deferred queue repository
            ack = HL7AckTransforms.createAckErrorFrom201305(request, ackMsg);
        }

        return ack;
    }

}
