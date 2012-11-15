package gov.hhs.fha.nhinc.direct.xdr;

import gov.hhs.fha.nhinc.direct.DirectMailClient;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;

import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nhindirect.xd.common.DirectDocuments;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DirectMailSender {
    
	private static final Log LOG = LogFactory.getLog(DirectMailSender.class);
	
    private DirectMailClient extDirectMailClient;
    
    public DirectMailSender() {
    	ApplicationContext context = 
    	    	  new ClassPathXmlApplicationContext(new String[] {getContextXml()});
    	 
    	extDirectMailClient = (DirectMailClient)context.getBean("extDirectMailClient");
    }
    
    protected DirectMailClient getDirectMailClient() {
    	return extDirectMailClient;
    }
    
    protected String getContextXml() {
    	return "direct.appcontext.xml";
    }
	
	public void send(Address sender, Address[] recipients, Document attachment, String attachmentName) {
		getDirectMailClient().send(sender, recipients, attachment, attachmentName);
	}
	
	public void send(Address sender, Address[] recipients, DirectDocuments documents, String messageId) {
		getDirectMailClient().send(sender, recipients, documents, messageId);
	}
	
    public void send(Address sender, Address[] recipients, MimeMessage message) {
    	try {
			message.setFrom(sender);
			message.setRecipients(RecipientType.TO , recipients);
		} catch (MessagingException e) {
			LOG.error("Error adding To/From to MimeMessage", e);
		}
		
    	getDirectMailClient().send(message);
    }
}
