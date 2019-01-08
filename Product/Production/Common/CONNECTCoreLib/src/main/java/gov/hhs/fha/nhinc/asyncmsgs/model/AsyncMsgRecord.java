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
package gov.hhs.fha.nhinc.asyncmsgs.model;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

/**
 *
 * @author JHOPPESC, richard.ettema
 */
public class AsyncMsgRecord implements Serializable {

    private static final long serialVersionUID = 2515212314474911391L;
    private Long Id = null;
    private String messageId = null;
    private Date creationTime = null;
    private Date responseTime = null;
    private Long duration = null;
    private String serviceName = null;
    private String direction = null;
    private String communityId = null;
    private String status = null;
    private String responseType = null;
    private String reserved = null;
    private Blob msgData = null;
    private Blob rspData = null;
    private Blob ackData = null;

    public AsyncMsgRecord() {
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String MessageId) {
        messageId = MessageId;
    }

    public Date getCreationTime() {
        if (creationTime == null) {
            return null;
        } else {
            return (Date) creationTime.clone();
        }
    }

    public void setCreationTime(Date creationTime) {
        if (creationTime != null) {
            this.creationTime = (Date) creationTime.clone();
        } else {
            this.creationTime = null;
        }
    }

    public Date getResponseTime() {
        if (responseTime == null) {
            return null;
        } else {
            return (Date) responseTime.clone();
        }
    }

    public void setResponseTime(Date responseTime) {
        if (responseTime != null) {
            this.responseTime = (Date) responseTime.clone();
        } else {
            this.responseTime = null;
        }
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long Duration) {
        duration = Duration;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String ServiceName) {
        serviceName = ServiceName;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String Direction) {
        direction = Direction;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String CommunityId) {
        communityId = CommunityId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String Status) {
        status = Status;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String ResponseType) {
        responseType = ResponseType;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String Reserved) {
        reserved = Reserved;
    }

    public Blob getMsgData() {
        return msgData;
    }

    public void setMsgData(Blob MsgData) {
        msgData = MsgData;
    }

    public Blob getRspData() {
        return rspData;
    }

    public void setRspData(Blob RspData) {
        rspData = RspData;
    }

    public Blob getAckData() {
        return ackData;
    }

    public void setAckData(Blob AckData) {
        ackData = AckData;
    }
}
