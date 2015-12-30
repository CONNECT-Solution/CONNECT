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
package gov.hhs.fha.nhinc.redactionengine.adapter;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocQueryResultsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocQueryResultsResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocRetrieveResultsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocRetrieveResultsResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Neil Webb
 */
public class AdapterComponentRedactionEngineImpl {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterComponentRedactionEngineImpl.class);
    private AsyncMessageIdExtractor extractor = new AsyncMessageIdExtractor();

    public FilterDocQueryResultsResponseType filterDocQueryResults(
            FilterDocQueryResultsRequestType filterDocQueryResultsRequest, WebServiceContext context) {
        LOG.debug("Begin filterDocQueryResults");
        FilterDocQueryResultsResponseType response = null;
        AssertionType assertion = getAssertion(context);

        if (filterDocQueryResultsRequest == null) {
            LOG.warn("FilterDocQueryResultsRequestType was null");
        } else {
            AdhocQueryResponse adhocQueryResponse = invokeRedactionEngineForQuery(filterDocQueryResultsRequest);
            response = new FilterDocQueryResultsResponseType();
            response.setAdhocQueryResponse(adhocQueryResponse);
        }
        LOG.debug("end filterDocQueryResults");
        return response;
    }

    public FilterDocRetrieveResultsResponseType filterDocRetrieveResults(
            FilterDocRetrieveResultsRequestType filterDocRetrieveResultsRequest, WebServiceContext context) {
        FilterDocRetrieveResultsResponseType response = null;
        AssertionType assertion = getAssertion(context);

        if (filterDocRetrieveResultsRequest == null) {
            LOG.warn("FilterDocRetrieveResultsRequestType was null");
        } else {
            RetrieveDocumentSetResponseType retrieveDocSetResonse = invokeRedactionEngineForRetrieve(filterDocRetrieveResultsRequest);
            response = new FilterDocRetrieveResultsResponseType();
            response.setRetrieveDocumentSetResponse(retrieveDocSetResonse);
        }
        return response;
    }

    protected AssertionType getAssertion(WebServiceContext context) {
        AssertionType assertion = new AssertionType();

        // Extract the relates to value from the WS-Addressing Header and place it in the Assertion Class
        assertion.setMessageId(extractor.getOrCreateAsyncMessageId(context));

        return assertion;
    }

    protected AdhocQueryResponse invokeRedactionEngineForQuery(
            FilterDocQueryResultsRequestType filterDocQueryResultsRequest) {
        AdhocQueryResponse adhocQueryResponse = new AdapterRedactionEngineOrchImpl().filterAdhocQueryResults(
                filterDocQueryResultsRequest.getAdhocQueryRequest(),
                filterDocQueryResultsRequest.getAdhocQueryResponse());

        return adhocQueryResponse;
    }

    protected RetrieveDocumentSetResponseType invokeRedactionEngineForRetrieve(
            FilterDocRetrieveResultsRequestType filterDocRetrieveResultsRequest) {
        RetrieveDocumentSetResponseType retrieveDocSetResponse = new AdapterRedactionEngineOrchImpl()
                .filterRetrieveDocumentSetResults(filterDocRetrieveResultsRequest.getRetrieveDocumentSetRequest(),
                        filterDocRetrieveResultsRequest.getRetrieveDocumentSetResponse());

        return retrieveDocSetResponse;
    }
}
