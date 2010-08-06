package gov.hhs.fha.nhinc.docretrievedeferred.entity.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
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
 * @author Sai Valluripalli
 */
public class EntityDocRetrieveDeferredReqTest {

    private static Mockery mockery = null;
    private EntityDocRetrieveDeferredReq testSubject = null;

    public EntityDocRetrieveDeferredReqTest() {
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

    @Ignore
    public void testCrossGatewayRetrieveRequestHappy() {
        final DocRetrieveAcknowledgementType mockAck = mockery.mock(DocRetrieveAcknowledgementType.class);
        final AssertionType mockAssertion = mockery.mock(AssertionType.class);
        final RetrieveDocumentSetRequestType mockRetrieveDocumentSetRequest = mockery.mock(RetrieveDocumentSetRequestType.class);
        final NhinTargetCommunitiesType mockNhinTargetCommunities = mockery.mock(NhinTargetCommunitiesType.class);
        EntityDocRetrieveDeferredReq testSubject = new EntityDocRetrieveDeferredReq() {

            @Override
            protected DocRetrieveAcknowledgementType getAckFromImpl(RetrieveDocumentSetRequestType retrieveDocumentSetRequest, AssertionType assertion, NhinTargetCommunitiesType nhinTargetCommunities) {
                return mockAck;
            }

            @Override
            protected AssertionType getAssertionInfo(RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest) {
                return mockAssertion;
            }

            @Override
            protected RetrieveDocumentSetRequestType getDocRequest(RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest) {
                return mockRetrieveDocumentSetRequest;
            }

            @Override
            protected NhinTargetCommunitiesType getTargetCommunities(RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest) {
                return mockNhinTargetCommunities;
            }
        };
//        mockery.checking(new Expectations() {
//            {
//                allowing()
//            }
//        });

        RespondingGatewayCrossGatewayRetrieveRequestType req = new RespondingGatewayCrossGatewayRetrieveRequestType();
        assertNotNull(testSubject.crossGatewayRetrieveRequest(req));
        assertEquals(testSubject.crossGatewayRetrieveRequest(req), mockAck);
    }

    @Test
    public void testCrossGatewayRetrieveRequestBad() {
        EntityDocRetrieveDeferredReq testSubject = new EntityDocRetrieveDeferredReq() {

            @Override
            protected DocRetrieveAcknowledgementType getAckFromImpl(RetrieveDocumentSetRequestType retrieveDocumentSetRequest, AssertionType assertion, NhinTargetCommunitiesType nhinTargetCommunities) {
                return null;
            }

            @Override
            protected AssertionType getAssertionInfo(RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest) {
                return null;
            }

            @Override
            protected RetrieveDocumentSetRequestType getDocRequest(RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest) {
                return null;
            }

            @Override
            protected NhinTargetCommunitiesType getTargetCommunities(RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest) {
                return null;
            }
        };

        RespondingGatewayCrossGatewayRetrieveRequestType req = new RespondingGatewayCrossGatewayRetrieveRequestType();
        assertNull(testSubject.crossGatewayRetrieveRequest(req));
    }

    @Test
    public void testGetAssertionInfoHappy() {
        final ExtractEntityDocRetrieveDeferredRequestValues mockExtractEntityDocRetrieveDeferredRequestValues = mockery.mock(ExtractEntityDocRetrieveDeferredRequestValues.class);
        final AssertionType mockAssertion = mockery.mock(AssertionType.class);
        EntityDocRetrieveDeferredReq testSubject = new EntityDocRetrieveDeferredReq();
        mockery.checking(new Expectations() {

            {
                allowing(mockExtractEntityDocRetrieveDeferredRequestValues).extractAssertion(with(any(RespondingGatewayCrossGatewayRetrieveRequestType.class)));
                will(returnValue(mockAssertion));
            }
        });
        RespondingGatewayCrossGatewayRetrieveRequestType req = new RespondingGatewayCrossGatewayRetrieveRequestType();
        req.setAssertion(mockAssertion);
        assertNotNull(testSubject.getAssertionInfo(req));

    }

    @Test
    public void testGetAssertionInfoBad() {
        final ExtractEntityDocRetrieveDeferredRequestValues mockExtractEntityDocRetrieveDeferredRequestValues = mockery.mock(ExtractEntityDocRetrieveDeferredRequestValues.class);
        EntityDocRetrieveDeferredReq testSubject = new EntityDocRetrieveDeferredReq();
        mockery.checking(new Expectations() {

            {
                allowing(mockExtractEntityDocRetrieveDeferredRequestValues).extractAssertion(with(any(RespondingGatewayCrossGatewayRetrieveRequestType.class)));
                will(returnValue(null));
            }
        });
        RespondingGatewayCrossGatewayRetrieveRequestType req = new RespondingGatewayCrossGatewayRetrieveRequestType();
        assertNull(testSubject.getAssertionInfo(req));
    }

    @Test
    public void testGetDocRequestHappy() {
        final ExtractEntityDocRetrieveDeferredRequestValues mockExtractEntityDocRetrieveDeferredRequestValues = mockery.mock(ExtractEntityDocRetrieveDeferredRequestValues.class);
        final RetrieveDocumentSetRequestType mockRetrieveDocumentSetRequest = mockery.mock(RetrieveDocumentSetRequestType.class);
        EntityDocRetrieveDeferredReq testSubject = new EntityDocRetrieveDeferredReq();
        mockery.checking(new Expectations() {

            {
                allowing(mockExtractEntityDocRetrieveDeferredRequestValues).extractRetrieveDocumentSetRequestType(with(any(RespondingGatewayCrossGatewayRetrieveRequestType.class)));
                will(returnValue(mockRetrieveDocumentSetRequest));
            }
        });
        RespondingGatewayCrossGatewayRetrieveRequestType req = new RespondingGatewayCrossGatewayRetrieveRequestType();
        req.setRetrieveDocumentSetRequest(mockRetrieveDocumentSetRequest);
        assertNotNull(testSubject.getDocRequest(req));
    }

    @Test
    public void testGetDocRequestBad() {
        final ExtractEntityDocRetrieveDeferredRequestValues mockExtractEntityDocRetrieveDeferredRequestValues = mockery.mock(ExtractEntityDocRetrieveDeferredRequestValues.class);
        EntityDocRetrieveDeferredReq testSubject = new EntityDocRetrieveDeferredReq();
        mockery.checking(new Expectations() {

            {
                allowing(mockExtractEntityDocRetrieveDeferredRequestValues).extractRetrieveDocumentSetRequestType(with(any(RespondingGatewayCrossGatewayRetrieveRequestType.class)));
                will(returnValue(null));
            }
        });
        RespondingGatewayCrossGatewayRetrieveRequestType req = new RespondingGatewayCrossGatewayRetrieveRequestType();
        assertNull(testSubject.getDocRequest(req));
    }

    @Test
    public void testGetTargetCommunitiesHappy() {
        final ExtractEntityDocRetrieveDeferredRequestValues mockExtractEntityDocRetrieveDeferredRequestValues = mockery.mock(ExtractEntityDocRetrieveDeferredRequestValues.class);
        final NhinTargetCommunitiesType mockNhinTargetCommunities = mockery.mock(NhinTargetCommunitiesType.class);
        EntityDocRetrieveDeferredReq testSubject = new EntityDocRetrieveDeferredReq();
        mockery.checking(new Expectations() {

            {
                allowing(mockExtractEntityDocRetrieveDeferredRequestValues).extractNhinTargetCommunities(with(any(RespondingGatewayCrossGatewayRetrieveRequestType.class)));
                will(returnValue(mockNhinTargetCommunities));
            }
        });
        RespondingGatewayCrossGatewayRetrieveRequestType req = new RespondingGatewayCrossGatewayRetrieveRequestType();
        req.setNhinTargetCommunities(mockNhinTargetCommunities);
        assertNotNull(testSubject.getTargetCommunities(req));
        assertEquals(testSubject.getTargetCommunities(req), mockNhinTargetCommunities);
    }

    @Test
    public void testGetTargetCommunitiesBad() {
        final ExtractEntityDocRetrieveDeferredRequestValues mockExtractEntityDocRetrieveDeferredRequestValues = mockery.mock(ExtractEntityDocRetrieveDeferredRequestValues.class);
        EntityDocRetrieveDeferredReq testSubject = new EntityDocRetrieveDeferredReq();
        mockery.checking(new Expectations() {

            {
                allowing(mockExtractEntityDocRetrieveDeferredRequestValues).extractNhinTargetCommunities(with(any(RespondingGatewayCrossGatewayRetrieveRequestType.class)));
                will(returnValue(null));
            }
        });
        RespondingGatewayCrossGatewayRetrieveRequestType req = new RespondingGatewayCrossGatewayRetrieveRequestType();
        assertNull(testSubject.getTargetCommunities(req));
    }
}
