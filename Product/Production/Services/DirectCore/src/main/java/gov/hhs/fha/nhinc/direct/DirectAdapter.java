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
import gov.hhs.fha.nhinc.mail.MailSender;

import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.nhindirect.gateway.smtp.MessageProcessResult;
import org.nhindirect.gateway.smtp.SmtpAgent;
import org.nhindirect.stagent.MessageEnvelope;
import org.nhindirect.stagent.NHINDAddressCollection;
import org.nhindirect.stagent.mail.notifications.NotificationMessage;

/**
 * This class adapts the Direct code responsible for processing messages.
 */
public abstract class DirectAdapter {

    private static final Logger LOG = Logger.getLogger(DirectAdapter.class);
    
    private final MailSender externalMailSender;
    private final SmtpAgent smtpAgent;
    private final DirectEventLogger directEventLogger;
    
    /**
     * @param externalMailSender external mail sender.
     * @param smtpAgent used to process direct messages.
     * @param directEventLogger used to log events.
     */
    public DirectAdapter(MailSender externalMailSender, SmtpAgent smtpAgent, DirectEventLogger directEventLogger) {
        this.externalMailSender = externalMailSender;
        this.smtpAgent = smtpAgent;
        this.directEventLogger = directEventLogger;
    }
    
    /**
     * Process a direct message and return {@link MessageProcessResult}. If the returned result is null, a Direct Error
     * event is created, and a DirectException is thrown. If the processed message envelope returned is null, a Direct
     * Error event is created with the notification messages if they are available. Then a {@link DirectException} is
     * thrown. This method is guaranteed to return a populated result, otherwise it throws a DirectException.
     * 
     * @param message (mime) to be processed.
     * @return message process result, populated with a processed message envelope.
     */
    protected MessageProcessResult process(MimeMessage message) {
        
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
            result = smtpAgent.processMessage(mimeMessage, collection,
                    DirectAdapterUtils.getNhindSender(mimeMessage));
        } catch (Exception e) {
            String errorString = "Error occurred while processing message.";
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
     * @return String representation of notification messages from the result.
     */
    protected String getErrorNotificationMsgs(MessageProcessResult result) {
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

    /**
     * @return the externalMailSender
     */
    protected MailSender getExternalMailSender() {
        return externalMailSender;
    }

    /**
     * @return the smtpAgent
     */
    protected SmtpAgent getSmtpAgent() {
        return smtpAgent;
    }

    /**
     * @return the directEventLogger
     */
    protected DirectEventLogger getDirectEventLogger() {
        return directEventLogger;
    }    
}
