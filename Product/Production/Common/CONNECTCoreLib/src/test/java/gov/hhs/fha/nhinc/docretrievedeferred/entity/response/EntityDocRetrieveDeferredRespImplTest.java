package gov.hhs.fha.nhinc.docretrievedeferred.entity.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
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
public class EntityDocRetrieveDeferredRespImplTest {

    public EntityDocRetrieveDeferredRespImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCrossGatewayRetrieveResponse()
    {
        EntityDocRetrieveDeferredRespImpl testSubject = new EntityDocRetrieveDeferredRespImpl();
        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
        AssertionType assertion = new AssertionType();
        NhinTargetCommunitiesType target = new NhinTargetCommunitiesType();
        assertNotNull(testSubject.crossGatewayRetrieveResponse(response, assertion, target));
    }

}