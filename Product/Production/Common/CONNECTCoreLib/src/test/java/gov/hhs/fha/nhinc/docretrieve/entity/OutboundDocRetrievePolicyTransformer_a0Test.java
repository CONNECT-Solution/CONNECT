/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
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
public class OutboundDocRetrievePolicyTransformer_a0Test {

    public OutboundDocRetrievePolicyTransformer_a0Test() {
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
     * Test of tranform method, of class OutboundDocRetrievePolicyTransformer_a0.
     */
    @Test
    public void testTranform() {
        System.out.println("tranform");
        Orchestratable message = null;
        Direction direction = null;
        OutboundDocRetrievePolicyTransformer_a0 instance = new OutboundDocRetrievePolicyTransformer_a0();
        CheckPolicyRequestType expResult = null;
        CheckPolicyRequestType result = instance.transform(message, direction);
        assertEquals(expResult, result);
    }

}