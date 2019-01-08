/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package gov.hhs.fha.nhinc.gateway.executorservice;

import static org.junit.Assert.assertEquals;

import org.junit.After;
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
