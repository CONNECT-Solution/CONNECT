/*
 * Copyright (c) 2009-2013, United States Government, as represented by the Secretary of Health and Human Services.
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
import java.util.Arrays;
import java.util.Collection;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.nhindirect.common.mail.MDNStandard;
import org.nhindirect.gateway.smtp.MessageProcessResult;
import org.nhindirect.gateway.smtp.NotificationProducer;
import org.nhindirect.gateway.smtp.NotificationSettings;
import org.nhindirect.gateway.smtp.ReliableDispatchedNotificationProducer;
import org.nhindirect.gateway.smtp.SmtpAgent;
import org.nhindirect.stagent.IncomingMessage;
import org.nhindirect.stagent.MessageEnvelope;
import org.nhindirect.stagent.NHINDAddress;
import org.nhindirect.stagent.mail.Message;
import org.nhindirect.stagent.mail.notifications.Notification;
import org.nhindirect.stagent.mail.notifications.NotificationMessage;
import org.nhindirect.stagent.mail.notifications.NotificationType;
import org.nhindirect.stagent.mail.notifications.ReportingUserAgent;

/**
 * Responsible for receiving direct messages.
 */
public class DirectReceiverImpl extends DirectAdapter implements DirectReceiver {

    /**
     * Header value meaning that the sending STA is requesting dispositions.
     */
    public static final String X_DIRECT_FINAL_DESTINATION_DELIVERY_HEADER_VALUE = "X-DIRECT-FINAL-DESTINATION-DELIVERY";
    /**
     * Header name to determine if dispositions are being requested by the sending STA.
     */
    public static final String DISPOSITION_NOTIFICATION_OPTIONS_HEADER_NAME = "Disposition-Notification-Options";
    /**
     * The dispatch mdn producer.
     */
    private ReliableDispatchedNotificationProducer dispatchProducer = null;
    /**
     * The error mdn producer.
     */
    private ErrorNotificationProducer errorDispatchProducer = null;
    /**
     * The Constant LOG.
     */
    private static final Logger LOG = Logger.getLogger(DirectAdapter.class);

    /**
     * Instantiates a new direct receiver impl.
     *
     * @param externalMailSender used to send mail.
     * @param smtpAgent used to process direct messages.
     * @param directEventLogger used to log direct events.
     */
    public DirectReceiverImpl(MailSender externalMailSender, SmtpAgent smtpAgent, DirectEventLogger directEventLogger) {
        super(externalMailSender, smtpAgent, directEventLogger);
        NotificationSettings settings = new NotificationSettings();
        dispatchProducer = new ReliableDispatchedNotificationProducer(settings);
        errorDispatchProducer = new ErrorNotificationProducer(settings);
    }

    /**
     * Instantiates a new direct receiver impl.
     *
     * @param externalMailSender the external mail sender
     * @param smtpAgent the smtp agent
     * @param directEventLogger the direct event logger
     * @param producer the producer
     */
    public DirectReceiverImpl(MailSender externalMailSender, SmtpAgent smtpAgent, DirectEventLogger directEventLogger,
        ReliableDispatchedNotificationProducer producer) {
        super(externalMailSender, smtpAgent, directEventLogger);
        dispatchProducer = producer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void receiveInbound(MimeMessage message) {

        MessageProcessResult result = process(message);
        MessageEnvelope processedEnvelope = result.getProcessedMessage();
        boolean isMdn = DirectAdapterUtils.isMdn(processedEnvelope);
        if (isMdn) {
            getDirectEventLogger().log(DirectEventType.BEGIN_INBOUND_MDN, message);
        } else {
            getDirectEventLogger().log(DirectEventType.BEGIN_INBOUND_DIRECT, message);
        }
        //send the Processed MDN notification
        sendMdnProcessed(result);

        DirectEdgeProxy proxy = getDirectEdgeProxy();
        boolean notificationToEdgeFailed = false;

        try {
            proxy.provideAndRegisterDocumentSetB(processedEnvelope.getMessage());
        } catch (DirectException e) {
            notificationToEdgeFailed = true;
        }

        if (isMdn) {
            getDirectEventLogger().log(DirectEventType.END_INBOUND_MDN, message);
        } else {
            try {
                if (notificationToEdgeFailed) {
                    sendMdnFailed(result);
                } else {
                    sendMdnDispatched(result);
                }
            } catch (MessagingException e) {
                throw new DirectException("Error sending MDN "+(notificationToEdgeFailed?"Dispatched":"Failed")+".", e, message);
            }
            getDirectEventLogger().log(DirectEventType.END_INBOUND_DIRECT, message);
        }
    }

    /**
     * Send mdn dispatched.
     *
     * @param message the message
     * @throws MessagingException the messaging exception
     */
    private void sendMdnDispatched(MessageProcessResult result) throws MessagingException {
        if (result == null || result.getProcessedMessage() == null || result.getProcessedMessage().getMessage() == null) {
            throw new MessagingException("Unable to get processed message.");
        }
        // check request message for disposition request.
        MessageEnvelope envelope = result.getProcessedMessage();
        Message processedMessage = envelope.getMessage();
        String[] headers = processedMessage.getHeader(DISPOSITION_NOTIFICATION_OPTIONS_HEADER_NAME);
        if (headers != null) {
            for (String header : headers) {
                if (checkHeaderForDispatchedRequest(header)) {
                    Message STAMessasge = new Message(processedMessage);
                    IncomingMessage incomingMessage = new IncomingMessage(STAMessasge);
                    if (getSmtpAgent() != null) {
                        incomingMessage.setAgent(getSmtpAgent().getAgent());
                        Collection<NotificationMessage> messages = dispatchProducer.produce(incomingMessage);
                        sendMdns(messages);
                    }
                }
            }
        }
    }

    /**
     * Send mdn Failed.
     *
     * @param message the message
     * @throws MessagingException the messaging exception
     */
    private void sendMdnFailed(MessageProcessResult result) throws MessagingException {
        if (result == null || result.getProcessedMessage() == null || result.getProcessedMessage().getMessage() == null) {
            throw new MessagingException("Unable to get processed message.");
        }
        // check request message for disposition request.
        MessageEnvelope envelope = result.getProcessedMessage();
        Message processedMessage = envelope.getMessage();
        String[] headers = processedMessage.getHeader(DISPOSITION_NOTIFICATION_OPTIONS_HEADER_NAME);
        if (headers != null) {
            for (String header : headers) {
                if (checkHeaderForDispatchedRequest(header)) {
                    Message STAMessasge = new Message(processedMessage);
                    IncomingMessage incomingMessage = new IncomingMessage(STAMessasge);
                    if (getSmtpAgent() != null) {
                        incomingMessage.setAgent(getSmtpAgent().getAgent());
                        Collection<NotificationMessage> messages = errorDispatchProducer.produce(incomingMessage);
                        sendMdns(messages);
                    }
                }
            }
        }
    }

    /**
     *
     * @param header
     * @return
     */
    private boolean checkHeaderForDispatchedRequest(String header) {
        boolean dispatchRequested = false;
        String[] parts = StringUtils.split(header, "=");
        if (parts != null && parts.length == 2) {
            if (StringUtils.contains(parts[0], X_DIRECT_FINAL_DESTINATION_DELIVERY_HEADER_VALUE)
                && StringUtils.contains(parts[1], "true")) {
                dispatchRequested = true;
            }
        }
        return dispatchRequested;
    }

    /**
     * Send mdn processed.
     *
     * @param result the result
     */
    private void sendMdnProcessed(MessageProcessResult result) {
        Collection<NotificationMessage> mdnMessages = DirectAdapterUtils.getMdnMessages(result);
        sendMdns(mdnMessages);
    }

    /**
     * Send mdns.
     *
     * @param mdnMessages the mdn messages
     */
    private void sendMdns(Collection<NotificationMessage> mdnMessages) {
        if (mdnMessages != null) {
            for (NotificationMessage mdnMessage : mdnMessages) {
                getDirectEventLogger().log(DirectEventType.BEGIN_OUTBOUND_MDN, mdnMessage);
                try {
                    MimeMessage message = process(mdnMessage).getProcessedMessage().getMessage();
                    getExternalMailSender().send(mdnMessage.getAllRecipients(), message);
                } catch (Exception e) {
                    throw new DirectException("Exception sending outbound direct mdn.", e, mdnMessage);
                }
                getDirectEventLogger().log(DirectEventType.END_OUTBOUND_MDN, mdnMessage);
                LOG.info("MDN notification sent.");
            }
        }
    }
}

/**
 * Error Notification message Producer.
 * 
 */
class ErrorNotificationProducer extends NotificationProducer {

    /**
     * Constructor
     *
     * @param settings Notification specific settings
     */
    public ErrorNotificationProducer(NotificationSettings settings) {
        super(settings);
    }

    /**
     * {@inheritDoc}
     */
    protected Notification createAck(InternetAddress address) {
        Notification notification = new Notification(NotificationType.Error);
        if (settings.hasText()) {
            notification.setExplanation(settings.getText());
        }

        notification.setReportingAgent(new ReportingUserAgent(NHINDAddress.getHost(address), settings.getProductName()));
        notification.setExtensions(Arrays.asList(MDNStandard.DispositionOption_TimelyAndReliable));
        return notification;
    }
}
