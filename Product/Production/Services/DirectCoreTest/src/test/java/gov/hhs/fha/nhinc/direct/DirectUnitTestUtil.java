/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.direct;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gov.hhs.fha.nhinc.mail.MailUtils;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage.RecipientType;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.nhindirect.gateway.smtp.MessageProcessResult;
import org.nhindirect.stagent.MessageEnvelope;
import org.nhindirect.stagent.mail.Message;
import org.nhindirect.stagent.mail.notifications.NotificationMessage;
import org.nhindirect.xd.common.DirectDocument2;
import org.nhindirect.xd.common.DirectDocuments;
import org.nhindirect.xd.common.XdmPackage;
import org.nhindirect.xd.common.type.ClassCodeEnum;
import org.nhindirect.xd.common.type.FormatCodeEnum;
import org.nhindirect.xd.common.type.HealthcareFacilityTypeCodeEnum;
import org.nhindirect.xd.common.type.LoincEnum;
import org.nhindirect.xd.common.type.PracticeSettingCodeEnum;
import org.nhindirect.xd.transform.pojo.SimplePerson;
import org.nhindirect.xd.transform.util.type.MimeType;

import com.icegreen.greenmail.store.MailFolder;
import com.icegreen.greenmail.store.SimpleStoredMessage;
import com.icegreen.greenmail.user.GreenMailUser;
import com.icegreen.greenmail.util.GreenMail;

/**
 * Utilities for running Direct Core Unit Tests.
 */
public class DirectUnitTestUtil {

    private static final Logger LOG = LoggerFactory.getLogger(DirectUnitTestUtil.class);

    /**
     * email for the sender at the initiating gateway.
     */
    public static final String SENDER_AT_INITIATING_GW = "sender@localhost";
    /**
     * email for the responder at the responding gateway.
     */
    public static final String RECIP_AT_RESPONDING_GW = "mlandis@5amsolutions.com";

    /**
     * Maximum number of messages in a batch.
     */
    protected static final int MAX_NUM_MSGS_IN_BATCH = 5;

    /**
     * Dummy port.
     */
    public static final int DUMMY_PORT = 998;

    /**
     * Connection Timeout in Milliseconds.
     */
    protected static final String TIMEOUT_CONNECTION_MILLIS = Long.toString(TimeUnit.SECONDS.toMillis(15));

    /**
     * Socket Timeout in Milliseconds.
     */
    protected static final String TIMEOUT_MILLIS = Long.toString(TimeUnit.MINUTES.toMillis(3));

    /**
     * Time to wait for the mail handlers to run.
     */
    protected static final long WAIT_TIME_FOR_MAIL_HANDLER = TimeUnit.SECONDS.toMillis(3);

    /**
     * content type for encrypted messages.
     */
    protected static final String CONTENT_TYPE_ENCRYPTED =
            "application/pkcs7-mime; smime-type=enveloped-data; name=\"smime.p7m\"";

    /**
     * content type for multi-part mixed mime messages.
     */
    protected static final String CONTENT_TYPE_MULTIPART = "Multipart/Mixed;";

    /**
     * content type for mdn messages.
     */
    protected static final String CONTENT_TYPE_MDN =
            "multipart/report; report-type=disposition-notification; boundary=\"";


    /**
     * Sets up the properties in order to connect to the green mail test server.
     *
     * @param toAddress is used for the username and password.
     * @param smtpPort for smtps
     * @param imapPort for imaps
     * @return Properties instance holding appropriate values for java mail.
     */
    public static Properties getMailServerProps(String toAddress, int smtpPort, int imapPort) {

        Properties props = new Properties();

        props.setProperty("connect.mail.user", toAddress);
        props.setProperty("connect.mail.pass", toAddress);
        props.setProperty("connect.max.msgs.in.batch", Integer.toString(MAX_NUM_MSGS_IN_BATCH));
        props.setProperty("connect.delete.unhandled.msgs", "false");
        props.setProperty("connect.mail.session.debug", "true");

        props.setProperty("mail.smtp.host", "localhost");
        props.setProperty("mail.smtp.auth", "TRUE");
        props.setProperty("mail.smtp.port", Integer.toString(smtpPort));
        props.setProperty("mail.smtp.starttls.enabled", "TRUE");

        props.setProperty("mail.imaps.host", "localhost");
        props.setProperty("mail.imaps.port", Integer.toString(imapPort));
        props.setProperty("mail.imaps.connectiontimeout", TIMEOUT_CONNECTION_MILLIS);
        props.setProperty("mail.imaps.timeout", TIMEOUT_MILLIS);

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

    /**
     * @return mock direct documents.
     */
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
        return toInternetAddress(SENDER_AT_INITIATING_GW);
    }

    /**
     * @return recipients.
     */
    public static Address[] getRecipients() {
        return new InternetAddress[] {toInternetAddress(RECIP_AT_RESPONDING_GW)};
    }

    /**
     * Return a stubbed out mime message builder.
     * @param session mail session
     * @return Mime Message Builder
     * @throws IOException on io error.
     */
    public static MimeMessageBuilder getMimeMessageBuilder(Session session) throws IOException {
        MimeMessageBuilder testBuilder = new MimeMessageBuilder(session, getSender(), getRecipients());
        testBuilder.text("text").subject("subject").attachment(getMockDocument()).attachmentName("attachmentName")
                .documents(getMockDirectDocuments()).messageId("1234");
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

    /**
     * The keystores references in smtp.agent.config.xml are fully qualified, so we have to make an absolute path
     * for them from a relative path in order to use inside a junit test. The template config file references the
     * keystore with a placeholder {jks.keystore.path} which we will replace with the classpath used by this test.
     */
    public static void writeSmtpAgentConfig() {
        try {
            String smtpAgentConfigTmpl = getFileAsString("smtp.agent.config.tmpl.xml");
            File path = getClassPath();
            FileUtils.writeStringToFile(new File(path + "/smtp.agent.config.xml"),
                    smtpAgentConfigTmpl.replace("{jks.keystore.path}", path.getPath() + "/"));
            LOG.debug("smtp.agent.config.xml: "
                    + smtpAgentConfigTmpl.replace("{jks.keystore.path}", path.getPath() + "/"));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Retrieve the contents of the resource file (relative to classpath) as a string .
     * @param filename resource file name to be stringified
     * @return String representation of the contents of the file
     */
    public static String getFileAsString(String filename) {
        String fileAsString = null;
        try {
            fileAsString =  FileUtils.readFileToString(new File(getClassPath() + "/" + filename));
        } catch (Exception e) {
            fail(e.getMessage());
        }
        return fileAsString;
    }

    /**
     * Delete the auto-generated smtp.agent.config.xml once the test is complete.
     */
    public static void removeSmtpAgentConfig() {
        try {
            FileUtils.deleteQuietly(new File(getClassPath() + "/smtp.agent.config.xml"));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Used when calling code requires absolute paths to test resources.
     * @return absolute classpath.
     */
    public static File getClassPath() throws URISyntaxException {
        return new File(DirectUnitTestUtil.class.getProtectionDomain().getCodeSource().getLocation().toURI());
    }

    /**
     * @return mock direct documents.
     */
    public static DirectDocuments mockDirectDocs() {

        // Create a collection of documents
        DirectDocuments documents = new DirectDocuments();

        documents.getSubmissionSet().setId("1");
        documents.getSubmissionSet().setName("2");
        documents.getSubmissionSet().setDescription("3");
        documents.getSubmissionSet().setSubmissionTime(new Date());
        documents.getSubmissionSet().setIntendedRecipient(Arrays.asList("5.1", "5.2"));
        documents.getSubmissionSet().setAuthorPerson("6");
        documents.getSubmissionSet().setAuthorInstitution(Arrays.asList("7.1", "7.2"));
        documents.getSubmissionSet().setAuthorRole("8");
        documents.getSubmissionSet().setAuthorSpecialty("9");
        documents.getSubmissionSet().setAuthorTelecommunication("10");
        documents.getSubmissionSet().setContentTypeCode("11");
        documents.getSubmissionSet().setContentTypeCode_localized("12");
        documents.getSubmissionSet().setUniqueId("13");
        documents.getSubmissionSet().setSourceId("14");
        documents.getSubmissionSet().setPatientId("xxx");

        DirectDocument2 doc1 = new DirectDocument2();
        doc1.setData("data1".getBytes());

        DirectDocument2.Metadata metadata1 = doc1.getMetadata();
        metadata1.setMimeType(MimeType.TEXT_PLAIN.getType());
        metadata1.setId("1.2");
        metadata1.setDescription("1.3");
        metadata1.setCreationTime(new Date());
        metadata1.setLanguageCode("1.5");
        metadata1.setServiceStartTime(new Date());
        metadata1.setServiceStopTime(new Date());
        metadata1.setSourcePatient(new SimplePerson("1.Bob", "1.Smith"));
        metadata1.setAuthorPerson("1.10");
        metadata1.setAuthorInstitution(Arrays.asList("1.11.1", "1.11.2"));
        metadata1.setAuthorRole("1.12");
        metadata1.setAuthorSpecialty("1.13");
        metadata1.setClassCode(ClassCodeEnum.HISTORY_AND_PHYSICAL.getValue());
        metadata1.setClassCode_localized(ClassCodeEnum.HISTORY_AND_PHYSICAL
                .getValue());
        metadata1.setConfidentialityCode("1.16");
        metadata1.setConfidentialityCode_localized("1.17");
        metadata1.setFormatCode(FormatCodeEnum.CARE_MANAGEMENT_CM);
        metadata1.setHealthcareFacilityTypeCode(HealthcareFacilityTypeCodeEnum.OF.getValue());
        metadata1.setHealthcareFacilityTypeCode_localized(HealthcareFacilityTypeCodeEnum.OF.getValue());
        metadata1.setPracticeSettingCode(PracticeSettingCodeEnum.MULTIDISCIPLINARY.getValue());
        metadata1.setPracticeSettingCode_localized(PracticeSettingCodeEnum.MULTIDISCIPLINARY.getValue());
        metadata1.setLoinc(LoincEnum.LOINC_34133_9.getValue());
        metadata1.setLoinc_localized(LoincEnum.LOINC_34133_9.getValue());
        metadata1.setPatientId("xxx");
        metadata1.setUniqueId("1.27");

        DirectDocument2 doc2 = new DirectDocument2();
        doc2.setData("doc2".getBytes());

        DirectDocument2.Metadata metadata2 = doc2.getMetadata();
        metadata2.setMimeType(MimeType.TEXT_XML.getType());
        metadata2.setId("2.2");
        metadata2.setDescription("2.3");
        metadata2.setCreationTime(new Date());
        metadata2.setLanguageCode("2.5");
        metadata2.setServiceStartTime(new Date());
        metadata2.setServiceStopTime(new Date());
        metadata2.setSourcePatient(new SimplePerson("2.Bob", "2.Smith"));
        metadata2.setAuthorPerson("2.10");
        metadata2.setAuthorInstitution(Arrays.asList("2.11.1", "2.11.2"));
        metadata2.setAuthorRole("2.12");
        metadata2.setAuthorSpecialty("2.13");
        metadata2.setClassCode(ClassCodeEnum.HISTORY_AND_PHYSICAL.getValue());
        metadata2.setClassCode_localized(ClassCodeEnum.HISTORY_AND_PHYSICAL.getValue());
        metadata2.setConfidentialityCode("2.16");
        metadata2.setConfidentialityCode_localized("2.17");
        metadata2.setFormatCode(FormatCodeEnum.CDA_LABORATORY_REPORT);
        metadata2.setHealthcareFacilityTypeCode(HealthcareFacilityTypeCodeEnum.OF.getValue());
        metadata2.setHealthcareFacilityTypeCode_localized(HealthcareFacilityTypeCodeEnum.OF.getValue());
        metadata2.setPracticeSettingCode(PracticeSettingCodeEnum.MULTIDISCIPLINARY.getValue());
        metadata2.setPracticeSettingCode_localized(PracticeSettingCodeEnum.MULTIDISCIPLINARY.getValue());
        metadata2.setLoinc(LoincEnum.LOINC_34133_9.getValue());
        metadata2.setLoinc_localized(LoincEnum.LOINC_34133_9.getValue());
        metadata2.setPatientId("xxx");
        metadata2.setUniqueId("2.27");

        documents.getDocuments().add(doc1);
        documents.getDocuments().add(doc2);
        return documents;
    }


    /**
     * @param numNotificationMessages number of notification messages expected.
     * @return mocked message process result.
     * @throws MessagingException on error.
     */
    public static MessageProcessResult getMockMessageProcessResult(int numNotificationMessages)
            throws MessagingException {

        MessageProcessResult mockMessageProcessResult = mock(MessageProcessResult.class);
        MessageEnvelope mockMessageEnvelope = mock(MessageEnvelope.class);
        Message mockMessage = mock(Message.class);

        Collection<NotificationMessage> notificationCollection = new ArrayList<NotificationMessage>();
        NotificationMessage mockNotificationMessage = mock(NotificationMessage.class);
        Address senderAddress = new InternetAddress(SENDER_AT_INITIATING_GW);
        Address recipAddress = new InternetAddress(RECIP_AT_RESPONDING_GW);

        for (int i = 0; i < numNotificationMessages; i++) {
            notificationCollection.add(mockNotificationMessage);
        }

        when(mockMessageProcessResult.getProcessedMessage()).thenReturn(mockMessageEnvelope);
        when(mockMessageEnvelope.getMessage()).thenReturn(mockMessage);
        when(mockMessage.getContentType()).thenReturn(CONTENT_TYPE_MULTIPART);
        when(mockMessageProcessResult.getNotificationMessages()).thenReturn(notificationCollection);
        when(mockMessage.getRecipients(any(RecipientType.class))).thenReturn(new Address[] {recipAddress});
        when(mockMessage.getAllRecipients()).thenReturn(new Address[] {recipAddress});
        when(mockMessage.getFrom()).thenReturn(new Address[] {senderAddress});

        when(mockNotificationMessage.getRecipients(any(RecipientType.class))).thenReturn(new Address[] {senderAddress});
        when(mockNotificationMessage.getAllRecipients()).thenReturn(new Address[] {senderAddress});
        when(mockNotificationMessage.getFrom()).thenReturn(new Address[] {recipAddress});

        return mockMessageProcessResult;
    }
}
