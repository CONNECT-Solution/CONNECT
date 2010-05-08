/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.passthru.xdr.async.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xdr._2007.AcknowledgementType;
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
        RespondingGatewayProvideAndRegisterDocumentSetRequestType provideAndRegisterAsyncReqRequest = null;
        PassthruXDRAsyncReqNoOpImpl instance = new PassthruXDRAsyncReqNoOpImpl();

        AcknowledgementType result = instance.provideAndRegisterDocumentSetBAsyncRequest(provideAndRegisterAsyncReqRequest);
        assertNotNull(result);
    }

}