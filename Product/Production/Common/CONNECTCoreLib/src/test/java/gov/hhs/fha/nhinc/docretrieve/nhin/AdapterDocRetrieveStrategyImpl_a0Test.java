/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.nhin;

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
public class AdapterDocRetrieveStrategyImpl_a0Test {

    public AdapterDocRetrieveStrategyImpl_a0Test() {
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
     * Test of execute method, of class AdapterDocRetrieveStrategyImpl_a0.
     */
    @Test
    public void testExecute() {
        NhinDocRetrieveOrchestratable message = null;
        AdapterDocRetrieveStrategyImpl_a0 instance = new AdapterDocRetrieveStrategyImpl_a0();
        instance.execute(message);
        // TODO: update this test once we can mock NhinDocRetrieveOrchestratable
    }

}