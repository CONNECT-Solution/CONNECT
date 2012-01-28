/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.orchestration;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
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
public class OutboundOrchestratableTest {

    public OutboundOrchestratableTest() {
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
     * Test of getNhinDelegate method, of class OutboundOrchestratable.
     */
    @Test
    public void testGetNhinDelegate() {
        OutboundOrchestratable instance = new EntityOrchestratableImpl();
        OutboundDelegate expResult = null;
        OutboundDelegate result = instance.getDelegate();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetAggregator() {
        OutboundOrchestratable instance = new EntityOrchestratableImpl();
        NhinAggregator expResult = null;
        NhinAggregator result = instance.getAggregator();
        assertEquals(expResult, result);
    }

    public class EntityOrchestratableImpl implements OutboundOrchestratable {

        public OutboundDelegate getNhinDelegate() {
            return null;
        }

        public boolean isEnabled() {
            return true;
        }

        public boolean isPassthru() {
            return false;
        }

        public AuditTransformer getAuditTransformer() {
            return null;
        }

        public PolicyTransformer getPolicyTransformer() {
            return null;
        }

        public AssertionType getAssertion()
        {
            return null;
        }

        public String getServiceName() {
            return "";
        }

        public NhinAggregator getAggregator() {
            return null;
        }

		@Override
		public OutboundDelegate getDelegate() {
			// TODO Auto-generated method stub
			return null;
		}
    }

}