package gov.hhs.fha.nhinc.patientdiscovery.entity.proxy;

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
public class EntityPatientDiscoveryProxyObjectFactoryTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    final Log mockLog = context.mock(Log.class);
    final EntityPatientDiscoveryProxy mockProxy = context.mock(EntityPatientDiscoveryProxy.class);
    final ApplicationContext appContext = new FileSystemXmlApplicationContext()
    {
        @Override
        public Object getBean(String beanName)
        {
            return mockProxy;
        }
    };

    @Test
    public void testGetConfigFileName()
    {
        try
        {
            final ApplicationContext mockContext = context.mock(ApplicationContext.class);
            EntityPatientDiscoveryProxyObjectFactory proxyFactory = new EntityPatientDiscoveryProxyObjectFactory()
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
    public void testGetEntityPatientDiscoveryProxyHappy()
    {
        try
        {
            EntityPatientDiscoveryProxyObjectFactory proxyFactory = new EntityPatientDiscoveryProxyObjectFactory()
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
            EntityPatientDiscoveryProxy proxy = proxyFactory.getEntityPatientDiscoveryProxy();
            assertNotNull("EntityPatientDiscoveryProxy was null", proxy);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetEntityPatientDiscoveryProxyHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetEntityPatientDiscoveryProxyHappy test: " + t.getMessage());
        }
    }

}