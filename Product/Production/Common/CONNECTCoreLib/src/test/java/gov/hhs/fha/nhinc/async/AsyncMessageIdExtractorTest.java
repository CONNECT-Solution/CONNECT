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
package gov.hhs.fha.nhinc.async;

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.headers.Header;
import org.junit.Test;
import org.w3c.dom.Element;

public class AsyncMessageIdExtractorTest {

    @Test
    // @Ignore
    public void pullsFirstSoapHeader() {
        List<Header> headers = new ArrayList<>();
        Element mockElement = addHeader(headers, "local");
        WebServiceContext mockServiceContext = createContextWithHeaders(headers);
        AsyncMessageIdExtractor extractor = new AsyncMessageIdExtractor();
        Element extractedElement = extractor.getSoapHeaderElement(mockServiceContext, "local");
        assertSame(mockElement, extractedElement);
    }

    private WebServiceContext createContextWithHeaders(List<Header> headers) {
        MessageContext mockMessageContext = mock(MessageContext.class);
        when(mockMessageContext.get(anyObject())).thenReturn(headers);

        WebServiceContext mockServiceContext = mock(WebServiceContext.class);
        when(mockServiceContext.getMessageContext()).thenReturn(mockMessageContext);

        return mockServiceContext;
    }

    private Element addHeader(List<Header> headers, String localHeaderName) {
        QName mockQName = new QName(localHeaderName);
        Element mockElement = mock(Element.class);
        SoapHeader header = mock(SoapHeader.class);
        when(header.getName()).thenReturn(mockQName);
        when(header.getObject()).thenReturn(mockElement);
        headers.add(header);
        return mockElement;
    }
}
