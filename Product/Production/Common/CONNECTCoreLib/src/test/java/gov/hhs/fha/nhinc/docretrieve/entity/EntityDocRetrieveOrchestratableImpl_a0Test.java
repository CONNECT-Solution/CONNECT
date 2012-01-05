/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.entity;

import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mweaver
 */
public class EntityDocRetrieveOrchestratableImpl_a0Test {

    public EntityDocRetrieveOrchestratableImpl_a0Test() {
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

    /**
     * Test of getResponse method, of class EntityDocRetrieveOrchestratableImpl_a0.
     */
    @Test
    public void testResponse() {
        EntityDocRetrieveOrchestratableFactory factory = new EntityDocRetrieveOrchestratableFactory();
        EntityDocRetrieveOrchestratableImpl_a0 instance = factory.getEntityDocRetrieveOrchestratableImpl_a0();
        RetrieveDocumentSetResponseType expResult = factory.getRetrieveDocumentSetResponseType();
        instance.setResponse(expResult);
        RetrieveDocumentSetResponseType result = instance.getResponse();
        assertEquals(expResult, result);
    }
}