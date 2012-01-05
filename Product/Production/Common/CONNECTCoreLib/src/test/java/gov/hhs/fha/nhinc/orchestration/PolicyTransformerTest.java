/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.orchestration;

import gov.hhs.fha.nhinc.orchestration.PolicyTransformer.Direction;
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
public class PolicyTransformerTest {

    public PolicyTransformerTest() {
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
     * Test of tranform method, of class PolicyTransformer.
     */
    @Test
    public void testTranformInterface() {
        Orchestratable message = null;
        Direction direction = null;
        PolicyTransformer instance = new PolicyTransformerImpl();
        instance.transform(message, direction);
        // we are just testing that the interface is good, so this test should suffice
    }

    public class PolicyTransformerImpl implements PolicyTransformer {
        public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType transform(Orchestratable message, Direction direction)
        {
            return null;
        }
    }

}