package gov.hhs.fha.nhinc.docretrieve.passthru.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhincproxydocretrieve.NhincProxyDocRetrievePortType;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import javax.xml.ws.Service;
import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 *
 * @author Neil Webb
 */
@RunWith(JMock.class)
public class PassthruDocRetrieveProxyWebServiceUnsecuredImplTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);
    final RetrieveDocumentSetRequestType mockDockRetrieveRequest = context.mock(RetrieveDocumentSetRequestType.class);
    final AssertionType mockAssertion = context.mock(AssertionType.class);
    final NhinTargetSystemType mockTargetSystem = context.mock(NhinTargetSystemType.class);
    final NhincProxyDocRetrievePortType mockPort = context.mock(NhincProxyDocRetrievePortType.class);
    final Service mockService = context.mock(Service.class);
    final WebServiceProxyHelper mockWebServiceProxyHelper = context.mock(WebServiceProxyHelper.class);

    @Test
    public void testCreateLogger()
    {
        try
        {
            PassthruDocRetrieveProxyWebServiceUnsecuredImpl sut = new PassthruDocRetrieveProxyWebServiceUnsecuredImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected WebServiceProxyHelper createWebServiceProxyHelper()
                {
                    return mockWebServiceProxyHelper;
                }
            };
            Log log = sut.createLogger();
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
    public void testCreateWebServiceProxyHelper()
    {
        try
        {
            PassthruDocRetrieveProxyWebServiceUnsecuredImpl sut = new PassthruDocRetrieveProxyWebServiceUnsecuredImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected WebServiceProxyHelper createWebServiceProxyHelper()
                {
                    return mockWebServiceProxyHelper;
                }
            };
            WebServiceProxyHelper wsProxyHelper = sut.createWebServiceProxyHelper();
            assertNotNull("WebServiceProxyHelper was null", wsProxyHelper);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testCreateWebServiceProxyHelper test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCreateWebServiceProxyHelper test: " + t.getMessage());
        }
    }

    @Test
    public void testGetPortNullService()
    {
        try
        {
            PassthruDocRetrieveProxyWebServiceUnsecuredImpl sut = new PassthruDocRetrieveProxyWebServiceUnsecuredImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected WebServiceProxyHelper createWebServiceProxyHelper()
                {
                    return mockWebServiceProxyHelper;
                }
                @Override
                protected Service getService()
                {
                    return null;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockLog).error("Unable to obtain serivce - no port created.");
                }
            });
            String url = "url";
            String wsAddressingAction = "";
            NhincProxyDocRetrievePortType port = sut.getPort(url, wsAddressingAction, mockAssertion);
            assertNull("Port was not null", port);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetPortNullService test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetPortNullService test: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayCrossGatewayRetrieveHappy()
    {
        try
        {
            final RetrieveDocumentSetResponseType mockResponse = context.mock(RetrieveDocumentSetResponseType.class);
            final WebServiceProxyHelper wsProxyHelper = new WebServiceProxyHelper()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                public String getUrlLocalHomeCommunity(String serviceName)
                {
                    return "url";
                }
                @Override
                public Object invokePort(Object portObject, Class portClass, String methodName, Object operationInput)
                    throws Exception
                {
                    return mockResponse;
                }
            };
            PassthruDocRetrieveProxyWebServiceUnsecuredImpl webProxy = new PassthruDocRetrieveProxyWebServiceUnsecuredImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected WebServiceProxyHelper createWebServiceProxyHelper()
                {
                    return wsProxyHelper;
                }
                @Override
                protected NhincProxyDocRetrievePortType getPort(String url, String wsAddressingAction, AssertionType assertion)
                {
                    return mockPort;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            RetrieveDocumentSetResponseType response = webProxy.respondingGatewayCrossGatewayRetrieve(mockDockRetrieveRequest, mockAssertion, mockTargetSystem);
            assertNotNull("RetrieveDocumentSetResponseType was null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRespondingGatewayCrossGatewayRetrieveHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayCrossGatewayRetrieveHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayCrossGatewayRetrieveNullUrl()
    {
        try
        {
            final WebServiceProxyHelper wsProxyHelper = new WebServiceProxyHelper()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                public String getUrlLocalHomeCommunity(String serviceName)
                {
                    return null;
                }
            };
            PassthruDocRetrieveProxyWebServiceUnsecuredImpl webProxy = new PassthruDocRetrieveProxyWebServiceUnsecuredImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected WebServiceProxyHelper createWebServiceProxyHelper()
                {
                    return wsProxyHelper;
                }
                @Override
                protected NhincProxyDocRetrievePortType getPort(String url, String wsAddressingAction, AssertionType assertion)
                {
                    return mockPort;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockLog).error("Failed to call the web service (" + NhincConstants.NHINC_PROXY_DOC_RETRIEVE_SERVICE_NAME + ").  The URL is null.");
                }
            });
            RetrieveDocumentSetResponseType response = webProxy.respondingGatewayCrossGatewayRetrieve(mockDockRetrieveRequest, mockAssertion, mockTargetSystem);
            assertNull("RetrieveDocumentSetResponseType was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRespondingGatewayCrossGatewayRetrieveNullUrl test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayCrossGatewayRetrieveNullUrl test: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayCrossGatewayRetrieveNullRequest()
    {
        try
        {
            final RetrieveDocumentSetResponseType mockResponse = context.mock(RetrieveDocumentSetResponseType.class);
            final WebServiceProxyHelper wsProxyHelper = new WebServiceProxyHelper()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                public String getUrlLocalHomeCommunity(String serviceName)
                {
                    return "url";
                }
                @Override
                public Object invokePort(Object portObject, Class portClass, String methodName, Object operationInput)
                    throws Exception
                {
                    return mockResponse;
                }
            };
            PassthruDocRetrieveProxyWebServiceUnsecuredImpl webProxy = new PassthruDocRetrieveProxyWebServiceUnsecuredImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected WebServiceProxyHelper createWebServiceProxyHelper()
                {
                    return wsProxyHelper;
                }
                @Override
                protected NhincProxyDocRetrievePortType getPort(String url, String wsAddressingAction, AssertionType assertion)
                {
                    return mockPort;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            RetrieveDocumentSetResponseType response = webProxy.respondingGatewayCrossGatewayRetrieve(null, mockAssertion, mockTargetSystem);
            assertNotNull("RetrieveDocumentSetResponseType was null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRespondingGatewayCrossGatewayRetrieveNullRequest test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayCrossGatewayRetrieveNullRequest test: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayCrossGatewayRetrieveException()
    {
        try
        {
            final WebServiceProxyHelper wsProxyHelper = new WebServiceProxyHelper()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                public String getUrlLocalHomeCommunity(String serviceName)
                {
                    return "url";
                }
                @Override
                public Object invokePort(Object portObject, Class portClass, String methodName, Object operationInput)
                    throws Exception
                {
                    throw new IllegalArgumentException("Thrown Exception");
                }
            };
            PassthruDocRetrieveProxyWebServiceUnsecuredImpl webProxy = new PassthruDocRetrieveProxyWebServiceUnsecuredImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected WebServiceProxyHelper createWebServiceProxyHelper()
                {
                    return wsProxyHelper;
                }
                @Override
                protected NhincProxyDocRetrievePortType getPort(String url, String wsAddressingAction, AssertionType assertion)
                {
                    return mockPort;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockLog).error("Error: Failed to retrieve url for service: " + NhincConstants.NHINC_PROXY_DOC_RETRIEVE_SERVICE_NAME + " for local home community");
                    oneOf(mockLog).error(with(aNonNull(String.class)), with(aNonNull(IllegalArgumentException.class)));
                }
            });
            RetrieveDocumentSetResponseType response = webProxy.respondingGatewayCrossGatewayRetrieve(mockDockRetrieveRequest, mockAssertion, mockTargetSystem);
            assertNull("RetrieveDocumentSetResponseType was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRespondingGatewayCrossGatewayRetrieveException test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayCrossGatewayRetrieveException test: " + t.getMessage());
        }
    }

}