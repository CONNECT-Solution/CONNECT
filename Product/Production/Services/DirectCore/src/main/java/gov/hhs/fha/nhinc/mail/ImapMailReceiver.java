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

import gov.hhs.fha.nhinc.direct.messagemonitoring.impl.MessageMonitoringAPI;
import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Uses Imap to receive Javamail messages.
 */
public class ImapMailReceiver extends AbstractMailClient implements MailReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(ImapMailReceiver.class);

    private static final int IMAP_MSG_INDEX_START = 1;
    private static final String DEF_NUM_MSGS_TO_HANDLE = "25";

    private final boolean deleteUnhandledMsgs;
    private final int maxNumberOfMsgsToHandle;
    private final String imapHost;

    private int handlerInvocations = 0;

    /**
     * @param mailServerProps
     */
    public ImapMailReceiver(Properties mailServerProps) {
        super(mailServerProps);
        deleteUnhandledMsgs = Boolean.parseBoolean(mailServerProps.getProperty("connect.delete.unhandled.msgs"));
        maxNumberOfMsgsToHandle = Integer
                .parseInt(mailServerProps.getProperty("connect.max.msgs.in.batch", DEF_NUM_MSGS_TO_HANDLE));
        imapHost = mailServerProps.getProperty("mail.imaps.host");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int handleMessages(MessageHandler handler) throws MailClientException {

        int numberOfMsgsHandled = 0;
        handlerInvocations++;

        if (LOG.isDebugEnabled()) {
            LOG.debug("handleMessages() invoked, (" + hashCode() + " : " + Thread.currentThread().getId() + "), ["
                    + imapHost + "], handler: " + handler.getClass().getName() + ", invocation count: "
                    + handlerInvocations);
        } else {
            LOG.info("handleMessages() invoked");
        }

        Store store = getImapsStore();
        Folder inbox = getInbox(store);
        Message[] messages = getMessages(store, inbox);
        for (Message message : messages) {
            boolean handledSuccessfully = handleMessage(handler, message);
            if (handledSuccessfully) {
                numberOfMsgsHandled++;
            }
        }
        LOG.info("Handled " + numberOfMsgsHandled + " messages.");

        MailUtils.closeQuietly(store, inbox, MailUtils.FOLDER_EXPUNGE_INBOX_TRUE);
        LOG.info("Call the message monitoring check here");
        // call the message monitoring service
        handleMessageMonitoring();

        return numberOfMsgsHandled;
    }

    /**
     * @return the handlerInvocations
     */
    @Override
    public int getHandlerInvocations() {
        return handlerInvocations;
    }

    private boolean handleMessage(MessageHandler handler, Message message) {

        boolean handledSuccessfully = false;
        if (!(message instanceof MimeMessage)) {
            return handledSuccessfully;
        }

        MimeMessage mimeMessage = (MimeMessage) message;

        handledSuccessfully = handler.handleMessage((MimeMessage) message);

        if (handledSuccessfully) {
            MailUtils.setDeletedQuietly(mimeMessage);
        }

        if (!handledSuccessfully && deleteUnhandledMsgs) {
            LOG.warn("Deleting unhandled message (check events and logs for more info)");
            MailUtils.setDeletedQuietly(mimeMessage);
        }

        return handledSuccessfully;
    }

    private Message[] getMessages(Store store, Folder inbox) throws MailClientException {
        Message[] messages;
        try {
            messages = inbox.getMessages(IMAP_MSG_INDEX_START, getNumberOfMsgsToHandle(inbox));
        } catch (MessagingException e) {
            MailUtils.closeQuietly(store, inbox, MailUtils.FOLDER_EXPUNGE_INBOX_FALSE);
            throw new MailClientException("Exception while retrieving messages from inbox.", e);
        }
        return messages;
    }

    private int getNumberOfMsgsToHandle(Folder folder) throws MessagingException {
        int numberOfMsgsInFolder = folder.getMessageCount();
        return numberOfMsgsInFolder < maxNumberOfMsgsToHandle ? numberOfMsgsInFolder : maxNumberOfMsgsToHandle;
    }

    protected Folder getInbox(Store store) throws MailClientException {
        Folder inbox;
        try {
            store.connect();
            inbox = store.getFolder(MailUtils.FOLDER_NAME_INBOX);
            inbox.open(Folder.READ_WRITE);
        } catch (MessagingException e) {
            MailUtils.closeQuietly(store);
            throw new MailClientException(
                    "Could not retrieve opened folder: " + MailUtils.FOLDER_NAME_INBOX + " for READ_WRITE", e);
        }
        return inbox;
    }

    Store getImapsStore() throws MailClientException {
        try {
            return getMailSession().getStore("imaps");
        } catch (NoSuchProviderException e) {
            throw new MailClientException("Exception getting imaps store from session", e);
        }
    }

    protected void handleMessageMonitoring() {
        MessageMonitoringAPI.getInstance().process();
    }
}
