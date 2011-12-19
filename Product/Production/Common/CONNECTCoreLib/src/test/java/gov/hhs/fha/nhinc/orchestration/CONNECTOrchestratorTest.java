/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.orchestration;

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
public class CONNECTOrchestratorTest {

    public CONNECTOrchestratorTest() {
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
     * Test of process method, of class CONNECTOrchestrator.
     */
    @Test
    public void testProcess() {
        System.out.println("process");
        Orchestratable message = null;
        CONNECTOrchestrator instance = new CONNECTOrchestratorImpl();
        instance.process(message);
        // there was no error, so success
        // TODO: make a better test
    }

    public class CONNECTOrchestratorImpl implements CONNECTOrchestrator {

        public void process(Orchestratable message) {
        }
    }

}