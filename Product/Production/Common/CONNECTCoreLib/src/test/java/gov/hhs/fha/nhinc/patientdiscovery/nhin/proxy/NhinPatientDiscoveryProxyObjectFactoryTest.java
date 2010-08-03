package gov.hhs.fha.nhinc.patientdiscovery.nhin.proxy ;

import gov.hhs.fha.nhinc.patientdiscovery.nhin.proxy.*;
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

/**
 *
 * @author Neil Webb
 */
@RunWith(JMock.class)
public class NhinPatientDiscoveryProxyObjectFactoryTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    final Log mockLog = context.mock(Log.class);
    final NhinPatientDiscoveryProxy mockProxy = context.mock(NhinPatientDiscoveryProxy.class);
    final ApplicationContext appContext = new FileSystemXmlApplicationContext()
    {
        @Override
        public Object getBean(String beanName)
        {
            return mockProxy;
        }
    };

//    @Test
//    public void testGetContext()
//    {
//        try
//        {
//            final ApplicationContext mockContext = context.mock(ApplicationContext.class);
//            NhinPatientDiscoveryProxyObjectFactory proxyFactory = new NhinPatientDiscoveryProxyObjectFactory()
//            {
//                @Override
//                protected ApplicationContext createApplicationContext()
//                {
//                    return mockContext;
//                }
//                @Override
//                protected ApplicationContext getContext()
//                {
//                    return mockContext;
//                }
//            };
//            assertNotNull("ApplicationContext", proxyFactory.getContext());
//        }
//        catch(Throwable t)
//        {
//            System.out.println("Error running testGetContext test: " + t.getMessage());
//            t.printStackTrace();
//            fail("Error running testGetContext test: " + t.getMessage());
//        }
//    }

    @Test
    public void testGetConfigFileName()
    {
        try
        {
            final ApplicationContext mockContext = context.mock(ApplicationContext.class);
            NhinPatientDiscoveryProxyObjectFactory proxyFactory = new NhinPatientDiscoveryProxyObjectFactory()
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

    @Test
    public void testGetNhinPatientDiscoveryProxyHappy()
    {
        try
        {
            NhinPatientDiscoveryProxyObjectFactory proxyFactory = new NhinPatientDiscoveryProxyObjectFactory()
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
//                protected ApplicationContext createApplicationContext()
//                {
//                    return appContext;
//                }
//                @Override
//                protected ApplicationContext getContext()
//                {
//                    return appContext;
//                }
            };
            NhinPatientDiscoveryProxy proxy = proxyFactory.getNhinPatientDiscoveryProxy();
            assertNotNull("NhinPatientDiscoveryProxy was null", proxy);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetNhinPatientDiscoveryProxyHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetNhinPatientDiscoveryProxyHappy test: " + t.getMessage());
        }
    }

//    @Test
//    public void testGetNhinPatientDiscoveryProxyNullContext()
//    {
//        try
//        {
//            NhinPatientDiscoveryProxyObjectFactory proxyFactory = new NhinPatientDiscoveryProxyObjectFactory()
//            {
//                @Override
//                protected ApplicationContext createApplicationContext()
//                {
//                    return null;
//                }
//                @Override
//                protected ApplicationContext getContext()
//                {
//                    return null;
//                }
//            };
//            NhinPatientDiscoveryProxy proxy = proxyFactory.getNhinPatientDiscoveryProxy();
//            assertNull("NhinPatientDiscoveryProxy was not null", proxy);
//        }
//        catch(Throwable t)
//        {
//            System.out.println("Error running testGetNhinPatientDiscoveryProxyNullContext test: " + t.getMessage());
//            t.printStackTrace();
//            fail("Error running testGetNhinPatientDiscoveryProxyNullContext test: " + t.getMessage());
//        }
//    }

}