/**
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permittntefed provided that the following conditions are met:
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
