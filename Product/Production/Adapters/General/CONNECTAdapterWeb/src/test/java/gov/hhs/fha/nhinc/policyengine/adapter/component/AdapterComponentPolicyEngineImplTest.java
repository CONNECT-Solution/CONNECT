package gov.hhs.fha.nhinc.policyengine.adapter.component;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.policyengine.adapter.orchestrator.AdapterPolicyEngineOrchestratorImpl;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
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
public class AdapterComponentPolicyEngineImplTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);
    final AdapterPolicyEngineOrchestratorImpl mockAdapterPolicyEngineOrchestratorImpl = context.mock(AdapterPolicyEngineOrchestratorImpl.class);
    final WebServiceContext mockWebServiceContext = context.mock(WebServiceContext.class);

    @Test
    public void testCreateLogger()
    {
        try
        {
            AdapterComponentPolicyEngineImpl sut = new AdapterComponentPolicyEngineImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
            };

            Log log = sut.createLogger();
            assertNotNull("Log was null", log);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testCreateLogger: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCreateLogger: " + t.getMessage());
        }
    }

    @Test
    public void testGetAdapterPEPImpl()
    {
        try
        {
            AdapterComponentPolicyEngineImpl sut = new AdapterComponentPolicyEngineImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AdapterPolicyEngineOrchestratorImpl getAdapterPolicyEngineOrchestratorImpl()
                {
                    return mockAdapterPolicyEngineOrchestratorImpl;
                }
            };

            AdapterPolicyEngineOrchestratorImpl orchImpl = sut.getAdapterPolicyEngineOrchestratorImpl();
            assertNotNull("AdapterPolicyEngineOrchestratorImpl was null", orchImpl);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetAdapterPEPImpl: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetAdapterPEPImpl: " + t.getMessage());
        }
    }

    @Test
    public void testLoadAssertion()
    {
        try
        {
            AdapterComponentPolicyEngineImpl sut = new AdapterComponentPolicyEngineImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected void loadAssertion(AssertionType assertion, WebServiceContext wsContext) throws Exception
                {
                }
            };

            final AssertionType mockAssertion = context.mock(AssertionType.class);
            sut.loadAssertion(mockAssertion, mockWebServiceContext);
            assertTrue("Passed loadAssertion", true);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testLoadAssertion: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testLoadAssertion: " + t.getMessage());
        }
    }

    @Test
    public void testCheckPolicyHappy()
    {
        try
        {
            AdapterComponentPolicyEngineImpl sut = new AdapterComponentPolicyEngineImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AdapterPolicyEngineOrchestratorImpl getAdapterPolicyEngineOrchestratorImpl()
                {
                    return mockAdapterPolicyEngineOrchestratorImpl;
                }
                @Override
                protected void loadAssertion(AssertionType assertion, WebServiceContext wsContext) throws Exception
                {
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                    oneOf(mockAdapterPolicyEngineOrchestratorImpl).checkPolicy(with(aNonNull(CheckPolicyRequestType.class)), with(aNonNull(AssertionType.class)));
                }
            });

            CheckPolicyRequestType request = new CheckPolicyRequestType();
            AssertionType assertion = new AssertionType();
            request.setAssertion(assertion);
            CheckPolicyResponseType response = sut.checkPolicy(request, mockWebServiceContext);
            assertNotNull("CheckPolicyResponseType was null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testCheckPolicyHappy: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCheckPolicyHappy: " + t.getMessage());
        }
    }

    @Test
    public void testCheckPolicyException()
    {
        try
        {
            AdapterComponentPolicyEngineImpl sut = new AdapterComponentPolicyEngineImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AdapterPolicyEngineOrchestratorImpl getAdapterPolicyEngineOrchestratorImpl()
                {
                    return mockAdapterPolicyEngineOrchestratorImpl;
                }
                @Override
                protected void loadAssertion(AssertionType assertion, WebServiceContext wsContext) throws Exception
                {
                    throw new IllegalArgumentException("Forced error.");
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                    oneOf(mockLog).error(with(aNonNull(String.class)), with(aNonNull(IllegalArgumentException.class)));
                }
            });

            CheckPolicyRequestType request = new CheckPolicyRequestType();

            sut.checkPolicy(request, mockWebServiceContext);
            fail("Should have had exception.");
        }
        catch(RuntimeException e)
        {
            assertEquals("Exception message", "Error occurred calling AdapterPolicyEngineOrchestratorLib.checkPolicy.  Error: Forced error.", e.getMessage());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testCheckPolicyException: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCheckPolicyException: " + t.getMessage());
        }
    }

}