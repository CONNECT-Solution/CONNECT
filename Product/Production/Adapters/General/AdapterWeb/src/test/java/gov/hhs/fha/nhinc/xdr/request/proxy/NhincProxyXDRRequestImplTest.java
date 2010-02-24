package gov.hhs.fha.nhinc.xdr.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.nhincproxyxdrsecured.async.request.ProxyXDRSecuredRequestPortType;
import gov.hhs.fha.nhinc.nhincproxyxdrsecured.async.request.ProxyXDRSecuredRequestService;
import ihe.iti.xdr._2007.AcknowledgementType;
import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Neil Webb
 */
public class NhincProxyXDRRequestImplTest
{
    private Mockery context;

    @Before
    public void setUp()
    {
        context = new Mockery()
        {
            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
    }

    @After
    public void tearDown()
    {
    }

    @Test
    public void testProvideAndRegisterDocumentSetBRequest()
    {
        final Log mockLogger = context.mock(Log.class);
        final ProxyXDRSecuredRequestService mockService = context.mock(ProxyXDRSecuredRequestService.class);
        
        NhincProxyXDRRequestImpl sut = new NhincProxyXDRRequestImpl()
        {
            @Override
            protected Log createLogger()
            {
                return mockLogger;
            }

            @Override
            protected String getURL()
            {
                return "Mock URL";
            }

            @Override
            protected ProxyXDRSecuredRequestPortType getPort(String url)
            {
                ProxyXDRSecuredRequestPortType port = new ProxyXDRSecuredRequestPortType()
                {
                    public AcknowledgementType provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType arg0)
                    {
                        AcknowledgementType ack = new AcknowledgementType();
                        ack.setMessage("Mock Success");
                        return ack;
                    }
                };

                return port;
            }

            @Override
            protected void setRequestContext(AssertionType assertion, String url, ProxyXDRSecuredRequestPortType port)
            {
            }

            @Override
            protected ProxyXDRSecuredRequestService createService()
            {
                return mockService;
            }

        };

        context.checking(new Expectations()
        {
            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                allowing(mockLogger).error(with(any(String.class)));
                will(returnValue(null));
            }
        });

        RespondingGatewayProvideAndRegisterDocumentSetRequestType request = new RespondingGatewayProvideAndRegisterDocumentSetRequestType();

        AcknowledgementType ack = sut.provideAndRegisterDocumentSetBRequest(request);

        assertNotNull("Ack was null", ack);
        assertEquals("Response message incorrect", "Mock Success", ack.getMessage());
    }

}