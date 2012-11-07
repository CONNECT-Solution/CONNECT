package gov.hhs.fha.nhinc.direct;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.activation.DataHandler;

import org.apache.commons.io.IOUtils;
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
     * Sets up the properties in order to connect to the green mail test server.
     * @param port mail server is listening to.
     * @param user used for login.
     * @param pass used for login.
     * @return Properties instance holding appropriate values for java mail.
     */
    public static Properties getMailServerProps(int smtpPort, int imapPort) {

        Properties props = new Properties();

        props.put("direct.mail.user", USER);
        props.put("direct.mail.pass", PASS);
        props.put("direct.number.of.messages", 5);
                
        props.put("mail.smtps.host", "localhost");
        props.put("mail.smtps.auth", "TRUE");
        props.put("mail.smtps.port", smtpPort);
        props.put("mail.smtps.starttls.enabled", "TRUE");

        props.put("mail.imaps.host", "localhost");
        props.put("mail.imaps.port", imapPort);

        // this allows us to run the test using a dummy in-memory keystore provided by GreenMail... don't use in prod.
        props.put("mail.smtps.ssl.socketFactory.class", "com.icegreen.greenmail.util.DummySSLSocketFactory");
        props.put("mail.smtps.ssl.socketFactory.port", smtpPort);
        props.put("mail.smtps.ssl.socketFactory.fallback", "false");
        props.put("mail.imaps.ssl.socketFactory.class", "com.icegreen.greenmail.util.DummySSLSocketFactory");
        props.put("mail.imaps.ssl.socketFactory.port", imapPort);
        props.put("mail.imaps.ssl.socketFactory.fallback", "false");
        
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

}
