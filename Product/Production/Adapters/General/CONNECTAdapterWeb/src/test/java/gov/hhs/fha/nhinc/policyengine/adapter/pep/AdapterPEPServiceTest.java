package gov.hhs.fha.nhinc.policyengine.adapter.pep;

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
public class AdapterPEPServiceTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final AdapterPEPServiceImpl mockServiceImpl = context.mock(AdapterPEPServiceImpl.class);
    final WebServiceContext mockWebServiceContext = context.mock(WebServiceContext.class);

    @Test
    public void testGetAdapterPEPServiceImpl()
    {
        try
        {
            AdapterPEPService sut = new AdapterPEPService()
            {
                @Override
                protected AdapterPEPServiceImpl getAdapterPEPServiceImpl()
                {
                    return mockServiceImpl;
                }
            };

            AdapterPEPServiceImpl serviceImpl = sut.getAdapterPEPServiceImpl();
            assertNotNull("AdapterPEPServiceImpl was null", serviceImpl);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetAdapterPEPServiceImpl: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetAdapterPEPServiceImpl: " + t.getMessage());
        }
    }

    @Test
    public void testGetWebServiceContext()
    {
        try
        {
            AdapterPEPService sut = new AdapterPEPService()
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
            AdapterPEPService sut = new AdapterPEPService()
            {
                @Override
                protected AdapterPEPServiceImpl getAdapterPEPServiceImpl()
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