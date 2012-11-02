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

import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.nhindirect.gateway.smtp.MessageProcessResult;
import org.nhindirect.gateway.smtp.SmtpAgent;
import org.nhindirect.stagent.AddressSource;
import org.nhindirect.stagent.NHINDAddress;
import org.nhindirect.stagent.NHINDAddressCollection;

/**
 * Mail Server implementation which used the direct libraries to send encrypted mail.
 */
public class DirectMailClient implements DirectClient {

    // TODO::Where should these come from?...
    private static final String MSG_SUBJECT = "DIRECT Message";
    private static final String MSG_TEXT = "DIRECT Message body text";
    
    private final Properties mailServerProps;    
    private final SmtpAgent smtpAgent;
    
    /**
     * Construct a direct mail server with mail server settings.
     * @param mailServerSettings used to define this mail server
     * @param smtpAgent direct smtp agent config file path relative to classpath used to configure SmtpAgent
     */
    public DirectMailClient(final Properties mailServerProps, final SmtpAgent smtpAgent) {
        this.mailServerProps = mailServerProps;
        this.smtpAgent = smtpAgent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(String sender, String recipient, Document attachment, String attachmentName) {

        Session session = Session.getInstance(mailServerProps, new SMTPAuthenticator());

        MimeMessage mimeMessage = new MimeMessageBuilder(session, sender, recipient).subject(MSG_SUBJECT)
                .text(MSG_TEXT).attachment(attachment).attachmentName(attachmentName).build();
            
        MessageProcessResult result = processAsDirectMessage(recipient, sender, mimeMessage);
        if (null == result || null == result.getProcessedMessage()) {
            throw new DirectException("Message processed by Direct is null.");
        }
        
        try {
            sendDirectProcessedMessage(recipient, session, result);
        } catch (MessagingException e) {
            throw new DirectException("Could not send message.", e);            
        }
    }
    
    private void sendDirectProcessedMessage(String recipient, Session session, MessageProcessResult result) 
            throws MessagingException {
        
        Transport transport = null;
        try {
            transport = session.getTransport("smtps");
            transport.connect();
            InternetAddress[] addressTo = new InternetAddress[1];
            addressTo[0] = new InternetAddress(recipient);
            transport.sendMessage(result.getProcessedMessage().getMessage(), addressTo);
        } finally {
            transport.close();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMdn(String sender, String recipient, MessageProcessResult result) {
        // TODO Auto-generated method stub
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int handleMessages(MessageHandler handler) {
        // TODO Auto-generated method stub
        return 0;
    }

    private class SMTPAuthenticator extends javax.mail.Authenticator {
        /**
         * {@inheritDoc}
         */
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(mailServerProps.getProperty("mail.smtp.user"), 
                    mailServerProps.getProperty("mail.smtp.password"));
        }
    }
    
    private MessageProcessResult processAsDirectMessage(String recipient, String sender, MimeMessage mimeMessage) {

        Address recipAddr = null;
        try {
            recipAddr = new InternetAddress(recipient);            
        } catch (AddressException e) {
            throw new DirectException("Could not transform recipient string into mail address: " + recipient, e);
        }        
        NHINDAddressCollection recipients = new NHINDAddressCollection();
        recipients.add(new NHINDAddress(recipAddr.toString(), (AddressSource) null));

        InternetAddress senderAddr = null;
        try {
            senderAddr = new InternetAddress(sender);            
        } catch (AddressException e) {
            throw new DirectException("Could not transform sender string into mail address: " + sender, e);
        }
        
        NHINDAddress senderNhindAddress = new NHINDAddress(senderAddr, AddressSource.From);
        return smtpAgent.processMessage(mimeMessage, recipients, senderNhindAddress);   
    }
    
}
