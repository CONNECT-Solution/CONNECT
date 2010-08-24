package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.response.proxy;

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
 * @author jhoppesc
 */
public class EntityPatientDiscoveryDeferredResponseProxyNoOpImplTest {

    public EntityPatientDiscoveryDeferredResponseProxyNoOpImplTest() {
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
     * Test of processPatientDiscoveryAsyncResp method, of class EntityPatientDiscoveryAsyncRespNoOpImpl.
     */
    @Test
    public void testProcessPatientDiscoveryAsyncResp() {
        System.out.println("processPatientDiscoveryAsyncResp");
        PRPAIN201306UV02 request = new PRPAIN201306UV02();
        EntityPatientDiscoveryDeferredResponseProxyNoOpImpl instance = new EntityPatientDiscoveryDeferredResponseProxyNoOpImpl();

        MCCIIN000002UV01 result = instance.processPatientDiscoveryAsyncResp(request, null, null);

        assertNotNull(result);
    }

}