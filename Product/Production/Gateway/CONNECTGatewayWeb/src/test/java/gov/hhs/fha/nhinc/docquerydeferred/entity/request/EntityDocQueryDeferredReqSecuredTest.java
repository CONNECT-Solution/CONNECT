package gov.hhs.fha.nhinc.docquerydeferred.entity.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQuerySecuredRequestType;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import javax.xml.ws.WebServiceContext;
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
 * @author Mark Goldman
 */
public class EntityDocQueryDeferredReqSecuredTest {

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
   * Test of respondingGatewayCrossGatewayQuery method, of class EntityDocQueryDeferredReqSecured.
   */
  @Test
  public void testRespondingGatewayCrossGatewayQuery() {
    final EntityDocQueryDeferredRequestHelper mockRequestHelper = mockery.mock(EntityDocQueryDeferredRequestHelper.class);
    final EntityDocQueryDeferredReqImpl mockServiceImpl = mockery.mock(EntityDocQueryDeferredReqImpl.class);

    final DocQueryAcknowledgementType mockAcknowledgementType = mockery.mock(DocQueryAcknowledgementType.class);
    final RegistryResponseType mockRespType = mockery.mock(RegistryResponseType.class);

    EntityDocQueryDeferredReqSecured testSubject = new EntityDocQueryDeferredReqSecured() {

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
        one(mockRequestHelper).getAdhocQueryRequest(with(any(RespondingGatewayCrossGatewayQuerySecuredRequestType.class)));
        one(mockRequestHelper).getAssertion(with(any(WebServiceContext.class)));
        one(mockRequestHelper).getNhinTargetCommunities(with(any(RespondingGatewayCrossGatewayQuerySecuredRequestType.class)));
        one(mockAcknowledgementType).getMessage();
        will(returnValue(mockRespType));
        one(mockRespType).getStatus();
        will(returnValue("Success"));
        one(mockServiceImpl).respondingGatewayCrossGatewayQuery(with(any(AdhocQueryRequest.class)), with(any(AssertionType.class)), with(any(NhinTargetCommunitiesType.class)));
        will(returnValue(mockAcknowledgementType));
      }
    });

    DocQueryAcknowledgementType result = testSubject.respondingGatewayCrossGatewayQuery(new RespondingGatewayCrossGatewayQuerySecuredRequestType());
    assertEquals("Success", result.getMessage().getStatus());

    mockery.assertIsSatisfied();
  }

  /**
   * Test of getEntityDocQueryDeferredRequestHelper method, of class EntityDocQueryDeferredReqSecured.
   */
  @Test
  public void testGetEntityDocQueryDeferredRequestHelper() {
    EntityDocQueryDeferredReqSecured testSubject = new EntityDocQueryDeferredReqSecured();

    EntityDocQueryDeferredRequestHelper result = testSubject.getEntityDocQueryDeferredRequestHelper();
    assertNotNull(result);
  }

  /**
   * Test of getEntityDocQueryDeferredReqImpl method, of class EntityDocQueryDeferredReqSecured.
   */
  @Test
  public void testGetEntityDocQueryDeferredReqImpl() {
    EntityDocQueryDeferredReqSecured testSubject = new EntityDocQueryDeferredReqSecured();

    EntityDocQueryDeferredReqImpl result = testSubject.getEntityDocQueryDeferredReqImpl();
    assertNotNull(result);
  }
}
