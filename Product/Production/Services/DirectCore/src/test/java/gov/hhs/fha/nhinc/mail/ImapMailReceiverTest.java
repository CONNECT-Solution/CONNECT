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

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Properties;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import org.junit.Test;

/**
 * @author achidambaram
 *
 */
public class ImapMailReceiverTest {

    private final Folder mockFolder = mock(Folder.class);
    private final Store mockStore = mock(Store.class);
    private final Properties properties = mock(Properties.class);
    private final MessageHandler handler = mock(MessageHandler.class);
    private final MimeMessage message = mock(MimeMessage.class);
    private final Message messageNotMIme = mock(Message.class);

    @Test
    public void testHandleMessages() throws MailClientException, MessagingException {
        Message[] msgs = { message };
        int result;
        when(properties.getProperty("connect.delete.unhandled.msgs")).thenReturn("false");
        when(properties.getProperty("connect.max.msgs.in.batch", "25")).thenReturn("30");
        when(mockStore.getFolder(MailUtils.FOLDER_NAME_INBOX)).thenReturn(mockFolder);
        when(mockFolder.getMessageCount()).thenReturn(1);
        when(mockFolder.getMessages(1, 1)).thenReturn(msgs);
        when(handler.handleMessage(message)).thenReturn(true);
        ImapMailReceiver receiver = new ImapMailReceiver(properties) {
            @Override
            Store getImapsStore() {
                return mockStore;
            }

            @Override
            protected void handleMessageMonitoring() {
                // do nothing
            }
        };
        result = receiver.handleMessages(handler);

        verify(mockStore, times(1)).connect();
        verify(mockStore).getFolder(MailUtils.FOLDER_NAME_INBOX);
        verify(mockFolder).open(Folder.READ_WRITE);

        assertEquals(result, 1);
    }

    @Test(expected = MailClientException.class)
    public void testHandleMessageExceptionOnStoreConnect() throws MessagingException, MailClientException {
        when(properties.getProperty("connect.delete.unhandled.msgs")).thenReturn("false");
        when(properties.getProperty("connect.max.msgs.in.batch", "25")).thenReturn("30");

        when(mockStore.getFolder(anyString())).thenThrow(new MessagingException());

        ImapMailReceiver receiver = new ImapMailReceiver(properties) {
            @Override
            Store getImapsStore() {
                return mockStore;
            }

            @Override
            protected void handleMessageMonitoring() {
                // do nothing
            }
        };

        try {
            receiver.handleMessages(handler);
        } catch (MailClientException ex) {
            verify(mockStore, times(1)).close();
            throw ex;
        }
    }

    @Test
    public void testHandleMessagesFailureCase() throws MailClientException, MessagingException {
        Message[] msgs = { message };
        int result;
        when(properties.getProperty("connect.delete.unhandled.msgs")).thenReturn("false");
        when(properties.getProperty("connect.max.msgs.in.batch", "25")).thenReturn("30");
        when(mockStore.getFolder("INBOX")).thenReturn(mockFolder);
        when(mockFolder.getMessageCount()).thenReturn(1);
        when(mockFolder.getMessages(1, 1)).thenReturn(msgs);
        when(handler.handleMessage(message)).thenReturn(false);
        ImapMailReceiver receiver = new ImapMailReceiver(properties) {
            @Override
            protected Folder getInbox(Store store) {
                return mockFolder;
            }

            @Override
            protected void handleMessageMonitoring() {
                // do nothing
            }
        };
        result = receiver.handleMessages(handler);
        assertEquals(result, 0);
    }

    @Test
    public void testHandleMessagesWhenMessageNotMIME() throws MailClientException, MessagingException {
        Message[] msgs = { messageNotMIme };
        int result;
        when(properties.getProperty("connect.delete.unhandled.msgs")).thenReturn("false");
        when(properties.getProperty("connect.max.msgs.in.batch", "25")).thenReturn("30");
        when(mockStore.getFolder("INBOX")).thenReturn(mockFolder);
        when(mockFolder.getMessageCount()).thenReturn(1);
        when(mockFolder.getMessages(1, 1)).thenReturn(msgs);
        ImapMailReceiver receiver = new ImapMailReceiver(properties) {
            @Override
            protected Folder getInbox(Store store) {
                return mockFolder;
            }

            @Override
            protected void handleMessageMonitoring() {
                // do nothing
            }
        };
        result = receiver.handleMessages(handler);
        assertEquals(result, 0);
    }

    @Test
    public void testHandleMessagesWhendeleteMsgs() throws MailClientException, MessagingException {
        Message[] msgs = { message };
        int result;
        when(properties.getProperty("connect.delete.unhandled.msgs")).thenReturn("true");
        when(properties.getProperty("connect.max.msgs.in.batch", "25")).thenReturn("30");
        when(mockStore.getFolder("INBOX")).thenReturn(mockFolder);
        when(mockFolder.getMessageCount()).thenReturn(1);
        when(mockFolder.getMessages(1, 1)).thenReturn(msgs);
        ImapMailReceiver receiver = new ImapMailReceiver(properties) {
            @Override
            protected Folder getInbox(Store store) {
                return mockFolder;
            }

            @Override
            protected void handleMessageMonitoring() {
                // do nothing for now
            }

        };
        result = receiver.handleMessages(handler);
        assertEquals(result, 0);
        verify(message).setFlag(Flags.Flag.DELETED, true);
    }

}
