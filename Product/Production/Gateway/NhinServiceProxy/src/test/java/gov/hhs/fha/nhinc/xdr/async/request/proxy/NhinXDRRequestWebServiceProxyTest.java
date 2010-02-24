package gov.hhs.fha.nhinc.xdr.async.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import ihe.iti.xdr._2007.AcknowledgementType;
import ihe.iti.xdr.async.request._2007.XDRRequestService;
import ihe.iti.xdr.async.request._2007.XDRRequestPortType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import org.junit.Test;
import static org.junit.Assert.*;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.apache.commons.logging.Log;

/**
 *
 * @author Neil Webb
 */
public class NhinXDRRequestWebServiceProxyTest
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
    @Test
    public void testProvideAndRegisterDocumentSetBRequest()
    {
        final Log mockLogger = context.mock(Log.class);
        final XDRRequestService mockService = context.mock(XDRRequestService.class);

        NhinXDRRequestWebServiceProxy proxy = new NhinXDRRequestWebServiceProxy()
        {
            @Override
            protected Log createLogger()
            {
                return mockLogger;
            }

            @Override
            protected String getUrl(NhinTargetSystemType target)
            {
                return "Mock URL";
            }

            @Override
            protected XDRRequestPortType getPort(String url)
            {
                XDRRequestPortType mockPort = new XDRRequestPortType()
                {
                    public AcknowledgementType provideAndRegisterDocumentSetBRequest(ProvideAndRegisterDocumentSetRequestType arg0)
                    {
                        AcknowledgementType ack = new AcknowledgementType();
                        ack.setMessage("Mock Success");
                        return ack;
                    }
                };
                return mockPort;
            }

            @Override
            protected void setRequestContext(AssertionType assertion, String url, XDRRequestPortType port)
            {
            }

            @Override
            protected XDRRequestService createService()
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
        
        ProvideAndRegisterDocumentSetRequestType request = null;
        AssertionType assertion = null;
        NhinTargetSystemType targetSystem = null;

        AcknowledgementType ack = proxy.provideAndRegisterDocumentSetBRequest(request, assertion, targetSystem);
        assertNotNull("Ack was null", ack);
        assertEquals("Ack value", "Mock Success", ack.getMessage());
    }

}