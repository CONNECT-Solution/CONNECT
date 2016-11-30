/**
 *
 */
package gov.hhs.fha.nhinc.messaging.client.interceptor;

import org.junit.Assert;

import org.apache.cxf.transports.http.configuration.ConnectionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.List;
import java.util.Map;
import org.apache.cxf.binding.soap.Soap11;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.message.Message;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author mpnguyen
 *
 */
public class HeaderRequestOutInterceptorTest {
    private HeaderRequestOutInterceptor headerRequestInterceptor;
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        headerRequestInterceptor = new HeaderRequestOutInterceptor();
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        headerRequestInterceptor = null;
    }

    /**
     * Test method for {@link gov.hhs.fha.nhinc.messaging.client.interceptor.HeaderRequestOutInterceptor#handleMessage(org.apache.cxf.message.Message)}.
     */
    @Test
    public void testHandleMessage() {
        final int duration = 20;
        final Message message = new SoapMessage(Soap11.getInstance());
        message.put(NhincConstants.KEEPALIVE_DURATION, duration);
        headerRequestInterceptor.handleMessage(message);
        final Map<String,List>headers = (Map<String, List>)message.get(Message.PROTOCOL_HEADERS);
        final List<String> result = headers.get(ConnectionType.KEEP_ALIVE.value());
        final String test = result.get(0);
        Assert.assertEquals("timeout=" + duration, result.get(0));
    }

    @Test
    public void testHandleMissingDuration() {
        final int duration = 20;
        final Message message = new SoapMessage(Soap11.getInstance());
        headerRequestInterceptor.handleMessage(message);
        final Map<String, List> headers = (Map<String, List>) message.get(Message.PROTOCOL_HEADERS);
        Assert.assertNull(headers);
    }

}
