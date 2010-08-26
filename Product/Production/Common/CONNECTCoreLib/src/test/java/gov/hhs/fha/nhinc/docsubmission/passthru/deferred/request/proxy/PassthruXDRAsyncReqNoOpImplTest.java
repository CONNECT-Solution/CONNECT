/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docsubmission.passthru.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
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
public class PassthruXDRAsyncReqNoOpImplTest {

    public PassthruXDRAsyncReqNoOpImplTest() {
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
     * Test of provideAndRegisterDocumentSetBAsyncRequest method, of class PassthruXDRAsyncReqNoOpImpl.
     */
    @Test
    public void testProvideAndRegisterDocumentSetBAsyncRequest() {
        System.out.println("testProvideAndRegisterDocumentSetBAsyncRequest");
        ProvideAndRegisterDocumentSetRequestType provideAndRegisterAsyncReqRequest = null;
        PassthruDocSubmissionDeferredRequestProxyNoOpImpl instance = new PassthruDocSubmissionDeferredRequestProxyNoOpImpl();

        XDRAcknowledgementType result = instance.provideAndRegisterDocumentSetBRequest(provideAndRegisterAsyncReqRequest, new AssertionType(), new NhinTargetSystemType());
        assertNotNull(result);
    }

}