/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
import gov.hhs.fha.nhinc.mail.ImapMailReceiver;
import gov.hhs.fha.nhinc.mail.MailReceiver;
import gov.hhs.fha.nhinc.mail.MailUtils;
import gov.hhs.fha.nhinc.mail.MessageHandler;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.mockito.Mockito.mock;

/**
 * This is basically a sanity check to make sure the smtp and imap settings we use can talk to a real mail server. If
 * you want to run it you have to remove the ignore and put your real connection creds in the class.
 */
@Ignore
public class DirectMailPollerGmailTest extends DirectBaseTest {

    private final Properties props = getMailServerProps();

    /**
     * Set up keystore for test.
     */
    @BeforeClass
    public static void setUpClass() {
    }

    /**
     * Tear down keystore created in setup.
     */
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Prove that fetch problem for {@link MimeMessage#getRecipients(javax.mail.Message.RecipientType)} is related to
     * greenmail and not the client code.
     *
     * @throws Exception on error.
     */
    @Test
    public void testImapsFetchWithGmail() throws Exception {
        MessageHandler mockHandler = mock(MessageHandler.class);
        MailReceiver mailReceiver = new ImapMailReceiver(props);
        DirectMailPoller mailPoller = new DirectMailPoller(mailReceiver, mockHandler);
        initiateEmail();
        mailPoller.poll();
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

        props.setProperty("connect.mail.user", "xxx");
        props.setProperty("connect.mail.pass", "xxx");
        props.setProperty("connect.max.msgs.in.batch", "5");

        props.setProperty("connect.mail.session.debug", "true");
        props.setProperty("connect.delete.unhandled.msgs", "false");

        props.setProperty("mail.smtp.host", "smtp-01.direct.connectopensource.org");
        props.setProperty("mail.smtp.auth", "false");
        props.setProperty("mail.smtp.port", "587");
        props.setProperty("mail.smtp.starttls.enable", "true");

        props.setProperty("mail.imaps.host", "imap-01.direct.connectopensource.org");
        props.setProperty("mail.imaps.port", "993");
        props.setProperty("mail.imaps.connectiontimeout", DirectUnitTestUtil.TIMEOUT_CONNECTION_MILLIS);
        props.setProperty("mail.imaps.timeout", DirectUnitTestUtil.TIMEOUT_MILLIS);

        return props;
    }

    private void initiateEmail() throws MessagingException {

        Session session = MailUtils.getMailSession(props, props.getProperty("connect.mail.user"),
            props.getProperty("connect.mail.pass"));
        MimeMessage originalMsg = new MimeMessage(session,
            IOUtils.toInputStream(getFileAsString("PlainOutgoingMessage.txt")));
        session.setDebug(true);
        session.setDebugOut(System.out);
        Transport transport = null;
        try {
            transport = session.getTransport("smtp");
            transport.connect();
            transport.sendMessage(originalMsg, originalMsg.getAllRecipients());
        } finally {
            transport.close();
        }
    }
}
