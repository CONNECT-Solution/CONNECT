/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.corex12.docsubmission.genericbatch.request.adapter.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import org.apache.log4j.Logger;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmission;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmissionResponse;

/**
 *
 * @author svalluripalli
 */
public class CORE_X12DSRequestAdapterExceptionBuilder implements AdapterCORE_X12DGenericBatchRequestProxy {

    private static final Logger LOG = Logger.getLogger(CORE_X12DSRequestAdapterExceptionBuilder.class);
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
        return new COREEnvelopeBatchSubmissionResponse();
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
        this.timeStamp = timeStamp;
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
