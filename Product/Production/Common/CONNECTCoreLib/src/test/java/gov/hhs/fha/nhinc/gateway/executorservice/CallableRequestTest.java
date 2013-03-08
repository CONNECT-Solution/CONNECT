/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.gateway.executorservice;

import gov.hhs.fha.nhinc.connectmgr.UrlInfo;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import static org.junit.Assert.*;
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
        CallableRequest<UrlInfo, RespondingGatewayPRPAIN201305UV02RequestType, ResponseWrapper> instance = new CallableRequest<UrlInfo, RespondingGatewayPRPAIN201305UV02RequestType, ResponseWrapper>(null, null, null, null);
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
        CallableRequest<UrlInfo, RespondingGatewayPRPAIN201305UV02RequestType, ResponseWrapper> instance = new CallableRequest<UrlInfo, RespondingGatewayPRPAIN201305UV02RequestType, ResponseWrapper>(null, null, null, null);
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
        CallableRequest<UrlInfo, RespondingGatewayPRPAIN201305UV02RequestType, ResponseWrapper> instance = new CallableRequest<UrlInfo, RespondingGatewayPRPAIN201305UV02RequestType, ResponseWrapper>(null, request, null, testClient);
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
        CallableRequest<UrlInfo, RespondingGatewayPRPAIN201305UV02RequestType, ResponseWrapper> instance = new CallableRequest<UrlInfo, RespondingGatewayPRPAIN201305UV02RequestType, ResponseWrapper>(null, null, null, null);
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
        CallableRequest<UrlInfo, RespondingGatewayPRPAIN201305UV02RequestType, ResponseWrapper> instance = new CallableRequest<UrlInfo, RespondingGatewayPRPAIN201305UV02RequestType, ResponseWrapper>(null, request, null, testClient);
        Object result = instance.call();
    }
    //create WebService client class

    private static class TestClient<Target extends UrlInfo, Request extends RespondingGatewayPRPAIN201305UV02RequestType, Response extends ResponseWrapper> implements WebServiceClient<Target, Request, Response> {

        private boolean returnResponseNull = false;

        @Override
        public Response callWebService(Target t, Request r) throws Exception {
            PRPAIN201306UV02 response = new PRPAIN201306UV02();
            if (returnResponseNull) {
                return null;
            } else {
                return (Response) (new ResponseWrapper(t, r, response));
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
