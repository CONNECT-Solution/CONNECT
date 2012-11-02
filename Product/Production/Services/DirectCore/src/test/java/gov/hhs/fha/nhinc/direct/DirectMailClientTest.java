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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.activation.DataHandler;
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
import com.icegreen.greenmail.util.ServerSetupTest;

/**
 * Test {@link DirectMailClient}.
 */
public class DirectMailClientTest {

    private static final String SENDER = "testsender@localhost";
    private static final String RECIPIENT = "testrecip@localhost";
    private static final String ATTACHMENT_NAME = "mymockattachment";
    
    private Properties mailServerProps;
    private SmtpAgent mockSmtpAgent;
    private DirectMailClient testDirectMailClient;
        
    private GreenMail greenMail;
        
    @Before
    public void setUp() {
        mockSmtpAgent = mock(SmtpAgent.class);
        greenMail = new GreenMail(ServerSetupTest.SMTPS);
        greenMail.start();
        mailServerProps = getMailServerProps();
        testDirectMailClient = new DirectMailClient(mailServerProps, mockSmtpAgent);
    }
    
    /**
     * Test {@link DirectMailClient#send(String, String, ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document, String)}
     * @throws InterruptedException 
     * @throws IOException 
     */
    @Test
    public void canSendOneMessage() throws InterruptedException, IOException {
        
        Document mockDocument = mock(Document.class);
        DataHandler mockDataHandler = mock(DataHandler.class);
        InputStream mockInputStream = mock(InputStream.class);
        MessageProcessResult mockMessageProcessResult = mock(MessageProcessResult.class);
        MessageEnvelope mockMessageEnvelope = mock(MessageEnvelope.class);
        Message mockMessage = mock(Message.class);
        
        when(mockDocument.getValue()).thenReturn(mockDataHandler);
        when(mockDataHandler.getInputStream()).thenReturn(mockInputStream);
        
        when(mockSmtpAgent.processMessage(any(MimeMessage.class), any(NHINDAddressCollection.class),
                any(NHINDAddress.class))).thenReturn(mockMessageProcessResult);
        
        when(mockMessageProcessResult.getProcessedMessage()).thenReturn(mockMessageEnvelope);
        when(mockMessageEnvelope.getMessage()).thenReturn(mockMessage);
        
        testDirectMailClient.send(SENDER, RECIPIENT, mockDocument, ATTACHMENT_NAME);

        verify(mockSmtpAgent).processMessage(any(MimeMessage.class), any(NHINDAddressCollection.class), 
                any(NHINDAddress.class));
        
        //wait for max 5s for 1 email to arrive
        //waitForIncomingEmail() is useful if you're sending stuff asynchronously in a separate thread
        assertTrue(greenMail.waitForIncomingEmail(5000, 1));
        
        //Retrieve using GreenMail API
        javax.mail.Message[] messages = greenMail.getReceivedMessages();
        assertEquals(1, messages.length);
    }
        
    @After
    public void tearDown() {
        if (null != greenMail) {
            greenMail.stop();
        }
    }
    
    private Properties getMailServerProps() {

        String port = Integer.toString(greenMail.getSmtps().getServerSetup().getPort());
        
        Properties props = new Properties();
        props.put("mail.smtps.host", "localhost");
        props.put("mail.smtps.auth", "TRUE");
        props.put("mail.smtps.port", port);
        props.put("mail.smtps.starttls.enabled", "TRUE");
        props.put("mail.smtp.user", SENDER);
        props.put("mail.smtp.password", SENDER);
        
        // this allows us to run the test using a dummy in-memory keystore... don't use in prod.
        props.put("mail.smtps.ssl.socketFactory.class", "com.icegreen.greenmail.util.DummySSLSocketFactory");
        props.put("mail.smtps.ssl.socketFactory.port", port);
        props.put("mail.smtps.ssl.socketFactory.fallback", "false");

        return props;
    }
}
