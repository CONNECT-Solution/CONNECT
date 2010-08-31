/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientdiscovery.passthru.proxy;

import org.apache.commons.logging.Log;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import static org.junit.Assert.*;
import gov.hhs.fha.nhinc.patientdiscovery.passthru.proxy.PassthruPatientDiscoveryProxy;

/**
 *
 * @author mflynn02
 */
public class NhincPatientDiscoveryProxyObjectFactoryTest {

    Mockery context = new JUnit4Mockery() {

        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);
    final PassthruPatientDiscoveryProxy mockProxy = context.mock(PassthruPatientDiscoveryProxy.class);
    final ApplicationContext appContext = new FileSystemXmlApplicationContext() {

        @Override
        public Object getBean(String beanName) {
            return mockProxy;
        }
    };


    /**
     * Test of getConfigFileName method, of class NhincPatientDiscoveryProxyObjectFactory.
     */
    @Test
    public void testGetConfigFileName()
    {
        try
        {
            final ApplicationContext mockContext = context.mock(ApplicationContext.class);
            PassthruPatientDiscoveryProxyObjectFactory proxyFactory = new PassthruPatientDiscoveryProxyObjectFactory()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected String getConfigFileName()
                {
                    return "TEST_CONFIG_FILE_NAME";
                }
                @Override
                protected ApplicationContext getContext()
                {
                    return mockContext;
                }
            };
            assertEquals("Config file name", "TEST_CONFIG_FILE_NAME", proxyFactory.getConfigFileName());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetConfigFileName test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetConfigFileName test: " + t.getMessage());
        }
    }

    /**
     * Test of getNhincPatientDiscoveryProxy method, of class NhincPatientDiscoveryProxyObjectFactory.
     */
    @Test
    public void testGetNhincPatientDiscoveryProxyHappy()     {
        try
        {
            PassthruPatientDiscoveryProxyObjectFactory proxyFactory = new PassthruPatientDiscoveryProxyObjectFactory()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected <T extends Object> T getBean(String beanName, Class<T> type)
                {
                    return type.cast(mockProxy);
                }
            };
            PassthruPatientDiscoveryProxy proxy = proxyFactory.getNhincPatientDiscoveryProxy();
            assertNotNull("NhincPatientDiscoveryProxy was null", proxy);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetNhincPatientDiscoveryProxyHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetNhincPatientDiscoveryProxyHappy test: " + t.getMessage());
        }
    }

}
