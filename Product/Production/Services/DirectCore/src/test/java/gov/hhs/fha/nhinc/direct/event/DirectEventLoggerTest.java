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

import gov.hhs.fha.nhinc.direct.DirectException;
import org.junit.Test;

/**
 * Test {@link DirectEventLogger}.
 */
public class DirectEventLoggerTest extends DirectEventTest {

    private static final String ERROR_MESSAGE = "I've got blisters on me fingers...";

    private final DirectEventLogger testLogger = DirectEventLogger.getInstance();

    /**
     * {@link DirectEventLogger#log(DirectEventType, javax.mail.internet.MimeMessage)}.
     */
    @Test
    public void canLogSuccess() {
        testLogger.log(DirectEventType.BEGIN_OUTBOUND_DIRECT, mockMimeMessage);
    }

    /**
     * {@link DirectEventLogger#log(DirectEventType, javax.mail.internet.MimeMessage, String)}.
     */
    @Test
    public void canLogError() {
        testLogger.log(DirectEventType.DIRECT_ERROR, mockMimeMessage, ERROR_MESSAGE);
    }

    /**
     * {@link DirectEventLogger#logException(DirectEventType, javax.mail.internet.MimeMessage, Exception)}.
     */
    @Test
    public void canLogException() {
        testLogger.logException(DirectEventType.DIRECT_ERROR, mockMimeMessage, new DirectException(ERROR_MESSAGE));
    }

}
