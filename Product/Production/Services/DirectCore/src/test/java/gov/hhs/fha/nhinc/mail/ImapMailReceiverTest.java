/**
 * 
 */
package gov.hhs.fha.nhinc.mail;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.Message;
import javax.mail.internet.MimeMessage;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;

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
        int result = 0;
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
        };
        result = receiver.handleMessages(handler);
        
        verify(mockStore, times(1)).connect();
        verify(mockStore).getFolder(MailUtils.FOLDER_NAME_INBOX);
        verify(mockFolder).open(Folder.READ_WRITE);
        
        assertEquals(result, 1);
    }
    
    @Test(expected = MailClientException.class)
    public void testHandleMessageExceptionOnStoreConnect() throws MessagingException, MailClientException{
        when(properties.getProperty("connect.delete.unhandled.msgs")).thenReturn("false");
        when(properties.getProperty("connect.max.msgs.in.batch", "25")).thenReturn("30");
        
        when(mockStore.getFolder(anyString())).thenThrow(new MessagingException());
        
        ImapMailReceiver receiver = new ImapMailReceiver(properties) {           
            @Override
            Store getImapsStore() {
                return mockStore;
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
        assertEquals(result, 0);
    }

    @Test
    public void testHandleMessagesWhenMessageNotMIME() throws MailClientException, MessagingException {
        Message[] msgs = { messageNotMIme };
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
        assertEquals(result, 0);
    }

    @Test
    public void testHandleMessagesWhendeleteMsgs() throws MailClientException, MessagingException {
        Message[] msgs = { message };
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
        assertEquals(result, 0);
        verify(message).setFlag(Flags.Flag.DELETED, true);
    }

}
