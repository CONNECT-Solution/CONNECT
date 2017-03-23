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
package gov.hhs.fha.nhinc.policyengine.adapter.pip;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This contains the implementation of the Adapter PIP (Policy Information Point).
 *
 * @author Les Westberg
 */
public class AdapterPIPImpl {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterPIPImpl.class);
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
     * @throws gov.hhs.fha.nhinc.policyengine.adapter.pip.AdapterPIPException
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
     * followed by the error information.
     * @throws gov.hhs.fha.nhinc.policyengine.adapter.pip.AdapterPIPException
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

}
