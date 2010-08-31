package gov.hhs.fha.nhinc.patientdiscovery.entity.proxy ;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.entitypatientdiscovery.EntityPatientDiscoveryPortType;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import javax.xml.ws.Service;
import org.apache.commons.logging.Log;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;
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
public class EntityPatientDiscoveryProxyWebServiceUnsecuredImplTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);
    final PRPAIN201305UV02 mockPdRequest = context.mock(PRPAIN201305UV02.class);
    final AssertionType mockAssertion = context.mock(AssertionType.class);
    final NhinTargetCommunitiesType mockTargetCommunities = context.mock(NhinTargetCommunitiesType.class);
    final EntityPatientDiscoveryPortType mockPort = context.mock(EntityPatientDiscoveryPortType.class);
    final Service mockService = context.mock(Service.class);
    final WebServiceProxyHelper mockWebServiceProxyHelper = context.mock(WebServiceProxyHelper.class);

    @Test
    public void testCreateLogger()
    {
        try
        {
            EntityPatientDiscoveryProxyWebServiceUnsecuredImpl sut = new EntityPatientDiscoveryProxyWebServiceUnsecuredImpl()
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
            EntityPatientDiscoveryProxyWebServiceUnsecuredImpl sut = new EntityPatientDiscoveryProxyWebServiceUnsecuredImpl()
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
    public void testInvokeConnectionManagerHappy()
    {
        try
        {
            EntityPatientDiscoveryProxyWebServiceUnsecuredImpl webProxy = new EntityPatientDiscoveryProxyWebServiceUnsecuredImpl()
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
        EntityPatientDiscoveryProxyWebServiceUnsecuredImpl webProxy = new EntityPatientDiscoveryProxyWebServiceUnsecuredImpl()
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
            EntityPatientDiscoveryProxyWebServiceUnsecuredImpl webProxy = new EntityPatientDiscoveryProxyWebServiceUnsecuredImpl()
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
            EntityPatientDiscoveryProxyWebServiceUnsecuredImpl webProxy = new EntityPatientDiscoveryProxyWebServiceUnsecuredImpl()
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
    public void testGetService()
    {
        try
        {
            EntityPatientDiscoveryProxyWebServiceUnsecuredImpl webProxy = new EntityPatientDiscoveryProxyWebServiceUnsecuredImpl()
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
            assertNotNull("Service was null", service);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetService test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetService test: " + t.getMessage());
        }
    }

    @Test
    public void testGetPortNullService()
    {
        try
        {
            EntityPatientDiscoveryProxyWebServiceUnsecuredImpl sut = new EntityPatientDiscoveryProxyWebServiceUnsecuredImpl()
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
            EntityPatientDiscoveryPortType port = sut.getPort(url, "action", "action", mockAssertion);
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
    public void testRespondingGatewayPRPAIN201305UV02Happy()
    {
        try
        {
            final RespondingGatewayPRPAIN201306UV02ResponseType mockResponse = context.mock(RespondingGatewayPRPAIN201306UV02ResponseType.class);
            final WebServiceProxyHelper wsProxyHelper = new WebServiceProxyHelper()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                public Object invokePort(Object portObject, Class portClass, String methodName, Object operationInput)
                    throws Exception
                {
                    return mockResponse;
                }
            };
            EntityPatientDiscoveryProxyWebServiceUnsecuredImpl webProxy = new EntityPatientDiscoveryProxyWebServiceUnsecuredImpl()
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
                protected String getEndpointURL()
                {
                    return "";
                }
                @Override
                protected EntityPatientDiscoveryPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion)
                {
                    return mockPort;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            RespondingGatewayPRPAIN201306UV02ResponseType response = webProxy.respondingGatewayPRPAIN201305UV02(mockPdRequest, mockAssertion, mockTargetCommunities);
            assertNotNull("RespondingGatewayPRPAIN201306UV02ResponseType was null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRespondingGatewayPRPAIN201305UV02Happy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayPRPAIN201305UV02Happy test: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayPRPAIN201305UV02NullPRPAIN201305UV02()
    {
        try
        {
            EntityPatientDiscoveryProxyWebServiceUnsecuredImpl webProxy = new EntityPatientDiscoveryProxyWebServiceUnsecuredImpl()
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
                protected String getEndpointURL()
                {
                    return "";
                }
                @Override
                protected EntityPatientDiscoveryPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion)
                {
                    return mockPort;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockLog).error("PRPAIN201305UV02 was null");
                }
            });
            RespondingGatewayPRPAIN201306UV02ResponseType response = webProxy.respondingGatewayPRPAIN201305UV02(null, mockAssertion, mockTargetCommunities);
            assertNull("RespondingGatewayPRPAIN201306UV02ResponseType was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRespondingGatewayPRPAIN201305UV02NullPRPAIN201305UV02 test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayPRPAIN201305UV02NullPRPAIN201305UV02 test: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayPRPAIN201305UV02NullAssertionType()
    {
        try
        {
            EntityPatientDiscoveryProxyWebServiceUnsecuredImpl webProxy = new EntityPatientDiscoveryProxyWebServiceUnsecuredImpl()
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
                protected String getEndpointURL()
                {
                    return "";
                }
                @Override
                protected EntityPatientDiscoveryPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion)
                {
                    return mockPort;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockLog).error("AssertionType was null");
                }
            });
            RespondingGatewayPRPAIN201306UV02ResponseType response = webProxy.respondingGatewayPRPAIN201305UV02(mockPdRequest, null, mockTargetCommunities);
            assertNull("RespondingGatewayPRPAIN201306UV02ResponseType was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRespondingGatewayPRPAIN201305UV02NullAssertionType test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayPRPAIN201305UV02NullAssertionType test: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayPRPAIN201305UV02NullNhinTargetCommunitiesType()
    {
        try
        {
            EntityPatientDiscoveryProxyWebServiceUnsecuredImpl webProxy = new EntityPatientDiscoveryProxyWebServiceUnsecuredImpl()
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
                protected String getEndpointURL()
                {
                    return "";
                }
                @Override
                protected EntityPatientDiscoveryPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion)
                {
                    return mockPort;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockLog).error("NhinTargetCommunitiesType was null");
                }
            });
            RespondingGatewayPRPAIN201306UV02ResponseType response = webProxy.respondingGatewayPRPAIN201305UV02(mockPdRequest, mockAssertion, null);
            assertNull("RespondingGatewayPRPAIN201306UV02ResponseType was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRespondingGatewayPRPAIN201305UV02NullNhinTargetCommunitiesType test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayPRPAIN201305UV02NullNhinTargetCommunitiesType test: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayPRPAIN201305UV02NullPort()
    {
        try
        {
            EntityPatientDiscoveryProxyWebServiceUnsecuredImpl webProxy = new EntityPatientDiscoveryProxyWebServiceUnsecuredImpl()
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
                protected String getEndpointURL()
                {
                    return "";
                }
                @Override
                protected EntityPatientDiscoveryPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion)
                {
                    return null;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockLog).error("EntityPatientDiscoverySecuredPortType was null");
                }
            });
            RespondingGatewayPRPAIN201306UV02ResponseType response = webProxy.respondingGatewayPRPAIN201305UV02(mockPdRequest, mockAssertion, mockTargetCommunities);
            assertNull("RespondingGatewayPRPAIN201306UV02ResponseType was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRespondingGatewayPRPAIN201305UV02NullPort test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayPRPAIN201305UV02NullPort test: " + t.getMessage());
        }
    }

}