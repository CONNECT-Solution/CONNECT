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
package gov.hhs.fha.nhinc.mail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Test {@link SmtpMailSender}.
 */
public class SmtpMailSenderTest extends GreenMailTest {

    private static final int NUMBER_OF_MSGS = 3;

    /**
     * Test {@link SmtpMailSender#send(Address[], javax.mail.internet.MimeMessage)}. Ignore this test until we upgrade
     * to Javamail 1.4.5 - upgrading solves the problem here (AssertionError) but introduces others (breaks the
     * MimeMessageBuilder in DirectCore).
     */
    @Test
    @Ignore
    public void canSendMultipleMessages() {

        SmtpMailSender testMailSender = getTestSmtpMailSender();
        try {
            Address[] recips = new Address[] {new InternetAddress(EMAIL_RECIP) };
            for (int i = 0; i < NUMBER_OF_MSGS; i++) {
                testMailSender.send(recips, getMimeMessage(MESSAGE_FILEPATH));
            }
            waitForIncomingMsg(NUMBER_OF_MSGS);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertEquals(NUMBER_OF_MSGS + " expected on server.", NUMBER_OF_MSGS, countRemainingMsgs());
    }

    private SmtpMailSender getTestSmtpMailSender() {
        Properties testMailProps = getTestMailServerProperties(true);
        return new SmtpMailSender(testMailProps);
    }

}
