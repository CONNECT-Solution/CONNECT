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
package gov.hhs.fha.nhinc.messaging.service.decorator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CONNECTCustomHttpHeadersType;
import gov.hhs.fha.nhinc.messaging.client.CONNECTTestClient;
import gov.hhs.fha.nhinc.messaging.service.ServiceEndpoint;
import gov.hhs.fha.nhinc.messaging.service.port.TestServicePortDescriptor;
import gov.hhs.fha.nhinc.messaging.service.port.TestServicePortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.ws.BindingProvider;
import org.apache.cxf.transports.http.configuration.ConnectionType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author jassmit
 */
public class HttpHeaderServiceEndpointDecoratorTest {

    public HttpHeaderServiceEndpointDecoratorTest() {
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
    }

    @Test
    public void testConfigure_WithAssertionValues() throws PropertyAccessException {
        final CONNECTTestClient<TestServicePortType> testClient = new CONNECTTestClient<>(
            new TestServicePortDescriptor());

        final ServiceEndpoint<TestServicePortType> serviceEndpoint = testClient.getServiceEndpoint();

        final PropertyAccessor mockPropAccessor = mock(PropertyAccessor.class);

        final AssertionType assertion = new AssertionType();
        assertion.setKeepAlive("true");

        final CONNECTCustomHttpHeadersType assertionHeaders = new CONNECTCustomHttpHeadersType();
        assertionHeaders.setHeaderName("headerName");
        assertionHeaders.setHeaderValue("headerValue");

        assertion.getCONNECTCustomHttpHeaders().add(assertionHeaders);

        final HttpHeaderServiceEndpointDecorator headerDecorator = new HttpHeaderServiceEndpointDecorator(
            serviceEndpoint, assertion) {

            @Override
            protected PropertyAccessor getPropertyAccessor() {
                return mockPropAccessor;
            }
        };

        when(mockPropAccessor.getPropertyNames(NhincConstants.GATEWAY_PROPERTY_FILE)).thenReturn(new HashSet<String>());

        headerDecorator.configure();

        validateConfiguration(headerDecorator);
    }

    @Test
    public void testConfigure_WithoutValues() throws PropertyAccessException {
        final CONNECTTestClient<TestServicePortType> testClient = new CONNECTTestClient<>(
            new TestServicePortDescriptor());

        final ServiceEndpoint<TestServicePortType> serviceEndpoint = testClient.getServiceEndpoint();

        final AssertionType assertion = new AssertionType();

        final HttpHeaderServiceEndpointDecorator headerDecorator = new HttpHeaderServiceEndpointDecorator(
            serviceEndpoint, assertion);

        headerDecorator.configure();

        final BindingProvider bp = (BindingProvider) headerDecorator.getPort();
        final Map<String, List<String>> bpMap = (Map<String, List<String>>) bp.getRequestContext()
            .get(NhincConstants.CUSTOM_HTTP_HEADERS);
        assertNull(bpMap);
    }

    @Test
    public void testConfigure_WithPropertyValues() throws PropertyAccessException {
        final CONNECTTestClient<TestServicePortType> testClient = new CONNECTTestClient<>(
            new TestServicePortDescriptor());

        final ServiceEndpoint<TestServicePortType> serviceEndpoint = testClient.getServiceEndpoint();

        final PropertyAccessor mockPropAccessor = mock(PropertyAccessor.class);

        final AssertionType assertion = new AssertionType();

        final HttpHeaderServiceEndpointDecorator headerDecorator = new HttpHeaderServiceEndpointDecorator(
            serviceEndpoint, assertion) {

            @Override
            protected PropertyAccessor getPropertyAccessor() {
                return mockPropAccessor;
            }
        };

        final String customHeaderName = "customName";
        final String customHeaderValue = "customValue";
        final Set<String> headerNames = new HashSet<>();
        headerNames.add(NhincConstants.CUSTOM_HTTP_HEADERS + "." + customHeaderName);

        when(mockPropAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.KEEP_ALIVE_PROP))
            .thenReturn("True");
        when(mockPropAccessor.getPropertyNames(NhincConstants.GATEWAY_PROPERTY_FILE)).thenReturn(headerNames);
        when(mockPropAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
            NhincConstants.CUSTOM_HTTP_HEADERS + "." + customHeaderName)).thenReturn(customHeaderValue);

        headerDecorator.configure();

        validateConfiguration(headerDecorator);
        final BindingProvider bp = (BindingProvider) headerDecorator.getPort();
        final Map<String, List<String>> bpMap = (Map<String, List<String>>) bp.getRequestContext()
            .get(NhincConstants.CUSTOM_HTTP_HEADERS);
        assertTrue(bpMap.containsKey("customName"));
    }

    private void validateConfiguration(final HttpHeaderServiceEndpointDecorator headerDecorator) {
        assertEquals(headerDecorator.getHTTPClientPolicy().getConnection(), ConnectionType.KEEP_ALIVE);

        final BindingProvider bp = (BindingProvider) headerDecorator.getPort();
        final Map<String, List<String>> bpMap = (Map<String, List<String>>) bp.getRequestContext()
            .get(NhincConstants.CUSTOM_HTTP_HEADERS);
        assertNotNull(bpMap);
        assertEquals(bpMap.size(), 1);
    }
}
