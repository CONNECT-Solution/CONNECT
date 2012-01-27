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
public class OutboundDocRetrieveDelegateTest {

    public OutboundDocRetrieveDelegateTest() {
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
     * Test of process method, of class OutboundDocRetrieveDelegate.
     */
    @Test
    public void testProcess() {
        System.out.println("process");
        OutboundOrchestratable message = null;
        OutboundDocRetrieveDelegate instance = new OutboundDocRetrieveDelegate();
        instance.process(message);
    }

    /**
     * Test of createErrorResponse method, of class OutboundDocRetrieveDelegate.
     */
    @Test
    public void testCreateErrorResponse() {
        System.out.println("createErrorResponse");
        OutboundOrchestratable message = null;
        String error = "";
        OutboundDocRetrieveDelegate instance = new OutboundDocRetrieveDelegate();
        instance.createErrorResponse(message, error);
    }

}