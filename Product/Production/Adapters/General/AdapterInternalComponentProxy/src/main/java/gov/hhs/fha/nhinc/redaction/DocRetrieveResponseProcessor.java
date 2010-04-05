package gov.hhs.fha.nhinc.redaction;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

/**
 *
 * @author Neil Webb
 */
public class DocRetrieveResponseProcessor
{
    private String documentId;
    private String homeCommunityId;
    private String repositoryId;

    public RetrieveDocumentSetResponseType filterRetrieveDocumentSetReults(RetrieveDocumentSetRequestType retrieveRequest, RetrieveDocumentSetResponseType retrieveResponse)
    {
        RetrieveDocumentSetResponseType response = null;

        return response;
    }

    protected void extractIdentifiers(RetrieveDocumentSetRequestType retrieveRequest)
    {

    }

    protected RetrieveDocumentSetResponseType filterResults(RetrieveDocumentSetResponseType retrieveResponse, PatientPreferencesType patientPreferences)
    {
        RetrieveDocumentSetResponseType response = null;

        return response;
    }

    protected String getDocumentId()
    {
        return documentId;
    }

    protected String getHomeCommunityId()
    {
        return homeCommunityId;
    }

    protected String getRepositoryId()
    {
        return repositoryId;
    }
}
