/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.entity.patientdiscovery.async.request.proxy;

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
 * @author jhoppesc
 */
public class EntityPatientDiscoveryAsyncReqNoOpImplTest {

    public EntityPatientDiscoveryAsyncReqNoOpImplTest() {
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
     * Test of processPatientDiscoveryAsyncReq method, of class EntityPatientDiscoveryAsyncReqNoOpImpl.
     */
    @Test
    public void testProcessPatientDiscoveryAsyncReq() {
        System.out.println("testProcessPatientDiscoveryAsyncReq");

        RespondingGatewayPRPAIN201305UV02RequestType processPatientDiscoveryAsyncReqAsyncRequest = null;
        EntityPatientDiscoveryAsyncReqNoOpImpl instance = new EntityPatientDiscoveryAsyncReqNoOpImpl();
        
        MCCIIN000002UV01 result = instance.processPatientDiscoveryAsyncReq(processPatientDiscoveryAsyncReqAsyncRequest);

        assertNotNull(result);
    }

}