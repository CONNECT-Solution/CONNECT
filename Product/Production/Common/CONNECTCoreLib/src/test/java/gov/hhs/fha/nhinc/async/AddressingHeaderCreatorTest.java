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
package gov.hhs.fha.nhinc.async;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.xml.ws.api.message.Header;

/**
 * This class is used to test the AsyncHeaderCreator class
 */
public class AddressingHeaderCreatorTest {

    /**
     * Default constructor
     */
    public AddressingHeaderCreatorTest() {
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
     * Test of createOutboundHeaders method, of class AsyncHeaderCreator. This case has all parameters provided.
     */
    @Test
    public void testCreateOutboundHeadersFull() {

        String url = "TestUrl";
        String action = "TestAction";
        String messageId = "TestMessageId";
        String relatesToId = "TestRelatesToId_1";
        String addrAnon = "http://www.w3.org/2005/08/addressing/anonymous";
        List<String> relatesToIds = new ArrayList<String>();
        final String messageId_prefix = "urn:uuid:";
        relatesToIds.add(relatesToId);

        AddressingHeaderCreator hdrCreator = new AddressingHeaderCreator(url, action, messageId, relatesToIds);
        List<Header> createdHeaders = hdrCreator.build();

        assertNotNull("List of created headers was null", createdHeaders);
        if (createdHeaders != null) {
            assertEquals("Number of headers created is not correct", 5, createdHeaders.size());
            for (Header hdr : createdHeaders) {
                assertNotNull("Created header was null", hdr);
                if (hdr != null) {
                    String desiredNS = "http://www.w3.org/2005/08/addressing";
                    assertEquals("Every header should be in the addressing namespace ", desiredNS,
                            hdr.getNamespaceURI());
                    String elemTag = hdr.getLocalPart();
                    if ("To".equals(elemTag)) {
                        assertEquals(url, hdr.getStringContent());
                    } else if ("Action".equals(elemTag)) {
                        assertEquals(action, hdr.getStringContent());
                    } else if ("ReplyTo".equals(elemTag)) {
                        assertEquals(addrAnon, hdr.getStringContent());
                    } else if ("MessageID".equals(elemTag)) {
                        assertFalse(messageId == hdr.getStringContent());
                        assertEquals(messageId_prefix + messageId, hdr.getStringContent());
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
     * Test of createOutboundHeaders method, of class AsyncHeaderCreator. This case has multiple RelatesTo items
     * provided.
     * @throws FactoryConfigurationError 
     * @throws XMLStreamException 
     */
    @Test
    public void testCreateOutboundHeadersMultipleRelatesTo() throws XMLStreamException, FactoryConfigurationError {

        String url = "TestUrl";
        String action = "TestAction";
        String messageId = "TestMessageId";
        String relatesToId1 = "TestRelatesToId_1";
        String relatesToId2 = "TestRelatesToId_2";
        String addrAnon = "http://www.w3.org/2005/08/addressing/anonymous";
        final String messageId_prefix = "urn:uuid:";
        List<String> relatesToIds = new ArrayList<String>();
        relatesToIds.add(relatesToId1);
        relatesToIds.add(relatesToId2);

        AddressingHeaderCreator hdrCreator = new AddressingHeaderCreator(url, action, messageId, relatesToIds);
        List<Header> createdHeaders = hdrCreator.build();
    
        assertNotNull("List of created headers was null", createdHeaders);
        if (createdHeaders != null) {
            assertEquals("Number of headers created is not correct", 6, createdHeaders.size());
            for (Header hdr : createdHeaders) {
            	
	            assertNotNull("Created header was null", hdr);
                if (hdr != null) {
                    String desiredNS = "http://www.w3.org/2005/08/addressing";
                    assertEquals("Every header should be in the addressing namespace ", desiredNS,
                            hdr.getNamespaceURI());
                    String elemTag = hdr.getLocalPart();
                    if ("To".equals(elemTag)) {
                        assertEquals(url, hdr.getStringContent());
                    } else if ("Action".equals(elemTag)) {
                        assertEquals(action, hdr.getStringContent());
                    } else if ("ReplyTo".equals(elemTag)) {
                        assertEquals(addrAnon, hdr.getStringContent());
                    } else if ("MessageID".equals(elemTag)) {
                        assertFalse(messageId == hdr.getStringContent());
                        assertEquals(messageId_prefix + messageId, hdr.getStringContent());
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
     * Test of createOutboundHeaders method, of class AsyncHeaderCreator. This case has no RelatesTo items provided.
     */
    @Test
    public void testCreateOutboundHeadersNullRelatesTo() {

        String url = "TestUrl";
        String action = "TestAction";
        String messageId = "TestMessageId";
        final String messageId_prefix = "urn:uuid:";
        String addrAnon = "http://www.w3.org/2005/08/addressing/anonymous";

        AddressingHeaderCreator hdrCreator = new AddressingHeaderCreator(url, action, messageId, null);
        List<Header> createdHeaders = hdrCreator.build();

        assertNotNull("List of created headers was null", createdHeaders);
        if (createdHeaders != null) {
            assertEquals("Number of headers created is not correct", 4, createdHeaders.size());
            for (Header hdr : createdHeaders) {
                assertNotNull("Created header was null", hdr);
                if (hdr != null) {
                    String desiredNS = "http://www.w3.org/2005/08/addressing";
                    assertEquals("Every header should be in the addressing namespace ", desiredNS,
                            hdr.getNamespaceURI());
                    String elemTag = hdr.getLocalPart();
                    if ("To".equals(elemTag)) {
                        assertEquals(url, hdr.getStringContent());
                    } else if ("Action".equals(elemTag)) {
                        assertEquals(action, hdr.getStringContent());
                    } else if ("ReplyTo".equals(elemTag)) {
                        assertEquals(addrAnon, hdr.getStringContent());
                    } else if ("MessageID".equals(elemTag)) {
                        assertFalse(messageId == hdr.getStringContent());
                        assertEquals(messageId_prefix + messageId, hdr.getStringContent());
                    } else {
                        fail("Unknown header element tag: " + elemTag);
                    }
                }
            }
        }
    }

    /**
     * Test of createOutboundHeaders method, of class AsyncHeaderCreator. This case has no MessageID header.
     */
    @Test
    public void testCreateOutboundHeadersNullMessageId() {

        String url = "TestUrl";
        String action = "TestAction";
        String relatesToId = "TestRelatesToId_1";
        String addrAnon = "http://www.w3.org/2005/08/addressing/anonymous";
        List<String> relatesToIds = new ArrayList<String>();
        relatesToIds.add(relatesToId);

        AddressingHeaderCreator hdrCreator = new AddressingHeaderCreator(url, action, null, relatesToIds);
        List<Header> createdHeaders = hdrCreator.build();

        assertNotNull("List of created headers was null", createdHeaders);
        if (createdHeaders != null) {
            assertEquals("Number of headers created is not correct", 4, createdHeaders.size());
            for (Header hdr : createdHeaders) {
                assertNotNull("Created header was null", hdr);
                if (hdr != null) {
                    String desiredNS = "http://www.w3.org/2005/08/addressing";
                    assertEquals("Every header should be in the addressing namespace ", desiredNS,
                            hdr.getNamespaceURI());
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
     * Test of createOutboundHeaders method, of class AsyncHeaderCreator. This case has no action provided.
     */
    @Test
    public void testCreateOutboundHeadersNullAction() {

        String url = "TestUrl";
        String messageId = "TestMessageId";
        final String messageId_prefix = "urn:uuid:";
        String relatesToId = "TestRelatesToId_1";
        String addrAnon = "http://www.w3.org/2005/08/addressing/anonymous";
        List<String> relatesToIds = new ArrayList<String>();
        relatesToIds.add(relatesToId);

        AddressingHeaderCreator hdrCreator = new AddressingHeaderCreator(url, null, messageId, relatesToIds);
        List<Header> createdHeaders = hdrCreator.build();

        assertNotNull("List of created headers was null", createdHeaders);
        if (createdHeaders != null) {
            assertEquals("Number of headers created is not correct", 5, createdHeaders.size());
            for (Header hdr : createdHeaders) {
                assertNotNull("Created header was null", hdr);
                if (hdr != null) {
                    String desiredNS = "http://www.w3.org/2005/08/addressing";
                    assertEquals("Every header should be in the addressing namespace ", desiredNS,
                            hdr.getNamespaceURI());
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
                        assertFalse(messageId == hdr.getStringContent());
                        assertEquals(messageId_prefix + messageId, hdr.getStringContent());
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
     * Test of createOutboundHeaders method, of class AsyncHeaderCreator. This case has no endpoint url provided.
     */
    @Test
    public void testCreateOutboundHeadersNullURL() {

        String action = "TestAction";
        String messageId = "TestMessageId";
        final String messageId_prefix = "urn:uuid:";
        String relatesToId = "TestRelatesToId_1";
        String addrAnon = "http://www.w3.org/2005/08/addressing/anonymous";
        List<String> relatesToIds = new ArrayList<String>();
        relatesToIds.add(relatesToId);

        AddressingHeaderCreator hdrCreator = new AddressingHeaderCreator(null, action, messageId, relatesToIds);
        List<Header> createdHeaders = hdrCreator.build();

        assertNotNull("List of created headers was null", createdHeaders);
        if (createdHeaders != null) {
            assertEquals("Number of headers created is not correct", 5, createdHeaders.size());
            for (Header hdr : createdHeaders) {
                assertNotNull("Created header was null", hdr);
                if (hdr != null) {
                    String desiredNS = "http://www.w3.org/2005/08/addressing";
                    assertEquals("Every header should be in the addressing namespace ", desiredNS,
                            hdr.getNamespaceURI());
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
                        assertFalse(messageId == hdr.getStringContent());
                        assertEquals(messageId_prefix + messageId, hdr.getStringContent());
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
