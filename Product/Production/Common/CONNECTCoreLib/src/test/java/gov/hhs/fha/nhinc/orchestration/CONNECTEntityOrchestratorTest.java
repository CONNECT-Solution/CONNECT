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
public class CONNECTEntityOrchestratorTest {

    public CONNECTEntityOrchestratorTest() {
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
     * Test of process method, of class CONNECTEntityOrchestrator.
     */
    @Test
    public void testProcess() {
        System.out.println("process");
        Orchestratable message = new EntityOrchestratableImpl();
        CONNECTEntityOrchestrator instance = new CONNECTEntityOrchestrator();
        instance.process(message);
        // there was no error, so success
        // TODO: make a better test
    }

    /**
     * Test of getLogger method, of class CONNECTEntityOrchestrator.
     */
    @Test
    public void testGetLogger() {
        System.out.println("getLogger");
        CONNECTEntityOrchestrator instance = new CONNECTEntityOrchestrator();
        Log result = instance.getLogger();
        try {
            result.info("testing CONNECTEntityOrchestrator getLogger()");
        }
        catch (Exception exc)
        {
            fail("The test case is a prototype.");
        }
    }

    /**
     * Test of delegateToNhin method, of class CONNECTEntityOrchestrator.
     */
    @Test
    public void testDelegateToNhin() {
        System.out.println("delegateToNhin");
        EntityOrchestratable message = new EntityOrchestratableImpl();
        CONNECTEntityOrchestrator instance = new CONNECTEntityOrchestrator();
        instance.delegateToNhin(message);
        // there was no error, so success
        // TODO: make a better test
    }

    public class EntityOrchestratableImpl implements EntityOrchestratable {

        public EntityOrchestratableImpl()
        {
            
        }
        
        public NhinDelegate getNhinDelegate() {
            NhinDelegateTest test = new NhinDelegateTest();
            return test.new NhinDelegateImpl();
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