package gov.hhs.fha.nhinc.direct;

import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.RECIP_AT_RESPONDING_GW;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.SENDER_AT_INITIATING_GW;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.getMailServerProps;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.removeSmtpAgentConfig;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.writeSmtpAgentConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.nhindirect.gateway.smtp.SmtpAgent;
import org.nhindirect.gateway.smtp.SmtpAgentFactory;

import com.icegreen.greenmail.user.GreenMailUser;
import com.icegreen.greenmail.user.UserException;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.icegreen.greenmail.util.ServerSetupTest;

public abstract class AbstractDirectMailClientTestBase {

    protected static final int NUM_MSGS_ONE_BATCH = 3;
    protected static final int NUM_MSGS_MULTI_BATCH = 28;
    protected static final String ATTACHMENT_NAME = "mymockattachment";
    protected static final String encryptedContentType = "application/pkcs7-mime; smime-type=enveloped-data; name=\"smime.p7m\"";
    protected static final String unencryptedContentType = "Multipart/Mixed;";
    
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
    
    /**
     * This test is intended to simulate the end-to-end send, receive and MDN of a direct mail send use case, with SMTP edge
     * clients on the sending and receiving side. Currently it does not run because Greenmail does not support the 
     * envelope fetching part of the IMAP spec.
     * @param smtpAgent 
     * @param outboundDirectClient 
     * @throws UserException when the test fails with a user exception.
     * @throws MessagingException when the test fails with a MessagingException.
     */
    protected void processDirectMessage(DirectMailClient inboundDirectClient, DirectMailClient outboundDirectClient, int respondingEdgeType) throws UserException, MessagingException {    	
    	
        /*
         * Responding Gateway...
         */
        
        // create a real outbound Message Handler
        InboundMessageHandler inboundMessageHandler = new InboundMessageHandler();
        inboundMessageHandler.setEdgeClientType(respondingEdgeType);
        inboundMessageHandler.setInternalDirectClient(inboundDirectClient);
        
        // we can use the same greenmail as external direct client
        outboundDirectClient.setMessageHandler(inboundMessageHandler);
        outboundDirectClient.handleMessages();
        DirectUnitTestUtil.expungeMissedMessages(greenMail, recipUser);
        
        // verify that the decrypted message is available on the mail client
        if (respondingEdgeType == InboundMessageHandler.EDGE_CLIENT_TYPE_SMTP) {
        	verifySmtpEdgeMessage();
        	verifyOutboundMdn();
        } else {
        	// need a way to verify soap
        }
    }
    
    protected void processMdn(DirectMailClient outboundDirectClient) {
//        /*
//         * Initiating Gateway collects an MDN 
//         */
//        intUser = greenMail.setUser(SENDER_AT_INITIATING_GW, DirectUnitTestUtil.USER,
//                DirectUnitTestUtil.PASS);
//        outboundDirectClient.handleMessages();
//        DirectUnitTestUtil.expungeMissedMessages(greenMail, intUser);
    }
    
    protected void verifyOutboundMessageSent() throws MessagingException {
    	verifyMessage(1, encryptedContentType, RECIP_AT_RESPONDING_GW, SENDER_AT_INITIATING_GW);
    }
    
    protected void verifySmtpEdgeMessage() throws MessagingException {
       verifyMessage(1, unencryptedContentType, RECIP_AT_RESPONDING_GW, SENDER_AT_INITIATING_GW);
    }
    
    protected void verifyOutboundMdn() throws MessagingException {
    	verifyMessage(2, encryptedContentType, SENDER_AT_INITIATING_GW, RECIP_AT_RESPONDING_GW);
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
        		System.out.println(message.getAllRecipients()[0] + message.getContentType());
        		i++;
        	}
        }
        assertEquals(expectedNumberOfMessages, i);
    }
}
