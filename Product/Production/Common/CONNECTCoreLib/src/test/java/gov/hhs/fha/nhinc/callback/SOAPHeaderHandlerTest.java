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
package gov.hhs.fha.nhinc.callback;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Iterator;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class SOAPHeaderHandlerTest {

    private static final String MESSAGE_ID_CONTEXT = "com.sun.xml.ws.addressing.response.messageID";
    private static final String WSA_NS = "http://www.w3.org/2005/08/addressing";
    private static final String MESSAGE_ID = "MessageID";
    private final String MESSAGE_ID_VALUE = "urn:uuid:1234";
    private SOAPMessageContext messageContext = mock(SOAPMessageContext.class);
    private SOAPMessage soapMessage = mock(SOAPMessage.class);
    private SOAPHeader soapHeader = mock(SOAPHeader.class);
    private ArgumentCaptor<SOAPElement> soapElementCaptor = ArgumentCaptor.forClass(SOAPElement.class);

    @Before
    public void setUp() throws SOAPException {
        when(messageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)).thenReturn(true);
        when(messageContext.getMessage()).thenReturn(soapMessage);
        when(soapMessage.getSOAPHeader()).thenReturn(soapHeader);
        when(messageContext.containsKey(MESSAGE_ID_CONTEXT)).thenReturn(false);
    }

    @Test
    public void testGetHeaders() {
        SOAPHeaderHandler soapHeaderHandler = new SOAPHeaderHandler();
        Set<QName> header = soapHeaderHandler.getHeaders();

        assertTrue(header.isEmpty());
    }

    @Test
    public void testHandleMessage_NullMessageId() throws SOAPException {
        SOAPHeaderHandler headerHandler = new SOAPHeaderHandler() {
            @Override
            protected String generateMessageId() {
                return MESSAGE_ID_VALUE;
            }
        };

        assertOnResults(headerHandler);
    }

    @Test
    public void testHandleMessage_HeaderHasMessageId() throws SOAPException {
        SOAPHeaderHandler headerHandler = new SOAPHeaderHandler();

        when(messageContext.get(MESSAGE_ID_CONTEXT)).thenReturn(MESSAGE_ID_VALUE);
        when(soapHeader.hasChildNodes()).thenReturn(true);

        SOAPFactory soapFactory = SOAPFactory.newInstance();
        SOAPElement element = soapFactory.createElement(MESSAGE_ID, "blah", WSA_NS);
        Iterator<SOAPElement> iterator = mock(Iterator.class);
        when(soapHeader.getChildElements(any(QName.class))).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true);
        when(iterator.next()).thenReturn(element);

        assertTrue(headerHandler.handleMessage(messageContext));
        assertEquals(element.getTextContent(), MESSAGE_ID_VALUE);
    }

    @Test
    public void testHandleMessage_MessageIdStartsWithUUID() throws SOAPException {
        final String TEST_MESSAGE_VALUE = "uuid:1234";
        SOAPHeaderHandler headerHandler = new SOAPHeaderHandler();

        when(messageContext.get(MESSAGE_ID_CONTEXT)).thenReturn(TEST_MESSAGE_VALUE);
        assertOnResults(headerHandler);
    }

    @Test
    public void testHandleMessage_MessageIdStartsWithNothing() throws SOAPException {
        final String TEST_MESSAGE_VALUE = "1234";
        SOAPHeaderHandler headerHandler = new SOAPHeaderHandler();

        when(messageContext.get(MESSAGE_ID_CONTEXT)).thenReturn(TEST_MESSAGE_VALUE);
        assertOnResults(headerHandler);
    }

    @Test
    public void testHandleFault() {
        SOAPHeaderHandler headerHandler = new SOAPHeaderHandler();

        assertTrue(headerHandler.handleFault(messageContext));
    }

    @Test
    public void testGenerateMessageId() {
        SOAPHeaderHandler headerHandler = new SOAPHeaderHandler();
        String generatedId = headerHandler.generateMessageId();

        assertTrue(generatedId.trim().startsWith("urn:uuid:"));
    }

    private void assertOnResults(SOAPHeaderHandler headerHandler) throws SOAPException {
        assertTrue(headerHandler.handleMessage(messageContext));
        verify(soapHeader).addChildElement(soapElementCaptor.capture());
        SOAPElement element = soapElementCaptor.getValue();
        String resultId = element.getTextContent();
        assertEquals(resultId, MESSAGE_ID_VALUE);
    }
}
