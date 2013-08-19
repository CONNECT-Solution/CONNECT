/*
 * Copyright (c) 2009-2013, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.direct.xdr;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gov.hhs.fha.nhinc.direct.addressparsing.ToAddressParser;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;

import java.util.Collections;
import java.util.Set;

import javax.mail.Address;

import org.junit.Before;
import org.junit.Test;
import org.nhindirect.xd.common.DirectDocuments;
import org.nhindirect.xd.transform.XdsDirectDocumentsTransformer;
import org.nhindirect.xd.transform.exception.TransformationException;

public class SoapDirectEdgeOrchestrationTest {
    private SoapDirectEdgeOrchestration orch;
    private XdsDirectDocumentsTransformer mockDocTransformer;
    private ToAddressParser mockToParser;
    private ProvideAndRegisterDocumentSetRequestType mockRequest;
    private SoapEdgeContext mockContext;

    @Before
    public void before() {
        orch = new SoapDirectEdgeOrchestration();
        mockDocTransformer = mock(XdsDirectDocumentsTransformer.class);
        mockToParser = mock(ToAddressParser.class);
        mockRequest = mock(ProvideAndRegisterDocumentSetRequestType.class);
        mockContext = mock(SoapEdgeContext.class);
    }

    @Test(expected = TransformationException.class)
    public void sendMessageNullToAddresses() throws TransformationException {
        sendWithToParserResult(null);
    }

    @Test(expected = TransformationException.class)
    public void sendMessageEmptyToAddresses() throws TransformationException {
        Set<Address> emptyAddresses = Collections.emptySet();
        sendWithToParserResult(emptyAddresses);
    }

    private void sendWithToParserResult(Set<Address> addresses) throws TransformationException {
        when(mockToParser.parse(anyString(), any(DirectDocuments.class))).thenReturn(addresses);

        orch.setDocumentsTransformer(mockDocTransformer);
        orch.setToAddressParser(mockToParser);

        orch.sendMessage(mockRequest, mockContext);
    }
}
