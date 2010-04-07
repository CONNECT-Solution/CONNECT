/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.patientdiscovery.async.req.proxy;

import gov.hhs.fha.nhinc.adapter.patientdiscovery.async.request.proxy.AdapterPatientDiscoveryAsyncReqNoOpImpl;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
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
public class AdapterPatientDiscoveryAsyncReqNoOpImplTest {

    public AdapterPatientDiscoveryAsyncReqNoOpImplTest() {
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
     * Test of processPatientDiscoveryAsyncReq method, of class AdapterPatientDiscoveryAsyncReqNoOpImpl.
     */
    @Test
    public void testProcessPatientDiscoveryAsyncReq() {
        System.out.println("processPatientDiscoveryAsyncReq");

        RespondingGatewayPRPAIN201305UV02RequestType request = null;
        AdapterPatientDiscoveryAsyncReqNoOpImpl instance = new AdapterPatientDiscoveryAsyncReqNoOpImpl();
        MCCIIN000002UV01 result = instance.processPatientDiscoveryAsyncReq(request);

        assertNotNull(result);
    }

}