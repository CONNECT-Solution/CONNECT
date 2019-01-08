/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.corex12.ds.genericbatch.common.adapter;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.largefile.LargeFileUtils;
import java.sql.Timestamp;
import java.util.Date;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmission;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmissionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author cmay, svalluripalli
 */
public final class AdapterX12BatchOrchImpl {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterX12BatchOrchImpl.class);
    private static final String X12_BATCH_PAYLOAD
    = "<xop:Include href=\"cid:1.urn:uuid:5117AAE1116EA8B87A1200060184692@apache.org\"\n"
        + "xmlns:xop=\"http://www.w3.org/2004/08/xop/include\"/>";

    public COREEnvelopeBatchSubmissionResponse batchSubmitTransaction(COREEnvelopeBatchSubmission message,
        AssertionType assertion) {

        COREEnvelopeBatchSubmissionResponse response;

        if (message != null) {
            response = buildAdapterCORE_X12DSGenericBatchResponseMetadata();
            logAdapterCORE_X12DSGenericBatchRequest(message);
        } else {
            // TODO: Need to add error handling
            response = new COREEnvelopeBatchSubmissionResponse();
        }

        return response;
    }

    private static COREEnvelopeBatchSubmissionResponse buildAdapterCORE_X12DSGenericBatchResponseMetadata() {
        COREEnvelopeBatchSubmissionResponse response = new COREEnvelopeBatchSubmissionResponse();
        response.setPayloadType("X12_BatchReceiptConfirmation");
        response.setProcessingMode("Batch");
        response.setPayloadID("f81d4fae-7dec-11d0-a765-00a0c91e6bf6");
        response.setPayloadLength(1551254);
        response.setTimeStamp(new Timestamp(new Date().getTime()).toString());
        response.setSenderID("PayerB");
        response.setReceiverID("HospitalA");
        response.setCORERuleVersion("2.2.0");
        response.setPayload(LargeFileUtils.getInstance().convertToDataHandler(X12_BATCH_PAYLOAD.getBytes()));
        response.setErrorCode("Success");
        response.setErrorMessage("None");
        return response;
    }

    private static void logAdapterCORE_X12DSGenericBatchRequest(COREEnvelopeBatchSubmission request) {
        LOG.info("Generich Batch Payload Type    = {}", request.getPayloadType());
        LOG.info("Generich Batch Processing Mode = {}", request.getProcessingMode());
        LOG.info("Generich Batch Payload Id      = {}", request.getPayloadID());
        LOG.info("Generich Batch TimeStamp       = {}", request.getTimeStamp());
        LOG.info("Generich Batch Sender Id       = {}", request.getSenderID());
        LOG.info("Generich Batch Receiver Id     = {}", request.getReceiverID());
        LOG.info("Generich Batch Rule version    = {}", request.getCORERuleVersion());
        LOG.info("Generich Batch Payload         = {}", request.getPayload());
    }
}
