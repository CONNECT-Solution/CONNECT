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
package gov.hhs.fha.nhinc.policyengine.adapter.pip;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentResponseType;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

import org.apache.log4j.Logger;

/**
 * This contains the implementation of the Adapter PIP (Policy Information Point).
 *
 * @author Les Westberg
 */
public class AdapterPIPImpl {

    private static final Logger LOG = Logger.getLogger(AdapterPIPImpl.class);
    private static final String ASSERTIONINFO_PROPFILE_NAME = "assertioninfo";

    protected PatientConsentManager getPatientConsentManager() {
        return new PatientConsentManager();
    }

    /**
     * Retrieve the patient consent settings for the given patient ID.
     *
     * @param request The patient ID for which the consent is being retrieved.
     * @return The patient consent information for that patient.
     * @throws AdapterPIPException This exception is thrown if the data cannot be retrieved.
     */
    public RetrievePtConsentByPtIdResponseType retrievePtConsentByPtId(RetrievePtConsentByPtIdRequestType request)
            throws AdapterPIPException {
        RetrievePtConsentByPtIdResponseType oResponse = new RetrievePtConsentByPtIdResponseType();

        String sPatientId = "";
        String sAssigningAuthority = "";

        if ((request != null) && (request.getAssigningAuthority() != null)) {
            sAssigningAuthority = request.getAssigningAuthority();
        }

        if ((request != null) && (request.getPatientId() != null)) {
            sPatientId = request.getPatientId();
        }

        PatientConsentManager oManager = new PatientConsentManager();
        PatientPreferencesType oPtPref = oManager.retrievePatientConsentByPatientId(sPatientId, sAssigningAuthority);

        if (oPtPref != null) {
            oResponse.setPatientPreferences(oPtPref);
        }

        return oResponse;
    }

    /**
     * Retrieve the patient consent settings for the patient associated with the given document identifiers.
     *
     * @param request The doucment identifiers of a document in the repository.
     * @return The patient consent settings for the patient associated with the given document identifiers.
     */
    public RetrievePtConsentByPtDocIdResponseType retrievePtConsentByPtDocId(
            RetrievePtConsentByPtDocIdRequestType request) throws AdapterPIPException {
        LOG.trace("Begin AdapterPIPImpl.retrievePtIdFromDocumentId()..");
        RetrievePtConsentByPtDocIdResponseType oResponse = new RetrievePtConsentByPtDocIdResponseType();

        String sHomeCommunityId = "";
        String sRepositoryId = "";
        String sDocumentUniqueId = "";

        if ((request != null) && (request.getHomeCommunityId() != null)) {
            sHomeCommunityId = request.getHomeCommunityId();
        }

        if ((request != null) && (request.getRepositoryId() != null)) {
            sRepositoryId = request.getRepositoryId();
        }

        if ((request != null) && (request.getDocumentId() != null)) {
            sDocumentUniqueId = request.getDocumentId();
        }

        PatientConsentManager oManager = new PatientConsentManager();
        PatientPreferencesType oPtPref = oManager.retrievePatientConsentByDocId(sHomeCommunityId, sRepositoryId,
                sDocumentUniqueId);

        if (oPtPref != null) {
            oResponse.setPatientPreferences(oPtPref);
        }

        LOG.trace("End AdapterPIPImpl.retrievePtIdFromDocumentId()..");
        return oResponse;
    }

    /**
     * Store the patient consent information into the repository.
     *
     * @param request The patient consent settings to be stored.
     * @return Status of the storage. Currently this is either "SUCCESS" or or the word "FAILED" followed by a ':'
     *         followed by the error information.
     */
    public StorePtConsentResponseType storePtConsent(StorePtConsentRequestType request) throws AdapterPIPException {
        LOG.trace("Begin AdapterPIPImpl.storePtConsent()..");
        StorePtConsentResponseType oResponse = new StorePtConsentResponseType();
        try {
            if ((request != null) && (request.getPatientPreferences() != null)) {
                PatientConsentManager oManager = getPatientConsentManager();
                oManager.storePatientConsent(request.getPatientPreferences());
                oResponse.setStatus("SUCCESS");
            } else {
                throw new AdapterPIPException("StorePtConsentRequest requires patient preferences");
            }
        } catch (Exception e) {
            oResponse.setStatus("FAILED: " + e.getMessage());
            String sErrorMessage = "Failed to store the patient consent.  Error: " + e.getMessage();
            LOG.error(sErrorMessage, e);
            throw new AdapterPIPException(sErrorMessage, e);
        }
        return oResponse;
    }

    /**
     * This method is used to build Assertion information to send Notification to Entity Notification Consumer
     *
     * @param sHid
     * @return AssertionType
     */
    private AssertionType buildAssertionInfo(String sHid) {
        LOG.trace("Begin - CPPOperations.buildAssertion() - ");
        AssertionType assertion = new AssertionType();
        String svalue = "";
        try {
            PropertyAccessor propertyAccessor = PropertyAccessor.getInstance();

            assertion.setHaveSignature(true);
            assertion.setHaveWitnessSignature(true);
            svalue = propertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.PERMISSION_DATE);
            if (svalue != null && svalue.length() > 0) {
                assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions()
                        .setNotBefore(svalue.trim());
            } else {
                assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().setNotBefore("");
            }
            svalue = propertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.EXPIRATION_DATE);
            if (null != svalue && svalue.length() > 0) {
                assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions()
                        .setNotOnOrAfter(svalue.trim());
            } else {
                assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions()
                        .setNotOnOrAfter("");
            }
            PersonNameType aPersonName = new PersonNameType();
            svalue = propertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.FIRST_NAME);
            if (null != svalue && svalue.length() > 0) {
                aPersonName.setGivenName(svalue.trim());
            } else {
                aPersonName.setGivenName("");
            }
            svalue = propertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.LAST_NAME);
            if (null != svalue && svalue.length() > 0) {
                aPersonName.setFamilyName(svalue.trim());
            } else {
                aPersonName.setFamilyName("");
            }
            UserType aUser = new UserType();
            aUser.setPersonName(aPersonName);
            HomeCommunityType userHm = new HomeCommunityType();
            svalue = propertyAccessor.getProperty(CDAConstants.SubscribeeCommunityList_PROPFILE_NAME, sHid);
            if (null != svalue && svalue.length() > 0) {
                userHm.setName(svalue.trim());
            } else {
                userHm.setName("");
            }
            userHm.setHomeCommunityId(sHid);
            aUser.setOrg(userHm);
            CeType userCe = new CeType();
            svalue = propertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.USER_ROLE_CD);
            if (null != svalue && svalue.length() > 0) {
                userCe.setCode(svalue.trim());
            } else {
                userCe.setCode("");
            }
            svalue = propertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.USER_ROLE_CD_SYSTEM);
            if (null != svalue && svalue.length() > 0) {
                userCe.setCodeSystem(svalue.trim());
            } else {
                userCe.setCodeSystem("");
            }
            svalue = propertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.USER_ROLE_CD_SYSTEM_NAME);
            if (null != svalue && svalue.length() > 0) {
                userCe.setCodeSystemName(svalue.trim());
            } else {
                userCe.setCodeSystemName("");
            }
            svalue = propertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.USER_ROLE_DISPLAY_NAME);
            if (null != svalue && svalue.length() > 0) {
                userCe.setDisplayName(svalue.trim());
            } else {
                userCe.setDisplayName("");
            }
            aUser.setRoleCoded(userCe);
            svalue = propertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.USER_NAME);
            if (null != svalue && svalue.length() > 0) {
                aUser.setUserName(svalue.trim());
            } else {
                aUser.setUserName("");
            }
            assertion.setUserInfo(aUser);
            svalue = propertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.ORG_NAME);
            HomeCommunityType hm = new HomeCommunityType();
            if (null != svalue && svalue.length() > 0) {
                hm.setName(svalue.trim());
            } else {
                hm.setName("");
            }
            assertion.setHomeCommunity(hm);
            CeType ce = new CeType();
            svalue = propertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.PURPOSE_FOR_USE_ROLE_CD);
            if (null != svalue && svalue.length() > 0) {
                ce.setCode(svalue.trim());
            } else {
                ce.setCode("");
            }
            svalue = propertyAccessor
                    .getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.PURPOSE_FOR_USE_CODE_SYSTEM);
            if (null != svalue && svalue.length() > 0) {
                ce.setCodeSystem(svalue.trim());
            } else {
                ce.setCodeSystem("");
            }
            svalue = propertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME,
                    CDAConstants.PURPOSE_FOR_USE_CODE_SYSTEM_NAME);
            if (null != svalue && svalue.length() > 0) {
                ce.setCodeSystemName(svalue.trim());
            } else {
                ce.setCodeSystemName("");
            }
            svalue = propertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME,
                    CDAConstants.PURPOSE_FOR_USE_DISPLAY_NAME);
            if (null != svalue && svalue.length() > 0) {
                ce.setDisplayName(svalue.trim());
            } else {
                ce.setDisplayName("");
            }
            assertion.setPurposeOfDisclosureCoded(ce);
            svalue = propertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.ACCESS_POLICY_CONSENT);
            if (null != svalue && svalue.length() > 0) {
                assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getAccessConsentPolicy()
                        .add(svalue.trim());
            } else {
                // Do not add empty string to AccessConsentPolicy (Can occur 0 times)
                // assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getAccessConsentPolicy().add("");
            }
            svalue = propertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME,
                    CDAConstants.INSTANCE_ACCESS_POLICY_CONSENT);
            if (null != svalue && svalue.length() > 0) {
                assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getInstanceAccessConsentPolicy()
                        .add(svalue.trim());
            } else {
                // Do not add empty string to InstanceAccessConsentPolicy (Can occur 0 times)
                // assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getInstanceAccessConsentPolicy().add("");
            }
        } catch (PropertyAccessException propExp) {
            propExp.printStackTrace();
        }
        LOG.trace("End - CPPOperations.buildAssertion() - ");
        return assertion;
    }
}
