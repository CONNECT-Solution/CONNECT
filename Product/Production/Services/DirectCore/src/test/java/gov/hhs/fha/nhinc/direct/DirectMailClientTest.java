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
package gov.hhs.fha.nhinc.direct;

import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.RECIPIENT;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.SENDER;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.getMailServerProps;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.getMockDocument;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nhindirect.gateway.smtp.MessageProcessResult;
import org.nhindirect.gateway.smtp.SmtpAgent;
import org.nhindirect.stagent.MessageEnvelope;
import org.nhindirect.stagent.NHINDAddress;
import org.nhindirect.stagent.NHINDAddressCollection;
import org.nhindirect.stagent.mail.Message;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.icegreen.greenmail.util.ServerSetupTest;

/**
 * Test {@link DirectMailClient}.
 */
public class DirectMailClientTest {

    private static final String ATTACHMENT_NAME = "mymockattachment";
    
    private Properties mailServerProps;
    private SmtpAgent mockSmtpAgent;
    private DirectMailClient testDirectMailClient;
        
    private GreenMail greenMail;
        
    /**
     * Set up tests.
     */
    @Before
    public void setUp() {
        mockSmtpAgent = mock(SmtpAgent.class);
        greenMail = new GreenMail(new ServerSetup[] {ServerSetupTest.SMTPS, ServerSetupTest.IMAPS});
        greenMail.start();

        mailServerProps = getMailServerProps(greenMail.getSmtps().getServerSetup().getPort(), 
                greenMail.getImaps().getServerSetup().getPort());
        testDirectMailClient = new DirectMailClient(mailServerProps, mockSmtpAgent);
    }
    
    /**
     * Test {@link DirectMailClient#
     * send(String, String, ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document, String)}.
     * @throws InterruptedException 
     * @throws IOException 
     */
    @Test
    public void canSendOneMessage() throws InterruptedException, IOException {        

        MessageProcessResult mockMessageProcessResult = getMockMessageProcessResult();
        when(mockSmtpAgent.processMessage(any(MimeMessage.class), any(NHINDAddressCollection.class),
                any(NHINDAddress.class))).thenReturn(mockMessageProcessResult);

        testDirectMailClient.send(SENDER, RECIPIENT, getMockDocument(), ATTACHMENT_NAME);

        verify(mockSmtpAgent).processMessage(any(MimeMessage.class), any(NHINDAddressCollection.class), 
                any(NHINDAddress.class));
        
        // Retrieve using GreenMail API
        javax.mail.Message[] messages = greenMail.getReceivedMessages();
        assertEquals(1, messages.length);
    }
    
    /**
     * Test {@link DirectMailClient#
     * send(String, String, ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document, String)}.
     * @throws InterruptedException 
     * @throws IOException 
     */
    @Test
    public void canSendAndReceiveOneMessage() throws InterruptedException, IOException {        

        // set up the greenmail user for imaps. Must be done before message is sent.
        greenMail.setUser(DirectUnitTestUtil.RECIPIENT, DirectUnitTestUtil.USER, DirectUnitTestUtil.PASS);

        MessageProcessResult mockMessageProcessResult = getMockMessageProcessResult();
        when(mockSmtpAgent.processMessage(any(MimeMessage.class), any(NHINDAddressCollection.class),
                any(NHINDAddress.class))).thenReturn(mockMessageProcessResult);

        testDirectMailClient.send(SENDER, RECIPIENT, getMockDocument(), ATTACHMENT_NAME);

        verify(mockSmtpAgent).processMessage(any(MimeMessage.class), any(NHINDAddressCollection.class), 
                any(NHINDAddress.class));
        
        MessageHandler mockMessageHandler = mock(MessageHandler.class);
        assertEquals(1, testDirectMailClient.handleMessages(mockMessageHandler));
        
        verify(mockMessageHandler).handleMessage(any(Message.class));        
    }

    /**
     * Tear down after tests run.
     */
    @After
    public void tearDown() {
        if (null != greenMail) {
            greenMail.stop();
        }
    }
        
    private MessageProcessResult getMockMessageProcessResult() {

        MessageProcessResult mockMessageProcessResult = mock(MessageProcessResult.class);
        MessageEnvelope mockMessageEnvelope = mock(MessageEnvelope.class);
        Message mockMessage = mock(Message.class);
        
        when(mockMessageProcessResult.getProcessedMessage()).thenReturn(mockMessageEnvelope);
        when(mockMessageEnvelope.getMessage()).thenReturn(mockMessage);

        return mockMessageProcessResult;
    }
    
}
