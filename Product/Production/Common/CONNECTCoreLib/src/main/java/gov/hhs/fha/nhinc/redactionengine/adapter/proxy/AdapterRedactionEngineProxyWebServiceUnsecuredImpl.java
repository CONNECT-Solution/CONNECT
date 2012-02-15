/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package gov.hhs.fha.nhinc.redactionengine.adapter.proxy;

import gov.hhs.fha.nhinc.adaptercomponentredaction.AdapterComponentRedactionEnginePortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocQueryResultsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocQueryResultsResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocRetrieveResultsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocRetrieveResultsResponseType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
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
public class AdapterRedactionEngineProxyWebServiceUnsecuredImpl implements AdapterRedactionEngineProxy {
    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:adaptercomponentredaction";
    private static final String SERVICE_LOCAL_PART = "AdapterComponentRedactionEngineService";
    private static final String PORT_LOCAL_PART = "AdapterComponentRedactionEnginePortTypeBindingPort";
    private static final String WSDL_FILE = "AdapterComponentRedactionEngine.wsdl";
    private static final String WS_ADDRESSING_ACTION_QUERY = "urn:gov:hhs:fha:nhinc:adaptercomponentredaction:FilterDocQueryResultsRequest";
    private static final String WS_ADDRESSING_ACTION_RETRIEVE = "urn:gov:hhs:fha:nhinc:adaptercomponentredaction:FilterDocRetrieveResultsRequest";
    private WebServiceProxyHelper oProxyHelper = null;

    public AdapterRedactionEngineProxyWebServiceUnsecuredImpl() {
        log = createLogger();
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    /**
     * This method retrieves and initializes the port.
     * 
     * @param url The URL for the web service.
     * @return The port object for the web service.
     */
    protected AdapterComponentRedactionEnginePortType getPort(String url, String wsAddressingAction,
            AssertionType assertion) {
        AdapterComponentRedactionEnginePortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART),
                    AdapterComponentRedactionEnginePortType.class);
            oProxyHelper
                    .initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, wsAddressingAction, assertion);
        } else {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }

    /**
     * Retrieve the service class for this web service.
     * 
     * @return The service class for this web service.
     */
    protected Service getService() {
        if (cachedService == null) {
            try {
                cachedService = oProxyHelper.createService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);
            } catch (Throwable t) {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }

    public AdhocQueryResponse filterAdhocQueryResults(AdhocQueryRequest adhocQueryRequest,
            AdhocQueryResponse adhocQueryResponse, AssertionType assertion) {
        log.debug("Begin filterAdhocQueryResults");
        AdhocQueryResponse response = null;

        try {
            String url = oProxyHelper.getEndPointFromConnectionManagerByAdapterAPILevel(NhincConstants.REDACTION_ENGINE_SERVICE_NAME, ADAPTER_API_LEVEL.LEVEL_a0);
            AdapterComponentRedactionEnginePortType port = getPort(url, WS_ADDRESSING_ACTION_QUERY, assertion);

            if (adhocQueryRequest == null) {
                log.error("adhocQueryRequest was null");
            } else if (adhocQueryResponse == null) {
                log.error("adhocQueryResponse was null");
            } else if (port == null) {
                log.error("port was null");
            } else {
                FilterDocQueryResultsRequestType filterDocQueryResultsRequest = new FilterDocQueryResultsRequestType();
                filterDocQueryResultsRequest.setAdhocQueryRequest(adhocQueryRequest);
                filterDocQueryResultsRequest.setAdhocQueryResponse(adhocQueryResponse);

                FilterDocQueryResultsResponseType filteredResponse = (FilterDocQueryResultsResponseType) oProxyHelper
                        .invokePort(port, AdapterComponentRedactionEnginePortType.class, "filterDocQueryResults",
                                filterDocQueryResultsRequest);
                response = filteredResponse.getAdhocQueryResponse();
            }
        } catch (Exception ex) {
            log.error("Error calling filterDocQueryResults: " + ex.getMessage(), ex);
        }

        log.debug("End respondingGatewayCrossGatewayQuery");
        return response;
    }

    public RetrieveDocumentSetResponseType filterRetrieveDocumentSetResults(
            RetrieveDocumentSetRequestType retrieveDocumentSetRequest,
            RetrieveDocumentSetResponseType retrieveDocumentSetResponse, AssertionType assertion) {
        log.debug("Begin filterAdhocQueryResults");
        RetrieveDocumentSetResponseType response = null;

        try {
            String url = oProxyHelper.getEndPointFromConnectionManagerByAdapterAPILevel(NhincConstants.REDACTION_ENGINE_SERVICE_NAME, ADAPTER_API_LEVEL.LEVEL_a0);         
            AdapterComponentRedactionEnginePortType port = getPort(url, WS_ADDRESSING_ACTION_RETRIEVE, assertion);

            if (retrieveDocumentSetRequest == null) {
                log.error("retrieveDocumentSetRequest was null");
            } else if (retrieveDocumentSetResponse == null) {
                log.error("retrieveDocumentSetResponse was null");
            } else if (port == null) {
                log.error("port was null");
            } else {
                FilterDocRetrieveResultsRequestType filterDocRetrieveResultsRequest = new FilterDocRetrieveResultsRequestType();
                filterDocRetrieveResultsRequest.setRetrieveDocumentSetRequest(retrieveDocumentSetRequest);
                filterDocRetrieveResultsRequest.setRetrieveDocumentSetResponse(retrieveDocumentSetResponse);

                FilterDocRetrieveResultsResponseType filteredResponse = (FilterDocRetrieveResultsResponseType) oProxyHelper
                        .invokePort(port, AdapterComponentRedactionEnginePortType.class, "filterDocRetrieveResults",
                                filterDocRetrieveResultsRequest);
                response = filteredResponse.getRetrieveDocumentSetResponse();
            }
        } catch (Exception ex) {
            log.error("Error calling filterDocRetrieveResults: " + ex.getMessage(), ex);
        }

        log.debug("End respondingGatewayCrossGatewayQuery");
        return response;

    }

}
