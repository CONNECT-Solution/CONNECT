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

import javax.mail.Address;
import javax.mail.internet.MimeMessage;

import org.nhindirect.gateway.smtp.MessageProcessResult;
import org.nhindirect.gateway.smtp.SmtpAgent;
import org.nhindirect.xd.common.DirectDocuments;

/**
 * Interface defining a Mail Client.
 */
public interface DirectClient {

    /**
     * Use the mail server to send a DIRECT message.
     *
     * @param sender of the message
     * @param recipients of the message
     * @param documents to be attached to the message
     * @param messageId for the message
     */
    void processAndSend(Address sender, Address[] recipients, DirectDocuments documents, String messageId);
    
    /**
     * Use the mail server to send a DIRECT message. When you already have a mail message and you want to send it
     * as a DIRECT message. Sender and recipients are extracted from the mime message.
     *
     * @param message (mime) to be sent using the direct
     */
    void processAndSend(MimeMessage message);

    /**
     * Use the mail server to send a processed message. When you have a mail message and you want to send it without
     * performing anymore encryption/decryption or direct processing. Sender and recipients are extracted from the mime 
     * message.
     *
     * @param message (mime) to be sent using the direct
     */
    void send(MimeMessage message);

    /**
     * Use the mail server to send MDN messages if result contains notification messages.
     *
     * @param result to be processed for MDN Messages.
     */
    void sendMdn(MessageProcessResult result);

    /**
     * Pull messages from a server and use an injected MessageHandler to handle them.
     * @return number of messages handled.
     */
    int handleMessages();
    
    /**
     * Make the smtp agent on this direct client available to the caller.
     * @return SmtpAgent property of this client.
     */
    SmtpAgent getSmtpAgent();
}
