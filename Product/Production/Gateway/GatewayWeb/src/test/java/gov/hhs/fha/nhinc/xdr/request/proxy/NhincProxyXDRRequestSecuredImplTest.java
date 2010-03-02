package gov.hhs.fha.nhinc.xdr.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.xdr.XDRAuditLogger;
import gov.hhs.fha.nhinc.xdr.async.request.proxy.NhinXDRRequestProxy;
import ihe.iti.xdr._2007.AcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import javax.xml.ws.WebServiceContext;
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
public class NhincProxyXDRRequestSecuredImplTest
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
    public void testProvideAndRegisterDocumentSetBRequestWithAssertion()
    {
        final Log mockLogger = context.mock(Log.class);
        final XDRAuditLogger mockAuditLogger = context.mock(XDRAuditLogger.class);
        final AssertionType mockAssertion = context.mock(AssertionType.class);

        NhincProxyXDRRequestSecuredImpl sut = new NhincProxyXDRRequestSecuredImpl()
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
            protected NhinXDRRequestProxy createNhinProxy()
            {
                NhinXDRRequestProxy nhinProxy = new NhinXDRRequestProxy()
                {
                    @Override
                    public AcknowledgementType provideAndRegisterDocumentSetBRequest(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion, NhinTargetSystemType targetSystem)
                    {
                        AcknowledgementType response = new AcknowledgementType();
                        response.setMessage("Mock Success");
                        return response;
                    }
                };
                return nhinProxy;
            }

        };

        context.checking(new Expectations()
        {
            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                oneOf(mockAuditLogger).auditXDR(with(any(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType.class)), with(any(AssertionType.class)), with(any(String.class)));
                oneOf(mockAuditLogger).auditAcknowledgement(with(any(AcknowledgementType.class)), with(any(AssertionType.class)), with(any(String.class)), with(any(String.class)));
                will(returnValue(null));
            }
        });

        RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request = new RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();

        AcknowledgementType ack = sut.provideAndRegisterDocumentSetBRequest(request, mockAssertion);

        assertNotNull("Ack was null", ack);
        assertEquals("Response message incorrect", "Mock Success", ack.getMessage());
    }

    @Test
    public void testProvideAndRegisterDocumentSetBRequestWithWebServiceContext()
    {
        final Log mockLogger = context.mock(Log.class);
        final XDRAuditLogger mockAuditLogger = context.mock(XDRAuditLogger.class);
        final AssertionType mockAssertion = context.mock(AssertionType.class);
        final WebServiceContext mockWebServiceContext = context.mock(WebServiceContext.class);

        NhincProxyXDRRequestSecuredImpl sut = new NhincProxyXDRRequestSecuredImpl()
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
            protected AssertionType extractAssertion(WebServiceContext context)
            {
                return mockAssertion;
            }

            @Override
            protected NhinXDRRequestProxy createNhinProxy()
            {
                NhinXDRRequestProxy nhinProxy = new NhinXDRRequestProxy()
                {
                    @Override
                    public AcknowledgementType provideAndRegisterDocumentSetBRequest(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion, NhinTargetSystemType targetSystem)
                    {
                        AcknowledgementType response = new AcknowledgementType();
                        response.setMessage("Mock Success");
                        return response;
                    }
                };
                return nhinProxy;
            }

        };

        context.checking(new Expectations()
        {
            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                oneOf(mockAuditLogger).auditXDR(with(any(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType.class)), with(any(AssertionType.class)), with(any(String.class)));
                oneOf(mockAuditLogger).auditAcknowledgement(with(any(AcknowledgementType.class)), with(any(AssertionType.class)), with(any(String.class)), with(any(String.class)));
                will(returnValue(null));
            }
        });

        RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request = new RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();

        AcknowledgementType ack = sut.provideAndRegisterDocumentSetBRequest(request, mockWebServiceContext);

        assertNotNull("Ack was null", ack);
        assertEquals("Response message incorrect", "Mock Success", ack.getMessage());
    }

}