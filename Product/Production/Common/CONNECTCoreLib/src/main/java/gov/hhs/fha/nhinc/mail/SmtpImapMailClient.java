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
package gov.hhs.fha.nhinc.mail;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

/**
 * Javamail Client that uses SMTP and IMAP to talk to send and receive messages with a specific mail server.
 */
public class SmtpImapMailClient implements MailClient {

    private static final Logger LOG = Logger.getLogger(SmtpImapMailClient.class);
    
    private static final int IMAP_MSG_INDEX_START = 1;
    private static final String DEF_NUM_MSGS_TO_HANDLE = "25";
    
    private final Properties mailServerProps;
    private final Session mailSession;
    private int handlerInvocations = 0;
    

    /**
     * @param mailServerProps.
     */
    public SmtpImapMailClient(Properties mailServerProps) {
        super();
        this.mailServerProps = mailServerProps;
        this.mailSession = createMailSession(mailServerProps);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void send(Address[] recipients, MimeMessage message) throws MailClientException {
        try {
            MailUtils.sendMessage(recipients, mailSession, message);
        } catch (MessagingException e) {
            throw new MailClientException("Exception while sending message.", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int handleMessages(MessageHandler handler) throws MailClientException {

        int numberOfMsgsHandled = 0;
        handlerInvocations++;
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("handleMessages() invoked, (" + this.hashCode() + " : " + Thread.currentThread().getId() + "), ["
                    + mailServerProps.getProperty("mail.imaps.host") + "], handler: "
                    + handler.getClass().getName() + ", invocation count: " + handlerInvocations);
        } else {
            LOG.info("handleMessages() invoked");            
        }
        
        Store store = getImapsStore();
        Folder inbox = getInbox(store);
        Message[] messages = getMessages(store, inbox);
        for (Message message : messages) {
            if ((message instanceof MimeMessage)) {                
                if (handleMessage(handler, (MimeMessage) message)) {
                    numberOfMsgsHandled++;
                }
            }   
        }   
        LOG.info("Handled " + numberOfMsgsHandled + " messages.");

        MailUtils.closeQuietly(store, inbox, MailUtils.FOLDER_EXPUNGE_INBOX_TRUE);
        return numberOfMsgsHandled;
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public Session getMailSession() {
        return mailSession;
    }

    /**
     * @return the handlerInvocations
     */
    @Override
    public int getHandlerInvocations() {
        return handlerInvocations;
    }

    private boolean handleMessage(MessageHandler handler, MimeMessage message) {

        MailUtils.logHeaders(message);

        boolean handled = false;
        if (handler.handleMessage(message)) {
            MailUtils.setDeletedQuietly(message);            
            handled = true;
        } else if (isDeleteUnhandledMsgs()) {
            LOG.warn("Deleting unhandled message (check events and logs for more info)");
            MailUtils.setDeletedQuietly(message);
        }
        return handled;
    }

    private Message[] getMessages(Store store, Folder inbox) throws MailClientException {
        Message[] messages = null;
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
        int maxNumberOfMsgsToHandle = Integer.parseInt(mailServerProps.getProperty("direct.max.msgs.in.batch",
                DEF_NUM_MSGS_TO_HANDLE));

        return numberOfMsgsInFolder < maxNumberOfMsgsToHandle ? numberOfMsgsInFolder : maxNumberOfMsgsToHandle;
    }
    
    private Folder getInbox(Store store) throws MailClientException {
        Folder inbox = null;
        connectToStore(store);
        try {
            inbox = store.getFolder(MailUtils.FOLDER_NAME_INBOX);
            inbox.open(Folder.READ_WRITE);
        } catch (MessagingException e) {
            MailUtils.closeQuietly(store);
            throw new MailClientException("Could not open " + MailUtils.FOLDER_NAME_INBOX + " for READ_WRITE", e);
        }
        return inbox;
    }
    
    private void connectToStore(Store store) throws MailClientException {
        try {
            store.connect();
        } catch (MessagingException e) {
            MailUtils.closeQuietly(store);
            throw new MailClientException("Could not connect to imaps mail store", e);
        }        
    }
    
    private Store getImapsStore() throws MailClientException {
        try {
            return mailSession.getStore("imaps");
        } catch (NoSuchProviderException e) {
            throw new MailClientException("Exception getting imaps store from session", e);
        }
    }
    
    private Session createMailSession(Properties mailServerProps) {        
        Session mailSession = MailUtils.getMailSession(mailServerProps, mailServerProps.getProperty("direct.mail.user"),
                mailServerProps.getProperty("direct.mail.pass"));
        mailSession.setDebug(Boolean.parseBoolean(mailServerProps.getProperty("direct.mail.session.debug")));
        mailSession.setDebugOut(System.out);
        return mailSession;
    }
    
    private boolean isDeleteUnhandledMsgs() {
        return Boolean.parseBoolean(mailServerProps.getProperty("connect.delete.unhandled.msgs"));
    }    


}
