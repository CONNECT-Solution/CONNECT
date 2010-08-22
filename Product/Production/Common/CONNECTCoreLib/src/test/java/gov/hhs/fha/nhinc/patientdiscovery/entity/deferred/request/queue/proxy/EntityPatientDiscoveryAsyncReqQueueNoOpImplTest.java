/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.queue.proxy;

import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
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
public class EntityPatientDiscoveryAsyncReqQueueNoOpImplTest {

    public EntityPatientDiscoveryAsyncReqQueueNoOpImplTest() {
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
     * Test of addPatientDiscoveryAsyncReq method, of class EntityPatientDiscoveryAsyncReqQueueNoOpImpl.
     */
    @Test
    public void testAddPatientDiscoveryAsyncReq() {
        System.out.println("addPatientDiscoveryAsyncReq");

        EntityPatientDiscoveryDeferredReqQueueProxyNoOpImpl instance = new EntityPatientDiscoveryDeferredReqQueueProxyNoOpImpl();
        MCCIIN000002UV01 result = instance.addPatientDiscoveryAsyncReq(new PRPAIN201305UV02(), new AssertionType(), new NhinTargetCommunitiesType());

        assertNotNull(result);
    }

}