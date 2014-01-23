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
import java.util.Collection;
import java.util.Map;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.mailet.Mailet;
import org.nhindirect.common.mail.dsn.DSNFailureTextBodyPartGenerator;
import org.nhindirect.common.mail.dsn.DSNGenerator;
import org.nhindirect.common.mail.dsn.impl.DefaultDSNFailureTextBodyPartGenerator;
import org.nhindirect.common.mail.dsn.impl.HumanReadableTextAssemblerFactory;
import org.nhindirect.common.tx.TxUtil;
import org.nhindirect.common.tx.impl.DefaultTxDetailParser;
import org.nhindirect.common.tx.model.Tx;
import org.nhindirect.common.tx.model.TxDetail;
import org.nhindirect.common.tx.model.TxDetailType;
import org.nhindirect.gateway.GatewayConfiguration;
import org.nhindirect.gateway.smtp.MessageProcessResult;
import org.nhindirect.gateway.smtp.NotificationSettings;
import org.nhindirect.gateway.smtp.ReliableDispatchedNotificationProducer;
import org.nhindirect.gateway.smtp.SmtpAgent;
import org.nhindirect.gateway.smtp.dsn.impl.FailedDeliveryDSNCreator;
import org.nhindirect.gateway.smtp.dsn.impl.RejectedRecipientDSNCreatorOptions;
import org.nhindirect.stagent.IncomingMessage;
import org.nhindirect.stagent.MessageEnvelope;
import org.nhindirect.stagent.NHINDAddress;
import org.nhindirect.stagent.NHINDAddressCollection;
import org.nhindirect.stagent.mail.Message;
import org.nhindirect.stagent.mail.notifications.NotificationMessage;

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
     * The Constant LOG.
     */
    private static final Logger LOG = Logger.getLogger(DirectAdapter.class);
    /**
     * SuppressMDNEdgeNotification system property
     */
    public static final String SUPPRESS_MDN_EDGE_NOTIFICATION = "org.connectopensource.suppressmdnedgenotification";
    //Default CONNECT failed notifcation header, error message, footer text.
    private static final String HEADER = "We were permanently unable to deliver your message to the following recipients.  Please contact your system administrator with further questions.";
    private static final String ERROR_MESSAGE = "The Direct address that you tried to reach is not responding. Try double-checking the recipient's address for typos or unnecessary spaces.";
    private static final String FOOTER = "";
    //specifies which Postmaster account to be user for DSN. Default value Sender's postmaster account.
    private static final boolean USE_SENDER_POSTMASTER_ACCOUNT = true;

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

        //send the MDN processed back to the receiver if the message is not a mdn
        if (!isMdn) {
            sendMdnProcessed(result);
        }
        boolean notificationToEdgeFailed = false;
        String notificationFailureMessage = null;

        //Only send the message to edge client 
        //1. if its a not a mdn (or)
        //2. if its a mdn and SuppressMDNEdgeNotification flag is false
        if ((isEdgeMDNNotificationEnabled() && isMdn) || (!isMdn)) {
            DirectEdgeProxy proxy = getDirectEdgeProxy();
            try {
                proxy.provideAndRegisterDocumentSetB(processedEnvelope.getMessage());
            } catch (DirectException e) {
                getDirectEventLogger().log(DirectEventType.DIRECT_ERROR,message,e.getMessage());
                //capture the error message
                notificationFailureMessage = e.getMessage();
                notificationToEdgeFailed = true;
            }
        }

        if (isMdn) {
            LOG.info("MDN Processed notification sent to the edge client.");
            getDirectEventLogger().log(DirectEventType.END_INBOUND_MDN, message);
        } else {
            try {
                if (notificationToEdgeFailed) {
                    sendMdnFailed(result.getProcessedMessage().getMessage(), notificationFailureMessage);
                } else {
                    sendMdnDispatched(result);
                }
            } catch (MessagingException e) {
                throw new DirectException("Error sending MDN dispatched.", e, message);
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

    /**
     * Returns true if the Edge MDN notification is enabled else false.
     *
     * @return true or false
     */
    protected boolean isEdgeMDNNotificationEnabled() {
        //read the system property SuppressMDNEdgeNotification
        String mdnNotificationSuppressed = System.getProperty(SUPPRESS_MDN_EDGE_NOTIFICATION);
        //if SuppressMDNEdgeNotification is false
        if (mdnNotificationSuppressed != null && mdnNotificationSuppressed.equals("false")) {
            return true;
        }
        return false;
    }

    /**
     * Creates Failed Delivery DSN messages
     *
     * @param mimeMessages email message received
     * @param tx
     * @param notificationFailureMessage
     *
     * @return returns a collection of DSN messages using message information provided.
     */
    public Collection<MimeMessage> createFailedDeliveryDSNFailureMessage(MimeMessage message, Tx tx, String notificationFailureMessage) throws MessagingException {
        //Default properties can be set in a property file agentSettings.properties
        //always use null
        Mailet mailet = null;
        //use the default prefix
        DSNGenerator generator = new DSNGenerator(RejectedRecipientDSNCreatorOptions.DEFAULT_PREFIX);
        //use the default postmasterbox
        String postmasterMailbox = GatewayConfiguration.getConfigurationParam(RejectedRecipientDSNCreatorOptions.DSN_POSTMASTER,
            mailet, RejectedRecipientDSNCreatorOptions.DEFAULT_POSTMASTER);
        //use the default reporting MTA
        String reportingMta = GatewayConfiguration.getConfigurationParam(RejectedRecipientDSNCreatorOptions.DSN_MTA_NAME,
            mailet, RejectedRecipientDSNCreatorOptions.DEFAULT_MTA_NAME);
        DSNFailureTextBodyPartGenerator textGenerator = new DefaultDSNFailureTextBodyPartGenerator(
            GatewayConfiguration.getConfigurationParam(RejectedRecipientDSNCreatorOptions.DSN_FAILED_HEADER,
            mailet, HEADER),
            GatewayConfiguration.getConfigurationParam(RejectedRecipientDSNCreatorOptions.DSN_FAILED_FOOTER,
            mailet, FOOTER),
            GatewayConfiguration.getConfigurationParam(RejectedRecipientDSNCreatorOptions.DSN_FAILED_RECIP_TITLE,
            mailet, RejectedRecipientDSNCreatorOptions.DEFAULT_FAILED_RECIP_TITLE),
            RejectedRecipientDSNCreatorOptions.DEFAULT_ERROR_MESSAGE_TITLE,
            GatewayConfiguration.getConfigurationParam(RejectedRecipientDSNCreatorOptions.DSN_FAILED_ERROR_MESSAGE,
            mailet, ERROR_MESSAGE),
            HumanReadableTextAssemblerFactory.getInstance());

        FailedDeliveryDSNCreator rejectedDNSCreator = new FailedDeliveryDSNCreator(generator, postmasterMailbox, reportingMta, textGenerator);
        final NHINDAddressCollection xdRecipients = new NHINDAddressCollection();
        Address[] recipients = message.getAllRecipients();
        xdRecipients.add(new NHINDAddress((InternetAddress) recipients[0]));
        return rejectedDNSCreator.createDSNFailure(tx, xdRecipients, USE_SENDER_POSTMASTER_ACCOUNT);
    }

    /**
     * Create a Tx object based on the email message
     *
     * @param message email message received
     *
     * @return returns a Tx object
     */
    private Tx convertMessagetoTx(MimeMessage message) throws MessagingException {
        Map<String, TxDetail> details = new DefaultTxDetailParser().getMessageDetails(message);
        details.put(TxDetailType.PARENT_MSG_ID.getType(), new TxDetail(TxDetailType.PARENT_MSG_ID, "NotNotification"));
        details.put(TxDetailType.SUBJECT.getType(), new TxDetail(TxDetailType.SUBJECT, message.getSubject()));
        details.put(TxDetailType.FROM.getType(), new TxDetail(TxDetailType.FROM, getFromAddress(message)));
        details.put(TxDetailType.MSG_ID.getType(), new TxDetail(TxDetailType.MSG_ID, message.getMessageID()));
        Tx tx = new Tx(TxUtil.getMessageType(message), details);
        return tx;
    }

    /**
     * Sends out a Failed MDN/Delivery Status Notification(DSN)
     *
     * @param message
     * @param notificationFailureMessage 
     *
     */
    private void sendMdnFailed(MimeMessage message, String notificationFailureMessage) throws MessagingException {
        Collection<MimeMessage> mimeMessage = createFailedDeliveryDSNFailureMessage(message, convertMessagetoTx(message), notificationFailureMessage);
        sendDSN(mimeMessage);
    }

    /**
     * Sends out a Delivery Status Notification(DSN)
     *
     * @param dsnMessages the list of DSN messages
     *
     */
    private void sendDSN(Collection<MimeMessage> dsnMessages) {
        if (dsnMessages != null) {
            for (MimeMessage dsnMessage : dsnMessages) {
                try {
                    LOG.info("Seding DSN.");
                    getExternalMailSender().send(dsnMessage.getAllRecipients(), dsnMessage);
                } catch (Exception e) {
                    throw new DirectException("Exception sending outbound direct dsn.", e, dsnMessage);
                }
                LOG.info("DSN notification sent.");
            }
        }
    }

    /**
     * Return From address from the MIME message
     *
     * @param messages the mail message
     *
     * @return returns the from email address
     */
    private String getFromAddress(MimeMessage message) throws MessagingException {
        Address[] fromAddress = message.getFrom();
        if ((fromAddress != null) && (fromAddress.length > 0)) {
            LOG.info("Message fromAddress found.");
            return ((InternetAddress) fromAddress[0]).getAddress();
        }
        LOG.info("Message fromAddress not found.");
        return null;
    }
}
