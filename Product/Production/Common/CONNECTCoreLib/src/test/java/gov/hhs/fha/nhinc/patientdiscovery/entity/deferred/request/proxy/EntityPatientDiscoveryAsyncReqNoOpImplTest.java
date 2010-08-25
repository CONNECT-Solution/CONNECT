package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntityPatientDiscoveryAsyncReqNoOpImplTest
{

    public EntityPatientDiscoveryAsyncReqNoOpImplTest()
    {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test of processPatientDiscoveryAsyncReq method, of class EntityPatientDiscoveryAsyncReqNoOpImpl.
     */
    @Test
    public void testProcessPatientDiscoveryAsyncReq()
    {
        System.out.println("testProcessPatientDiscoveryAsyncReq");

        EntityPatientDiscoveryDeferredRequestProxyNoOpImpl instance = new EntityPatientDiscoveryDeferredRequestProxyNoOpImpl();

        PRPAIN201305UV02 request = null;
        AssertionType assertion = null;
        NhinTargetCommunitiesType target = null;
        MCCIIN000002UV01 result = instance.processPatientDiscoveryAsyncReq(request, assertion, target);

        assertNotNull(result);
    }
}
