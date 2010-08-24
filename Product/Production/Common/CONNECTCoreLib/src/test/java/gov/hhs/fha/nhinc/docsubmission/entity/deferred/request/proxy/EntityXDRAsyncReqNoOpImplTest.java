/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docsubmission.entity.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
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
public class EntityXDRAsyncReqNoOpImplTest {

    public EntityXDRAsyncReqNoOpImplTest() {
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
     * Test of provideAndRegisterDocumentSetBAsyncRequest method, of class EntityXDRAsyncReqNoOpImpl.
     */
    @Test
    public void testProvideAndRegisterDocumentSetBAsyncRequest() {
        System.out.println("testProvideAndRegisterDocumentSetBAsyncRequest");
        EntityDocSubmissionDeferredRequestProxyNoOpImpl instance = new EntityDocSubmissionDeferredRequestProxyNoOpImpl();
        
        XDRAcknowledgementType result = instance.provideAndRegisterDocumentSetBAsyncRequest(new ProvideAndRegisterDocumentSetRequestType(), new AssertionType(), new NhinTargetCommunitiesType(), new UrlInfoType());
        assertNotNull(result);
    }

}