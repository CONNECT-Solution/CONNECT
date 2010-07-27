
package gov.hhs.fha.nhinc.async;

import com.sun.xml.ws.api.message.Header;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamException;
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
     * This case has all parameters provided.
     */
    @Test
    public void testCreateOutboundHeadersFull() {

        String url = "TestUrl";
        String action = "TestAction";
        String messageId = "TestMessageId";
        String relatesToId = "TestRelatesToId_1";
        String addrAnon = "http://www.w3.org/2005/08/addressing/anonymous";
        List<String> relatesToIds = new ArrayList<String>();
        relatesToIds.add(relatesToId);

        AsyncHeaderCreator hdrCreator = new AsyncHeaderCreator();
        List<Header> createdHeaders = hdrCreator.createOutboundHeaders(url, action, messageId, relatesToIds);

        assertNotNull("List of created headers was null", createdHeaders);
        if (createdHeaders != null) {
            assertEquals("Number of headers created is not correct", 5, createdHeaders.size());
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
                    } else if ("RelatesTo".equals(elemTag)) {
                        assertEquals(relatesToId, hdr.getStringContent());
                    } else {
                        fail("Unknown header element tag: " + elemTag);
                    }
                }
            }
        }
    }

    /**
     * Test of createOutboundHeaders method, of class AsyncHeaderCreator.
     * This case has multiple RelatesTo items provided.
     */
    @Test
    public void testCreateOutboundHeadersMultipleRelatesTo() {

        String url = "TestUrl";
        String action = "TestAction";
        String messageId = "TestMessageId";
        String relatesToId1 = "TestRelatesToId_1";
        String relatesToId2 = "TestRelatesToId_2";
        String addrAnon = "http://www.w3.org/2005/08/addressing/anonymous";
        List<String> relatesToIds = new ArrayList<String>();
        relatesToIds.add(relatesToId1);
        relatesToIds.add(relatesToId2);

        AsyncHeaderCreator hdrCreator = new AsyncHeaderCreator();
        List<Header> createdHeaders = hdrCreator.createOutboundHeaders(url, action, messageId, relatesToIds);

        assertNotNull("List of created headers was null", createdHeaders);
        if (createdHeaders != null) {
            assertEquals("Number of headers created is not correct", 6, createdHeaders.size());
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
                    } else if ("RelatesTo".equals(elemTag)) {
                        assertTrue(hdr.getStringContent().startsWith("TestRelatesToId_"));
                    } else {
                        fail("Unknown header element tag: " + elemTag);
                    }
                }
            }
        }
    }

    /**
     * Test of createOutboundHeaders method, of class AsyncHeaderCreator.
     * This case has no RelatesTo items provided.
     */
    @Test
    public void testCreateOutboundHeadersNullRelatesTo() {

        String url = "TestUrl";
        String action = "TestAction";
        String messageId = "TestMessageId";
        String addrAnon = "http://www.w3.org/2005/08/addressing/anonymous";

        AsyncHeaderCreator hdrCreator = new AsyncHeaderCreator();
        List<Header> createdHeaders = hdrCreator.createOutboundHeaders(url, action, messageId, null);

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

    /**
     * Test of createOutboundHeaders method, of class AsyncHeaderCreator.
     * This case has no MessageID header.
     */
    @Test
    public void testCreateOutboundHeadersNullMessageId() {

        String url = "TestUrl";
        String action = "TestAction";
        String relatesToId = "TestRelatesToId_1";
        String addrAnon = "http://www.w3.org/2005/08/addressing/anonymous";
        List<String> relatesToIds = new ArrayList<String>();
        relatesToIds.add(relatesToId);

        AsyncHeaderCreator hdrCreator = new AsyncHeaderCreator();
        List<Header> createdHeaders = hdrCreator.createOutboundHeaders(url, action, null, relatesToIds);

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
                    } else if ("RelatesTo".equals(elemTag)) {
                        assertEquals(relatesToId, hdr.getStringContent());
                    } else {
                        fail("Unknown header element tag: " + elemTag);
                    }
                }
            }
        }
    }

    /**
     * Test of createOutboundHeaders method, of class AsyncHeaderCreator.
     * This case has no action provided.
     */
    @Test
    public void testCreateOutboundHeadersNullAction() {

        String url = "TestUrl";
        String messageId = "TestMessageId";
        String relatesToId = "TestRelatesToId_1";
        String addrAnon = "http://www.w3.org/2005/08/addressing/anonymous";
        List<String> relatesToIds = new ArrayList<String>();
        relatesToIds.add(relatesToId);

        AsyncHeaderCreator hdrCreator = new AsyncHeaderCreator();
        List<Header> createdHeaders = hdrCreator.createOutboundHeaders(url, null, messageId, relatesToIds);

        assertNotNull("List of created headers was null", createdHeaders);
        if (createdHeaders != null) {
            assertEquals("Number of headers created is not correct", 5, createdHeaders.size());
            for (Header hdr : createdHeaders) {
                assertNotNull("Created header was null", hdr);
                if (hdr != null) {
                    String desiredNS = "http://www.w3.org/2005/08/addressing";
                    assertEquals("Every header should be in the addressing namespace ", desiredNS, hdr.getNamespaceURI());
                    String elemTag = hdr.getLocalPart();
                    if ("To".equals(elemTag)) {
                        assertEquals(url, hdr.getStringContent());
                    } else if ("Action".equals(elemTag)) {
                        try {
                            assertFalse(hdr.readHeader().hasText());
                        } catch (XMLStreamException ex) {
                            fail("Action element has unknown form " + ex.getMessage());
                        }
                    } else if ("ReplyTo".equals(elemTag)) {
                        assertEquals(addrAnon, hdr.getStringContent());
                    } else if ("MessageID".equals(elemTag)) {
                        assertEquals(messageId, hdr.getStringContent());
                    } else if ("RelatesTo".equals(elemTag)) {
                        assertEquals(relatesToId, hdr.getStringContent());
                    } else {
                        fail("Unknown header element tag: " + elemTag);
                    }
                }
            }
        }
    }

    /**
     * Test of createOutboundHeaders method, of class AsyncHeaderCreator.
     * This case has no endpoint url provided.
     */
    @Test
    public void testCreateOutboundHeadersNullURL() {

        String action = "TestAction";
        String messageId = "TestMessageId";
        String relatesToId = "TestRelatesToId_1";
        String addrAnon = "http://www.w3.org/2005/08/addressing/anonymous";
        List<String> relatesToIds = new ArrayList<String>();
        relatesToIds.add(relatesToId);

        AsyncHeaderCreator hdrCreator = new AsyncHeaderCreator();
        List<Header> createdHeaders = hdrCreator.createOutboundHeaders(null, action, messageId, relatesToIds);

        assertNotNull("List of created headers was null", createdHeaders);
        if (createdHeaders != null) {
            assertEquals("Number of headers created is not correct", 5, createdHeaders.size());
            for (Header hdr : createdHeaders) {
                assertNotNull("Created header was null", hdr);
                if (hdr != null) {
                    String desiredNS = "http://www.w3.org/2005/08/addressing";
                    assertEquals("Every header should be in the addressing namespace ", desiredNS, hdr.getNamespaceURI());
                    String elemTag = hdr.getLocalPart();
                    if ("To".equals(elemTag)) {
                        try {
                            assertFalse(hdr.readHeader().hasText());
                        } catch (XMLStreamException ex) {
                            fail("To element has unknown form " + ex.getMessage());
                        }
                    } else if ("Action".equals(elemTag)) {
                        assertEquals(action, hdr.getStringContent());
                    } else if ("ReplyTo".equals(elemTag)) {
                        assertEquals(addrAnon, hdr.getStringContent());
                    } else if ("MessageID".equals(elemTag)) {
                        assertEquals(messageId, hdr.getStringContent());
                    } else if ("RelatesTo".equals(elemTag)) {
                        assertEquals(relatesToId, hdr.getStringContent());
                    } else {
                        fail("Unknown header element tag: " + elemTag);
                    }
                }
            }
        }
    }
}
