/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docretrievedeferred.nhinc.proxy.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
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
 * @author svalluripalli
 */
public class NhincProxyDocRetrieveDeferredReqTest {

    private Mockery mockery = null;

    public NhincProxyDocRetrieveDeferredReqTest() {
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

    @Test
    public void testCrossGatewayRetrieveRequestHappy() {
        final AssertionType mockAssertion = mockery.mock(AssertionType.class);
        final RetrieveDocumentSetRequestType mockRetrieveDocumentSetRequestType = mockery.mock(RetrieveDocumentSetRequestType.class);
        final NhinTargetSystemType mockNhinTargetSystemType = mockery.mock(NhinTargetSystemType.class);
        RespondingGatewayCrossGatewayRetrieveRequestType req = new RespondingGatewayCrossGatewayRetrieveRequestType();
        
        NhincProxyDocRetrieveDeferredReq testSubject = new NhincProxyDocRetrieveDeferredReq() {
            @Override
            protected NhinTargetSystemType extractNhinTargetSystem(RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest) {
                return mockNhinTargetSystemType;
            }

            @Override
            protected RetrieveDocumentSetRequestType extractDocRetrieveRequest(RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest) {
                return mockRetrieveDocumentSetRequestType;
            }

            @Override
            protected AssertionType extractAssertion(RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest) {
                return mockAssertion;
            }
        };
        assertNotNull(testSubject.crossGatewayRetrieveRequest(req));
    }

    @Test
    public void testCrossGatewayRetrieveRequestBad() {
        NhincProxyDocRetrieveDeferredReq testSubject = new NhincProxyDocRetrieveDeferredReq();
        assertNull(testSubject.crossGatewayRetrieveRequest(null));
    }
}
