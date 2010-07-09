/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.xdr.async.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.xdr.XDRAuditLogger;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
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
 * @author patlollav
 */
public class NhinXDRRequestImplTest {

    public NhinXDRRequestImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of provideAndRegisterDocumentSetBRequest method, of class NhinXDRRequestImpl.
     */
    @Test
    public void testProvideAndRegisterDocumentSetBRequestSuccessfulPolicyCheck() {
        System.out.println("testProvideAndRegisterDocumentSetBRequestSuccessfulPolicyCheck");
        Mockery mockery = new Mockery() {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
        final WebServiceContext context = mockery.mock(WebServiceContext.class);
        final Log mockLogger = mockery.mock(Log.class);
        final AssertionType mockAssertion = mockery.mock(AssertionType.class);
        final XDRAuditLogger mockXDRAuditLogger = mockery.mock(XDRAuditLogger.class);

        final NhinXDRRequestImpl nhinXDRRequest = new NhinXDRRequestImpl() {

            @Override
            protected Log getLogger() {
                return mockLogger;
            }

            @Override
            protected String retrieveHomeCommunityID() {
                return "1.1";
            }

            @Override
            protected AssertionType createAssertion(WebServiceContext context) {
                return mockAssertion;
            }

            @Override
            protected XDRAuditLogger getXDRAuditLogger() {
                return mockXDRAuditLogger;
            }

            @Override
            protected boolean isPolicyOk(ProvideAndRegisterDocumentSetRequestType newRequest, AssertionType assertion, String senderHCID, String receiverHCID) {
                return true;
            }

            @Override
            protected XDRAcknowledgementType forwardToAgency(ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion) {
                XDRAcknowledgementType ack = new XDRAcknowledgementType();
                RegistryResponseType regResp = new RegistryResponseType();
                regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
                ack.setMessage(regResp);
                return ack;
            }

            @Override
            protected String extractMessageId(WebServiceContext context) {
                return "uuid:1111111111.11111.111.11";
            }

            @Override
            protected boolean isLiftMessage(ProvideAndRegisterDocumentSetRequestType request) {
                return false;
            }
        };

        final ProvideAndRegisterDocumentSetRequestType body = new ProvideAndRegisterDocumentSetRequestType();
        final gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType ack = new gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType();
        ack.setMessage("SUCCESS");
        final HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId("2.2");

        mockery.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                oneOf(mockAssertion).setAsyncMessageId(with(any(String.class)));
                one(mockXDRAuditLogger).auditNhinXDR(with(any(ProvideAndRegisterDocumentSetRequestType.class)), with(any(AssertionType.class)), with(any(String.class)));
                will(returnValue(ack));
                one(mockXDRAuditLogger).auditAcknowledgement(with(any(XDRAcknowledgementType.class)), with(any(AssertionType.class)), with(any(String.class)), with(any(String.class)));
                will(returnValue(ack));
                one(mockAssertion).getHomeCommunity();
                will(returnValue(homeCommunity));
            }
        });

        XDRAcknowledgementType result = nhinXDRRequest.provideAndRegisterDocumentSetBRequest(body, context);
        mockery.assertIsSatisfied();
        assertEquals(NhincConstants.XDR_ACK_STATUS_MSG, result.getMessage().getStatus());

    }

    /**
     * Test of provideAndRegisterDocumentSetBRequest method, of class NhinXDRRequestImpl.
     */
    @Test
    public void testProvideAndRegisterDocumentSetBRequestFailedPolicyCheck() {
        System.out.println("testProvideAndRegisterDocumentSetBRequestFailedPolicyCheck");
        Mockery mockery = new Mockery() {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
        final WebServiceContext context = mockery.mock(WebServiceContext.class);
        final Log mockLogger = mockery.mock(Log.class);
        final AssertionType mockAssertion = mockery.mock(AssertionType.class);
        final XDRAuditLogger mockXDRAuditLogger = mockery.mock(XDRAuditLogger.class);

        final NhinXDRRequestImpl nhinXDRRequest = new NhinXDRRequestImpl() {

            @Override
            protected Log getLogger() {
                return mockLogger;
            }

            @Override
            protected String retrieveHomeCommunityID() {
                return "1.1";
            }

            @Override
            protected AssertionType createAssertion(WebServiceContext context) {
                return mockAssertion;
            }

            @Override
            protected XDRAuditLogger getXDRAuditLogger() {
                return mockXDRAuditLogger;
            }

            @Override
            protected boolean isPolicyOk(ProvideAndRegisterDocumentSetRequestType newRequest, AssertionType assertion, String senderHCID, String receiverHCID) {
                return false;
            }

            @Override
            protected XDRAcknowledgementType forwardToAgency(ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion) {
                XDRAcknowledgementType ack = new XDRAcknowledgementType();
                RegistryResponseType regResp = new RegistryResponseType();
                regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
                ack.setMessage(regResp);
                return ack;
            }

            @Override
            protected XDRAcknowledgementType sendErrorToAgency(ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion, String errMsg) {
                XDRAcknowledgementType ack = new XDRAcknowledgementType();
                RegistryResponseType regResp = new RegistryResponseType();
                regResp.setStatus(errMsg);
                ack.setMessage(regResp);
                return ack;
            }

            @Override
            protected String extractMessageId(WebServiceContext context) {
                return "uuid:1111111111.11111.111.11";
            }

            @Override
            protected boolean isLiftMessage(ProvideAndRegisterDocumentSetRequestType request) {
                return false;
            }
        };

        final ProvideAndRegisterDocumentSetRequestType body = new ProvideAndRegisterDocumentSetRequestType();
        final gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType ack = new gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType();
        ack.setMessage("SUCCESS");
        final HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId("2.2");

        mockery.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                oneOf(mockAssertion).setAsyncMessageId(with(any(String.class)));
                one(mockXDRAuditLogger).auditNhinXDR(with(any(ProvideAndRegisterDocumentSetRequestType.class)), with(any(AssertionType.class)), with(any(String.class)));
                will(returnValue(ack));
                one(mockXDRAuditLogger).auditAcknowledgement(with(any(XDRAcknowledgementType.class)), with(any(AssertionType.class)), with(any(String.class)), with(any(String.class)));
                will(returnValue(ack));
                one(mockAssertion).getHomeCommunity();
                will(returnValue(homeCommunity));
                oneOf(mockLogger).error("Policy Check Failed");
            }
        });

        XDRAcknowledgementType result = nhinXDRRequest.provideAndRegisterDocumentSetBRequest(body, context);
        mockery.assertIsSatisfied();
        assertEquals("Policy Check Failed", result.getMessage().getStatus());

    }

    /**
     * Test of provideAndRegisterDocumentSetBRequest method, of class NhinXDRRequestImpl.
     */
    @Test
    public void testProvideAndRegisterDocumentSetBRequestLift() {
        System.out.println("testProvideAndRegisterDocumentSetBRequestLift");
        Mockery mockery = new Mockery() {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
        final WebServiceContext context = mockery.mock(WebServiceContext.class);
        final Log mockLogger = mockery.mock(Log.class);
        final AssertionType mockAssertion = mockery.mock(AssertionType.class);
        final XDRAuditLogger mockXDRAuditLogger = mockery.mock(XDRAuditLogger.class);

        final NhinXDRRequestImpl nhinXDRRequest = new NhinXDRRequestImpl() {

            @Override
            protected Log getLogger() {
                return mockLogger;
            }

            @Override
            protected String retrieveHomeCommunityID() {
                return "1.1";
            }

            @Override
            protected AssertionType createAssertion(WebServiceContext context) {
                return mockAssertion;
            }

            @Override
            protected XDRAuditLogger getXDRAuditLogger() {
                return mockXDRAuditLogger;
            }

            @Override
            protected boolean isPolicyOk(ProvideAndRegisterDocumentSetRequestType newRequest, AssertionType assertion, String senderHCID, String receiverHCID) {
                return true;
            }

            @Override
            protected XDRAcknowledgementType forwardToAgency(ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion) {
                XDRAcknowledgementType ack = new XDRAcknowledgementType();
                RegistryResponseType regResp = new RegistryResponseType();
                regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
                ack.setMessage(regResp);
                return ack;
            }

            @Override
            protected String extractMessageId(WebServiceContext context) {
                return "uuid:1111111111.11111.111.11";
            }

            @Override
            protected boolean isLiftMessage(ProvideAndRegisterDocumentSetRequestType request) {
                return true;
            }

            @Override
            protected boolean checkLiftProperty() {
                return true;
            }

            @Override
            protected XDRAcknowledgementType processLiftMessage(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion) {
                XDRAcknowledgementType ack = new XDRAcknowledgementType();
                RegistryResponseType regResp = new RegistryResponseType();
                regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
                ack.setMessage(regResp);
                return ack;
            }
        };

        final ProvideAndRegisterDocumentSetRequestType body = new ProvideAndRegisterDocumentSetRequestType();
        final gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType ack = new gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType();
        ack.setMessage("SUCCESS");
        final HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId("2.2");

        mockery.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                oneOf(mockAssertion).setAsyncMessageId(with(any(String.class)));
                one(mockXDRAuditLogger).auditNhinXDR(with(any(ProvideAndRegisterDocumentSetRequestType.class)), with(any(AssertionType.class)), with(any(String.class)));
                will(returnValue(ack));
                one(mockXDRAuditLogger).auditAcknowledgement(with(any(XDRAcknowledgementType.class)), with(any(AssertionType.class)), with(any(String.class)), with(any(String.class)));
                will(returnValue(ack));
                one(mockAssertion).getHomeCommunity();
                will(returnValue(homeCommunity));
            }
        });

        XDRAcknowledgementType result = nhinXDRRequest.provideAndRegisterDocumentSetBRequest(body, context);
        mockery.assertIsSatisfied();
        assertEquals(NhincConstants.XDR_ACK_STATUS_MSG, result.getMessage().getStatus());

    }

    /**
     * Test of provideAndRegisterDocumentSetBRequest method, of class NhinXDRRequestImpl.
     */
    @Test
    public void testProvideAndRegisterDocumentSetBRequestLiftPropFalse() {
        System.out.println("testProvideAndRegisterDocumentSetBRequestLiftPropFalse");
        Mockery mockery = new Mockery() {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
        final WebServiceContext context = mockery.mock(WebServiceContext.class);
        final Log mockLogger = mockery.mock(Log.class);
        final AssertionType mockAssertion = mockery.mock(AssertionType.class);
        final XDRAuditLogger mockXDRAuditLogger = mockery.mock(XDRAuditLogger.class);

        final NhinXDRRequestImpl nhinXDRRequest = new NhinXDRRequestImpl() {

            @Override
            protected Log getLogger() {
                return mockLogger;
            }

            @Override
            protected String retrieveHomeCommunityID() {
                return "1.1";
            }

            @Override
            protected AssertionType createAssertion(WebServiceContext context) {
                return mockAssertion;
            }

            @Override
            protected XDRAuditLogger getXDRAuditLogger() {
                return mockXDRAuditLogger;
            }

            @Override
            protected boolean isPolicyOk(ProvideAndRegisterDocumentSetRequestType newRequest, AssertionType assertion, String senderHCID, String receiverHCID) {
                return true;
            }

            @Override
            protected XDRAcknowledgementType forwardToAgency(ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion) {
                XDRAcknowledgementType ack = new XDRAcknowledgementType();
                RegistryResponseType regResp = new RegistryResponseType();
                regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
                ack.setMessage(regResp);
                return ack;
            }

            @Override
            protected String extractMessageId(WebServiceContext context) {
                return "uuid:1111111111.11111.111.11";
            }

            @Override
            protected boolean isLiftMessage(ProvideAndRegisterDocumentSetRequestType request) {
                return true;
            }

            @Override
            protected boolean checkLiftProperty() {
                return false;
            }

            @Override
            protected XDRAcknowledgementType processLiftMessage(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion) {
                XDRAcknowledgementType ack = new XDRAcknowledgementType();
                RegistryResponseType regResp = new RegistryResponseType();
                regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
                ack.setMessage(regResp);
                return ack;
            }
        };

        final ProvideAndRegisterDocumentSetRequestType body = new ProvideAndRegisterDocumentSetRequestType();
        final gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType ack = new gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType();
        ack.setMessage("SUCCESS");
        final HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId("2.2");

        mockery.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                oneOf(mockAssertion).setAsyncMessageId(with(any(String.class)));
                one(mockXDRAuditLogger).auditNhinXDR(with(any(ProvideAndRegisterDocumentSetRequestType.class)), with(any(AssertionType.class)), with(any(String.class)));
                will(returnValue(ack));
                one(mockXDRAuditLogger).auditAcknowledgement(with(any(XDRAcknowledgementType.class)), with(any(AssertionType.class)), with(any(String.class)), with(any(String.class)));
                will(returnValue(ack));
                one(mockAssertion).getHomeCommunity();
                will(returnValue(homeCommunity));
            }
        });

        XDRAcknowledgementType result = nhinXDRRequest.provideAndRegisterDocumentSetBRequest(body, context);
        mockery.assertIsSatisfied();
        assertEquals(NhincConstants.XDR_ACK_STATUS_MSG, result.getMessage().getStatus());

    }
}
