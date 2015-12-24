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

import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Neil Webb
 */
@RunWith(JMock.class)
public class RedactionEngineTest {
    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    final DocQueryResponseProcessor mockDocQueryResponseProcessor = context.mock(DocQueryResponseProcessor.class);
    final DocRetrieveResponseProcessor mockDocRetrieveResponseProcessor = context
            .mock(DocRetrieveResponseProcessor.class);

    @Test
    public void testGetDocQueryResponseProcessor() {
        try {
            RedactionEngine redactionEngine = new RedactionEngine() {
                @Override
                protected DocQueryResponseProcessor getDocQueryResponseProcessor() {
                    return mockDocQueryResponseProcessor;
                }

                @Override
                protected DocRetrieveResponseProcessor getDocRetrieveResponseProcessor() {
                    return mockDocRetrieveResponseProcessor;
                }
            };
            DocQueryResponseProcessor processor = redactionEngine.getDocQueryResponseProcessor();
            assertNotNull("DocQueryResponseProcessor was null", processor);
        } catch (Throwable t) {
            System.out.println("Error running testGetDocQueryResponseProcessor test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetDocQueryResponseProcessor test: " + t.getMessage());
        }
    }

    @Test
    public void testGetDocRetrieveResponseProcessor() {
        try {
            RedactionEngine redactionEngine = new RedactionEngine() {
                @Override
                protected DocQueryResponseProcessor getDocQueryResponseProcessor() {
                    return mockDocQueryResponseProcessor;
                }

                @Override
                protected DocRetrieveResponseProcessor getDocRetrieveResponseProcessor() {
                    return mockDocRetrieveResponseProcessor;
                }
            };
            DocRetrieveResponseProcessor processor = redactionEngine.getDocRetrieveResponseProcessor();
            assertNotNull("DocRetrieveResponseProcessor was null", processor);
        } catch (Throwable t) {
            System.out.println("Error running testGetDocRetrieveResponseProcessor test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetDocRetrieveResponseProcessor test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterAdhocQueryResultsHappy() {
        try {
            final AdhocQueryRequest mockAdhocQueryRequest = context.mock(AdhocQueryRequest.class);
            final AdhocQueryResponse mockAdhocQueryResponse = context.mock(AdhocQueryResponse.class);

            RedactionEngine redactionEngine = new RedactionEngine() {
                @Override
                protected DocQueryResponseProcessor getDocQueryResponseProcessor() {
                    return mockDocQueryResponseProcessor;
                }

                @Override
                protected DocRetrieveResponseProcessor getDocRetrieveResponseProcessor() {
                    return mockDocRetrieveResponseProcessor;
                }
            };
            context.checking(new Expectations() {
                {
                    oneOf(mockDocQueryResponseProcessor).filterAdhocQueryResults(
                            with(aNonNull(AdhocQueryRequest.class)), with(aNonNull(AdhocQueryResponse.class)));
                }
            });

            AdhocQueryResponse response = redactionEngine.filterAdhocQueryResults(mockAdhocQueryRequest,
                    mockAdhocQueryResponse);
            assertNotNull("AdhocQueryResponse should not be null", response);
        } catch (Throwable t) {
            System.out.println("Error running testFilterAdhocQueryResultsHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterAdhocQueryResultsHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterAdhocQueryResultsNullInputs() {
        try {
            final AdhocQueryRequest mockAdhocQueryRequest = null;
            final AdhocQueryResponse mockAdhocQueryResponse = null;

            RedactionEngine redactionEngine = new RedactionEngine() {
                @Override
                protected DocQueryResponseProcessor getDocQueryResponseProcessor() {
                    return mockDocQueryResponseProcessor;
                }

                @Override
                protected DocRetrieveResponseProcessor getDocRetrieveResponseProcessor() {
                    return mockDocRetrieveResponseProcessor;
                }
            };
            context.checking(new Expectations() {
                {
                    oneOf(mockDocQueryResponseProcessor).filterAdhocQueryResults(with(aNull(AdhocQueryRequest.class)),
                            with(aNull(AdhocQueryResponse.class)));
                }
            });

            AdhocQueryResponse response = redactionEngine.filterAdhocQueryResults(mockAdhocQueryRequest,
                    mockAdhocQueryResponse);
            assertNotNull("AdhocQueryResponse should not be null", response);
        } catch (Throwable t) {
            System.out.println("Error running testFilterAdhocQueryResultsNullInputs test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterAdhocQueryResultsNullInputs test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterAdhocQueryResultsNullProcessor() {
        try {
            final AdhocQueryRequest mockAdhocQueryRequest = context.mock(AdhocQueryRequest.class);
            final AdhocQueryResponse mockAdhocQueryResponse = context.mock(AdhocQueryResponse.class);

            RedactionEngine redactionEngine = new RedactionEngine() {
                @Override
                protected DocQueryResponseProcessor getDocQueryResponseProcessor() {
                    return null;
                }

                @Override
                protected DocRetrieveResponseProcessor getDocRetrieveResponseProcessor() {
                    return mockDocRetrieveResponseProcessor;
                }
            };
            AdhocQueryResponse response = redactionEngine.filterAdhocQueryResults(mockAdhocQueryRequest,
                    mockAdhocQueryResponse);
            assertNull("AdhocQueryResponse should be null", response);
        } catch (Throwable t) {
            System.out.println("Error running testFilterAdhocQueryResultsNullProcessor test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterAdhocQueryResultsNullProcessor test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterAdhocQueryResultsNullResponse() {
        try {
            final AdhocQueryRequest mockAdhocQueryRequest = context.mock(AdhocQueryRequest.class);
            final AdhocQueryResponse mockAdhocQueryResponse = context.mock(AdhocQueryResponse.class);

            RedactionEngine redactionEngine = new RedactionEngine() {
                @Override
                protected DocQueryResponseProcessor getDocQueryResponseProcessor() {
                    DocQueryResponseProcessor processor = new DocQueryResponseProcessor() {
                        @Override
                        public AdhocQueryResponse filterAdhocQueryResults(AdhocQueryRequest adhocQueryRequest,
                                AdhocQueryResponse adhocQueryResponse) {
                            return null;
                        }
                    };
                    return processor;
                }

                @Override
                protected DocRetrieveResponseProcessor getDocRetrieveResponseProcessor() {
                    return mockDocRetrieveResponseProcessor;
                }
            };
            AdhocQueryResponse response = redactionEngine.filterAdhocQueryResults(mockAdhocQueryRequest,
                    mockAdhocQueryResponse);
            assertNull("AdhocQueryResponse should be null", response);
        } catch (Throwable t) {
            System.out.println("Error running testFilterAdhocQueryResultsNullResponse test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterAdhocQueryResultsNullResponse test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterRetrieveDocumentSetResultsHappy() {
        try {
            final RetrieveDocumentSetRequestType mockRequest = context.mock(RetrieveDocumentSetRequestType.class);
            final RetrieveDocumentSetResponseType mockResponse = context.mock(RetrieveDocumentSetResponseType.class);

            RedactionEngine redactionEngine = new RedactionEngine() {
                @Override
                protected DocQueryResponseProcessor getDocQueryResponseProcessor() {
                    return mockDocQueryResponseProcessor;
                }

                @Override
                protected DocRetrieveResponseProcessor getDocRetrieveResponseProcessor() {
                    return mockDocRetrieveResponseProcessor;
                }
            };
            context.checking(new Expectations() {
                {
                    oneOf(mockDocRetrieveResponseProcessor).filterRetrieveDocumentSetReults(
                            with(aNonNull(RetrieveDocumentSetRequestType.class)),
                            with(aNonNull(RetrieveDocumentSetResponseType.class)));
                }
            });

            RetrieveDocumentSetResponseType response = redactionEngine.filterRetrieveDocumentSetResults(mockRequest,
                    mockResponse);
            assertNotNull("RetrieveDocumentSetResponseType should not be null", response);
        } catch (Throwable t) {
            System.out.println("Error running testFilterRetrieveDocumentSetResultsHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterRetrieveDocumentSetResultsHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterRetrieveDocumentSetResultsNullInputs() {
        try {
            final RetrieveDocumentSetRequestType mockRequest = null;
            final RetrieveDocumentSetResponseType mockResponse = null;

            RedactionEngine redactionEngine = new RedactionEngine() {
                @Override
                protected DocQueryResponseProcessor getDocQueryResponseProcessor() {
                    return mockDocQueryResponseProcessor;
                }

                @Override
                protected DocRetrieveResponseProcessor getDocRetrieveResponseProcessor() {
                    return mockDocRetrieveResponseProcessor;
                }
            };
            context.checking(new Expectations() {
                {
                    oneOf(mockDocRetrieveResponseProcessor).filterRetrieveDocumentSetReults(
                            with(aNull(RetrieveDocumentSetRequestType.class)),
                            with(aNull(RetrieveDocumentSetResponseType.class)));
                }
            });

            RetrieveDocumentSetResponseType response = redactionEngine.filterRetrieveDocumentSetResults(mockRequest,
                    mockResponse);
            assertNotNull("RetrieveDocumentSetResponseType should not be null", response);
        } catch (Throwable t) {
            System.out.println("Error running testFilterRetrieveDocumentSetResultsNullInputs test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterRetrieveDocumentSetResultsNullInputs test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterRetrieveDocumentSetResultsNullProcessor() {
        try {
            final RetrieveDocumentSetRequestType mockRequest = context.mock(RetrieveDocumentSetRequestType.class);
            final RetrieveDocumentSetResponseType mockResponse = context.mock(RetrieveDocumentSetResponseType.class);

            RedactionEngine redactionEngine = new RedactionEngine() {
                @Override
                protected DocRetrieveResponseProcessor getDocRetrieveResponseProcessor() {
                    return null;
                }

                @Override
                protected DocQueryResponseProcessor getDocQueryResponseProcessor() {
                    return mockDocQueryResponseProcessor;
                }
            };
            RetrieveDocumentSetResponseType response = redactionEngine.filterRetrieveDocumentSetResults(mockRequest,
                    mockResponse);
            assertNull("RetrieveDocumentSetResponseType should be null", response);
        } catch (Throwable t) {
            System.out.println("Error running testFilterRetrieveDocumentSetResultsNullProcessor test: "
                    + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterRetrieveDocumentSetResultsNullProcessor test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterRetrieveDocumentSetResultsNullResponse() {
        try {
            final RetrieveDocumentSetRequestType mockRequest = context.mock(RetrieveDocumentSetRequestType.class);
            final RetrieveDocumentSetResponseType mockResponse = context.mock(RetrieveDocumentSetResponseType.class);

            RedactionEngine redactionEngine = new RedactionEngine() {
                @Override
                protected DocRetrieveResponseProcessor getDocRetrieveResponseProcessor() {
                    DocRetrieveResponseProcessor processor = new DocRetrieveResponseProcessor() {
                        @Override
                        public RetrieveDocumentSetResponseType filterRetrieveDocumentSetReults(
                                RetrieveDocumentSetRequestType retrieveRequest,
                                RetrieveDocumentSetResponseType retrieveResponse) {
                            return null;
                        }
                    };
                    return processor;
                }

                @Override
                protected DocQueryResponseProcessor getDocQueryResponseProcessor() {
                    return mockDocQueryResponseProcessor;
                }
            };

            RetrieveDocumentSetResponseType response = redactionEngine.filterRetrieveDocumentSetResults(mockRequest,
                    mockResponse);
            assertNull("RetrieveDocumentSetResponseType should be null", response);
        } catch (Throwable t) {
            System.out
                    .println("Error running testFilterRetrieveDocumentSetResultsNullResponse test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterRetrieveDocumentSetResultsNullResponse test: " + t.getMessage());
        }
    }

}