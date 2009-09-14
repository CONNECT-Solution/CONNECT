/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.adapter.cppgui.valueobject;

/**
 *
 * @author patlollav
 */
public class FineGrainedPolicyCriterionVO {

    /** Creates a new instance of FineGrainedPolicyCriteriaForm */
    public FineGrainedPolicyCriterionVO() {
    }
    private String documentTypeCode;
    private String userRole;
    private String purposeOfUse;
    private String confidentialityCode;
    private String policyOID;
    private String permit;

    public String getConfidentialityCode() {
        return confidentialityCode;
    }

    public void setConfidentialityCode(String confidentialityCode) {
        this.confidentialityCode = confidentialityCode;
    }

    public String getDocumentTypeCode() {
        return documentTypeCode;
    }

    public void setDocumentTypeCode(String documentTypeCode) {
        this.documentTypeCode = documentTypeCode;
    }

    public String getPermit() {
        return permit;
    }

    public void setPermit(String permit) {
        this.permit = permit;
    }

    public String getPolicyOID() {
        return policyOID;
    }

    public void setPolicyOID(String policyOID) {
        this.policyOID = policyOID;
    }

    public String getPurposeOfUse() {
        return purposeOfUse;
    }

    public void setPurposeOfUse(String purposeOfUse) {
        this.purposeOfUse = purposeOfUse;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
    
}
