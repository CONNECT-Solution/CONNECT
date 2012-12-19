/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.messaging.service.decorator;

import static org.junit.Assert.assertEquals;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTTestClient;
import gov.hhs.fha.nhinc.messaging.service.ServiceEndpoint;
import gov.hhs.fha.nhinc.messaging.service.port.TestServicePortDescriptor;
import gov.hhs.fha.nhinc.messaging.service.port.TestServicePortType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.junit.Test;

/**
 * @author akong
 * 
 */
public class TimeoutServiceEndpointDecoratorTest {

    @Test
    public void testTimeoutIsSet() {
        CONNECTClient<TestServicePortType> client = createClient();

        verifyTimeoutIsSet(client);
    }

    public void verifyTimeoutIsSet(CONNECTClient<?> client) {
        int timeout = getTimeoutFromConfig();
        
        Client clientProxy = ClientProxy.getClient(client.getPort());
        HTTPConduit conduit = (HTTPConduit) clientProxy.getConduit();

        assertEquals(timeout, conduit.getClient().getConnectionTimeout());
        assertEquals(timeout, conduit.getClient().getReceiveTimeout());
    }
    
    private int getTimeoutFromConfig() {
        int timeout = 0;
        try {
            String sValue = PropertyAccessor.getInstance().getProperty(TimeoutServiceEndpointDecorator.CONFIG_KEY_TIMEOUT);
            if (NullChecker.isNotNullish(sValue)) {
                timeout = Integer.parseInt(sValue);
            }
        } catch (Exception ex) {
            // Do Nothing
        } 
        return timeout;
    }

    private CONNECTClient<TestServicePortType> createClient() {
        CONNECTTestClient<TestServicePortType> testClient = new CONNECTTestClient<TestServicePortType>(
                new TestServicePortDescriptor());

        ServiceEndpoint<TestServicePortType> serviceEndpoint = testClient.getServiceEndpoint();
        serviceEndpoint = new TimeoutServiceEndpointDecorator<TestServicePortType>(serviceEndpoint);
        serviceEndpoint.configure();

        return testClient;
    }
}
