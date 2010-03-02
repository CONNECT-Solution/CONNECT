package gov.hhs.fha.nhinc.xdr.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType;
import gov.hhs.fha.nhinc.xdr.XDRAuditLogger;
import gov.hhs.fha.nhinc.xdr.async.response.proxy.NhinXDRResponseProxy;
import ihe.iti.xdr._2007.AcknowledgementType;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Neil Webb
 */
public class NhincProxyXDRResponseSecuredImplTest
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
    public void testProvideAndRegisterDocumentSetBResponseWithAssertion()
    {
        final Log mockLogger = context.mock(Log.class);
        final XDRAuditLogger mockAuditLogger = context.mock(XDRAuditLogger.class);
        final AssertionType mockAssertion = context.mock(AssertionType.class);
        final WebServiceContext mockWebServiceContext = context.mock(WebServiceContext.class);

        NhincProxyXDRResponseSecuredImpl sut = new NhincProxyXDRResponseSecuredImpl()
        {
            @Override
            protected XDRAuditLogger createAuditLogger()
            {
                return mockAuditLogger;
            }

            @Override
            protected Log createLogger()
            {
                return mockLogger;
            }

            @Override
            protected NhinXDRResponseProxy createNhinProxy()
            {
                NhinXDRResponseProxy mockProxy = new NhinXDRResponseProxy()
                {
                    @Override
                    public AcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType request, AssertionType assertion, NhinTargetSystemType targetSystem)
                    {
                        AcknowledgementType response = new AcknowledgementType();
                        response.setMessage("Mock Success");
                        return response;
                    }
                };
                return mockProxy;
            }
        };

        context.checking(new Expectations()
        {
            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                oneOf(mockAuditLogger).auditNhinXDRResponseRequest(with(any(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType.class)), with(any(AssertionType.class)), with(any(String.class)));
                oneOf(mockAuditLogger).auditAcknowledgement(with(any(AcknowledgementType.class)), with(any(AssertionType.class)), with(any(String.class)), with(any(String.class)));
                will(returnValue(null));
            }
        });

        RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request = new RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType();

        AcknowledgementType ack = sut.provideAndRegisterDocumentSetBResponse(request, mockAssertion);

        assertNotNull("Ack was null", ack);
        assertEquals("Response message incorrect", "Mock Success", ack.getMessage());
    }

    @Test
    public void testProvideAndRegisterDocumentSetBResponseWithWebServiceContext()
    {
        final Log mockLogger = context.mock(Log.class);
        final XDRAuditLogger mockAuditLogger = context.mock(XDRAuditLogger.class);
        final AssertionType mockAssertion = context.mock(AssertionType.class);
        final WebServiceContext mockWebServiceContext = context.mock(WebServiceContext.class);

        NhincProxyXDRResponseSecuredImpl sut = new NhincProxyXDRResponseSecuredImpl()
        {
            @Override
            protected XDRAuditLogger createAuditLogger()
            {
                return mockAuditLogger;
            }

            @Override
            protected Log createLogger()
            {
                return mockLogger;
            }

            @Override
            protected NhinXDRResponseProxy createNhinProxy()
            {
                NhinXDRResponseProxy mockProxy = new NhinXDRResponseProxy()
                {
                    @Override
                    public AcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType request, AssertionType assertion, NhinTargetSystemType targetSystem)
                    {
                        AcknowledgementType response = new AcknowledgementType();
                        response.setMessage("Mock Success");
                        return response;
                    }
                };
                return mockProxy;
            }

            @Override
            protected AssertionType extractAssertion(WebServiceContext context)
            {
                return mockAssertion;
            }
        };

        context.checking(new Expectations()
        {
            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                oneOf(mockAuditLogger).auditNhinXDRResponseRequest(with(any(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType.class)), with(any(AssertionType.class)), with(any(String.class)));
                oneOf(mockAuditLogger).auditAcknowledgement(with(any(AcknowledgementType.class)), with(any(AssertionType.class)), with(any(String.class)), with(any(String.class)));
                will(returnValue(null));
            }
        });

        RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request = new RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType();

        AcknowledgementType ack = sut.provideAndRegisterDocumentSetBResponse(request, mockWebServiceContext);

        assertNotNull("Ack was null", ack);
        assertEquals("Response message incorrect", "Mock Success", ack.getMessage());
    }

}