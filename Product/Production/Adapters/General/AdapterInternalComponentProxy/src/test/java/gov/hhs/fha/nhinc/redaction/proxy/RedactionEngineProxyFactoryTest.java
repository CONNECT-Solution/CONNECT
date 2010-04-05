package gov.hhs.fha.nhinc.redaction.proxy;

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
public class RedactionEngineProxyFactoryTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    @Test
    public void testGetRedactionEngine()
    {
        try
        {
            RedactionEngineProxyFactory proxyFactory = new RedactionEngineProxyFactory();
            RedactionEngineProxy proxy = proxyFactory.getRedactionEngineProxy();
            // TODO: Correct after implemented
            assertNull("RedactionEngineProxy was not null", proxy);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetRedactionEngine test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetRedactionEngine test: " + t.getMessage());
        }
    }

}