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
    public void testProcess() {
        System.out.println("process");
        NhinOrchestratable message = null;
        AdapterDelegate instance = new AdapterDelegateImpl();
        instance.process(message);
        // we are just testing that the interface is good, so this test should suffice
    }

    public class AdapterDelegateImpl implements AdapterDelegate {

        public void process(NhinOrchestratable message) {
        }
    }

}