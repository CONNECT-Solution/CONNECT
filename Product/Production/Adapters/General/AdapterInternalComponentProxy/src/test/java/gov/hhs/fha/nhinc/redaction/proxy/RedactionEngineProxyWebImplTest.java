package gov.hhs.fha.nhinc.redaction.proxy;

import gov.hhs.fha.nhinc.adaptercomponentredaction.AdapterComponentRedactionEnginePortType;
import gov.hhs.fha.nhinc.adaptercomponentredaction.AdapterComponentRedactionEngineService;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocQueryResultsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocQueryResultsResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocRetrieveResultsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocRetrieveResultsResponseType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileWriter;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 *
 * @author Neil Webb
 */
@RunWith(JMock.class)
public class RedactionEngineProxyWebImplTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);
    final AdapterComponentRedactionEngineService mockService = context.mock(AdapterComponentRedactionEngineService.class);
    final AdapterComponentRedactionEnginePortType mockPort = context.mock(AdapterComponentRedactionEnginePortType.class);
    final AdhocQueryRequest mockAdhocQueryRequest = context.mock(AdhocQueryRequest.class);
    final AdhocQueryResponse mockAdhocQueryResponse = context.mock(AdhocQueryResponse.class);
    final RetrieveDocumentSetRequestType mockRequest = context.mock(RetrieveDocumentSetRequestType.class);
    final RetrieveDocumentSetResponseType mockResponse = context.mock(RetrieveDocumentSetResponseType.class);

    @BeforeClass
    public static void setUpClass() throws Exception
    {
        System.setProperty("nhinc.properties.dir", System.getenv("NHINC_PROPERTIES_DIR"));
        System.setProperty("wsdl.path", System.getenv("NHINC_PROPERTIES_DIR") + File.separator + "wsdl");
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }



    @Test
    public void testCreateLogger()
    {
        try
        {
            RedactionEngineProxyWebImpl webProxy = new RedactionEngineProxyWebImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AdapterComponentRedactionEnginePortType getAdapterComponentRedactionEnginePortType()
                {
                    return mockPort;
                }
                @Override
                protected String invokeConnectionManager(String serviceName) throws ConnectionManagerException
                {
                    return "test_endpoint";
                }
            };
            Log log = webProxy.createLogger();
            assertNotNull("Log was null", log);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testCreateLogger test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCreateLogger test: " + t.getMessage());
        }
    }

    @Test
    public void testInvokeConnectionManagerHappy()
    {
        try
        {
            RedactionEngineProxyWebImpl webProxy = new RedactionEngineProxyWebImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AdapterComponentRedactionEnginePortType getAdapterComponentRedactionEnginePortType()
                {
                    return mockPort;
                }
                @Override
                protected String invokeConnectionManager(String serviceName) throws ConnectionManagerException
                {
                    return "test_endpoint";
                }
            };
            String endpointURL = webProxy.invokeConnectionManager("not_used_by_override");
            assertNotNull("EndpointURL was null", endpointURL);
            assertEquals("EndpointURL was not correct", "test_endpoint", endpointURL);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testInvokeConnectionManagerHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testInvokeConnectionManagerHappy test: " + t.getMessage());
        }
    }

    @Test (expected= gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException.class)
    public void testInvokeConnectionManagerException() throws ConnectionManagerException
    {
        RedactionEngineProxyWebImpl webProxy = new RedactionEngineProxyWebImpl()
        {
            @Override
            protected Log createLogger()
            {
                return mockLog;
            }
            @Override
            protected AdapterComponentRedactionEnginePortType getAdapterComponentRedactionEnginePortType()
            {
                return mockPort;
            }
            @Override
            protected String invokeConnectionManager(String serviceName) throws ConnectionManagerException
            {
                throw new ConnectionManagerException();
            }
        };
        context.checking(new Expectations()
        {
            {
                allowing(mockLog).isDebugEnabled();
                allowing(mockLog).debug(with(any(String.class)));
            }
        });
        webProxy.invokeConnectionManager("not_used_by_override");
        fail("Exception should have been thrown");
    }

    @Test
    public void testGetEndpointURLHappy()
    {
        try
        {
            RedactionEngineProxyWebImpl webProxy = new RedactionEngineProxyWebImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AdapterComponentRedactionEnginePortType getAdapterComponentRedactionEnginePortType()
                {
                    return mockPort;
                }
                @Override
                protected String invokeConnectionManager(String serviceName) throws ConnectionManagerException
                {
                    return "test_endpoint";
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            String endpointURL = webProxy.getEndpointURL();
            assertNotNull("EndpointURL was null", endpointURL);
            assertEquals("EndpointURL was not correct", "test_endpoint", endpointURL);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetEndpointURLHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetEndpointURLHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testGetEndpointURLException()
    {
        try
        {
            RedactionEngineProxyWebImpl webProxy = new RedactionEngineProxyWebImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AdapterComponentRedactionEnginePortType getAdapterComponentRedactionEnginePortType()
                {
                    return mockPort;
                }
                @Override
                protected String invokeConnectionManager(String serviceName) throws ConnectionManagerException
                {
                    throw new ConnectionManagerException();
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).error(with(any(String.class)), with(aNonNull(ConnectionManagerException.class)));
                }
            });
            String endpointURL = webProxy.getEndpointURL();
            assertNull("EndpointURL was not null", endpointURL);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetEndpointURLException test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetEndpointURLException test: " + t.getMessage());
        }
    }

    @Test
    public void testGetAdapterComponentRedactionEngineServiceHappy()
    {
        try
        {
            RedactionEngineProxyWebImpl webProxy = new RedactionEngineProxyWebImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AdapterComponentRedactionEngineService getAdapterComponentRedactionEngineService()
                {
                    return mockService;
                }
                @Override
                protected String invokeConnectionManager(String serviceName) throws ConnectionManagerException
                {
                    return "test_endpoint";
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            AdapterComponentRedactionEngineService service = webProxy.getAdapterComponentRedactionEngineService();
            assertNotNull("AdapterComponentRedactionEngineService was null", service);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetAdapterComponentRedactionEngineServiceHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetAdapterComponentRedactionEngineServiceHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testGetAdapterComponentRedactionEnginePortTypeHappy()
    {
        try
        {
            RedactionEngineProxyWebImpl webProxy = new RedactionEngineProxyWebImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AdapterComponentRedactionEngineService getAdapterComponentRedactionEngineService()
                {
                    return mockService;
                }
                @Override
                protected AdapterComponentRedactionEnginePortType getAdapterComponentRedactionEnginePortType()
                {
                    return mockPort;
                }
                @Override
                protected String invokeConnectionManager(String serviceName) throws ConnectionManagerException
                {
                    return "test_endpoint";
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                    //allowing(mockService).getAdapterComponentRedactionEnginePortTypeBindingPort();
                }
            });
            AdapterComponentRedactionEnginePortType port = webProxy.getAdapterComponentRedactionEnginePortType();
            assertNotNull("AdapterComponentRedactionEnginePortType was null", port);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetAdapterComponentRedactionEnginePortTypeHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetAdapterComponentRedactionEnginePortTypeHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testGetAdapterComponentRedactionEnginePortTypeNullEndpointURL()
    {
        try
        {
            RedactionEngineProxyWebImpl webProxy = new RedactionEngineProxyWebImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AdapterComponentRedactionEngineService getAdapterComponentRedactionEngineService()
                {
                    return mockService;
                }
                @Override
                protected String getEndpointURL()
                {
                    return null;
                }
                @Override
                protected String invokeConnectionManager(String serviceName) throws ConnectionManagerException
                {
                    return "test_endpoint";
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).warn("Endpoint url was missing.");
                }
            });
            AdapterComponentRedactionEnginePortType port = webProxy.getAdapterComponentRedactionEnginePortType();
            assertNull("AdapterComponentRedactionEnginePortType was not null", port);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetAdapterComponentRedactionEnginePortTypeNullEndpointURL test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetAdapterComponentRedactionEnginePortTypeNullEndpointURL test: " + t.getMessage());
        }
    }

    @Test
    public void testGetAdapterComponentRedactionEnginePortTypeEmptyEndpointURL()
    {
        try
        {
            RedactionEngineProxyWebImpl webProxy = new RedactionEngineProxyWebImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AdapterComponentRedactionEngineService getAdapterComponentRedactionEngineService()
                {
                    return mockService;
                }
                @Override
                protected String getEndpointURL()
                {
                    return "";
                }
                @Override
                protected String invokeConnectionManager(String serviceName) throws ConnectionManagerException
                {
                    return "test_endpoint";
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).warn("Endpoint url was missing.");
                }
            });
            AdapterComponentRedactionEnginePortType port = webProxy.getAdapterComponentRedactionEnginePortType();
            assertNull("AdapterComponentRedactionEnginePortType was not null", port);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetAdapterComponentRedactionEnginePortTypeEmptyEndpointURL test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetAdapterComponentRedactionEnginePortTypeEmptyEndpointURL test: " + t.getMessage());
        }
    }

    @Test
    public void testGetAdapterComponentRedactionEnginePortTypeNullService()
    {
        try
        {
            RedactionEngineProxyWebImpl webProxy = new RedactionEngineProxyWebImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AdapterComponentRedactionEngineService getAdapterComponentRedactionEngineService()
                {
                    return null;
                }
                @Override
                protected String invokeConnectionManager(String serviceName) throws ConnectionManagerException
                {
                    return "test_endpoint";
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).warn("AdapterComponentRedactionEngineService was null");
                }
            });
            AdapterComponentRedactionEnginePortType port = webProxy.getAdapterComponentRedactionEnginePortType();
            assertNull("AdapterComponentRedactionEnginePortType was not null", port);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetAdapterComponentRedactionEnginePortTypeNullService test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetAdapterComponentRedactionEnginePortTypeNullService test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterAdhocQueryResultsHappy()
    {
        try
        {
            RedactionEngineProxyWebImpl webProxy = new RedactionEngineProxyWebImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AdapterComponentRedactionEnginePortType getAdapterComponentRedactionEnginePortType()
                {
                    return mockPort;
                }
                @Override
                protected String invokeConnectionManager(String serviceName) throws ConnectionManagerException
                {
                    return "test_endpoint";
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockPort).filterDocQueryResults(with(aNonNull(FilterDocQueryResultsRequestType.class)));
                }
            });

            AdhocQueryResponse response = webProxy.filterAdhocQueryResults(mockAdhocQueryRequest, mockAdhocQueryResponse);
            assertNotNull("AdhocQueryResponse should not be null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterAdhocQueryResultsHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterAdhocQueryResultsHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterAdhocQueryResultsNullAdhocQueryRequest()
    {
        try
        {
            RedactionEngineProxyWebImpl webProxy = new RedactionEngineProxyWebImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AdapterComponentRedactionEnginePortType getAdapterComponentRedactionEnginePortType()
                {
                    return mockPort;
                }
                @Override
                protected String invokeConnectionManager(String serviceName) throws ConnectionManagerException
                {
                    return "test_endpoint";
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).warn("AdhocQueryRequest was null.");
                }
            });

            AdhocQueryResponse response = webProxy.filterAdhocQueryResults(null, mockAdhocQueryResponse);
            assertNull("AdhocQueryResponse should be null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterAdhocQueryResultsNullAdhocQueryRequest test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterAdhocQueryResultsNullAdhocQueryRequest test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterAdhocQueryResultsNullAdhocQueryResponse()
    {
        try
        {
            RedactionEngineProxyWebImpl webProxy = new RedactionEngineProxyWebImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AdapterComponentRedactionEnginePortType getAdapterComponentRedactionEnginePortType()
                {
                    return mockPort;
                }
                @Override
                protected String invokeConnectionManager(String serviceName) throws ConnectionManagerException
                {
                    return "test_endpoint";
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).warn("AdhocQueryResponse was null.");
                }
            });

            AdhocQueryResponse response = webProxy.filterAdhocQueryResults(mockAdhocQueryRequest, null);
            assertNull("AdhocQueryResponse should be null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterAdhocQueryResultsNullAdhocQueryResponse test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterAdhocQueryResultsNullAdhocQueryResponse test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterAdhocQueryResultsNullPort()
    {
        try
        {
            RedactionEngineProxyWebImpl webProxy = new RedactionEngineProxyWebImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AdapterComponentRedactionEnginePortType getAdapterComponentRedactionEnginePortType()
                {
                    return null;
                }
                @Override
                protected String invokeConnectionManager(String serviceName) throws ConnectionManagerException
                {
                    return "test_endpoint";
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).warn("AdapterComponentRedactionEnginePortType was null.");
                }
            });

            AdhocQueryResponse response = webProxy.filterAdhocQueryResults(mockAdhocQueryRequest, mockAdhocQueryResponse);
            assertNull("AdhocQueryResponse should be null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterAdhocQueryResultsNullPort test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterAdhocQueryResultsNullPort test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterAdhocQueryResultsNullResults()
    {
        try
        {
            RedactionEngineProxyWebImpl webProxy = new RedactionEngineProxyWebImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AdapterComponentRedactionEnginePortType getAdapterComponentRedactionEnginePortType()
                {
                    AdapterComponentRedactionEnginePortType port = new AdapterComponentRedactionEnginePortType()
                    {
                        public FilterDocQueryResultsResponseType filterDocQueryResults(FilterDocQueryResultsRequestType arg0)
                        {
                            return null;
                        }

                        public FilterDocRetrieveResultsResponseType filterDocRetrieveResults(FilterDocRetrieveResultsRequestType arg0)
                        {
                            throw new UnsupportedOperationException("Not supported");
                        }
                    };
                    return port;
                }
                @Override
                protected String invokeConnectionManager(String serviceName) throws ConnectionManagerException
                {
                    return "test_endpoint";
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).warn("FilterDocQueryResultsResponseType was null.");
                }
            });

            AdhocQueryResponse response = webProxy.filterAdhocQueryResults(mockAdhocQueryRequest, mockAdhocQueryResponse);
            assertNull("AdhocQueryResponse should be null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterAdhocQueryResultsNullResults test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterAdhocQueryResultsNullResults test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterAdhocQueryResultsException()
    {
        try
        {
            RedactionEngineProxyWebImpl webProxy = new RedactionEngineProxyWebImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AdapterComponentRedactionEnginePortType getAdapterComponentRedactionEnginePortType()
                {
                    AdapterComponentRedactionEnginePortType port = new AdapterComponentRedactionEnginePortType()
                    {
                        public FilterDocQueryResultsResponseType filterDocQueryResults(FilterDocQueryResultsRequestType arg0)
                        {
                            throw new RuntimeException();
                        }

                        public FilterDocRetrieveResultsResponseType filterDocRetrieveResults(FilterDocRetrieveResultsRequestType arg0)
                        {
                            throw new UnsupportedOperationException("Not supported");
                        }
                    };
                    return port;
                }
                @Override
                protected String invokeConnectionManager(String serviceName) throws ConnectionManagerException
                {
                    return "test_endpoint";
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).error(with(any(String.class)), with(aNonNull(RuntimeException.class)));
                }
            });

            AdhocQueryResponse response = webProxy.filterAdhocQueryResults(mockAdhocQueryRequest, mockAdhocQueryResponse);
            assertNull("AdhocQueryResponse should be null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterAdhocQueryResultsException test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterAdhocQueryResultsException test: " + t.getMessage());
        }
    }









    @Test
    public void testFilterRetrieveDocumentSetResultsHappy()
    {
        try
        {
            RedactionEngineProxyWebImpl webProxy = new RedactionEngineProxyWebImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AdapterComponentRedactionEnginePortType getAdapterComponentRedactionEnginePortType()
                {
                    return mockPort;
                }
                @Override
                protected String invokeConnectionManager(String serviceName) throws ConnectionManagerException
                {
                    return "test_endpoint";
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockPort).filterDocRetrieveResults(with(aNonNull(FilterDocRetrieveResultsRequestType.class)));
                }
            });

            RetrieveDocumentSetResponseType response = webProxy.filterRetrieveDocumentSetResults(mockRequest, mockResponse);
            assertNotNull("RetrieveDocumentSetResponseType should not be null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterRetrieveDocumentSetResultsHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterRetrieveDocumentSetResultsHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterRetrieveDocumentSetResultsNullDocRetrieveRequest()
    {
        try
        {
            RedactionEngineProxyWebImpl webProxy = new RedactionEngineProxyWebImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AdapterComponentRedactionEnginePortType getAdapterComponentRedactionEnginePortType()
                {
                    return mockPort;
                }
                @Override
                protected String invokeConnectionManager(String serviceName) throws ConnectionManagerException
                {
                    return "test_endpoint";
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).warn("RetrieveDocumentSetRequestType was null.");
                }
            });

            RetrieveDocumentSetResponseType response = webProxy.filterRetrieveDocumentSetResults(null, mockResponse);
            assertNull("RetrieveDocumentSetResponseType should be null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterRetrieveDocumentSetResultsNullDocRetrieveRequest test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterRetrieveDocumentSetResultsNullDocRetrieveRequest test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterRetrieveDocumentSetResultsNullDocRetrieveResponse()
    {
        try
        {
            RedactionEngineProxyWebImpl webProxy = new RedactionEngineProxyWebImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AdapterComponentRedactionEnginePortType getAdapterComponentRedactionEnginePortType()
                {
                    return mockPort;
                }
                @Override
                protected String invokeConnectionManager(String serviceName) throws ConnectionManagerException
                {
                    return "test_endpoint";
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).warn("RetrieveDocumentSetResponseType was null.");
                }
            });

            RetrieveDocumentSetResponseType response = webProxy.filterRetrieveDocumentSetResults(mockRequest, null);
            assertNull("RetrieveDocumentSetResponseType should be null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterRetrieveDocumentSetResultsNullDocRetrieveResponse test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterRetrieveDocumentSetResultsNullDocRetrieveResponse test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterRetrieveDocumentSetResultsNullPort()
    {
        try
        {
            RedactionEngineProxyWebImpl webProxy = new RedactionEngineProxyWebImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AdapterComponentRedactionEnginePortType getAdapterComponentRedactionEnginePortType()
                {
                    return null;
                }
                @Override
                protected String invokeConnectionManager(String serviceName) throws ConnectionManagerException
                {
                    return "test_endpoint";
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).warn("AdapterComponentRedactionEnginePortType was null.");
                }
            });

            RetrieveDocumentSetResponseType response = webProxy.filterRetrieveDocumentSetResults(mockRequest, mockResponse);
            assertNull("RetrieveDocumentSetResponseType should be null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterRetrieveDocumentSetResultsNullPort test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterRetrieveDocumentSetResultsNullPort test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterRetrieveDocumentSetResultsNullResponse()
    {
        try
        {
            RedactionEngineProxyWebImpl webProxy = new RedactionEngineProxyWebImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AdapterComponentRedactionEnginePortType getAdapterComponentRedactionEnginePortType()
                {
                    AdapterComponentRedactionEnginePortType port = new AdapterComponentRedactionEnginePortType()
                    {
                        public FilterDocQueryResultsResponseType filterDocQueryResults(FilterDocQueryResultsRequestType arg0)
                        {
                            throw new UnsupportedOperationException("Not supported");
                        }

                        public FilterDocRetrieveResultsResponseType filterDocRetrieveResults(FilterDocRetrieveResultsRequestType arg0)
                        {
                            return null;
                        }
                    };
                    return port;
                }
                @Override
                protected String invokeConnectionManager(String serviceName) throws ConnectionManagerException
                {
                    return "test_endpoint";
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).warn("FilterDocRetrieveResultsResponseType was null.");
                }
            });

            RetrieveDocumentSetResponseType response = webProxy.filterRetrieveDocumentSetResults(mockRequest, mockResponse);
            assertNull("RetrieveDocumentSetResponseType should be null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterRetrieveDocumentSetResultsNullResponse test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterRetrieveDocumentSetResultsNullResponse test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterRetrieveDocumentSetResultsException()
    {
        try
        {
            RedactionEngineProxyWebImpl webProxy = new RedactionEngineProxyWebImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AdapterComponentRedactionEnginePortType getAdapterComponentRedactionEnginePortType()
                {
                    AdapterComponentRedactionEnginePortType port = new AdapterComponentRedactionEnginePortType()
                    {
                        public FilterDocQueryResultsResponseType filterDocQueryResults(FilterDocQueryResultsRequestType arg0)
                        {
                            throw new UnsupportedOperationException("Not supported");
                        }

                        public FilterDocRetrieveResultsResponseType filterDocRetrieveResults(FilterDocRetrieveResultsRequestType arg0)
                        {
                            throw new RuntimeException();
                        }
                    };
                    return port;
                }
                @Override
                protected String invokeConnectionManager(String serviceName) throws ConnectionManagerException
                {
                    return "test_endpoint";
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).error(with(any(String.class)), with(aNonNull(RuntimeException.class)));
                }
            });

            RetrieveDocumentSetResponseType response = webProxy.filterRetrieveDocumentSetResults(mockRequest, mockResponse);
            assertNull("RetrieveDocumentSetResponseType should be null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterRetrieveDocumentSetResultsException test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterRetrieveDocumentSetResultsException test: " + t.getMessage());
        }
    }

}