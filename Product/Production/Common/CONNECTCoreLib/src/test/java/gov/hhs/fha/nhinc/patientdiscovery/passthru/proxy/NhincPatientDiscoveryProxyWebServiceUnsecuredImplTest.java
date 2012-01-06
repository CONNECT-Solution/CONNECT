/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.passthru.proxy;

import gov.hhs.fha.nhinc.nhincproxypatientdiscovery.NhincProxyPatientDiscoveryPortType;
import javax.xml.ws.Service;
import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mflynn02
 */
public class NhincPatientDiscoveryProxyWebServiceUnsecuredImplTest {
   Mockery context = new JUnit4Mockery()
    {


        {
             setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);
    final Service mockService = context.mock(Service.class);

    /**
     * Test of createLogger method, of class PassthruPatientDiscoveryProxyWebServiceUnsecuredImpl.
     */
    @Test
    public void testCreateLogger() {
        try
        {
            PassthruPatientDiscoveryProxyWebServiceUnsecuredImpl sut = new PassthruPatientDiscoveryProxyWebServiceUnsecuredImpl()
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


    /**
     * Test of getService method, of class PassthruPatientDiscoveryProxyWebServiceUnsecuredImpl.
     */
    @Test
    public void testGetService() {
        try
        {
            PassthruPatientDiscoveryProxyWebServiceUnsecuredImpl sut = new PassthruPatientDiscoveryProxyWebServiceUnsecuredImpl()
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

    /**
     * Test of getPort method, of class PassthruPatientDiscoveryProxyWebServiceUnsecuredImpl.
     */
    @Test
    public void testGetPortNullService()
    {
        try
        {
            PassthruPatientDiscoveryProxyWebServiceUnsecuredImpl sut = new PassthruPatientDiscoveryProxyWebServiceUnsecuredImpl()
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
            NhincProxyPatientDiscoveryPortType port = sut.getPort(url, "", "", null);
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