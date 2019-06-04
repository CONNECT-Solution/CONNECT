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
package gov.hhs.fha.nhinc.messaging.client;

import static org.junit.Assert.assertEquals;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.messaging.service.decorator.MTOMServiceEndpointDecoratorTest;
import gov.hhs.fha.nhinc.messaging.service.decorator.TimeoutServiceEndpointDecorator;
import gov.hhs.fha.nhinc.messaging.service.decorator.TimeoutServiceEndpointDecoratorTest;
import gov.hhs.fha.nhinc.messaging.service.decorator.URLServiceEndpointDecoratorTest;
import gov.hhs.fha.nhinc.messaging.service.decorator.cxf.SoapResponseServiceEndpointDecoratorTest;
import gov.hhs.fha.nhinc.messaging.service.port.TestServicePortDescriptor;
import gov.hhs.fha.nhinc.messaging.service.port.TestServicePortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author akong
 *
 */
public class CONNECTCXFClientUnsecuredTest {

    private final TimeoutServiceEndpointDecoratorTest timeoutTest = new TimeoutServiceEndpointDecoratorTest();
    private final MTOMServiceEndpointDecoratorTest mtomTest = new MTOMServiceEndpointDecoratorTest();
    private final SoapResponseServiceEndpointDecoratorTest responseTest = new SoapResponseServiceEndpointDecoratorTest();
    private final URLServiceEndpointDecoratorTest urlTest = new URLServiceEndpointDecoratorTest();

    private static final int TIMEOUT = 100;

    @Before
    public void setUpTest(){
        System.setProperty("nhinc.properties.dir", System.getProperty("user.dir") + "/src/test/resources/");
        System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
        try {
            PropertyAccessor.getInstance().setProperty(NhincConstants.GATEWAY_PROPERTY_FILE, TimeoutServiceEndpointDecorator.CONFIG_KEY_TIMEOUT, Integer.toString(TIMEOUT));
        } catch (PropertyAccessException ex) {
            Assert.fail("Unable to set in memory property for timeout decorator: " + ex.getMessage());
        }
    }

    /**
     * This test ensures that the interceptor count is the same no matter how many times the decorator is called on the
     * constructor.
     */
    @Test
    public void ensureInterceptorCountIsConstant() {
        String url = "url";
        AssertionType assertion = new AssertionType();
        assertion.setTransactionTimeout(-1);

        CONNECTClient<TestServicePortType> client = createClient(url, assertion);

        Client cxfClient = ClientProxy.getClient(client.getPort());
        int numInInterceptors = cxfClient.getInInterceptors().size();
        int numOutInterceptors = cxfClient.getOutInterceptors().size();

        createClient(url, assertion);
        createClient(url, assertion);
        CONNECTClient<TestServicePortType> client2 = createClient(url, assertion);

        Client cxfClient2 = ClientProxy.getClient(client2.getPort());
        assertEquals(numInInterceptors, cxfClient2.getInInterceptors().size());
        assertEquals(numOutInterceptors, cxfClient2.getOutInterceptors().size());
    }

    @Test
    public void unsecuredClientConfiguration() {
        String url = "url";
        AssertionType assertion = new AssertionType();
        assertion.setTransactionTimeout(-1);

        CONNECTClient<TestServicePortType> client = createClient(url, assertion);

        // default configuration
        timeoutTest.verifyTimeoutIsSet(client, TIMEOUT);
        responseTest.verifySoapResponseInInterceptor(client);
        urlTest.verifyURLConfiguration(client, url);
    }

    @Test
    public void testEnableMtom() {
        String url = "url";
        AssertionType assertion = new AssertionType();
        assertion.setTransactionTimeout(-1);

        CONNECTClient<TestServicePortType> client = createClient(url, assertion);
        client.enableMtom();

        // default configuration
        timeoutTest.verifyTimeoutIsSet(client, TIMEOUT);
        responseTest.verifySoapResponseInInterceptor(client);
        urlTest.verifyURLConfiguration(client, url);

        // test mtom
        mtomTest.verifyMTOMEnabled(client);
    }

    private CONNECTClient<TestServicePortType> createClient(String url, AssertionType assertion) {
        return CONNECTClientFactory.getInstance().getCONNECTClientUnsecured(new TestServicePortDescriptor(), url,
            assertion);
    }

    @Test
    public void testHttpsClientDisableCNCheckTrue() {
        String url = "https://url";
        AssertionType assertion = new AssertionType();
        assertion.setTransactionTimeout(-1);

        CONNECTClient<TestServicePortType> endpoint = null;
        try {
            PropertyAccessor.getInstance().setProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                NhincConstants.DISABLE_CN_CHECK, "true");

            boolean disableCNCheck = PropertyAccessor.getInstance()
                .getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.DISABLE_CN_CHECK);

            endpoint = createClient(url, assertion);

            Client client = ClientProxy.getClient(endpoint.getPort());

            HTTPConduit conduit = (HTTPConduit) client.getConduit();
            TLSClientParameters tlsPara = conduit.getTlsClientParameters();

            Assert.assertTrue("disableCNCheck==true", tlsPara.isDisableCNCheck());
        } catch (PropertyAccessException ex) {
            Assert.fail("Unable to set in memory property for disable-CN-Check.");
        }
    }

    @Test
    public void testHttpsClientDisableCNCheckFalse() {
        String url = "https://url";
        AssertionType assertion = new AssertionType();
        assertion.setTransactionTimeout(-1);

        CONNECTClient<TestServicePortType> endpoint = null;
        try {
            PropertyAccessor.getInstance().setProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                NhincConstants.DISABLE_CN_CHECK, "false");

            boolean disableCNCheck = PropertyAccessor.getInstance()
                .getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.DISABLE_CN_CHECK);

            endpoint = createClient(url, assertion);

            Client client = ClientProxy.getClient(endpoint.getPort());

            HTTPConduit conduit = (HTTPConduit) client.getConduit();
            TLSClientParameters tlsPara = conduit.getTlsClientParameters();

            Assert.assertFalse("disableCNCheck==false", tlsPara.isDisableCNCheck());
        } catch (PropertyAccessException ex) {
            Assert.fail("Unable to set in memory property for disable-CN-Check.");
        }
    }

}
