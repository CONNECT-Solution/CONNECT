/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.queue.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
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
public class AdapterPatientDiscoveryAsyncReqQueueNoOpImplTest {

    public AdapterPatientDiscoveryAsyncReqQueueNoOpImplTest() {
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
     * Test of addPatientDiscoveryAsyncReq method, of class AdapterPatientDiscoveryAsyncReqQueueNoOpImpl.
     */
    @Test
    public void testAddPatientDiscoveryAsyncReq() {
        System.out.println("addPatientDiscoveryAsyncReq");

        PRPAIN201305UV02 request = null;
        AssertionType assertion = null;
        AdapterPatientDiscoveryAsyncReqQueueProxyNoOpImpl instance = new AdapterPatientDiscoveryAsyncReqQueueProxyNoOpImpl();
        MCCIIN000002UV01 result = instance.addPatientDiscoveryAsyncReq(request, assertion);

        assertNotNull(result);
    }

}