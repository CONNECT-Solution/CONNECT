/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docretrievedeferred.entity.request;

import gov.hhs.fha.nhinc.docretrieve.entity.deferred.request.EntityDocRetrieveDeferredReqImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author svalluripalli
 */
public class EntityDocRetrieveDeferredReqImplTest {

    public EntityDocRetrieveDeferredReqImplTest() {
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
    public void testCrossGatewayRetrieveRequest() {
        EntityDocRetrieveDeferredReqImpl testSubject = new EntityDocRetrieveDeferredReqImpl();
        assertNull(testSubject.crossGatewayRetrieveRequest(null, null, null));
    }
}
