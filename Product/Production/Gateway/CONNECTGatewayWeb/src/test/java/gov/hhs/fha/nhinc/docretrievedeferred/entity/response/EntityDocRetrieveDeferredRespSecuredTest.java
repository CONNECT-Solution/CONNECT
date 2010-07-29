package gov.hhs.fha.nhinc.docretrievedeferred.entity.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
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
        EntityDocRetrieveDeferredRespSecured testSubject = new EntityDocRetrieveDeferredRespSecured()
        {
            @Override
            protected AssertionType extractAssertionInfo() {
                return mockAssertion;
            }
        };
        RespondingGatewayCrossGatewayRetrieveSecuredResponseType req = new RespondingGatewayCrossGatewayRetrieveSecuredResponseType();
        assertNotNull(testSubject.crossGatewayRetrieveResponse(req));
    }

}