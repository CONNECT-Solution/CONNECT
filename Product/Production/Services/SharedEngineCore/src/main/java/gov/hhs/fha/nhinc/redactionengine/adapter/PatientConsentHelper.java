/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.redactionengine.adapter;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.FineGrainedPolicyCriteriaType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FineGrainedPolicyCriterionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdResponseType;
import gov.hhs.fha.nhinc.policyengine.adapter.pip.AdapterPIPException;
import gov.hhs.fha.nhinc.policyengine.adapter.pip.AdapterPIPImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Neil Webb
 */
public class PatientConsentHelper {

    private static final Logger LOG = LoggerFactory.getLogger(PatientConsentHelper.class);

    protected AdapterPIPImpl getAdapterPIP() {
        return new AdapterPIPImpl();
    }

    public PatientPreferencesType retrievePatientConsentbyPatientId(String patientId, String assigningAuthorityId) {
        PatientPreferencesType response = null;
        try {
            LOG.debug("Retrieving patient preferences by patient id. Patient id (" + patientId
                + "), assigning authority id (" + assigningAuthorityId + ")");
            RetrievePtConsentByPtIdRequestType retrieveRequest = new RetrievePtConsentByPtIdRequestType();

            retrieveRequest.setPatientId(patientId);
            retrieveRequest.setAssigningAuthority(assigningAuthorityId);

            RetrievePtConsentByPtIdResponseType retrieveResponse = getAdapterPIP().retrievePtConsentByPtId(
                retrieveRequest);
            if (retrieveResponse != null) {
                response = retrieveResponse.getPatientPreferences();
            }
        } catch (AdapterPIPException ape) {
            LOG.error("Error retrieving patient preferences. Patient id (" + patientId + "), assigning authority id ("
                + assigningAuthorityId + ") Error: " + ape.getMessage(), ape);
        }
        return response;
    }

    public PatientPreferencesType retrievePatientConsentbyDocumentId(String homeCommunityId, String repositoryId,
        String documentId) {

        PatientPreferencesType response = null;
        try {
            LOG.debug("Retrieving patient preferences by document id. Home community id (" + homeCommunityId
                + "), repository id (" + repositoryId + "), document id (" + documentId + ")");
            RetrievePtConsentByPtDocIdRequestType retrieveRequest = new RetrievePtConsentByPtDocIdRequestType();

            retrieveRequest.setHomeCommunityId(homeCommunityId);
            retrieveRequest.setRepositoryId(repositoryId);
            retrieveRequest.setDocumentId(documentId);

            RetrievePtConsentByPtDocIdResponseType retrieveResponse = getAdapterPIP().retrievePtConsentByPtDocId(
                retrieveRequest);
            if (retrieveResponse != null) {
                response = retrieveResponse.getPatientPreferences();
                LOG.debug("Retrieved patient consent document.");
            } else {
                LOG.debug("Patient consent document was null.");
            }
        } catch (AdapterPIPException ape) {
            LOG.error(
                "Error retrieving patient preferences. Home community id (" + homeCommunityId
                + "), repository id (" + repositoryId + "), document id (" + documentId + ") Error: "
                + ape.getMessage(), ape);
        }
        return response;
    }

    /**
     * This method will extract the document type from Patient Preferences and compare with the one in document
     * response, if the document type present and matches then returns true otherwise it is considered as false (Deny).
     *
     * @param documentType
     * @param ptPreferences
     * @return boolean
     */
    public boolean documentSharingAllowed(String documentType, PatientPreferencesType ptPreferences) {
        LOG.debug("Begin extract permit value from patient preferences - document type code tested: " + documentType);
        // Default to false in case something goes wrong.
        boolean allowDocumentSharing = false;
        FineGrainedPolicyCriteriaType findGrainedPolicy;
        if (documentType == null || documentType.isEmpty()) {
            LOG.error("Invalid documentType");
            return allowDocumentSharing;
        }
        if (ptPreferences == null) {
            LOG.error("Patient preferences was null");
            return allowDocumentSharing;
        }

        findGrainedPolicy = ptPreferences.getFineGrainedPolicyCriteria();
        if (findGrainedPolicy == null || findGrainedPolicy.getFineGrainedPolicyCriterion() == null
            || findGrainedPolicy.getFineGrainedPolicyCriterion().isEmpty()) {
            // No fine grained policy info - use simple opt-in/opt-out
            allowDocumentSharing = ptPreferences.isOptIn();
            LOG.debug("Simple opt-in/opt-out value from patient preferences: " + allowDocumentSharing);
        } else {
            // No global opt-in/opt-out. Look at fine grained policy for opt-in limited
            LOG.debug("Patient preferences has " + findGrainedPolicy.getFineGrainedPolicyCriterion().size()
                + " fine grained policy criterion.");

            String criterionDocumentTypeCode;
            for (FineGrainedPolicyCriterionType eachFineGrainedPolicyCriterion : findGrainedPolicy
                .getFineGrainedPolicyCriterion()) {
                if (eachFineGrainedPolicyCriterion != null) {
                    if (eachFineGrainedPolicyCriterion.getDocumentTypeCode() != null) {
                        criterionDocumentTypeCode = eachFineGrainedPolicyCriterion.getDocumentTypeCode().getCode();
                        LOG.debug("Looking at criterion for document type: " + criterionDocumentTypeCode);
                        if (criterionDocumentTypeCode != null && !criterionDocumentTypeCode.isEmpty()
                            && criterionDocumentTypeCode.equals(documentType)) {
                            allowDocumentSharing = eachFineGrainedPolicyCriterion.isPermit();
                            // The algorithm is to use the first found - leave after the first match.
                            break;
                        }
                    } else if (isDefaultFineGrainedPolicyCriterion(eachFineGrainedPolicyCriterion)) {
                        allowDocumentSharing = eachFineGrainedPolicyCriterion.isPermit();
                        break;
                    }
                }
            }
            LOG.debug("End extract permit value from fine grained policy criterian");
        }
        LOG.debug("Permit sharing flag for document filter: " + allowDocumentSharing);
        return allowDocumentSharing;
    }

    protected boolean isDefaultFineGrainedPolicyCriterion(FineGrainedPolicyCriterionType criterion) {
        LOG.debug("Begin isDefaultFineGrainedPolicyCriterion");
        boolean defaultCriterion = false;
        if (criterion != null) {
            // Add other values when additional options are considered.
            defaultCriterion = ((criterion.getDocumentTypeCode() == null));
        }
        LOG.debug("End isDefaultFineGrainedPolicyCriterion - value: " + defaultCriterion);
        return defaultCriterion;
    }
}
