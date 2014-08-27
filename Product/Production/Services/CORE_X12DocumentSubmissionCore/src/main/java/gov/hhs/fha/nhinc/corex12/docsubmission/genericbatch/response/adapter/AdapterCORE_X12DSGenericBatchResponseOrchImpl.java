/*
 * Copyright (c) 2014, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.corex12.docsubmission.genericbatch.response.adapter;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import org.apache.log4j.Logger;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmission;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmissionResponse;

/**
 * @author svalluripalli
 *
 */
public class AdapterCORE_X12DSGenericBatchResponseOrchImpl {

    private static final Logger LOG = Logger.getLogger(AdapterCORE_X12DSGenericBatchResponseOrchImpl.class);

    public COREEnvelopeBatchSubmissionResponse batchSubmitTransaction(COREEnvelopeBatchSubmission msg, AssertionType assertion) {
        LOG.trace("Begin AdapterCORE_X12DSGenericBatchResponseOrchImpl.batchSubmitTransaction()");
        COREEnvelopeBatchSubmissionResponse oCOREEnvelopeBatchSubmissionResponse = null;
        if (null != msg) {
            oCOREEnvelopeBatchSubmissionResponse = new COREEnvelopeBatchSubmissionResponse();
            buildAdapterCORE_X12DSSGenericBatchRequestMetaData(oCOREEnvelopeBatchSubmissionResponse);
            logCOREEnvelopeBatchSubmissionRequestMessage(msg);
        }
        LOG.trace("End AdapterCORE_X12DSGenericBatchResponseOrchImpl.batchSubmitTransaction()");
        return oCOREEnvelopeBatchSubmissionResponse;
    }

    /**
     *
     * @param msg
     */
    private void logCOREEnvelopeBatchSubmissionRequestMessage(COREEnvelopeBatchSubmission msg) {
        LOG.info("CORERuleVersion       :" + msg.getCORERuleVersion());
        LOG.info("CheckSum              :" + msg.getCheckSum());
        LOG.info("PayLoad ID            :" + msg.getPayloadID());
        LOG.info("PayLoad Type          :" + msg.getPayloadType());
        LOG.info("Processing Mode       :" + msg.getProcessingMode());
        LOG.info("Receiver ID           :" + msg.getReceiverID());
        LOG.info("Sender ID             :" + msg.getSenderID());
        LOG.info("Time Stamp            :" + msg.getTimeStamp());
        LOG.info("Payload Length        :" + msg.getPayloadLength());
        LOG.info("Payload               :" + msg.getPayload());
    }

    /**
     *
     * @param oCOREEnvelopeBatchSubmissionResponse
     */
    private void buildAdapterCORE_X12DSSGenericBatchRequestMetaData(COREEnvelopeBatchSubmissionResponse oCOREEnvelopeBatchSubmissionResponse) {
        oCOREEnvelopeBatchSubmissionResponse.setCORERuleVersion("2.2.0");
        String payLoad = "<xop:Include href=\"cid:1.urn:uuid:5117AAE1116EA8B87A1200060184692@apache.org\" xmlns:xop=\"http://www.w3.org/2004/08/xop/include\"/>";
        oCOREEnvelopeBatchSubmissionResponse.setPayload(payLoad.getBytes());
        oCOREEnvelopeBatchSubmissionResponse.setPayloadLength(1551254);
        oCOREEnvelopeBatchSubmissionResponse.setPayloadID("f81d4fae-7dec-11d0-a765-00a0c91e6bf6");
        oCOREEnvelopeBatchSubmissionResponse.setPayloadType("X12_999_Response_005010x231A1");
        oCOREEnvelopeBatchSubmissionResponse.setProcessingMode("Batch");
        oCOREEnvelopeBatchSubmissionResponse.setReceiverID("HospitalA");
        oCOREEnvelopeBatchSubmissionResponse.setSenderID("PayerB");
        oCOREEnvelopeBatchSubmissionResponse.setTimeStamp("2007-08-30T10:20:34Z");
        oCOREEnvelopeBatchSubmissionResponse.setErrorCode("Success");
        oCOREEnvelopeBatchSubmissionResponse.setErrorMessage("NONE");
        oCOREEnvelopeBatchSubmissionResponse.setCheckSum("43B8485AB5");
    }
}
