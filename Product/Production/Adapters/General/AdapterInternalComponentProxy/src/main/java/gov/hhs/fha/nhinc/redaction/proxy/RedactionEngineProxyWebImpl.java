package gov.hhs.fha.nhinc.redaction.proxy;

import gov.hhs.fha.nhinc.adaptercomponentredaction.AdapterComponentRedactionEnginePortType;
import gov.hhs.fha.nhinc.adaptercomponentredaction.AdapterComponentRedactionEngineService;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocQueryResultsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocQueryResultsResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocRetrieveResultsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocRetrieveResultsResponseType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class RedactionEngineProxyWebImpl implements RedactionEngineProxy
{
    private static String ADAPTER_REDACTION_ENGINE_SERVICE_NAME = "adapterredactionengine";
    private Log log = null;
    private static AdapterComponentRedactionEngineService redactionService = null;

    public RedactionEngineProxyWebImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    protected String invokeConnectionManager(String serviceName) throws ConnectionManagerException
    {
        return ConnectionManagerCache.getLocalEndpointURLByServiceName(serviceName);
    }

    protected String getEndpointURL()
    {
        String endpointURL = null;
        try
        {
            endpointURL = invokeConnectionManager(ADAPTER_REDACTION_ENGINE_SERVICE_NAME);
            log.debug("Retrieved endpoint URL for service " + ADAPTER_REDACTION_ENGINE_SERVICE_NAME + ": " + endpointURL);
        }
        catch (ConnectionManagerException ex)
        {
            log.error("Error getting url for " + ADAPTER_REDACTION_ENGINE_SERVICE_NAME + " from the connection manager. Error: " + ex.getMessage(), ex);
        }

        return endpointURL;
    }

    protected AdapterComponentRedactionEngineService getAdapterComponentRedactionEngineService()
    {
        if(redactionService == null)
        {
            redactionService = new AdapterComponentRedactionEngineService();
        }
        return redactionService;
    }

    protected AdapterComponentRedactionEnginePortType getAdapterComponentRedactionEnginePortType()
    {
        AdapterComponentRedactionEnginePortType port = null;
        
        String endpointURL = getEndpointURL();

        if((endpointURL != null) && (!endpointURL.isEmpty()))
        {
            AdapterComponentRedactionEngineService service = getAdapterComponentRedactionEngineService();
            if(service != null)
            {
                port = service.getAdapterComponentRedactionEnginePortTypeBindingPort();
                ((javax.xml.ws.BindingProvider)port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointURL);
            }
            else
            {
                log.warn("AdapterComponentRedactionEngineService was null");
            }
        }
        else
        {
            log.warn("Endpoint url was missing.");
        }
        
        return port;
    }

    public AdhocQueryResponse filterAdhocQueryResults(AdhocQueryRequest adhocQueryRequest, AdhocQueryResponse adhocQueryResponse)
    {
        AdhocQueryResponse response = null;

        try
        {
            AdapterComponentRedactionEnginePortType port = getAdapterComponentRedactionEnginePortType();

            if(adhocQueryRequest == null)
            {
                log.warn("AdhocQueryRequest was null.");
            }
            else if(adhocQueryResponse == null)
            {
                log.warn("AdhocQueryResponse was null.");
            }
            else if(port == null)
            {
                log.warn("AdapterComponentRedactionEnginePortType was null.");
            }
            else
            {
                FilterDocQueryResultsRequestType filterDocQueryResultsRequest = new FilterDocQueryResultsRequestType();
                filterDocQueryResultsRequest.setAdhocQueryRequest(adhocQueryRequest);
                filterDocQueryResultsRequest.setAdhocQueryResponse(adhocQueryResponse);

                FilterDocQueryResultsResponseType result = port.filterDocQueryResults(filterDocQueryResultsRequest);
                if(result != null)
                {
                    response = result.getAdhocQueryResponse();
                }
                else
                {
                    log.warn("FilterDocQueryResultsResponseType was null.");
                }
            }
        }
        catch (Exception ex)
        {
            log.error("Error calling filterDocQueryResults: " + ex.getMessage(), ex);
        }

        return response;
    }

    public RetrieveDocumentSetResponseType filterRetrieveDocumentSetResults(String homeCommunityId, RetrieveDocumentSetRequestType retrieveDocumentSetRequest, RetrieveDocumentSetResponseType retrieveDocumentSetResponse)
    {
        RetrieveDocumentSetResponseType response = null;

        try
        {
            AdapterComponentRedactionEnginePortType port = getAdapterComponentRedactionEnginePortType();

            if(retrieveDocumentSetRequest == null)
            {
                log.warn("RetrieveDocumentSetRequestType was null.");
            }
            else if(retrieveDocumentSetResponse == null)
            {
                log.warn("RetrieveDocumentSetResponseType was null.");
            }
            else if(port == null)
            {
                log.warn("AdapterComponentRedactionEnginePortType was null.");
            }
            else
            {
                FilterDocRetrieveResultsRequestType filterDocRetrieveResultsRequest = new FilterDocRetrieveResultsRequestType();
                HomeCommunityType homeCommunity = new HomeCommunityType();
                homeCommunity.setHomeCommunityId(homeCommunityId);
                filterDocRetrieveResultsRequest.setHomeCommunity(homeCommunity);
                filterDocRetrieveResultsRequest.setRetrieveDocumentSetRequest(retrieveDocumentSetRequest);
                filterDocRetrieveResultsRequest.setRetrieveDocumentSetResponse(retrieveDocumentSetResponse);

                FilterDocRetrieveResultsResponseType result = port.filterDocRetrieveResults(filterDocRetrieveResultsRequest);
                if(result != null)
                {
                    response = result.getRetrieveDocumentSetResponse();
                }
                else
                {
                    log.warn("FilterDocRetrieveResultsResponseType was null.");
                }
            }
        }
        catch (Exception ex)
        {
            log.error("Error calling filterDocRetrieveResults: " + ex.getMessage(), ex);
        }

        return response;
    }

}
