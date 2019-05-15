/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.deferredresults.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterDeferredResultsResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryResponseType;
import gov.hhs.fha.nhinc.docrepository.adapter.dao.DeferredResponseOptionDao;
import gov.hhs.fha.nhinc.docrepository.adapter.model.DocQueryDeferredResponseMetadata;
import gov.hhs.fha.nhinc.test.DAOIntegrationTest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AdapterDeferredResultsOptionImplTest extends DAOIntegrationTest {

    private static final String ENDPOINT = "RESPONSE ENDPOINT";
    private static final String REQUEST = "REQUEST ID";
    @Mock
    DeferredResponseOptionDao dao;

    @InjectMocks
    AdapterDeferredResultsOptionImpl impl;

    @Before
    public void setupDB() {
        DocQueryDeferredResponseMetadata metadata = new DocQueryDeferredResponseMetadata();
        metadata.setRequestId(REQUEST);
        metadata.setResponseEndpoint(ENDPOINT);
        when(dao.findByRequest(Mockito.eq(REQUEST))).thenReturn(metadata);
    }

    @Test
    public void testRespondingGatewayCrossGatewayQueryResultsUnsecured() {

        AdhocQueryResponse message = getHappyPathResponse();
        RespondingGatewayCrossGatewayQueryResponseType request = new RespondingGatewayCrossGatewayQueryResponseType();
        request.setAdhocQueryResponse(message);

        request.setAssertion(generateAssertion());

        AdapterDeferredResultsResponseType response = impl.respondingGatewayCrossGatewayQueryResults(request);
        verify(dao, Mockito.times(1)).findByRequest(Mockito.eq(REQUEST));
        assertEquals(Boolean.TRUE, response.isStatus());
    }

    @Test
    public void testRespondingGatewayCrossGatewayQueryResultsSecured() {
        AdhocQueryResponse message = getHappyPathResponse();
        AdapterDeferredResultsResponseType response = impl.respondingGatewayCrossGatewayQueryResults(message,
            generateAssertion());
        verify(dao, Mockito.times(1)).findByRequest(Mockito.eq(REQUEST));
        assertEquals(Boolean.TRUE, response.isStatus());
    }

    @Test
    public void testRespondingGatewayCrossGatewayQueryResultsUnsecuredNoAssertion() {

        AdhocQueryResponse message = getHappyPathResponse();
        RespondingGatewayCrossGatewayQueryResponseType request = new RespondingGatewayCrossGatewayQueryResponseType();
        request.setAdhocQueryResponse(message);

        request.setAssertion(null);

        AdapterDeferredResultsResponseType response = impl.respondingGatewayCrossGatewayQueryResults(request);
        verify(dao, Mockito.never()).findByRequest(Mockito.any(String.class));
        assertEquals(Boolean.FALSE, response.isStatus());
    }

    @Test
    public void testRespondingGatewayCrossGatewayQueryResultsSecuredNoAssertion() {
        AdhocQueryResponse message = getHappyPathResponse();
        AdapterDeferredResultsResponseType response = impl.respondingGatewayCrossGatewayQueryResults(message, null);
        verify(dao, Mockito.never()).findByRequest(Mockito.any(String.class));
        assertEquals(Boolean.FALSE, response.isStatus());
    }

    @Test
    public void testRespondingGatewayCrossGatewayQueryResultsUnsecuredNoResult() {

        AdhocQueryResponse message = getNegativePathResponse();
        RespondingGatewayCrossGatewayQueryResponseType request = new RespondingGatewayCrossGatewayQueryResponseType();
        request.setAdhocQueryResponse(message);

        request.setAssertion(generateAssertion());

        AdapterDeferredResultsResponseType response = impl.respondingGatewayCrossGatewayQueryResults(request);
        verify(dao, Mockito.times(1)).findByRequest(Mockito.eq("NOT CONTAINED"));
        assertEquals(Boolean.FALSE, response.isStatus());
    }

    @Test
    public void testRespondingGatewayCrossGatewayQueryResultsSecuredNoResult() {
        AdhocQueryResponse message = getNegativePathResponse();
        AdapterDeferredResultsResponseType response = impl.respondingGatewayCrossGatewayQueryResults(message,
            generateAssertion());
        verify(dao, Mockito.times(1)).findByRequest(Mockito.eq("NOT CONTAINED"));
        assertEquals(Boolean.FALSE, response.isStatus());
    }

    private static AssertionType generateAssertion() {
        AssertionType assertion = new AssertionType();
        assertion.setDeferredResponseEndpoint(ENDPOINT);
        return assertion;

    }

    private static AdhocQueryResponse getHappyPathResponse() {
        AdhocQueryResponse response = new AdhocQueryResponse();
        response.setRequestId(REQUEST);
        return response;
    }

    private static AdhocQueryResponse getNegativePathResponse() {
        AdhocQueryResponse response = new AdhocQueryResponse();
        response.setRequestId("NOT CONTAINED");
        return response;
    }
}
