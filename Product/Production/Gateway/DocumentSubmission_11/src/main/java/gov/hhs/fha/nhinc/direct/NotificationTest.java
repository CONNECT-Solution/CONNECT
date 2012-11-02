package gov.hhs.fha.nhinc.direct;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.mail.Message.RecipientType;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.junit.Test;
import org.nhindirect.gateway.smtp.MessageProcessResult;
import org.nhindirect.gateway.smtp.SmtpAgent;
import org.nhindirect.stagent.DefaultMessageEnvelope;
import org.nhindirect.stagent.mail.Message;
import org.nhindirect.stagent.mail.notifications.NotificationHelper;
import org.nhindirect.stagent.mail.notifications.NotificationMessage;



public class NotificationTest extends TestCase 
{
	private static Log log = null;

	public static String readResource(String _rec) throws Exception
	{

		int BUF_SIZE = 2048;		
		int count = 0;

		String msgResource = "/messages/" + _rec;

		InputStream stream = NotificationTest.class.getResourceAsStream(msgResource);;

		ByteArrayOutputStream ouStream = new ByteArrayOutputStream();
		if (stream != null) 
		{
			byte buf[] = new byte[BUF_SIZE];

			while ((count = stream.read(buf)) > -1)
			{
				ouStream.write(buf, 0, count);
			}

			try 
			{
				stream.close();
			} 
			catch (IOException ieo) 
			{
				throw ieo;
			}
			catch (Exception e)
			{
				throw e;
			}					
		} 
		else
			throw new IOException("Failed to open resource " + _rec);

		return new String(ouStream.toByteArray());		
	}



	@Test
	public void testMDNMessageAssertions(SmtpAgent agent)
	{
		String message = null ;
		try
		{
			message = readResource("PlainIncomingMessage.txt");
		}
		catch (Exception e)
		{
			log.error("Exception while reading properties file");
		}
		try
		{
			DefaultMessageEnvelope env = new DefaultMessageEnvelope(message);

			// add the notification request
			NotificationHelper.requestNotification(env.getMessage());

			MessageProcessResult result = agent.processMessage(env.getMessage(), env.getRecipients(), env.getSender());			

			assertNotNull(result);
			assertNotNull(result.getProcessedMessage());
			assertNotNull(result.getProcessedMessage().getMessage());
			assertNotNull(result.getNotificationMessages());
			assertTrue(result.getNotificationMessages().size() > 0);

			// get the first message
			NotificationMessage notiMsg = result.getNotificationMessages().iterator().next();				

			assertEquals(1, notiMsg.getRecipients(RecipientType.TO).length);

			Message processedMessage = result.getProcessedMessage().getMessage();
			String processedSender = processedMessage.getFrom()[0].toString();
			String notiRecip = notiMsg.getRecipients(RecipientType.TO)[0].toString();

			// make sure the to and from are the same
			assertEquals(processedSender, notiRecip);
		}
		catch (Exception e)
		{
			log.error("Exception while running test case.", e);
		}				
	}
}


