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

import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.getFileAsString;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.removeSmtpAgentConfig;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.writeSmtpAgentConfig;
import static org.mockito.Mockito.mock;

import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.nhindirect.gateway.smtp.SmtpAgent;
import org.nhindirect.gateway.smtp.SmtpAgentFactory;

/**
 * the SMTP/IMAP using gmail
 */
public class DirectClientGmailTest {
    
    private final Properties props = getMailServerProps();
    
    /**
     * Set up keystore for test.
     */
    @BeforeClass
    public static void setUpClass() {
        writeSmtpAgentConfig();
    }
    
    /**
     * Tear down keystore created in setup.
     */
    @AfterClass
    public static void tearDownClass() {
        removeSmtpAgentConfig();
    }  
    

    /**
     * Prove that fetch problem for {@link MimeMessage#getRecipients(javax.mail.Message.RecipientType)} is related to
     * greenmail and not the client code.
     * @throws Exception
     */
    @Test
    @Ignore
    public void testImapsFetchWithGmail() throws Exception {        
        DirectClient directClient = new DirectMailClient(props, getSmtpAgent());
        initiateEmail();
        MessageHandler mockHandler = mock(MessageHandler.class);
        directClient.handleMessages(mockHandler);
    }

    /**
     * Sets up the properties in order to connect to the green mail test server.
     * 
     * @param smtpPort for smtps
     * @param imapPort for imaps
     * @return Properties instance holding appropriate values for java mail.
     */
    private static Properties getMailServerProps() {

        Properties props = new Properties();

        props.setProperty("direct.mail.user", "mlandisdirect@gmail.com");
        props.setProperty("direct.mail.pass", "xxxxxxx");
        props.setProperty("direct.max.msgs.in.batch", "5");

        props.setProperty("mail.smtps.host", "smtp.gmail.com");
        props.setProperty("mail.smtps.auth", "TRUE");
        props.setProperty("mail.smtps.port", "465");
        props.setProperty("mail.smtps.starttls.enabled", "TRUE");

        props.setProperty("mail.imaps.host", "imap.gmail.com");
        props.setProperty("mail.imaps.port", "993");
        props.setProperty("mail.imaps.connectiontimeout", Integer.toString(15 * 1000));
        props.setProperty("mail.imaps.timeout", Integer.toString(15 * 60 * 1000));

        return props;
    }
    
    private SmtpAgent getSmtpAgent() {
        return SmtpAgentFactory.createAgent(getClass().getClassLoader().getResource("smtp.agent.config.xml"));
    }

    private void initiateEmail() throws Exception {
        MimeMessage originalMsg = new MimeMessage(null, IOUtils.toInputStream(getFileAsString("PlainOutgoingMessage.txt")));

        Session session = MailUtils.getMailSession(props, props.getProperty("direct.mail.user"),
                props.getProperty("direct.mail.pass"));
        session.setDebug(true);
        session.setDebugOut(System.out);
        Transport transport = null;
        try {
            transport = session.getTransport("smtps");
            transport.connect();
            transport.sendMessage(originalMsg, originalMsg.getAllRecipients());
        } finally {
            transport.close();
        }
    }
    

}
