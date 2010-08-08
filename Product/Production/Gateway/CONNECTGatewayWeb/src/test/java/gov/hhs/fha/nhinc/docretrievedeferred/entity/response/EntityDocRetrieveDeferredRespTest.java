package gov.hhs.fha.nhinc.docretrievedeferred.entity.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveResponseType;
import gov.hhs.fha.nhinc.docretrieve.entity.deferred.response.EntityDocRetrieveDeferredRespImpl;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
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
public class EntityDocRetrieveDeferredRespTest {
    private Mockery mockery = null;

    public EntityDocRetrieveDeferredRespTest() {
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
      EntityDocRetrieveDeferredResp testSubject = new EntityDocRetrieveDeferredResp();
      assertNull(testSubject.crossGatewayRetrieveResponse(null));
    }

    @Test
    public void testCrossGatewayRetrieveResponseHappy()
    {
      final DocRetrieveAcknowledgementType mockDocRetrieveAcknowledgementType = mockery.mock(DocRetrieveAcknowledgementType.class);
      final EntityDocRetrieveDeferredRespImpl mockEntityDocRetrieveDeferredRespImpl = mockery.mock(EntityDocRetrieveDeferredRespImpl.class);
      EntityDocRetrieveDeferredResp testSubject = new EntityDocRetrieveDeferredResp()
      {
          @Override
          protected DocRetrieveAcknowledgementType sendToCrossGatewayRetrieveResponseImpl(RetrieveDocumentSetResponseType retrieveDocumentSetResponse, AssertionType assertion, NhinTargetCommunitiesType nhinTargetCommunities) {
            return mockDocRetrieveAcknowledgementType;
          }
      };

      mockery.checking(new Expectations(){
          {
              allowing(mockEntityDocRetrieveDeferredRespImpl).crossGatewayRetrieveResponse(with(any(RetrieveDocumentSetResponseType.class)), with(any(AssertionType.class)), with(any(NhinTargetCommunitiesType.class)));
              will(returnValue(mockDocRetrieveAcknowledgementType));
          }
      });
      RetrieveDocumentSetResponseType retrieveDocumentSetResponse = new RetrieveDocumentSetResponseType();
      AssertionType assertion = new AssertionType();
      NhinTargetCommunitiesType target = new NhinTargetCommunitiesType();
      RespondingGatewayCrossGatewayRetrieveResponseType crossGatewayRetrieveResponse = new RespondingGatewayCrossGatewayRetrieveResponseType();
      crossGatewayRetrieveResponse.setRetrieveDocumentSetResponse(retrieveDocumentSetResponse);
      crossGatewayRetrieveResponse.setAssertion(assertion);
      crossGatewayRetrieveResponse.setNhinTargetCommunities(target);
      assertNotNull(testSubject.crossGatewayRetrieveResponse(crossGatewayRetrieveResponse));
    }

    @Test
    public void testCrossGatewayRetrieveResponseHappySomethingWentBad()
    {
      final DocRetrieveAcknowledgementType mockDocRetrieveAcknowledgementType = mockery.mock(DocRetrieveAcknowledgementType.class);
      final EntityDocRetrieveDeferredRespImpl mockEntityDocRetrieveDeferredRespImpl = mockery.mock(EntityDocRetrieveDeferredRespImpl.class);
      EntityDocRetrieveDeferredResp testSubject = new EntityDocRetrieveDeferredResp()
      {
          @Override
          protected DocRetrieveAcknowledgementType sendToCrossGatewayRetrieveResponseImpl(RetrieveDocumentSetResponseType retrieveDocumentSetResponse, AssertionType assertion, NhinTargetCommunitiesType nhinTargetCommunities) {
            return null;
          }
      };

      mockery.checking(new Expectations(){
          {
              allowing(mockEntityDocRetrieveDeferredRespImpl).crossGatewayRetrieveResponse(with(any(RetrieveDocumentSetResponseType.class)), with(any(AssertionType.class)), with(any(NhinTargetCommunitiesType.class)));
              will(returnValue(null));
          }
      });
      RetrieveDocumentSetResponseType retrieveDocumentSetResponse = new RetrieveDocumentSetResponseType();
      AssertionType assertion = new AssertionType();
      NhinTargetCommunitiesType target = new NhinTargetCommunitiesType();
      RespondingGatewayCrossGatewayRetrieveResponseType crossGatewayRetrieveResponse = new RespondingGatewayCrossGatewayRetrieveResponseType();
      crossGatewayRetrieveResponse.setRetrieveDocumentSetResponse(retrieveDocumentSetResponse);
      crossGatewayRetrieveResponse.setAssertion(assertion);
      crossGatewayRetrieveResponse.setNhinTargetCommunities(target);
      assertNull(testSubject.crossGatewayRetrieveResponse(crossGatewayRetrieveResponse));
    }
}
