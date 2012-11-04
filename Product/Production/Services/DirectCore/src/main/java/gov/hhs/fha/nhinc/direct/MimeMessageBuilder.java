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

import java.io.IOException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

/**
 * Builder for {@link MimeMessage}.
 */
public class MimeMessageBuilder {

    private final Session session;
    private final String fromAddress;
    private final String recipient;

    private String subject;
    private String text;
    private Document attachment;
    private String attachmentName;
    
    /**
     * Construct the Mime Message builder with required fields.
     * @param session used to build the message.
     * @param fromAddress sender of the message.
     * @param recipient of the message.
     */
    public MimeMessageBuilder(Session session, String fromAddress, String recipient) {
        super();
        this.session = session;
        this.fromAddress = fromAddress;
        this.recipient = recipient;
    }
    
    /**
     * @param str for subject.
     * @return builder
     */
    public MimeMessageBuilder subject(String str) {
        this.subject = str;
        return this;
    }

    /**
     * @param str for text
     * @return builder
     */
    public MimeMessageBuilder text(String str) {
        this.text = str;
        return this;
    }
    
    /**
     * @param doc for attachment
     * @return builder
     */
    public MimeMessageBuilder attachment(Document doc) {
        this.attachment = doc;
        return this;
    }
    
    /**
     * @param str for attachment name
     * @return builder
     */
    public MimeMessageBuilder attachmentName(String str) {
        this.attachmentName = str;
        return this;
    }

    /**
     * Build the Mime Message.
     * @return the Mime message.
     */
    public MimeMessage build()  {

        final MimeMessage message = new MimeMessage(session);
        
        try {
            message.setFrom(new InternetAddress(fromAddress));
        } catch (Exception e) {
            throw new DirectException("Exception setting from address: " + fromAddress, e);
        }
        
        try {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        } catch (Exception e) {
            throw new DirectException("Exception setting recipient to address: " + recipient, e);            
        }
        
        try {
            message.setSubject(subject);
        } catch (Exception e) {
            throw new DirectException("Exception setting subject: " + recipient, e);                        
        }
        
        MimeBodyPart messagePart = new MimeBodyPart();
        try {
            messagePart.setText(text);
        } catch (Exception e) {
            throw new DirectException("Exception setting mime message part text: " + text, e);
        }

        MimeBodyPart attachmentPart = null;
        try {
            attachmentPart = createAttachmentFromSOAPRequest(attachment, attachmentName);
        } catch (Exception e) {
            throw new DirectException("Exception creating attachment: " + attachmentName, e);
        }
        
        Multipart multipart = new MimeMultipart();
        try {
            multipart.addBodyPart(messagePart);
            multipart.addBodyPart(attachmentPart);
            message.setContent(multipart);
        } catch (Exception e) {
            throw new DirectException("Exception creating multi-part attachment.", e);
        }
        
        try {
            message.saveChanges();
        } catch (Exception e) {
            throw new DirectException("Exception saving changes.", e);
        }

        return message;
    }

    private MimeBodyPart createAttachmentFromSOAPRequest(Document data, String name) throws MessagingException,
            IOException {
        DataSource source = new ByteArrayDataSource(data.getValue().getInputStream(), "application/octet-stream");
        DataHandler dhnew = new DataHandler(source);
        MimeBodyPart bodypart = new MimeBodyPart();
        bodypart.setDataHandler(dhnew);
        bodypart.setHeader("Content-Type", "application/octet-stream");
        bodypart.setDisposition(Part.ATTACHMENT);
        bodypart.setFileName(name);
        return (MimeBodyPart) bodypart;
    }

}
