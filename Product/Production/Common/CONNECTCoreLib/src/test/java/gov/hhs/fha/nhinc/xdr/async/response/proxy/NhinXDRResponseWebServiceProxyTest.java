package gov.hhs.fha.nhinc.xdr.async.response.proxy;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.apache.commons.logging.Log;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xdr._2007.XDRDeferredResponseService;
import ihe.iti.xdr._2007.XDRDeferredResponsePortType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 *
 * @author Neil Webb
 */
public class NhinXDRResponseWebServiceProxyTest
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
    public void testProvideAndRegisterDocumentSetBResponse()
    {
        final Log mockLogger = context.mock(Log.class);
        final XDRDeferredResponseService mockService = context.mock(XDRDeferredResponseService.class);

        NhinXDRResponseWebServiceProxy proxy = new NhinXDRResponseWebServiceProxy()
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
            protected XDRDeferredResponsePortType getPort(String url)
            {
                XDRDeferredResponsePortType mockPort = new XDRDeferredResponsePortType()
                {
                    public XDRAcknowledgementType provideAndRegisterDocumentSetBDeferredResponse(RegistryResponseType request)
                    {
                        XDRAcknowledgementType ack = new XDRAcknowledgementType();
                        RegistryResponseType regResp = new RegistryResponseType();
                        regResp.setStatus("Mock Success");
                        ack.setMessage(regResp);
                        return ack;
                    }
                };
                return mockPort;
            }

            @Override
            protected void setRequestContext(AssertionType assertion, String url, XDRDeferredResponsePortType port)
            {
            }

            @Override
            protected XDRDeferredResponseService createService()
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

        RegistryResponseType request = null;
        AssertionType assertion = null;
        NhinTargetSystemType targetSystem = null;

        XDRAcknowledgementType ack = proxy.provideAndRegisterDocumentSetBResponse(request, assertion, targetSystem);
        assertNotNull("Ack was null", ack);
        assertEquals("Ack value", "Mock Success", ack.getMessage().getStatus());
    }

}