package gov.hhs.fha.nhinc.redaction.proxy;

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
public class RedactionEngineProxyFactoryTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final RedactionEngineProxy mockProxy = context.mock(RedactionEngineProxy.class);
    final ApplicationContext appContext = new FileSystemXmlApplicationContext()
    {
        @Override
        public Object getBean(String beanName)
        {
            return mockProxy;
        }
    };

    @Test
    public void testGetRedactionEngineProxyNullContext()
    {
        try
        {
            RedactionEngineProxyFactory proxyFactory = new RedactionEngineProxyFactory()
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
            RedactionEngineProxy proxy = proxyFactory.getRedactionEngineProxy();
            assertNull("RedactionEngineProxy was not null", proxy);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetRedactionEngineProxyNullContext test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetRedactionEngineProxyNullContext test: " + t.getMessage());
        }
    }

    @Test
    public void testGetRedactionEngineProxyHappy()
    {
        try
        {
            RedactionEngineProxyFactory proxyFactory = new RedactionEngineProxyFactory()
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
            RedactionEngineProxy proxy = proxyFactory.getRedactionEngineProxy();
            assertNotNull("RedactionEngineProxy was null", proxy);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetRedactionEngineProxyHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetRedactionEngineProxyHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testGetContext()
    {
        try
        {
            final ApplicationContext mockContext = context.mock(ApplicationContext.class);
            RedactionEngineProxyFactory proxyFactory = new RedactionEngineProxyFactory()
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

}