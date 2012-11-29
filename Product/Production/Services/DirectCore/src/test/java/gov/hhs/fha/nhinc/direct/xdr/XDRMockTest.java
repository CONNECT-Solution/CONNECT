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
		verifySoapEdgeMessage();
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
		verifySoapEdgeMessage();
		verifyOutboundMdn();

		/* Initiating Gateway collects an MDN */
		setUpDirectClients(senderMailServerProps, new DirectEdgeProxySoapImpl());

		handleMessages(extDirectClient, 2, senderUser);
		verifyInboundMdn();
	}
}
