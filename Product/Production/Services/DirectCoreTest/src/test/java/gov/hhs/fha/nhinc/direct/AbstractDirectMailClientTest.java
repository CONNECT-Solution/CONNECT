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

import com.icegreen.greenmail.user.GreenMailUser;
import com.icegreen.greenmail.user.UserException;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.icegreen.greenmail.util.ServerSetupTest;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.RECIP_AT_RESPONDING_GW;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.SENDER_AT_INITIATING_GW;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.getFileAsString;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.getMailServerProps;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.removeSmtpAgentConfig;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.writeSmtpAgentConfig;
import gov.hhs.fha.nhinc.direct.edge.proxy.DirectEdgeProxy;
import gov.hhs.fha.nhinc.direct.event.DirectEventLogger;
import gov.hhs.fha.nhinc.event.EventManager;
import gov.hhs.fha.nhinc.mail.ImapMailReceiver;
import gov.hhs.fha.nhinc.mail.MailClientException;
import gov.hhs.fha.nhinc.mail.MailReceiver;
import gov.hhs.fha.nhinc.mail.MailSender;
import gov.hhs.fha.nhinc.mail.MessageHandler;
import gov.hhs.fha.nhinc.mail.SmtpMailSender;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.nhindirect.gateway.smtp.MessageProcessResult;
import org.nhindirect.gateway.smtp.SmtpAgent;
import org.nhindirect.gateway.smtp.SmtpAgentFactory;

/**
 * Base Test class for Direct Mail Client tests.
 */
/**
 *
 */
public abstract class AbstractDirectMailClientTest extends DirectBaseTest {

    /**
     * number of messages in one batch.
     */
    protected static final int NUM_MSGS_ONE_BATCH = 3;
    /**
     * total number of messages in a multi-batch test.
     */
    protected static final int NUM_MSGS_MULTI_BATCH = 28;
    /**
     * attachment name.
     */
    protected static final String ATTACHMENT_NAME = "mymockattachment";
    private static final Logger LOG = LoggerFactory.getLogger(AbstractDirectMailClientTest.class);
    protected Properties recipMailServerProps;
    protected Properties senderMailServerProps;
    protected MailSender intMailSender;
    protected MailReceiver intMailReceiver;
    protected MailSender extMailSender;
    protected MailReceiver extMailReceiver;
    protected MessageHandler mockMessageHandler;
    protected DirectMailPoller extDirectMailPoller;
    protected DirectMailPoller intDirectMailPoller;
    protected MessageHandler inboundMsgHandler;
    protected MessageHandler outboundMsgHandler;
    protected SmtpAgent mockSmtpAgent;
    protected DirectSender testDirectSender;
    protected DirectReceiver testDirectReceiver;
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

        greenMail = new GreenMail(new ServerSetup[]{ServerSetupTest.SMTP, ServerSetupTest.IMAPS});
        greenMail.start();

        recipMailServerProps = getMailServerProps(RECIP_AT_RESPONDING_GW, greenMail.getSmtp().getServerSetup()
            .getPort(), greenMail.getImaps().getServerSetup().getPort());
        senderMailServerProps = getMailServerProps(SENDER_AT_INITIATING_GW, greenMail.getSmtp().getServerSetup()
            .getPort(), greenMail.getImaps().getServerSetup().getPort());

        senderUser = greenMail.setUser(SENDER_AT_INITIATING_GW, SENDER_AT_INITIATING_GW, SENDER_AT_INITIATING_GW);
        recipUser = greenMail.setUser(RECIP_AT_RESPONDING_GW, RECIP_AT_RESPONDING_GW, RECIP_AT_RESPONDING_GW);

        mockMessageHandler = mock(MessageHandler.class);
        when(mockMessageHandler.handleMessage(any(MimeMessage.class))).thenReturn(true);

        intMailSender = new SmtpMailSender(recipMailServerProps);
        intMailReceiver = new ImapMailReceiver(recipMailServerProps);
        extMailSender = new SmtpMailSender(senderMailServerProps);
        extMailReceiver = new ImapMailReceiver(senderMailServerProps);

        testDirectSender = new DirectSenderImpl(extMailSender, mockSmtpAgent, DirectEventLogger.getInstance());
        testDirectReceiver = new DirectReceiverImpl(extMailSender, mockSmtpAgent, DirectEventLogger.getInstance());

        extDirectMailPoller = new DirectMailPoller(extMailReceiver, mockMessageHandler);
        intDirectMailPoller = new DirectMailPoller(intMailReceiver, mockMessageHandler);

    }

    /**
     * Tear down after tests run.
     */
    @After
    public void tearDown() {
        if (greenMail != null) {
            greenMail.stop();
        }

        EventManager.getInstance().deleteObservers();
    }

    /**
     * @return smtp agent.
     */
    protected SmtpAgent getSmtpAgent() {
        SmtpAgent smtpAgent = SmtpAgentFactory.createAgent(getClass().getClassLoader().getResource(
            "smtp.agent.config.xml"));
        return smtpAgent;
    }

    /**
     * Handle outbound messages.
     *
     * @param originalMsg to be sent.
     * @throws UserException on user exception.
     * @throws MessagingException on messaging exception.
     */
    protected void handleOutboundMessage(final MimeMessage originalMsg) throws UserException, MessagingException {

        assertNotNull(originalMsg);

        /*
         * Initiating Gateway...
         */
        // handle the messages on the internal server

        // create a real outbound Message Handler
        MessageHandler outboundMessageHandler = new DirectOutboundMsgHandler(testDirectSender);

        // we can use the same greenmail as external direct client
        outboundMessageHandler.handleMessage(originalMsg);

        verifyOutboundMessageSent();
    }

    /**
     * Set up direct clients using props.
     *
     * @param props to use.
     * @param proxy edge proxy to use for handling message.
     */
    protected void setUpDirectClients(Properties props, final DirectEdgeProxy proxy) {

        SmtpAgent smtpAgent = getSmtpAgent();

        intMailSender = new SmtpMailSender(props);
        intMailReceiver = new ImapMailReceiver(props);
        extMailSender = new SmtpMailSender(props);
        extMailReceiver = new ImapMailReceiver(props);

        testDirectReceiver = new DirectReceiverImpl(extMailSender, smtpAgent, DirectEventLogger.getInstance()) {
            /**
             * {@inheritDoc}
             */
            @Override
            protected DirectEdgeProxy getDirectEdgeProxy() {
                return proxy;
            }
        };

        testDirectSender = new DirectSenderImpl(extMailSender, smtpAgent, DirectEventLogger.getInstance());

        inboundMsgHandler = new DirectInboundMsgHandler(testDirectReceiver);
        outboundMsgHandler = new DirectOutboundMsgHandler(testDirectSender);

    }

    /**
     * @param messageFile to be delivered.
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

    /**
     * @throws MessagingException on error.
     */
    protected void verifyOutboundMessageSent() throws MessagingException {
        verifyMessage(1, DirectUnitTestUtil.CONTENT_TYPE_ENCRYPTED, RECIP_AT_RESPONDING_GW, SENDER_AT_INITIATING_GW);
    }

    /**
     * @throws MessagingException on error.
     */
    protected void verifySmtpEdgeMessage() throws MessagingException {
        verifyMessage(1, DirectUnitTestUtil.CONTENT_TYPE_MULTIPART, RECIP_AT_RESPONDING_GW, SENDER_AT_INITIATING_GW);
    }

    /**
     * @throws MessagingException on error.
     */
    protected void verifyOutboundMdn() throws MessagingException {
        // ...there are 2 MDNs right now because of a quirk in greenmail.
        verifyMessage(2, DirectUnitTestUtil.CONTENT_TYPE_ENCRYPTED, SENDER_AT_INITIATING_GW, RECIP_AT_RESPONDING_GW);
    }

    /**
     * @throws MessagingException on error.
     */
    protected void verifyInboundMdn(int expectedNumberOfMessages) throws MessagingException {
        // ...there are 2 MDNs right now because of a quirk in greenmail.
        verifyMessage(expectedNumberOfMessages, DirectUnitTestUtil.CONTENT_TYPE_MDN, SENDER_AT_INITIATING_GW, RECIP_AT_RESPONDING_GW);
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

    /**
     * @return mock message process result.
     * @throws MessagingException on messaging error.
     */
    protected MessageProcessResult getMockMessageProcessResult() throws MessagingException {
        return DirectUnitTestUtil.getMockMessageProcessResult(1);
    }

    /**
     * Invoke and test {@link MailClient#handleMessages(MessageHandler)}. Note that this method also cleans up after
     * GreenMail by expunging any straggling deleted messages that GreenMail misses.
     *
     * @param receiver mail receiver used to retrieve messages.
     * @param handler used to handle retrieved messages.
     * @param expectedNumberOfMsgs number of messages expected to be retrieved.
     * @param user used to expunge messages.
     * @throws MailClientException on error.
     */
    protected void handleMessages(MailReceiver receiver, MessageHandler handler, int expectedNumberOfMsgs,
        GreenMailUser user) throws MailClientException {
        assertEquals(expectedNumberOfMsgs, receiver.handleMessages(handler));
        DirectUnitTestUtil.expungeMissedMessages(greenMail, user);
    }

    /**
     * @param props mail properties to set on the internal client.
     * @return direct client.
     */
    protected MailSender getMailSender(Properties props) {
        return new SmtpMailSender(props);
    }

    /**
     * @param props mail properties to set on the internal client.
     * @return direct client.
     */
    protected MailReceiver getMailReceiver(Properties props) {
        return new ImapMailReceiver(props);
    }
}
