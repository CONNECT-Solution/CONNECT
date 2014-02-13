/**
 * 
 */
package gov.hhs.fha.nhinc.mail;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

/**
 * @author achidambaram
 * 
 */
public class AbstractMailPollerTest {

    private static final String EXCEPTION_MSG = "Error Message from MailClient";

    MailReceiver mailReceiver = mock(MailReceiver.class);
    MessageHandler messageHandler = mock(MessageHandler.class);

    @Test
    public void testPollMethodCanInvokeHandler() throws MailClientException {
        MailClientException mockException = mock(MailClientException.class);
        when(mockException.getMessage()).thenReturn(EXCEPTION_MSG);

        AbstractMailPoller poller = new AbstractMailPoller(mailReceiver, messageHandler) {

            @Override
            public void handleException(MailClientException e) {
                assertEquals(EXCEPTION_MSG, e.getMessage());
            }

        };

        poller.poll();
        verify(mailReceiver).handleMessages(messageHandler);
        verify(mockException, times(0)).getMessage();
    }

    @Test
    public void testPollMethodCanHandleException() throws MailClientException {

        MailClientException mockException = mock(MailClientException.class);
        when(mockException.getMessage()).thenReturn(EXCEPTION_MSG);
        when(mailReceiver.handleMessages(messageHandler)).thenThrow(mockException);

        AbstractMailPoller poller = new AbstractMailPoller(mailReceiver, messageHandler) {

            @Override
            public void handleException(MailClientException e) {
                assertEquals(EXCEPTION_MSG, e.getMessage());
            }

        };

        poller.poll();
        verify(mailReceiver).handleMessages(messageHandler);
        verify(mockException).getMessage();

    }

}
