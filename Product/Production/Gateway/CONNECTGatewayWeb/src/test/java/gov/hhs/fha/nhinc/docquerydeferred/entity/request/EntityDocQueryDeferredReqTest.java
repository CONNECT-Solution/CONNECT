package gov.hhs.fha.nhinc.docquerydeferred.entity.request;

import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import org.jmock.Mockery;
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
    EntityDocQueryDeferredReq testSubject = new EntityDocQueryDeferredReq();
    RespondingGatewayCrossGatewayQueryRequestType respondingGatewayCrossGatewayQueryRequest = new RespondingGatewayCrossGatewayQueryRequestType();
    DocQueryAcknowledgementType expResult = null;
    DocQueryAcknowledgementType result = testSubject.respondingGatewayCrossGatewayQuery(respondingGatewayCrossGatewayQueryRequest);
    assertEquals(expResult, result);
  }
}
