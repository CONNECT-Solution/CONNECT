/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.corex12.docsubmission.utils;

import gov.hhs.fha.nhinc.messaging.server.BaseService;
import java.sql.Timestamp;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmission;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmissionResponse;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeRequest;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeResponse;

/**
 *
 * @author svalluripalli
 */
public class CORE_X12DSEntityExceptionBuilder extends BaseService {

    private static final Logger LOG = LoggerFactory.getLogger(CORE_X12DSEntityExceptionBuilder.class);
    private static final String ERROR_CODE = "Sender";
    private static final String ERROR_MESSAGE = "NwHIN target communities not specified in entity request";
    private static final String PAYLOAD_TYPE = "CoreEnvelopeError";

    /**
     * Singleton Constructor
     */
    public CORE_X12DSEntityExceptionBuilder() {
    }

    /**
     *
     * @param oCOREEnvelopeBatchSubmission
     * @param oBatchSubmissionResponse
     */
    public void buildCOREEnvelopeGenericBatchErrorResponse(COREEnvelopeBatchSubmission oCOREEnvelopeBatchSubmission, COREEnvelopeBatchSubmissionResponse oBatchSubmissionResponse) {
        Date currentDate = new Date();
        oBatchSubmissionResponse.setCORERuleVersion(oCOREEnvelopeBatchSubmission.getCORERuleVersion());
        oBatchSubmissionResponse.setCheckSum(oCOREEnvelopeBatchSubmission.getCheckSum());
        oBatchSubmissionResponse.setErrorCode(ERROR_CODE);
        oBatchSubmissionResponse.setErrorMessage(ERROR_MESSAGE);
        oBatchSubmissionResponse.setPayload(oCOREEnvelopeBatchSubmission.getPayload());
        oBatchSubmissionResponse.setPayloadLength(oCOREEnvelopeBatchSubmission.getPayloadLength());
        oBatchSubmissionResponse.setPayloadType(PAYLOAD_TYPE);
        oBatchSubmissionResponse.setProcessingMode(oCOREEnvelopeBatchSubmission.getProcessingMode());
        oBatchSubmissionResponse.setReceiverID(oCOREEnvelopeBatchSubmission.getReceiverID());
        oBatchSubmissionResponse.setSenderID(oCOREEnvelopeBatchSubmission.getSenderID());
        oBatchSubmissionResponse.setTimeStamp(new Timestamp(currentDate.getTime()).toString());
    }

    /**
     *
     * @param oCOREEnvelopeRealTimeRequest
     * @param realTimeResponse
     */
    public void buildCOREEnvelopeRealTimeErrorResponse(COREEnvelopeRealTimeRequest oCOREEnvelopeRealTimeRequest, COREEnvelopeRealTimeResponse realTimeResponse) {
        realTimeResponse.setCORERuleVersion(oCOREEnvelopeRealTimeRequest.getCORERuleVersion());
        realTimeResponse.setErrorCode(ERROR_CODE);
        realTimeResponse.setErrorMessage(ERROR_MESSAGE);
        realTimeResponse.setPayload(oCOREEnvelopeRealTimeRequest.getPayload());
        realTimeResponse.setPayloadType(PAYLOAD_TYPE);
        realTimeResponse.setProcessingMode(oCOREEnvelopeRealTimeRequest.getProcessingMode());
        realTimeResponse.setReceiverID(oCOREEnvelopeRealTimeRequest.getReceiverID());
        realTimeResponse.setSenderID(oCOREEnvelopeRealTimeRequest.getSenderID());
        realTimeResponse.setTimeStamp(getTimeStamp().toString());
        realTimeResponse.setPayloadID(oCOREEnvelopeRealTimeRequest.getPayloadID());
    }

    /**
     *
     * @param msg RealTimeRequest from Nhin Proxy
     * @param targetHCID targetHCID extracted from RealTimeRequest
     * @return COREEnvelopeRealTimeResponse Error Response created from initiating Gateway if community is not found.
     */
    public static COREEnvelopeRealTimeResponse createErrorResponse(COREEnvelopeRealTimeRequest msg, String targetHCID) {
        COREEnvelopeRealTimeResponse response = new COREEnvelopeRealTimeResponse();
        response.setPayloadType("CoreEnvelopeError");
        response.setProcessingMode("RealTime");
        response.setPayloadID(msg.getPayloadID());
        response.setReceiverID(msg.getReceiverID());
        response.setErrorCode(ERROR_CODE);
        response.setErrorMessage("No endpoint available for target community HCID: " + targetHCID);
        response.setTimeStamp(getTimeStamp().toString());
        return response;
    }

    /**
     *
     * @param msg RealTimeRequest from Nhin Proxy
     * @param message ExceptionMessage from stacktrace
     * @return COREEnvelopeRealTimeResponse Error Response created if WebServiceException caused while the initiating
     * Gateway send the request to Responding Gateway.
     */
    public static COREEnvelopeRealTimeResponse createWebServiceErrorResponse(COREEnvelopeRealTimeRequest msg, String message) {
        COREEnvelopeRealTimeResponse response = new COREEnvelopeRealTimeResponse();
        response.setPayloadType("CoreEnvelopeError");
        response.setProcessingMode("RealTime");
        response.setPayloadID(msg.getPayloadID());
        response.setCORERuleVersion("2.2.0");
        response.setSenderID(msg.getSenderID());
        response.setReceiverID(msg.getReceiverID());
        response.setErrorCode(ERROR_CODE);
        response.setErrorMessage(message);
        response.setTimeStamp(getTimeStamp().toString());
        return response;
    }

    /**
     *
     * @param msg COREEnvelopeBatchSubmission from Nhin Proxy
     * @param targetHCID targetHCID extracted from RealTimeRequest
     * @return COREEnvelopeBatchSubmissionResponse Error Response created from initiating Gateway if community is not
     * found.
     */
    public static COREEnvelopeBatchSubmissionResponse createErrorResponse(COREEnvelopeBatchSubmission msg, String targetHCID) {
        COREEnvelopeBatchSubmissionResponse response = new COREEnvelopeBatchSubmissionResponse();
        response.setCORERuleVersion("2.2.0");
        response.setPayloadType("CoreEnvelopeError");
        response.setProcessingMode("Batch");
        response.setSenderID(msg.getSenderID());
        response.setReceiverID(msg.getReceiverID());
        response.setTimeStamp(getTimeStamp().toString());
        response.setPayloadID(msg.getPayloadID());
        response.setPayload(msg.getPayload());
        response.setErrorCode(ERROR_CODE);
        response.setErrorMessage("No endpoint available for target community HCID: " + targetHCID);
        return response;
    }

    /**
     *
     * @param msg COREEnvelopeBatchSubmission from Nhin Proxy
     * @param message ExceptionMessage from stacktrace
     * @return COREEnvelopeBatchSubmissionResponse Error Response created if WebServiceException caused while the
     * initiating Gateway send the request to Responding Gateway.
     */
    public static COREEnvelopeBatchSubmissionResponse createWebServiceErrorResponse(COREEnvelopeBatchSubmission msg, String message) {
        COREEnvelopeBatchSubmissionResponse response = new COREEnvelopeBatchSubmissionResponse();
        response.setCORERuleVersion("2.2.0");
        response.setPayloadType("CoreEnvelopeError");
        response.setProcessingMode("Batch");
        response.setSenderID(msg.getSenderID());
        response.setReceiverID(msg.getReceiverID());
        response.setTimeStamp(getTimeStamp().toString());
        response.setPayloadID(msg.getPayloadID());
        response.setPayload(msg.getPayload());
        response.setErrorCode(ERROR_CODE);
        response.setErrorMessage(message);
        return response;
    }

    private static Timestamp getTimeStamp() {
        Date currentDate = new Date();
        return new Timestamp(currentDate.getTime());
    }
}
