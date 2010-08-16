package gov.hhs.fha.nhinc.redactionengine.adapter.proxy;

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
    final AdapterRedactionEngineProxy mockProxy = context.mock(AdapterRedactionEngineProxy.class);
    final ApplicationContext appContext = new FileSystemXmlApplicationContext()
    {
        @Override
        public Object getBean(String beanName)
        {
            return mockProxy;
        }
    };



    @Test
    public void testGetRedactionEngineProxyHappy()
    {
        try
        {
            AdapterRedactionEngineProxyObjectFactory proxyFactory = new AdapterRedactionEngineProxyObjectFactory();
            
            AdapterRedactionEngineProxy proxy = proxyFactory.getRedactionEngineProxy();
            assertNotNull("RedactionEngineProxy was null", proxy);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetRedactionEngineProxyHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetRedactionEngineProxyHappy test: " + t.getMessage());
        }
    }

    

}