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

import org.nhindirect.gateway.smtp.MessageProcessResult;
import org.nhindirect.gateway.smtp.SmtpAgent;
import org.nhindirect.gateway.smtp.SmtpAgentFactory;

/**
 * Mail Server implementation which used the direct libraries to send encrypted mail.
 */
public class DirectMailClient implements DirectClient {

    private final MailServerSettings mailServerSettings;
    private final SmtpAgent smtpAgent;
    
    /**
     * Construct a direct mail server with mail server settings.
     * @param mailServerSettings used to define this mail server
     * @param smtpAgent direct smtp agent config file path relative to classpath used to configure SmtpAgent
     */
    public DirectMailClient(MailServerSettings mailServerSettings, final String smtpAgentFileName) {
        this.mailServerSettings = mailServerSettings;
        this.smtpAgent = SmtpAgentFactory.createAgent(getClass().getClassLoader().getResource(smtpAgentFileName));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(String sender, String recipient, Document attachment, String attachmentName) {
        // TODO Auto-generated method stub
        
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

    
    
}
