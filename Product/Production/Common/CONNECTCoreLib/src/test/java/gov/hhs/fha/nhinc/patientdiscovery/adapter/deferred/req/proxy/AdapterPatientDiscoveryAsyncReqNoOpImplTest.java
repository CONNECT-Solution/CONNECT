/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.req.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.proxy.AdapterPatientDiscoveryDeferredReqProxyNoOpImpl;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
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

        AdapterPatientDiscoveryDeferredReqProxyNoOpImpl instance = new AdapterPatientDiscoveryDeferredReqProxyNoOpImpl();
        MCCIIN000002UV01 result = instance.processPatientDiscoveryAsyncReq(new PRPAIN201305UV02(), new AssertionType());

        assertNotNull(result);
    }

}