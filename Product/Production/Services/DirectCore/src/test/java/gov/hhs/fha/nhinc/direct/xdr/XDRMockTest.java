package gov.hhs.fha.nhinc.direct.xdr;

import static org.junit.Assert.assertEquals;
import gov.hhs.fha.nhinc.direct.AbstractDirectMailClientTest;
import gov.hhs.fha.nhinc.direct.DirectUnitTestUtil;
import gov.hhs.fha.nhinc.direct.MimeMessageBuilder;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.junit.Test;

import com.icegreen.greenmail.user.UserException;

public class XDRMockTest extends AbstractDirectMailClientTest {

//	@Test
//	public void testWithPlainMimeAttachment() throws MessagingException, UserException {
//        final MimeMessage originalMsg = new MimeMessage(null,
//                IOUtils.toInputStream(DirectUnitTestUtil.getFileAsString("PlainOutgoingMessage.txt")));
//
//        DirectMailClient inboundMailClient = getInboundClient();
//        DirectMailClient outboundMailClient = getOutboundClient();
//        
//        sendMimeMessageToRemoteMailServer(originalMsg, outboundMailClient);
//        
//        processDirectMessage(inboundMailClient, outboundMailClient, InboundMessageHandler.EDGE_CLIENT_TYPE_SMTP);
//        
//        processMdn(outboundMailClient);
//	}
	
	@Test
	public void testWithXdmAttachment() throws MessagingException, UserException, IOException {

	    Session session = Session.getInstance(recipMailServerProps);

	    MimeMessageBuilder builder = DirectUnitTestUtil.getMimeMessageBuilder(session);
		builder.attachment(null).attachmentName(null).documents(DirectUnitTestUtil.mockDirectDocs());
		MimeMessage originalMsg = builder.build();
		
        /* Initiating Gateway */
        setUpDirectClients(recipMailServerProps);

        // use the direct client to send a message
        extDirectClient.processAndSend(originalMsg);

        /* Responding Gateway */
        handleMessages(extDirectClient, 1, recipUser);
        verifySmtpEdgeMessage();
        verifyOutboundMdn();

        /* Initiating Gateway collects an MDN */
        setUpDirectClients(senderMailServerProps);
        
        handleMessages(extDirectClient, 2, senderUser);
        verifyInboundMdn();
	}
}
