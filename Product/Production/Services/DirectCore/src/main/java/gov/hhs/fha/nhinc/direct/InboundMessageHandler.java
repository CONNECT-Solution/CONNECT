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

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nhindirect.gateway.smtp.MessageProcessResult;
import org.nhindirect.stagent.MessageEnvelope;
import org.nhindirect.stagent.NHINDAddress;
import org.nhindirect.stagent.NHINDAddressCollection;
import org.nhindirect.stagent.mail.notifications.NotificationMessage;

/**
 * Handles inbound messages from an external mail client. Inbound messages are un-directified and either
 * - sent to a recipient on an internal mail client
 *      - SMTP+Mime
 *      - SMTP+XDM
 * - process XDM as XDR
 * - SOAP+XDR       
 */
public class InboundMessageHandler implements MessageHandler {

    private static final Log LOG = LogFactory.getLog(InboundMessageHandler.class);
    
    /**
     * Define edge client for SMTP.
     */
    public static final int EDGE_CLIENT_TYPE_SMTP = 1;
        
    /**
     * Define edge client for SOAP.
     */
    public static final int EDGE_CLIENT_TYPE_SOAP = 2;
    
    /**
     * Instance for Internal Direct Client is only used when the edge client uses SMTP.
     */
    private DirectClient internalDirectClient;
    
    /**
     * Stores the configuration for which edge client is used.
     */
    private int edgeClientType;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void handleMessage(MimeMessage message, DirectClient externalDirectClient) {

        MessageProcessResult result = null;
        NHINDAddress origSender = null;
        NHINDAddressCollection origRecipients = null;
        
        try {
            origSender = DirectClientUtils.getNhindSender(message);
            origRecipients = DirectClientUtils.getNhindRecipients(message);
            result = externalDirectClient.getSmtpAgent().processMessage(message, origRecipients, origSender);
        } catch (MessagingException e) {
            throw new DirectException("Error processing message.", e);
        }
        
        externalDirectClient.sendMdn(result);
        
        MessageEnvelope processedMessage = result.getProcessedMessage();
        if (processedMessage == null) {
            logNotfications(result);
            return;
        } 

        switch (edgeClientType) {
        case EDGE_CLIENT_TYPE_SMTP:
            handleMessageAsSmtp(processedMessage);
            break;
            
        case EDGE_CLIENT_TYPE_SOAP:
            handleMessageAsSoap(processedMessage);
            break;

        default:
            throw new DirectException("Unknown edge client type: " + edgeClientType);
        }        
    }        
    
    /**
     * Handles the message for SMTP edge clients.
     * @param processedMessage decrypted message to be handled.
     */
    private void handleMessageAsSmtp(MessageEnvelope processedMessage) {
        if (internalDirectClient == null) {
            throw new DirectException("Internal Direct Client is not set as a property on this Message Handler.");
        }
        internalDirectClient.send(processedMessage.getMessage());
    }
    
    /**
     * Handles the message for SOAP edge clients.
     * @param processedMessage decrypted message to be handled.
     */
    private void handleMessageAsSoap(MessageEnvelope processedMessage) {
        if (internalDirectClient != null) {
            LOG.info("Internal Direct Client property is not needed on this Message Handler.");
        }
        
        // TODO - Handle inbound direct messages as a SOAP/XDR transformation.
        throw new UnsupportedOperationException("TODO - we need to implement SOAP/XDR handling from XDM.");
    }
    
    /**
     * Log any notification messages that were produced by direct processing.
     * @param result to pull notification messages from
     */
    private void logNotfications(MessageProcessResult result) {
        StringBuilder builder = new StringBuilder("Inbound Mime Message could not be processed by DIRECT.");
        for (NotificationMessage notif : result.getNotificationMessages()) {
            try {
                builder.append(" " + notif.getContent());
            } catch (Exception e) {
                LOG.warn("Exception while logging notification messages.", e);
            }
        }
        LOG.info(builder.toString());
    }

    /**
     * @param internalDirectClient the internalDirectClient to set
     */
    public void setInternalDirectClient(DirectClient internalDirectClient) {
        this.internalDirectClient = internalDirectClient;
    }

    /**
     * @param edgeClientType the edge client type to set
     */
    public void setEdgeClientType(int edgeClientType) {
        this.edgeClientType = edgeClientType;
    }   
}
