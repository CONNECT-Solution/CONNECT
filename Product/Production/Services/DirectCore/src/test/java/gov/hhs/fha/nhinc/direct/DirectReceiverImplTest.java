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

import gov.hhs.fha.nhinc.direct.edge.proxy.DirectEdgeProxy;
import gov.hhs.fha.nhinc.direct.event.DirectEventLogger;
import gov.hhs.fha.nhinc.direct.event.DirectEventType;
import gov.hhs.fha.nhinc.mail.MailSender;
import java.net.URL;
import java.util.Collections;
import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import static org.junit.Assert.fail;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.nhindirect.gateway.smtp.MessageProcessResult;
import org.nhindirect.gateway.smtp.ReliableDispatchedNotificationProducer;
import org.nhindirect.gateway.smtp.SmtpAgent;
import org.nhindirect.stagent.IncomingMessage;
import org.nhindirect.stagent.MessageEnvelope;
import org.nhindirect.stagent.NHINDAddress;
import org.nhindirect.stagent.NHINDAddressCollection;
import org.nhindirect.stagent.mail.Message;
import org.nhindirect.stagent.mail.notifications.Disposition;
import org.nhindirect.stagent.mail.notifications.Notification;
import org.nhindirect.stagent.mail.notifications.NotificationMessage;
import org.nhindirect.stagent.mail.notifications.NotificationType;

/**
 * The Class DirectReceiverImplTest.
 *
 * @author msw
 */
public class DirectReceiverImplTest {

    /**
     * The external mail sender.
     */
    private final MailSender externalMailSender = mock(MailSender.class);
    /**
     * The direct event logger.
     */
    private final DirectEventLogger directEventLogger = mock(DirectEventLogger.class);
    /**
     * The stmp agent.
     */
    private final SmtpAgent smtpAgent = mock(SmtpAgent.class);
    /**
     * The producer.
     */
    private final ReliableDispatchedNotificationProducer producer = mock(ReliableDispatchedNotificationProducer.class);
    /**
     * The result.
     */
    private final MessageProcessResult result = mock(MessageProcessResult.class);
    /**
     * The envelope.
     */
    private final MessageEnvelope envelope = mock(MessageEnvelope.class);
    /**
     * The proxy.
     */
    private final DirectEdgeProxy proxy = mock(DirectEdgeProxy.class);

    private final DirectAdapter directAdapter = mock(DirectAdapter.class);

    /**
     * Test message with no dispatched MDN requested.
     *
     * @throws MessagingException the messaging exception
     */
    @Test
    public void testNoDispatchRequest() throws MessagingException {
        Message message = getMessage();

        setCommonExpectations(message);
        runTestOnMessage(message, false);

        verify(directEventLogger).log(eq(DirectEventType.BEGIN_INBOUND_DIRECT), any(MimeMessage.class));
        verify(directEventLogger).log(eq(DirectEventType.BEGIN_OUTBOUND_MDN_PROCESSED), any(MimeMessage.class));
        verify(directEventLogger).log(eq(DirectEventType.END_OUTBOUND_MDN_PROCESSED), any(MimeMessage.class));
        verify(directEventLogger).log(eq(DirectEventType.END_INBOUND_DIRECT), any(MimeMessage.class));
        verify(smtpAgent, times(2)).processMessage(any(Message.class), any(NHINDAddressCollection.class),
            any(NHINDAddress.class));
    }

    /**
     * Test message with dispatch MDN requested.
     *
     * @throws MessagingException the messaging exception
     */
    @Test
    public void testDispatchRequest() throws MessagingException {
        Message message = getMessage();
        addDispatchRequest(message);

        setCommonExpectations(message);
        when(producer.produce(any(IncomingMessage.class))).thenReturn(
            Collections.singleton(new NotificationMessage("to", "from", new Notification(new Disposition(
                            NotificationType.Dispatched)))));
        runTestOnMessage(message, false);

        verify(directEventLogger).log(eq(DirectEventType.BEGIN_INBOUND_DIRECT), any(MimeMessage.class));
        verify(directEventLogger, times(1)).log(eq(DirectEventType.BEGIN_OUTBOUND_MDN_PROCESSED), any(MimeMessage.class));
        verify(directEventLogger, times(1)).log(eq(DirectEventType.END_OUTBOUND_MDN_PROCESSED), any(MimeMessage.class));
        verify(directEventLogger, times(1)).log(eq(DirectEventType.BEGIN_OUTBOUND_MDN_DISPATCHED), any(MimeMessage.class));
        verify(directEventLogger, times(1)).log(eq(DirectEventType.END_OUTBOUND_MDN_DISPATCHED), any(MimeMessage.class));
        verify(directEventLogger).log(eq(DirectEventType.END_INBOUND_DIRECT), any(MimeMessage.class));
        verify(smtpAgent, times(3)).processMessage(any(Message.class), any(NHINDAddressCollection.class),
            any(NHINDAddress.class));
    }

    /**
     * Test message with dispatch MDN requested.
     *
     * @throws MessagingException the messaging exception
     */
    @Test
    public void testNotificationTOEdgeFailed() throws MessagingException {
        Message message = getMessage();
        addDispatchRequest(message);
        setCommonExpectations(message);

        when(producer.produce(any(IncomingMessage.class))).thenReturn(
            Collections.singleton(new NotificationMessage("to", "from", new Notification(new Disposition(
                            NotificationType.Dispatched)))));

        runTestOnMessage(message, true);

        setCommonExpectations(message);
        verify(directEventLogger).log(eq(DirectEventType.BEGIN_INBOUND_DIRECT), any(MimeMessage.class));
        verify(directEventLogger).log(eq(DirectEventType.BEGIN_OUTBOUND_MDN_PROCESSED), any(MimeMessage.class));
        verify(directEventLogger).log(eq(DirectEventType.END_OUTBOUND_MDN_PROCESSED), any(MimeMessage.class));
        verify(directEventLogger).log(eq(DirectEventType.END_INBOUND_DIRECT), any(MimeMessage.class));
        verify(smtpAgent, times(2)).processMessage(any(Message.class), any(NHINDAddressCollection.class),
            any(NHINDAddress.class));
        verify(directEventLogger).log(eq(DirectEventType.DIRECT_ERROR), any(MimeMessage.class), any(String.class));

    }

    /**
     * Sets the common expectations.
     *
     * @param message the new common expectations
     * @throws MessagingException the messaging exception
     */
    private void setCommonExpectations(Message message) throws MessagingException {
        when(smtpAgent.processMessage(eq(message), any(NHINDAddressCollection.class), any(NHINDAddress.class)))
            .thenReturn(result);
        when(smtpAgent.processMessage(any(Message.class), any(NHINDAddressCollection.class), any(NHINDAddress.class)))
            .thenReturn(result);
        when(result.getProcessedMessage()).thenReturn(envelope);
        when(envelope.getMessage()).thenReturn(message);
        when(result.getNotificationMessages()).thenReturn(
            Collections.singleton(new NotificationMessage("to", "from", new Notification(new Disposition(
                            NotificationType.Processed)))));
    }

    /**
     * Adds the dispatch request.
     *
     * @param message the messag
     * @throws MessagingException the messaging exception
     */
    private void addDispatchRequest(MimeMessage message) throws MessagingException {
        String headerValue = DirectReceiverImpl.X_DIRECT_FINAL_DESTINATION_DELIVERY_HEADER_VALUE + "=optional, true";
        message.addHeader(DirectReceiverImpl.DISPOSITION_NOTIFICATION_OPTIONS_HEADER_NAME, headerValue);
    }

    /**
     * Run test on message.
     *
     * @param message the message
     */
    private void runTestOnMessage(MimeMessage message, boolean mockErrorCondition) {
        DirectReceiverImpl impl = getDirectReceiverImpl();
        if (impl != null) {
            if (mockErrorCondition) {
                DirectException directException = mock(DirectException.class);
                when(proxy.provideAndRegisterDocumentSetB(any(MimeMessage.class))).thenThrow(directException);
            }
            impl.receiveInbound(message);
        } else {
            fail("Class under test: DirectReceiverImpl was null.");
        }
    }

    /**
     * Gets the message.
     *
     * @return the message
     * @throws MessagingException the messaging exception
     */
    private Message getMessage() throws MessagingException {
        MimeMessage message = new MimeMessage((Session) null);
        Address from = new InternetAddress("test@directri.connectopensource.org");
        message.setFrom(from);
        Address to = new InternetAddress("test@direct.connectopensource.org");
        message.addRecipient(RecipientType.TO, to);
        message.setSubject("this is a test message");
        message.setText("this is a test message body");
        return new Message(message);
    }

    /**
     * Gets the direct receiver impl.
     *
     * @return the direct receiver impl
     */
    private DirectReceiverImpl getDirectReceiverImpl() {
        return new DirectReceiverImpl(externalMailSender, directEventLogger, producer) {
            /*
             * (non-Javadoc)
             *
             * @see gov.hhs.fha.nhinc.direct.DirectAdapter#getDirectEdgeProxy()
             */
            @Override
            protected DirectEdgeProxy getDirectEdgeProxy() {
                return proxy;
            }

            @Override
            protected SmtpAgent getSmtpAgent(URL url) {
                return smtpAgent;
            }
        };
    }
}
