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

import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.RECIP_AT_RESPONDING_GW;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.getMailServerProps;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.getMimeMessageBuilder;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;
import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import org.apache.commons.io.IOUtils;
import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.nhindirect.xd.common.DirectDocuments;

/**
 * Test {@link MimeMessageBuilder}.
 */
public class MimeMessageBuilderTest extends DirectBaseTest {

    /**
     * Java mail session.
     */
    private final Session session = Session.getInstance(getMailServerProps(RECIP_AT_RESPONDING_GW, 3456, 3143));

    /**
     * Test that we can build a message with all of the properties of the mime
     * message set.
     *
     * @throws IOException is a possible error.
     */
    @Test
    public void canBuildMessage() throws IOException {
        assertNotNull(getBuilder().build());
    }

    /**
     * Test that we can build a message with all of the properties of the mime
     * message set.
     *
     * @throws IOException is a possible error.
     */
    @Test
    public void canBuildMessageWithDirectDocuments() throws IOException {
        assertNotNull(getBuilder().attachment(null).attachmentName(null).build());
    }

    /**
     * Throw an exception when the text of the message is missing.
     *
     * @throws IOException is a possible error.
     */
    @Test(expected = DirectException.class)
    public void willThrowExceptionWhenTextIsMissing() throws IOException {
        MimeMessageBuilder testBuilder = getBuilder().text(null);
        testBuilder.build();
    }

    /**
     * Allow message to be built if the subject is missing.
     *
     * @throws IOException is a possible error.
     */
    @Test
    public void canBuildMesageWithoutSubject() throws IOException {
        MimeMessageBuilder testBuilder = getBuilder().subject(null);
        testBuilder.build();
    }

    /**
     * Throw an exception when the attachment of the message is missing.
     *
     * @throws IOException is a possible error.
     */
    @Test(expected = DirectException.class)
    public void willThrowExceptionWhenAttachmentIsMissing() throws IOException {
        MimeMessageBuilder testBuilder = getBuilder().documents(null).messageId(null).attachment(null);
        testBuilder.build();
    }

    /**
     * Throw an exception when the attachment name of the message is missing.
     *
     * @throws IOException is a possible error.
     */
    @Test(expected = DirectException.class)
    public void willThrowExceptionWhenAttachmentNameIsMissing() throws IOException {
        MimeMessageBuilder testBuilder = getBuilder().documents(null).messageId(null).attachmentName(null);
        testBuilder.build();
    }

    /**
     * Throw an exception when the direct documents are missing.
     *
     * @throws IOException is a possible error.
     */
    @Test(expected = DirectException.class)
    public void willThrowExceptionWhenDocumentsAreMissing() throws IOException {
        MimeMessageBuilder testBuilder = getBuilder().attachment(null).attachmentName(null).documents(null);
        testBuilder.build();
    }

    /**
     * Throw an exception when the direct documents messageId is missing.
     *
     * @throws IOException is a possible error.
     */
    @Test(expected = DirectException.class)
    public void willThrowExceptionWhenMessageIdIsMissing() throws IOException {
        MimeMessageBuilder testBuilder = getBuilder().attachment(null).attachmentName(null).messageId(null);
        testBuilder.build();
    }

    /**
     * Test that we can build a mime message with mock direct docs.
     */
    @Test
    public void buildWithMockDirectDocs() {
        MimeMessageBuilder testBuilder = getBuilder().attachment(null).attachmentName(null)
                .documents(DirectUnitTestUtil.mockDirectDocs()).messageId("1234");
        testBuilder.build();
    }

    private MimeMessageBuilder getBuilder() {
        MimeMessageBuilder builder = null;
        try {
            builder = getMimeMessageBuilder(session);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        return builder;
    }

    /**
     * Test of subject method, of class MimeMessageBuilder.
     */
    @Test
    public void testSubject() {
        String str = "Any String...";
        MimeMessageBuilder instance = new MimeMessageBuilder(null, null, null);
        MimeMessageBuilder result = instance.subject(str);
        assertTrue(result instanceof MimeMessageBuilder);
    }

    /**
     * Test of text method, of class MimeMessageBuilder.
     */
    @Test
    public void testText() {
        String str = "Any String...";
        MimeMessageBuilder instance = new MimeMessageBuilder(null, null, null);
        MimeMessageBuilder result = instance.text(str);
        assertTrue(result instanceof MimeMessageBuilder);
    }

    /**
     * Test of documents method, of class MimeMessageBuilder.
     */
    @Test
    public void testDocuments() {
        DirectDocuments directDocuments = null;
        MimeMessageBuilder instance = new MimeMessageBuilder(null, null, null);
        MimeMessageBuilder result = instance.documents(directDocuments);
        assertTrue(result instanceof MimeMessageBuilder);
    }

    /**
     * Test of messageId method, of class MimeMessageBuilder.
     */
    @Test
    public void testMessageId() {
        String str = "Any String...";
        MimeMessageBuilder instance = new MimeMessageBuilder(null, null, null);
        MimeMessageBuilder result = instance.messageId(str);
        assertTrue(result instanceof MimeMessageBuilder);
    }

    /**
     * Test of attachment method, of class MimeMessageBuilder.
     */
    @Test
    public void testAttachment() {
        System.out.println("attachment");
        ProvideAndRegisterDocumentSetRequestType.Document doc = null;
        MimeMessageBuilder instance = new MimeMessageBuilder(null, null, null);
        MimeMessageBuilder result = instance.attachment(doc);
        assertTrue(result instanceof MimeMessageBuilder);
    }

    /**
     * Test of attachmentName method, of class MimeMessageBuilder.
     */
    @Test
    public void testAttachmentName() {
        String str = "Any String...";
        MimeMessageBuilder instance = new MimeMessageBuilder(null, null, null);
        MimeMessageBuilder result = instance.attachmentName(str);
        assertTrue(result instanceof MimeMessageBuilder);
    }

    @Test
    public void testBuild_Happy() throws IOException, MessagingException
    {
            Address mockFromAddress = mock(Address.class);
            Document mockDoc = mock(Document.class);
            Address mockAddress1 = mock(Address.class);
            Set<Address> toAddresses = new HashSet<>();
            toAddresses.add(mockAddress1);
            Address[] mockReciepients = toAddresses.toArray(new Address[0]);
            MimeMessageBuilder instance = new MimeMessageBuilder(session, mockFromAddress, mockReciepients);
            instance.attachmentName("test");
            instance.attachment(mockDoc);
            instance.text("Some text...");
            final String someText = "This is a test steam...";
            InputStream obj = new ByteArrayInputStream(someText.getBytes());
            DataHandler mockDataHandler = mock(DataHandler.class);
            when(mockDoc.getValue()).thenReturn(mockDataHandler);
            when(mockDataHandler.getInputStream()).thenReturn(obj);
            MimeMessage result = instance.build();
            Multipart multipart = (Multipart) result.getContent();
            MimeBodyPart oBodyPart = (MimeBodyPart) multipart.getBodyPart(1);
            InputStream inputStream = oBodyPart.getDataHandler().getInputStream();
            StringWriter writer = new StringWriter();
            IOUtils.copy(inputStream, writer, null);
            String theString = writer.toString();
            assertEquals(theString, someText);
            assertNotNull(result);
    }
}
