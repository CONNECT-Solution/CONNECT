package gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.ProxyPRPAIN201306UVProxyRequestType;
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
public class PassthruPatientDiscoveryDeferredRespProxyNoOpImplTest {

    public PassthruPatientDiscoveryDeferredRespProxyNoOpImplTest() {
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
     * Test of proxyProcessPatientDiscoveryAsyncResp method, of class PassthruPatientDiscoveryAsyncRespNoOpImpl.
     */
    @Test
    public void testProxyProcessPatientDiscoveryAsyncResp() {
        System.out.println("proxyProcessPatientDiscoveryAsyncResp");
        ProxyPRPAIN201306UVProxyRequestType proxyProcessPatientDiscoveryAsyncRespRequest = new ProxyPRPAIN201306UVProxyRequestType();
        PassthruPatientDiscoveryDeferredRespProxyNoOpImpl instance = new PassthruPatientDiscoveryDeferredRespProxyNoOpImpl();
        AssertionType assertion = null;
        NhinTargetSystemType targetSystem = null;
        MCCIIN000002UV01 result = instance.proxyProcessPatientDiscoveryAsyncResp(proxyProcessPatientDiscoveryAsyncRespRequest, assertion, targetSystem);

        assertNotNull(result);
    }

}