/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.entity;

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
public class NhinDocRetrieveStrategyTest {

    public NhinDocRetrieveStrategyTest() {
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
     * Test of execute method, of class NhinDocRetrieveStrategy.
     */
    @Test
    public void testExecute() {
        System.out.println("execute");
        EntityDocRetrieveOrchestratable message = null;
        NhinDocRetrieveStrategy instance = new NhinDocRetrieveStrategyImpl();
        instance.execute(message);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public class NhinDocRetrieveStrategyImpl implements NhinDocRetrieveStrategy {

        public void execute(EntityDocRetrieveOrchestratable message) {
        }
    }

}