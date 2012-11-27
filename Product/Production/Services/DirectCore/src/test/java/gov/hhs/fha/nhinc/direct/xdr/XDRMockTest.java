package gov.hhs.fha.nhinc.direct.xdr;

import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.getMailServerProps;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.RECIP_AT_RESPONDING_GW;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.icegreen.greenmail.user.UserException;

import gov.hhs.fha.nhinc.direct.AbstractDirectMailClientTestBase;
import gov.hhs.fha.nhinc.direct.DirectMailClient;
import gov.hhs.fha.nhinc.direct.DirectUnitTestUtil;
import gov.hhs.fha.nhinc.direct.InboundMessageHandler;
import gov.hhs.fha.nhinc.direct.MimeMessageBuilder;

public class XDRMockTest extends AbstractDirectMailClientTestBase {

	@Test
	public void testWithPlainMimeAttachment() throws MessagingException, UserException {
        final MimeMessage originalMsg = new MimeMessage(null,
                IOUtils.toInputStream(DirectUnitTestUtil.getFileAsString("PlainOutgoingMessage.txt")));

        DirectMailClient inboundMailClient = getInboundClient();
        DirectMailClient outboundMailClient = getOutboundClient();
        
        sendMimeMessageToRemoteMailServer(originalMsg, outboundMailClient);
        
        processDirectMessage(inboundMailClient, outboundMailClient, InboundMessageHandler.EDGE_CLIENT_TYPE_SMTP);
        
        processMdn(outboundMailClient);
	}
	
	@Test
	public void testWithXdmAttachment() throws MessagingException, UserException, IOException {
		Session session = Session.getInstance(getMailServerProps(RECIP_AT_RESPONDING_GW, 3456, 3143));
		MimeMessageBuilder builder = DirectUnitTestUtil.getMimeMessageBuilder(session);
		builder.attachment(null);
		builder.attachmentName(null);
		builder.documents(DirectUnitTestUtil.mockDirectDocs());
		MimeMessage originalMsg = builder.build();

        DirectMailClient inboundMailClient = getInboundClient();
        DirectMailClient outboundMailClient = getOutboundClient();
        
        sendMimeMessageToRemoteMailServer(originalMsg, outboundMailClient);
        
        processDirectMessage(inboundMailClient, outboundMailClient, InboundMessageHandler.EDGE_CLIENT_TYPE_SMTP);
        
        processMdn(outboundMailClient);
	}
}
