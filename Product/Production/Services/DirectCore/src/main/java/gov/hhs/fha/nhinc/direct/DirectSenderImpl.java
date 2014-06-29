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

import gov.hhs.fha.nhinc.direct.event.DirectEventLogger;
import gov.hhs.fha.nhinc.direct.event.DirectEventType;
import gov.hhs.fha.nhinc.direct.messagemonitoring.dao.impl.MessageMonitoringDAOImpl;
import gov.hhs.fha.nhinc.direct.messagemonitoring.impl.MessageMonitoringAPI;
import gov.hhs.fha.nhinc.mail.MailSender;

import javax.mail.Address;
import javax.mail.internet.MimeMessage;
import org.apache.log4j.Logger;

import org.nhindirect.gateway.smtp.SmtpAgent;
import org.nhindirect.xd.common.DirectDocuments;

/**
 * Used to send outbound direct messages.
 */
public class DirectSenderImpl extends DirectAdapter implements DirectSender {

    private static final Logger LOG = Logger.getLogger(DirectSenderImpl.class);
    private static final String MSG_SUBJECT = "DIRECT Message";
    private static final String MSG_TEXT = "DIRECT Message body text";
    private static final int OUTBOUND_RETRY_COUNT = 1;
    private static final int OUTBOUND_NUMBER_TIMES_TRIED = 1;
    /**
     * @param externalMailSender used to send messages.
     * @param smtpAgent used to process direct messages.
     * @param directEventLogger used to log direct events.
     */
    public DirectSenderImpl(MailSender externalMailSender, SmtpAgent smtpAgent, DirectEventLogger directEventLogger) {
        super(externalMailSender, smtpAgent, directEventLogger);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendOutboundDirect(MimeMessage message) {
        
        //if (MessageMonitoringImpl.getInstance().isRetryOutgoingMeesage(message)){
            //update the DB & Cache
            //MessageMonitoringImpl.getInstance().updateMessageMonitoring(message);
        //}
        getDirectEventLogger().log(DirectEventType.BEGIN_OUTBOUND_DIRECT, message);
        try {
            MimeMessage processedMessage = process(message).getProcessedMessage().getMessage();
            getExternalMailSender().send(message.getAllRecipients(), processedMessage);
        } catch (Exception e) {
            //if its security error then return send a message back to sender
            if (OUTBOUND_RETRY_COUNT >= OUTBOUND_NUMBER_TIMES_TRIED){
                //write the code to send a Failure message
                //getDirectEventLogger().log(DirectEventType.END_OUTBOUND_DIRECT, message);
                //put it on a delete bin folder
                return;
            }
            throw new DirectException("Exception sending outbound direct.", e, message);
        }finally{
            LOG.debug("Before inserting Outgoing Message");
            MessageMonitoringAPI.getInstance().addOutgoingMessage(message);
        }
        getDirectEventLogger().log(DirectEventType.END_OUTBOUND_DIRECT, message);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void sendOutboundDirect(Address sender, Address[] recipients, DirectDocuments documents, String messageId) {
        MimeMessage message = null;
        try {
            message = new MimeMessageBuilder(getExternalMailSender().getMailSession(), sender, recipients)
                    .subject(MSG_SUBJECT).text(MSG_TEXT).documents(documents).messageId(messageId).build();
            sendOutboundDirect(message);
        } catch (Exception e) {
            throw new DirectException("Error building and sending mime message.", e, message);
        }
    }
    
}
