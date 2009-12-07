/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.busorchestration.nhinadapteserviceejb.documentquery;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequestType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
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
public class AdapterDocumentQueryServiceImplTest {

    public AdapterDocumentQueryServiceImplTest() {
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
     * Test of getInstance method, of class AdapterDocumentQueryServiceImpl.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        AdapterDocumentQueryServiceImpl expResult = null;
        AdapterDocumentQueryServiceImpl result = AdapterDocumentQueryServiceImpl.getInstance();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of respondingGatewayCrossGatewayQuery method, of class AdapterDocumentQueryServiceImpl.
     */
    @Test
    public void testRespondingGatewayCrossGatewayQuery() {
        System.out.println("respondingGatewayCrossGatewayQuery");
        RespondingGatewayCrossGatewayQueryRequestType respondingGatewayCrossGatewayQueryRequest = null;
        AdapterDocumentQueryServiceImpl instance = null;
        AdhocQueryResponse expResult = null;
        AdhocQueryResponse result = instance.respondingGatewayCrossGatewayQuery(respondingGatewayCrossGatewayQueryRequest);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}