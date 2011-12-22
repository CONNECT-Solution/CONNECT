/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.passthru.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docquery.passthru.deferred.request.PassthruDocQueryDeferredRequestUnsecuredImpl;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.docquery.DocQueryAuditLog;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
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
public class PassthruDocQueryDeferredRequestUnsecuredImplTest {

    private Mockery mockery = null;

    public PassthruDocQueryDeferredRequestUnsecuredImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        mockery = new Mockery() {
            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of crossGatewayQueryRequest method, of class PassthruDocQueryDeferredRequestUnsecured.
     */
    @Test
    public void testCrossGatewayQueryRequestHappyPath() {
        System.out.println("crossGatewayQueryRequest -- Happy Path");

        final Log mockLogger = mockery.mock(Log.class);
        final PassthruDocQueryDeferredRequestOrchImpl mockNhincDocQueryDeferredRequestOrchImpl = mockery.mock(PassthruDocQueryDeferredRequestOrchImpl.class);

        final RespondingGatewayCrossGatewayQueryRequestType mockCrossGatewayQueryRequest = mockery.mock(RespondingGatewayCrossGatewayQueryRequestType.class);
        final AssertionType mockAssertion = mockery.mock(AssertionType.class);
        final DocQueryAuditLog mockDocQueryAuditLog = mockery.mock(DocQueryAuditLog.class);
        final AdhocQueryRequest mockAdhocQueryRequest = mockery.mock(AdhocQueryRequest.class);
        final NhinTargetSystemType mockTarget = mockery.mock(NhinTargetSystemType.class);


        PassthruDocQueryDeferredRequestUnsecuredImpl instance = new PassthruDocQueryDeferredRequestUnsecuredImpl(){
            @Override
            protected Log getLogger(){
                return mockLogger;
            }

            @Override
            protected PassthruDocQueryDeferredRequestOrchImpl getPassthruDocQueryDeferredRequestOrchImpl() {
                return mockNhincDocQueryDeferredRequestOrchImpl;
            }
        };

        mockery.checking(new Expectations() {
            {
                allowing(mockLogger).isDebugEnabled();
                will(returnValue(true));
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockCrossGatewayQueryRequest).getAdhocQueryRequest();
                will(returnValue(mockAdhocQueryRequest));
                one(mockCrossGatewayQueryRequest).getAssertion();
                will(returnValue(mockAssertion));
                one(mockCrossGatewayQueryRequest).getNhinTargetSystem();
                will(returnValue(mockTarget));
                one(mockAssertion).setMessageId(with(any(String.class)));
                one(mockNhincDocQueryDeferredRequestOrchImpl).crossGatewayQueryRequest(with(any(AdhocQueryRequest.class)), with(any(AssertionType.class)),
                        with(any(NhinTargetSystemType.class)));
                will(returnValue(new DocQueryAcknowledgementType()));
                
            }
        });

        DocQueryAcknowledgementType result = instance.crossGatewayQueryRequest(mockCrossGatewayQueryRequest, null);
        assertNotNull(result);
    }

}