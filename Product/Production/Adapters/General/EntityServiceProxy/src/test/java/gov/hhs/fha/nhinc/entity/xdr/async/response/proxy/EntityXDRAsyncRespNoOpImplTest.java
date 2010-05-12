/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.entity.xdr.async.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
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
public class EntityXDRAsyncRespNoOpImplTest {

    public EntityXDRAsyncRespNoOpImplTest() {
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
     * Test of provideAndRegisterDocumentSetBAsyncResponse method, of class EntityXDRAsyncRespNoOpImpl.
     */
    @Test
    public void testProvideAndRegisterDocumentSetBAsyncResponse() {
        System.out.println("testProvideAndRegisterDocumentSetBAsyncResponse");
        RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType provideAndRegisterDocumentSetAsyncRespRequest = null;
        EntityXDRAsyncRespNoOpImpl instance = new EntityXDRAsyncRespNoOpImpl();
        
        XDRAcknowledgementType result = instance.provideAndRegisterDocumentSetBAsyncResponse(provideAndRegisterDocumentSetAsyncRespRequest);
        assertNotNull(result);
    }

}