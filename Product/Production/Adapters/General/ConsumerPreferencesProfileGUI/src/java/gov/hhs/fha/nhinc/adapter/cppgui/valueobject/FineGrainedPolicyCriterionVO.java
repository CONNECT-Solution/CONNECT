/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.adapter.cppgui.valueobject;

import gov.hhs.fha.nhinc.adapter.cppgui.CPPConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author patlollav
 */
public class FineGrainedPolicyCriterionVO {

    private static Log log = LogFactory.getLog(FineGrainedPolicyCriterionVO.class);

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

    public String getConfidentialityCodeDesc() {
        return this.getDescription(CPPConstants.CONFIDENTIALITY_CODE_PROPERTIES, this.confidentialityCode);
    }

    public void setConfidentialityCode(String confidentialityCode) {
        this.confidentialityCode = confidentialityCode;
    }

    public String getDocumentTypeCode() {
        return documentTypeCode;
    }
    public String getDocumentTypeCodeDesc()
    {
        return getDescription(CPPConstants.DOCUMENT_TYPE_CODE_PROPERTIES, this.documentTypeCode);
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

    public String getPurposeOfUseDesc() {
        return this.getDescription(CPPConstants.PURPOSE_OF_USE_PROPERTIES, this.purposeOfUse);
    }


    public void setPurposeOfUse(String purposeOfUse) {
        this.purposeOfUse = purposeOfUse;
    }

    public String getUserRole() {
        return userRole;
    }

    public String getUserRoleDesc() {
        return this.getDescription(CPPConstants.USER_ROLE_PROPERTIES, this.userRole);
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }


    private String getDescription(String propertyFile, String propertyName)
    {
        String description = null;

        try
        {
            description = PropertyAccessor.getProperty(propertyFile, propertyName);
        }
        catch (Exception e)
        {
            log.error("Exception while reading the property: " + propertyFile + "." + propertyName, e);
        }

        return description;
    }
}
