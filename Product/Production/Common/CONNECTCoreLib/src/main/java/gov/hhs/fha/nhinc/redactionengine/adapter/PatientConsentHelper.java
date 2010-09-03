/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.redactionengine.adapter;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.FineGrainedPolicyCriteriaType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FineGrainedPolicyCriterionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdResponseType;
import gov.hhs.fha.nhinc.policyengine.adapter.pip.AdapterPIPImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class PatientConsentHelper
{
    private Log log = null;

    public PatientConsentHelper()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected AdapterPIPImpl getAdapterPIP()
    {
        return new AdapterPIPImpl();
    }

    public PatientPreferencesType retrievePatientConsentbyPatientId(String patientId, String assigningAuthorityId)
    {
        PatientPreferencesType response = null;
        try
        {
            log.debug("Retrieving patient preferences by patient id. Patient id (" + patientId + "), assigning authority id (" + assigningAuthorityId + ")");
            RetrievePtConsentByPtIdRequestType retrieveRequest = new RetrievePtConsentByPtIdRequestType();

            retrieveRequest.setPatientId(patientId);
            retrieveRequest.setAssigningAuthority(assigningAuthorityId);

            RetrievePtConsentByPtIdResponseType retrieveResponse = getAdapterPIP().retrievePtConsentByPtId(retrieveRequest);
            if(retrieveResponse != null)
            {
                response = retrieveResponse.getPatientPreferences();
            }
        }
        catch(Throwable t)
        {
            log.error("Error retrieving patient preferences. Patient id (" + patientId + "), assigning authority id (" + assigningAuthorityId + ") Error: " + t.getMessage(), t);
        }
        return response;
    }

    public PatientPreferencesType retrievePatientConsentbyDocumentId(String homeCommunityId, String repositoryId, String documentId)
    {
        PatientPreferencesType response = null;
        try
        {
            log.debug("Retrieving patient preferences by document id. Home community id (" + homeCommunityId + "), repository id (" + repositoryId + "), document id (" + documentId + ")");
            RetrievePtConsentByPtDocIdRequestType retrieveRequest = new RetrievePtConsentByPtDocIdRequestType();

            retrieveRequest.setHomeCommunityId(homeCommunityId);
            retrieveRequest.setRepositoryId(repositoryId);
            retrieveRequest.setDocumentId(documentId);

            RetrievePtConsentByPtDocIdResponseType retrieveResponse = getAdapterPIP().retrievePtConsentByPtDocId(retrieveRequest);
            if(retrieveResponse != null)
            {
                response = retrieveResponse.getPatientPreferences();
                log.debug("Retrieved patient consent document.");
            }
            else
            {
                log.debug("Patient consent document was null.");
            }
        }
        catch(Throwable t)
        {
            log.error("Error retrieving patient preferences. Home community id (" + homeCommunityId + "), repository id (" + repositoryId + "), document id (" + documentId + ") Error: " + t.getMessage(), t);
        }
        return response;
    }

    /**
     * This method will extract the document type from Patient Preferences and compare with the one in document
     * response, if the document type present and matches then returns true otherwise it is considered as false (Deny).
     * @param documentType
     * @param ptPreferences
     * @return boolean
     */
    public boolean documentSharingAllowed(String documentType, PatientPreferencesType ptPreferences)
    {
        log.debug("Begin extract permit value from patient preferences - document type code tested: " + documentType);
        // Default to false in case something goes wrong.
        boolean allowDocumentSharing = false;
        FineGrainedPolicyCriteriaType findGrainedPolicy = null;
        if(documentType == null || documentType.equals(""))
        {
            log.error("Invalid documentType");
            return allowDocumentSharing;
        }
        if(ptPreferences == null)
        {
            log.error("Patient preferences was null");
            return allowDocumentSharing;
        }

        findGrainedPolicy = ptPreferences.getFineGrainedPolicyCriteria();
        if(findGrainedPolicy == null ||
                findGrainedPolicy.getFineGrainedPolicyCriterion() == null ||
                findGrainedPolicy.getFineGrainedPolicyCriterion().isEmpty())
        {
            // No fine grained policy info - use simple opt-in/opt-out
            allowDocumentSharing = ptPreferences.isOptIn();
            log.debug("Simple opt-in/opt-out value from patient preferences: " + allowDocumentSharing);
        }
        else
        {
            // No global opt-in/opt-out. Look at fine grained policy for opt-in limited
            log.debug("Patient preferences has " + findGrainedPolicy.getFineGrainedPolicyCriterion().size() + " fine grained policy criterion.");

            String criterionDocumentTypeCode = null;
            for(FineGrainedPolicyCriterionType eachFineGrainedPolicyCriterion : findGrainedPolicy.getFineGrainedPolicyCriterion())
            {
                if(eachFineGrainedPolicyCriterion != null)
                {
                    if(eachFineGrainedPolicyCriterion.getDocumentTypeCode() != null)
                    {
                        criterionDocumentTypeCode = eachFineGrainedPolicyCriterion.getDocumentTypeCode().getCode();
                        log.debug("Looking at criterion for document type: " + criterionDocumentTypeCode);
                        if(criterionDocumentTypeCode != null &&
                                !criterionDocumentTypeCode.equals("") &&
                                criterionDocumentTypeCode.equals(documentType))
                        {
                            allowDocumentSharing = eachFineGrainedPolicyCriterion.isPermit();
                            // The algorithm is to use the first found - leave after the first match.
                            break;
                        }
                    }
                    else if(isDefaultFineGrainedPolicyCriterion(eachFineGrainedPolicyCriterion))
                    {
                        allowDocumentSharing = eachFineGrainedPolicyCriterion.isPermit();
                        break;
                    }
                }
            }
            log.debug("End extract permit value from fine grained policy criterian");
        }
        log.debug("Permit sharing flag for document filter: " + allowDocumentSharing);
        return allowDocumentSharing;
    }


    protected boolean isDefaultFineGrainedPolicyCriterion(FineGrainedPolicyCriterionType criterion)
    {
        log.debug("Begin isDefaultFineGrainedPolicyCriterion");
        boolean defaultCriterion = false;
        if(criterion != null)
        {
            // Add other values when additional options are considered.
            defaultCriterion = ((criterion.getDocumentTypeCode() == null));
        }
        log.debug("End isDefaultFineGrainedPolicyCriterion - value: " + defaultCriterion);
        return defaultCriterion;
    }
}
