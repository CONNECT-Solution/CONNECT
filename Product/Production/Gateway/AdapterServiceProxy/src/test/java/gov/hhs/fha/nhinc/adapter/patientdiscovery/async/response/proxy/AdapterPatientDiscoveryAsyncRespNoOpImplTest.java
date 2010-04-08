/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.patientdiscovery.async.response.proxy;

import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author JHOPPESC
 */
public class AdapterPatientDiscoveryAsyncRespNoOpImplTest {

    public AdapterPatientDiscoveryAsyncRespNoOpImplTest() {
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
     * Test of processPatientDiscoveryAsyncResp method, of class AdapterPatientDiscoveryAsyncRespNoOpImpl.
     */
    @Test
    public void testProcessPatientDiscoveryAsyncResp() {
        System.out.println("processPatientDiscoveryAsyncResp");

        RespondingGatewayPRPAIN201306UV02RequestType processPatientDiscoveryAsyncRespAsyncRequest = null;
        AdapterPatientDiscoveryAsyncRespNoOpImpl instance = new AdapterPatientDiscoveryAsyncRespNoOpImpl();
        MCCIIN000002UV01 result = instance.processPatientDiscoveryAsyncResp(processPatientDiscoveryAsyncRespAsyncRequest);
        
        assertNotNull(result);
    }

}