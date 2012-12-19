/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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
package gov.hhs.fha.nhinc.adapter.cppgui.valueobject;

import gov.hhs.fha.nhinc.adapter.cppgui.CPPConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

import org.apache.log4j.Logger;

/**
 * 
 * @author patlollav
 */
public class FineGrainedPolicyCriterionVO {

    private static final Logger LOG = Logger.getLogger(FineGrainedPolicyCriterionVO.class);

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

    public String getDocumentTypeCodeDesc() {
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

    private String getDescription(String propertyFile, String propertyName) {
        String description = null;

        try {
            if ((propertyFile == null) || ((propertyFile.trim()).equals(""))) {
                LOG.error("propertyFile value is null");
            } else if ((propertyName == null) || ((propertyName.trim()).equals(""))) {
                LOG.error("propertyName value is null");
            } else {
                description = PropertyAccessor.getInstance().getProperty(propertyFile, propertyName);
            }

        } catch (Exception e) {
            LOG.error("Exception while reading the property: " + propertyFile + "." + propertyName, e);
        }

        return description;
    }
}
