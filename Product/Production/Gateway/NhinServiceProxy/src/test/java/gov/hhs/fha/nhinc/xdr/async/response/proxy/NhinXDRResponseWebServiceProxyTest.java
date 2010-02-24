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
import ihe.iti.xdr._2007.AcknowledgementType;
import ihe.iti.xdr.async.response._2007.XDRResponseService;
import ihe.iti.xdr.async.response._2007.XDRResponsePortType;
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
        final XDRResponseService mockService = context.mock(XDRResponseService.class);

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
            protected XDRResponsePortType getPort(String url)
            {
                XDRResponsePortType mockPort = new XDRResponsePortType()
                {
                    public AcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType request)
                    {
                        AcknowledgementType ack = new AcknowledgementType();
                        ack.setMessage("Mock Success");
                        return ack;
                    }
                };
                return mockPort;
            }

            @Override
            protected void setRequestContext(AssertionType assertion, String url, XDRResponsePortType port)
            {
            }

            @Override
            protected XDRResponseService createService()
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

        AcknowledgementType ack = proxy.provideAndRegisterDocumentSetBResponse(request, assertion, targetSystem);
        assertNotNull("Ack was null", ack);
        assertEquals("Ack value", "Mock Success", ack.getMessage());
    }

}