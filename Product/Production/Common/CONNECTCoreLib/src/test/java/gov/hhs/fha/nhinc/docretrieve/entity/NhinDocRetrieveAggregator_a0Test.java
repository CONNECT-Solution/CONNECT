/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.orchestration.EntityOrchestratable;
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
public class NhinDocRetrieveAggregator_a0Test {

    public NhinDocRetrieveAggregator_a0Test() {
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
     * Test of aggregate method, of class NhinDocRetrieveAggregator_a0.
     */
    @Test
    public void testAggregate() {
        System.out.println("aggregate");
        EntityOrchestratable to = null;
        EntityOrchestratable from = null;
        NhinDocRetrieveAggregator_a0 instance = new NhinDocRetrieveAggregator_a0();
        instance.aggregate(to, from);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}