package gov.hhs.fha.nhinc.event;

import java.util.List;

public class BaseEventDescription implements EventDescription {

    private List<String> responseMsgids;
    private String action;
    private String errorCode;
    private String status;
    private String npi;
    private String respondingHCID;
    private String initiatingHCID;
    private String payloadSize;
    private String payLoadType;
    private String transactionId;
    private String serviceType;
    private String messageId;
    private String timeStamp;
    
    
    public BaseEventDescription() {
    }

    @Override
    public String getMessageId() {
        return messageId;
    }

    @Override
    public String getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String getServiceType() {
        return serviceType;
    }

    @Override
    public String getTransactionId() {
        return transactionId;
    }

    @Override
    public String getPayloadType() {
        return payLoadType;
    }

    @Override
    public String getPayloadSize() {
        return payloadSize;
    }

    @Override
    public String getInitiatingHCID() {
        return initiatingHCID;
    }

    @Override
    public String getRespondingHCID() {
        return respondingHCID;
    }

    @Override
    public String getNPI() {
        return npi;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getAction() {
        return action;
    }

    @Override
    public List<String> getResponseMsgIdList() {
        return responseMsgids;
    }

    public void setResponseMsgids(List<String> responseMsgids) {
        this.responseMsgids = responseMsgids;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setNpi(String npi) {
        this.npi = npi;
    }

    public void setRespondingHCID(String respondingHCID) {
        this.respondingHCID = respondingHCID;
    }

    public void setInitiatingHCID(String initiatingHCID) {
        this.initiatingHCID = initiatingHCID;
    }

    public void setPayloadSize(String payloadSize) {
        this.payloadSize = payloadSize;
    }

    public void setPayLoadType(String payLoadType) {
        this.payLoadType = payLoadType;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    
    
}
