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
package gov.hhs.fha.nhinc.mail;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.junit.Test;

/**
 * Test {@link ImapMailReceiver}.
 */
public class ImapMailReceiverTest extends GreenMailTest {

    /**
     * Test {@link ImapMailReceiver#handleMessages(MessageHandler)} can receive and handle messages.
     * 
     * @throws MailClientException mail client exception
     * @throws MessagingException messaging exception
     */
    @Test
    public void canReceiveAndHandleMsgs() throws MailClientException, MessagingException {

        final ImapMailReceiver testMailReceiver = getTestMailReceiver(false);

        deliverMsgs(NUMBER_OF_MSGS);
        final MessageHandler mockHandler = getMockHandler(true);

        final int numberOfFullBatches = NUMBER_OF_MSGS / NUMBER_OF_MSGS_IN_BATCH;
        final int lastBatchCount = NUMBER_OF_MSGS % NUMBER_OF_MSGS_IN_BATCH;
        for (int i = 0; i < numberOfFullBatches; i++) {
            assertEquals("Number of handled messages matches batch size.", NUMBER_OF_MSGS_IN_BATCH,
                    testMailReceiver.handleMessages(mockHandler));
            expungeMissedMessages();
        }
        assertEquals("Last batch should handle correct count of remaining messages <= batch size.", lastBatchCount,
                testMailReceiver.handleMessages(mockHandler));
        assertEquals("Invocation count is 1 + number of full batches", numberOfFullBatches + 1,
                testMailReceiver.getHandlerInvocations());
        expungeMissedMessages();
        assertEquals("No messages should be left on the server.", 0, countRemainingMsgs());
    }

    /**
     * Test {@link ImapMailReceiver#handleMessages(MessageHandler)} will delete messages that were received but could
     * not be handled (based on configuration).
     * 
     * @throws MailClientException
     * @throws MessagingException
     */
    @Test
    public void willDeleteUnhandledMsgs() throws MailClientException, MessagingException {

        final ImapMailReceiver testMailReceiver = getTestMailReceiver(true);
        MessageHandler handler = getMockHandler(false);

        deliverMsgs(NUMBER_OF_MSGS);
        assertEquals("Expecting zero messages handled.", 0, testMailReceiver.handleMessages(handler));
        expungeMissedMessages();

        assertEquals(NUMBER_OF_MSGS_IN_BATCH + " should be removed from the server.",
                NUMBER_OF_MSGS - NUMBER_OF_MSGS_IN_BATCH, countRemainingMsgs());
    }

    /**
     * Test {@link ImapMailReceiver#handleMessages(MessageHandler)} will leave messages that were received but could not
     * be handled on the server (based on configuration).
     * 
     * @throws MailClientException
     * @throws MessagingException
     */
    @Test
    public void canLeaveUnhandledMsgsOnServer() throws MailClientException, MessagingException {

        ImapMailReceiver testMailReceiver = getTestMailReceiver(false);
        MessageHandler handler = getMockHandler(false);

        deliverMsgs(NUMBER_OF_MSGS);
        assertEquals("Expecting zero messages handled.", 0, testMailReceiver.handleMessages(handler));
        expungeMissedMessages();

        assertEquals("All messages should be left on the server.", NUMBER_OF_MSGS, countRemainingMsgs());
    }

    /**
     * Test {@link ImapMailReceiver#handleMessages(MessageHandler)} can handle zero messages.
     * 
     * @throws MailClientException
     */
    @Test
    public void canHandleNoMessages() throws MailClientException {
        ImapMailReceiver testMailReceiver = getTestMailReceiver(false);
        assertEquals("No messages on server returns zero, does not fail.", 0,
                testMailReceiver.handleMessages(getMockHandler(true)));
    }

    private void deliverMsgs(final int numberOfMsgs) {
        for (int i = 0; i < numberOfMsgs; i++) {
            deliverMsg(MESSAGE_FILEPATH);
        }
    }

    private ImapMailReceiver getTestMailReceiver(final boolean deleteUnhandledMsgs) {
        Properties testMailProps = getTestMailServerProperties(deleteUnhandledMsgs);
        return new ImapMailReceiver(testMailProps);
    }

    /**
     * @param seen flag indicates if message has been read
     * @param deleted flag indicates if message has a deleted status (to be expunged)
     * @param returnStatus indicates if the message was handled successfully.
     */
    private MessageHandler getMockHandler(final boolean returnStatus) {
        MessageHandler mockHandler = mock(MessageHandler.class);
        when(mockHandler.handleMessage(any(MimeMessage.class))).thenReturn(returnStatus);
        return mockHandler;
    }

}
