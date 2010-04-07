package gov.hhs.fha.nhinc.redaction.proxy;

import gov.hhs.fha.nhinc.redaction.RedactionEngine;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Simple redirection to the Java implementation of the redaction engine.
 * 
 * @author Neil Webb
 */
public class RedactionEngineProxyJavaImpl implements RedactionEngineProxy
{
    private Log log = null;

    public RedactionEngineProxyJavaImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    protected RedactionEngine getRedactionEngine()
    {
        return new RedactionEngine();
    }

    public AdhocQueryResponse filterAdhocQueryResults(AdhocQueryRequest adhocQueryRequest, AdhocQueryResponse adhocQueryResponse)
    {
        log.debug("Begin filterAdhocQueryResults");
        AdhocQueryResponse response = null;
        RedactionEngine redactionEngine = getRedactionEngine();
        if(redactionEngine != null)
        {
            response = redactionEngine.filterAdhocQueryResults(adhocQueryRequest, adhocQueryResponse);
        }
        else
        {
            log.warn("RedactionEngine was null");
        }
        log.debug("End filterAdhocQueryResults");
        return response;
    }

    public RetrieveDocumentSetResponseType filterRetrieveDocumentSetResults(String homeCommunityId, RetrieveDocumentSetRequestType retrieveDocumentSetRequest, RetrieveDocumentSetResponseType retrieveDocumentSetResponse)
    {
        log.debug("Begin filterRetrieveDocumentSetResults");
        RetrieveDocumentSetResponseType response = null;
        RedactionEngine redactionEngine = getRedactionEngine();
        if(redactionEngine != null)
        {
            response = redactionEngine.filterRetrieveDocumentSetResults(homeCommunityId, retrieveDocumentSetRequest, retrieveDocumentSetResponse);
        }
        else
        {
            log.warn("RedactionEngine was null");
        }
        log.debug("Begin filterRetrieveDocumentSetResults");
        return response;
    }

}
