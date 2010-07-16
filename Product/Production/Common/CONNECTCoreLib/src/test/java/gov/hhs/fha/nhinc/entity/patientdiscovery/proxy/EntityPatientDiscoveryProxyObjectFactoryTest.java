package gov.hhs.fha.nhinc.entity.patientdiscovery.proxy;

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
    public void testGetContext()
    {
        try
        {
            final ApplicationContext mockContext = context.mock(ApplicationContext.class);
            EntityPatientDiscoveryProxyObjectFactory proxyFactory = new EntityPatientDiscoveryProxyObjectFactory()
            {
                @Override
                protected ApplicationContext createApplicationContext()
                {
                    return mockContext;
                }
                @Override
                protected ApplicationContext getContext()
                {
                    return mockContext;
                }
            };
            assertNotNull("ApplicationContext", proxyFactory.getContext());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetContext test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetContext test: " + t.getMessage());
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
                protected ApplicationContext createApplicationContext()
                {
                    return appContext;
                }
                @Override
                protected ApplicationContext getContext()
                {
                    return appContext;
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

    @Test
    public void testGetEntityPatientDiscoveryProxyNullContext()
    {
        try
        {
            EntityPatientDiscoveryProxyObjectFactory proxyFactory = new EntityPatientDiscoveryProxyObjectFactory()
            {
                @Override
                protected ApplicationContext createApplicationContext()
                {
                    return null;
                }
                @Override
                protected ApplicationContext getContext()
                {
                    return null;
                }
            };
            EntityPatientDiscoveryProxy proxy = proxyFactory.getEntityPatientDiscoveryProxy();
            assertNull("EntityPatientDiscoveryProxy was not null", proxy);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetEntityPatientDiscoveryProxyNullContext test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetEntityPatientDiscoveryProxyNullContext test: " + t.getMessage());
        }
    }

}