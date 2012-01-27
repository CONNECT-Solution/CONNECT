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
public class InboundDelegateTest {

    public InboundDelegateTest() {
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
     * Test of process method, of class OutboundDelegate.
     */
    @Test
    public void testProcessInterface() {
        OutboundOrchestratable message = null;
        OutboundDelegate instance = new NhinDelegateImpl();
        instance.process(message);
    }

    public class NhinDelegateImpl implements OutboundDelegate {

        public OutboundOrchestratable process(OutboundOrchestratable message) {
            return null;
        }

		@Override
		public Orchestratable process(Orchestratable message) {
			// TODO Auto-generated method stub
			return null;
		}
    }

}