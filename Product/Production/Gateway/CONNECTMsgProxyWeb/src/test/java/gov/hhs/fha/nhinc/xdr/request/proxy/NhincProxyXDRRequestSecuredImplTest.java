package gov.hhs.fha.nhinc.xdr.request.proxy;

import gov.hhs.fha.nhinc.docsubmission.passthru.deferred.request.NhincProxyXDRRequestSecuredImpl;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.docsubmission.XDRAuditLogger;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Ignore;
import static org.junit.Assert.*;

/**
 *
 * @author Neil Webb
 */
public class NhincProxyXDRRequestSecuredImplTest {

    private Mockery context;

    @Before
    public void setUp() {
        context = new Mockery() {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
    }

//    @Ignore
//    public void testProvideAndRegisterDocumentSetBRequestWithAssertion() {
//        final Log mockLogger = context.mock(Log.class);
//        final XDRAuditLogger mockAuditLogger = context.mock(XDRAuditLogger.class);
//        final AssertionType mockAssertion = context.mock(AssertionType.class);
//
//        NhincProxyXDRRequestSecuredImpl sut = new NhincProxyXDRRequestSecuredImpl() {
//
//            @Override
//            protected XDRAuditLogger createAuditLogger() {
//                return mockAuditLogger;
//            }
//
//            @Override
//            protected Log createLogger() {
//                return mockLogger;
//            }
//
//            @Override
//            protected NhinXDRRequestProxy createNhinProxy() {
//                NhinXDRRequestProxy nhinProxy = new NhinXDRRequestProxy() {
//
//                    @Override
//                    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion, NhinTargetSystemType targetSystem) {
//                        XDRAcknowledgementType response = new XDRAcknowledgementType();
//                        RegistryResponseType regResp = new RegistryResponseType();
//                        regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
//                        response.setMessage(regResp);
//                        return response;
//                    }
//                };
//                return nhinProxy;
//            }
//
//            @Override
//            protected String extractMessageId(WebServiceContext context) {
//                return "uuid:1111111111.11111.111.11";
//            }
//        };
//
//        context.checking(new Expectations() {
//
//            {
//                allowing(mockLogger).info(with(any(String.class)));
//                allowing(mockLogger).debug(with(any(String.class)));
//                oneOf(mockAssertion).setMessageId(with(any(String.class)));
//                oneOf(mockAuditLogger).auditXDR(with(any(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType.class)), with(any(AssertionType.class)), with(any(String.class)));
//                oneOf(mockAuditLogger).auditAcknowledgement(with(any(XDRAcknowledgementType.class)), with(any(AssertionType.class)), with(any(String.class)), with(any(String.class)));
//                will(returnValue(null));
//            }
//        });
//
//        RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request = new RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();
//
//        XDRAcknowledgementType ack = sut.provideAndRegisterDocumentSetBRequest(request, mockAssertion);
//
//        assertNotNull("Ack was null", ack);
//        assertEquals("Response message incorrect", NhincConstants.XDR_ACK_STATUS_MSG, ack.getMessage().getStatus());
//    }
//
//    @Ignore
//    public void testProvideAndRegisterDocumentSetBRequestWithWebServiceContext() {
//        final Log mockLogger = context.mock(Log.class);
//        final XDRAuditLogger mockAuditLogger = context.mock(XDRAuditLogger.class);
//        final AssertionType mockAssertion = context.mock(AssertionType.class);
//        final WebServiceContext mockWebServiceContext = context.mock(WebServiceContext.class);
//
//        NhincProxyXDRRequestSecuredImpl sut = new NhincProxyXDRRequestSecuredImpl() {
//
//            @Override
//            protected XDRAuditLogger createAuditLogger() {
//                return mockAuditLogger;
//            }
//
//            @Override
//            protected Log createLogger() {
//                return mockLogger;
//            }
//
//            @Override
//            protected AssertionType extractAssertion(WebServiceContext context) {
//                return mockAssertion;
//            }
//
//            @Override
//            protected NhinXDRRequestProxy createNhinProxy() {
//                NhinXDRRequestProxy nhinProxy = new NhinXDRRequestProxy() {
//
//                    @Override
//                    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion, NhinTargetSystemType targetSystem) {
//                        XDRAcknowledgementType response = new XDRAcknowledgementType();
//                        RegistryResponseType regResp = new RegistryResponseType();
//                        regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
//                        response.setMessage(regResp);
//                        return response;
//                    }
//                };
//                return nhinProxy;
//            }
//
//            @Override
//            protected String extractMessageId(WebServiceContext context) {
//                return "uuid:1111111111.11111.111.11";
//            }
//        };
//
//        context.checking(new Expectations() {
//
//            {
//                allowing(mockLogger).info(with(any(String.class)));
//                allowing(mockLogger).debug(with(any(String.class)));
//                oneOf(mockAssertion).setMessageId(with(any(String.class)));
//                oneOf(mockAuditLogger).auditXDR(with(any(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType.class)), with(any(AssertionType.class)), with(any(String.class)));
//                oneOf(mockAuditLogger).auditAcknowledgement(with(any(XDRAcknowledgementType.class)), with(any(AssertionType.class)), with(any(String.class)), with(any(String.class)));
//                will(returnValue(null));
//            }
//        });
//
//        RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request = new RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();
//
//        XDRAcknowledgementType ack = sut.provideAndRegisterDocumentSetBRequest(request, mockWebServiceContext);
//
//        assertNotNull("Ack was null", ack);
//        assertEquals("Response message incorrect", NhincConstants.XDR_ACK_STATUS_MSG, ack.getMessage().getStatus());
//    }
}
