package gov.hhs.fha.nhinc.docretrieve.passthru.proxy;

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
public class PassthruDocRetrieveProxyObjectFactoryTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    final Log mockLog = context.mock(Log.class);
    final PassthruDocRetrieveProxy mockProxy = context.mock(PassthruDocRetrieveProxy.class);
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
            PassthruDocRetrieveProxyObjectFactory proxyFactory = new PassthruDocRetrieveProxyObjectFactory()
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
    public void testGetPassthruDocRetrieveProxyHappy()
    {
        try
        {
            PassthruDocRetrieveProxyObjectFactory proxyFactory = new PassthruDocRetrieveProxyObjectFactory()
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
            PassthruDocRetrieveProxy proxy = proxyFactory.getPassthruDocRetrieveProxy();
            assertNotNull("EntityPatientDiscoveryProxy was null", proxy);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetPassthruDocRetrieveProxyHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetPassthruDocRetrieveProxyHappy test: " + t.getMessage());
        }
    }

}