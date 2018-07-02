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

    public String getrlsId() {
        return rlsId;
    }

}
