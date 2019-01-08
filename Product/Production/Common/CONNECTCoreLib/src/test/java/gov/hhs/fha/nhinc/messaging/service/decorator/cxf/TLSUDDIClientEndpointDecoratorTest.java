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

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTTestClient;
import gov.hhs.fha.nhinc.messaging.service.ServiceEndpoint;
import gov.hhs.fha.nhinc.messaging.service.port.TestServicePortDescriptor;
import gov.hhs.fha.nhinc.messaging.service.port.TestServicePortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author mpnguyen
 *
 */
public class TLSUDDIClientEndpointDecoratorTest {
    private PropertyAccessor mockPropAccessor;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        System.setProperty("nhinc.properties.dir", System.getProperty("user.dir") + "/src/test/resources");
        System.setProperty("javax.net.ssl.keyStore",
            System.getProperty("user.dir") + "/src/test/resources/gateway.jks");
        System.setProperty("javax.net.ssl.keyStoreType", "JKS");
        System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
        System.setProperty("javax.net.ssl.trustStore",
            System.getProperty("user.dir") + "/src/test/resources/gateway.jks");
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

        mockPropAccessor = mock(PropertyAccessor.class);

    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        mockPropAccessor = null;
    }

    @Test
    public final void testTLSConfiguration() throws PropertyAccessException {
        when(mockPropAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, "UDDI.TLS")).thenReturn(null);
        when(mockPropAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.DISABLE_CN_CHECK,
            false)).thenReturn(true);
        CONNECTClient<TestServicePortType> uddiClient = createClient(mockPropAccessor, null);
        Assert.assertNotNull(uddiClient);
        TLSClientParameters tlsCP = retrieveTLSClientParamters(uddiClient);
        assertTrue(tlsCP.isDisableCNCheck());
        Assert.assertNull(tlsCP.getSecureSocketProtocol());
    }

    @Test
    public void testTLSV12Configuration() throws PropertyAccessException {
        when(mockPropAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, "UDDI.TLS")).thenReturn("TLSv1.2");
        when(mockPropAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE,
            NhincConstants.DISABLE_CN_CHECK, false)).thenReturn(true);
        CONNECTClient<TestServicePortType> uddiClient = createClient(mockPropAccessor, null);
        Assert.assertNotNull(uddiClient);
        TLSClientParameters tlsCP = retrieveTLSClientParamters(uddiClient);
        assertTrue(tlsCP.isDisableCNCheck());
        String tls_protocol = tlsCP.getSecureSocketProtocol();
        Assert.assertEquals("TLSv1.2", tlsCP.getSecureSocketProtocol());
    }

    /**
     * @param uddiClient
     * @return
     */
    private TLSClientParameters retrieveTLSClientParamters(CONNECTClient<TestServicePortType> uddiClient) {
        Client clientProxy = ClientProxy.getClient(uddiClient.getPort());
        HTTPConduit conduit = (HTTPConduit) clientProxy.getConduit();
        TLSClientParameters tlsCP = conduit.getTlsClientParameters();
        return tlsCP;
    }

    private CONNECTClient<TestServicePortType> createClient(final PropertyAccessor propAccessor, String gatewayAlias)
        throws PropertyAccessException {
        CONNECTTestClient<TestServicePortType> testClient = new CONNECTTestClient<>(new TestServicePortDescriptor());

        ServiceEndpoint<TestServicePortType> serviceEndpoint = testClient.getServiceEndpoint();
        final TLSUDDIClientEndpointDecorator uddiDecorator = new TLSUDDIClientEndpointDecorator(serviceEndpoint,
            gatewayAlias) {
            @Override
            protected PropertyAccessor getPropertyAccessor() {
                return propAccessor;
            }
        };
        uddiDecorator.configure();
        return testClient;
    }

}
