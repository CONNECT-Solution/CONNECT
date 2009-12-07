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
public class AdapterDocRetrieveImplTest {

    public AdapterDocRetrieveImplTest() {
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
     * Test of getInstance method, of class AdapterDocRetrieveImpl.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        AdapterDocRetrieveImpl result = AdapterDocRetrieveImpl.getInstance();
        assertNotNull(result);
    }

    /**
     * Test of respondingGatewayCrossGatewayRetrieve method, of class AdapterDocRetrieveImpl.
     */
    @Test
    public void testRespondingGatewayCrossGatewayRetrieve() {

        System.out.println("respondingGatewayCrossGatewayRetrieve");
        //TODO: Put togeter a real query here
        RespondingGatewayCrossGatewayRetrieveRequestType respondingGatewayCrossGatewayRetrieveRequest = null;
        AdapterDocRetrieveImpl instance = null;
        RetrieveDocumentSetResponseType result = instance.respondingGatewayCrossGatewayRetrieve(respondingGatewayCrossGatewayRetrieveRequest);
        assertNotNull(result);
        // TODO: Analyze the retrun result and see if it make sense.
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}