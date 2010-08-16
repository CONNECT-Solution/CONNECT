package gov.hhs.fha.nhinc.redactionengine.adapter.proxy;

import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Return the response object that was provided.
 * 
 * @author Neil Webb
 */
public class AdapterRedactionEngineProxyNoOpImpl implements AdapterRedactionEngineProxy
{
    private static Log log = LogFactory.getLog(AdapterRedactionEngineProxyNoOpImpl.class);

    public AdhocQueryResponse filterAdhocQueryResults(AdhocQueryRequest adhocQueryRequest, AdhocQueryResponse adhocQueryResponse)
    {
        log.debug("Using NoOp Implementation for Adapter Redaction Engine Service");
        return adhocQueryResponse;
    }

    public RetrieveDocumentSetResponseType filterRetrieveDocumentSetResults(RetrieveDocumentSetRequestType retrieveDocumentSetRequest, RetrieveDocumentSetResponseType retrieveDocumentSetResponse)
    {
        log.debug("Using NoOp Implementation for Adapter Redaction Engine Service");
        return retrieveDocumentSetResponse;
    }

}
