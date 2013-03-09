package gov.hhs.fha.nhinc.callback.cxf.largefile;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;
import org.junit.Test;

public class TimestampInterceptorTest {

	@Test
	public void testHandleMessage() {
		Message message = new MessageImpl();

		Date beforeDate = new Date();

		TimestampInterceptor interceptor = new TimestampInterceptor();
		interceptor.handleMessage(message);

		Date interceptorDate = (Date) message
				.get(interceptor.INVOCATION_TIME_KEY);

		assertNotNull(interceptorDate);
		assertTrue(beforeDate.before(interceptorDate)
				|| beforeDate.equals(interceptorDate));
	}

}
