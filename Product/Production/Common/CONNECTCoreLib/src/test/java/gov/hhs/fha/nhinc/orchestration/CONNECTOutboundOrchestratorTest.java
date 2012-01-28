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
public class CONNECTOutboundOrchestratorTest {

    public CONNECTOutboundOrchestratorTest() {
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
        Orchestratable message = new EntityOrchestratableImpl();
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
        Log result = instance.getLogger();
        try {
            result.info("testing CONNECTEntityOrchestrator getLogger()");
        }
        catch (Exception exc)
        {
            fail("An exception has occurred:" + exc.getMessage());
        }
    }

    /**
     * Test of delegateToNhin method, of class CONNECTInboundOrchestrator.
     */
    @Test
    public void testDelegateToNhin() {
        OutboundOrchestratable message = new EntityOrchestratableImpl();
        CONNECTInboundOrchestrator instance = new CONNECTInboundOrchestrator();
        instance.delegate(message);
        // there was no error, so success
        // TODO: make a better test
    }

    public class EntityOrchestratableImpl implements OutboundOrchestratable {

        public EntityOrchestratableImpl()
        {
            
        }
        
        public OutboundDelegate getNhinDelegate() {
            InboundDelegateTest test = new InboundDelegateTest();
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

        public NhinAggregator getAggregator() {
            return null;
        }

        public AssertionType getAssertion() {
            return null;
        }

        public String getServiceName() {
            return "";
        }

		@Override
		public OutboundDelegate getDelegate() {
			return getNhinDelegate();
		}
    }
}