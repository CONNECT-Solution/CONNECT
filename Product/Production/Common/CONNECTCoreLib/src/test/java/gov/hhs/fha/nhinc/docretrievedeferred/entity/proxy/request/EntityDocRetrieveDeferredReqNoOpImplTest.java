package gov.hhs.fha.nhinc.docretrievedeferred.entity.proxy.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
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
public class EntityDocRetrieveDeferredReqNoOpImplTest {

    public EntityDocRetrieveDeferredReqNoOpImplTest() {
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
    public void test()
    {
        EntityDocRetrieveDeferredReqNoOpImpl testSubject = new EntityDocRetrieveDeferredReqNoOpImpl();
        RetrieveDocumentSetRequestType message = new RetrieveDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        NhinTargetCommunitiesType target = new NhinTargetCommunitiesType();
        assertNotNull(testSubject.crossGatewayRetrieveRequest(message, assertion, target));
        assertEquals("Success", testSubject.crossGatewayRetrieveRequest(message, assertion, target).getMessage().getStatus() );
    }
}