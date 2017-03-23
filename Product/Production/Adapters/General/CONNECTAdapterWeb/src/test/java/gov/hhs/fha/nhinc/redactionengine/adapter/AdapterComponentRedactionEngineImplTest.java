/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocQueryResultsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocQueryResultsResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocRetrieveResultsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocRetrieveResultsResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Neil Webb
 */
@RunWith(JMock.class)
public class AdapterComponentRedactionEngineImplTest {
    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final WebServiceContext mockContext = context.mock(WebServiceContext.class);

    @Test
    public void testFilterDocQueryResultsHappy() {
        try {
            AdapterComponentRedactionEngineImpl redactionEngine = new AdapterComponentRedactionEngineImpl() {
                @Override
                protected AssertionType getAssertion(WebServiceContext context) {
                    return new AssertionType();
                }

            };

            FilterDocQueryResultsRequestType filterDocQueryResultsRequest = new FilterDocQueryResultsRequestType();
            AdhocQueryRequest adhocQueryRequest = new AdhocQueryRequest();
            AdhocQueryResponse adhocQueryResponse = new AdhocQueryResponse();
            filterDocQueryResultsRequest.setAdhocQueryRequest(adhocQueryRequest);
            filterDocQueryResultsRequest.setAdhocQueryResponse(adhocQueryResponse);

            FilterDocQueryResultsResponseType response = redactionEngine
                    .filterDocQueryResults(filterDocQueryResultsRequest, mockContext);
            assertNotNull("FilterDocQueryResultsResponseType was null", response);
            assertNotNull("AdhocQueryResponse was null", response.getAdhocQueryResponse());
        } catch (Throwable t) {
            System.out.println("Error running testFilterDocQueryResultsHappy: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterDocQueryResultsHappy: " + t.getMessage());
        }
    }

    @Test
    public void testFilterDocQueryResultsNullInput() {
        try {
            AdapterComponentRedactionEngineImpl redactionEngine = new AdapterComponentRedactionEngineImpl() {
                @Override
                protected AssertionType getAssertion(WebServiceContext context) {
                    return new AssertionType();
                }
            };

            FilterDocQueryResultsRequestType filterDocQueryResultsRequest = null;
            FilterDocQueryResultsResponseType response = redactionEngine
                    .filterDocQueryResults(filterDocQueryResultsRequest, mockContext);
            assertNull("FilterDocQueryResultsResponseType was not null", response);
        } catch (Throwable t) {
            System.out.println("Error running testFilterDocQueryResultsNullInput: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterDocQueryResultsNullInput: " + t.getMessage());
        }
    }

    @Test
    public void testFilterDocQueryResultsNullResponse() {
        try {
            AdapterComponentRedactionEngineImpl redactionEngine = new AdapterComponentRedactionEngineImpl() {
                @Override
                protected AssertionType getAssertion(WebServiceContext context) {
                    return new AssertionType();
                }

                @Override
                protected AdhocQueryResponse invokeRedactionEngineForQuery(
                        FilterDocQueryResultsRequestType filterDocQueryResultsRequest) {
                    return null;
                }

            };
            FilterDocQueryResultsRequestType filterDocQueryResultsRequest = new FilterDocQueryResultsRequestType();
            AdhocQueryRequest adhocQueryRequest = new AdhocQueryRequest();
            AdhocQueryResponse adhocQueryResponse = new AdhocQueryResponse();
            filterDocQueryResultsRequest.setAdhocQueryRequest(adhocQueryRequest);
            filterDocQueryResultsRequest.setAdhocQueryResponse(adhocQueryResponse);

            FilterDocQueryResultsResponseType response = redactionEngine
                    .filterDocQueryResults(filterDocQueryResultsRequest, mockContext);
            assertNotNull("FilterDocQueryResultsResponseType was null", response);
            assertNull("AdhocQueryResponse was not null", response.getAdhocQueryResponse());
        } catch (Throwable t) {
            System.out.println("Error running testFilterDocQueryResultsNullResponse: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterDocQueryResultsNullResponse: " + t.getMessage());
        }
    }

    @Test
    public void testFilterDocQueryResultsNullQueryRequest() {
        try {
            AdapterComponentRedactionEngineImpl redactionEngine = new AdapterComponentRedactionEngineImpl() {
                @Override
                protected AssertionType getAssertion(WebServiceContext context) {
                    return new AssertionType();
                }

                @Override
                protected AdhocQueryResponse invokeRedactionEngineForQuery(
                        FilterDocQueryResultsRequestType filterDocQueryResultsRequest) {
                    return new AdhocQueryResponse();
                }

            };
            FilterDocQueryResultsRequestType filterDocQueryResultsRequest = new FilterDocQueryResultsRequestType();
            AdhocQueryRequest adhocQueryRequest = null;
            AdhocQueryResponse adhocQueryResponse = new AdhocQueryResponse();
            filterDocQueryResultsRequest.setAdhocQueryRequest(adhocQueryRequest);
            filterDocQueryResultsRequest.setAdhocQueryResponse(adhocQueryResponse);

            FilterDocQueryResultsResponseType response = redactionEngine
                    .filterDocQueryResults(filterDocQueryResultsRequest, mockContext);
            assertNotNull("FilterDocQueryResultsResponseType was null", response);
            assertNotNull("AdhocQueryResponse was null", response.getAdhocQueryResponse());
        } catch (Throwable t) {
            System.out.println("Error running testFilterDocQueryResultsNullQueryRequest: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterDocQueryResultsNullQueryRequest: " + t.getMessage());
        }
    }

    @Test
    public void testFilterDocQueryResultsNullQueryResponse() {
        try {
            AdapterComponentRedactionEngineImpl redactionEngine = new AdapterComponentRedactionEngineImpl() {
                @Override
                protected AssertionType getAssertion(WebServiceContext context) {
                    return new AssertionType();
                }

                @Override
                protected AdhocQueryResponse invokeRedactionEngineForQuery(
                        FilterDocQueryResultsRequestType filterDocQueryResultsRequest) {
                    return new AdhocQueryResponse();
                }

            };
            FilterDocQueryResultsRequestType filterDocQueryResultsRequest = new FilterDocQueryResultsRequestType();
            AdhocQueryRequest adhocQueryRequest = new AdhocQueryRequest();
            AdhocQueryResponse adhocQueryResponse = null;
            filterDocQueryResultsRequest.setAdhocQueryRequest(adhocQueryRequest);
            filterDocQueryResultsRequest.setAdhocQueryResponse(adhocQueryResponse);

            FilterDocQueryResultsResponseType response = redactionEngine
                    .filterDocQueryResults(filterDocQueryResultsRequest, mockContext);
            assertNotNull("FilterDocQueryResultsResponseType was null", response);
            assertNotNull("AdhocQueryResponse was null", response.getAdhocQueryResponse());
        } catch (Throwable t) {
            System.out.println("Error running testFilterDocQueryResultsNullQueryResponse: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterDocQueryResultsNullQueryResponse: " + t.getMessage());
        }
    }

    @Test
    public void testFilterDocRetrieveResultsHappy() {
        try {
            AdapterComponentRedactionEngineImpl redactionEngine = new AdapterComponentRedactionEngineImpl() {
                @Override
                protected AssertionType getAssertion(WebServiceContext context) {
                    return new AssertionType();
                }

                @Override
                protected RetrieveDocumentSetResponseType invokeRedactionEngineForRetrieve(
                        FilterDocRetrieveResultsRequestType filterDocRetrieveResultsRequest) {
                    return new RetrieveDocumentSetResponseType();
                }

            };
            FilterDocRetrieveResultsRequestType filterDocRetrieveResultsRequest = new FilterDocRetrieveResultsRequestType();
            RetrieveDocumentSetRequestType retrieveDocRequest = new RetrieveDocumentSetRequestType();
            RetrieveDocumentSetResponseType retreiveDocResponse = new RetrieveDocumentSetResponseType();
            filterDocRetrieveResultsRequest.setRetrieveDocumentSetRequest(retrieveDocRequest);
            filterDocRetrieveResultsRequest.setRetrieveDocumentSetResponse(retreiveDocResponse);

            FilterDocRetrieveResultsResponseType response = redactionEngine
                    .filterDocRetrieveResults(filterDocRetrieveResultsRequest, mockContext);
            assertNotNull("FilterDocRetrieveResultsResponseType was null", response);
            assertNotNull("RetrieveDocumentSetResponseType was null", response.getRetrieveDocumentSetResponse());
        } catch (Throwable t) {
            System.out.println("Error running testFilterDocRetrieveResultsHappy: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterDocRetrieveResultsHappy: " + t.getMessage());
        }
    }

    @Test
    public void testFilterDocRetrieveResultsNullInput() {
        try {
            AdapterComponentRedactionEngineImpl redactionEngine = new AdapterComponentRedactionEngineImpl() {
                @Override
                protected AssertionType getAssertion(WebServiceContext context) {
                    return new AssertionType();
                }

                @Override
                protected RetrieveDocumentSetResponseType invokeRedactionEngineForRetrieve(
                        FilterDocRetrieveResultsRequestType filterDocRetrieveResultsRequest) {
                    return new RetrieveDocumentSetResponseType();
                }

            };
            FilterDocRetrieveResultsRequestType filterDocRetrieveResultsRequest = null;

            FilterDocRetrieveResultsResponseType response = redactionEngine
                    .filterDocRetrieveResults(filterDocRetrieveResultsRequest, mockContext);
            assertNull("FilterDocRetrieveResultsResponseType was not null", response);
        } catch (Throwable t) {
            System.out.println("Error running testFilterDocRetrieveResultsNullInput: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterDocRetrieveResultsNullInput: " + t.getMessage());
        }
    }

    @Test
    public void testFilterDocRetrieveResultsNullResponse() {
        try {
            AdapterComponentRedactionEngineImpl redactionEngine = new AdapterComponentRedactionEngineImpl() {
                @Override
                protected AssertionType getAssertion(WebServiceContext context) {
                    return new AssertionType();
                }

                @Override
                protected RetrieveDocumentSetResponseType invokeRedactionEngineForRetrieve(
                        FilterDocRetrieveResultsRequestType filterDocRetrieveResultsRequest) {
                    return null;
                }

            };
            FilterDocRetrieveResultsRequestType filterDocRetrieveResultsRequest = new FilterDocRetrieveResultsRequestType();
            RetrieveDocumentSetRequestType retrieveDocRequest = new RetrieveDocumentSetRequestType();
            RetrieveDocumentSetResponseType retreiveDocResponse = new RetrieveDocumentSetResponseType();
            filterDocRetrieveResultsRequest.setRetrieveDocumentSetRequest(retrieveDocRequest);
            filterDocRetrieveResultsRequest.setRetrieveDocumentSetResponse(retreiveDocResponse);

            FilterDocRetrieveResultsResponseType response = redactionEngine
                    .filterDocRetrieveResults(filterDocRetrieveResultsRequest, mockContext);
            assertNotNull("FilterDocRetrieveResultsResponseType was null", response);
            assertNull("RetrieveDocumentSetResponseType was not null", response.getRetrieveDocumentSetResponse());
        } catch (Throwable t) {
            System.out.println("Error running testFilterDocRetrieveResultsNullResponse: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterDocRetrieveResultsNullResponse: " + t.getMessage());
        }
    }

    @Test
    public void testFilterDocRetrieveResultsNullRetrieveRequest() {
        try {
            AdapterComponentRedactionEngineImpl redactionEngine = new AdapterComponentRedactionEngineImpl() {
                @Override
                protected AssertionType getAssertion(WebServiceContext context) {
                    return new AssertionType();
                }

                @Override
                protected RetrieveDocumentSetResponseType invokeRedactionEngineForRetrieve(
                        FilterDocRetrieveResultsRequestType filterDocRetrieveResultsRequest) {
                    return new RetrieveDocumentSetResponseType();
                }

            };
            FilterDocRetrieveResultsRequestType filterDocRetrieveResultsRequest = new FilterDocRetrieveResultsRequestType();
            RetrieveDocumentSetRequestType retrieveDocRequest = null;
            RetrieveDocumentSetResponseType retreiveDocResponse = new RetrieveDocumentSetResponseType();
            filterDocRetrieveResultsRequest.setRetrieveDocumentSetRequest(retrieveDocRequest);
            filterDocRetrieveResultsRequest.setRetrieveDocumentSetResponse(retreiveDocResponse);

            FilterDocRetrieveResultsResponseType response = redactionEngine
                    .filterDocRetrieveResults(filterDocRetrieveResultsRequest, mockContext);
            assertNotNull("FilterDocRetrieveResultsResponseType was null", response);
            assertNotNull("RetrieveDocumentSetResponseType was null", response.getRetrieveDocumentSetResponse());
        } catch (Throwable t) {
            System.out.println("Error running testFilterDocRetrieveResultsNullRetrieveRequest: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterDocRetrieveResultsNullRetrieveRequest: " + t.getMessage());
        }
    }

    @Test
    public void testFilterDocRetrieveResultsNullRetrieveResponse() {
        try {
            AdapterComponentRedactionEngineImpl redactionEngine = new AdapterComponentRedactionEngineImpl() {
                @Override
                protected AssertionType getAssertion(WebServiceContext context) {
                    return new AssertionType();
                }

                @Override
                protected RetrieveDocumentSetResponseType invokeRedactionEngineForRetrieve(
                        FilterDocRetrieveResultsRequestType filterDocRetrieveResultsRequest) {
                    return new RetrieveDocumentSetResponseType();
                }

            };
            FilterDocRetrieveResultsRequestType filterDocRetrieveResultsRequest = new FilterDocRetrieveResultsRequestType();
            RetrieveDocumentSetRequestType retrieveDocRequest = new RetrieveDocumentSetRequestType();
            RetrieveDocumentSetResponseType retreiveDocResponse = null;
            filterDocRetrieveResultsRequest.setRetrieveDocumentSetRequest(retrieveDocRequest);
            filterDocRetrieveResultsRequest.setRetrieveDocumentSetResponse(retreiveDocResponse);

            FilterDocRetrieveResultsResponseType response = redactionEngine
                    .filterDocRetrieveResults(filterDocRetrieveResultsRequest, mockContext);
            assertNotNull("FilterDocRetrieveResultsResponseType was null", response);
            assertNotNull("RetrieveDocumentSetResponseType was null", response.getRetrieveDocumentSetResponse());
        } catch (Throwable t) {
            System.out.println("Error running testFilterDocRetrieveResultsNullRetrieveResponse: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterDocRetrieveResultsNullRetrieveResponse: " + t.getMessage());
        }
    }

}