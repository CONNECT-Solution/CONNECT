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
package gov.hhs.fha.nhinc.direct;

import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.getSampleMimeMessage;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.direct.event.DirectEventLogger;
import gov.hhs.fha.nhinc.mail.MailClientException;
import gov.hhs.fha.nhinc.mail.MailSender;
import gov.hhs.fha.nhinc.mail.MessageHandler;
import java.net.URL;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.junit.Test;
import org.nhindirect.gateway.smtp.MessageProcessResult;
import org.nhindirect.gateway.smtp.SmtpAgent;
import org.nhindirect.stagent.NHINDAddress;
import org.nhindirect.stagent.NHINDAddressCollection;

/**
 * Test {@link OutboundMessageHandler}.
 */
public class DirectOutboundMsgHandlerTest extends DirectBaseTest {

    private SmtpAgent mockSmtpAgent;
    private MessageProcessResult mockResult;
    private MailSender mockExtMailSender;
    private DirectSender directSender;
    private MessageHandler testOutboundMsgHandler;
    private DirectAdapter directAdapter;

    /**
     * Verify that the Outbound Message Handler will use the external direct mail client to directify the message. The
     * internal direct mail client passed in will be ignored.
     *
     * @throws MailClientException
     * @throws MessagingException
     */
    @Test
    public void canHandleOutboundMsg() throws MailClientException, MessagingException {

        mockSmtpAgent = mock(SmtpAgent.class);
        mockResult = DirectUnitTestUtil.getMockMessageProcessResult(1);
        directAdapter = mock(DirectAdapter.class);
        when(directAdapter.getSmtpAgent()).thenReturn(mockSmtpAgent);
        when(mockSmtpAgent.processMessage(any(MimeMessage.class), any(NHINDAddressCollection.class),
            any(NHINDAddress.class))).thenReturn(mockResult);

        mockExtMailSender = mock(MailSender.class);
        directSender = new DirectSenderImpl(mockExtMailSender, DirectEventLogger.getInstance()) {
            //igonre it for now. Need to come up with better unit tests for this
            @Override
            protected void addOutgoingMessage(MimeMessage message, boolean failed) {
            }

            @Override
            protected SmtpAgent getSmtpAgent(URL url) {
                return mockSmtpAgent;
            }
        };
        testOutboundMsgHandler = new DirectOutboundMsgHandler(directSender);

        MimeMessage mimeMessage = getSampleMimeMessage();
        testOutboundMsgHandler.handleMessage(mimeMessage);

        verify(mockExtMailSender).send(any(Address[].class), any(MimeMessage.class));
    }
}
