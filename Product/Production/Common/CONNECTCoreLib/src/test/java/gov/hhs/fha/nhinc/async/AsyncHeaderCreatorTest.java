
package gov.hhs.fha.nhinc.async;

import com.sun.xml.ws.api.message.Header;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class is used to test the AsyncHeaderCreator class
 */
public class AsyncHeaderCreatorTest {

    /**
     * Default constructor
     */
    public AsyncHeaderCreatorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of createOutboundHeaders method, of class AsyncHeaderCreator.
     */
    @Test
    public void testCreateOutboundHeaders() {

        String url = "TestUrl";
        String action = "TestAction";
        String messageId = "TestMessageId";
        String addrAnon = "http://www.w3.org/2005/08/addressing/anonymous";

        AsyncHeaderCreator hdrCreator = new AsyncHeaderCreator();
        List<Header> createdHeaders = hdrCreator.createOutboundHeaders(url, action, messageId);

        assertNotNull("List of created headers was null", createdHeaders);
        if (createdHeaders != null) {
            assertEquals("Number of headers created is not correct", 4, createdHeaders.size());
            for (Header hdr : createdHeaders) {
                assertNotNull("Created header was null", hdr);
                if (hdr != null) {
                    String desiredNS = "http://www.w3.org/2005/08/addressing";
                    assertEquals("Every header should be in the addressing namespace ", desiredNS, hdr.getNamespaceURI());
                    String elemTag = hdr.getLocalPart();
                    if ("To".equals(elemTag)) {
                        assertEquals(url, hdr.getStringContent());
                    } else if ("Action".equals(elemTag)) {
                        assertEquals(action, hdr.getStringContent());
                    } else if ("ReplyTo".equals(elemTag)) {
                        assertEquals(addrAnon, hdr.getStringContent());
                    } else if ("MessageID".equals(elemTag)) {
                        assertEquals(messageId, hdr.getStringContent());
                    } else {
                        fail("Unknown header element tag: " + elemTag);
                    }
                }
            }
        }
    }
}
