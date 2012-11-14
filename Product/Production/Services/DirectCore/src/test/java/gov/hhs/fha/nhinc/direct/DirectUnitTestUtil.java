package gov.hhs.fha.nhinc.direct;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.anyString;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.io.IOUtils;
import org.nhindirect.xd.common.DirectDocuments;
import org.nhindirect.xd.common.XdmPackage;

import com.icegreen.greenmail.store.MailFolder;
import com.icegreen.greenmail.store.SimpleStoredMessage;
import com.icegreen.greenmail.user.GreenMailUser;
import com.icegreen.greenmail.util.GreenMail;
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

/**
 * Utilities for running Direct Core Unit Tests.
 */
public class DirectUnitTestUtil {

    /**
     * Sender of a mail message.
     */
    protected static final String SENDER = "testsender@localhost";
    /**
     * Recipient of a mail message.
     */
    protected static final String RECIPIENT = "testrecip@localhost";
    /**
     * Login username.
     */
    protected static final String USER = "testuser";    
    /**
     * Login password.
     */
    protected static final String PASS = "testpass1";
    /**
     * Max number of messages to process at once, allows us to throttle and distribute load.
     */
    protected static final int MAX_NUM_MSGS_IN_BATCH = 5;

    /**
     * Sets up the properties in order to connect to the green mail test server.
     * @param smtpPort for smtps
     * @param imapPort for imaps
     * @return Properties instance holding appropriate values for java mail.
     */
    public static Properties getMailServerProps(int smtpPort, int imapPort) {

        Properties props = new Properties();
        
        props.setProperty("direct.mail.user", USER);
        props.setProperty("direct.mail.pass", PASS);
        props.setProperty("direct.max.msgs.in.batch", Integer.toString(MAX_NUM_MSGS_IN_BATCH));
                        
        props.setProperty("mail.smtps.host", "localhost");
        props.setProperty("mail.smtps.auth", "TRUE");
        props.setProperty("mail.smtps.port", Integer.toString(smtpPort));
        props.setProperty("mail.smtps.starttls.enabled", "TRUE");

        props.setProperty("mail.imaps.host", "localhost");
        props.setProperty("mail.imaps.port", Integer.toString(imapPort));

        // this allows us to run the test using a dummy in-memory keystore provided by GreenMail... don't use in prod.
        props.setProperty("mail.smtps.ssl.socketFactory.class", "com.icegreen.greenmail.util.DummySSLSocketFactory");
        props.setProperty("mail.smtps.ssl.socketFactory.port", Integer.toString(smtpPort));
        props.setProperty("mail.smtps.ssl.socketFactory.fallback", "false");
        props.setProperty("mail.imaps.ssl.socketFactory.class", "com.icegreen.greenmail.util.DummySSLSocketFactory");
        props.setProperty("mail.imaps.ssl.socketFactory.port", Integer.toString(imapPort));
        props.setProperty("mail.imaps.ssl.socketFactory.fallback", "false");

        return props;
    }

    /**
     * Mock document for testing direct messages.
     * @return the mocked document.
     * @throws IOException possible error.
     */
    public static Document getMockDocument() throws IOException {        

        Document mockDocument = mock(Document.class);
        DataHandler mockDataHandler = mock(DataHandler.class);
        InputStream dummyInputStream = IOUtils.toInputStream("Dummy Content for the attachment document.");
        when(mockDocument.getValue()).thenReturn(mockDataHandler);
        when(mockDataHandler.getInputStream()).thenReturn(dummyInputStream);

        return mockDocument;        
    }
    
    public static DirectDocuments getMockDirectDocuments() {
    	DirectDocuments mockDirectDocuments = mock(DirectDocuments.class);
    	XdmPackage mockXdm = mock(XdmPackage.class);
    	File mockFile = mock(File.class);
    	
    	when(mockDirectDocuments.toXdmPackage(anyString())).thenReturn(mockXdm);
    	when(mockXdm.toFile()).thenReturn(mockFile);
    	when(mockFile.getName()).thenReturn("fileName");
    	
    	return mockDirectDocuments;
    }
    
    /**
     * Workaround for defect in greenmail expunging messages: 
     * http://sourceforge.net/tracker/?func=detail&aid=2688036&group_id=159695&atid=812857
     * 
     * We have to delete these ourselves...
     * @param greenMail mock mail server
     * @param user used to access folder to be expunged
     */
    public static void expungeMissedMessages(GreenMail greenMail, GreenMailUser user) {
        try {
            MailFolder folder = greenMail.getManagers().getImapHostManager()
                    .getFolder(user, MailUtils.FOLDER_NAME_INBOX);
            while (folderHasDeletedMsgs(folder)) {
                folder.expunge();
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }    
    
    private static  boolean folderHasDeletedMsgs(MailFolder folder) throws MessagingException {
        for (Object object : folder.getMessages()) {
            SimpleStoredMessage message = (SimpleStoredMessage) object;
            if (message.getFlags().contains(Flags.Flag.DELETED)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * @return sender.
     */
    public static Address getSender() {
        return toInternetAddress(SENDER);
    }
    
    /**
     * @return recipients.
     */
    public static Address[] getRecipients() {
        return new InternetAddress[] {toInternetAddress(RECIPIENT)};
    }
    
    /**
     * Return a stubbed out mime message builder.
     * @param session mail session
     * @return Mime Message Builder
     * @throws IOException on io error.
     */
    public static MimeMessageBuilder getMimeMessageBuilder(Session session) throws IOException {
        MimeMessageBuilder testBuilder = new MimeMessageBuilder(session, getSender(), getRecipients());
        testBuilder.text("text").subject("subject").attachment(getMockDocument()).attachmentName("attachmentName").documents(getMockDirectDocuments()).messageId("1234");
        return testBuilder;
    }

    
    private static InternetAddress toInternetAddress(String email) {
        InternetAddress address = null;
        try {
            address = new InternetAddress(email);
        } catch (AddressException e) {
            fail(e.getMessage());
        }
        return address;        
    }

}
