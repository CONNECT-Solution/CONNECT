/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.hibernate;

import java.util.Date;
import java.sql.Blob;

/**
 *
 * @author MFLYNN02
 */
public class AuditRepositoryRecord {
    private int id = 0;
    private Date timeStamp = null;
    private int eventId = 0;
    private String userId = null;
    private int participationTypeCode = 0;
    private int participationTypeCodeRole = 0;
    private String participationIDTypeCode = null;
    private String receiverPatientId = null;
    private String senderPatientId = null;
    private String communityId = null;
    private String messageType = null;
    private Blob message = null;
    
    public AuditRepositoryRecord() {
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Blob getMessage() {
        return message;
    }

    public void setMessage(Blob message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getParticipationIDTypeCode() {
        return participationIDTypeCode;
    }

    public void setParticipationIDTypeCode(String participationIDTypeCode) {
        this.participationIDTypeCode = participationIDTypeCode;
    }

    public int getParticipationTypeCode() {
        return participationTypeCode;
    }

    public void setParticipationTypeCode(int participationTypeCode) {
        this.participationTypeCode = participationTypeCode;
    }

    public int getParticipationTypeCodeRole() {
        return participationTypeCodeRole;
    }

    public void setParticipationTypeCodeRole(int participationTypeCodeRole) {
        this.participationTypeCodeRole = participationTypeCodeRole;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getReceiverPatientId() {
        return receiverPatientId;
    }

    public void setReceiverPatientId(String receiverPatientId) {
        this.receiverPatientId = receiverPatientId;
    }

    public String getSenderPatientId() {
        return senderPatientId;
    }

    public void setSenderPatientId(String senderPatientId) {
        this.senderPatientId = senderPatientId;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
