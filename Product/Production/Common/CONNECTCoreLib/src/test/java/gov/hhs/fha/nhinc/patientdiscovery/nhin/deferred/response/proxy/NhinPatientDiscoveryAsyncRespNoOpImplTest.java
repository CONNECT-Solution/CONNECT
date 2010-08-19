/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.nhin.deferred.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;
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
public class NhinPatientDiscoveryAsyncRespNoOpImplTest {

    public NhinPatientDiscoveryAsyncRespNoOpImplTest() {
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
     * Test of respondingGatewayPRPAIN201306UV02 method, of class NhinPatientDiscoveryAsyncRespNoOpImpl.
     */
    @Test
    public void testRespondingGatewayPRPAIN201306UV02() {
        System.out.println("testRespondingGatewayPRPAIN201306UV02");
        PRPAIN201306UV02 body = new PRPAIN201306UV02();
        AssertionType assertion = new AssertionType();
        NhinTargetSystemType target = new NhinTargetSystemType();
        NhinPatientDiscoveryDeferredRespProxyNoOpImpl instance = new NhinPatientDiscoveryDeferredRespProxyNoOpImpl();
       
        MCCIIN000002UV01 result = instance.respondingGatewayPRPAIN201306UV02(body, assertion, target);

        assertNotNull(result);
    }

}