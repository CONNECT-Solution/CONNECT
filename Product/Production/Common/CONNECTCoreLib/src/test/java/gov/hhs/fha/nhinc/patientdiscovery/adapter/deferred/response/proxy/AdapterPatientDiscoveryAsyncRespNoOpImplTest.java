/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;
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

        AdapterPatientDiscoveryDeferredRespProxyNoOpImpl instance = new AdapterPatientDiscoveryDeferredRespProxyNoOpImpl();
        MCCIIN000002UV01 result = instance.processPatientDiscoveryAsyncResp(new PRPAIN201306UV02(), new AssertionType ());
        
        assertNotNull(result);
    }

}