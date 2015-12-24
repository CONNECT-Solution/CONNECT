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
package gov.hhs.fha.nhinc.adapter.cppgui.servicefacade;

import gov.hhs.fha.nhinc.adapter.cppgui.CPPConstants;
import gov.hhs.fha.nhinc.adapter.cppgui.ConsumerPreferencesSearchCriteria;
import gov.hhs.fha.nhinc.adapter.cppgui.valueobject.FineGrainedPolicyCriterionVO;
import gov.hhs.fha.nhinc.adapter.cppgui.valueobject.PatientPreferencesVO;
import gov.hhs.fha.nhinc.adapter.cppgui.valueobject.PatientVO;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FineGrainedPolicyCriteriaType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FineGrainedPolicyCriterionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentResponseType;
import gov.hhs.fha.nhinc.policyengine.adapter.pip.proxy.AdapterPIPProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.pip.proxy.AdapterPIPProxyObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author patlollav
 */
public class AdapterPIPFacade {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterPIPFacade.class);

    public String saveConsumerPreferences(StorePtConsentRequestType consentRequest) {
        AdapterPIPProxy adapterPIPProxy = getAdapterPIPProxy();

        AssertionType assertion = null;
        if (consentRequest != null) {
            assertion = consentRequest.getAssertion();
        }
        StorePtConsentResponseType consentResponse = adapterPIPProxy.storePtConsent(consentRequest, assertion);

        if (consentResponse != null) {
            LOG.debug("Save Consumer PReference Status: " + consentResponse.getStatus());
            return consentResponse.getStatus();
        }

        return null;
    }

    public AdapterPIPProxy getAdapterPIPProxy() {
        AdapterPIPProxyObjectFactory factory = new AdapterPIPProxyObjectFactory();
        AdapterPIPProxy adapterPIPProxy = factory.getAdapterPIPProxy();
        return adapterPIPProxy;
    }

    public RetrievePtConsentByPtIdResponseType retrieveConsumerPreferences(
            RetrievePtConsentByPtIdRequestType consentRequest) {
        AdapterPIPProxy adapterPIPProxy = getAdapterPIPProxy();
        AssertionType assertion = null;
        if (consentRequest != null) {
            assertion = consentRequest.getAssertion();
        }
        RetrievePtConsentByPtIdResponseType consentResponse = adapterPIPProxy.retrievePtConsentByPtId(consentRequest,
                assertion);

        return consentResponse;
    }

    public PatientPreferencesVO retriveConsumerPreferences(ConsumerPreferencesSearchCriteria criteria) {
        AdapterPIPProxy adapterPIPProxy = getAdapterPIPProxy();

        RetrievePtConsentByPtIdRequestType consentReq = createRetrievePtConsentByPtIdRequestType(criteria);
        AssertionType assertion = null;
        if (consentReq != null) {
            assertion = consentReq.getAssertion();
        }

        RetrievePtConsentByPtIdResponseType consentResp = adapterPIPProxy
                .retrievePtConsentByPtId(consentReq, assertion);

        return convertConsentResponseToPatientPreferences(consentResp);
    }

    private PatientPreferencesVO convertConsentResponseToPatientPreferences(
            RetrievePtConsentByPtIdResponseType consentResponse) {
        PatientPreferencesVO patientPreferences = new PatientPreferencesVO();

        if (consentResponse != null && consentResponse.getPatientPreferences() != null) {
            PatientPreferencesType preferencesRespObj = consentResponse.getPatientPreferences();

            patientPreferences.setOptIn(preferencesRespObj.isOptIn());

            if (preferencesRespObj.getFineGrainedPolicyCriteria() != null) {
                FineGrainedPolicyCriteriaType fineGrainedCriteriaRespObj = preferencesRespObj
                        .getFineGrainedPolicyCriteria();

                if (fineGrainedCriteriaRespObj.getFineGrainedPolicyCriterion() != null
                        && fineGrainedCriteriaRespObj.getFineGrainedPolicyCriterion().size() > 0) {
                    for (FineGrainedPolicyCriterionType fineGrainedCriterionRespObj : fineGrainedCriteriaRespObj
                            .getFineGrainedPolicyCriterion()) {
                        FineGrainedPolicyCriterionVO fineGrainedPolicyCriterion = new FineGrainedPolicyCriterionVO();

                        fineGrainedPolicyCriterion.setPolicyOID(fineGrainedCriterionRespObj.getPolicyOID());

                        if (fineGrainedCriterionRespObj.isPermit()) {
                            fineGrainedPolicyCriterion.setPermit("Permit");
                        } else {
                            fineGrainedPolicyCriterion.setPermit("Deny");
                        }

                        if (fineGrainedCriterionRespObj.getDocumentTypeCode() != null) {
                            fineGrainedPolicyCriterion.setDocumentTypeCode(fineGrainedCriterionRespObj
                                    .getDocumentTypeCode().getCode());
                        }

                        if (fineGrainedCriterionRespObj.getUserRole() != null) {
                            fineGrainedPolicyCriterion.setUserRole(fineGrainedCriterionRespObj.getUserRole().getCode());
                        }

                        if (fineGrainedCriterionRespObj.getPurposeOfUse() != null) {
                            fineGrainedPolicyCriterion.setPurposeOfUse(fineGrainedCriterionRespObj.getPurposeOfUse()
                                    .getCode());
                        }

                        if (fineGrainedCriterionRespObj.getConfidentialityCode() != null) {
                            fineGrainedPolicyCriterion.setConfidentialityCode(fineGrainedCriterionRespObj
                                    .getConfidentialityCode().getCode());
                        }

                        patientPreferences.addFineGrainedPolicyCriterion(fineGrainedPolicyCriterion);
                    }
                }
            }
        }

        return patientPreferences;
    }

    /**
     *
     * @param criteria
     * @return
     */
    private RetrievePtConsentByPtIdRequestType createRetrievePtConsentByPtIdRequestType(
            ConsumerPreferencesSearchCriteria criteria) {
        RetrievePtConsentByPtIdRequestType consentReq = new RetrievePtConsentByPtIdRequestType();

        consentReq.setAssigningAuthority(criteria.getAssigningAuthorityID());
        consentReq.setPatientId(criteria.getPatientID());
        return consentReq;
    }

    public String saveOptInConsumerPreference(PatientVO patientVO) {
        StorePtConsentRequestType consentRequest = createStorePtConsentRequestType(patientVO);
        String status = saveConsumerPreferences(consentRequest);
        return status;
    }

    private StorePtConsentRequestType createStorePtConsentRequestType(PatientVO patientVO) {
        StorePtConsentRequestType consentRequest = new StorePtConsentRequestType();
        PatientPreferencesType patientPreference = new PatientPreferencesType();

        LOG.debug("createStorePtConsentRequestType - patientVO.getPatientID(): " + patientVO.getPatientID());
        LOG.debug("createStorePtConsentRequestType - patientVO.getAssigningAuthorityID(): "
                + patientVO.getAssigningAuthorityID());

        patientPreference.setPatientId(patientVO.getPatientID());
        patientPreference.setAssigningAuthority(patientVO.getAssigningAuthorityID());

        if (patientVO.getPatientPreferences() != null) {
            PatientPreferencesVO patientPreferencesVO = patientVO.getPatientPreferences();

            patientPreference.setOptIn(patientPreferencesVO.getOptIn());

            if (patientPreferencesVO.getFineGrainedPolicyCriteria() != null
                    && patientPreferencesVO.getFineGrainedPolicyCriteria().size() > 0) {
                FineGrainedPolicyCriteriaType fineGrainedPolicyCriteria = new FineGrainedPolicyCriteriaType();

                for (FineGrainedPolicyCriterionVO fineGrainedPolicyCriterionVO : patientPreferencesVO
                        .getFineGrainedPolicyCriteria()) {
                    FineGrainedPolicyCriterionType fineGrainedPolicyCriterion = new FineGrainedPolicyCriterionType();

                    fineGrainedPolicyCriterion.setPolicyOID(fineGrainedPolicyCriterionVO.getPolicyOID());

                    if (CPPConstants.PERMIT.equalsIgnoreCase(fineGrainedPolicyCriterionVO.getPermit())) {
                        fineGrainedPolicyCriterion.setPermit(true);
                    } else {
                        fineGrainedPolicyCriterion.setPermit(false);
                    }

                    if (fineGrainedPolicyCriterionVO.getDocumentTypeCode() != null
                            && !fineGrainedPolicyCriterionVO.getDocumentTypeCode().isEmpty()) {
                        CeType documentTypeCode = new CeType();
                        documentTypeCode.setCode(fineGrainedPolicyCriterionVO.getDocumentTypeCode());
                        fineGrainedPolicyCriterion.setDocumentTypeCode(documentTypeCode);
                    }

                    if (fineGrainedPolicyCriterionVO.getUserRole() != null
                            && !fineGrainedPolicyCriterionVO.getUserRole().isEmpty()) {
                        CeType userRole = new CeType();
                        userRole.setCode(fineGrainedPolicyCriterionVO.getUserRole());
                        fineGrainedPolicyCriterion.setUserRole(userRole);
                    }

                    if (fineGrainedPolicyCriterionVO.getPurposeOfUse() != null
                            && !fineGrainedPolicyCriterionVO.getUserRole().isEmpty()) {
                        CeType purposeOfUse = new CeType();
                        purposeOfUse.setCode(fineGrainedPolicyCriterionVO.getPurposeOfUse());
                        fineGrainedPolicyCriterion.setPurposeOfUse(purposeOfUse);
                    }

                    if (fineGrainedPolicyCriterionVO.getConfidentialityCode() != null
                            && !fineGrainedPolicyCriterionVO.getConfidentialityCode().isEmpty()) {
                        CeType confidentialityCode = new CeType();
                        confidentialityCode.setCode(fineGrainedPolicyCriterionVO.getConfidentialityCode());
                        fineGrainedPolicyCriterion.setConfidentialityCode(confidentialityCode);
                    }

                    fineGrainedPolicyCriteria.getFineGrainedPolicyCriterion().add(fineGrainedPolicyCriterion);
                    patientPreference.setFineGrainedPolicyCriteria(fineGrainedPolicyCriteria);
                }
            }
            consentRequest.setPatientPreferences(patientPreference);
        }

        return consentRequest;
    }
}
