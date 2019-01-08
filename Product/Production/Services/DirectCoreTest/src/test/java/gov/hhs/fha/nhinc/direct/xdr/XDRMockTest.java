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
package gov.hhs.fha.nhinc.direct.xdr;

import com.icegreen.greenmail.user.UserException;
import gov.hhs.fha.nhinc.direct.AbstractDirectMailClientTest;
import gov.hhs.fha.nhinc.direct.DirectReceiverImpl;
import gov.hhs.fha.nhinc.direct.DirectUnitTestUtil;
import gov.hhs.fha.nhinc.direct.MimeMessageBuilder;
import gov.hhs.fha.nhinc.direct.edge.proxy.DirectEdgeProxySmtpImpl;
import gov.hhs.fha.nhinc.direct.edge.proxy.DirectEdgeProxySoapImpl;
import gov.hhs.fha.nhinc.mail.MailClientException;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import java.io.IOException;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test Direct Client with XDR edge clients.
 */
@Ignore
public class XDRMockTest extends AbstractDirectMailClientTest {

    /**
     * Test with plain MIME attachment
     *
     * @throws MessagingException on a failure
     * @throws UserException on a failure
     * @throws MailClientException on a failure
     */
    @Test
    public void testWithPlainMimeAttachment() throws MessagingException, UserException, MailClientException {
        //should not receive any mdn in the edge client by default
        System.setProperty(DirectReceiverImpl.SUPPRESS_MDN_EDGE_NOTIFICATION, "true");
        testWithPlainMimeAttachment(0);
    }

    /**
     * Test with plain MIME attachment
     *
     * @throws MessagingException on a failure
     * @throws UserException on a failure
     * @throws MailClientException on a failure
     */
    @Test
    public void testWithPlainMimeAttachmentWithSuppressNotificationDisabled() throws MessagingException, UserException, MailClientException {
        //test with suppressmdnedgenotification false. Should receive a mdn from the sending end.
        //set the system property
        System.setProperty(DirectReceiverImpl.SUPPRESS_MDN_EDGE_NOTIFICATION, "false");
        // ...there are 2 MDNs right now because of a quirk in greenmail.
        testWithPlainMimeAttachment(2);
    }

    /**
     * @throws MessagingException on a failure
     * @throws UserException on a failure
     * @throws MailClientException on a failure
     */
    public void testWithPlainMimeAttachment(int expectedNumberOfMessages) throws MessagingException, UserException, MailClientException {

        final MimeMessage originalMsg = new MimeMessage(null, IOUtils.toInputStream(DirectUnitTestUtil
            .getFileAsString("PlainOutgoingMessage.txt")));

        /* Initiating Gateway */
        extMailSender = getMailSender(senderMailServerProps);
        DirectEdgeProxySmtpImpl initiatingSmtp = new DirectEdgeProxySmtpImpl(extMailSender);
        setUpDirectClients(recipMailServerProps, initiatingSmtp);

        // use the direct client to send a message
        testDirectSender.sendOutboundDirect(originalMsg);

        /* Responding Gateway */
        handleMessages(extMailReceiver, inboundMsgHandler, 1, recipUser);
        verifySmtpEdgeMessage();
        verifyOutboundMdn();

        /* Initiating Gateway collects an MDN */
        DirectEdgeProxySmtpImpl respondingSmtp = new DirectEdgeProxySmtpImpl(intMailSender);
        setUpDirectClients(senderMailServerProps, respondingSmtp);

        handleMessages(extMailReceiver, inboundMsgHandler, 2, senderUser);
        verifyInboundMdn(expectedNumberOfMessages);
    }

    /**
     * @throws MessagingException on a failure
     * @throws UserException on failure
     * @throws IOException on failure
     * @throws MailClientException on failure
     */
    @Test
    public void testWithXdmAttachment() throws MessagingException, UserException, IOException, MailClientException {
        //should not receive any mdn in the edge client by default
        System.setProperty(DirectReceiverImpl.SUPPRESS_MDN_EDGE_NOTIFICATION, "true");
        testWithXdmAttachment(0);
    }

    /**
     * @throws MessagingException on a failure
     * @throws UserException on failure
     * @throws IOException on failure
     * @throws MailClientException on failure
     */
    @Test
    public void testWithXdmAttachmentWithSuppressNotificationDisabled() throws MessagingException, UserException, IOException, MailClientException {
        //test with suppressmdnedgenotification false. Should receive a mdn from the sending end.
        //set the system property
        System.setProperty(DirectReceiverImpl.SUPPRESS_MDN_EDGE_NOTIFICATION, "false");
        // ...there are 2 MDNs right now because of a quirk in greenmail.
        testWithXdmAttachment(2);
    }

    /**
     * @throws MessagingException on a failure
     * @throws UserException on failure
     * @throws IOException on failure
     * @throws MailClientException on failure
     */
    public void testWithXdmAttachment(int expectedNumberOfMessages) throws MessagingException, UserException, IOException, MailClientException {
        Session session = Session.getInstance(recipMailServerProps);

        MimeMessageBuilder builder = DirectUnitTestUtil.getMimeMessageBuilder(session);
        builder.attachment(null).attachmentName(null).documents(DirectUnitTestUtil.mockDirectDocs());
        MimeMessage originalMsg = builder.build();

        /* Initiating Gateway */
        DirectEdgeProxySmtpImpl initiatingSmtp = new DirectEdgeProxySmtpImpl(extMailSender);
        setUpDirectClients(recipMailServerProps, initiatingSmtp);

        // use the direct client to send a message
        testDirectSender.sendOutboundDirect(originalMsg);

        /* Responding Gateway */
        handleMessages(extMailReceiver, inboundMsgHandler, 1, recipUser);
        verifySmtpEdgeMessage();
        verifyOutboundMdn();

        /* Initiating Gateway collects an MDN */
        DirectEdgeProxySmtpImpl respondingSmtp = new DirectEdgeProxySmtpImpl(intMailSender);
        setUpDirectClients(senderMailServerProps, respondingSmtp);

        handleMessages(extMailReceiver, inboundMsgHandler, 2, senderUser);
        verifyInboundMdn(expectedNumberOfMessages);
    }

    /**
     * @throws MessagingException on a failure
     * @throws UserException on a failure
     * @throws MailClientException on a failure
     */
    @Test
    @Ignore
    public void testWithPlainMimeAttachmentWithXDREdge() throws MessagingException, UserException, MailClientException {

        final MimeMessage originalMsg = new MimeMessage(null, IOUtils.toInputStream(DirectUnitTestUtil
            .getFileAsString("PlainOutgoingMessage.txt")));

        /* Initiating Gateway */
        setUpDirectClients(recipMailServerProps, new DirectEdgeProxySoapImpl(new WebServiceProxyHelper()));

        // use the direct client to send a message
        testDirectSender.sendOutboundDirect(originalMsg);

        /* Responding Gateway */
        handleMessages(extMailReceiver, inboundMsgHandler, 1, recipUser);
        // verifySoapEdgeMessage();
        verifyOutboundMdn();

        /* Initiating Gateway collects an MDN */
        setUpDirectClients(senderMailServerProps, new DirectEdgeProxySoapImpl(new WebServiceProxyHelper()));

        handleMessages(extMailReceiver, inboundMsgHandler, 2, senderUser);
        verifyInboundMdn(2);
    }

    /**
     * @throws MessagingException on a failure
     * @throws UserException on a failure
     * @throws IOException on a failure
     * @throws MailClientException on a failure
     */
    @Test
    @Ignore
    public void testWithXdmAttachmentWithXDREdge() throws MessagingException, UserException, IOException,
        MailClientException {
        //set the system property
        System.setProperty(DirectReceiverImpl.SUPPRESS_MDN_EDGE_NOTIFICATION, "false");

        Session session = Session.getInstance(recipMailServerProps);

        MimeMessageBuilder builder = DirectUnitTestUtil.getMimeMessageBuilder(session);
        builder.attachment(null).attachmentName(null).documents(DirectUnitTestUtil.mockDirectDocs());
        MimeMessage originalMsg = builder.build();

        /* Initiating Gateway */
        setUpDirectClients(recipMailServerProps, new DirectEdgeProxySoapImpl(new WebServiceProxyHelper()));

        // use the direct client to send a message
        testDirectSender.sendOutboundDirect(originalMsg);

        /* Responding Gateway */
        handleMessages(extMailReceiver, inboundMsgHandler, 1, recipUser);
        // verifySoapEdgeMessage();
        verifyOutboundMdn();

        /* Initiating Gateway collects an MDN */
        setUpDirectClients(senderMailServerProps, new DirectEdgeProxySoapImpl(new WebServiceProxyHelper()));

        handleMessages(extMailReceiver, inboundMsgHandler, 2, senderUser);
        verifyInboundMdn(2);
    }
}
