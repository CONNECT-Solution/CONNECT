package gov.hhs.fha.nhinc.patientcorrelation.nhinc.model;

import java.io.Serializable;

/**
 *
 * @author ptambellini
 */
public class RecordLocatorService implements Serializable {

    private static final long serialVersionUID = -5756784032763662810L;
    private Long id;
    private String requestedPatientId;
    private String patientId;
    private String assigningAuthorityId;

    public String getAssigningAuthorityId() {
        return assigningAuthorityId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestedPatientId() {
        return requestedPatientId;
    }

    public void setRequestedPatientId(String requestedPatientId) {
        this.requestedPatientId = requestedPatientId;
    }

    public void setAssigningAuthorityId(String assigningAuthorityId) {
        this.assigningAuthorityId = assigningAuthorityId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

}
