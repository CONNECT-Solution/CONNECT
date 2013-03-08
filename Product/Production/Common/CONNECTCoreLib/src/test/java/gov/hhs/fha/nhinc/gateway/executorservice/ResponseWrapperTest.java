/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.gateway.executorservice;

import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author nsubramanyan
 */
public class ResponseWrapperTest {

    public ResponseWrapperTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getCallableRequest method, of class ResponseWrapper.
     */
    @Test
    public void testGetCallableRequest() {
        System.out.println("getCallableRequest");
        ResponseWrapper instance = new ResponseWrapper();
        Object expResult = null;
        Object result = instance.getCallableRequest();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCallableRequest method, of class ResponseWrapper.
     */
    @Test
    public void testSetCallableRequest() {
        System.out.println("setCallableRequest");
        Object r = null;
        ResponseWrapper instance = new ResponseWrapper();
        instance.setCallableRequest(r);
        Object result = instance.getCallableRequest();
        assertEquals(r, result);
    }

    /**
     * Test of getCallableTarget method, of class ResponseWrapper.
     */
    @Test
    public void testGetCallableTarget() {
        System.out.println("getCallableTarget");
        ResponseWrapper instance = new ResponseWrapper();
        Object expResult = null;
        Object result = instance.getCallableTarget();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCallableTarget method, of class ResponseWrapper.
     */
    @Test
    public void testSetCallableTarget() {
        System.out.println("setCallableTarget");
        Object t = null;
        ResponseWrapper instance = new ResponseWrapper();
        instance.setCallableTarget(t);
        Object expResult = instance.getCallableRequest();
        assertEquals(expResult, t);
    }

    /**
     * Test of getResponse method, of class ResponseWrapper.
     */
    @Test
    public void testGetResponse() {
        System.out.println("getResponse");
        ResponseWrapper instance = new ResponseWrapper();
        Object expResult = null;
        Object result = instance.getResponse();
        assertEquals(expResult, result);
    }

    /**
     * Test of setResponse method, of class ResponseWrapper.
     */
    @Test
    public void testSetResponse() {
        System.out.println("setResponse");
        Object r = null;
        ResponseWrapper instance = new ResponseWrapper();
        instance.setResponse(r);
        Object result = instance.getResponse();
        assertEquals(r, result);
    }

    @Test
    public void testResponseWrapper() {
        Object target = null;
        Object request = null;
        Object response = null;
        ResponseWrapper instance = new ResponseWrapper(target, request, request);
        assertEquals(target, instance.getCallableTarget());
        assertEquals(request, instance.getCallableRequest());
        assertEquals(response, instance.getResponse());
    }
}
