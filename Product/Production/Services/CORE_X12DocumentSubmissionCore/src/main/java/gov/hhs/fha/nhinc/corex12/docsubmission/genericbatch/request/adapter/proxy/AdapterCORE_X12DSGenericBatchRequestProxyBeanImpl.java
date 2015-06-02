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

package gov.hhs.fha.nhinc.corex12.docsubmission.genericbatch.request.adapter.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.largefile.LargeFileUtils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.log4j.Logger;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmission;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmissionResponse;

/**
 *
 * @author svalluripalli
 */
public class AdapterCORE_X12DSGenericBatchRequestProxyBeanImpl implements AdapterCORE_X12DGenericBatchRequestProxy {

    private static final Logger LOG = Logger.getLogger(AdapterCORE_X12DSGenericBatchRequestProxyBeanImpl.class);
    private String payload;
    private String payloadId;
    private String payloadType;
    private String coreRulesVersion;
    private String processingMode;
    private String payloadLength;
    private String timeStamp;
    private String senderId;
    private String receiverId;
    private String checkSum;
    private String errorCode;
    private String errorMessage;

    /**
     *
     * @param msg
     * @param assertion
     * @return COREEnvelopeBatchSubmissionResponse
     */
    @Override
    public COREEnvelopeBatchSubmissionResponse batchSubmitTransaction(COREEnvelopeBatchSubmission msg, AssertionType assertion) {
        LOG.debug("Using Bean Implementation for Adapter X12 Doc Submission request");
        COREEnvelopeBatchSubmissionResponse oResponse = new COREEnvelopeBatchSubmissionResponse();
        oResponse.setCORERuleVersion(getCoreRulesVersion());
        oResponse.setCheckSum(getCheckSum());
        oResponse.setErrorCode(getErrorCode());
        oResponse.setErrorMessage(getErrorMessage());
        oResponse.setPayload(LargeFileUtils.getInstance().convertToDataHandler(getPayload()));
        oResponse.setPayloadID(getPayloadId());
        oResponse.setPayloadLength(new Integer(Integer.parseInt(getPayloadLength())));
        oResponse.setPayloadType(getPayloadType());
        oResponse.setProcessingMode(getProcessingMode());
        oResponse.setReceiverID(getReceiverId());
        oResponse.setSenderID(getSenderId());
        oResponse.setTimeStamp(getTimeStamp());
        return oResponse;
    }

    /**
     *
     * @return String
     */
    public String getPayload() {
        return payload;
    }

    /**
     *
     * @param payload
     */
    public void setPayload(String payload) {
        this.payload = payload;
    }

    /**
     *
     * @return String
     */
    public String getPayloadId() {
        return payloadId;
    }

    /**
     *
     * @param payloadId
     */
    public void setPayloadId(String payloadId) {
        this.payloadId = payloadId;
    }

    /**
     *
     * @return String
     */
    public String getPayloadType() {
        return payloadType;
    }

    /**
     *
     * @param payloadType
     */
    public void setPayloadType(String payloadType) {
        this.payloadType = payloadType;
    }

    /**
     *
     * @return String
     */
    public String getCoreRulesVersion() {
        return coreRulesVersion;
    }

    /**
     *
     * @param coreRulesVersion
     */
    public void setCoreRulesVersion(String coreRulesVersion) {
        this.coreRulesVersion = coreRulesVersion;
    }

    /**
     *
     * @return String
     */
    public String getProcessingMode() {
        return processingMode;
    }

    /**
     *
     * @param processingMode
     */
    public void setProcessingMode(String processingMode) {
        this.processingMode = processingMode;
    }

    /**
     *
     * @return String
     */
    public String getPayloadLength() {
        return payloadLength;
    }

    /**
     *
     * @param payloadLength
     */
    public void setPayloadLength(String payloadLength) {
        this.payloadLength = payloadLength;
    }

    /**
     *
     * @return String
     */
    public String getTimeStamp() {
        return timeStamp;
    }

    /**
     *
     * @param timeStamp
     */
    public void setTimeStamp(String timeStamp) {
        if (null != timeStamp && timeStamp.length() > 0) {
            this.timeStamp = timeStamp;
        } else {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            this.timeStamp = dateFormat.format(cal.getTime());
        }
    }

    /**
     *
     * @return String
     */
    public String getSenderId() {
        return senderId;
    }

    /**
     *
     * @param senderId
     */
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    /**
     *
     * @return String
     */
    public String getReceiverId() {
        return receiverId;
    }

    /**
     *
     * @param receiverId
     */
    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    /**
     *
     * @return String
     */
    public String getCheckSum() {
        return checkSum;
    }

    /**
     *
     * @param checkSum
     */
    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    /**
     *
     * @return String
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     *
     * @param errorCode
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     *
     * @return String
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     *
     * @param errorMessage
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
