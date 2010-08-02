package gov.hhs.fha.nhinc.docretrievedeferred.nhinc.proxy.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
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
 * @author Sai Valluripalli
 */
public class NhincProxyDocRetrieveDeferredReqSecuredTest {
    private Mockery mockery = null;

    public NhincProxyDocRetrieveDeferredReqSecuredTest() {
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
    public void testCrossGatewayRetrieveRequestHappy()
    {
        final AssertionType mockAssertion = mockery.mock(AssertionType.class);
        final RetrieveDocumentSetRequestType mockRetrieveDocumentSetRequestType = mockery.mock(RetrieveDocumentSetRequestType.class);
        final NhinTargetSystemType mockNhinTargetSystemType = mockery.mock(NhinTargetSystemType.class);
        final DocRetrieveAcknowledgementType mockDocRetrieveAcknowledgementType = mockery.mock(DocRetrieveAcknowledgementType.class);
        RespondingGatewayCrossGatewayRetrieveSecuredRequestType req = new RespondingGatewayCrossGatewayRetrieveSecuredRequestType();
        NhincProxyDocRetrieveDeferredReqSecured testSubject = new NhincProxyDocRetrieveDeferredReqSecured()
        {
            @Override
            protected AssertionType extractAssertionFromServiceContext() {
                return mockAssertion;
            }

            @Override
            protected RetrieveDocumentSetRequestType extractDocRetrieveRequest(RespondingGatewayCrossGatewayRetrieveSecuredRequestType body) {
                return mockRetrieveDocumentSetRequestType;
            }

            @Override
            protected NhinTargetSystemType extractNhinTargetSystem(RespondingGatewayCrossGatewayRetrieveSecuredRequestType body) {
                return mockNhinTargetSystemType;
            }

            @Override
            protected DocRetrieveAcknowledgementType sendToNhincProxyDocretrieveImplementation(RetrieveDocumentSetRequestType retrieveDocumentSetRequest, AssertionType assertion, NhinTargetSystemType nhinTargetSystem) {
                return mockDocRetrieveAcknowledgementType;
            }
        };
        
        assertNotNull(testSubject.crossGatewayRetrieveRequest(req));
    }

    @Test
    public void testCrossGatewayRetrieveRequestBad()
    {
        NhincProxyDocRetrieveDeferredReqSecured testSubject = new NhincProxyDocRetrieveDeferredReqSecured();
        assertNull(testSubject.crossGatewayRetrieveRequest(null));

    }

    @Test
    public void testSendToNhincProxyDocretrieveImplementation()
    {
//        final NhincProxyDocRetrieveDeferredReqImpl mockNhincProxyDocRetrieveDeferredReqImpl = mockery.mock(NhincProxyDocRetrieveDeferredReqImpl.class);
//        final DocRetrieveAcknowledgementType mockDocRetrieveAcknowledgementType = mockery.mock(DocRetrieveAcknowledgementType.class);
//        NhincProxyDocRetrieveDeferredReqSecured testSubject = new NhincProxyDocRetrieveDeferredReqSecured();
//        mockery.checking(new Expectations() {
//            {
//                allowing(mockNhincProxyDocRetrieveDeferredReqImpl).crossGatewayRetrieveRequest(with(any(RetrieveDocumentSetRequestType.class)), with(any(AssertionType.class)), with(any(NhinTargetSystemType.class)));
//                will(returnValue(mockDocRetrieveAcknowledgementType));
//            }
//        });
//        RetrieveDocumentSetRequestType retrieveDocumentSetRequestType = new RetrieveDocumentSetRequestType();
//        AssertionType assertion = new AssertionType();
//        NhinTargetSystemType nhinTargetSystem = new NhinTargetSystemType();
//        assertNotNull(testSubject.sendToNhincProxyDocretrieveImplementation(retrieveDocumentSetRequestType, assertion, nhinTargetSystem));
//        mockery.assertIsSatisfied();
    }
}
