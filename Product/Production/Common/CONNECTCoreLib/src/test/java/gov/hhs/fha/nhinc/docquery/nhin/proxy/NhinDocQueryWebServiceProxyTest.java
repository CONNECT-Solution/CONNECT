package gov.hhs.fha.nhinc.docquery.nhin.proxy;

import gov.hhs.fha.nhinc.docquery.nhin.proxy.NhinDocQueryProxyWebServiceSecuredImpl;
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
            NhinDocQueryProxyWebServiceSecuredImpl sut = new NhinDocQueryProxyWebServiceSecuredImpl()
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
            NhinDocQueryProxyWebServiceSecuredImpl sut = new NhinDocQueryProxyWebServiceSecuredImpl()
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
    public void testGetPortNullService()
    {
        try
        {
            NhinDocQueryProxyWebServiceSecuredImpl sut = new NhinDocQueryProxyWebServiceSecuredImpl()
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
            RespondingGatewayQueryPortType port = sut.getPort(url, "", null, null);
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