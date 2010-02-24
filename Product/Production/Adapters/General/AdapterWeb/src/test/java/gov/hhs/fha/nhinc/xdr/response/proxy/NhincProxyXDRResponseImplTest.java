package gov.hhs.fha.nhinc.xdr.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType;
import gov.hhs.fha.nhinc.nhincproxyxdrsecured.async.response.ProxyXDRSecuredResponseService;
import gov.hhs.fha.nhinc.nhincproxyxdrsecured.async.response.ProxyXDRSecuredResponsePortType;
import ihe.iti.xdr._2007.AcknowledgementType;
import org.apache.commons.logging.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import static org.junit.Assert.*;

/**
 *
 * @author Neil Webb
 */
public class NhincProxyXDRResponseImplTest
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
    public void testProvideAndRegisterDocumentSetBResponse()
    {
        final Log mockLogger = context.mock(Log.class);
        final ProxyXDRSecuredResponseService mockService = context.mock(ProxyXDRSecuredResponseService.class);

        NhincProxyXDRResponseImpl sut = new NhincProxyXDRResponseImpl()
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
            protected ProxyXDRSecuredResponsePortType getPort(String url)
            {
                ProxyXDRSecuredResponsePortType port = new ProxyXDRSecuredResponsePortType()
                {
                    public AcknowledgementType provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType arg0)
                    {
                        AcknowledgementType ack = new AcknowledgementType();
                        ack.setMessage("Mock Success");
                        return ack;
                    }
                };
                return port;
            }

            @Override
            protected void setRequestContext(AssertionType assertion, String url, ProxyXDRSecuredResponsePortType port)
            {
            }

            @Override
            protected ProxyXDRSecuredResponseService createService()
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

        RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType request = new RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType();

        AcknowledgementType ack = sut.provideAndRegisterDocumentSetBResponse(request);

        assertNotNull("Ack was null", ack);
        assertEquals("Response message incorrect", "Mock Success", ack.getMessage());
    }

}