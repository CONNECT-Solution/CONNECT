package gov.hhs.fha.nhinc.redaction.proxy;

import gov.hhs.fha.nhinc.redaction.RedactionEngine;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

/**
 * Simple redirection to the Java implementation of the redaction engine.
 * 
 * @author Neil Webb
 */
public class RedactionEngineProxyJavaImpl implements RedactionEngineProxy
{
    protected RedactionEngine getRedactionEngine()
    {
        return new RedactionEngine();
    }

    public AdhocQueryResponse filterAdhocQueryResults(AdhocQueryRequest adhocQueryRequest, AdhocQueryResponse adhocQueryResponse)
    {
        return getRedactionEngine().filterAdhocQueryResults(adhocQueryRequest, adhocQueryResponse);
    }

    public RetrieveDocumentSetResponseType filterRetrieveDocumentSetResults(String homeCommunityId, RetrieveDocumentSetRequestType retrieveDocumentSetRequest, RetrieveDocumentSetResponseType retrieveDocumentSetResponse)
    {
        return getRedactionEngine().filterRetrieveDocumentSetResults(homeCommunityId, retrieveDocumentSetRequest, retrieveDocumentSetResponse);
    }

}
