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
package gov.hhs.fha.nhinc.direct.xdr;

import gov.hhs.fha.nhinc.direct.AbstractDirectMailClientTest;
import gov.hhs.fha.nhinc.direct.DirectUnitTestUtil;
import gov.hhs.fha.nhinc.direct.MimeMessageBuilder;
import gov.hhs.fha.nhinc.direct.edge.proxy.DirectEdgeProxySmtpImpl;
import gov.hhs.fha.nhinc.direct.edge.proxy.DirectEdgeProxySoapImpl;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;

import com.icegreen.greenmail.user.UserException;

/**
 * Test Direct Client with XDR edge clients.
 */
public class XDRMockTest extends AbstractDirectMailClientTest {

	@Test
	public void testWithPlainMimeAttachment() throws MessagingException,
			UserException {
		final MimeMessage originalMsg = new MimeMessage(null,
				IOUtils.toInputStream(DirectUnitTestUtil
						.getFileAsString("PlainOutgoingMessage.txt")));

		/* Initiating Gateway */
		DirectEdgeProxySmtpImpl initiatingSmtp = new DirectEdgeProxySmtpImpl();
        initiatingSmtp.setInternalDirectClient(getInternalDirectClient(recipMailServerProps));
		setUpDirectClients(recipMailServerProps, initiatingSmtp);

		// use the direct client to send a message
		extDirectClient.processAndSend(originalMsg);

		/* Responding Gateway */
		handleMessages(extDirectClient, 1, recipUser);
		verifySmtpEdgeMessage();
		verifyOutboundMdn();

		/* Initiating Gateway collects an MDN */
        DirectEdgeProxySmtpImpl respondingSmtp = new DirectEdgeProxySmtpImpl();
        respondingSmtp.setInternalDirectClient(getInternalDirectClient(senderMailServerProps));
		setUpDirectClients(senderMailServerProps, respondingSmtp);

		handleMessages(extDirectClient, 2, senderUser);
		verifyInboundMdn();
	}

	@Test
	public void testWithXdmAttachment() throws MessagingException,
			UserException, IOException {

		Session session = Session.getInstance(recipMailServerProps);

		MimeMessageBuilder builder = DirectUnitTestUtil
				.getMimeMessageBuilder(session);
		builder.attachment(null).attachmentName(null)
				.documents(DirectUnitTestUtil.mockDirectDocs());
		MimeMessage originalMsg = builder.build();

		/* Initiating Gateway */
		DirectEdgeProxySmtpImpl initiatingSmtp = new DirectEdgeProxySmtpImpl();
        initiatingSmtp.setInternalDirectClient(getInternalDirectClient(recipMailServerProps));
		setUpDirectClients(recipMailServerProps, initiatingSmtp);

		// use the direct client to send a message
		extDirectClient.processAndSend(originalMsg);

		/* Responding Gateway */
		handleMessages(extDirectClient, 1, recipUser);
		verifySmtpEdgeMessage();
		verifyOutboundMdn();

		/* Initiating Gateway collects an MDN */
        DirectEdgeProxySmtpImpl respondingSmtp = new DirectEdgeProxySmtpImpl();
        respondingSmtp.setInternalDirectClient(getInternalDirectClient(senderMailServerProps));
		setUpDirectClients(senderMailServerProps, respondingSmtp);

		handleMessages(extDirectClient, 2, senderUser);
		verifyInboundMdn();
	}

	@Test
	@Ignore
	public void testWithPlainMimeAttachmentWithXDREdge()
			throws MessagingException, UserException {
		final MimeMessage originalMsg = new MimeMessage(null,
				IOUtils.toInputStream(DirectUnitTestUtil
						.getFileAsString("PlainOutgoingMessage.txt")));

		/* Initiating Gateway */
		setUpDirectClients(recipMailServerProps, new DirectEdgeProxySoapImpl());

		// use the direct client to send a message
		extDirectClient.processAndSend(originalMsg);

		/* Responding Gateway */
		handleMessages(extDirectClient, 1, recipUser);
//		verifySoapEdgeMessage();
		verifyOutboundMdn();

		/* Initiating Gateway collects an MDN */
		setUpDirectClients(senderMailServerProps, new DirectEdgeProxySoapImpl());

		handleMessages(extDirectClient, 2, senderUser);
		verifyInboundMdn();
	}

	@Test
	@Ignore
	public void testWithXdmAttachmentWithXDREdge() throws MessagingException,
			UserException, IOException {
		Session session = Session.getInstance(recipMailServerProps);

		MimeMessageBuilder builder = DirectUnitTestUtil
				.getMimeMessageBuilder(session);
		builder.attachment(null).attachmentName(null)
				.documents(DirectUnitTestUtil.mockDirectDocs());
		MimeMessage originalMsg = builder.build();

		/* Initiating Gateway */
		setUpDirectClients(recipMailServerProps, new DirectEdgeProxySoapImpl());

		// use the direct client to send a message
		extDirectClient.processAndSend(originalMsg);

		/* Responding Gateway */
		handleMessages(extDirectClient, 1, recipUser);
//		verifySoapEdgeMessage();
		verifyOutboundMdn();

		/* Initiating Gateway collects an MDN */
		setUpDirectClients(senderMailServerProps, new DirectEdgeProxySoapImpl());

		handleMessages(extDirectClient, 2, senderUser);
		verifyInboundMdn();
	}
}
