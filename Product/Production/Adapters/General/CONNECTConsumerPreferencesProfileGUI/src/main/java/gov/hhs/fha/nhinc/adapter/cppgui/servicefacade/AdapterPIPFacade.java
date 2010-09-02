/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.adapter.cppgui.servicefacade;

import gov.hhs.fha.nhinc.adapter.cppgui.valueobject.PatientVO;
import gov.hhs.fha.nhinc.adapter.cppgui.valueobject.PatientPreferencesVO;
import gov.hhs.fha.nhinc.adapter.cppgui.valueobject.FineGrainedPolicyCriterionVO;
import gov.hhs.fha.nhinc.adapter.cppgui.*;
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
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author patlollav
 */
public class AdapterPIPFacade {

    private static Log log = LogFactory.getLog(AdapterPIPFacade.class);

    public String saveConsumerPreferences(StorePtConsentRequestType consentRequest) {
        AdapterPIPProxy adapterPIPProxy = getAdapterPIPProxy();

        AssertionType assertion = null;
        if(consentRequest != null)
        {
            assertion = consentRequest.getAssertion();
        }
        StorePtConsentResponseType consentResponse = adapterPIPProxy.storePtConsent(consentRequest, assertion);

        if (consentResponse != null)
        {
            log.debug("Save Consumer PReference Status: " + consentResponse.getStatus());
            return consentResponse.getStatus();
        }

        return null;
    }

    public AdapterPIPProxy getAdapterPIPProxy() {
        AdapterPIPProxyObjectFactory factory = new AdapterPIPProxyObjectFactory();
        AdapterPIPProxy adapterPIPProxy = factory.getAdapterPIPProxy();
        return adapterPIPProxy;
    }

    public RetrievePtConsentByPtIdResponseType retrieveConsumerPreferences(RetrievePtConsentByPtIdRequestType consentRequest) {
        AdapterPIPProxy adapterPIPProxy = getAdapterPIPProxy();
        AssertionType assertion = null;
        if(consentRequest != null)
        {
            assertion = consentRequest.getAssertion();
        }
        RetrievePtConsentByPtIdResponseType consentResponse = adapterPIPProxy.retrievePtConsentByPtId(consentRequest, assertion);

        return consentResponse;
    }

    public PatientPreferencesVO retriveConsumerPreferences(ConsumerPreferencesSearchCriteria criteria)
    {
        AdapterPIPProxy adapterPIPProxy = getAdapterPIPProxy();

        RetrievePtConsentByPtIdRequestType consentReq = createRetrievePtConsentByPtIdRequestType(criteria);
        AssertionType assertion = null;
        if(consentReq != null)
        {
            assertion = consentReq.getAssertion();
        }

        RetrievePtConsentByPtIdResponseType consentResp = adapterPIPProxy.retrievePtConsentByPtId(consentReq, assertion);

        return convertConsentResponseToPatientPreferences(consentResp);
    }

    private PatientPreferencesVO convertConsentResponseToPatientPreferences(RetrievePtConsentByPtIdResponseType consentResponse)
    {
        PatientPreferencesVO patientPreferences = new PatientPreferencesVO();

        if (consentResponse != null && consentResponse.getPatientPreferences() != null)
        {
            PatientPreferencesType preferencesRespObj = consentResponse.getPatientPreferences();
            
            patientPreferences.setOptIn(preferencesRespObj.isOptIn());

            if (preferencesRespObj.getFineGrainedPolicyCriteria() != null)
            {
                FineGrainedPolicyCriteriaType fineGrainedCriteriaRespObj = preferencesRespObj.getFineGrainedPolicyCriteria();

                if (fineGrainedCriteriaRespObj.getFineGrainedPolicyCriterion() != null && fineGrainedCriteriaRespObj.getFineGrainedPolicyCriterion().size() > 0)
                {
                    for (FineGrainedPolicyCriterionType fineGrainedCriterionRespObj:fineGrainedCriteriaRespObj.getFineGrainedPolicyCriterion() )
                    {
                        FineGrainedPolicyCriterionVO fineGrainedPolicyCriterion = new FineGrainedPolicyCriterionVO();

                        fineGrainedPolicyCriterion.setPolicyOID(fineGrainedCriterionRespObj.getPolicyOID());

                        if (fineGrainedCriterionRespObj.isPermit())
                        {
                            fineGrainedPolicyCriterion.setPermit("Permit");
                        }
                        else
                        {
                            fineGrainedPolicyCriterion.setPermit("Deny");
                        }

                        if (fineGrainedCriterionRespObj.getDocumentTypeCode() != null)
                        {
                            fineGrainedPolicyCriterion.setDocumentTypeCode(fineGrainedCriterionRespObj.getDocumentTypeCode().getCode());
                        }

                        if (fineGrainedCriterionRespObj.getUserRole() != null)
                        {
                            fineGrainedPolicyCriterion.setUserRole(fineGrainedCriterionRespObj.getUserRole().getCode());
                        }

                        if (fineGrainedCriterionRespObj.getPurposeOfUse() != null)
                        {
                            fineGrainedPolicyCriterion.setPurposeOfUse(fineGrainedCriterionRespObj.getPurposeOfUse().getCode());
                        }

                        if (fineGrainedCriterionRespObj.getConfidentialityCode() != null)
                        {
                            fineGrainedPolicyCriterion.setConfidentialityCode(fineGrainedCriterionRespObj.getConfidentialityCode().getCode());
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
    private RetrievePtConsentByPtIdRequestType createRetrievePtConsentByPtIdRequestType(ConsumerPreferencesSearchCriteria criteria) {
        RetrievePtConsentByPtIdRequestType consentReq = new RetrievePtConsentByPtIdRequestType();

        consentReq.setAssigningAuthority(criteria.getAssigningAuthorityID());
        consentReq.setPatientId(criteria.getPatientID());
        return consentReq;
    }

    public String saveOptInConsumerPreference(PatientVO patientVO)
    {
        StorePtConsentRequestType consentRequest = createStorePtConsentRequestType(patientVO);
        String status = saveConsumerPreferences(consentRequest);
        return status;
    }

    private StorePtConsentRequestType createStorePtConsentRequestType(PatientVO patientVO)
    {
        StorePtConsentRequestType consentRequest = new StorePtConsentRequestType();
        PatientPreferencesType patientPreference = new PatientPreferencesType();

        patientPreference.setPatientId(patientVO.getPatientID());
        patientPreference.setAssigningAuthority(patientVO.getAssigningAuthorityID());

        if (patientVO.getPatientPreferences() != null)
        {
            PatientPreferencesVO patientPreferencesVO = patientVO.getPatientPreferences();

            patientPreference.setOptIn(patientPreferencesVO.getOptIn());

            if (patientPreferencesVO.getFineGrainedPolicyCriteria() != null
                    && patientPreferencesVO.getFineGrainedPolicyCriteria().size() > 0)
            {
                FineGrainedPolicyCriteriaType fineGrainedPolicyCriteria = new FineGrainedPolicyCriteriaType();

                for (FineGrainedPolicyCriterionVO fineGrainedPolicyCriterionVO : patientPreferencesVO.getFineGrainedPolicyCriteria())
                {
                    FineGrainedPolicyCriterionType fineGrainedPolicyCriterion = new FineGrainedPolicyCriterionType();

                    fineGrainedPolicyCriterion.setPolicyOID(fineGrainedPolicyCriterionVO.getPolicyOID());
                    
                    if (CPPConstants.PERMIT.equalsIgnoreCase(fineGrainedPolicyCriterionVO.getPermit()))
                    {
                        fineGrainedPolicyCriterion.setPermit(true);
                    }
                    else
                    {
                        fineGrainedPolicyCriterion.setPermit(false);
                    }

                    if (fineGrainedPolicyCriterionVO.getDocumentTypeCode() != null &&
                            !fineGrainedPolicyCriterionVO.getDocumentTypeCode().isEmpty())
                    {
                        CeType documentTypeCode = new CeType();
                        documentTypeCode.setCode(fineGrainedPolicyCriterionVO.getDocumentTypeCode());
                        fineGrainedPolicyCriterion.setDocumentTypeCode(documentTypeCode);
                    }

                    if (fineGrainedPolicyCriterionVO.getUserRole() != null &&
                            !fineGrainedPolicyCriterionVO.getUserRole().isEmpty())
                    {
                        CeType userRole = new CeType();
                        userRole.setCode(fineGrainedPolicyCriterionVO.getUserRole());
                        fineGrainedPolicyCriterion.setUserRole(userRole);
                    }

                    if (fineGrainedPolicyCriterionVO.getPurposeOfUse() != null &&
                            !fineGrainedPolicyCriterionVO.getUserRole().isEmpty())
                    {
                        CeType purposeOfUse = new CeType();
                        purposeOfUse.setCode(fineGrainedPolicyCriterionVO.getPurposeOfUse());
                        fineGrainedPolicyCriterion.setPurposeOfUse(purposeOfUse);
                    }

                    if (fineGrainedPolicyCriterionVO.getConfidentialityCode() != null &&
                            !fineGrainedPolicyCriterionVO.getConfidentialityCode().isEmpty())
                    {
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
