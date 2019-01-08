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
package gov.hhs.fha.nhinc.direct;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.direct.event.DirectEventLogger;
import gov.hhs.fha.nhinc.mail.MailClientException;
import gov.hhs.fha.nhinc.mail.MailSender;
import java.util.HashSet;
import java.util.Set;
import javax.mail.Address;
import javax.mail.internet.MimeMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.nhindirect.gateway.smtp.SmtpAgent;
import org.nhindirect.xd.common.DirectDocuments;

/**
 *
 * @author svalluripalli
 */
public class DirectSenderImplTest extends DirectBaseTest {

    @Mock
    private MailSender mockMailSender;
    @Mock
    private SmtpAgent mockSmtpAgent;
    @Mock
    private MimeMessage mockMessage;
    private DirectSenderImpl oDirectSenderImpl;
    private final DirectEventLogger testLogger = DirectEventLogger.getInstance();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        oDirectSenderImpl = new DirectSenderImpl(mockMailSender, testLogger) {
            @Override
            protected MailSender getExternalMailSender() {
                return mockMailSender;
            }

            //igonre it for now. Need to come up with better unit tests for this
            @Override
            protected void addOutgoingMessage(MimeMessage message, boolean failed) {
            }
        };
    }

    @After
    public void tearDown() {
        oDirectSenderImpl = null;
    }

    /**
     * Test of sendOutboundDirect method, of class DirectSenderImpl.
     */
    //@Test(expected = DirectException.class)
    public void testSendOutboundDirect_MimeMessage() throws MailClientException {
        Address mockAddress = mock(Address.class);
        Set<Address> sAddress = new HashSet<>();
        sAddress.add(mockAddress);
        Address[] recipients = sAddress.toArray(new Address[0]);
        oDirectSenderImpl.sendOutboundDirect(mockMessage);
    }

    /**
     * Test of sendOutboundDirect method, of class DirectSenderImpl.
     */
    @Test(expected = DirectException.class)
    public void testSendOutboundDirect_4args() {
        System.out.println("sendOutboundDirect");
        Address sender = mock(Address.class);
        Address mockAddress1 = mock(Address.class);
        Set<Address> toAddresses = new HashSet<>();
        toAddresses.add(mockAddress1);
        Address[] recipients = toAddresses.toArray(new Address[0]);
        DirectDocuments documents = mock(DirectDocuments.class);
        MimeMessageBuilder mockMMBuilder = mock(MimeMessageBuilder.class);
        String messageId = "";
        when(mockMMBuilder.build()).thenReturn(mockMessage);
        oDirectSenderImpl.sendOutboundDirect(sender, recipients, documents, messageId);
    }
}
