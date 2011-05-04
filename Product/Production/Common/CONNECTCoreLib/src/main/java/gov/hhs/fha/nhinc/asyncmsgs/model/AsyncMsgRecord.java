/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.asyncmsgs.model;

import java.sql.Blob;
import java.util.Date;

/**
 *
 * @author JHOPPESC, richard.ettema
 */
public class AsyncMsgRecord {

    private Long Id = null;
    private String MessageId = null;
    private Date CreationTime = null;
    private Date ResponseTime = null;
    private Long Duration = null;
    private String ServiceName = null;
    private String Direction = null;
    private String CommunityId = null;
    private String Status = null;
    private String ResponseType = null;
    private String Reserved = null;
    private Blob MsgData = null;
    private Blob RspData = null;
    private Blob AckData = null;

    public AsyncMsgRecord() {}

    public Long getId () {
        return Id;
    }

    public void setId (Long Id) {
        this.Id = Id;
    }

    public String getMessageId () {
        return MessageId;
    }

    public void setMessageId (String MessageId) {
        this.MessageId = MessageId;
    }

    public Date getCreationTime () {
        return CreationTime;
    }

    public void setCreationTime (Date CreationTime) {
        this.CreationTime = CreationTime;
    }

    public Date getResponseTime () {
        return ResponseTime;
    }

    public void setResponseTime (Date ResponseTime) {
        this.ResponseTime = ResponseTime;
    }

    public Long getDuration () {
        return Duration;
    }

    public void setDuration (Long Duration) {
        this.Duration = Duration;
    }

    public String getServiceName () {
        return ServiceName;
    }

    public void setServiceName (String ServiceName) {
        this.ServiceName = ServiceName;
    }

    public String getDirection () {
        return Direction;
    }

    public void setDirection (String Direction) {
        this.Direction = Direction;
    }

    public String getCommunityId () {
        return CommunityId;
    }

    public void setCommunityId (String CommunityId) {
        this.CommunityId = CommunityId;
    }

    public String getStatus () {
        return Status;
    }

    public void setStatus (String Status) {
        this.Status = Status;
    }

    public String getResponseType () {
        return ResponseType;
    }

    public void setResponseType (String ResponseType) {
        this.ResponseType = ResponseType;
    }

    public String getReserved () {
        return Reserved;
    }

    public void setReserved (String Reserved) {
        this.Reserved = Reserved;
    }

    public Blob getMsgData () {
        return MsgData;
    }

    public void setMsgData (Blob MsgData) {
        this.MsgData = MsgData;
    }

    public Blob getRspData () {
        return RspData;
    }

    public void setRspData (Blob RspData) {
        this.RspData = RspData;
    }

    public Blob getAckData () {
        return AckData;
    }

    public void setAckData (Blob AckData) {
        this.AckData = AckData;
    }

}
