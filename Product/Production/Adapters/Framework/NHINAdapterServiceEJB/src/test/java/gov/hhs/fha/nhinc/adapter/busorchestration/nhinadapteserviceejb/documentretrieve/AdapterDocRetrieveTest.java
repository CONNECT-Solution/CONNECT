/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.busorchestration.nhinadapteserviceejb.documentretrieve;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jerry Goodnough
 */
public class AdapterDocRetrieveTest {

    public AdapterDocRetrieveTest() {
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
     * Test of respondingGatewayCrossGatewayRetrieve method, of class AdapterDocRetrieve.
     */
    @Test
    public void testRespondingGatewayCrossGatewayRetrieve() {
        System.out.println("respondingGatewayCrossGatewayRetrieve");
        RespondingGatewayCrossGatewayRetrieveRequestType respondingGatewayCrossGatewayRetrieveRequest = null;
        AdapterDocRetrieve instance = new AdapterDocRetrieve();
        RetrieveDocumentSetResponseType expResult = null;
        RetrieveDocumentSetResponseType result = instance.respondingGatewayCrossGatewayRetrieve(respondingGatewayCrossGatewayRetrieveRequest);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}