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
public class AdapterDelegateTest {

    public AdapterDelegateTest() {
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
     * Test of process method, of class AdapterDelegate.
     */
    @Test
    public void testProcessInterface() {
        NhinOrchestratable message = null;
        AdapterDelegate instance = new AdapterDelegateImpl();
        instance.process(message);
    }

    @Test
    public void testCreateErrorResponseInterface() {
        NhinOrchestratable message = null;
        AdapterDelegate instance = new AdapterDelegateImpl();
        instance.createErrorResponse(message, null);
    }

    public class AdapterDelegateImpl implements AdapterDelegate {

        public NhinOrchestratable process(NhinOrchestratable message) {
        	return null;
        }

        public void createErrorResponse(NhinOrchestratable message, String error) {
        }

		@Override
		public Orchestratable process(Orchestratable message) {
			// TODO Auto-generated method stub
			return null;
		}
    }

}