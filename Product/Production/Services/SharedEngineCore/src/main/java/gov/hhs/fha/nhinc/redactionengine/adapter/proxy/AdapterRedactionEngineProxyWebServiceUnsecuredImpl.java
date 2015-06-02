/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
import gov.hhs.fha.nhinc.messaging.client.CONNECTCXFClientFactory;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.redactionengine.adapter.proxy.service.AdapterRedactionEngineQueryServicePortDescriptor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

import org.apache.log4j.Logger;

/**
 *
 * @author Neil Webb
 */
public class AdapterRedactionEngineProxyWebServiceUnsecuredImpl implements AdapterRedactionEngineProxy {
    private static final Logger LOG = Logger.getLogger(AdapterRedactionEngineProxyWebServiceUnsecuredImpl.class);
    private WebServiceProxyHelper oProxyHelper = null;

    public AdapterRedactionEngineProxyWebServiceUnsecuredImpl() {
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    protected CONNECTClient<AdapterComponentRedactionEnginePortType> getCONNECTClientUnsecured(
            ServicePortDescriptor<AdapterComponentRedactionEnginePortType> portDescriptor, String url,
            AssertionType assertion) {

        return CONNECTCXFClientFactory.getInstance().getCONNECTClientUnsecured(portDescriptor, url, assertion);
    }

    public AdhocQueryResponse filterAdhocQueryResults(AdhocQueryRequest adhocQueryRequest,
            AdhocQueryResponse adhocQueryResponse, AssertionType assertion) {
        LOG.debug("Begin filterAdhocQueryResults");
        AdhocQueryResponse response = null;

        try {
            String serviceName = NhincConstants.REDACTION_ENGINE_SERVICE_NAME;
            String url = oProxyHelper.getAdapterEndPointFromConnectionManager(serviceName);
            if (NullChecker.isNotNullish(url)) {
                if (adhocQueryRequest == null) {
                    LOG.error("adhocQueryRequest was null");
                } else if (adhocQueryResponse == null) {
                    LOG.error("adhocQueryResponse was null");
                } else {
                    FilterDocQueryResultsRequestType filterDocQueryResultsRequest = new FilterDocQueryResultsRequestType();
                    filterDocQueryResultsRequest.setAdhocQueryRequest(adhocQueryRequest);
                    filterDocQueryResultsRequest.setAdhocQueryResponse(adhocQueryResponse);

                    ServicePortDescriptor<AdapterComponentRedactionEnginePortType> portDescriptor = new AdapterRedactionEngineQueryServicePortDescriptor();

                    CONNECTClient<AdapterComponentRedactionEnginePortType> client = getCONNECTClientUnsecured(portDescriptor, url, assertion);

                    FilterDocQueryResultsResponseType filteredResponse = (FilterDocQueryResultsResponseType) client
                            .invokePort(AdapterComponentRedactionEnginePortType.class, "filterDocQueryResults",
                                    filterDocQueryResultsRequest);
                    response = filteredResponse.getAdhocQueryResponse();
                }
            } else {
                LOG.error("Failed to call the web service (" + serviceName + ").  The URL is null.");
            }
        } catch (Exception ex) {
            LOG.error("Error calling filterDocQueryResults: " + ex.getMessage(), ex);
        }

        LOG.debug("End respondingGatewayCrossGatewayQuery");
        return response;
    }

    public RetrieveDocumentSetResponseType filterRetrieveDocumentSetResults(
            RetrieveDocumentSetRequestType retrieveDocumentSetRequest,
            RetrieveDocumentSetResponseType retrieveDocumentSetResponse, AssertionType assertion) {
        LOG.debug("Begin filterAdhocQueryResults");
        RetrieveDocumentSetResponseType response = null;

        try {
            String serviceName = NhincConstants.REDACTION_ENGINE_SERVICE_NAME;
            String url = oProxyHelper.getAdapterEndPointFromConnectionManager(serviceName);
            if (NullChecker.isNotNullish(url)) {
                if (retrieveDocumentSetRequest == null) {
                    LOG.error("retrieveDocumentSetRequest was null");
                } else if (retrieveDocumentSetResponse == null) {
                    LOG.error("retrieveDocumentSetResponse was null");
                } else {
                    FilterDocRetrieveResultsRequestType filterDocRetrieveResultsRequest = new FilterDocRetrieveResultsRequestType();
                    filterDocRetrieveResultsRequest.setRetrieveDocumentSetRequest(retrieveDocumentSetRequest);
                    filterDocRetrieveResultsRequest.setRetrieveDocumentSetResponse(retrieveDocumentSetResponse);

                    ServicePortDescriptor<AdapterComponentRedactionEnginePortType> portDescriptor = new AdapterRedactionEngineQueryServicePortDescriptor();

                    CONNECTClient<AdapterComponentRedactionEnginePortType> client = getCONNECTClientUnsecured(portDescriptor, url, assertion);

                    FilterDocRetrieveResultsResponseType filteredResponse = (FilterDocRetrieveResultsResponseType) client
                            .invokePort(AdapterComponentRedactionEnginePortType.class,
                                    "filterDocRetrieveResults", filterDocRetrieveResultsRequest);
                    response = filteredResponse.getRetrieveDocumentSetResponse();
                }
            } else {
                LOG.error("Failed to call the web service (" + serviceName + ").  The URL is null.");
            }
        } catch (Exception ex) {
            LOG.error("Error calling filterDocRetrieveResults: " + ex.getMessage(), ex);
        }

        LOG.debug("End respondingGatewayCrossGatewayQuery");
        return response;

    }

}
