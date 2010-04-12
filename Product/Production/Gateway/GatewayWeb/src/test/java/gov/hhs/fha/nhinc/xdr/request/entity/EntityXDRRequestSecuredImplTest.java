package gov.hhs.fha.nhinc.xdr.request.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.xdr.XDRAuditLogger;
import gov.hhs.fha.nhinc.xdr.request.proxy.NhincProxyXDRRequestSecuredImpl;
import ihe.iti.xdr._2007.AcknowledgementType;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Neil Webb
 */
public class EntityXDRRequestSecuredImplTest
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
        final XDRAuditLogger mockAuditLogger = context.mock(XDRAuditLogger.class);
        final AssertionType mockAssertion = context.mock(AssertionType.class);
        final WebServiceContext mockWebServiceContext = context.mock(WebServiceContext.class);

        EntityXDRRequestSecuredImpl sut = new EntityXDRRequestSecuredImpl()
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
            protected NhincProxyXDRRequestSecuredImpl createNhinProxy()
            {
                NhincProxyXDRRequestSecuredImpl mockProxy = new NhincProxyXDRRequestSecuredImpl()
                {
                    @Override
                    public AcknowledgementType provideAndRegisterDocumentSetBRequest(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType provideAndRegisterRequestRequest, AssertionType assertion)
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
            protected boolean checkPolicy(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request, AssertionType assertion)
            {
                return true;
            }
        };

        context.checking(new Expectations()
        {
            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                oneOf(mockAuditLogger).auditEntityXDR(with(any(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType.class)), with(any(AssertionType.class)), with(any(String.class)));
                oneOf(mockAuditLogger).auditEntityAcknowledgement(with(any(AcknowledgementType.class)), with(any(AssertionType.class)), with(any(String.class)), with(any(String.class)));
                will(returnValue(null));
            }
        });

        RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request = new RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();

        AcknowledgementType ack = sut.provideAndRegisterDocumentSetBRequest(request, mockWebServiceContext);

        assertNotNull("Ack was null", ack);
        assertEquals("Response message incorrect", "Mock Success", ack.getMessage());
    }

}