package gov.hhs.fha.nhinc.patientcorrelation.nhinc.model;

import java.io.Serializable;

/**
 *
 * @author ptambellini
 */
public class RecordLocatorService implements Serializable {

    private static final long serialVersionUID = -5756784032763662810L;
    private Long id;
    private String rlsId;
    private String patientId;
    private String assigningAuthorityId;

    public String getAssigningAuthorityId() {
        return assigningAuthorityId;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the rlsId
     */
    public String getRlsId() {
        return rlsId;
    }

    public void setAssigningAuthorityId(String assigningAuthorityId) {
        assigningAuthorityId = assigningAuthorityId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        patientId = patientId;
    }

    public void setRlsId(String rlsId) {
        rlsId = rlsId;
    }

}
