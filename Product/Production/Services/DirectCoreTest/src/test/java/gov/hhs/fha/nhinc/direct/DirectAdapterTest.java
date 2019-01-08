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

import com.google.common.collect.ImmutableList;
import com.icegreen.greenmail.user.UserException;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.getMockDirectDocuments;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.getRecipients;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.getSender;
import gov.hhs.fha.nhinc.direct.edge.proxy.DirectEdgeProxySmtpImpl;
import gov.hhs.fha.nhinc.direct.event.DirectEventType;
import gov.hhs.fha.nhinc.event.Event;
import gov.hhs.fha.nhinc.event.EventLogger;
import gov.hhs.fha.nhinc.event.EventLoggerFactory;
import gov.hhs.fha.nhinc.event.EventManager;
import gov.hhs.fha.nhinc.event.Log4jEventLogger;
import gov.hhs.fha.nhinc.mail.MailClientException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Test;
import static org.mockito.Matchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.nhindirect.gateway.smtp.MessageProcessResult;
import org.nhindirect.stagent.NHINDAddress;
import org.nhindirect.stagent.NHINDAddressCollection;
import org.nhindirect.stagent.mail.Message;

/**
 * Test {@link DirectMailClient}.
 */
public class DirectAdapterTest extends AbstractDirectMailClientTest {

    private static int eventIndex = 0;

    /**
     * Ensure that we can read all mail server properties as strings.
     */
    @Test
    public void canReadPropsAsStrings() {
        assertNotNull(recipMailServerProps.getProperty("connect.mail.user"));
        assertNotNull(recipMailServerProps.getProperty("connect.mail.pass"));
        assertNotNull(recipMailServerProps.getProperty("connect.max.msgs.in.batch"));
        assertNotNull(recipMailServerProps.getProperty("connect.delete.unhandled.msgs"));
        assertNotNull(recipMailServerProps.getProperty("mail.smtp.host"));
        assertNotNull(recipMailServerProps.getProperty("mail.smtp.auth"));
        assertNotNull(recipMailServerProps.getProperty("mail.smtp.port"));
        assertNotNull(recipMailServerProps.getProperty("mail.smtp.starttls.enabled"));
        assertNotNull(recipMailServerProps.getProperty("mail.imaps.host"));
        assertNotNull(recipMailServerProps.getProperty("mail.imaps.port"));
    }

    /**
     * Test {@link DirectMailClient#handleMessages(MessageHandler)} Verify that we can send and receive messages on the
     * direct mail client with one batch when the message count is less than the max batch size.
     *
     * @throws IOException on io error.
     * @throws MessagingException on failure.
     * @throws MailClientException
     */
    @Test
    public void canSendAndReceiveInOneBatch() throws IOException, MessagingException, MailClientException {

        whenProcessingMessageReturnMockResult();
        processAndSendMultipleMsgs(NUM_MSGS_ONE_BATCH);

        verify(mockSmtpAgent, times(NUM_MSGS_ONE_BATCH)).processMessage(any(MimeMessage.class),
            any(NHINDAddressCollection.class), any(NHINDAddress.class));

        intMailReceiver.handleMessages(mockMessageHandler);

        verify(mockMessageHandler, times(NUM_MSGS_ONE_BATCH)).handleMessage(any(Message.class));
    }

    /**
     * Test {@link DirectMailClient#handleMessages(MessageHandler)} Verify that we can send an receive messages on the
     * direct mail client with one batch when the message count is less than the max batch size.
     *
     * @throws IOException on io error.
     * @throws MessagingException on failure.
     * @throws MailClientException
     */
    @Test
    public void canSendAndReceiveMultipleMsgsInBatches() throws IOException, MessagingException, MailClientException {

        whenProcessingMessageReturnMockResult();
        processAndSendMultipleMsgs(NUM_MSGS_MULTI_BATCH);

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
            numberOfMsgsHandled += intMailReceiver.handleMessages(mockMessageHandler);
            verify(mockMessageHandler, times(numberOfMsgsHandled)).handleMessage(any(Message.class));

            // there is a greenmail bug that only expunges every other message... delete read messages
            DirectUnitTestUtil.expungeMissedMessages(greenMail, recipUser);
        }

        assertEquals("Expected number of batches that ran", expectedBatchCount, batchCount);
        assertEquals("All messages were handled", NUM_MSGS_MULTI_BATCH, numberOfMsgsHandled);
        assertEquals("No messages should be left on the server", 0, greenMail.getReceivedMessages().length);
    }

    private void whenProcessingMessageReturnMockResult() throws MessagingException {
        MessageProcessResult messageProcessResult = getMockMessageProcessResult();
        when(mockSmtpAgent.processMessage(any(MimeMessage.class), any(NHINDAddressCollection.class),
            any(NHINDAddress.class))).thenReturn(messageProcessResult);
    }

    private void processAndSendMultipleMsgs(int numberOfMsgs) {
        for (int i = 0; i < numberOfMsgs; i++) {
            testDirectSender.sendOutboundDirect(getSender(), getRecipients(), getMockDirectDocuments(),
                ATTACHMENT_NAME);
        }
        try {
            greenMail.waitForIncomingEmail(5000, numberOfMsgs);
        } catch (InterruptedException e) {
            fail("Interrupted while waiting for inbound messages." + e.getMessage());
        }
    }

    /**
     * This test is intended to simulate the end-to-end send, receive and MDN of a direct mail send use case, with SMTP
     * edge clients on the sending and receiving side.
     *
     * @throws UserException when the test fails with a user exception.
     * @throws MessagingException when the test fails with a MessagingException.
     * @throws MailClientException
     * @throws InterruptedException
     */
    private void canEndToEndWithSmtpEdgeClients(int expectedNumberOfMessages) throws UserException, MessagingException, MailClientException {

        deliverMessage("PlainOutgoingMessage.txt");
        verifySmtpEdgeMessage();

        /* Initiating Gateway */
        DirectEdgeProxySmtpImpl initiatingSmtp = new DirectEdgeProxySmtpImpl(intMailSender);
        setUpDirectClients(recipMailServerProps, initiatingSmtp);

        handleMessages(intMailReceiver, outboundMsgHandler, 1, recipUser);
        verifyOutboundMessageSent();

        /* Responding Gateway */
        handleMessages(extMailReceiver, inboundMsgHandler, 1, recipUser);
        verifySmtpEdgeMessage();
        verifyOutboundMdn();

        /* Initiating Gateway collects an MDN */
        DirectEdgeProxySmtpImpl respondingSmtp = new DirectEdgeProxySmtpImpl(intMailSender);
        setUpDirectClients(senderMailServerProps, respondingSmtp);

        handleMessages(extMailReceiver, inboundMsgHandler, 2, senderUser);
        verifyInboundMdn(expectedNumberOfMessages);
    }

    /**
     * Run the end to end test and also verify that the events are logged in the correct order.
     *
     * @throws UserException on failure.
     * @throws MessagingException on failure.
     * @throws MailClientException
     */
    @Test
    public void canLogEventsDuringEndToEnd() throws UserException, MessagingException, MailClientException {
        //should not receive any mdn in the edge client by default
        //set the system property
        System.setProperty(DirectReceiverImpl.SUPPRESS_MDN_EDGE_NOTIFICATION, "true");
        eventIndex = 0;
        canLogEventsDuringEndToEnd(0);
    }

    /**
     * Run the end to end test and also verify that the events are logged in the correct order.
     *
     * @throws UserException on failure.
     * @throws MessagingException on failure.
     * @throws MailClientException
     */
    @Test
    public void canLogEventsDuringEndToEndWithSuppressNotificationDisabled() throws UserException, MessagingException, MailClientException {
        //test with suppressmdnedgenotification false. Should receive a mdn from the sending end.
        //set the system property
        System.setProperty(DirectReceiverImpl.SUPPRESS_MDN_EDGE_NOTIFICATION, "false");
        eventIndex = 0;
        // ...there are 2 MDNs right now because of a quirk in greenmail.
        canLogEventsDuringEndToEnd(2);
    }

    /**
     * Run the end to end test and also verify that the events are logged in the correct order.
     *
     * @throws UserException on failure.
     * @throws MessagingException on failure.
     * @throws MailClientException
     */
    public void canLogEventsDuringEndToEnd(int expectedNumberOfMessages) throws UserException, MessagingException, MailClientException {

        final EventLoggerFactory loggerFactory = new EventLoggerFactory(EventManager.getInstance());
        final EventLogger mockEventLogger = mock(EventLogger.class);
        final List<Event> triggeredEvents = new ArrayList<Event>();

        // mock up the first callback to serviceB:
        doAnswer(new Answer<Void>() {

            public Void answer(InvocationOnMock invocation) throws Throwable {

                Event event = (Event) invocation.getArguments()[1];
                triggeredEvents.add(event);

                return null;
            }
        }).when(mockEventLogger).update(Mockito.isA(EventManager.class), Mockito.isA(Event.class));

        loggerFactory.setLoggers(ImmutableList.of(mockEventLogger, new Log4jEventLogger()));
        loggerFactory.registerLoggers();

        canEndToEndWithSmtpEdgeClients(expectedNumberOfMessages);

        // verify that the events were fired in order.
        assertTriggered(DirectEventType.BEGIN_OUTBOUND_DIRECT, triggeredEvents);
        assertTriggered(DirectEventType.END_OUTBOUND_DIRECT, triggeredEvents);

        assertTriggered(DirectEventType.BEGIN_INBOUND_DIRECT, triggeredEvents);
        assertTriggered(DirectEventType.BEGIN_OUTBOUND_MDN, triggeredEvents);
        assertTriggered(DirectEventType.END_OUTBOUND_MDN, triggeredEvents);

        // extra MDN generated (Greenmail quirk)
        assertTriggered(DirectEventType.BEGIN_OUTBOUND_MDN, triggeredEvents);
        assertTriggered(DirectEventType.END_OUTBOUND_MDN, triggeredEvents);

        assertTriggered(DirectEventType.END_INBOUND_DIRECT, triggeredEvents);

        assertTriggered(DirectEventType.BEGIN_INBOUND_MDN, triggeredEvents);
        assertTriggered(DirectEventType.END_INBOUND_MDN, triggeredEvents);

        // extra MDN generated (Greenmail quirk)
        assertTriggered(DirectEventType.BEGIN_INBOUND_MDN, triggeredEvents);
        assertTriggered(DirectEventType.END_INBOUND_MDN, triggeredEvents);
    }

    private void assertTriggered(DirectEventType eventType, List<Event> events) {
        Event event = events.get(eventIndex);
        assertEquals("Event at index: " + eventIndex + " --> " + event.getDescription(), eventType.toString(),
            event.getEventName());
        eventIndex++;
    }

    @SuppressWarnings("unchecked")
    @Test(expected = DirectException.class)
    public void testExceptionHandlingMessagingException() throws MessagingException {
        DirectAdapter instance = new DirectAdapter(extMailSender, mockSmtpAgent, null) {
            // this abstract class has no abstract methods so...
        };

        MimeMessage mockMessage = mock(MimeMessage.class);
        when(mockMessage.getRecipients(any(RecipientType.class))).thenReturn(getMockAddresses());
        when(mockMessage.getFrom()).thenReturn(getMockAddresses());
        when(mockSmtpAgent.processMessage(any(MimeMessage.class), any(NHINDAddressCollection.class),
            any(NHINDAddress.class))).thenThrow(MessagingException.class);
        instance.process(mockMessage);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = DirectException.class)
    public void testExceptionHandlingException() throws MessagingException {
        DirectAdapter instance = new DirectAdapter(extMailSender, mockSmtpAgent, null) {
            // this abstract class has no abstract methods so...
        };

        MimeMessage mockMessage = mock(MimeMessage.class);
        when(mockMessage.getRecipients(any(RecipientType.class))).thenReturn(getMockAddresses());
        when(mockMessage.getFrom()).thenReturn(getMockAddresses());
        when(mockSmtpAgent.processMessage(any(MimeMessage.class), any(NHINDAddressCollection.class),
            any(NHINDAddress.class))).thenThrow(Exception.class);
        instance.process(mockMessage);
    }

    private Address[] getMockAddresses() throws AddressException {
        Address[] addresses = new Address[1];
        addresses[0] = new InternetAddress("test@direct.example.com");
        return addresses;
    }
}
