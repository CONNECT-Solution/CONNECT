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
package gov.hhs.fha.nhinc.docsubmission._11.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.docsubmission.entity.EntityDocSubmissionOrchImpl;
import gov.hhs.fha.nhinc.service.WebServiceHelper;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nhindirect.gateway.smtp.MessageProcessResult;
import org.nhindirect.gateway.smtp.SmtpAgent;
import org.nhindirect.gateway.smtp.SmtpAgentException;
import org.nhindirect.gateway.smtp.SmtpAgentFactory;
import org.nhindirect.stagent.AddressSource;
import org.nhindirect.stagent.NHINDAddress;
import org.nhindirect.stagent.NHINDAddressCollection;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.util.ByteArrayDataSource;

class EntityDocSubmissionImpl {

	private static Log log = null;	
	private static final String configURL = "";
	private static SmtpAgent agent;
    private static final String HOSTNAME = "LOCALHOST";
    private static final String MAIL_PROTOCOL = "SMTP";
    private static final String SENDER = "matthew.tiller@esacinc.com";    
    private static final String RECIPIENT = "ashish.rathee@esacinc.com";
    private static final String SUBJECT = "Document from CONNECT ";
    private static final String TEXT = "Test Message for CONNECT to DIRECT use case";
    private static final String ATTACHMENT_NAME = "CONNECT_Document";
    private static String username = "ashish.rathee@esacinc.com";
    private static String password = "";
    private static Properties smtpProps = new Properties();
    
    public static Session getSession()
    {
        Properties props = new Properties();
        props.put("mail.transport.protocol", MAIL_PROTOCOL);
        props.put("mail.smtp.host", HOSTNAME);
        Session session = Session.getInstance(props);
        return session;  
    }
    
    public static MimeBodyPart createAttachmentFromSOAPRequest(Document data,String name) throws MessagingException, IOException
    {
        DataSource source = new ByteArrayDataSource(data.getValue(),"application/octet-stream");
        DataHandler dhnew = new DataHandler(source);
        MimeBodyPart bodypart = new MimeBodyPart();        
        bodypart.setDataHandler(dhnew);
        bodypart.setHeader("Content-Type", "application/octet-stream");
        bodypart.setDisposition(Part.ATTACHMENT);
        bodypart.setFileName(name);
        return (MimeBodyPart)bodypart;
    }
    
    
    public void sendMessage(Document attachment, String name)
    {
        try 
        {
            String configURLParam = "http://localhost:8081/config-service/ConfigurationService";
    		URL configURL = null;
    		try
    		{
    			configURL = new URL(configURLParam);
    		}
    		catch (MalformedURLException ex)
    		{
    			log.error("Invalid configuration URL:" + ex.getMessage(), ex);
    	
    		}
    		
    		try
    		{
    			agent = SmtpAgentFactory.createAgent(configURL);
    		}
    		catch (SmtpAgentException e)
    		{
    			log.error("Failed to create the SMTP agent: " + e.getMessage(), e);
    		}
    		
    		try
    		{
    			log.trace("Calling agent.processMessage");
    			Session session = Session.getInstance(smtpProps, new SMTPAuthenticator());    			
                MimeMessage message =  new MimeMessage(session);       
                message.setFrom(new InternetAddress(SENDER));
                message.addRecipient(Message.RecipientType.TO,new InternetAddress(RECIPIENT));
                message.setSubject(SUBJECT);
                MimeBodyPart messagePart = new MimeBodyPart();
                messagePart.setText(TEXT);
                
                MimeBodyPart attachmentPart = createAttachmentFromSOAPRequest(attachment,name);     
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messagePart);
                multipart.addBodyPart(attachmentPart);
                message.setContent(multipart);
                message.saveChanges();

                Address recipAddr = new InternetAddress(RECIPIENT);       		
                NHINDAddressCollection recipients = new NHINDAddressCollection();
                recipients.add(new NHINDAddress(recipAddr.toString(), (AddressSource)null));                
                InternetAddress senderAddr = new InternetAddress(SENDER);
                NHINDAddress sender = new NHINDAddress(senderAddr, AddressSource.From);	
    			MessageProcessResult result = agent.processMessage(message, recipients, sender);
    			log.trace("Finished calling agent.processMessage");
    			Transport transport;
    			
    			if (result.getProcessedMessage() != null)
    			{
    	            transport = session.getTransport("smtps");
    		        smtpProps.setProperty("mail.smtps.host","smtp.gmail.com");
    		        smtpProps.setProperty("mail.smtps.auth", "true");
    		        smtpProps.setProperty("mail.smtps.port", "465"); 
    		        smtpProps.setProperty("mail.smtps.dsn.notify", "SUCCESS,FAILURE,DELAY");
    		        smtpProps.setProperty("mail.smtps.sslenable", "true");
    		        smtpProps.setProperty("mail.smtps.starttls.enabled","true");
    		            
    	            transport.connect();
    	            try
    	            {
    	             	InternetAddress[] addressTo = new InternetAddress[1];
    	               	addressTo[0] = new InternetAddress(RECIPIENT);
    	                transport.sendMessage(result.getProcessedMessage().getMessage(), addressTo);
                    }
    		        catch (AddressException e)
    		        {
    		            log.error("Failed to process message: " + e.getMessage(), e);	
    		        }
                    finally 
                    {
                        transport.close();
                    }
                }
    	     }
    		catch (Exception e)
    		{
		    	log.error("Failed to process message: " + e.getMessage(), e);	
    		}
        } 
        catch (Exception e) 
        {
        	 log.error(
                     "Failed to call the sendMessage in EntityDocSubmissionImpl.  An unexpected exception occurred.  " + "Exception: "
                             + e.getMessage(), e);
        }
    }
    
 
    
    private static class SMTPAuthenticator extends javax.mail.Authenticator
    {
    	@Override
    	public PasswordAuthentication getPasswordAuthentication()
        {
            return new PasswordAuthentication(username, password);
        }
    }
    
    public EntityDocSubmissionImpl() {
       log = createLogger();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected WebServiceHelper createWebServiceHelper() {
        return new WebServiceHelper();
    }

    

    RegistryResponseType provideAndRegisterDocumentSetBUnsecured(
            RespondingGatewayProvideAndRegisterDocumentSetRequestType request, WebServiceContext context) {
        log.info("Begin EntityDocSubmissionImpl.provideAndRegisterDocumentSetBUnsecured(RespondingGatewayProvideAndRegisterDocumentSetRequestType, WebServiceContext)");
       
        WebServiceHelper oHelper = createWebServiceHelper();
        EntityDocSubmissionOrchImpl implOrch = createEntityDocSubmissionOrchImpl();
        RegistryResponseType response = null;
        
       
        try {
            if (request != null) {
                ProvideAndRegisterDocumentSetRequestType msg = request.getProvideAndRegisterDocumentSetRequest();
                
                log.info("------------------------------------------------------------------");
                log.info("Begin - Sending mail to Direct Instance");
                sendMessage(msg.getDocument().get(0), ATTACHMENT_NAME);  
                log.info("End - Mail sent to Direct Instance");
                log.info("------------------------------------------------------------------");
                
                NhinTargetCommunitiesType targets = request.getNhinTargetCommunities();
                AssertionType assertIn = request.getAssertion();
                UrlInfoType urlInfo = request.getUrl();
                response = (RegistryResponseType) oHelper.invokeUnsecureWebService(implOrch, implOrch.getClass(),
                        "provideAndRegisterDocumentSetB", msg, assertIn, targets, urlInfo, context);
            } else {
                log.error("Failed to call the web orchestration (" + implOrch.getClass()
                        + ".provideAndRegisterDocumentSetB).  The input parameter is null.");
            }
        } catch (Exception e) {
            log.error(
                    "Failed to call the web orchestration (" + implOrch.getClass()
                            + ".provideAndRegisterDocumentSetB).  An unexpected exception occurred.  " + "Exception: "
                            + e.getMessage(), e);
        }
        log.info("End EntityDocSubmissionImpl.provideAndRegisterDocumentSetBUnsecured with response: " + response);
        return response;
    }

    RegistryResponseType provideAndRegisterDocumentSetBSecured(
            RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request, WebServiceContext context) {
        log.info("Begin EntityDocSubmissionImpl.provideAndRegisterDocumentSetBSecured(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType, WebServiceContext)");
        WebServiceHelper oHelper = createWebServiceHelper();
        EntityDocSubmissionOrchImpl implOrch = createEntityDocSubmissionOrchImpl();
        RegistryResponseType response = null;

        try {
            if (request != null) {
                ProvideAndRegisterDocumentSetRequestType msg = request.getProvideAndRegisterDocumentSetRequest();
                NhinTargetCommunitiesType targets = request.getNhinTargetCommunities();
                UrlInfoType urlInfo = request.getUrl();
                response = (RegistryResponseType) oHelper.invokeSecureWebService(implOrch, implOrch.getClass(),
                        "provideAndRegisterDocumentSetB", msg, targets, urlInfo, context);
            } else {
                log.error("Failed to call the web orchestration (" + implOrch.getClass()
                        + ".provideAndRegisterDocumentSetB).  The input parameter is null.");
            }
        } catch (Exception e) {
            log.error(
                    "Failed to call the web orchestration (" + implOrch.getClass()
                            + ".provideAndRegisterDocumentSetB).  An unexpected exception occurred.  " + "Exception: "
                            + e.getMessage(), e);
        }
        log.info("End EntityDocSubmissionImpl.provideAndRegisterDocumentSetBSecured with response: " + response);

        return response;
    }

    private EntityDocSubmissionOrchImpl createEntityDocSubmissionOrchImpl() {
        return new EntityDocSubmissionOrchImpl();
    }
}
