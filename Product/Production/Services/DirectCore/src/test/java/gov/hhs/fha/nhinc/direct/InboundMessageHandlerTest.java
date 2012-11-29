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
package gov.hhs.fha.nhinc.direct;

import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.getSampleMimeMessage;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.hhs.fha.nhinc.direct.edge.proxy.DirectEdgeProxy;
import gov.hhs.fha.nhinc.direct.edge.proxy.DirectEdgeProxySmtpImpl;
import gov.hhs.fha.nhinc.direct.edge.proxy.DirectEdgeProxySoapImpl;

import javax.mail.internet.MimeMessage;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.nhindirect.gateway.smtp.MessageProcessResult;
import org.nhindirect.gateway.smtp.SmtpAgent;
import org.nhindirect.stagent.MessageEnvelope;
import org.nhindirect.stagent.NHINDAddress;
import org.nhindirect.stagent.NHINDAddressCollection;

/**
 * Test {@link InboundMessageHandler}.
 */
public class InboundMessageHandlerTest {

    private final SmtpAgent mockSmtpAgent = mock(SmtpAgent.class);
    private final DirectMailClient mockExternalDirectMailClient = mock(DirectMailClient.class);
    private final DirectMailClient mockInternalDirectMailClient = mock(DirectMailClient.class);
    private final MessageProcessResult mockResult = mock(MessageProcessResult.class);
    private final MessageEnvelope mockProcessedMessage = mock(MessageEnvelope.class);
    private InboundMessageHandler testInBoundMessageHandler = new InboundMessageHandler();

    /**
     * Set up before each test.
     */
    @Before
    public void setUp() {        
        when(mockSmtpAgent.processMessage(any(MimeMessage.class), any(NHINDAddressCollection.class),
                any(NHINDAddress.class))).thenReturn(mockResult);
        when(mockResult.getProcessedMessage()).thenReturn(mockProcessedMessage);
        when(mockExternalDirectMailClient.getSmtpAgent()).thenReturn(mockSmtpAgent);
    }
    
    /**
     * Test {@link InboundMessageHandler#handleMessage(MimeMessage, DirectClient)}
     * Verify that an inbound message is handled and passed to the internal smtp mail server.
     */
    @Test
    public void canHandleInboundMessageSmtpEdgeClient() {                
        setUpForSmtpEdgeClient();
        testInBoundMessageHandler.handleMessage(getSampleMimeMessage(), mockExternalDirectMailClient);
        verifyInboundMessageHandler(1);
    }
    
    /**
     * Test {@link InboundMessageHandler#handleMessage(MimeMessage, DirectClient)}
     * Verify that the notification messages are logged if the processed message is null. 
     */
    @Test
    public void willLogNotificationsWhenProcessedMessageIsNullSmtpEdgeClient() {
        setUpForSmtpEdgeClient();
        when(mockResult.getProcessedMessage()).thenReturn(null);
        testInBoundMessageHandler.handleMessage(getSampleMimeMessage(), mockExternalDirectMailClient);
        verifyInboundMessageHandler(0);
    }
    
    /**
     * Test {@link InboundMessageHandler#handleMessage(MimeMessage, DirectClient)}
     * Verify that an inbound message is handled and passed to the internal smtp mail server.
     */
    @Test
    @Ignore
    public void canHandleInboundMessageSoapEdgeClient() {                
        setUpForSoapEdgeClient();
        testInBoundMessageHandler.handleMessage(getSampleMimeMessage(), mockExternalDirectMailClient);
        verifyInboundMessageHandler(0);
    }

    private void verifyInboundMessageHandler(int timesSentToInternalSmtp) {
        // verify the smtp agent is used to process the message.
        verify(mockSmtpAgent).processMessage(any(MimeMessage.class), any(NHINDAddressCollection.class),
                any(NHINDAddress.class));

        // verify that the external direct mail client is used to send the MDN notification emails.
        verify(mockExternalDirectMailClient).sendMdn(eq(mockResult));

        // verify that the internal direct mail client is used n times to resend the message
        verify(mockInternalDirectMailClient, times(timesSentToInternalSmtp)).send(any(MimeMessage.class));        
    }
    
    private void setUpForSmtpEdgeClient() {
        testInBoundMessageHandler = new InboundMessageHandler() {
            @Override
            protected DirectEdgeProxy getDirectEdgeProxy() {
                DirectEdgeProxySmtpImpl impl = new DirectEdgeProxySmtpImpl();
                impl.setInternalDirectClient(mockInternalDirectMailClient);
                return impl;
            }
        };
    }

    private void setUpForSoapEdgeClient() {
        testInBoundMessageHandler = new InboundMessageHandler() {
            @Override
            protected DirectEdgeProxy getDirectEdgeProxy() {
                return new DirectEdgeProxySoapImpl();
            }
        };        
    }

}
