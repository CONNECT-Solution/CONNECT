package gov.hhs.fha.nhinc.xdr.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.docsubmission.XDRAuditLogger;
import gov.hhs.fha.nhinc.xdr.async.response.proxy.NhinXDRResponseProxy;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
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
public class NhincProxyXDRResponseSecuredImplTest {

    private Mockery context;

    @Before
    public void setUp() {
        context = new Mockery() {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
    }

    @Test
    public void testProvideAndRegisterDocumentSetBResponseWithAssertion() {
        final Log mockLogger = context.mock(Log.class);
        final XDRAuditLogger mockAuditLogger = context.mock(XDRAuditLogger.class);
        final AssertionType mockAssertion = context.mock(AssertionType.class);
        final WebServiceContext mockWebServiceContext = context.mock(WebServiceContext.class);

        NhincProxyXDRResponseSecuredImpl sut = new NhincProxyXDRResponseSecuredImpl() {

            @Override
            protected XDRAuditLogger createAuditLogger() {
                return mockAuditLogger;
            }

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            @Override
            protected NhinXDRResponseProxy createNhinProxy() {
                NhinXDRResponseProxy mockProxy = new NhinXDRResponseProxy() {

                    @Override
                    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType request, AssertionType assertion, NhinTargetSystemType targetSystem) {
                        XDRAcknowledgementType response = new XDRAcknowledgementType();
                        RegistryResponseType regResp = new RegistryResponseType();
                        regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
                        response.setMessage(regResp);
                        return response;
                    }
                };
                return mockProxy;
            }

            @Override
            protected String extractMessageId(WebServiceContext context) {
                return "uuid:1111111111.11111.111.11";
            }
        };

        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                allowing(mockLogger).warn(with(any(String.class)));
                oneOf(mockAssertion).setAsyncMessageId(with(any(String.class)));
                oneOf(mockAuditLogger).auditNhinXDRResponseRequest(with(any(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType.class)), with(any(AssertionType.class)), with(any(String.class)));
                oneOf(mockAuditLogger).auditAcknowledgement(with(any(XDRAcknowledgementType.class)), with(any(AssertionType.class)), with(any(String.class)), with(any(String.class)));
                will(returnValue(null));
            }
        });

        RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request = new RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType();

        XDRAcknowledgementType ack = sut.provideAndRegisterDocumentSetBResponse(request, mockAssertion);

        assertNotNull("Ack was null", ack);
        assertEquals("Response message incorrect", NhincConstants.XDR_ACK_STATUS_MSG, ack.getMessage().getStatus());
    }

    @Test
    public void testProvideAndRegisterDocumentSetBResponseWithWebServiceContext() {
        final Log mockLogger = context.mock(Log.class);
        final XDRAuditLogger mockAuditLogger = context.mock(XDRAuditLogger.class);
        final AssertionType mockAssertion = context.mock(AssertionType.class);
        final WebServiceContext mockWebServiceContext = context.mock(WebServiceContext.class);

        NhincProxyXDRResponseSecuredImpl sut = new NhincProxyXDRResponseSecuredImpl() {

            @Override
            protected XDRAuditLogger createAuditLogger() {
                return mockAuditLogger;
            }

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            @Override
            protected NhinXDRResponseProxy createNhinProxy() {
                NhinXDRResponseProxy mockProxy = new NhinXDRResponseProxy() {

                    @Override
                    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType request, AssertionType assertion, NhinTargetSystemType targetSystem) {
                        XDRAcknowledgementType response = new XDRAcknowledgementType();
                        RegistryResponseType regResp = new RegistryResponseType();
                        regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
                        response.setMessage(regResp);
                        return response;
                    }
                };
                return mockProxy;
            }

            @Override
            protected AssertionType extractAssertion(WebServiceContext context) {
                return mockAssertion;
            }

            @Override
            protected String extractMessageId(WebServiceContext context) {
                return "uuid:1111111111.11111.111.11";
            }
        };

        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                allowing(mockLogger).warn(with(any(String.class)));
                oneOf(mockAssertion).setAsyncMessageId(with(any(String.class)));
                oneOf(mockAuditLogger).auditNhinXDRResponseRequest(with(any(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType.class)), with(any(AssertionType.class)), with(any(String.class)));
                oneOf(mockAuditLogger).auditAcknowledgement(with(any(XDRAcknowledgementType.class)), with(any(AssertionType.class)), with(any(String.class)), with(any(String.class)));
                will(returnValue(null));
            }
        });

        RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request = new RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType();

        XDRAcknowledgementType ack = sut.provideAndRegisterDocumentSetBResponse(request, mockWebServiceContext);

        assertNotNull("Ack was null", ack);
        assertEquals("Response message incorrect", NhincConstants.XDR_ACK_STATUS_MSG, ack.getMessage().getStatus());
    }
}
