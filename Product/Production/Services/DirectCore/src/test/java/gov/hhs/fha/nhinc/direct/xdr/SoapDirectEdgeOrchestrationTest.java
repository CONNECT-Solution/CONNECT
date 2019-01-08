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
package gov.hhs.fha.nhinc.direct.xdr;

import gov.hhs.fha.nhinc.direct.DirectAdapterFactory;
import gov.hhs.fha.nhinc.direct.DirectSender;
import gov.hhs.fha.nhinc.direct.addressparsing.FromAddressParser;
import gov.hhs.fha.nhinc.direct.addressparsing.FromAddressParserFactory;
import gov.hhs.fha.nhinc.direct.addressparsing.ToAddressParser;
import gov.hhs.fha.nhinc.direct.addressparsing.ToAddressParserFactory;
import gov.hhs.fha.nhinc.direct.xdr.audit.SoapEdgeAuditor;
import gov.hhs.fha.nhinc.direct.xdr.audit.SoapEdgeAuditorFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.util.HashSet;
import java.util.Set;
import javax.mail.Address;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.nhindirect.xd.common.DirectDocuments;
import org.nhindirect.xd.transform.XdsDirectDocumentsTransformer;

public class SoapDirectEdgeOrchestrationTest {

    private final XdsDirectDocumentsTransformer mockDocTransformer =
            mock(XdsDirectDocumentsTransformer.class);
    private final ProvideAndRegisterDocumentSetRequestType mockRequest =
            mock(ProvideAndRegisterDocumentSetRequestType.class);
    private final SoapEdgeContext mockContext = mock(SoapEdgeContext.class);

    private final SoapEdgeAuditorFactory mockAuditorFactory =
            mock(SoapEdgeAuditorFactory.class);
    private final ToAddressParserFactory mockToParserFactory =
            mock(ToAddressParserFactory.class);
    private final FromAddressParserFactory mockFromParserFactory =
            mock(FromAddressParserFactory.class);
    private final DirectAdapterFactory mockAdapterFactory =
            mock(DirectAdapterFactory.class);

    private final ToAddressParser mockToParser = mock(ToAddressParser.class);
    private final FromAddressParser mockFromParser = mock(FromAddressParser.class);
    private final SoapEdgeAuditor mockAuditor = mock(SoapEdgeAuditor.class);
    private final DirectSender mockSender = mock(DirectSender.class);

    private static final String TO_ADDRESS_VALUE = "To Address";
    private static final String FROM_ADDRESS_VALUE = "From Address";
    private static final String MESSAGE_ID = "12345";

    @Test
    public void testNotNull(){
        SoapDirectEdgeOrchestration orch = new SoapDirectEdgeOrchestration();
        assertNotNull(orch);
    }

    @Test
    public void testOrchestrate() throws Exception{
        SoapDirectEdgeOrchestration orch =
                new SoapDirectEdgeOrchestration(mockAuditorFactory,
                mockToParserFactory, mockFromParserFactory,
                mockAdapterFactory, mockDocTransformer);

        when(mockAuditorFactory.getAuditor()).thenReturn(mockAuditor);

        final DirectDocuments documents = new DirectDocuments();
        when(mockDocTransformer.transform(mockRequest)).thenReturn(documents);

        when(mockContext.getDirectTo()).thenReturn(TO_ADDRESS_VALUE);
        when(mockContext.getDirectFrom()).thenReturn(FROM_ADDRESS_VALUE);
        when(mockContext.getMessageId()).thenReturn(MESSAGE_ID);

        Address toAddress = mock(Address.class);
        Set<Address> toAddresses = new HashSet<>();
        toAddresses.add(toAddress);
        Address[] addressArray = toAddresses.toArray(new Address[0]);

        Address fromAddress = mock(Address.class);

        when(mockToParserFactory.getToParser()).thenReturn(mockToParser);
        when(mockFromParserFactory.getFromParser()).thenReturn(mockFromParser);

        when(mockToParser.parse(TO_ADDRESS_VALUE, documents)).thenReturn(toAddresses);
        when(mockFromParser.parse(FROM_ADDRESS_VALUE, documents)).thenReturn(fromAddress);

        when(mockAdapterFactory.getDirectSender()).thenReturn(mockSender);

        RegistryResponseType response =
                orch.orchestrate(mockRequest, mockContext);

        verify(mockAuditor).audit(SoapEdgeAuditor.PRINCIPAL,
                SoapEdgeAuditor.REQUESTRECIEVED_CATEGORY,
                SoapEdgeAuditor.REQUESTRECIEVED_MESSAGE,
                mockContext);

        verify(mockToParser).parse(TO_ADDRESS_VALUE, documents);
        verify(mockFromParser).parse(FROM_ADDRESS_VALUE, documents);

        verify(mockSender).sendOutboundDirect(fromAddress,
                addressArray, documents,
                MESSAGE_ID);

        verify(mockAuditor).audit(SoapEdgeAuditor.PRINCIPAL,
                SoapEdgeAuditor.RESPONSERETURNED_CATEGORY,
                SoapEdgeAuditor.RESPONSERETURNED_MESSAGE,
                mockContext);

        assertNotNull(response);
        assertEquals(response.getStatus(),
                NhincConstants.NHINC_ADHOC_QUERY_SUCCESS_RESPONSE);
    }
}
