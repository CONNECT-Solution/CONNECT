package gov.hhs.fha.nhinc.redactionengine.adapter.proxy;

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

import javax.xml.ws.Service;
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
            AdapterRedactionEngineProxyWebServiceUnsecuredImpl webProxy = new AdapterRedactionEngineProxyWebServiceUnsecuredImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AdapterComponentRedactionEnginePortType getPort(String url, String wsAddressingAction)
                {
                    return mockPort;
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
    public void testGetAdapterComponentRedactionEngineServiceHappy()
    {
        try
        {
            AdapterRedactionEngineProxyWebServiceUnsecuredImpl webProxy = new AdapterRedactionEngineProxyWebServiceUnsecuredImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected Service getService()
                {
                    return mockService;
                }

            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            Service service = webProxy.getService();
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
            AdapterRedactionEngineProxyWebServiceUnsecuredImpl webProxy = new AdapterRedactionEngineProxyWebServiceUnsecuredImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected Service getService()
                {
                    return mockService;
                }
                @Override
                protected AdapterComponentRedactionEnginePortType getPort(String url, String wsAddressingAction)
                {
                    return mockPort;
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
            AdapterComponentRedactionEnginePortType port = webProxy.getPort("testurl", "testaction");
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
    public void testFilterAdhocQueryResultsHappy()
    {
        try
        {
            AdapterRedactionEngineProxyWebServiceUnsecuredImpl webProxy = new AdapterRedactionEngineProxyWebServiceUnsecuredImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AdapterComponentRedactionEnginePortType getPort(String url, String wsAddressingAction)
                {
                    return mockPort;
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
    public void testFilterAdhocQueryResultsException()
    {
        try
        {
            AdapterRedactionEngineProxyWebServiceUnsecuredImpl webProxy = new AdapterRedactionEngineProxyWebServiceUnsecuredImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AdapterComponentRedactionEnginePortType getPort(String url, String wsAddressingAction)
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
            AdapterRedactionEngineProxyWebServiceUnsecuredImpl webProxy = new AdapterRedactionEngineProxyWebServiceUnsecuredImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AdapterComponentRedactionEnginePortType getPort(String url, String wsAddressingAction)
                {
                    return mockPort;
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

    
//    @Test
//    public void testFilterRetrieveDocumentSetResultsNullResponse()
//    {
//        try
//        {
//            AdapterRedactionEngineProxyWebServiceUnsecuredImpl webProxy = new AdapterRedactionEngineProxyWebServiceUnsecuredImpl()
//            {
//                @Override
//                protected Log createLogger()
//                {
//                    return mockLog;
//                }
//                @Override
//                protected AdapterComponentRedactionEnginePortType getPort(String url, String wsAddressingAction)
//                {
//                    AdapterComponentRedactionEnginePortType port = new AdapterComponentRedactionEnginePortType()
//                    {
//                        public FilterDocQueryResultsResponseType filterDocQueryResults(FilterDocQueryResultsRequestType arg0)
//                        {
//                            throw new UnsupportedOperationException("Not supported");
//                        }
//
//                        public FilterDocRetrieveResultsResponseType filterDocRetrieveResults(FilterDocRetrieveResultsRequestType arg0)
//                        {
//                            return null;
//                        }
//                    };
//                    return port;
//                }
//
//            };
//            context.checking(new Expectations()
//            {
//                {
//                    allowing(mockLog).isDebugEnabled();
//                    allowing(mockLog).debug(with(any(String.class)));
//                    allowing(mockLog).warn("FilterDocRetrieveResultsResponseType was null.");
//                }
//            });
//
//            RetrieveDocumentSetResponseType response = webProxy.filterRetrieveDocumentSetResults(mockRequest, mockResponse);
//            assertNull("RetrieveDocumentSetResponseType should be null", response);
//        }
//        catch(Throwable t)
//        {
//            System.out.println("Error running testFilterRetrieveDocumentSetResultsNullResponse test: " + t.getMessage());
//            t.printStackTrace();
//            fail("Error running testFilterRetrieveDocumentSetResultsNullResponse test: " + t.getMessage());
//        }
//    }
//
//    @Test
//    public void testFilterRetrieveDocumentSetResultsException()
//    {
//        try
//        {
//            AdapterRedactionEngineProxyWebServiceUnsecuredImpl webProxy = new AdapterRedactionEngineProxyWebServiceUnsecuredImpl()
//            {
//                @Override
//                protected Log createLogger()
//                {
//                    return mockLog;
//                }
//                @Override
//                protected AdapterComponentRedactionEnginePortType getPort(String url, String wsAddressingAction)
//                {
//                    AdapterComponentRedactionEnginePortType port = new AdapterComponentRedactionEnginePortType()
//                    {
//                        public FilterDocQueryResultsResponseType filterDocQueryResults(FilterDocQueryResultsRequestType arg0)
//                        {
//                            throw new UnsupportedOperationException("Not supported");
//                        }
//
//                        public FilterDocRetrieveResultsResponseType filterDocRetrieveResults(FilterDocRetrieveResultsRequestType arg0)
//                        {
//                            throw new RuntimeException();
//                        }
//                    };
//                    return port;
//                }
//
//            };
//            context.checking(new Expectations()
//            {
//                {
//                    allowing(mockLog).isDebugEnabled();
//                    allowing(mockLog).debug(with(any(String.class)));
//                    allowing(mockLog).error(with(any(String.class)), with(aNonNull(RuntimeException.class)));
//                }
//            });
//
//            RetrieveDocumentSetResponseType response = webProxy.filterRetrieveDocumentSetResults(mockRequest, mockResponse);
//            assertNull("RetrieveDocumentSetResponseType should be null", response);
//        }
//        catch(Throwable t)
//        {
//            System.out.println("Error running testFilterRetrieveDocumentSetResultsException test: " + t.getMessage());
//            t.printStackTrace();
//            fail("Error running testFilterRetrieveDocumentSetResultsException test: " + t.getMessage());
//        }
//    }

}