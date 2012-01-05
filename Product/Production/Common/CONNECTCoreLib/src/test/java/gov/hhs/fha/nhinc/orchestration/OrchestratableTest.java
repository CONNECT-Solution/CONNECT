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
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;

/**
 *
 * @author mweaver
 */
public class OrchestratableTest {

    public OrchestratableTest() {
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
     * Test of isEnabled method, of class Orchestratable.
     */
    @Test
    public void testIsEnabled() {
        Orchestratable instance = new OrchestratableImpl();
        boolean expResult = false;
        boolean result = instance.isEnabled();
        assertEquals(expResult, result);
    }

    /**
     * Test of isPassthru method, of class Orchestratable.
     */
    @Test
    public void testIsPassthru() {
        Orchestratable instance = new OrchestratableImpl();
        boolean expResult = false;
        boolean result = instance.isPassthru();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAuditTransformer method, of class Orchestratable.
     */
    @Test
    public void testGetAuditTransformer() {
        Orchestratable instance = new OrchestratableImpl();
        AuditTransformer expResult = null;
        AuditTransformer result = instance.getAuditTransformer();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPolicyTransformer method, of class Orchestratable.
     */
    @Test
    public void testGetPolicyTransformer() {
        Orchestratable instance = new OrchestratableImpl();
        PolicyTransformer expResult = null;
        PolicyTransformer result = instance.getPolicyTransformer();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAssertion method, of class Orchestratable.
     */
    @Test
    public void testGetAssertion() {
        Orchestratable instance = new OrchestratableImpl();
        AssertionType expResult = null;
        AssertionType result = instance.getAssertion();
        assertEquals(expResult, result);
    }

    public class OrchestratableImpl implements Orchestratable {

        public boolean isEnabled() {
            return false;
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

		@Override
		public Delegate getDelegate() {
			// TODO Auto-generated method stub
			return null;
		}
    }

}