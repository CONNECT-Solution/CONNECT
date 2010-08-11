package gov.hhs.fha.nhinc.policyengine.adapter.component;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import javax.xml.ws.WebServiceContext;
import org.jmock.Expectations;
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
public class AdapterPolicyEngineOrchestratorTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final AdapterComponentPolicyEngineImpl mockServiceImpl = context.mock(AdapterComponentPolicyEngineImpl.class);
    final WebServiceContext mockWebServiceContext = context.mock(WebServiceContext.class);

    @Test
    public void testGetAdapterComponentPolicyEngineImpl()
    {
        try
        {
            AdapterPolicyEngineOrchestrator sut = new AdapterPolicyEngineOrchestrator()
            {
                @Override
                protected AdapterComponentPolicyEngineImpl getAdapterComponentPolicyEngineImpl()
                {
                    return mockServiceImpl;
                }
            };

            AdapterComponentPolicyEngineImpl serviceImpl = sut.getAdapterComponentPolicyEngineImpl();
            assertNotNull("AdapterComponentPolicyEngineImpl was null", serviceImpl);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetAdapterComponentPolicyEngineImpl: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetAdapterComponentPolicyEngineImpl: " + t.getMessage());
        }
    }

    @Test
    public void testGetWebServiceContext()
    {
        try
        {
            AdapterPolicyEngineOrchestrator sut = new AdapterPolicyEngineOrchestrator()
            {
                @Override
                protected WebServiceContext getWebServiceContext()
                {
                    return mockWebServiceContext;
                }
            };

            WebServiceContext wsContext = sut.getWebServiceContext();
            assertNotNull("WebServiceContext was null", wsContext);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetWebServiceContext: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetWebServiceContext: " + t.getMessage());
        }
    }

    @Test
    public void testCheckPolicy()
    {
        try
        {
            AdapterPolicyEngineOrchestrator sut = new AdapterPolicyEngineOrchestrator()
            {
                @Override
                protected AdapterComponentPolicyEngineImpl getAdapterComponentPolicyEngineImpl()
                {
                    return mockServiceImpl;
                }
                @Override
                protected WebServiceContext getWebServiceContext()
                {
                    return mockWebServiceContext;
                }
            };
            context.checking(new Expectations()
            {
                {
                    oneOf(mockServiceImpl).checkPolicy(with(aNonNull(CheckPolicyRequestType.class)), with(aNonNull(WebServiceContext.class)));
                }
            });

            CheckPolicyRequestType request = new CheckPolicyRequestType();

            CheckPolicyResponseType response = sut.checkPolicy(request);
            assertNotNull("CheckPolicyResponseType was null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testCheckPolicy: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCheckPolicy: " + t.getMessage());
        }
    }

}