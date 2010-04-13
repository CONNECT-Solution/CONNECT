package gov.hhs.fha.nhinc.redaction;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.FineGrainedPolicyCriteriaType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FineGrainedPolicyCriterionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdResponseType;
import gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPImpl;
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
    public boolean extractDocTypeFromPatPref(String documentType, PatientPreferencesType ptPreferences)
    {
        log.debug("Begin extract DocumentType value from fine grained policy criterian");
        boolean bPtDocTypeCd = false;
        FineGrainedPolicyCriteriaType findGrainedPolicy = null;
        if(documentType == null || documentType.equals(""))
        {
            log.error("Invalid documentType");
            return bPtDocTypeCd;
        }
        findGrainedPolicy = ptPreferences.getFineGrainedPolicyCriteria();
        if(findGrainedPolicy == null ||
                findGrainedPolicy.getFineGrainedPolicyCriterion() == null ||
                findGrainedPolicy.getFineGrainedPolicyCriterion().size() <= 0)
        {
            log.debug("No Fine Grained Policy Criteria. Looking at global opt-in/opt-out");
            bPtDocTypeCd = ptPreferences.isOptIn();
            return bPtDocTypeCd;
        }
        else
        {
            String sPtDocTypeCd = null;
            for(FineGrainedPolicyCriterionType eachFineGrainedPolicyCriterion : findGrainedPolicy.getFineGrainedPolicyCriterion())
            {
                if(null != eachFineGrainedPolicyCriterion &&
                        eachFineGrainedPolicyCriterion.getDocumentTypeCode()!=null)
                {
                    sPtDocTypeCd = eachFineGrainedPolicyCriterion.getDocumentTypeCode().getCode();
                    if(sPtDocTypeCd != null &&
                            !sPtDocTypeCd.equals("") &&
                            sPtDocTypeCd.equals(documentType))
                    {
                        bPtDocTypeCd = true;
                        break;
                    }
                }
            }
            log.debug("End extract DocumentType value from fine grained policy criterian");
        }
        return bPtDocTypeCd;
    }

}
