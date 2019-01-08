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
package gov.hhs.fha.nhinc.corex12.ds.utils;

import java.sql.Timestamp;
import java.util.Date;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmission;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmissionResponse;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeRequest;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeResponse;

/**
 * TODO: This class should not be using java.sql.Timestamp
 *
 * @author cmay, svalluripalli
 */
public abstract class AbstractX12ExceptionBuilder {

    /**
     *
     * @param request
     * @return COREEnvelopeBatchSubmissionResponse error response
     */
    public COREEnvelopeBatchSubmissionResponse buildCOREEnvelopeGenericBatchErrorResponse(
        COREEnvelopeBatchSubmission request) {

        COREEnvelopeBatchSubmissionResponse response = new COREEnvelopeBatchSubmissionResponse();

        response.setCORERuleVersion(request.getCORERuleVersion());
        response.setCheckSum(request.getCheckSum());
        response.setPayload(request.getPayload());
        response.setPayloadLength(request.getPayloadLength());
        response.setProcessingMode(request.getProcessingMode());
        response.setReceiverID(request.getReceiverID());
        response.setSenderID(request.getSenderID());

        response.setTimeStamp(new Timestamp(new Date().getTime()).toString());
        response.setErrorCode(getErrorCode());
        response.setErrorMessage(getErrorMessage());
        response.setPayloadType(getPayloadType());

        return response;
    }

    /**
     *
     * @param request
     * @return
     */
    public COREEnvelopeRealTimeResponse buildCOREEnvelopeRealTimeErrorResponse(COREEnvelopeRealTimeRequest request) {
        COREEnvelopeRealTimeResponse response = new COREEnvelopeRealTimeResponse();

        response.setCORERuleVersion(request.getCORERuleVersion());
        response.setPayload(request.getPayload());
        response.setProcessingMode(request.getProcessingMode());
        response.setReceiverID(request.getReceiverID());
        response.setSenderID(request.getSenderID());
        response.setTimeStamp(new Timestamp(new Date().getTime()).toString());
        response.setPayloadID(request.getPayloadID());

        response.setErrorCode(getErrorCode());
        response.setErrorMessage(getErrorMessage());
        response.setPayloadType(getPayloadType());

        return response;
    }

    /**
     *
     * @param msg RealTimeRequest from Nhin Proxy
     * @param targetHCID targetHCID extracted from RealTimeRequest
     * @return COREEnvelopeRealTimeResponse Error Response created from initiating Gateway if community is not found.
     */
    public COREEnvelopeRealTimeResponse createErrorResponse(COREEnvelopeRealTimeRequest msg, String targetHCID) {
        COREEnvelopeRealTimeResponse response = new COREEnvelopeRealTimeResponse();

        response.setPayloadType("CoreEnvelopeError");
        response.setProcessingMode("RealTime");
        response.setPayloadID(msg.getPayloadID());
        response.setReceiverID(msg.getReceiverID());
        response.setErrorMessage("No endpoint available for target community HCID: " + targetHCID);

        response.setTimeStamp(new Timestamp(new Date().getTime()).toString());
        response.setErrorCode(getErrorCode());

        return response;
    }

    /**
     *
     * @param msg RealTimeRequest from Nhin Proxy
     * @param message ExceptionMessage from stacktrace
     * @return COREEnvelopeRealTimeResponse Error Response created if WebServiceException caused while the initiating
     * Gateway send the request to Responding Gateway.
     */
    public COREEnvelopeRealTimeResponse createWebServiceErrorResponse(COREEnvelopeRealTimeRequest msg,
        String message) {

        COREEnvelopeRealTimeResponse response = new COREEnvelopeRealTimeResponse();

        response.setPayloadType("CoreEnvelopeError");
        response.setProcessingMode("RealTime");
        response.setPayloadID(msg.getPayloadID());
        response.setCORERuleVersion("2.2.0");
        response.setSenderID(msg.getSenderID());
        response.setReceiverID(msg.getReceiverID());

        response.setErrorCode(getErrorCode());
        response.setErrorMessage(message);
        response.setTimeStamp(new Timestamp(new Date().getTime()).toString());

        return response;
    }

    /**
     *
     * @param msg COREEnvelopeBatchSubmission from Nhin Proxy
     * @param targetHCID targetHCID extracted from RealTimeRequest
     * @return COREEnvelopeBatchSubmissionResponse Error Response created from initiating Gateway if community is not
     * found.
     */
    public COREEnvelopeBatchSubmissionResponse createErrorResponse(COREEnvelopeBatchSubmission msg,
        String targetHCID) {

        COREEnvelopeBatchSubmissionResponse response = new COREEnvelopeBatchSubmissionResponse();

        response.setCORERuleVersion("2.2.0");
        response.setPayloadType("CoreEnvelopeError");
        response.setProcessingMode("Batch");
        response.setSenderID(msg.getSenderID());
        response.setReceiverID(msg.getReceiverID());
        response.setTimeStamp(new Timestamp(new Date().getTime()).toString());
        response.setPayloadID(msg.getPayloadID());
        response.setPayload(msg.getPayload());

        response.setErrorCode(getErrorCode());
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
    public COREEnvelopeBatchSubmissionResponse createWebServiceErrorResponse(COREEnvelopeBatchSubmission msg,
        String message) {

        COREEnvelopeBatchSubmissionResponse response = new COREEnvelopeBatchSubmissionResponse();

        response.setCORERuleVersion("2.2.0");
        response.setPayloadType("CoreEnvelopeError");
        response.setProcessingMode("Batch");
        response.setSenderID(msg.getSenderID());
        response.setReceiverID(msg.getReceiverID());
        response.setPayloadID(msg.getPayloadID());
        response.setPayload(msg.getPayload());

        response.setErrorCode(getErrorCode());
        response.setErrorMessage(message);
        response.setTimeStamp(new Timestamp(new Date().getTime()).toString());

        return response;
    }

    protected abstract String getErrorCode();

    protected abstract String getErrorMessage();

    protected abstract String getPayloadType();
}
