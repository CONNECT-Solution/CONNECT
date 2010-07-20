package gov.hhs.fha.nhinc.docquery.proxy;

import gov.hhs.fha.nhinc.nhincproxydocquerysecured.NhincProxyDocQuerySecuredPortType;
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
public class NhincProxyDocQueryImplTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    final Log mockLog = context.mock(Log.class);
    final Service mockService = context.mock(Service.class);
    final NhincProxyDocQuerySecuredPortType mockPort = context.mock(NhincProxyDocQuerySecuredPortType.class);

    @Test
    public void testCreateLogger()
    {
        try
        {
            NhincProxyDocQueryImpl sut = new NhincProxyDocQueryImpl()
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
            NhincProxyDocQueryImpl sut = new NhincProxyDocQueryImpl()
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
            NhincProxyDocQueryImpl sut = new NhincProxyDocQueryImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected void setEndpointAddress(NhincProxyDocQuerySecuredPortType port, String url)
                {
                    // Do nothing
                }
            };
            NhincProxyDocQuerySecuredPortType port = mockPort;
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
            NhincProxyDocQueryImpl sut = new NhincProxyDocQueryImpl()
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
            NhincProxyDocQuerySecuredPortType port = null;
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
            NhincProxyDocQueryImpl sut = new NhincProxyDocQueryImpl()
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
            NhincProxyDocQuerySecuredPortType port = mockPort;
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
            NhincProxyDocQueryImpl sut = new NhincProxyDocQueryImpl()
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
            NhincProxyDocQuerySecuredPortType port = mockPort;
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
            NhincProxyDocQueryImpl sut = new NhincProxyDocQueryImpl()
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
            NhincProxyDocQuerySecuredPortType port = sut.getPort(url);
            assertNull("NhincProxyDocQuerySecuredPortType was not null", port);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetPortNullService test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetPortNullService test: " + t.getMessage());
        }
    }

}