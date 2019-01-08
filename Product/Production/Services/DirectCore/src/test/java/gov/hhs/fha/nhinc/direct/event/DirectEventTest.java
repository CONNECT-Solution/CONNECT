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
package gov.hhs.fha.nhinc.direct.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.direct.DirectBaseTest;
import gov.hhs.fha.nhinc.direct.DirectUnitTestUtil;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.junit.Before;
import org.junit.Test;

/**
 * Test {@link DirectEvent}.
 */
public class DirectEventTest extends DirectBaseTest {

    private static final String MSG_ID = "123456789";
    private static final String ERROR_MSG = "Something wicked this way comes.";

    /**
     * Mocked mime message.
     */
    protected MimeMessage mockMimeMessage = mock(MimeMessage.class);

    /**
     * @throws MessagingException on failure.
     */
    @Before
    public void setUp() throws MessagingException {
        when(mockMimeMessage.getMessageID()).thenReturn(MSG_ID);
        when(mockMimeMessage.getSender()).thenReturn(new InternetAddress(DirectUnitTestUtil.SENDER_AT_INITIATING_GW));
        when(mockMimeMessage.getAllRecipients()).thenReturn(
                new Address[] {new InternetAddress(DirectUnitTestUtil.RECIP_AT_RESPONDING_GW) });
    }

    /**
     * Test that we can create a direct event.
     */
    @Test
    public void canCreateDirectEvent() {
        DirectEvent testDirectEvent = new DirectEvent.Builder().mimeMessage(mockMimeMessage).build(
                DirectEventType.BEGIN_OUTBOUND_DIRECT);
        verifyEventFields(testDirectEvent, "success", null);
    }

    /**
     * Test that we can create a direct failure event.
     */
    @Test
    public void canCreateFailedDirectEvent() {
        DirectEvent testDirectEvent = new DirectEvent.Builder().mimeMessage(mockMimeMessage).errorMsg(ERROR_MSG)
                .build(DirectEventType.BEGIN_OUTBOUND_DIRECT);
        verifyEventFields(testDirectEvent, "error", ERROR_MSG);
    }

    private void verifyEventFields(DirectEvent event, String status, String errorMsg) {
        String eventName = DirectEventType.BEGIN_OUTBOUND_DIRECT.toString();
        assertEquals(eventName, event.getEventName());
        assertEquals(MSG_ID, event.getMessageID());
        assertJsonStrContains(event.getDescription(), "\"action\":\"" + eventName);
        assertJsonStrContains(event.getDescription(), "\"message_id\":\"" + MSG_ID);
        assertJsonStrContains(event.getDescription(), "\"timestamp\":\"");
        assertJsonStrContains(event.getDescription(), "\"sender\":\"" + DirectUnitTestUtil.SENDER_AT_INITIATING_GW);
        assertJsonStrContains(event.getDescription(), "\"recipient\":[\"" + DirectUnitTestUtil.RECIP_AT_RESPONDING_GW);
        assertJsonStrContains(event.getDescription(), "\"statuses\":[\"" + status);
        if (errorMsg != null) {
            assertJsonStrContains(event.getDescription(), "\"error_msg\":\"" + errorMsg);
        }
    }

    private void assertJsonStrContains(String jsonStr, String str) {
        assertTrue("JSON [" + jsonStr + "] contains: " + str, jsonStr.contains(str));
    }

}
