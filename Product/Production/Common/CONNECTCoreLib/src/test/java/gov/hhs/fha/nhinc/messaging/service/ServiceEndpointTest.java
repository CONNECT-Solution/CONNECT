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
package gov.hhs.fha.nhinc.messaging.service;

import java.util.HashMap;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import static junit.framework.Assert.assertSame;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author bhumphrey
 *
 */
public class ServiceEndpointTest {

    BaseServiceEndpoint<BindingProvider> serviceEndpoint;
    Map<String, Object> requestContext;

    @Before
    public void setUpTest() {
        serviceEndpoint = (BaseServiceEndpoint<BindingProvider>) mock(BaseServiceEndpoint.class,
                Mockito.CALLS_REAL_METHODS);
        BindingProvider bindingProvider = mock(BindingProvider.class);
        serviceEndpoint.port = bindingProvider;

        requestContext = new HashMap<>();
        when(serviceEndpoint.port.getRequestContext()).thenReturn(requestContext);

    }

    @Test
    public void verifyNewIsCreatedWhenNull() {

        HTTPClientPolicy httpClientPolicy = serviceEndpoint.getHTTPClientPolicy();
        assertNotNull(httpClientPolicy);

        assertTrue(requestContext.containsKey(HTTPClientPolicy.class.getName()));

    }

    @Test
    public void verifyExsistingOneIsUsedWhenPresent() {
        HTTPClientPolicy realHttpClientPolicy = new HTTPClientPolicy();
        requestContext.put(HTTPClientPolicy.class.getName(), realHttpClientPolicy);

        HTTPClientPolicy httpClientPolicy = serviceEndpoint.getHTTPClientPolicy();
        assertNotNull(httpClientPolicy);

        assertSame(realHttpClientPolicy, httpClientPolicy);

    }

    @Test
    public void getSOAPBinding() {

        serviceEndpoint.getSOAPBinding();
        verify(serviceEndpoint.port).getBinding();

    }
}
