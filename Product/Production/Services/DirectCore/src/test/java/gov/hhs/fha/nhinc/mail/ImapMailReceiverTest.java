/**
 * 
 */
package gov.hhs.fha.nhinc.mail;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import javax.mail.MessagingException;

import java.util.Properties;

import org.junit.Test;


/**
 * @author achidambaram
 *
 */
public class ImapMailReceiverTest {
    
    private static final String EXCEPTION_MSG = "Error Message from Messaging service";
    
    Folder mockFolder = mock(Folder.class);
    Store mockStore = mock(Store.class);
    Properties properties = mock(Properties.class);
    MessageHandler handler = mock(MessageHandler.class);
    MimeMessage message = mock(MimeMessage.class);
    Message messageNotMIme = mock(Message.class);
    

    @Test
    public void testHandleMessages() throws MailClientException, MessagingException {
        Message[] msgs  = {message};
        int result = 0;
        when(properties.getProperty("connect.delete.unhandled.msgs")).thenReturn("false");
        when(properties.getProperty("connect.max.msgs.in.batch", "25")).thenReturn("30");
        when(mockStore.getFolder("INBOX")).thenReturn(mockFolder);
        when(mockFolder.getMessageCount()).thenReturn(1);
        when(mockFolder.getMessages(1, 1)).thenReturn(msgs);
        when(handler.handleMessage(message)).thenReturn(true);
        ImapMailReceiver receiver = new ImapMailReceiver(properties) {
            @Override
            protected Folder getInbox(Store store) {
               return mockFolder; 
            }
        };
        result = receiver.handleMessages(handler);
        assertEquals(result,1);
    }
    
    @Test
    public void testHandleMessagesFailureCase() throws MailClientException, MessagingException {
        Message[] msgs  = {message};
        int result = 0;
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
        };
        result = receiver.handleMessages(handler);
        assertEquals(result,0);
    }
    
    @Test
    public void testHandleMessagesWhenMessageNotMIME() throws MailClientException, MessagingException {
        Message[] msgs  = {messageNotMIme};
        int result = 0;
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
        };
        result = receiver.handleMessages(handler);
        assertEquals(result,0);
    }
    
    @Test
    public void testHandleMessagesWhendeleteMsgs() throws MailClientException, MessagingException {
        Message[] msgs  = {message};
        int result = 0;
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
        };
        result = receiver.handleMessages(handler);
        assertEquals(result,0);
    }
    
}
