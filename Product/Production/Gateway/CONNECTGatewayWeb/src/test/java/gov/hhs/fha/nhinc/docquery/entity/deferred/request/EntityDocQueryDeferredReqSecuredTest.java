package gov.hhs.fha.nhinc.docquery.entity.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQuerySecuredRequestType;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import javax.xml.ws.WebServiceContext;
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
    final EntityDocQueryDeferredReqOrchImpl mockOrchImpl = mockery.mock(EntityDocQueryDeferredReqOrchImpl.class);

    final DocQueryAcknowledgementType mockAckType = mockery.mock(DocQueryAcknowledgementType.class);
    final RegistryResponseType mockRespType = mockery.mock(RegistryResponseType.class);

    EntityDocQueryDeferredReqSecured testSubject = new EntityDocQueryDeferredReqSecured() {

      @Override
      protected EntityDocQueryDeferredReqOrchImpl getEntityDocQueryDeferredReqOrchImpl() {
        return mockOrchImpl;
      }
    };

    mockery.checking(new Expectations() {

      {
        one(mockAckType).getMessage();
        will(returnValue(mockRespType));
        one(mockRespType).getStatus();
        will(returnValue("Success"));
        one(mockOrchImpl).respondingGatewayCrossGatewayQuery(
                with(any(RespondingGatewayCrossGatewayQuerySecuredRequestType.class)),
                with(any(WebServiceContext.class)));
        will(returnValue(mockAckType));
      }
    });

    DocQueryAcknowledgementType result = testSubject.respondingGatewayCrossGatewayQuery(new RespondingGatewayCrossGatewayQuerySecuredRequestType());
    assertEquals("Success", result.getMessage().getStatus());

    mockery.assertIsSatisfied();
  }

  /**
   * Test of getEntityDocQueryDeferredReqImpl method, of class EntityDocQueryDeferredReqSecured.
   */
  @Test
  public void testGetEntityDocQueryDeferredReqOrchImpl() {
    EntityDocQueryDeferredReqSecured testSubject = new EntityDocQueryDeferredReqSecured();

    EntityDocQueryDeferredReqOrchImpl result1 = testSubject.getEntityDocQueryDeferredReqOrchImpl();
    assertNotNull(result1);

    EntityDocQueryDeferredReqOrchImpl result2 = testSubject.getEntityDocQueryDeferredReqOrchImpl();
    assertSame(result2, result1);
  }
}
