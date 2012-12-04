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

import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.RECIP_AT_RESPONDING_GW;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.SENDER_AT_INITIATING_GW;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.getFileAsString;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.getMailServerProps;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.removeSmtpAgentConfig;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.writeSmtpAgentConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.direct.edge.proxy.DirectEdgeProxy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.nhindirect.gateway.smtp.MessageProcessResult;
import org.nhindirect.gateway.smtp.SmtpAgent;
import org.nhindirect.gateway.smtp.SmtpAgentFactory;
import org.nhindirect.stagent.MessageEnvelope;
import org.nhindirect.stagent.NHINDAddress;
import org.nhindirect.stagent.NHINDAddressCollection;
import org.nhindirect.stagent.mail.Message;
import org.nhindirect.stagent.mail.notifications.NotificationMessage;

import com.icegreen.greenmail.user.GreenMailUser;
import com.icegreen.greenmail.user.UserException;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.icegreen.greenmail.util.ServerSetupTest;

public abstract class AbstractDirectMailClientTest {

    protected static final int NUM_MSGS_ONE_BATCH = 3;
    protected static final int NUM_MSGS_MULTI_BATCH = 28;
    protected static final String ATTACHMENT_NAME = "mymockattachment";
    protected static final String encryptedContentType = "application/pkcs7-mime; smime-type=enveloped-data; name=\"smime.p7m\"";
    protected static final String unencryptedContentType = "Multipart/Mixed;";
    protected static final String unencryptedMdnContentType = "multipart/report; report-type=disposition-notification; boundary=\"";
    
    private static final Log LOG = LogFactory.getLog(AbstractDirectMailClientTest.class);

    protected Properties recipMailServerProps;
    protected Properties senderMailServerProps;
    protected SmtpAgent mockSmtpAgent;

    protected DirectMailClient intDirectClient;
    protected DirectMailClient extDirectClient;

    protected MessageHandler mockMessageHandler;

    protected GreenMail greenMail;
    protected GreenMailUser recipUser;
    protected GreenMailUser senderUser;

    /**
     * Set up keystore for test.
     */
    @BeforeClass
    public static void setUpClass() {
        writeSmtpAgentConfig();
    }
    
    /**
     * Tear down keystore created in setup.
     */
    @AfterClass
    public static void tearDownClass() {
        removeSmtpAgentConfig();
    }  
    
    /**
     * Set up tests.
     */
    @Before
    public void setUp() {

        mockSmtpAgent = mock(SmtpAgent.class);

        greenMail = new GreenMail(new ServerSetup[] { ServerSetupTest.SMTPS, ServerSetupTest.IMAPS });
        greenMail.start();

        recipMailServerProps = getMailServerProps(RECIP_AT_RESPONDING_GW, greenMail.getSmtps().getServerSetup()
                .getPort(), greenMail.getImaps().getServerSetup().getPort());
        senderMailServerProps = getMailServerProps(SENDER_AT_INITIATING_GW, greenMail.getSmtps().getServerSetup()
                .getPort(), greenMail.getImaps().getServerSetup().getPort());

        senderUser = greenMail.setUser(SENDER_AT_INITIATING_GW, SENDER_AT_INITIATING_GW, SENDER_AT_INITIATING_GW);
        recipUser = greenMail.setUser(RECIP_AT_RESPONDING_GW, RECIP_AT_RESPONDING_GW, RECIP_AT_RESPONDING_GW);
        
        mockMessageHandler = mock(MessageHandler.class);
        intDirectClient = new DirectMailClient(recipMailServerProps, mockSmtpAgent);
        intDirectClient.setMessageHandler(mockMessageHandler);
    }

    /**
     * Tear down after tests run.
     */
    @After
    public void tearDown() {
        if (greenMail != null) {
            greenMail.stop();
        }
    }
    
    protected SmtpAgent getSmtpAgent() {
    	SmtpAgent smtpAgent = SmtpAgentFactory.createAgent(getClass().getClassLoader().getResource(
                "smtp.agent.config.xml"));
    	return smtpAgent;
    }
    
    protected DirectMailClient getOutboundClient() {        
        DirectMailClient outboundDirectClient = new DirectMailClient(recipMailServerProps, getSmtpAgent());
        return outboundDirectClient;
    }
    
    protected DirectMailClient getInboundClient() {
    	DirectMailClient inboundDirectClient = new DirectMailClient(recipMailServerProps, getSmtpAgent());
    	return inboundDirectClient;
    }
    
    protected void sendMimeMessageToRemoteMailServer(final MimeMessage originalMsg,
            DirectMailClient outboundDirectClient) throws UserException, MessagingException {

        assertNotNull(originalMsg);
        
        /*
         * Initiating Gateway...
         */
        // handle the messages on the internal server
        
        // create a real outbound Message Handler
        OutboundMessageHandler outboundMessageHandler = new OutboundMessageHandler();
        outboundMessageHandler.setExternalDirectClient(outboundDirectClient);
        
        // we can use the same greenmail as external direct client
        outboundMessageHandler.handleMessage(originalMsg, outboundDirectClient);
        
        verifyOutboundMessageSent();
    }
    
    protected DirectClient getInternalDirectClient(Properties props) {
        SmtpAgent smtpAgent = getSmtpAgent();
        
        return new DirectMailClient(props, smtpAgent);
    }
    
    /**
     * Set up direct clients using props.
     * @param props to use.
     */
    protected void setUpDirectClients(Properties props, final DirectEdgeProxy proxy) {
        
        SmtpAgent smtpAgent = getSmtpAgent();

        extDirectClient = new DirectMailClient(props, smtpAgent);
        intDirectClient = new DirectMailClient(props, smtpAgent);

        InboundMessageHandler inboundMessageHandler = new InboundMessageHandler() {
            @Override
            protected DirectEdgeProxy getDirectEdgeProxy() {
                return proxy;
            }
        };
        extDirectClient.setMessageHandler(inboundMessageHandler);

        OutboundMessageHandler outboundMessageHandler = new OutboundMessageHandler();
        outboundMessageHandler.setExternalDirectClient(extDirectClient);                
        intDirectClient.setMessageHandler(outboundMessageHandler);
    }
    
    /**
     * @param messageFile to be delivered.
     * @throws MessagingException on error.
     * @throws UserException
     */
    protected void deliverMessage(String messageFile) {
        
        MimeMessage originalMsg;
        try {
            originalMsg = new MimeMessage(null, IOUtils.toInputStream(getFileAsString(messageFile)));
            assertNotNull(originalMsg);        
            recipUser.deliver(originalMsg);        
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    protected void verifySoapEdgeMessage() throws MessagingException {
        
     }
    
    protected void verifyOutboundMessageSent() throws MessagingException {
    	verifyMessage(1, encryptedContentType, RECIP_AT_RESPONDING_GW, SENDER_AT_INITIATING_GW);
    }
    
    protected void verifySmtpEdgeMessage() throws MessagingException {
       verifyMessage(1, unencryptedContentType, RECIP_AT_RESPONDING_GW, SENDER_AT_INITIATING_GW);
    }
    
    protected void verifyOutboundMdn() throws MessagingException {
        // ...there are 2 MDNs right now because of a quirk in greenmail.
    	verifyMessage(2, encryptedContentType, SENDER_AT_INITIATING_GW, RECIP_AT_RESPONDING_GW);
    }
    
    protected void verifyInboundMdn() throws MessagingException {
        // ...there are 2 MDNs right now because of a quirk in greenmail.
        verifyMessage(2, unencryptedMdnContentType, SENDER_AT_INITIATING_GW, RECIP_AT_RESPONDING_GW);
    }

    private void verifyMessage(int expectedNumberOfMessages, String contentType, String recipient, String sender)
            throws MessagingException {
        // verify that the encrypted message is available on the external mail client
        MimeMessage[] messages = greenMail.getReceivedMessages();
        int i = 0;
        for (MimeMessage message : messages) {
        	if (message.getContentType().toUpperCase().startsWith(contentType.toUpperCase())) {
        		assertEquals(sender, message.getFrom()[0].toString());
        		assertEquals(recipient, message.getAllRecipients()[0].toString());
        		LOG.debug(message.getAllRecipients()[0] + message.getContentType());
        		i++;
        	}
        }
        assertEquals(expectedNumberOfMessages, i);
    }
    
    protected MessageProcessResult getMockMessageProcessResult() throws MessagingException {
        return getMockMessageProcessResult(1);
    }

    protected MessageProcessResult getMockMessageProcessResult(int numNotificationMessages) throws MessagingException {

        MessageProcessResult mockMessageProcessResult = mock(MessageProcessResult.class);
        MessageEnvelope mockMessageEnvelope = mock(MessageEnvelope.class);
        Message mockMessage = mock(Message.class);

        Collection<NotificationMessage> notificationCollection = new ArrayList<NotificationMessage>();
        NotificationMessage mockNotificationMessage = mock(NotificationMessage.class);
        Address senderAddress = new InternetAddress(SENDER_AT_INITIATING_GW);
        Address recipAddress = new InternetAddress(RECIP_AT_RESPONDING_GW);

        for (int i = 0; i < numNotificationMessages; i++) {
            notificationCollection.add(mockNotificationMessage);
        }

        when(mockMessageProcessResult.getProcessedMessage()).thenReturn(mockMessageEnvelope);
        when(mockMessageEnvelope.getMessage()).thenReturn(mockMessage);
        when(mockMessageProcessResult.getNotificationMessages()).thenReturn(notificationCollection);
        when(mockNotificationMessage.getRecipients(any(RecipientType.class))).thenReturn(new Address[] {recipAddress});
        when(mockNotificationMessage.getAllRecipients()).thenReturn(new Address[] {recipAddress});
        when(mockNotificationMessage.getFrom()).thenReturn(new Address[] {senderAddress});
        when(mockSmtpAgent.processMessage(any(MimeMessage.class), any(NHINDAddressCollection.class),
                any(NHINDAddress.class))).thenReturn(mockMessageProcessResult);

        return mockMessageProcessResult;
    }
    
    /**
     * Invoke and test {@link DirectClient#handleMessages()}. Note that this method also cleans up after GreenMail by 
     * expunging any straggling deleted messages that GreenMail misses.
     * 
     * @param directClient to invoke handleMessages() on.
     * @param expectedNumberOfMsgs to be handled.
     * @param user used to connect to mail server. (used to expunge)
     */
    protected void handleMessages(DirectClient directClient, int expectedNumberOfMsgs, GreenMailUser user) {
        assertEquals(expectedNumberOfMsgs, directClient.handleMessages());
        DirectUnitTestUtil.expungeMissedMessages(greenMail, user);        
    }

}
