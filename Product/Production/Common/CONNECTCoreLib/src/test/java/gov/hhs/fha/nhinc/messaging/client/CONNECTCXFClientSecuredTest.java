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
import gov.hhs.fha.nhinc.messaging.service.decorator.SAMLServiceEndpointDecoratorTest;
import gov.hhs.fha.nhinc.messaging.service.decorator.TimeoutServiceEndpointDecorator;
import gov.hhs.fha.nhinc.messaging.service.decorator.TimeoutServiceEndpointDecoratorTest;
import gov.hhs.fha.nhinc.messaging.service.decorator.URLServiceEndpointDecoratorTest;
import gov.hhs.fha.nhinc.messaging.service.decorator.cxf.SoapResponseServiceEndpointDecoratorTest;
import gov.hhs.fha.nhinc.messaging.service.decorator.cxf.TLSClientServiceEndpointDecoratorTest;
import gov.hhs.fha.nhinc.messaging.service.decorator.cxf.WsAddressingServiceEndpointDecoratorTest;
import gov.hhs.fha.nhinc.messaging.service.decorator.cxf.WsSecurityServiceEndpointDecoratorTest;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.messaging.service.port.TestServicePortDescriptor;
import gov.hhs.fha.nhinc.messaging.service.port.TestServicePortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author akong
 *
 */
public class CONNECTCXFClientSecuredTest {

    private final TimeoutServiceEndpointDecoratorTest timeoutTest = new TimeoutServiceEndpointDecoratorTest();
    private final MTOMServiceEndpointDecoratorTest mtomTest = new MTOMServiceEndpointDecoratorTest();
    private final SoapResponseServiceEndpointDecoratorTest responseTest = new SoapResponseServiceEndpointDecoratorTest();
    private final URLServiceEndpointDecoratorTest urlTest = new URLServiceEndpointDecoratorTest();

    private final SAMLServiceEndpointDecoratorTest samlTest = new SAMLServiceEndpointDecoratorTest();
    private final TLSClientServiceEndpointDecoratorTest tlsTest = new TLSClientServiceEndpointDecoratorTest();
    private final WsAddressingServiceEndpointDecoratorTest addressTest = new WsAddressingServiceEndpointDecoratorTest();
    private final WsSecurityServiceEndpointDecoratorTest securityTest = new WsSecurityServiceEndpointDecoratorTest();

    private static final int TIMEOUT = 100;

    @Before
    public void setUpTest(){
        try {
            PropertyAccessor.getInstance().setProperty(NhincConstants.GATEWAY_PROPERTY_FILE, TimeoutServiceEndpointDecorator.CONFIG_KEY_TIMEOUT, Integer.toString(TIMEOUT));
        } catch (PropertyAccessException ex) {
            System.out.println("Unable to set in memory property for timeout decorator.");
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

    @Ignore
    @Test
    public void securedClientConfiguration() {
        String url = "url";
        String messageId = "urn:uuid:messageId";
        String relatesTo = "relatesTo";
        AssertionType assertion = new AssertionType();
        assertion.setMessageId(messageId);
        assertion.getRelatesToList().add(relatesTo);
        ServicePortDescriptor<TestServicePortType> portDescriptor = new TestServicePortDescriptor();

        CONNECTClient<TestServicePortType> client = createClient(url, assertion);

        // default configuration
        timeoutTest.verifyTimeoutIsSet(client, TIMEOUT);
        responseTest.verifySoapResponseInInterceptor(client);
        urlTest.verifyURLConfiguration(client, url);

        // secured configuration
        samlTest.verifySAMLConfiguration(client, assertion);
        tlsTest.verifyTLSConfiguration(client);
        addressTest.verifyAddressingProperties(client, url, portDescriptor.getWSAddressingAction(),
                messageId, relatesTo);
        securityTest.verifyWsSecurityProperties(client);
    }

    @Ignore
    @Test
    public void testEnableMtom() {
        String url = "url";
        String messageId = "urn:uuid:messageId";
        String relatesTo = "relatesTo";
        AssertionType assertion = new AssertionType();
        assertion.setMessageId(messageId);
        assertion.getRelatesToList().add(relatesTo);
        ServicePortDescriptor<TestServicePortType> portDescriptor = new TestServicePortDescriptor();

        CONNECTClient<TestServicePortType> client = createClient(url, assertion);
        client.enableMtom();

        // default configuration
        timeoutTest.verifyTimeoutIsSet(client, TIMEOUT);
        responseTest.verifySoapResponseInInterceptor(client);
        urlTest.verifyURLConfiguration(client, url);

        // secured configuration
        samlTest.verifySAMLConfiguration(client, assertion);
        tlsTest.verifyTLSConfiguration(client);
        addressTest.verifyAddressingProperties(client, url, portDescriptor.getWSAddressingAction(),
                messageId, relatesTo);
        securityTest.verifyWsSecurityProperties(client);

        // mtom configuration
        mtomTest.verifyMTOMEnabled(client);
    }

    private CONNECTClient<TestServicePortType> createClient(String url, AssertionType assertion) {
        return CONNECTClientFactory.getInstance().getCONNECTClientSecured(new TestServicePortDescriptor(), url,
            assertion);
    }

}
