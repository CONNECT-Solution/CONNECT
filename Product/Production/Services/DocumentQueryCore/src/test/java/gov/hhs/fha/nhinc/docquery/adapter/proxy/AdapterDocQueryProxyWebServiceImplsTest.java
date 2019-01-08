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
package gov.hhs.fha.nhinc.docquery.adapter.proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.event.error.ErrorEventException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import org.junit.Test;

public class AdapterDocQueryProxyWebServiceImplsTest {

    private BaseAdapterDocQueryProxy[] proxies = new BaseAdapterDocQueryProxy[]{
        new AdapterDocQueryProxyWebServiceSecuredImpl(), new AdapterDocQueryProxyWebServiceUnsecuredImpl()};

    private String[] serciveNames = {NhincConstants.ADAPTER_DOC_QUERY_SECURED_SERVICE_NAME,NhincConstants.ADAPTER_DOC_QUERY_SERVICE_NAME};

    @Test
    public void hasDefaultAdapterHelper() {
        for (BaseAdapterDocQueryProxy impl : proxies) {
            assertNotNull(impl.getAdapterHelper());
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void adapterHelperCreatesErrorResponse() throws Exception {
        for (BaseAdapterDocQueryProxy impl : proxies) {
            WebServiceProxyHelper proxyMock = mock(WebServiceProxyHelper.class);
            when(proxyMock.getAdapterEndPointFromConnectionManager(anyString())).thenThrow(RuntimeException.class);
            when(proxyMock.getEndPointFromConnectionManagerByAdapterAPILevel(anyString(), any(NhincConstants.ADAPTER_API_LEVEL.class))).thenThrow(RuntimeException.class);
            impl.setWebServiceProxyHelper(proxyMock);
            AdapterHelper helper = mock(AdapterHelper.class);
            impl.setAdapterHelper(helper);
            try {
                impl.respondingGatewayCrossGatewayQuery(null, null);
                fail("We didnt throw an exception with invalid parameters!");
            } catch (Exception e) {
                assertTrue(e instanceof ErrorEventException);
                ErrorEventException error = (ErrorEventException) e;
                assertEquals(error.getReturnOverride(), helper.createErrorResponse());
            }

        }
    }

    @Test
    public void checkEndpointURLBasedOnImplementsSpecVersion() throws Exception {
        final String a0_URL = "a0 URL";
        final String a1_URL = "a1 URL";
        String url;

        WebServiceProxyHelper proxyMock = mock(WebServiceProxyHelper.class);
        when(proxyMock.getAdapterEndPointFromConnectionManager(anyString())).thenReturn(a1_URL);
        when(proxyMock.getEndPointFromConnectionManagerByAdapterAPILevel(anyString(), eq(NhincConstants.ADAPTER_API_LEVEL.LEVEL_a0))).thenReturn(a0_URL);
        when(proxyMock.getEndPointFromConnectionManagerByAdapterAPILevel(anyString(), eq(NhincConstants.ADAPTER_API_LEVEL.LEVEL_a1))).thenReturn(a1_URL);
        //check for bot secured and unsecured endpoints
        for (int i = 0; i < proxies.length; ++i) {
            //instance of assertion type
            AssertionType assertion = new AssertionType();
            BaseAdapterDocQueryProxy impl = proxies[i];
            impl.setWebServiceProxyHelper(proxyMock);
            //for 2010
            assertion.setImplementsSpecVersion(NhincConstants.UDDI_SPEC_VERSION.SPEC_2_0.toString());
            url = impl.getEndPointFromConnectionManagerByAdapterAPILevel(assertion,serciveNames[i]);
            assertEquals(a0_URL, url);

            //for 2011
            assertion.setImplementsSpecVersion(NhincConstants.UDDI_SPEC_VERSION.SPEC_3_0.toString());
            url = impl.getEndPointFromConnectionManagerByAdapterAPILevel(assertion,serciveNames[i]);
            assertEquals(a1_URL, url);

            //if ImplementsSpecVersion is null
            assertion.setImplementsSpecVersion(null);
            url = impl.getEndPointFromConnectionManagerByAdapterAPILevel(assertion,serciveNames[i]);
            assertEquals(a1_URL, url);
        }
    }
}
