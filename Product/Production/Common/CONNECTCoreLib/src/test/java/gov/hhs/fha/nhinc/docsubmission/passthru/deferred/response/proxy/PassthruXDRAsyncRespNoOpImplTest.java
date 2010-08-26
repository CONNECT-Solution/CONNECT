/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docsubmission.passthru.deferred.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
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
public class PassthruXDRAsyncRespNoOpImplTest {

    public PassthruXDRAsyncRespNoOpImplTest() {
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
     * Test of provideAndRegisterDocumentSetBAsyncResponse method, of class PassthruXDRAsyncRespNoOpImpl.
     */
    @Test
    public void testProvideAndRegisterDocumentSetBAsyncResponse() {
        System.out.println("testProvideAndRegisterDocumentSetBAsyncResponse");
        RegistryResponseType provideAndRegisterAsyncRespRequest = null;
        PassthruDocSubmissionDeferredResponseProxyNoOpImpl instance = new PassthruDocSubmissionDeferredResponseProxyNoOpImpl();
        
        XDRAcknowledgementType result = instance.provideAndRegisterDocumentSetBResponse(provideAndRegisterAsyncRespRequest, new AssertionType(), new NhinTargetSystemType());
       assertNotNull(result);
    }

}