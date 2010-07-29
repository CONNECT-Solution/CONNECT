package gov.hhs.fha.nhinc.docretrievedeferred.entity.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import javax.xml.ws.WebServiceContext;
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
public class EntityDocRetrieveDeferredRespSecuredTest {
    private Mockery mockery = null;

    public EntityDocRetrieveDeferredRespSecuredTest() {
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
    public void testCrossGatewayRetrieveResponseBad()
    {
        EntityDocRetrieveDeferredRespSecured testSubject = new EntityDocRetrieveDeferredRespSecured();
        assertNull(testSubject.crossGatewayRetrieveResponse(null));
    }

    @Test
    public void testCrossGatewayRetrieveResponseHappy()
    {
        final AssertionType mockAssertion = mockery.mock(AssertionType.class);
        final DocRetrieveAcknowledgementType mockDocRetrieveAcknowledgementType = mockery.mock(DocRetrieveAcknowledgementType.class);
        final EntityDocRetrieveDeferredRespImpl mockEntityDocRetrieveDeferredRespImpl = mockery.mock(EntityDocRetrieveDeferredRespImpl.class);
        final RetrieveDocumentSetResponseType retrieveDocumentSetResponse = new RetrieveDocumentSetResponseType();
        final NhinTargetCommunitiesType target = new NhinTargetCommunitiesType();
        EntityDocRetrieveDeferredRespSecured testSubject = new EntityDocRetrieveDeferredRespSecured()
        {
            @Override
            protected AssertionType extractAssertionInfo() {
                return mockAssertion;
            }

            @Override
            protected DocRetrieveAcknowledgementType sendToCrossGatewayRetrieveResponseImpl(RetrieveDocumentSetResponseType retrieveDocumentSetResponse, AssertionType assertion, NhinTargetCommunitiesType nhinTargetCommunities) {
                return mockDocRetrieveAcknowledgementType;
            }
        };

        mockery.checking(new Expectations(){
            {
                allowing(mockEntityDocRetrieveDeferredRespImpl).crossGatewayRetrieveResponse(retrieveDocumentSetResponse , mockAssertion, target);
                will(returnValue(mockDocRetrieveAcknowledgementType));
            }
        });
        
        RespondingGatewayCrossGatewayRetrieveSecuredResponseType req = new RespondingGatewayCrossGatewayRetrieveSecuredResponseType();
        req.setRetrieveDocumentSetResponse(retrieveDocumentSetResponse);
        req.setNhinTargetCommunities(target);
        assertNotNull(testSubject.crossGatewayRetrieveResponse(req));
    }

}