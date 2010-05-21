package gov.hhs.fha.nhinc.lift.proxy;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.context.ApplicationContext;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Test the GatewayLiftManagerProxyObjectFactory class.
 *
 * @author Les Westberg
 */
@RunWith(JMock.class)
public class GatewayLiftManagerProxyObjectFactoryTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    final GatewayLiftManagerProxy mockProxy = context.mock(GatewayLiftManagerProxy.class);
    final ApplicationContext appContext = new FileSystemXmlApplicationContext()
    {
        @Override
        public Object getBean(String beanName)
        {
            return mockProxy;
        }
    };

    /**
     * Test the getContext method.
     */
    @Test
    public void testGetContext()
    {
        try
        {
            final ApplicationContext mockContext = context.mock(ApplicationContext.class);
            GatewayLiftManagerProxyObjectFactory proxyFactory = new GatewayLiftManagerProxyObjectFactory()
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

    /**
     * Test the getGatewayLiftManagerProxy method with a happy path case.
     */
    @Test
    public void testGetGatewayLiftManagerProxyHappy()
    {
        try
        {
            GatewayLiftManagerProxyObjectFactory proxyFactory = new GatewayLiftManagerProxyObjectFactory()
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
            GatewayLiftManagerProxy proxy = proxyFactory.getGatewayLiftManagerProxy();
            assertNotNull("GatewayLiftManagerProxy was null", proxy);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetGatewayLiftManagerProxyHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetGatewayLiftManagerProxyHappy test: " + t.getMessage());
        }
    }

    /**
     * Test the getGatewayLiftManagerProxy method where the expected result should be null.
     */
    @Test
    public void testGetGatewayLiftManagerProxyNullContext()
    {
        try
        {
            GatewayLiftManagerProxyObjectFactory proxyFactory = new GatewayLiftManagerProxyObjectFactory()
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
            GatewayLiftManagerProxy proxy = proxyFactory.getGatewayLiftManagerProxy();
            assertNull("GatewayLiftManagerProxy was not null", proxy);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetGatewayLiftManagerProxyNullContext test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetGatewayLiftManagerProxyNullContext test: " + t.getMessage());
        }
    }

}
