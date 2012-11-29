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

import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.getMockDirectDocuments;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.getRecipients;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.getSender;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.direct.edge.proxy.DirectEdgeProxySmtpImpl;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.Test;
import org.nhindirect.gateway.smtp.MessageProcessResult;
import org.nhindirect.stagent.NHINDAddress;
import org.nhindirect.stagent.NHINDAddressCollection;
import org.nhindirect.stagent.mail.Message;

import com.icegreen.greenmail.user.UserException;

/**
 * Test {@link DirectMailClient}.
 */
public class DirectMailClientTest extends AbstractDirectMailClientTest {

    /**
     * Ensure that we can read all mail server properties as strings.
     */
    @Test
    public void canReadPropsAsStrings() {
        assertNotNull(recipMailServerProps.getProperty("direct.mail.user"));
        assertNotNull(recipMailServerProps.getProperty("direct.mail.pass"));
        assertNotNull(recipMailServerProps.getProperty("direct.max.msgs.in.batch"));
        assertNotNull(recipMailServerProps.getProperty("mail.smtps.host"));
        assertNotNull(recipMailServerProps.getProperty("mail.smtps.auth"));
        assertNotNull(recipMailServerProps.getProperty("mail.smtps.port"));
        assertNotNull(recipMailServerProps.getProperty("mail.smtps.starttls.enabled"));
        assertNotNull(recipMailServerProps.getProperty("mail.imaps.host"));
        assertNotNull(recipMailServerProps.getProperty("mail.imaps.port"));
    }

    /**
     * Test {@link DirectMailClient#handleMessages(MessageHandler)} Verify that we can send an receive messages on the
     * direct mail client with one batch when the message count is less than the max batch size.
     * 
     * @throws IOException on io error.
     * @throws MessagingException
     */
    @Test
    public void canSendAndReceiveInOneBatch() throws IOException, MessagingException {
        MessageProcessResult mockMessageProcessResult = getMockMessageProcessResult();
        when(
                mockSmtpAgent.processMessage(any(MimeMessage.class), any(NHINDAddressCollection.class),
                        any(NHINDAddress.class))).thenReturn(mockMessageProcessResult);

        for (int i = 0; i < NUM_MSGS_ONE_BATCH; i++) {
            intDirectClient.processAndSend(getSender(), getRecipients(), getMockDirectDocuments(), ATTACHMENT_NAME);
        }

        verify(mockSmtpAgent, times(NUM_MSGS_ONE_BATCH)).processMessage(any(MimeMessage.class),
                any(NHINDAddressCollection.class), any(NHINDAddress.class));

        intDirectClient.handleMessages();

        verify(mockMessageHandler, times(NUM_MSGS_ONE_BATCH)).handleMessage(any(Message.class), eq(intDirectClient));
    }

    /**
     * Test {@link DirectMailClient#handleMessages(MessageHandler)} Verify that we can send an receive messages on the
     * direct mail client with one batch when the message count is less than the max batch size.
     * 
     * @throws IOException on io error.
     * @throws MessagingException
     */
    @Test
    public void canSendAndReceiveMultipleMsgsInBatches() throws IOException, MessagingException {
        MessageProcessResult mockMessageProcessResult = getMockMessageProcessResult();
        when(
                mockSmtpAgent.processMessage(any(MimeMessage.class), any(NHINDAddressCollection.class),
                        any(NHINDAddress.class))).thenReturn(mockMessageProcessResult);

        // blast out all of the messages at once...
        for (int i = 0; i < NUM_MSGS_MULTI_BATCH; i++) {
            intDirectClient.processAndSend(getSender(), getRecipients(), getMockDirectDocuments(), ATTACHMENT_NAME);
        }

        verify(mockSmtpAgent, times(NUM_MSGS_MULTI_BATCH)).processMessage(any(MimeMessage.class),
                any(NHINDAddressCollection.class), any(NHINDAddress.class));

        int expectedBatchCount = NUM_MSGS_MULTI_BATCH / DirectUnitTestUtil.MAX_NUM_MSGS_IN_BATCH;
        if (NUM_MSGS_MULTI_BATCH % DirectUnitTestUtil.MAX_NUM_MSGS_IN_BATCH > 0) {
            expectedBatchCount++;
        }

        int numberOfMsgsHandled = 0;
        int batchCount = 0;
        while (numberOfMsgsHandled < NUM_MSGS_MULTI_BATCH) {
            batchCount++;
            numberOfMsgsHandled += intDirectClient.handleMessages();
            verify(mockMessageHandler, times(numberOfMsgsHandled)).handleMessage(any(Message.class),
                    eq(intDirectClient));

            // there is a greenmail bug that only expunges every other message... delete read messages
            DirectUnitTestUtil.expungeMissedMessages(greenMail, recipUser);
        }

        assertEquals("Expected number of batches that ran", expectedBatchCount, batchCount);
        assertEquals("All messages were handled", NUM_MSGS_MULTI_BATCH, numberOfMsgsHandled);
        assertEquals("No messages should be left on the server", 0, greenMail.getReceivedMessages().length);
    }

    @Test
    public void canSendSingleMDNMessage() throws MessagingException {
        MessageProcessResult mockMessageProcessResult = getMockMessageProcessResult();
        intDirectClient.sendMdn(mockMessageProcessResult);
        assertEquals(1, greenMail.getReceivedMessages().length);
    }

    @Test
    public void canSendMultipleMDNMessages() throws MessagingException {
        int numMdnMessages = 5;
        MessageProcessResult mockMessageProcessResult = getMockMessageProcessResult(numMdnMessages);
        intDirectClient.sendMdn(mockMessageProcessResult);
        assertEquals(numMdnMessages, greenMail.getReceivedMessages().length);
    }

    /**
     * This test is intended to simulate the end-to-end send, receive and MDN of a direct mail send use case, with SMTP
     * edge clients on the sending and receiving side.
     * 
     * @throws UserException when the test fails with a user exception.
     * @throws MessagingException when the test fails with a MessagingException.
     */
    @Test
    public void canEndToEndWithSmtpEdgeClients() throws UserException, MessagingException {

        deliverMessage("PlainOutgoingMessage.txt");
        verifySmtpEdgeMessage();

        /* Initiating Gateway */
        DirectEdgeProxySmtpImpl initiatingSmtp = new DirectEdgeProxySmtpImpl();
        initiatingSmtp.setInternalDirectClient(getInternalDirectClient(recipMailServerProps));
        setUpDirectClients(recipMailServerProps, initiatingSmtp);

        handleMessages(intDirectClient, 1, recipUser);
        verifyOutboundMessageSent();

        /* Responding Gateway */
        handleMessages(extDirectClient, 1, recipUser);
        verifySmtpEdgeMessage();
        verifyOutboundMdn();

        /* Initiating Gateway collects an MDN */
        DirectEdgeProxySmtpImpl respondingSmtp = new DirectEdgeProxySmtpImpl();
        respondingSmtp.setInternalDirectClient(getInternalDirectClient(senderMailServerProps));
        setUpDirectClients(senderMailServerProps, respondingSmtp);

        handleMessages(extDirectClient, 2, senderUser);
        verifyInboundMdn();
    }

}
