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

    @Test
    public void testCrossGatewayRetrieveRequestHappy() {
        final EntityDocRetrieveDeferredReqImpl mockEntityDocRetrieveDeferredReqImpl = mockery.mock(EntityDocRetrieveDeferredReqImpl.class);
        final RetrieveDocumentSetRequestType mockRetrieveDocumentSetRequestType = mockery.mock(RetrieveDocumentSetRequestType.class);
        final AssertionType mockAssertionType = mockery.mock(AssertionType.class);
        final NhinTargetCommunitiesType mockNhinTargetCommunitiesType = mockery.mock(NhinTargetCommunitiesType.class);
        RespondingGatewayCrossGatewayRetrieveRequestType req = new RespondingGatewayCrossGatewayRetrieveRequestType();
        EntityDocRetrieveDeferredReq retrieveRequest = new EntityDocRetrieveDeferredReq();
        mockery.checking(new Expectations() {
            {
                allowing(mockEntityDocRetrieveDeferredReqImpl).crossGatewayRetrieveRequest(mockRetrieveDocumentSetRequestType, mockAssertionType, mockNhinTargetCommunitiesType);
                will(returnValue(null));
            }
        });
        NhinTargetCommunitiesType targets = new NhinTargetCommunitiesType();
        req.setNhinTargetCommunities(targets);
        RetrieveDocumentSetRequestType retrieveDocumentSetRequestType = new RetrieveDocumentSetRequestType();
        req.setRetrieveDocumentSetRequest(retrieveDocumentSetRequestType);
        AssertionType assertionType = new AssertionType();
        req.setAssertion(assertionType);
        DocRetrieveAcknowledgementType ack = retrieveRequest.crossGatewayRetrieveRequest(req);
        assertNotNull("Ack was null", ack);
        mockery.assertIsSatisfied();
    }
    
    
}
