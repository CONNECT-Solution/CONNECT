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
public class NhinDelegateTest {

    public NhinDelegateTest() {
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
     * Test of process method, of class NhinDelegate.
     */
    @Test
    public void testProcessInterface() {
        EntityOrchestratable message = null;
        NhinDelegate instance = new NhinDelegateImpl();
        instance.process(message);
    }

    public class NhinDelegateImpl implements NhinDelegate {

        public void process(EntityOrchestratable message) {
        }
    }

}