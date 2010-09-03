/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.redactionengine.adapter.proxy;

import gov.hhs.fha.nhinc.adaptercomponentredaction.AdapterComponentRedactionEnginePortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocQueryResultsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocQueryResultsResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocRetrieveResultsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocRetrieveResultsResponseType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class AdapterRedactionEngineProxyWebServiceUnsecuredImpl implements AdapterRedactionEngineProxy
{
    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:adaptercomponentredaction";
    private static final String SERVICE_LOCAL_PART = "AdapterComponentRedactionEngineService";
    private static final String PORT_LOCAL_PART = "AdapterComponentRedactionEnginePortTypeBindingPort";
    private static final String WSDL_FILE = "AdapterComponentRedactionEngine.wsdl";
    private static final String WS_ADDRESSING_ACTION_QUERY = "urn:gov:hhs:fha:nhinc:adaptercomponentredaction:FilterDocQueryResultsRequest";
    private static final String WS_ADDRESSING_ACTION_RETRIEVE = "urn:gov:hhs:fha:nhinc:adaptercomponentredaction:FilterDocRetrieveResultsRequest";
    private WebServiceProxyHelper oProxyHelper = null;

    public AdapterRedactionEngineProxyWebServiceUnsecuredImpl()
    {
        log = createLogger();
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper()
    {
        return new WebServiceProxyHelper();
    }

    /**
     * This method retrieves and initializes the port.
     *
     * @param url The URL for the web service.
     * @return The port object for the web service.
     */
    protected AdapterComponentRedactionEnginePortType getPort(String url, String wsAddressingAction, AssertionType assertion)
    {
        AdapterComponentRedactionEnginePortType port = null;
        Service service = getService();
        if (service != null)
        {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), AdapterComponentRedactionEnginePortType.class);
            oProxyHelper.initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, wsAddressingAction, assertion);
        }
        else
        {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }

    /**
     * Retrieve the service class for this web service.
     *
     * @return The service class for this web service.
     */
    protected Service getService()
    {
        if (cachedService == null)
        {
            try
            {
                cachedService = oProxyHelper.createService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);
            }
            catch (Throwable t)
            {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }
 
    public AdhocQueryResponse filterAdhocQueryResults(AdhocQueryRequest adhocQueryRequest, AdhocQueryResponse adhocQueryResponse, AssertionType assertion)
    {
        log.debug("Begin filterAdhocQueryResults");
        AdhocQueryResponse response = null;

        try
        {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.REDACTION_ENGINE_SERVICE_NAME);
            AdapterComponentRedactionEnginePortType port = getPort(url, WS_ADDRESSING_ACTION_QUERY, assertion);

            if(adhocQueryRequest == null)
            {
                log.error("adhocQueryRequest was null");
            }
            else if(adhocQueryResponse == null)
            {
                log.error("adhocQueryResponse was null");
            }
            else if(port == null)
            {
                log.error("port was null");
            }
            else
            {
                FilterDocQueryResultsRequestType filterDocQueryResultsRequest = new FilterDocQueryResultsRequestType();
                filterDocQueryResultsRequest.setAdhocQueryRequest(adhocQueryRequest);
                filterDocQueryResultsRequest.setAdhocQueryResponse(adhocQueryResponse);

                FilterDocQueryResultsResponseType filteredResponse = (FilterDocQueryResultsResponseType)oProxyHelper.invokePort(port, AdapterComponentRedactionEnginePortType.class, "filterDocQueryResults",filterDocQueryResultsRequest);
                response = filteredResponse.getAdhocQueryResponse();
            }
        }
        catch (Exception ex)
        {
            log.error("Error calling filterDocQueryResults: " + ex.getMessage(), ex);
        }

        log.debug("End respondingGatewayCrossGatewayQuery");
        return response;
    }

    public RetrieveDocumentSetResponseType filterRetrieveDocumentSetResults(RetrieveDocumentSetRequestType retrieveDocumentSetRequest, RetrieveDocumentSetResponseType retrieveDocumentSetResponse, AssertionType assertion)
    {
        log.debug("Begin filterAdhocQueryResults");
        RetrieveDocumentSetResponseType response = null;

        try
        {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.REDACTION_ENGINE_SERVICE_NAME);
            AdapterComponentRedactionEnginePortType port = getPort(url, WS_ADDRESSING_ACTION_RETRIEVE, assertion);

            if(retrieveDocumentSetRequest == null)
            {
                log.error("retrieveDocumentSetRequest was null");
            }
            else if(retrieveDocumentSetResponse == null)
            {
                log.error("retrieveDocumentSetResponse was null");
            }
            else if(port == null)
            {
                log.error("port was null");
            }
            else
            {
                FilterDocRetrieveResultsRequestType filterDocRetrieveResultsRequest = new FilterDocRetrieveResultsRequestType();
                filterDocRetrieveResultsRequest.setRetrieveDocumentSetRequest(retrieveDocumentSetRequest);
                filterDocRetrieveResultsRequest.setRetrieveDocumentSetResponse(retrieveDocumentSetResponse);

                FilterDocRetrieveResultsResponseType filteredResponse = (FilterDocRetrieveResultsResponseType)oProxyHelper.invokePort(port, AdapterComponentRedactionEnginePortType.class, "filterDocRetrieveResults",filterDocRetrieveResultsRequest);
                response = filteredResponse.getRetrieveDocumentSetResponse();
            }
        }
        catch (Exception ex)
        {
            log.error("Error calling filterDocRetrieveResults: " + ex.getMessage(), ex);
        }

        log.debug("End respondingGatewayCrossGatewayQuery");
        return response;

        
    }

}
