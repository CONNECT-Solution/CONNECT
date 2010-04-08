package gov.hhs.fha.nhinc.adaptercomponentredaction;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocQueryResultsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocQueryResultsResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocRetrieveResultsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocRetrieveResultsResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.redaction.RedactionEngine;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

/**
 *
 * @author Neil Webb
 */
public class AdapterComponentRedactionEngineImpl
{
    private Log log = null;

    public AdapterComponentRedactionEngineImpl()
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

    public FilterDocQueryResultsResponseType filterDocQueryResults(FilterDocQueryResultsRequestType filterDocQueryResultsRequest)
    {
        log.debug("Begin filterDocQueryResults");
        FilterDocQueryResultsResponseType response = null;
        RedactionEngine redactionEngine = getRedactionEngine();

        if(filterDocQueryResultsRequest == null)
        {
            log.warn("FilterDocQueryResultsRequestType was null");
        }
        else if(redactionEngine == null)
        {
            log.warn("RedactionEngine was null");
        }
        else
        {
            AdhocQueryResponse adhocQueryResponse = redactionEngine.filterAdhocQueryResults(filterDocQueryResultsRequest.getAdhocQueryRequest(), filterDocQueryResultsRequest.getAdhocQueryResponse());
            response = new FilterDocQueryResultsResponseType();
            response.setAdhocQueryResponse(adhocQueryResponse);
        }
        log.debug("end filterDocQueryResults");
        return response;
    }

    public FilterDocRetrieveResultsResponseType filterDocRetrieveResults(FilterDocRetrieveResultsRequestType filterDocRetrieveResultsRequest)
    {
        FilterDocRetrieveResultsResponseType response = null;
        RedactionEngine redactionEngine = getRedactionEngine();
        if(filterDocRetrieveResultsRequest == null)
        {
            log.warn("FilterDocRetrieveResultsRequestType was null");
        }
        else if (redactionEngine == null)
        {
            log.warn("RedactionEngine was null");
        }
        else
        {
            String homeCommunityId = null;
            if(filterDocRetrieveResultsRequest.getHomeCommunity() != null)
            {
                homeCommunityId = filterDocRetrieveResultsRequest.getHomeCommunity().getHomeCommunityId();
                if(log.isDebugEnabled())
                {
                    log.debug("Home community id: " + homeCommunityId);
                }
            }
            else
            {
                log.warn("Home community was null");
            }
            RetrieveDocumentSetResponseType retrieveDocSetResonse = redactionEngine.filterRetrieveDocumentSetResults(homeCommunityId, filterDocRetrieveResultsRequest.getRetrieveDocumentSetRequest(), filterDocRetrieveResultsRequest.getRetrieveDocumentSetResponse());
            response = new FilterDocRetrieveResultsResponseType();
            response.setRetrieveDocumentSetResponse(retrieveDocSetResonse);
        }
        return response;
    }

}
