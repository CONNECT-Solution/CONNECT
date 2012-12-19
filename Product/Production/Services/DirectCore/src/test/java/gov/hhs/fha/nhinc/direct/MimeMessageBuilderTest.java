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
package gov.hhs.fha.nhinc.direct;

import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.getMailServerProps;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.getMimeMessageBuilder;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.RECIP_AT_RESPONDING_GW;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;

import javax.mail.Session;

import org.junit.Test;

/**
 * Test {@link MimeMessageBuilder}.
 */
public class MimeMessageBuilderTest extends DirectBaseTest {

    /**
     * Java mail session.
     */
    private final Session session = Session.getInstance(getMailServerProps(RECIP_AT_RESPONDING_GW, 3456, 3143));
    
    /**
     * Test that we can build a message with all of the properties of the mime message set.
     * @throws IOException is a possible error.
     */
    @Test
    public void canBuildMessage() throws IOException {
        assertNotNull(getBuilder().build());
    }
    
    /**
     * Test that we can build a message with all of the properties of the mime message set.
     * @throws IOException is a possible error.
     */
    @Test
    public void canBuildMessageWithDirectDocuments() throws IOException {
        assertNotNull(getBuilder().attachment(null).attachmentName(null).build());
    }

    /**
     * Throw an exception when the text of the message is missing.
     * @throws IOException is a possible error.
     */
    @Test(expected = DirectException.class)
    public void willThrowExceptionWhenTextIsMissing() throws IOException {
        MimeMessageBuilder testBuilder = getBuilder().text(null);
        testBuilder.build();        
    }
    
    /**
     * Allow message to be built if the subject is missing.
     * @throws IOException is a possible error.
     */
    @Test
    public void canBuildMesageWithoutSubject() throws IOException {
        MimeMessageBuilder testBuilder = getBuilder().subject(null);
        testBuilder.build();        
    }

    /**
     * Throw an exception when the attachment of the message is missing.
     * @throws IOException is a possible error.
     */
    @Test(expected = DirectException.class)
    public void willThrowExceptionWhenAttachmentIsMissing() throws IOException {
        MimeMessageBuilder testBuilder = getBuilder().documents(null).messageId(null).attachment(null);
        testBuilder.build();        
    }

    /**
     * Throw an exception when the attachment name of the message is missing.
     * @throws IOException is a possible error.
     */
    @Test(expected = DirectException.class)
    public void willThrowExceptionWhenAttachmentNameIsMissing() throws IOException {
        MimeMessageBuilder testBuilder = getBuilder().documents(null).messageId(null).attachmentName(null);
        testBuilder.build();        
    }
    
    /**
     * Throw an exception when the direct documents are missing.
     * @throws IOException is a possible error.
     */
    @Test(expected = DirectException.class)
    public void willThrowExceptionWhenDocumentsAreMissing() throws IOException {
        MimeMessageBuilder testBuilder = getBuilder().attachment(null).attachmentName(null).documents(null);
        testBuilder.build();        
    }
    
    /**
     * Throw an exception when the direct documents messageId is missing.
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

}
