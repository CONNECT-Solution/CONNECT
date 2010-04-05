package gov.hhs.fha.nhinc.redaction;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

/**
 *
 * @author Neil Webb
 */
public class DocQueryResponseProcessor
{
    private String patientId;
    private String assigningAuthorityId;

    public AdhocQueryResponse filterAdhocQueryResults(AdhocQueryRequest adhocQueryRequest, AdhocQueryResponse adhocQueryResponse)
    {
        AdhocQueryResponse response = null;

        return response;
    }

    protected void extractIdentifiers(AdhocQueryRequest adhocQueryRequest)
    {

    }

    protected AdhocQueryResponse filterResults(AdhocQueryResponse adhocQueryResponse, PatientPreferencesType patientPreferences)
    {
        AdhocQueryResponse response = null;

        return response;
    }

    protected String getPatientId()
    {
        return patientId;
    }

    protected String getAssigningAuthorityId()
    {
        return assigningAuthorityId;
    }
    
}
