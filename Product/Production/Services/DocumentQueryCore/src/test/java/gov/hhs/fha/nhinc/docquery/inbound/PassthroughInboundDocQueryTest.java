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
package gov.hhs.fha.nhinc.docquery.inbound;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docquery.deferredresponse.adapter.proxy.AdapterDeferredResponseOptionProxy;
import gov.hhs.fha.nhinc.document.DocumentConstants;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author akong
 *
 */
public class PassthroughInboundDocQueryTest extends InboundDocQueryTest {

    private static final int NUM_TIMES_TO_INVOKE_ADAPTER_AUDIT = 0;

    @Test
    public void passthroughInboundDocQueryOrgHcid() {
        passthroughInboundDocQueryHomeHcid(SENDING_HCID_ORG, SENDING_HCID_ORG_FORMATTED);
    }

    @Test
    public void passthroughInboundDocQueryHomeHcid() {
        passthroughInboundDocQueryHomeHcid(SENDING_HCID_HOME, SENDING_HCID_HOME_FORMATTED);
    }

    private void passthroughInboundDocQueryHomeHcid(String sendingHcid, String sendingHcidFormatted) {
        AssertionType mockAssertion = getMockAssertion(sendingHcid);

        when(mockAssertion.getDeferredResponseEndpoint()).thenReturn("http://deferredEnpoint/");
        final AdapterDeferredResponseOptionProxy mockDeferredProxy = mock(AdapterDeferredResponseOptionProxy.class);
        RegistryResponseType mockResponse = mock(RegistryResponseType.class);
        when(mockDeferredProxy.processRequest(Mockito.any(AdhocQueryRequest.class), Mockito.any(AssertionType.class)))
        .thenReturn(mockResponse);
        when(mockResponse.getStatus()).thenReturn(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_SUCCESS);

        PassthroughInboundDocQuery passthroughDocQuery = new PassthroughInboundDocQuery(
            getMockAdapterFactory(mockAssertion), getAuditLogger(true)) {
            @Override
            protected AdapterDeferredResponseOptionProxy getAdapterDeferredProxy() {
                return mockDeferredProxy;
            }
        };

        verifyInboundDocQuery(mockAssertion, sendingHcidFormatted, passthroughDocQuery,
            NUM_TIMES_TO_INVOKE_ADAPTER_AUDIT);
        verify(mockDeferredProxy, times(1)).processRequest(Mockito.any(AdhocQueryRequest.class),
            Mockito.any(AssertionType.class));
    }

}
