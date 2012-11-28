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

import java.util.Collection;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nhindirect.gateway.smtp.MessageProcessResult;
import org.nhindirect.gateway.smtp.SmtpAgent;
import org.nhindirect.stagent.NHINDAddressCollection;
import org.nhindirect.stagent.mail.notifications.NotificationMessage;
import org.nhindirect.xd.common.DirectDocuments;
import org.springframework.beans.factory.InitializingBean;

/**
 * Mail Server implementation which used the direct libraries to send encrypted mail.
 */
public class DirectMailClient implements DirectClient, InitializingBean {

    private static final Log LOG = LogFactory.getLog(DirectMailClient.class);

    // TODO - Where should these come from?...
    private static final String MSG_SUBJECT = "DIRECT Message";
    private static final String MSG_TEXT = "DIRECT Message body text";
    private static final String DEF_NUM_MSGS_TO_HANDLE = "25";
    private static final int MSG_INDEX_START = 1;

    private final Properties mailServerProps;
    private final SmtpAgent smtpAgent;

    private MessageHandler messageHandler;

    private int handlerInvocations = 0;

    /**
     * Construct a direct mail server with mail server settings.
     * 
     * @param mailServerProps used to define this mail server
     * @param smtpAgent direct smtp agent config file path relative to classpath used to configure SmtpAgent
     * @param message handler for handling messages from the server.
     */
    public DirectMailClient(final Properties mailServerProps, final SmtpAgent smtpAgent) {
        this.mailServerProps = mailServerProps;
        this.smtpAgent = smtpAgent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processAndSend(Address sender, Address[] recipients, DirectDocuments documents, String messageId) {

        Session session = getMailSession();

        MimeMessage mimeMessage = new MimeMessageBuilder(session, sender, recipients).subject(MSG_SUBJECT)
                .text(MSG_TEXT).documents(documents).messageId(messageId).build();

        processAndSend(mimeMessage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processAndSend(MimeMessage message) {
        processAndSend(message, getMailSession());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(MimeMessage message) {
        try {
            MailUtils.sendMessage(message.getAllRecipients(), getMailSession(), message);
        } catch (MessagingException e) {
            throw new DirectException("Exception while sending mime message.", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMdn(MessageProcessResult result) {

        Collection<NotificationMessage> mdnMessages = DirectClientUtils.getMdnMessages(result);
        if (mdnMessages != null) {
            Session session = getMailSession();
            for (NotificationMessage mdnMessage : mdnMessages) {
                processAndSend(mdnMessage, session);
                LOG.info("MDN notification sent.");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int handleMessages() {

        int numberOfMsgsHandled = 0;
        handlerInvocations++;
        LOG.info("handleMessages() invoked [" + mailServerProps.getProperty("mail.imaps.host") + "], handler: "
                + messageHandler.getClass().getName() + ", invocation count: " + handlerInvocations);

        Session session = getMailSession();
        session.setDebug(Boolean.parseBoolean(mailServerProps.getProperty("direct.mail.session.debug")));
        session.setDebugOut(System.out);

        Store store = null;
        try {
            store = session.getStore("imaps");
        } catch (NoSuchProviderException e) {
            throw new DirectException("Exception getting imaps store from session", e);
        }

        try {
            store.connect();
        } catch (MessagingException e) {
            MailUtils.closeQuietly(store);
            throw new DirectException("Could not connect to imaps mail store", e);
        }

        Folder inbox = null;
        try {
            inbox = store.getFolder(MailUtils.FOLDER_NAME_INBOX);
            inbox.open(Folder.READ_WRITE);
        } catch (MessagingException e) {
            MailUtils.closeQuietly(store);
            throw new DirectException("Could not open " + MailUtils.FOLDER_NAME_INBOX + " for READ_WRITE", e);
        }

        Message[] messages = null;
        try {
            messages = inbox.getMessages(MSG_INDEX_START, getNumberOfMsgsToHandle(inbox));
        } catch (MessagingException e) {
            MailUtils.closeQuietly(store, inbox, MailUtils.FOLDER_EXPUNGE_INBOX_FALSE);
            throw new DirectException("Exception while retrieving messages from inbox.", e);
        }

        for (Message message : messages) {
            try {
                if ((message instanceof MimeMessage)) {
                    MailUtils.logHeaders((MimeMessage) message);
                    messageHandler.handleMessage((MimeMessage) message, this);
                    numberOfMsgsHandled++;
                }
                message.setFlag(Flags.Flag.DELETED, true);
            } catch (Exception e) {
                // messages that were handled successfully are removed from the server...
                MailUtils.closeQuietly(store, inbox, MailUtils.FOLDER_EXPUNGE_INBOX_TRUE);
                throw new DirectException("Exception while handling message.", e);
            }
        }
        LOG.info("Handled " + numberOfMsgsHandled + " messages.");

        MailUtils.closeQuietly(store, inbox, MailUtils.FOLDER_EXPUNGE_INBOX_TRUE);
        return numberOfMsgsHandled;
    }

    /**
     * @return the smtpAgent direct smtp agent.
     */
    public SmtpAgent getSmtpAgent() {
        return smtpAgent;
    }

    /**
     * @param messageHandler the messageHandler to set
     */
    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.messageHandler == null) {
            throw new DirectException("DirectMailClient instantiated without setting a message handler property.");
        }
    }

    /**
     * @return the handlerInvocations
     */
    public int getHandlerInvocations() {
        return handlerInvocations;
    }

    private Session getMailSession() {
        return MailUtils.getMailSession(mailServerProps, mailServerProps.getProperty("direct.mail.user"),
                mailServerProps.getProperty("direct.mail.pass"));
    }

    private void processAndSend(MimeMessage mimeMessage, Session session) {

        MessageProcessResult result = processAsDirectMessage(mimeMessage);
        if (null == result || null == result.getProcessedMessage()) {
            throw new DirectException("Message processed by Direct is null.");
        }

        try {
            Address[] recips = mimeMessage.getAllRecipients();
            MailUtils.sendMessage(recips, session, result.getProcessedMessage().getMessage());
        } catch (MessagingException e) {
            throw new DirectException("Could not send message.", e);
        }
    }

    private MessageProcessResult processAsDirectMessage(MimeMessage mimeMessage) {
        try {
            NHINDAddressCollection collection = DirectClientUtils.getNhindRecipients(mimeMessage);
            return smtpAgent.processMessage(mimeMessage, collection, DirectClientUtils.getNhindSender(mimeMessage));
        } catch (MessagingException e) {
            throw new DirectException("Error occurred while extracting addresses.", e);
        }
    }

    /**
     * @param folder used to get message count on the server.
     * @return number of messages we need to handle (whichever number is less).
     * @throws MessagingException if error communicating with mail server.
     */
    private int getNumberOfMsgsToHandle(Folder folder) throws MessagingException {

        int numberOfMsgsInFolder = folder.getMessageCount();
        int maxNumberOfMsgsToHandle = Integer.parseInt(mailServerProps.getProperty("direct.max.msgs.in.batch",
                DEF_NUM_MSGS_TO_HANDLE));

        return numberOfMsgsInFolder < maxNumberOfMsgsToHandle ? numberOfMsgsInFolder : maxNumberOfMsgsToHandle;
    }
}
