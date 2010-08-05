package gov.hhs.fha.nhinc.docquerydeferred.entity.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.jmock.Mockery;
import org.jmock.Expectations;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author goldmanm
 */
public class EntityDocQueryDeferredReqTest {

  Mockery mockery;

  @Before
  public void setup() {
    mockery = new Mockery() {

      {
        setImposteriser(ClassImposteriser.INSTANCE);
      }
    };
  }

  /**
   * Test of respondingGatewayCrossGatewayQuery method, of class EntityDocQueryDeferredReq.
   */
  @Test
  public void testRespondingGatewayCrossGatewayQuery() {
    final EntityDocQueryDeferredRequestHelper mockRequestHelper = mockery.mock(EntityDocQueryDeferredRequestHelper.class);
    final EntityDocQueryDeferredReqImpl mockServiceImpl = mockery.mock(EntityDocQueryDeferredReqImpl.class);
    final DocQueryAcknowledgementType mockAcknowledgementType = mockery.mock(DocQueryAcknowledgementType.class);
    final RegistryResponseType mockRespType = mockery.mock(RegistryResponseType.class);

    EntityDocQueryDeferredReq testSubject = new EntityDocQueryDeferredReq() {

      @Override
      protected EntityDocQueryDeferredRequestHelper getEntityDocQueryDeferredRequestHelper() {
        return mockRequestHelper;
      }

      @Override
      protected EntityDocQueryDeferredReqImpl getEntityDocQueryDeferredReqImpl() {
        return mockServiceImpl;
      }
    };

    mockery.checking(new Expectations() {

      {
        one(mockRequestHelper).getAdhocQueryRequest(with(any(RespondingGatewayCrossGatewayQueryRequestType.class)));
        one(mockRequestHelper).getAssertion(with(any(RespondingGatewayCrossGatewayQueryRequestType.class)));
        one(mockRequestHelper).getNhinTargetCommunities(with(any(RespondingGatewayCrossGatewayQueryRequestType.class)));
        one(mockAcknowledgementType).getMessage();
        will(returnValue(mockRespType));
        one(mockRespType).getStatus();
        will(returnValue("Success"));
        one(mockServiceImpl).respondingGatewayCrossGatewayQuery(with(any(AdhocQueryRequest.class)), with(any(AssertionType.class)), with(any(NhinTargetCommunitiesType.class)));
        will(returnValue(mockAcknowledgementType));
      }
    });

    DocQueryAcknowledgementType result = testSubject.respondingGatewayCrossGatewayQuery(new RespondingGatewayCrossGatewayQueryRequestType());
    assertEquals("Success", result.getMessage().getStatus());

    mockery.assertIsSatisfied();
  }

  /**
   * Test of getEntityDocQueryDeferredRequestHelper method, of class EntityDocQueryDeferredReq.
   */
  @Test
  public void testGetEntityDocQueryDeferredRequestHelper() {
    EntityDocQueryDeferredReq testSubject = new EntityDocQueryDeferredReq();

    EntityDocQueryDeferredRequestHelper result = testSubject.getEntityDocQueryDeferredRequestHelper();
    assertNotNull(result);
  }

  /**
   * Test of getEntityDocQueryDeferredReqImpl method, of class EntityDocQueryDeferredReq.
   */
  @Test
  public void testGetEntityDocQueryDeferredReqImpl() {
    EntityDocQueryDeferredReq testSubject = new EntityDocQueryDeferredReq();

    EntityDocQueryDeferredReqImpl result = testSubject.getEntityDocQueryDeferredReqImpl();
    assertNotNull(result);
  }
}
