package gov.hhs.fha.nhinc.docretrievedeferred.entity.response;

import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveResponseType;
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
      EntityDocRetrieveDeferredResp testSubject = new EntityDocRetrieveDeferredResp();
      RespondingGatewayCrossGatewayRetrieveResponseType crossGatewayRetrieveResponse = new RespondingGatewayCrossGatewayRetrieveResponseType();
      assertNotNull(testSubject.crossGatewayRetrieveResponse(crossGatewayRetrieveResponse));
    }
}