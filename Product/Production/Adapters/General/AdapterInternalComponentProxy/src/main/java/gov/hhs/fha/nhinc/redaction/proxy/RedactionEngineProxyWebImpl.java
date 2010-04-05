package gov.hhs.fha.nhinc.redaction.proxy;

import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

/**
 *
 * @author Neil Webb
 */
public class RedactionEngineProxyWebImpl implements RedactionEngineProxy
{

    public AdhocQueryResponse filterAdhocQueryResults(AdhocQueryRequest adhocQueryRequest, AdhocQueryResponse adhocQueryResponse)
    {
        AdhocQueryResponse response = null;

        return response;
    }

    public RetrieveDocumentSetResponseType filterRetrieveDocumentSetResults(String homeCommunityId, RetrieveDocumentSetRequestType retrieveDocumentSetRequest, RetrieveDocumentSetResponseType retrieveDocumentSetResponse)
    {
        RetrieveDocumentSetResponseType response = null;

        return response;
    }

}
