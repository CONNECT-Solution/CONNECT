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
package gov.hhs.fha.nhinc.mail;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.mail.Flags;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;

import com.icegreen.greenmail.store.MailFolder;
import com.icegreen.greenmail.store.SimpleStoredMessage;
import com.icegreen.greenmail.user.GreenMailUser;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.icegreen.greenmail.util.ServerSetupTest;

/**
 * Green Mail Test - base class for tests which rely upon GreenMail.
 */
public abstract class GreenMailTest {

    /**
     * Email of Greenmail User Account.
     */
    protected static final String EMAIL = "test@connect.example.org";

    /**
     * Email of Recipient.
     */
    protected static final String EMAIL_RECIP = "recip@connect.example.org";

    /**
     * Number of messages to handle in a multi-batch test.
     */
    protected static final int NUMBER_OF_MSGS = 28;

    /**
     * Number of messages defined for a single batch.
     */
    protected static final int NUMBER_OF_MSGS_IN_BATCH = 5;

    /**
     * A sample mail message resource.
     */
    protected static final String MESSAGE_FILEPATH = "/gov/hhs/fha/nhinc/mail/message.txt";

    /**
     * Timeout value to wait for an incoming SMTP msg.
     */
    protected static final long WAIT_TIME_FOR_SMTP_SENDER = TimeUnit.SECONDS.toMillis(10);

    private GreenMail greenMail;
    private GreenMailUser greenMailUser;

    /**
     * Start greenmail before each test.
     */
    @Before
    public void setUpGreenMail() {
        greenMail = new GreenMail(new ServerSetup[] {ServerSetupTest.SMTP, ServerSetupTest.IMAPS });
        greenMail.start();
        greenMailUser = greenMail.setUser(EMAIL, EMAIL, EMAIL);
    }

    /**
     * Tear down greenmail at the end of each test.
     * @throws InterruptedException on an interrupted exception.
     */
    @After
    public void tearDownGreenMail() throws InterruptedException {
        if (greenMail != null) {
            greenMail.stop();
        }
    }

    /**
     * Wait for the number of messages to be received or timeout. This allows you to send the messages from a separate
     * thread in a test. Otherwise the execution thread might check for the received messages before they actually
     * get there.
     * @param emailCount number of email msgs expected.
     */
    protected void waitForIncomingMsg(int emailCount) {
        try {
            greenMail.waitForIncomingEmail(WAIT_TIME_FOR_SMTP_SENDER, emailCount);
        } catch (InterruptedException e) {
            fail("Thread interrupted waiting for incoming email: " + e.getMessage());
        }
    }

    /**
     * Get mail server properties for a test.
     * @param deleteUnhandledMsgs determines if unhandled messages should be deleted or not.
     * @return populated properties
     */
    protected Properties getTestMailServerProperties(boolean deleteUnhandledMsgs) {
        return getTestMailServerProperties(EMAIL, NUMBER_OF_MSGS_IN_BATCH,
                greenMail.getSmtp().getPort(), greenMail.getImaps().getPort(), "3000", "5000", deleteUnhandledMsgs);
    }

    /**
     * @param email address used as credentials
     * @param maxMsgsInBatch number of messages in each batch.
     * @param smtpPort smtp port
     * @param imapPort imap port
     * @param timeoutConnectionMillis timeout on making the connection
     * @param timeoutMillis timeout on data from the socket
     * @param deleteUnhandledMsgs determines if unhandled messages should be deleted or not.
     * @return populate properties
     */
    protected Properties getTestMailServerProperties(String email, int maxMsgsInBatch, int smtpPort, int imapPort,
            String timeoutConnectionMillis, String timeoutMillis, boolean deleteUnhandledMsgs) {

        Properties props = new Properties();

        props.setProperty("connect.mail.user", email);
        props.setProperty("connect.mail.pass", email);
        props.setProperty("connect.max.msgs.in.batch", Integer.toString(maxMsgsInBatch));
        props.setProperty("connect.delete.unhandled.msgs", deleteUnhandledMsgs ? "true" : "false");
        props.setProperty("connect.mail.session.debug", "true");

        props.setProperty("mail.smtp.host", "localhost");
        props.setProperty("mail.smtp.auth", "TRUE");
        props.setProperty("mail.smtp.port", Integer.toString(smtpPort));
        props.setProperty("mail.smtp.starttls.enabled", "TRUE");

        props.setProperty("mail.imaps.host", "localhost");
        props.setProperty("mail.imaps.port", Integer.toString(imapPort));
        props.setProperty("mail.imaps.connectiontimeout", timeoutConnectionMillis);
        props.setProperty("mail.imaps.timeout", timeoutMillis);

        // this allows us to run the test using a dummy in-memory keystore provided by GreenMail... don't use in prod.
        props.setProperty("mail.smtps.ssl.socketFactory.class", "com.icegreen.greenmail.util.DummySSLSocketFactory");
        props.setProperty("mail.smtps.ssl.socketFactory.port", Integer.toString(smtpPort));
        props.setProperty("mail.smtps.ssl.socketFactory.fallback", "false");
        props.setProperty("mail.imaps.ssl.socketFactory.class", "com.icegreen.greenmail.util.DummySSLSocketFactory");
        props.setProperty("mail.imaps.ssl.socketFactory.port", Integer.toString(imapPort));
        props.setProperty("mail.imaps.ssl.socketFactory.fallback", "false");

        return props;
    }

    /**
     * @param messageFile to be delivered.
     */
    protected void deliverMsg(String messageFile) {
        try {
            MimeMessage originalMsg;
            originalMsg = getMimeMessage(messageFile);
            greenMailUser.deliver(originalMsg);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Get a mime message from a message file.
     * @param messageFile path
     * @return mime message
     */
    protected MimeMessage getMimeMessage(String messageFile) {

        MimeMessage mimeMsg = null;
        try {
            mimeMsg = new MimeMessage(null, IOUtils.toInputStream(MailTestUtil.getFileAsString(messageFile)));
            assertNotNull(mimeMsg);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        return mimeMsg;
    }

    /**
     * @return count of unread messages on the server.
     * @throws MessagingException
     */
    protected int countRemainingMsgs() {
        return greenMail.getReceivedMessages().length;
    }

    /**
     * Workaround for defect in greenmail expunging messages.
     * http://sourceforge.net/tracker/?func=detail&aid=2688036&group_id=159695&atid=812857
     *
     * We have to delete these ourselves...
     */
    protected void expungeMissedMessages() {
        try {
            MailFolder folder = greenMail.getManagers().getImapHostManager()
                    .getFolder(greenMailUser, MailUtils.FOLDER_NAME_INBOX);
            while (folderHasDeletedMsgs(folder)) {
                folder.expunge();
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private boolean folderHasDeletedMsgs(MailFolder folder) throws MessagingException {
        for (Object object : folder.getMessages()) {
            SimpleStoredMessage message = (SimpleStoredMessage) object;
            if (message.getFlags().contains(Flags.Flag.DELETED)) {
                return true;
            }
        }
        return false;
    }

}
