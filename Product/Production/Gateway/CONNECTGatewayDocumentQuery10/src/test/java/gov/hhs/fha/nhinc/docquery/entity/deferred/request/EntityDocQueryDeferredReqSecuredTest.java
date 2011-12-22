package gov.hhs.fha.nhinc.docquery.entity.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQuerySecuredRequestType;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;

import org.jmock.Mockery;
import org.jmock.Expectations;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Ignore;
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
  @Ignore
  public void testRespondingGatewayCrossGatewayQuery_rem() {
    final EntityDocQueryDeferredReqOrchImpl mockOrchImpl = mockery.mock(EntityDocQueryDeferredReqOrchImpl.class);

    final RespondingGatewayCrossGatewayQuerySecuredRequestType mockReq = mockery.mock(RespondingGatewayCrossGatewayQuerySecuredRequestType.class);
    final DocQueryAcknowledgementType expected = new DocQueryAcknowledgementType();

    EntityDocQueryDeferredReqSecured testSubject = new EntityDocQueryDeferredReqSecured() {

      @Override
      protected EntityDocQueryDeferredReqOrchImpl getOrchImpl() {
        return mockOrchImpl;
      }

      @Override
      protected AssertionType extractAssertion(WebServiceContext context) {
        return new AssertionType();
      }
    };

    mockery.checking(new Expectations() {

      {
        one(mockReq).getAdhocQueryRequest();
        one(mockReq).getNhinTargetCommunities();
        one(mockOrchImpl).respondingGatewayCrossGatewayQuery(
                with(any(AdhocQueryRequest.class)),
                with(any(AssertionType.class)),
                with(any(NhinTargetCommunitiesType.class)));
        will(returnValue(expected));

      }
    });

    DocQueryAcknowledgementType result = testSubject.respondingGatewayCrossGatewayQuery(new RespondingGatewayCrossGatewayQuerySecuredRequestType());
    assertSame(result, expected);

    mockery.assertIsSatisfied();
  }

   /**
   * Test of respondingGatewayCrossGatewayQuery method, of class EntityDocQueryDeferredReqSecured.
   */
  @Test
  public void testRespondingGatewayCrossGatewayQuery() {
    final DocQueryAcknowledgementType expected = new DocQueryAcknowledgementType();

    EntityDocQueryDeferredReqSecured testSubject = new EntityDocQueryDeferredReqSecured() {

      @Override
      protected DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(
          final RespondingGatewayCrossGatewayQuerySecuredRequestType body, final WebServiceContext context) {
        return expected;
      }
    };

    DocQueryAcknowledgementType result = testSubject.respondingGatewayCrossGatewayQuery(new RespondingGatewayCrossGatewayQuerySecuredRequestType());
    assertSame(result, expected);
  }
}
