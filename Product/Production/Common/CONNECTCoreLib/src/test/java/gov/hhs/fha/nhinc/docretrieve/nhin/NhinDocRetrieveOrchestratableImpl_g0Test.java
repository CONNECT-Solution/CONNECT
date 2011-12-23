/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.nhin;

import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mweaver
 */
public class NhinDocRetrieveOrchestratableImpl_g0Test {

    public NhinDocRetrieveOrchestratableImpl_g0Test() {
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
     * Test of getRequest method, of class NhinDocRetrieveOrchestratableImpl_g0.
     */
    @Test
    public void testGetRequest() {
        RetrieveDocumentSetRequestType request = new RetrieveDocumentSetRequestType();
        NhinDocRetrieveOrchestratableImpl_g0 instance = new NhinDocRetrieveOrchestratableImpl_g0(request, null, null, null, null);
        RetrieveDocumentSetRequestType expResult = request;
        RetrieveDocumentSetRequestType result = instance.getRequest();
        assertEquals(expResult, result);
    }

    /**
     * Test of getResponse method, of class NhinDocRetrieveOrchestratableImpl_g0.
     */
    @Test
    public void testGetResponse() {
        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
        NhinDocRetrieveOrchestratableImpl_g0 instance = new NhinDocRetrieveOrchestratableImpl_g0(null, null, null, null, null);
        instance.setResponse(response);
        RetrieveDocumentSetResponseType expResult = response;
        RetrieveDocumentSetResponseType result = instance.getResponse();
        assertEquals(expResult, result);
    }

    /**
     * Test of setResponse method, of class NhinDocRetrieveOrchestratableImpl_g0.
     */
    @Test
    public void testSetResponse() {
        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
        NhinDocRetrieveOrchestratableImpl_g0 instance = new NhinDocRetrieveOrchestratableImpl_g0(null, null, null, null, null);
        instance.setResponse(response);
    }

    /**
     * Test of getServiceName method, of class NhinDocRetrieveOrchestratableImpl_g0.
     */
    @Test
    public void testGetServiceName() {
        NhinDocRetrieveOrchestratableImpl_g0 instance = new NhinDocRetrieveOrchestratableImpl_g0(null, null, null, null, null);
        String expResult = "NhinDocumentRetrieve_g0";
        String result = instance.getServiceName();
        assertEquals(expResult, result);
    }

}