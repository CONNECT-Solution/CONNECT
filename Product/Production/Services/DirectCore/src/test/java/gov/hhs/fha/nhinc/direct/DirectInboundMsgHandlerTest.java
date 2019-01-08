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
import gov.hhs.fha.nhinc.direct.edge.proxy.DirectEdgeProxy;
import gov.hhs.fha.nhinc.direct.edge.proxy.DirectEdgeProxySmtpImpl;
import gov.hhs.fha.nhinc.direct.edge.proxy.DirectEdgeProxySoapImpl;
import gov.hhs.fha.nhinc.direct.event.DirectEventLogger;
import gov.hhs.fha.nhinc.mail.MailClientException;
import gov.hhs.fha.nhinc.mail.MailSender;
import gov.hhs.fha.nhinc.mail.MessageHandler;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import java.net.MalformedURLException;
import java.net.URL;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.nhindirect.gateway.smtp.MessageProcessResult;
import org.nhindirect.gateway.smtp.SmtpAgent;
import org.nhindirect.stagent.NHINDAddress;
import org.nhindirect.stagent.NHINDAddressCollection;

/**
 * Test {@link InboundMessageHandler}.
 */
public class DirectInboundMsgHandlerTest extends DirectBaseTest {

    private SmtpAgent mockSmtpAgent;
    private MailSender mockExtMailSender;
    private MailSender mockIntMailSender;
    private MessageProcessResult mockResult;
    private DirectReceiver directReceiver;
    private MessageHandler testInboundMsgHandler;

    /**
     * Set up before each test.
     *
     * @throws MessagingException
     */
    @Before
    public void setUp() throws MessagingException, MalformedURLException {
        mockSmtpAgent = mock(SmtpAgent.class);
        mockExtMailSender = mock(MailSender.class);
        mockIntMailSender = mock(MailSender.class);
        mockResult = DirectUnitTestUtil.getMockMessageProcessResult(1);
        when(mockSmtpAgent.processMessage(any(MimeMessage.class), any(NHINDAddressCollection.class), any(NHINDAddress.class))).thenReturn(mockResult);
    }

    /**
     * Test {@link InboundMessageHandler#handleMessage(MimeMessage, DirectClient)} Verify that an inbound message is
     * handled and passed to the internal smtp mail server.
     *
     * @throws MailClientException
     */
    @Test
    public void canHandleInboundMessageSmtpEdgeClient() throws MailClientException {
        setUpForSmtpEdgeClient();
        testInboundMsgHandler.handleMessage(getSampleMimeMessage());
        verifyInboundMessageHandler(2, 1, 1);
    }

    /**
     * Test {@link InboundMessageHandler#handleMessage(MimeMessage, DirectClient)} Verify that the notification messages
     * are logged if the processed message is null.
     *
     * @throws MailClientException
     */
    @Test
    public void willLogNotificationsWhenProcessedMessageIsNullSmtpEdgeClient() throws MailClientException {
        setUpForSmtpEdgeClient();
        when(mockResult.getProcessedMessage()).thenReturn(null);
        testInboundMsgHandler.handleMessage(getSampleMimeMessage());
        verifyInboundMessageHandler(1, 0, 0);
    }

    /**
     * Test {@link InboundMessageHandler#handleMessage(MimeMessage, DirectClient)} Verify that an inbound message is
     * handled and passed to the internal smtp mail server.
     *
     * @throws MailClientException
     */
    @Test
    public void canHandleInboundMessageSoapEdgeClient() throws MailClientException {
        setUpForSoapEdgeClient();
        testInboundMsgHandler.handleMessage(getSampleMimeMessage());
        verifyInboundMessageHandler(2, 1, 0);
    }

    private void verifyInboundMessageHandler(int timesToProcess, int timesToSendMdn, int timesSentToInternalSmtp)
        throws MailClientException {
        //verify the smtp agent is used to process the message and process the mdn response.
        verify(mockSmtpAgent, times(timesToProcess)).processMessage(any(MimeMessage.class),
            any(NHINDAddressCollection.class), any(NHINDAddress.class));
        //verify that the external direct mail client is used to send the MDN notification emails.
        verify(mockExtMailSender, times(timesToSendMdn)).send(any(Address[].class), any(MimeMessage.class));
        //verify that the internal direct mail client is used n times to resend the message
        verify(mockIntMailSender, times(timesSentToInternalSmtp)).send(any(Address[].class), any(MimeMessage.class));
    }

    private void setUpForSmtpEdgeClient() {

        directReceiver = new DirectReceiverImpl(mockExtMailSender, DirectEventLogger.getInstance()) {
            /**
             * {@inheritDoc}
             */
            @Override
            protected DirectEdgeProxy getDirectEdgeProxy() {
                return new DirectEdgeProxySmtpImpl(mockIntMailSender);
            }

            @Override
            protected SmtpAgent getSmtpAgent(URL url) {
                return mockSmtpAgent;
            }
        };
        testInboundMsgHandler = new DirectInboundMsgHandler(directReceiver) {
        };
    }

    private void setUpForSoapEdgeClient() {
        directReceiver = new DirectReceiverImpl(mockExtMailSender, DirectEventLogger.getInstance()) {
            /**
             * {@inheritDoc}
             */
            @Override
            protected DirectEdgeProxy getDirectEdgeProxy() {
                return new DirectEdgeProxySoapImpl(new WebServiceProxyHelper());
            }

            @Override
            protected SmtpAgent getSmtpAgent(URL url) {
                return mockSmtpAgent;
            }
        };

        testInboundMsgHandler = new DirectInboundMsgHandler(directReceiver) {
        };

    }
}
