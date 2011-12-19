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
public class EntityOrchestratableTest {

    public EntityOrchestratableTest() {
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
     * Test of getNhinDelegate method, of class EntityOrchestratable.
     */
    @Test
    public void testGetNhinDelegate() {
        System.out.println("getNhinDelegate");
        EntityOrchestratable instance = new EntityOrchestratableImpl();
        NhinDelegate expResult = null;
        NhinDelegate result = instance.getNhinDelegate();
        assertEquals(expResult, result);
        // there was no error, so success
        // TODO: make a better test
    }

    public class EntityOrchestratableImpl implements EntityOrchestratable {

        public NhinDelegate getNhinDelegate() {
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
    }

}