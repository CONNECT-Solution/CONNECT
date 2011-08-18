/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientcorrelation.nhinc.model;

import java.util.Date;

/**
 *
 * @author akong
 */
public class PDDeferredCorrelation {

    private Long Id = null;
    private String MessageId = null;
    private String AssigningAuthorityId = null;
    private String PatientId = null;
    private Date CreationTime = null;

    public Long getId () {
        return Id;
    }

    public void setId (Long Id) {
        this.Id = Id;
    }

    public String getMessageId () {
        return MessageId;
    }

    public void setMessageId (String messageId) {
        this.MessageId = messageId;
    }

    public String getAssigningAuthorityId() {
        return AssigningAuthorityId;
    }

    public void setAssigningAuthorityId(String assigningAuthorityId) {
        this.AssigningAuthorityId = assigningAuthorityId;
    }

    public String getPatientId() {
        return PatientId;
    }

    public void setPatientId(String patientId) {
        this.PatientId = patientId;
    }

    public Date getCreationTime () {
        return CreationTime;
    }

    public void setCreationTime (Date creationTime) {
        this.CreationTime = creationTime;
    }


}
