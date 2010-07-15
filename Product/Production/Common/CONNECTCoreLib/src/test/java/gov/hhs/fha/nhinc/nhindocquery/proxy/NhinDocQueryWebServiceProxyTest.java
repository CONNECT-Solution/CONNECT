package gov.hhs.fha.nhinc.nhindocquery.proxy;

import ihe.iti.xds_b._2007.RespondingGatewayQueryPortType;
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
public class NhinDocQueryWebServiceProxyTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    final Log mockLog = context.mock(Log.class);
    final Service mockService = context.mock(Service.class);
    final RespondingGatewayQueryPortType mockPort = context.mock(RespondingGatewayQueryPortType.class);

    @Test
    public void testCreateLogger()
    {
        try
        {
            NhinDocQueryWebServiceProxy sut = new NhinDocQueryWebServiceProxy()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
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
    public void testGetService()
    {
        try
        {
            NhinDocQueryWebServiceProxy sut = new NhinDocQueryWebServiceProxy()
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
            Service service = sut.getService();
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
    public void testSetEndpointAddressHappy()
    {
        try
        {
            NhinDocQueryWebServiceProxy sut = new NhinDocQueryWebServiceProxy()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected void setEndpointAddress(RespondingGatewayQueryPortType port, String url)
                {
                    // Do nothing
                }
            };
            RespondingGatewayQueryPortType port = mockPort;
            String url = "url";
            sut.setEndpointAddress(port, url);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testSetEndpointAddressHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testSetEndpointAddressHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testSetEndpointAddressNullPort()
    {
        try
        {
            NhinDocQueryWebServiceProxy sut = new NhinDocQueryWebServiceProxy()
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
                    oneOf(mockLog).error("Port was null - not setting endpoint address.");
                }
            });
            RespondingGatewayQueryPortType port = null;
            String url = "url";
            sut.setEndpointAddress(port, url);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testSetEndpointAddressNullPort test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testSetEndpointAddressNullPort test: " + t.getMessage());
        }
    }

    @Test
    public void testSetEndpointAddressNullURL()
    {
        try
        {
            NhinDocQueryWebServiceProxy sut = new NhinDocQueryWebServiceProxy()
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
                    oneOf(mockLog).error("URL was null or empty - not setting endpoint address.");
                }
            });
            RespondingGatewayQueryPortType port = mockPort;
            String url = null;
            sut.setEndpointAddress(port, url);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testSetEndpointAddressNullURL test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testSetEndpointAddressNullURL test: " + t.getMessage());
        }
    }

    @Test
    public void testSetEndpointAddressEmptyURL()
    {
        try
        {
            NhinDocQueryWebServiceProxy sut = new NhinDocQueryWebServiceProxy()
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
                    oneOf(mockLog).error("URL was null or empty - not setting endpoint address.");
                }
            });
            RespondingGatewayQueryPortType port = mockPort;
            String url = "";
            sut.setEndpointAddress(port, url);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testSetEndpointAddressEmptyURL test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testSetEndpointAddressEmptyURL test: " + t.getMessage());
        }
    }

    @Test
    public void testGetPortNullService()
    {
        try
        {
            NhinDocQueryWebServiceProxy sut = new NhinDocQueryWebServiceProxy()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
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
            RespondingGatewayQueryPortType port = sut.getPort(url);
            assertNull("Port was not null", port);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetPortNullService test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetPortNullService test: " + t.getMessage());
        }
    }

}