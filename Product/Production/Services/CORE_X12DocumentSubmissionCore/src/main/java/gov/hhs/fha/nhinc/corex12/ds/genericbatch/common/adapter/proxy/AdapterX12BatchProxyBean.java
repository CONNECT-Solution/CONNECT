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
package gov.hhs.fha.nhinc.corex12.ds.genericbatch.common.adapter.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.largefile.LargeFileUtils;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.commons.lang.StringUtils;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmission;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmissionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author cmay
 */
public abstract class AdapterX12BatchProxyBean extends AbstractAdapterX12BatchProxy {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterX12BatchProxyBean.class);

    // TODO: This should be kept in a common location for use across all services
    private static final String TIMESTAMP_FORMAT = "yyyy/MM/dd HH:mm:ss";

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

    @Override
    public final COREEnvelopeBatchSubmissionResponse batchSubmitTransaction(COREEnvelopeBatchSubmission msg,
        AssertionType assertion) {

        LOG.debug("Using {} Implementation for Adapter X12 Doc Submission request", getServiceName());
        COREEnvelopeBatchSubmissionResponse response = new COREEnvelopeBatchSubmissionResponse();
        response.setCORERuleVersion(getCoreRulesVersion());
        response.setCheckSum(getCheckSum());
        response.setErrorCode(getErrorCode());
        response.setErrorMessage(getErrorMessage());
        response.setPayload(LargeFileUtils.getInstance().convertToDataHandler(getPayload()));
        response.setPayloadID(getPayloadId());
        response.setPayloadLength(Integer.parseInt(getPayloadLength()));
        response.setPayloadType(getPayloadType());
        response.setProcessingMode(getProcessingMode());
        response.setReceiverID(getReceiverId());
        response.setSenderID(getSenderId());
        response.setTimeStamp(getTimeStamp());
        return response;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getPayloadId() {
        return payloadId;
    }

    public void setPayloadId(String payloadId) {
        this.payloadId = payloadId;
    }

    public String getPayloadType() {
        return payloadType;
    }

    public void setPayloadType(String payloadType) {
        this.payloadType = payloadType;
    }

    public String getCoreRulesVersion() {
        return coreRulesVersion;
    }

    public void setCoreRulesVersion(String coreRulesVersion) {
        this.coreRulesVersion = coreRulesVersion;
    }

    public String getProcessingMode() {
        return processingMode;
    }

    public void setProcessingMode(String processingMode) {
        this.processingMode = processingMode;
    }

    public String getPayloadLength() {
        return payloadLength;
    }

    public void setPayloadLength(String payloadLength) {
        this.payloadLength = payloadLength;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        if (StringUtils.isNotEmpty(timeStamp)) {
            this.timeStamp = timeStamp;
        } else {
            this.timeStamp = new SimpleDateFormat(TIMESTAMP_FORMAT).format(Calendar.getInstance().getTime());
        }
    }
}
