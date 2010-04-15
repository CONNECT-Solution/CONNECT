/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.passthru.patientdiscovery.async.request.proxy;

import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType;
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
public class PassthruPatientDiscoveryAsyncReqNoOpImplTest {

    public PassthruPatientDiscoveryAsyncReqNoOpImplTest() {
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
     * Test of proxyProcessPatientDiscoveryAsyncReq method, of class PassthruPatientDiscoveryAsyncReqNoOpImpl.
     */
    @Test
    public void testProxyProcessPatientDiscoveryAsyncReq() {
        System.out.println("proxyProcessPatientDiscoveryAsyncReq");

        ProxyPRPAIN201305UVProxyRequestType proxyProcessPatientDiscoveryAsyncReqRequest = null;
        PassthruPatientDiscoveryAsyncReqNoOpImpl instance = new PassthruPatientDiscoveryAsyncReqNoOpImpl();
        
        MCCIIN000002UV01 result = instance.proxyProcessPatientDiscoveryAsyncReq(proxyProcessPatientDiscoveryAsyncReqRequest);

        assertNotNull(result);
    }

}