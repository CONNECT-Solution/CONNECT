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
import static org.junit.Assert.assertNotNull;

import gov.hhs.fha.nhinc.connectmgr.UrlInfo;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.junit.Test;

/**
 *
 * @author nsubramanyan
 */
public class CallableRequestTest {

    public CallableRequestTest() {
    }

    /**
     * Test of getRequest method, of class CallableRequest.
     */
    @Test
    public void testGetRequest() {
        System.out.println("getRequest");
        CallableRequest<UrlInfo, RespondingGatewayPRPAIN201305UV02RequestType, ResponseWrapper> instance = new CallableRequest<>(
                null, null, null, null);
        Object expResult = null;
        Object result = instance.getRequest();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTarget method, of class CallableRequest.
     */
    @Test
    public void testGetTarget() {
        System.out.println("getTarget");
        CallableRequest<UrlInfo, RespondingGatewayPRPAIN201305UV02RequestType, ResponseWrapper> instance = new CallableRequest<>(
                null, null, null, null);
        Object expResult = null;
        Object result = instance.getTarget();
        assertEquals(expResult, result);
    }

    /**
     * Test of call method, of class CallableRequest.
     */
    @Test
    public void testCall() throws Exception {
        System.out.println("call");
        RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
        TestClient testClient = new TestClient();
        CallableRequest<UrlInfo, RespondingGatewayPRPAIN201305UV02RequestType, ResponseWrapper> instance = new CallableRequest<>(
                null, request, null, testClient);
        ResponseWrapper rw = new ResponseWrapper();
        Object result = instance.call();
        assertNotNull(result);
    }

    /**
     * Test of call method, of class CallableRequest with request null.
     */
    @Test(expected = Exception.class)
    public void testCallWithRequestNull() throws Exception {
        System.out.println("testCallWithRequestNull");
        CallableRequest<UrlInfo, RespondingGatewayPRPAIN201305UV02RequestType, ResponseWrapper> instance = new CallableRequest<>(
                null, null, null, null);
        Object result = instance.call();
    }

    /**
     * Test of call method, of class CallableRequest with request null.
     */
    @Test(expected = Exception.class)
    public void testCallWithResponseNull() throws Exception {
        System.out.println("testCallWithResponseNull");
        TestClient testClient = new TestClient();
        testClient.setReturnResponseNull(true);
        RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
        CallableRequest<UrlInfo, RespondingGatewayPRPAIN201305UV02RequestType, ResponseWrapper> instance = new CallableRequest<>(
                null, request, null, testClient);
        Object result = instance.call();
    }
    // create WebService client class

    private static class TestClient<Target extends UrlInfo, Request extends RespondingGatewayPRPAIN201305UV02RequestType, Response extends ResponseWrapper>
            implements WebServiceClient<Target, Request, Response> {

        private boolean returnResponseNull = false;

        @Override
        public Response callWebService(Target t, Request r) throws Exception {
            PRPAIN201306UV02 response = new PRPAIN201306UV02();
            if (returnResponseNull) {
                return null;
            } else {
                return (Response) new ResponseWrapper(t, r, response);
            }
        }

        /**
         * @param returnResponseNull the returnResponseNull to set
         */
        public void setReturnResponseNull(boolean returnResponseNull) {
            this.returnResponseNull = returnResponseNull;
        }
    }
}
