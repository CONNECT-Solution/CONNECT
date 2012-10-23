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
import gov.hhs.fha.nhinc.cxf.extraction.SAML2AssertionExtractor;
import gov.hhs.fha.nhinc.docsubmission.entity.EntityDocSubmissionOrchImpl;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.ws.WebServiceContext;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nhindirect.gateway.smtp.MessageProcessResult;
import org.nhindirect.gateway.smtp.SmtpAgent;
import org.nhindirect.gateway.smtp.SmtpAgentException;
import org.nhindirect.gateway.smtp.SmtpAgentFactory;
import org.nhindirect.stagent.AddressSource;
import org.nhindirect.stagent.NHINDAddress;
import org.nhindirect.stagent.NHINDAddressCollection;
import org.nhindirect.stagent.parser.EntitySerializer;

class EntityDocSubmissionImpl {

    private static Log log = null;
    private static final String configURLParam = "http://127.0.0.1:8081/config-service/ConfigurationService";
    private static SmtpAgent agent;
    private static final String SUBJECT = "Document from CONNECT ";
    private static final String TEXT = "Test Message for CONNECT to DIRECT use case";
    private static final String ATTACHMENT_NAME = "CONNECT_Document";
    private static Properties smtpProps = new Properties();
    private static String host, username, password, sender, recipient;

    private static void initData(String protocol) {

        Properties props = null;
        try {
            props = PropertyAccessor.getInstance().getProperties("direct.properties");
        } catch (Exception e) {
            log.error("Exception while reading properties file: direct.properties", e);
        }

        sender = props.getProperty("direct.sender");
        recipient = props.getProperty("direct.recipient");

        if (protocol.equalsIgnoreCase("smtp")) {
            // passthru smtp props from the direct.properties file
            smtpProps = props;
        } else if (protocol.equalsIgnoreCase("imap")) {
            host = props.getProperty("imap.host");
            username = props.getProperty("imap.username");
            password = props.getProperty("imap.password");
        }
    }

    private static void copyMessage(MimeMessage message, String folder) {
        String rand = UUID.randomUUID().toString() + ".eml";
        File fl = new File(System.getProperty("java.io.tmpdir") + folder + File.separator + rand);
        try {
            FileUtils.writeStringToFile(fl, EntitySerializer.Default.serialize(message));
        } catch (IOException e) {
            log.error("Failed to copy outbound message: " + e.getMessage(), e);
        }
    }

    /************ BEGIN - Send Message **************************************************************/

    public static MimeBodyPart createAttachmentFromSOAPRequest(Document data, String name) throws MessagingException,
            IOException {
        DataSource source = new ByteArrayDataSource(data.getValue().getInputStream(), "application/octet-stream");
        DataHandler dhnew = new DataHandler(source);
        MimeBodyPart bodypart = new MimeBodyPart();
        bodypart.setDataHandler(dhnew);
        bodypart.setHeader("Content-Type", "application/octet-stream");
        bodypart.setDisposition(Part.ATTACHMENT);
        bodypart.setFileName(name);
        return (MimeBodyPart) bodypart;
    }

    public void sendMessage(Document attachment, String name) {
        try {
            URL configURL = null;
            try {
                configURL = new URL(configURLParam);
            } catch (MalformedURLException ex) {
                log.error("Invalid configuration URL:" + ex.getMessage(), ex);

            }

            try {
                agent = SmtpAgentFactory.createAgent(configURL);
            } catch (SmtpAgentException e) {
                log.error("Failed to create the SMTP agent: " + e.getMessage(), e);
            }

            try {
                log.trace("Calling agent.processMessage");
                initData("smtp");
                Session session = Session.getInstance(smtpProps, new SMTPAuthenticator());
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(sender));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
                message.setSubject(SUBJECT);
                MimeBodyPart messagePart = new MimeBodyPart();
                messagePart.setText(TEXT);

                MimeBodyPart attachmentPart = createAttachmentFromSOAPRequest(attachment, name);
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messagePart);
                multipart.addBodyPart(attachmentPart);
                message.setContent(multipart);
                message.saveChanges();

                Address recipAddr = new InternetAddress(recipient);
                NHINDAddressCollection recipients = new NHINDAddressCollection();
                recipients.add(new NHINDAddress(recipAddr.toString(), (AddressSource) null));
                InternetAddress senderAddr = new InternetAddress(sender);
                NHINDAddress sender = new NHINDAddress(senderAddr, AddressSource.From);
                MessageProcessResult result = agent.processMessage(message, recipients, sender);
                copyMessage(result.getProcessedMessage().getMessage(), "outbox");
                log.trace("Finished calling agent.processMessage");
                Transport transport;

                if (result.getProcessedMessage() != null) {
                    transport = session.getTransport("smtps");
                    transport.connect();
                    try {
                        InternetAddress[] addressTo = new InternetAddress[1];
                        addressTo[0] = new InternetAddress(recipient);
                        transport.sendMessage(result.getProcessedMessage().getMessage(), addressTo);
                    } catch (AddressException e) {
                        log.error("Failed to process message: " + e.getMessage(), e);
                    } finally {
                        transport.close();
                    }
                }
            } catch (Exception e) {
                log.error("Failed to process message: " + e.getMessage(), e);
            }
        } catch (Exception e) {
            log.error("Failed to call the sendMessage in EntityDocSubmissionImpl.  An unexpected exception occurred.  "
                    + "Exception: " + e.getMessage(), e);
        }
    }

    private static class SMTPAuthenticator extends javax.mail.Authenticator {
        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    }

    /************ END - Send Message **************************************************************/

    /************ BEGIN - Receive Message **************************************************************/

    public void receiveMessage(Document attachment, String name) {
        Folder inbox;
        Store store;
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        try {
            initData("imap");
            Session session = Session.getDefaultInstance(props, null);
            store = session.getStore("imaps");
            store.connect(host, username, password);
            inbox = store.getFolder("Inbox");
            inbox.open(Folder.READ_ONLY);
            FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
            Message messages[] = inbox.search(ft);
            Message message[] = reverseMessageOrder(messages);

            for (int i = 0; i < 1; i++) {
                Format formatter = new SimpleDateFormat("MM/dd/yy");
                String sentDate = formatter.format(message[i].getSentDate());
                String todaysDate = formatter.format(new Date());
                if (sentDate.equalsIgnoreCase(todaysDate)) {
                    String sender = message[i].getFrom()[0].toString();
                    if (sender.indexOf(username) != -1) {
                        decryptMessage((MimeMessage) message[i]);
                        Object content = message[i].getContent();
                    }
                }
            }
            inbox.close(true);
            store.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void decryptMessage(MimeMessage message) {
        URL configURL = null;
        try {
            configURL = new URL(configURLParam);
        } catch (MalformedURLException ex) {
            System.out.println("Invalid configuration URL:" + ex.getMessage());

        }
        try {
            agent = SmtpAgentFactory.createAgent(configURL);
        } catch (SmtpAgentException e) {
            log.error("Failed to create the SMTP agent: " + e.getMessage());
        }

        try {
            Address recipAddr = new InternetAddress(recipient);
            NHINDAddressCollection recipients = new NHINDAddressCollection();
            recipients.add(new NHINDAddress(recipAddr.toString(), (AddressSource) null));
            InternetAddress senderAddr = new InternetAddress(sender);
            NHINDAddress sender = new NHINDAddress(senderAddr, AddressSource.From);
            org.nhindirect.stagent.mail.Message msg = new org.nhindirect.stagent.mail.Message(message);
            MessageProcessResult result = agent.processMessage(message, recipients, sender);
            copyMessage(result.getProcessedMessage().getMessage(), "inbox");
        } catch (Exception ex) {
            log.error("Invalid configuration URL:" + ex.getMessage());

        }
    }

    private static Message[] reverseMessageOrder(Message[] messages) {
        Message revMessages[] = new Message[messages.length];
        int i = messages.length - 1;
        for (int j = 0; j < messages.length; j++, i--) {
            revMessages[j] = messages[i];
        }
        return revMessages;
    }

    /************ END - Receive Message **************************************************************/

    public EntityDocSubmissionImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }
    
    RegistryResponseType provideAndRegisterDocumentSetBUnsecured(
            RespondingGatewayProvideAndRegisterDocumentSetRequestType request, WebServiceContext context) {
        log.info("Begin EntityDocSubmissionImpl.provideAndRegisterDocumentSetBUnsecured(RespondingGatewayProvideAndRegisterDocumentSetRequestType, WebServiceContext)");
        EntityDocSubmissionOrchImpl implOrch = createEntityDocSubmissionOrchImpl();
        RegistryResponseType response = null;

        try {
            if (request != null) {
                ProvideAndRegisterDocumentSetRequestType msg = request.getProvideAndRegisterDocumentSetRequest();
                
                if (msg.getSubmitObjectsRequest().getId().equalsIgnoreCase("send")) {
                    log.info("------------------------------------------------------------------");
                    log.info("Begin - Sending mail to responding gateway mail server");
                    sendMessage(msg.getDocument().get(0), ATTACHMENT_NAME);
                    log.info("End - Mail sent to responding gateway mail server");
                    log.info("------------------------------------------------------------------");
                } else if (msg.getSubmitObjectsRequest().getId().equalsIgnoreCase("receive")) {
                    log.info("------------------------------------------------------------------");
                    log.info("Begin - Receiving mail from responding gateway mail server");
                    receiveMessage(msg.getDocument().get(0), ATTACHMENT_NAME);
                    log.info("End - Mail received from responding gateway mail server");
                    log.info("------------------------------------------------------------------");
                }
                
                NhinTargetCommunitiesType targets = request.getNhinTargetCommunities();
                AssertionType assertIn = request.getAssertion();
                UrlInfoType urlInfo = request.getUrl();  
                response = implOrch.provideAndRegisterDocumentSetB( msg, assertIn, targets, urlInfo);
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
        EntityDocSubmissionOrchImpl implOrch = createEntityDocSubmissionOrchImpl();
        RegistryResponseType response = null;

        try {
            if (request != null) {
                ProvideAndRegisterDocumentSetRequestType msg = request.getProvideAndRegisterDocumentSetRequest();
                NhinTargetCommunitiesType targets = request.getNhinTargetCommunities();
                UrlInfoType urlInfo = request.getUrl();
                
                AssertionType assertion = SAML2AssertionExtractor.getInstance().extractSamlAssertion(context);
                response = implOrch.provideAndRegisterDocumentSetB( msg, assertion, targets, urlInfo);
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
