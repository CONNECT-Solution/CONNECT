package gov.hhs.fha.nhinc.docquery.entity.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType;
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
    final EntityDocQueryDeferredReqOrchImpl mockOrchImpl = mockery.mock(EntityDocQueryDeferredReqOrchImpl.class);
    final DocQueryAcknowledgementType mockAckType = mockery.mock(DocQueryAcknowledgementType.class);
    final RegistryResponseType mockRespType = mockery.mock(RegistryResponseType.class);

    EntityDocQueryDeferredReq testSubject = new EntityDocQueryDeferredReq() {

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
                with(any(RespondingGatewayCrossGatewayQueryRequestType.class)),
                with(any(WebServiceContext.class)));
        will(returnValue(mockAckType));
      }
    });

    DocQueryAcknowledgementType result = testSubject.respondingGatewayCrossGatewayQuery(new RespondingGatewayCrossGatewayQueryRequestType());
    assertEquals("Success", result.getMessage().getStatus());

    mockery.assertIsSatisfied();
  }

  /**
   * Test of getEntityDocQueryDeferredReqOrchImpl method, of class EntityDocQueryDeferredReq.
   */
  @Test
  public void testGetEntityDocQueryDeferredReqOrchImpl() {
    EntityDocQueryDeferredReq testSubject = new EntityDocQueryDeferredReq();

    EntityDocQueryDeferredReqOrchImpl result1 = testSubject.getEntityDocQueryDeferredReqOrchImpl();
    assertNotNull(result1);
    
    EntityDocQueryDeferredReqOrchImpl result2 = testSubject.getEntityDocQueryDeferredReqOrchImpl();
    assertSame(result2, result1);
  }

}
