/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
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
public class OutboundDocRetrieveAggregator_a0Test {

    public OutboundDocRetrieveAggregator_a0Test() {
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
     * Test of aggregate method, of class OutboundDocRetrieveAggregator_a0.
     */
    @Test
    public void testAggregate() {
        System.out.println("aggregate");
        OutboundOrchestratable to = null;
        OutboundOrchestratable from = null;
        OutboundDocRetrieveAggregator_a0 instance = new OutboundDocRetrieveAggregator_a0();
        instance.aggregate(to, from);
    }

}