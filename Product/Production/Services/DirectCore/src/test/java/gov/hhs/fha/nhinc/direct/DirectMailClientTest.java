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

import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.getFileAsString;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.getMockDocument;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.getRecipients;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.getSender;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.RECIP_AT_RESPONDING_GW;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.SENDER_AT_INITIATING_GW;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.nhindirect.gateway.smtp.MessageProcessResult;
import org.nhindirect.gateway.smtp.SmtpAgent;
import org.nhindirect.gateway.smtp.SmtpAgentFactory;
import org.nhindirect.stagent.MessageEnvelope;
import org.nhindirect.stagent.NHINDAddress;
import org.nhindirect.stagent.NHINDAddressCollection;
import org.nhindirect.stagent.mail.Message;
import org.nhindirect.stagent.mail.notifications.NotificationMessage;
import com.icegreen.greenmail.user.UserException;

/**
 * Test {@link DirectMailClient}.
 */
public class DirectMailClientTest extends AbstractDirectMailClientTestBase {
	
    /**
     * Ensure that we can read all mail server properties as strings.
     */
    @Test
    public void canReadPropsAsStrings() {
        assertNotNull(intMailServerProps.getProperty("direct.mail.user"));
        assertNotNull(intMailServerProps.getProperty("direct.mail.pass"));
        assertNotNull(intMailServerProps.getProperty("direct.max.msgs.in.batch"));
        assertNotNull(intMailServerProps.getProperty("mail.smtps.host"));
        assertNotNull(intMailServerProps.getProperty("mail.smtps.auth"));
        assertNotNull(intMailServerProps.getProperty("mail.smtps.port"));
        assertNotNull(intMailServerProps.getProperty("mail.smtps.starttls.enabled"));
        assertNotNull(intMailServerProps.getProperty("mail.imaps.host"));
        assertNotNull(intMailServerProps.getProperty("mail.imaps.port"));
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
        when(mockSmtpAgent.processMessage(any(MimeMessage.class), any(NHINDAddressCollection.class),
                        any(NHINDAddress.class))).thenReturn(mockMessageProcessResult);

        for (int i = 0; i < NUM_MSGS_ONE_BATCH; i++) {
            testDirectMailClient.processAndSend(getSender(), getRecipients(), getMockDocument(), ATTACHMENT_NAME);
        }

        verify(mockSmtpAgent, times(NUM_MSGS_ONE_BATCH)).processMessage(any(MimeMessage.class),
                any(NHINDAddressCollection.class), any(NHINDAddress.class));

        testDirectMailClient.handleMessages();

        verify(mockMessageHandler, times(NUM_MSGS_ONE_BATCH)).handleMessage(any(Message.class), 
                eq(testDirectMailClient));
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
        when(mockSmtpAgent.processMessage(any(MimeMessage.class), any(NHINDAddressCollection.class),
                        any(NHINDAddress.class))).thenReturn(mockMessageProcessResult);

        // blast out all of the messages at once...
        for (int i = 0; i < NUM_MSGS_MULTI_BATCH; i++) {
            testDirectMailClient.processAndSend(getSender(), getRecipients(), getMockDocument(), ATTACHMENT_NAME);
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
            numberOfMsgsHandled += testDirectMailClient.handleMessages();
            verify(mockMessageHandler, times(numberOfMsgsHandled)).handleMessage(any(Message.class), 
                    eq(testDirectMailClient));

            // there is a greenmail bug that only expunges every other message... delete read messages
            DirectUnitTestUtil.expungeMissedMessages(greenMail, intUser);
        }

        assertEquals("Expected number of batches that ran", expectedBatchCount, batchCount);
        assertEquals("All messages were handled", NUM_MSGS_MULTI_BATCH, numberOfMsgsHandled);
        assertEquals("No messages should be left on the server", 0, greenMail.getReceivedMessages().length);
    }

    @Test
    public void canSendSingleMDNMessage() throws MessagingException {
        MessageProcessResult mockMessageProcessResult = getMockMessageProcessResult();
        testDirectMailClient.sendMdn(mockMessageProcessResult);
        assertEquals(1, greenMail.getReceivedMessages().length);
    }

    @Test
    public void canSendMultipleMDNMessages() throws MessagingException {
        int numMdnMessages = 5;
        MessageProcessResult mockMessageProcessResult = getMockMessageProcessResult(numMdnMessages);
        testDirectMailClient.sendMdn(mockMessageProcessResult);
        assertEquals(numMdnMessages, greenMail.getReceivedMessages().length);
    }

    /**
     * This test is intended to simulate the end-to-end send, receive and MDN of a direct mail send use case, with SMTP edge
     * clients on the sending and receiving side. Currently it does not run because Greenmail does not support the 
     * envelope fetching part of the IMAP spec.
     * @throws UserException when the test fails with a user exception.
     * @throws MessagingException when the test fails with a MessagingException.
     */
    @Test
    public void canEndToEndWithSmtpEdgeClients() throws UserException, MessagingException {        

        final MimeMessage originalMsg = new MimeMessage(null,
                IOUtils.toInputStream(getFileAsString("PlainOutgoingMessage.txt")));

        assertNotNull(originalMsg);
        
        // place message on internal server (simulates the user sending a message)
        intUser.deliver(originalMsg);
        assertEquals(1, greenMail.getReceivedMessages().length);
        
        /*
         * Initiating Gateway...
         */
        // handle the messages on the internal server
        SmtpAgent smtpAgent = SmtpAgentFactory.createAgent(getClass().getClassLoader().getResource(
                "smtp.agent.config.xml"));

        DirectMailClient internalDirectClient = new DirectMailClient(intMailServerProps, smtpAgent);        
        DirectMailClient externalDirectClient = new DirectMailClient(intMailServerProps, smtpAgent);
        
        // create a real outbound Message Handler
        OutboundMessageHandler outboundMessageHandler = new OutboundMessageHandler();
        outboundMessageHandler.setExternalDirectClient(externalDirectClient);                
        internalDirectClient.setMessageHandler(outboundMessageHandler);
        
        // we can use the same greenmail as external direct client
        internalDirectClient.handleMessages();
        DirectUnitTestUtil.expungeMissedMessages(greenMail, intUser);
        
        // verify that the encrypted message is available on the external mail client
        MimeMessage[] messages = greenMail.getReceivedMessages();
        assertEquals(1, messages.length);
        assertEquals("application/pkcs7-mime; smime-type=enveloped-data; name=\"smime.p7m\"",
                messages[0].getContentType());
        assertEquals(RECIP_AT_RESPONDING_GW, messages[0].getAllRecipients()[0].toString());
        
        for (MimeMessage message : messages) {
            System.out.println(message.getAllRecipients()[0] + message.getContentType());
        }
        
        /*
         * Responding Gateway...
         */
        // create a real outbound Message Handler
        InboundMessageHandler inboundMessageHandler = new InboundMessageHandler();
        inboundMessageHandler.setEdgeClientType(InboundMessageHandler.EDGE_CLIENT_TYPE_SMTP);
        inboundMessageHandler.setInternalDirectClient(internalDirectClient);
        
        // we can use the same greenmail as external direct client
        externalDirectClient.setMessageHandler(inboundMessageHandler);
        externalDirectClient.handleMessages();
        DirectUnitTestUtil.expungeMissedMessages(greenMail, intUser);
        
        // verify that the decrypted message is available on the mail client
        // verify that the MDN was also put on the mail client.
        messages = greenMail.getReceivedMessages();
        for (MimeMessage message : messages) {
            System.out.println(message.getAllRecipients()[0] + message.getContentType());
        }

        // there are 2 MDNs right now... not sure why...
        assertEquals(3, messages.length);        
        for (MimeMessage message : messages) {
            
            String sender = message.getFrom()[0].toString();
            switch (sender) {
            case SENDER_AT_INITIATING_GW:
                assertEquals("Multipart/Mixed; boundary=\"NextPart\"", message.getContentType());
                assertEquals(RECIP_AT_RESPONDING_GW, message.getAllRecipients()[0].toString());
                break;
            case RECIP_AT_RESPONDING_GW:
                assertEquals("application/pkcs7-mime; smime-type=enveloped-data; name=\"smime.p7m\"",
                        message.getContentType());
                assertEquals(SENDER_AT_INITIATING_GW, message.getAllRecipients()[0].toString());
                break;                
            default:
                fail("Sender: " + sender + " is not expected.");
            }
            
            System.out.println(message.getAllRecipients()[0] + message.getContentType());
        }
//        assertEquals("", messages[0].getContentType());
        
        /*
         * Initiating Gateway collects an MDN 
         */
        intUser = greenMail.setUser(SENDER_AT_INITIATING_GW, DirectUnitTestUtil.USER,
                DirectUnitTestUtil.PASS);
        externalDirectClient.handleMessages();
        DirectUnitTestUtil.expungeMissedMessages(greenMail, intUser);

    }
    
    
    private MessageProcessResult getMockMessageProcessResult() throws MessagingException {
        return getMockMessageProcessResult(1);
    }

    private MessageProcessResult getMockMessageProcessResult(int numNotificationMessages) throws MessagingException {

        MessageProcessResult mockMessageProcessResult = mock(MessageProcessResult.class);
        MessageEnvelope mockMessageEnvelope = mock(MessageEnvelope.class);
        Message mockMessage = mock(Message.class);

        Collection<NotificationMessage> notificationCollection = new ArrayList<NotificationMessage>();
        NotificationMessage mockNotificationMessage = mock(NotificationMessage.class);
        Address senderAddress = new InternetAddress(SENDER_AT_INITIATING_GW);
        Address recipAddress = new InternetAddress(RECIP_AT_RESPONDING_GW);

        for (int i = 0; i < numNotificationMessages; i++) {
            notificationCollection.add(mockNotificationMessage);
        }

        when(mockMessageProcessResult.getProcessedMessage()).thenReturn(mockMessageEnvelope);
        when(mockMessageEnvelope.getMessage()).thenReturn(mockMessage);
        when(mockMessageProcessResult.getNotificationMessages()).thenReturn(notificationCollection);
        when(mockNotificationMessage.getRecipients(any(RecipientType.class))).thenReturn(new Address[] {recipAddress});
        when(mockNotificationMessage.getAllRecipients()).thenReturn(new Address[] {recipAddress});
        when(mockNotificationMessage.getFrom()).thenReturn(new Address[] {senderAddress});
        when(mockSmtpAgent.processMessage(any(MimeMessage.class), any(NHINDAddressCollection.class),
                any(NHINDAddress.class))).thenReturn(mockMessageProcessResult);

        return mockMessageProcessResult;
    }

}
