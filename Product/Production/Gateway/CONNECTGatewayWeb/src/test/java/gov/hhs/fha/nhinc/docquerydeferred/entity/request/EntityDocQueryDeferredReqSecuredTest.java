package gov.hhs.fha.nhinc.docquerydeferred.entity.request;

import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQuerySecuredRequestType;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementsType;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author goldmanm
 */
public class EntityDocQueryDeferredReqSecuredTest {
  Mockery mockery;

	@Before
	public void setup(){
		mockery = new Mockery() {{
	        setImposteriser(ClassImposteriser.INSTANCE);
	    }};
	}

  /**
   * Test of respondingGatewayCrossGatewayQuery method, of class EntityDocQueryDeferredReqSecured.
   */
  @Test
  public void testRespondingGatewayCrossGatewayQuery() {
    EntityDocQueryDeferredReqSecured testSubject = new EntityDocQueryDeferredReqSecured();
    RespondingGatewayCrossGatewayQuerySecuredRequestType respondingGatewayCrossGatewayQueryRequest = new RespondingGatewayCrossGatewayQuerySecuredRequestType();
    DocQueryAcknowledgementsType expResult = null;
    DocQueryAcknowledgementsType result = testSubject.respondingGatewayCrossGatewayQuery(respondingGatewayCrossGatewayQueryRequest);
    assertEquals(expResult, result);
  }
}
