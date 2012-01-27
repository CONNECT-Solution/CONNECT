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
public class CONNECTInboundOrchestratorTest {

    public CONNECTInboundOrchestratorTest() {
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
     * Test of process method, of class CONNECTInboundOrchestrator.
     */
    @Test
    public void testProcess() {
        Orchestratable message = new NhinOrchestratableImpl();
        CONNECTInboundOrchestrator instance = new CONNECTInboundOrchestrator();
        instance.process(message);
        // there was no error, so success
        // TODO: make a better test
    }

    /**
     * Test of getLogger method, of class CONNECTInboundOrchestrator.
     */
    @Test
    public void testGetLogger() {
        CONNECTInboundOrchestrator instance = new CONNECTInboundOrchestrator();
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
     * Test of delegateToAdapter method, of class CONNECTInboundOrchestrator.
     */
    @Test
    public void testDelegateToAdapter() {
        InboundOrchestratable message = new NhinOrchestratableImpl();
        CONNECTInboundOrchestrator instance = new CONNECTInboundOrchestrator();
        instance.delegate(message);
        // there was no error, so success
        // TODO: make a better test
    }
    public class NhinOrchestratableImpl implements InboundOrchestratable {

        public NhinOrchestratableImpl()
        {

        }

        public InboundDelegate getAdapterDelegate() {
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

        public String getServiceName() {
            return "";
        }

		@Override
		public Delegate getDelegate() {
			return getAdapterDelegate();
		}
    }
}