/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.orchestration;

import gov.hhs.fha.nhinc.orchestration.CONNECTEntityOrchestratorTest.EntityOrchestratableImpl;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer.Direction;
import org.apache.commons.logging.Log;
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
public class CONNECTOrchestrationBaseTest {

    public CONNECTOrchestrationBaseTest() {
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
     * Test of getLogger method, of class CONNECTOrchestrationBase.
     */
    @Test
    public void testGetLogger() {
        CONNECTOrchestrationBase instance = new CONNECTOrchestrationBaseImpl();
        Log expResult = null;
        Log result = instance.getLogger();
        try {
            result.info("testing CONNECTOrchestrationBase getLogger()");
        } catch (Exception exc) {
            fail("The test case is a prototype.");
        }
    }

    /**
     * Test of audit method, of class CONNECTOrchestrationBase.
     */
    @Test
    public void testAuditRequest() {
        Orchestratable message = null;
        CONNECTOrchestrationBase instance = new CONNECTOrchestrationBaseImpl();
        instance.auditRequest(message);
        // there was no error, so success
        // TODO: make a better test
    }

    @Test
    public void testAuditResponse() {
        Orchestratable message = null;
        CONNECTOrchestrationBase instance = new CONNECTOrchestrationBaseImpl();
        instance.auditResponse(message);
        // there was no error, so success
        // TODO: make a better test
    }

    /**
     * Test of isAuditServiceEnabled method, of class CONNECTOrchestrationBase.
     */
    @Test
    public void testIsAuditServiceEnabled() {
        CONNECTOrchestrationBase instance = new CONNECTOrchestrationBaseImpl();
        boolean expResult = true;
        boolean result = instance.isAuditServiceEnabled();
        assertEquals(expResult, result);
    }

    /**
     * Test of isPolicyOk method, of class CONNECTOrchestrationBase.
     */
    @Test
    public void testIsPolicyOk() {
        Orchestratable message = null;
        Direction direction = null;
        CONNECTOrchestrationBase instance = new CONNECTOrchestrationBaseImpl();
        boolean expResult = false;
        boolean result = instance.isPolicyOk(message, direction);
        assertEquals(expResult, result);
    }

    public class CONNECTOrchestrationBaseImpl extends CONNECTOrchestrationBase {
    }
}
