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
package gov.hhs.fha.nhinc.callback.cxf.largefile;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.dom.WSConstants;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class CONNECTTimestampTest {

    private static final Element TIMESTAMP_ELEMENT = mock(Element.class);
    private static final String CREATE_TIME = "2013-01-01T01:00:05.000Z";
    private static final String EXPIRE_TIME = "2013-01-01T01:00:10.000Z";
    private static final String BEFORE_EXPIRE_TIME = "2013-01-01T01:00:05.000Z";
    private static final String AFTER_EXPIRE_TIME = "2013-01-01T01:00:15.000Z";

    private static final String INVOCATION_OLD_ERROR_TIME = "2013-01-01T01:00:06.000Z";
    private static final String INVOCATION_FUTURE_ERROR_TIME = "2013-01-01T01:00:02.000Z";
    private static final String INVOCATION_HAPPY_PATH = "2013-01-01T01:00:04.000Z";

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @Before
    public void setUp() {
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Element createdTimeElement = mock(Element.class);
        Element expiresTimeElement = mock(Element.class);
        Node node = null;
        Text dateTextNode = mock(Text.class);

        when(TIMESTAMP_ELEMENT.getFirstChild()).thenReturn(createdTimeElement);
        when(createdTimeElement.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(createdTimeElement.getNextSibling()).thenReturn(expiresTimeElement, node);
        when(expiresTimeElement.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(createdTimeElement.getLocalName()).thenReturn(WSConstants.CREATED_LN);
        when(createdTimeElement.getNamespaceURI()).thenReturn(WSConstants.WSU_NS);
        when(expiresTimeElement.getLocalName()).thenReturn(WSConstants.EXPIRES_LN);
        when(expiresTimeElement.getNamespaceURI()).thenReturn(WSConstants.WSU_NS);

        when(createdTimeElement.getFirstChild()).thenReturn(dateTextNode);
        when(expiresTimeElement.getFirstChild()).thenReturn(dateTextNode);
        // yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
        when(dateTextNode.getData()).thenReturn(CREATE_TIME, EXPIRE_TIME);
    }

    @Test
    public void testIsExpired_False() throws WSSecurityException, ParseException {
        Date invocationDate = dateFormat.parse(BEFORE_EXPIRE_TIME);

        CONNECTTimestamp connectTimestamp = new CONNECTTimestamp(TIMESTAMP_ELEMENT);
        boolean result = connectTimestamp.isExpired(invocationDate);

        assertFalse(result);
    }

    @Test
    public void testIsExpired_True() throws ParseException, WSSecurityException {
        Date invocationDate = dateFormat.parse(AFTER_EXPIRE_TIME);

        CONNECTTimestamp connectTimestamp = new CONNECTTimestamp(TIMESTAMP_ELEMENT);
        ;

        assertTrue(connectTimestamp.isExpired(invocationDate));
    }

    @Test
    public void testVerifyCreated_Pass() throws ParseException, WSSecurityException {
        Date invocationDate = dateFormat.parse(INVOCATION_HAPPY_PATH);

        CONNECTTimestamp connectTimestamp = new CONNECTTimestamp(TIMESTAMP_ELEMENT);

        assertTrue(connectTimestamp.verifyCreated(2, 2, invocationDate));
    }

    @Test
    public void testVerifyCreated_Fail_CreatedTimeInFuture() throws ParseException, WSSecurityException {
        Date invocationDate = dateFormat.parse(INVOCATION_FUTURE_ERROR_TIME);

        CONNECTTimestamp connectTimestamp = new CONNECTTimestamp(TIMESTAMP_ELEMENT);

        assertFalse(connectTimestamp.verifyCreated(2, 2, invocationDate));
    }

    @Test
    public void testVerifyCreated_Fail_CreatedTimeInPast() throws ParseException, WSSecurityException {
        Date invocationDate = dateFormat.parse(INVOCATION_OLD_ERROR_TIME);

        CONNECTTimestamp connectTimestamp = new CONNECTTimestamp(TIMESTAMP_ELEMENT);

        assertFalse(connectTimestamp.verifyCreated(0, 2, invocationDate));
    }
}
