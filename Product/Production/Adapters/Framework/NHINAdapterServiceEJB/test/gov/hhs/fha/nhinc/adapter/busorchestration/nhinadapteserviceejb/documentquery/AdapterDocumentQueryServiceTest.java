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
public class AdapterDocumentQueryServiceTest {

    public AdapterDocumentQueryServiceTest() {
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
     * Test of respondingGatewayCrossGatewayQuery method, of class AdapterDocumentQueryService.
     */
    @Test
    public void testRespondingGatewayCrossGatewayQuery() {
        System.out.println("respondingGatewayCrossGatewayQuery");
        RespondingGatewayCrossGatewayQueryRequestType respondingGatewayCrossGatewayQueryRequest = null;
        AdapterDocumentQueryService instance = new AdapterDocumentQueryService();
        AdhocQueryResponse expResult = null;
        AdhocQueryResponse result = instance.respondingGatewayCrossGatewayQuery(respondingGatewayCrossGatewayQueryRequest);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}