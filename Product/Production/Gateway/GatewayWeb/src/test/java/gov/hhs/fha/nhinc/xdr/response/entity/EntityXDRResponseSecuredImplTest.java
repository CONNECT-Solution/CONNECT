package gov.hhs.fha.nhinc.xdr.response.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType;
import gov.hhs.fha.nhinc.xdr.XDRAuditLogger;
import gov.hhs.fha.nhinc.xdr.response.proxy.NhincProxyXDRResponseSecuredImpl;
import ihe.iti.xdr._2007.AcknowledgementType;
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
public class EntityXDRResponseSecuredImplTest
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
        final XDRAuditLogger mockAuditLogger = context.mock(XDRAuditLogger.class);
        final AssertionType mockAssertion = context.mock(AssertionType.class);
        final WebServiceContext mockWebServiceContext = context.mock(WebServiceContext.class);

        EntityXDRResponseSecuredImpl sut = new EntityXDRResponseSecuredImpl()
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
            protected NhincProxyXDRResponseSecuredImpl createNhinProxy()
            {
                NhincProxyXDRResponseSecuredImpl mockProxy = new NhincProxyXDRResponseSecuredImpl()
                {
                    @Override
                    public AcknowledgementType provideAndRegisterDocumentSetBResponse(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType provideAndRegisterResponseRequest, AssertionType assertion)
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

            @Override
            protected boolean checkPolicy(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request, AssertionType assertion)
            {
                return true;
            }
        };

        context.checking(new Expectations()
        {
            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                oneOf(mockAuditLogger).auditEntityXDRResponseRequest(with(any(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType.class)), with(any(AssertionType.class)), with(any(String.class)));
                oneOf(mockAuditLogger).auditEntityAcknowledgement(with(any(AcknowledgementType.class)), with(any(AssertionType.class)), with(any(String.class)), with(any(String.class)));
                will(returnValue(null));
            }
        });

        RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request = new RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType();

        AcknowledgementType ack = sut.provideAndRegisterDocumentSetBResponse(request, mockWebServiceContext);

        assertNotNull("Ack was null", ack);
        assertEquals("Response message incorrect", "Mock Success", ack.getMessage());
    }

}