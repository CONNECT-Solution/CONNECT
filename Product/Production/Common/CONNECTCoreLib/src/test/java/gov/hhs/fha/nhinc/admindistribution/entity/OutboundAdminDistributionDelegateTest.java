/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admindistribution.entity;

import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author mweaver
 */
public class OutboundAdminDistributionDelegateTest {

    public OutboundAdminDistributionDelegateTest() {
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
     * Test of process method, of class OutboundAdminDistributionDelegate.
     */
    @Test
    public void testProcess() {
        System.out.println("process");
        OutboundOrchestratable message = null;
        OutboundAdminDistributionDelegate instance = new OutboundAdminDistributionDelegate();
        instance.process(message);
    }

}