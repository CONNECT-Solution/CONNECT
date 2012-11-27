package gov.hhs.fha.nhinc.direct.xdr;

import gov.hhs.fha.nhinc.direct.DirectMailClient;
import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;

import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nhindirect.xd.common.DirectDocuments;

public class DirectSender extends ComponentProxyObjectFactory {
    
	private static final Log LOG = LogFactory.getLog(DirectSender.class);
	
    public DirectSender() {
    	 
    	extDirectClient = (DirectClient) context.getBean("extDirectMailClient");
    }
    
    protected DirectClient getDirectClient() {
    	return getBean("extDirectMailClient", DirectMailClient.class);
    }
    
	public void send(Address sender, Address[] recipients, Document attachment, String attachmentName) {
		getDirectMailClient().processAndSend(sender, recipients, attachment, attachmentName);
	}
	
	public void send(Address sender, Address[] recipients, DirectDocuments documents, String messageId) {
		getDirectMailClient().processAndSend(sender, recipients, documents, messageId);
	}
	
    public void send(Address sender, Address[] recipients, MimeMessage message) {
    	try {
			message.setFrom(sender);
			message.setRecipients(RecipientType.TO , recipients);
		} catch (MessagingException e) {
			LOG.error("Error adding To/From to MimeMessage", e);
		}
		
    	getDirectClient().processAndSend(message);
    }

	@Override
	protected String getConfigFileName() {
		return "direct.appcontext.xml";
}
}
