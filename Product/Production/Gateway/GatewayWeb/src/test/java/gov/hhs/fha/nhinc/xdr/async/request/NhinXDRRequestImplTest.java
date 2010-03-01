/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.async.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.xdr.XDRAuditLogger;
import ihe.iti.xdr._2007.AcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
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
     * Test of getLogger method, of class NhinXDRRequestImpl.
     */
    @Ignore
    public void testGetLogger() {
        System.out.println("getLogger");
        NhinXDRRequestImpl instance = new NhinXDRRequestImpl();
        Log expResult = null;
        Log result = instance.getLogger();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of provideAndRegisterDocumentSetBRequest method, of class NhinXDRRequestImpl.
     */
    @Test
    public void testProvideAndRegisterDocumentSetBRequestSuccessfulPolicyCheck() {
        System.out.println("provideAndRegisterDocumentSetBRequest");
        Mockery mockery = new Mockery() {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
        final WebServiceContext context = mockery.mock(WebServiceContext.class);
        final Log mockLogger = mockery.mock(Log.class);
        final AssertionType mockAssertion = mockery.mock(AssertionType.class);
        final XDRAuditLogger mockXDRAuditLogger = mockery.mock(XDRAuditLogger.class);

        final NhinXDRRequestImpl nhinXDRRequest = new NhinXDRRequestImpl(){

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
            protected AcknowledgementType forwardToAgency(ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion) {
                AcknowledgementType ack = new AcknowledgementType();
                ack.setMessage("SUCCESS");
                return ack;
            }

        };
        
        final ProvideAndRegisterDocumentSetRequestType body = new ProvideAndRegisterDocumentSetRequestType();
        final gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType ack = new gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType();
        ack.setMessage("SUCCESS");
        final HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId("2.2");
        
        AcknowledgementType expResult = null;

        mockery.checking(new Expectations() {
            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockXDRAuditLogger).auditNhinXDR(with(any(ProvideAndRegisterDocumentSetRequestType.class)), with(any(AssertionType.class)), with(any(String.class)));
                will(returnValue(ack));
                one(mockXDRAuditLogger).auditAcknowledgement(with(any(AcknowledgementType.class)), with(any(AssertionType.class)), with(any(String.class)), with(any(String.class)));
                will(returnValue(ack));
                one(mockAssertion).getHomeCommunity();
                will(returnValue(homeCommunity));
            }
        });

        AcknowledgementType result = nhinXDRRequest.provideAndRegisterDocumentSetBRequest(body, context);
        mockery.assertIsSatisfied();
        assertEquals("SUCCESS", result.getMessage());

    }

    @Test
    public void testProvideAndRegisterDocumentSetBRequestFailedPolicyCheck() {
        System.out.println("provideAndRegisterDocumentSetBRequest");
        Mockery mockery = new Mockery() {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
        final WebServiceContext context = mockery.mock(WebServiceContext.class);
        final Log mockLogger = mockery.mock(Log.class);
        final AssertionType mockAssertion = mockery.mock(AssertionType.class);
        final XDRAuditLogger mockXDRAuditLogger = mockery.mock(XDRAuditLogger.class);

        final NhinXDRRequestImpl nhinXDRRequest = new NhinXDRRequestImpl(){

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
            protected AcknowledgementType forwardToAgency(ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion) {
                AcknowledgementType ack = new AcknowledgementType();
                ack.setMessage("SUCCESS");
                return ack;
            }

        };

        final ProvideAndRegisterDocumentSetRequestType body = new ProvideAndRegisterDocumentSetRequestType();
        final gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType ack = new gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType();
        ack.setMessage("SUCCESS");
        final HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId("2.2");

        AcknowledgementType expResult = null;

        mockery.checking(new Expectations() {
            {
                allowing(mockLogger).debug(with(any(String.class)));
                allowing(mockLogger).error(with(any(String.class)));
                one(mockXDRAuditLogger).auditNhinXDR(with(any(ProvideAndRegisterDocumentSetRequestType.class)), with(any(AssertionType.class)), with(any(String.class)));
                will(returnValue(ack));
                one(mockXDRAuditLogger).auditAcknowledgement(with(any(AcknowledgementType.class)), with(any(AssertionType.class)), with(any(String.class)), with(any(String.class)));
                will(returnValue(ack));
                one(mockAssertion).getHomeCommunity();
                will(returnValue(homeCommunity));
            }
        });

        AcknowledgementType result = nhinXDRRequest.provideAndRegisterDocumentSetBRequest(body, context);
        mockery.assertIsSatisfied();
        assertEquals(NhinXDRRequestImpl.XDR_POLICY_ERROR, result.getMessage());

    }

    /**
     * Test of getXDRAuditLogger method, of class NhinXDRRequestImpl.
     */
    @Ignore
    public void testGetXDRAuditLogger() {
        System.out.println("getXDRAuditLogger");
        NhinXDRRequestImpl instance = new NhinXDRRequestImpl();
        XDRAuditLogger expResult = null;
        XDRAuditLogger result = instance.getXDRAuditLogger();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }


    /**
     * Test of isPolicyOk method, of class NhinXDRRequestImpl.
     */
    @Ignore
    public void testIsPolicyOk() {
        System.out.println("isPolicyOk");
        ProvideAndRegisterDocumentSetRequestType newRequest = null;
        AssertionType assertion = null;
        String senderHCID = "";
        String receiverHCID = "";
        NhinXDRRequestImpl instance = new NhinXDRRequestImpl();
        boolean expResult = false;
        boolean result = instance.isPolicyOk(newRequest, assertion, senderHCID, receiverHCID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}