/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.orchestration;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
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
public class CONNECTNhinOrchestratorTest {

    public CONNECTNhinOrchestratorTest() {
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
     * Test of process method, of class CONNECTNhinOrchestrator.
     */
    @Test
    public void testProcess() {
        System.out.println("process");
        Orchestratable message = new NhinOrchestratableImpl();
        CONNECTNhinOrchestrator instance = new CONNECTNhinOrchestrator();
        instance.process(message);
        // there was no error, so success
        // TODO: make a better test
    }

    /**
     * Test of getLogger method, of class CONNECTNhinOrchestrator.
     */
    @Test
    public void testGetLogger() {
        System.out.println("getLogger");
        CONNECTNhinOrchestrator instance = new CONNECTNhinOrchestrator();
        Log expResult = null;
        Log result = instance.getLogger();
        try {
            result.info("testing CONNECTNhinOrchestrator getLogger()");
        }
        catch (Exception exc)
        {
            fail("The test case is a prototype.");
        }
    }

    /**
     * Test of delegateToAdapter method, of class CONNECTNhinOrchestrator.
     */
    @Test
    public void testDelegateToAdapter() {
        System.out.println("delegateToAdapter");
        NhinOrchestratable message = new NhinOrchestratableImpl();
        CONNECTNhinOrchestrator instance = new CONNECTNhinOrchestrator();
        instance.delegateToAdapter(message);
        // there was no error, so success
        // TODO: make a better test
    }
    public class NhinOrchestratableImpl implements NhinOrchestratable {

        public NhinOrchestratableImpl()
        {

        }

        public AdapterDelegate getAdapterDelegate() {
            AdapterDelegateTest test = new AdapterDelegateTest();
            return test.new AdapterDelegateImpl();
        }

        public boolean isEnabled() {
            return true;
        }

        public boolean isPassthru() {
            return true;
        }

        public AuditTransformer getAuditTransformer() {
            return null;
        }

        public PolicyTransformer getPolicyTransformer() {
            return null;
        }

        public AssertionType getAssertion() {
            return null;
        }
    }
}