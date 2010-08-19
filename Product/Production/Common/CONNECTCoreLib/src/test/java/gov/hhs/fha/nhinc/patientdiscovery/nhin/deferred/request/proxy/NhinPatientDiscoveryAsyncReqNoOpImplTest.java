/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.nhin.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
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
public class NhinPatientDiscoveryAsyncReqNoOpImplTest {

    public NhinPatientDiscoveryAsyncReqNoOpImplTest() {
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
     * Test of respondingGatewayPRPAIN201305UV02 method, of class NhinPatientDiscoveryAsyncReqNoOpImpl.
     */
    @Test
    public void testRespondingGatewayPRPAIN201305UV02() {
        System.out.println("respondingGatewayPRPAIN201305UV02");
        PRPAIN201305UV02 body = new PRPAIN201305UV02();
        AssertionType assertion = new AssertionType();
        NhinTargetSystemType target = new NhinTargetSystemType();
        NhinPatientDiscoveryDeferredReqProxyNoOpImpl instance = new NhinPatientDiscoveryDeferredReqProxyNoOpImpl();
        
        MCCIIN000002UV01 result = instance.respondingGatewayPRPAIN201305UV02(body, assertion, target);

        assertNotNull(result);
    }

}