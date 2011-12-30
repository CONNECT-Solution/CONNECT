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
    public void testGetResponse() {
        System.out.println("getResponse");
        EntityDocRetrieveOrchestratableImpl_a0 instance = null;
        RetrieveDocumentSetResponseType expResult = null;
        RetrieveDocumentSetResponseType result = instance.getResponse();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setResponse method, of class EntityDocRetrieveOrchestratableImpl_a0.
     */
    @Test
    public void testSetResponse() {
        System.out.println("setResponse");
        RetrieveDocumentSetResponseType response = null;
        EntityDocRetrieveOrchestratableImpl_a0 instance = null;
        instance.setResponse(response);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}