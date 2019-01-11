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
package gov.hhs.fha.nhinc.messaging.service.decorator.cxf;

import static org.junit.Assert.assertEquals;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTTestClient;
import gov.hhs.fha.nhinc.messaging.service.ServiceEndpoint;
import gov.hhs.fha.nhinc.messaging.service.port.TestServicePortDescriptor;
import gov.hhs.fha.nhinc.messaging.service.port.TestServicePortType;
import javax.xml.ws.BindingProvider;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.cxf.ws.addressing.AddressingProperties;
import org.apache.cxf.ws.addressing.JAXWSAConstants;
import org.junit.Test;

/**
 * @author akong
 *
 */
public class WsAddressingServiceEndpointDecoratorTest {

    @Test
    public void testAddressingProperties() {
        String wsAddressingTo = "wsAddressingTo";
        String wsAddressingAction = "wsAddressingAction";
        String messageId = "urn:uuid:messageId";
        String relatesTo = "relatesTo";
        AssertionType assertion = new AssertionType();
        assertion.setMessageId(messageId);
        assertion.getRelatesToList().add(relatesTo);

        CONNECTClient<TestServicePortType> client = createClient(wsAddressingTo, wsAddressingAction, assertion);

        verifyAddressingProperties(client, wsAddressingTo, wsAddressingAction, messageId, relatesTo);
    }

    @Test
    public void verifyMessageIdPrefix() {
        String messageId = "messageId";
        String messageIdWithPrefix = "urn:uuid:messageId";

        AssertionType assertion = new AssertionType();
        assertion.setMessageId(messageId);

        CONNECTClient<TestServicePortType> client = createClient(null, null, assertion);

        BindingProvider bindingProviderPort = (BindingProvider) client.getPort();
        AddressingProperties addressingProps = (AddressingProperties) bindingProviderPort.getRequestContext()
                .get(JAXWSAConstants.CLIENT_ADDRESSING_PROPERTIES);

        assertEquals(messageIdWithPrefix, addressingProps.getMessageID().getValue());
    }

    @Test
    public void verifyNullRelatesTo() {
        String wsAddressingTo = "wsAddressingTo";
        String wsAddressingAction = "wsAddressingAction";
        String messageId = "urn:uuid:messageId";
        AssertionType assertion = new AssertionType();
        assertion.setMessageId(messageId);

        CONNECTClient<TestServicePortType> client = createClient(wsAddressingTo, wsAddressingAction, assertion);

        BindingProvider bindingProviderPort = (BindingProvider) client.getPort();

        AddressingProperties addressingProps = (AddressingProperties) bindingProviderPort.getRequestContext()
                .get(JAXWSAConstants.CLIENT_ADDRESSING_PROPERTIES);

        assertEquals(wsAddressingTo, addressingProps.getTo().getValue());
        assertEquals(wsAddressingAction, addressingProps.getAction().getValue());
        assertEquals(messageId, addressingProps.getMessageID().getValue());
        assertEquals(null, addressingProps.getRelatesTo());
    }

    @Test
    public void verifyEmptyRelatesTo() {
        String wsAddressingTo = "wsAddressingTo";
        String wsAddressingAction = "wsAddressingAction";
        String messageId = "urn:uuid:messageId";
        AssertionType assertion = new AssertionType();
        assertion.setMessageId(messageId);

        CONNECTClient<TestServicePortType> client = createClient(wsAddressingTo, wsAddressingAction, assertion);

        BindingProvider bindingProviderPort = (BindingProvider) client.getPort();

        AddressingProperties addressingProps = (AddressingProperties) bindingProviderPort.getRequestContext()
                .get(JAXWSAConstants.CLIENT_ADDRESSING_PROPERTIES);

        assertEquals(wsAddressingTo, addressingProps.getTo().getValue());
        assertEquals(wsAddressingAction, addressingProps.getAction().getValue());
        assertEquals(messageId, addressingProps.getMessageID().getValue());
        assertEquals(null, addressingProps.getRelatesTo());
    }

    /**
     * This method will verify the client is configured with the passed in WS-Addressing values. Note that the messageId
     * will need to have the proper "urn:uuid" prefix or else this verification will always fail.
     *
     * @param client
     * @param wsAddressingTo
     * @param wsAddressingAction
     * @param messageId
     * @param relatesTo
     */
    public void verifyAddressingProperties(CONNECTClient<?> client, String wsAddressingTo, String wsAddressingAction,
            String messageId, String relatesTo) {
        BindingProvider bindingProviderPort = (BindingProvider) client.getPort();

        AddressingProperties addressingProps = (AddressingProperties) bindingProviderPort.getRequestContext()
                .get(JAXWSAConstants.CLIENT_ADDRESSING_PROPERTIES);
        HTTPClientPolicy httpClientPolicy = (HTTPClientPolicy) bindingProviderPort.getRequestContext().get(
                HTTPClientPolicy.class.getName());

        assertEquals(wsAddressingTo, addressingProps.getTo().getValue());
        assertEquals(wsAddressingAction, addressingProps.getAction().getValue());
        assertEquals(messageId, addressingProps.getMessageID().getValue());
        assertEquals(relatesTo, addressingProps.getRelatesTo().getValue());
        assertEquals("application/soap+xml; charset=UTF-8", httpClientPolicy.getContentType());
    }

    private CONNECTClient<TestServicePortType> createClient(String wsAddressingTo, String wsAddressingAction,
            AssertionType assertion) {
        CONNECTTestClient<TestServicePortType> testClient = new CONNECTTestClient<>(
                new TestServicePortDescriptor());

        ServiceEndpoint<TestServicePortType> serviceEndpoint = testClient.getServiceEndpoint();
        serviceEndpoint = new WsAddressingServiceEndpointDecorator<>(serviceEndpoint,
                wsAddressingTo, wsAddressingAction, assertion);
        serviceEndpoint.configure();

        return testClient;
    }

}
