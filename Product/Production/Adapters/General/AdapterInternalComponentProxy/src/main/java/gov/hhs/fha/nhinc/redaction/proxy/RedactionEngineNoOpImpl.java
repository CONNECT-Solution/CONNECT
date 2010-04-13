package gov.hhs.fha.nhinc.redaction.proxy;

import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

/**
 * Return the response object that was provided.
 * 
 * @author Neil Webb
 */
public class RedactionEngineNoOpImpl implements RedactionEngineProxy
{

    public AdhocQueryResponse filterAdhocQueryResults(AdhocQueryRequest adhocQueryRequest, AdhocQueryResponse adhocQueryResponse)
    {
        return adhocQueryResponse;
    }

    public RetrieveDocumentSetResponseType filterRetrieveDocumentSetResults(RetrieveDocumentSetRequestType retrieveDocumentSetRequest, RetrieveDocumentSetResponseType retrieveDocumentSetResponse)
    {
        return retrieveDocumentSetResponse;
    }

}
