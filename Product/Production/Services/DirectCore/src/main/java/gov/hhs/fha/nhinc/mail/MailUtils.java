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

import java.util.Enumeration;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility methods to help with Java Mail.
 */
public class MailUtils {

    private static final Logger LOG = LoggerFactory.getLogger(MailUtils.class);

    /**
     * Folder Name for "Inbox".
     */
    public static final String FOLDER_NAME_INBOX = "Inbox";
    /**
     * boolean flag when we do want to expunge the inbox.
     */
    public static final boolean FOLDER_EXPUNGE_INBOX_TRUE = true;
    /**
     * boolean flag when we don't want to expunge the inbox.
     */
    public static final boolean FOLDER_EXPUNGE_INBOX_FALSE = false;

    private MailUtils() {
    }

    /**
     * Close the java mail store and folder, specifying whether deleted messages should be expunged. Log exceptions.
     *
     * @param store to be closed.
     * @param folder to be closed.
     * @param expunge true if deleted messages should be expunged from the folder.
     */
    public static void closeQuietly(Store store, Folder folder, boolean expunge) {
        if (folder != null) {
            try {
                folder.close(expunge);
            } catch (Exception e) {
                LOG.warn("Exception while closing java mail folder, expunge = " + expunge + ".", e);
            }
        }
        closeQuietly(store);
    }

    /**
     * Close the java mail store. Log exceptions.
     *
     * @param store to be closed.
     */
    public static void closeQuietly(Store store) {
        if (store != null) {
            try {
                store.close();
            } catch (Exception e) {
                LOG.warn("Exception while closing java mail store.", e);
            }
        }
    }

    /**
     * Return a mail session using the provided properties, username and password.
     *
     * @param mailServerProps properties for the mail server
     * @param user username credential
     * @param pass password credential
     * @return mail session.
     */
    public static Session getMailSession(Properties mailServerProps, String user, String pass) {
        return Session.getInstance(mailServerProps, getMailAuthenticator(user, pass));
    }

    /**
     * Send a mime message.
     *
     * @param recipients of the mime message.
     * @param session used to send the mime message.
     * @param message to be sent.
     * @throws MessagingException if there is an error.
     */
    public static void sendMessage(Address[] recipients, Session session, MimeMessage message)
        throws MessagingException {

        Transport transport = null;
        try {
            transport = session.getTransport("smtp");
            String host = session.getProperty("mail.smtp.host");
            String port = session.getProperty("mail.smtp.port");
            String user = session.getProperty("connect.mail.user");
            String pass = session.getProperty("connect.mail.pass");
            if (!StringUtils.isBlank(port)) {
                port = StringUtils.trim(port);
            }

            transport.connect(host, Integer.parseInt(port), user, pass);
            transport.sendMessage(message, recipients);
            logHeaders(message);
        } catch (AssertionError e) {
            LOG.error("Assertion Error while sending.", e);
            throw e;
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (MessagingException e) {
                    LOG.error("Exception while closing the transport: {}", e.getMessage(), e);
                }
            }
        }
    }

    /**
     * @param user username login credential
     * @param pass password login credential
     * @return mail Authenticator using credentials
     */
    private static Authenticator getMailAuthenticator(final String user, final String pass) {
        return new Authenticator() {
            /**
             * {@inheritDoc}
             */
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        };
    }

    /**
     * Log message headers.
     *
     * @param mimeMessage to log headers from
     */
    @SuppressWarnings("unchecked")
    public static void logHeaders(final MimeMessage mimeMessage) {
        if (LOG.isDebugEnabled()) {
            Enumeration<String> headerLines = null;
            try {
                headerLines = mimeMessage.getAllHeaderLines();
            } catch (MessagingException e) {
                LOG.error("Could not extract headers: ", e);
            }
            while (headerLines != null && headerLines.hasMoreElements()) {
                LOG.debug(headerLines.nextElement());
            }
        }
    }

    /**
     * Set the deleted flag on a message, log and swallow exceptions. Note: deleted messages must be "expunged" to be
     * removed from server.
     *
     * @param message mime message to be deleted.
     */
    public static void setDeletedQuietly(MimeMessage message) {
        try {
            message.setFlag(Flags.Flag.DELETED, true);
        } catch (MessagingException e) {
            LOG.warn("Exception ", e);
        }
    }
}
