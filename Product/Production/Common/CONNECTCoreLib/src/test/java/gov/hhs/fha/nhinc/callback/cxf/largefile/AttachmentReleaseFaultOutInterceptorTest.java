package gov.hhs.fha.nhinc.callback.cxf.largefile;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.any;

import gov.hhs.fha.nhinc.largefile.LargeFileUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import javax.activation.DataHandler;

import org.apache.cxf.attachment.AttachmentDataSource;
import org.apache.cxf.message.Attachment;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.junit.Test;

public class AttachmentReleaseFaultOutInterceptorTest {

	@Test
	public void testHandleMessage() {
		final LargeFileUtils largeFileUtils = mock(LargeFileUtils.class);
		Message message = mock(Message.class);
		Exchange exchange = mock(Exchange.class);
		Collection<Attachment> attachments = new ArrayList<Attachment>();
		Attachment attachment = mock(Attachment.class);
		attachments.add(attachment);
		DataHandler dataHandler = mock(DataHandler.class);
		AttachmentDataSource dataSource = mock(AttachmentDataSource.class);

		AttachmentReleaseFaultOutInterceptor interceptor = new AttachmentReleaseFaultOutInterceptor() {
			@Override
			protected LargeFileUtils getLargeFileUtils() {
				return largeFileUtils;
			}
		};

		when(message.getExchange()).thenReturn(exchange);
		when(exchange.getInMessage()).thenReturn(message);
		when(message.getAttachments()).thenReturn(attachments);

		when(attachment.getDataHandler()).thenReturn(dataHandler);
		when(dataHandler.getDataSource()).thenReturn(dataSource);

		interceptor.handleMessage(message);

		verify(dataSource).release();
		verify(largeFileUtils).closeStreamWithoutException(
				any(InputStream.class));
	}

	@Test
	public void testGetLargeFileUtils() {
		AttachmentReleaseFaultOutInterceptor interceptor = new AttachmentReleaseFaultOutInterceptor();
		assertTrue(interceptor.getLargeFileUtils() instanceof LargeFileUtils);
	}
}
