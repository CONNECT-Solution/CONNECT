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

/**
 *
 * @author mweaver
 */
public class InboundAggregatorTest {

    public InboundAggregatorTest() {
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
     * Test of aggregate method, of class OutboundAggregator.
     */
    @Test
    public void testAggregate() {
        System.out.println("aggregate");
        OutboundOrchestratable to = null;
        OutboundOrchestratable from = null;
        NhinAggregator instance = new NhinAggregatorImpl();
        instance.aggregate(to, from);
    }

    public class NhinAggregatorImpl implements NhinAggregator {

        public void aggregate(OutboundOrchestratable to, OutboundOrchestratable from) {
        }
    }

}