package gov.hhs.fha.nhinc.patientdiscovery.entity.proxy ;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.entitypatientdiscovery.EntityPatientDiscovery;
import gov.hhs.fha.nhinc.entitypatientdiscovery.EntityPatientDiscoveryPortType;
import org.apache.commons.logging.Log;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
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
    final EntityPatientDiscovery mockEntityPatientDiscovery = context.mock(EntityPatientDiscovery.class);
    final EntityPatientDiscoveryPortType mockPort = context.mock(EntityPatientDiscoveryPortType.class);

    @Test
    public void testCreateLogger()
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
            EntityPatientDiscoveryProxyWebServiceUnsecuredImpl webProxy = new EntityPatientDiscoveryProxyWebServiceUnsecuredImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
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
    public void testConfigurePortNullPort()
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
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockLog).warn("configurePort - Port was null.");
                }
            });
            webProxy.configurePort(null, "endpoint_url");
        }
        catch(Throwable t)
        {
            System.out.println("Error running testConfigurePortNullPort test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testConfigurePortNullPort test: " + t.getMessage());
        }
    }

    @Test
    public void testConfigurePortNullEndpointURL()
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
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockLog).warn("configurePort - Endpoint URL was null.");
                }
            });
            webProxy.configurePort(mockPort, null);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testConfigurePortNullEndpointURL test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testConfigurePortNullEndpointURL test: " + t.getMessage());
        }
    }


















    @Test
    public void testGetEntityPatientDiscoveryHappy()
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
                protected EntityPatientDiscovery getEntityPatientDiscovery()
                {
                    return mockEntityPatientDiscovery;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            EntityPatientDiscovery service = webProxy.getEntityPatientDiscovery();
            assertNotNull("EntityPatientDiscoverySecured was null", service);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetEntityPatientDiscoverySecuredHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetEntityPatientDiscoverySecuredHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testGetEntityPatientDiscoverySecuredPortTypeHappy()
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
                protected EntityPatientDiscovery getEntityPatientDiscovery()
                {
                    return mockEntityPatientDiscovery;
                }
                @Override
                protected String getEndpointURL()
                {
                    return "endpoint_url";
                }
                @Override
                protected String invokeConnectionManager(String serviceName) throws ConnectionManagerException
                {
                    return "test_endpoint";
                }
                @Override
                protected void configurePort(EntityPatientDiscoveryPortType port, String endpointURL)
                {

                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockEntityPatientDiscovery).getEntityPatientDiscoveryPortSoap();
                }
            });
            EntityPatientDiscoveryPortType port = webProxy.getEntityPatientDiscoveryPortType();
            assertNotNull("EntityPatientDiscoverySecuredPortType was null", port);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetEntityPatientDiscoverySecuredPortTypeHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetEntityPatientDiscoverySecuredPortTypeHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testGetEntityPatientDiscoverySecuredPortTypeNullEndpointURL()
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
                protected EntityPatientDiscovery getEntityPatientDiscovery()
                {
                    return mockEntityPatientDiscovery;
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
                    oneOf(mockLog).warn("Endpoint url was missing.");
                }
            });
            EntityPatientDiscoveryPortType port = webProxy.getEntityPatientDiscoveryPortType();
            assertNull("AdapterComponentRedactionEnginePortType was not null", port);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetEntityPatientDiscoverySecuredPortTypeNullEndpointURL test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetEntityPatientDiscoverySecuredPortTypeNullEndpointURL test: " + t.getMessage());
        }
    }

    @Test
    public void testGetEntityPatientDiscoverySecuredPortTypeEmptyEndpointURL()
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
                protected EntityPatientDiscovery getEntityPatientDiscovery()
                {
                    return mockEntityPatientDiscovery;
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
                    oneOf(mockLog).warn("Endpoint url was missing.");
                }
            });
            EntityPatientDiscoveryPortType port = webProxy.getEntityPatientDiscoveryPortType();
            assertNull("AdapterComponentRedactionEnginePortType was not null", port);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetEntityPatientDiscoverySecuredPortTypeEmptyEndpointURL test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetEntityPatientDiscoverySecuredPortTypeEmptyEndpointURL test: " + t.getMessage());
        }
    }

    @Test
    public void testGetEntityPatientDiscoverySecuredPortTypeNullService()
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
                protected EntityPatientDiscovery getEntityPatientDiscovery()
                {
                    return null;
                }
                @Override
                protected String getEndpointURL()
                {
                    return "endpoint_url";
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
                    oneOf(mockLog).warn("EntityPatientDiscoverySecured was null");
                }
            });
            EntityPatientDiscoveryPortType port = webProxy.getEntityPatientDiscoveryPortType();
            assertNull("AdapterComponentRedactionEnginePortType was not null", port);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetEntityPatientDiscoverySecuredPortTypeNullService test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetEntityPatientDiscoverySecuredPortTypeNullService test: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayPRPAIN201305UV02Happy()
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
                protected EntityPatientDiscoveryPortType getEntityPatientDiscoveryPortType()
                {
                    return mockPort;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockPort).respondingGatewayPRPAIN201305UV02(with(aNonNull(RespondingGatewayPRPAIN201305UV02RequestType.class)));
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
                protected EntityPatientDiscoveryPortType getEntityPatientDiscoveryPortType()
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
                protected EntityPatientDiscoveryPortType getEntityPatientDiscoveryPortType()
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
                protected EntityPatientDiscoveryPortType getEntityPatientDiscoveryPortType()
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
                protected EntityPatientDiscoveryPortType getEntityPatientDiscoveryPortType()
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