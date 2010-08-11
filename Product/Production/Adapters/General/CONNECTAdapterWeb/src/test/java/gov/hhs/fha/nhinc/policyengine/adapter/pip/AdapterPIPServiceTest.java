package gov.hhs.fha.nhinc.policyengine.adapter.pip;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentResponseType;
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
public class AdapterPIPServiceTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final AdapterPIPServiceImpl mockServiceImpl = context.mock(AdapterPIPServiceImpl.class);
    final WebServiceContext mockWebServiceContext = context.mock(WebServiceContext.class);

    @Test
    public void testGetAdapterPIPServiceImpl()
    {
        try
        {
            AdapterPIPService sut = new AdapterPIPService()
            {
                @Override
                protected AdapterPIPServiceImpl getAdapterPIPServiceImpl()
                {
                    return mockServiceImpl;
                }
            };

            AdapterPIPServiceImpl serviceImpl = sut.getAdapterPIPServiceImpl();
            assertNotNull("AdapterPIPServiceImpl was null", serviceImpl);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetAdapterPIPServiceImpl: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetAdapterPIPServiceImpl: " + t.getMessage());
        }
    }

    @Test
    public void testGetWebServiceContext()
    {
        try
        {
            AdapterPIPService sut = new AdapterPIPService()
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
    public void testRetrievePtConsentByPtId()
    {
        try
        {
            AdapterPIPService sut = new AdapterPIPService()
            {
                @Override
                protected AdapterPIPServiceImpl getAdapterPIPServiceImpl()
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
                    oneOf(mockServiceImpl).retrievePtConsentByPtId(with(aNonNull(RetrievePtConsentByPtIdRequestType.class)), with(aNonNull(WebServiceContext.class)));
                }
            });

            RetrievePtConsentByPtIdRequestType request = new RetrievePtConsentByPtIdRequestType();

            RetrievePtConsentByPtIdResponseType response = sut.retrievePtConsentByPtId(request);
            assertNotNull("RetrievePtConsentByPtIdResponseType was null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRetrievePtConsentByPtId: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRetrievePtConsentByPtId: " + t.getMessage());
        }
    }

    @Test
    public void testRetrievePtConsentByPtDocId()
    {
        try
        {
            AdapterPIPService sut = new AdapterPIPService()
            {
                @Override
                protected AdapterPIPServiceImpl getAdapterPIPServiceImpl()
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
                    oneOf(mockServiceImpl).retrievePtConsentByPtDocId(with(aNonNull(RetrievePtConsentByPtDocIdRequestType.class)), with(aNonNull(WebServiceContext.class)));
                }
            });

            RetrievePtConsentByPtDocIdRequestType request = new RetrievePtConsentByPtDocIdRequestType();

            RetrievePtConsentByPtDocIdResponseType response = sut.retrievePtConsentByPtDocId(request);
            assertNotNull("RetrievePtConsentByPtDocIdResponseType was null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRetrievePtConsentByPtDocId: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRetrievePtConsentByPtDocId: " + t.getMessage());
        }
    }

    @Test
    public void testStorePtConsent()
    {
        try
        {
            AdapterPIPService sut = new AdapterPIPService()
            {
                @Override
                protected AdapterPIPServiceImpl getAdapterPIPServiceImpl()
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
                    oneOf(mockServiceImpl).storePtConsent(with(aNonNull(StorePtConsentRequestType.class)), with(aNonNull(WebServiceContext.class)));
                }
            });

            StorePtConsentRequestType request = new StorePtConsentRequestType();

            StorePtConsentResponseType response = sut.storePtConsent(request);
            assertNotNull("StorePtConsentResponseType was null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testStorePtConsent: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testStorePtConsent: " + t.getMessage());
        }
    }

}