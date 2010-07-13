/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientcorrelation.model;

import java.util.Date;

/**
 *
 * @author svalluripalli
 */
public class CorrelatedIdentifiers {
    private Long correlationId;
    private String patientAssigningAuthorityId;
    private String patientId;
    private String CorrelatedPatientAssigningAuthorityId;
    private String CorrelatedPatientId;
    private Date correlationExpirationDate;

    public Date getCorrelationExpirationDate() {
        return correlationExpirationDate;
    }

    public void setCorrelationExpirationDate(Date correlationExpirationDate) {
        this.correlationExpirationDate = correlationExpirationDate;
    }

    public String getCorrelatedPatientAssigningAuthorityId() {
        return CorrelatedPatientAssigningAuthorityId;
    }

    public void setCorrelatedPatientAssigningAuthorityId(String CorrelatedPatientAssigningAuthority) {
        this.CorrelatedPatientAssigningAuthorityId = CorrelatedPatientAssigningAuthority;
    }

    public String getCorrelatedPatientId() {
        return CorrelatedPatientId;
    }

    public void setCorrelatedPatientId(String CorrelatedPatientId) {
        this.CorrelatedPatientId = CorrelatedPatientId;
    }

    public Long getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(Long correlationId) {
        this.correlationId = correlationId;
    }

    public String getPatientAssigningAuthorityId() {
        return patientAssigningAuthorityId;
    }

    public void setPatientAssigningAuthorityId(String patientAssigningAuthority) {
        this.patientAssigningAuthorityId = patientAssigningAuthority;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }  
}
