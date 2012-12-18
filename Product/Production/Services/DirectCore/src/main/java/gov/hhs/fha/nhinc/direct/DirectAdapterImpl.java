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

import gov.hhs.fha.nhinc.direct.edge.proxy.DirectEdgeProxy;
import gov.hhs.fha.nhinc.direct.edge.proxy.DirectEdgeProxyObjectFactory;
import gov.hhs.fha.nhinc.direct.event.DirectEventLogger;
import gov.hhs.fha.nhinc.direct.event.DirectEventType;
import gov.hhs.fha.nhinc.mail.MailClient;

import java.util.Collection;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.nhindirect.gateway.smtp.MessageProcessResult;
import org.nhindirect.gateway.smtp.SmtpAgent;
import org.nhindirect.stagent.MessageEnvelope;
import org.nhindirect.stagent.NHINDAddressCollection;
import org.nhindirect.stagent.mail.notifications.NotificationMessage;
import org.nhindirect.xd.common.DirectDocuments;

/**
 * This class adapts the Direct code responsible for processing messages.
 */
public class DirectAdapterImpl implements DirectAdapter {

    private static final Logger LOG = Logger.getLogger(DirectAdapterImpl.class);
    
    private final MailClient externalMailClient;
    private final SmtpAgent smtpAgent;
    
    private static final String MSG_SUBJECT = "DIRECT Message";
    private static final String MSG_TEXT = "DIRECT Message body text";

    /**
     * @param externalMailClient external mail client.
     * @param smtpAgent used to process direct messages.
     */
    public DirectAdapterImpl(MailClient externalMailClient, SmtpAgent smtpAgent) {
        super();
        this.externalMailClient = externalMailClient;
        this.smtpAgent = smtpAgent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendOutboundDirect(MimeMessage message) {
        DirectEventLogger.getInstance().log(DirectEventType.BEGIN_OUTBOUND_DIRECT, message);
        try {
            externalMailClient.send(message.getAllRecipients(), process(message).getProcessedMessage().getMessage());
        } catch (Exception e) {
            throw new DirectException("Exception sending outbound direct.", e, message);
        }
        DirectEventLogger.getInstance().log(DirectEventType.END_OUTBOUND_DIRECT, message);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void sendOutboundDirect(Address sender, Address[] recipients, DirectDocuments documents, String messageId) {
        MimeMessage message = null;
        try {
            message = new MimeMessageBuilder(externalMailClient.getMailSession(), sender, recipients)
                    .subject(MSG_SUBJECT).text(MSG_TEXT).documents(documents).messageId(messageId).build();
            sendOutboundDirect(message);
        } catch (Exception e) {
            throw new DirectException("Error building and sending mime message.", e, message);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void receiveInbound(MimeMessage message) {
        
        MessageProcessResult result = processAsDirectMessage(message);        
        MessageEnvelope processedEnvelope = result.getProcessedMessage();
        if (processedEnvelope == null) {            
            throw new DirectException("Result Message Envelope is null: "
                    + getErrorNotificationMsgs(result), message);
        }
        
        boolean isMdn = DirectAdapterUtils.isMdn(processedEnvelope);
        if (isMdn) {
            DirectEventLogger.getInstance().log(DirectEventType.BEGIN_INBOUND_MDN, message);
        } else {
            DirectEventLogger.getInstance().log(DirectEventType.BEGIN_INBOUND_DIRECT, message);            
        }

        sendMdn(result);
        
        DirectEdgeProxy proxy = getDirectEdgeProxy();
        proxy.provideAndRegisterDocumentSetB(processedEnvelope.getMessage());
        
        if (isMdn) {
            DirectEventLogger.getInstance().log(DirectEventType.END_INBOUND_MDN, message);
        } else {
            DirectEventLogger.getInstance().log(DirectEventType.END_INBOUND_DIRECT, message);            
        }
    }
    
    private void sendMdn(MessageProcessResult result) {

        Collection<NotificationMessage> mdnMessages = DirectAdapterUtils.getMdnMessages(result);
        if (mdnMessages != null) {
            for (NotificationMessage mdnMessage : mdnMessages) {
                DirectEventLogger.getInstance().log(DirectEventType.BEGIN_OUTBOUND_MDN, mdnMessage);
                try {
                    externalMailClient.send(mdnMessage.getAllRecipients(), process(mdnMessage).getProcessedMessage()
                            .getMessage());
                } catch (Exception e) {
                    throw new DirectException("Exception sending outbound direct mdn.", e, mdnMessage);
                }
                DirectEventLogger.getInstance().log(DirectEventType.END_OUTBOUND_MDN, mdnMessage);
                LOG.info("MDN notification sent.");
            }
        }
    }
    
    /**
     * Process a direct message and return {@link MessageProcessResult}. If the processed message envelope returned
     * is null, a Direct Error event is created with the notification messages if they are available. Then a
     * {@link DirectException} is thrown.
     * @param message processed message.
     * @return message process result.
     */
    private MessageProcessResult process(MimeMessage message) {
        
        MessageProcessResult result = processAsDirectMessage(message);
        MessageEnvelope envelope = result.getProcessedMessage();
        if (envelope == null) {
            throw new DirectException("Result Message Envelope is null: " + getErrorNotificationMsgs(result), message);
        }
                
        return result;
    }

    private MessageProcessResult processAsDirectMessage(MimeMessage mimeMessage) {
        MessageProcessResult result;
        try {
            NHINDAddressCollection collection = DirectAdapterUtils.getNhindRecipients(mimeMessage);
            result = smtpAgent.processMessage(mimeMessage, collection, DirectAdapterUtils.getNhindSender(mimeMessage));
        } catch (MessagingException e) {
            String errorString = "Error occurred while extracting addresses.";
            LOG.error(errorString, e);
            throw new DirectException(errorString, e, mimeMessage);
        }
        
        if (result == null) {
            throw new DirectException("Message Process Result by Direct is null.", mimeMessage);
        }
        return result;
    }
    
    /**
     * @return DirectEdgeProxy implementation to handle the direct edge
     */
    protected DirectEdgeProxy getDirectEdgeProxy() {
        DirectEdgeProxyObjectFactory factory = new DirectEdgeProxyObjectFactory();
        return factory.getDirectEdgeProxy();
    }    
    
    /**
     * Log any notification messages that were produced by direct processing.
     * 
     * @param result to pull notification messages from
     */
    private String getErrorNotificationMsgs(MessageProcessResult result) {
        StringBuilder builder = new StringBuilder("Inbound Mime Message could not be processed by DIRECT.");
        for (NotificationMessage notif : result.getNotificationMessages()) {
            try {
                builder.append(" " + notif.getContent());
            } catch (Exception e) {
                LOG.warn("Exception while logging notification messages.", e);
            }
        }
        return builder.toString();
    }
}
