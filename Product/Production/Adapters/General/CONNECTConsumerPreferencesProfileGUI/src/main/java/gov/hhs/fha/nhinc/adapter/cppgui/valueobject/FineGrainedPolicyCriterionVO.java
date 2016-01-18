/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author patlollav
 */
public class FineGrainedPolicyCriterionVO implements Serializable {

    private static final long serialVersionUID = 3208678668380200036L;
    private static final Logger LOG = LoggerFactory.getLogger(FineGrainedPolicyCriterionVO.class);

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
        return getDescription(CPPConstants.CONFIDENTIALITY_CODE_PROPERTIES, confidentialityCode);
    }

    public void setConfidentialityCode(final String confidentialityCode) {
        this.confidentialityCode = confidentialityCode;
    }

    public String getDocumentTypeCode() {
        return documentTypeCode;
    }

    public String getDocumentTypeCodeDesc() {
        return getDescription(CPPConstants.DOCUMENT_TYPE_CODE_PROPERTIES, documentTypeCode);
    }

    public void setDocumentTypeCode(final String documentTypeCode) {
        this.documentTypeCode = documentTypeCode;
    }

    public String getPermit() {
        return permit;
    }

    public void setPermit(final String permit) {
        this.permit = permit;
    }

    public String getPolicyOID() {
        return policyOID;
    }

    public void setPolicyOID(final String policyOID) {
        this.policyOID = policyOID;
    }

    public String getPurposeOfUse() {
        return purposeOfUse;
    }

    public String getPurposeOfUseDesc() {
        return getDescription(CPPConstants.PURPOSE_OF_USE_PROPERTIES, purposeOfUse);
    }

    public void setPurposeOfUse(final String purposeOfUse) {
        this.purposeOfUse = purposeOfUse;
    }

    public String getUserRole() {
        return userRole;
    }

    public String getUserRoleDesc() {
        return getDescription(CPPConstants.USER_ROLE_PROPERTIES, userRole);
    }

    public void setUserRole(final String userRole) {
        this.userRole = userRole;
    }

    private String getDescription(final String propertyFile, final String propertyName) {
        String description = null;

        try {
            if (propertyFile == null || propertyFile.trim().isEmpty()) {
                LOG.error("propertyFile value is null");
            } else if (propertyName == null || propertyName.trim().isEmpty()) {
                LOG.error("propertyName value is null");
            } else {
                description = PropertyAccessor.getInstance().getProperty(propertyFile, propertyName);
            }

        } catch (final Exception e) {
            LOG.error("Exception while reading the property: " + propertyFile + "." + propertyName, e);
        }

        return description;
    }
}
