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
package gov.hhs.fha.nhinc.hiem.processor.common;

import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.hiem.configuration.topicconfiguration.TopicConfigurationEntry;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Element;
import static org.junit.Assert.*;

/**
 * 
 * 
 * @author Neil Webb
 */
public class PatientIdExtractorTest {
    private static final String EXPECTED_PATIENT_ID = "222498764";
    private static final String EXPECTED_ASSIGNING_AUTHORITY_ID = "2.16.840.1.113883.3.18.103";
    private static final String PATIENT_ID = "222498764^^^&amp;2.16.840.1.113883.3.18.103&amp;ISO";

    private static final String SUBSCRIBE_MESSAGE = "<wsnt:Subscribe xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\" >"
            + "<wsnt:ConsumerReference>"
            + "<wsa:Address>https://localhost:8181/NotificationConsumerService/HiemNotifyTestHelper</wsa:Address>"
            + "</wsnt:ConsumerReference>"
            + "<rim:AdhocQuery id=\"urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d\" xmlns:rim=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0\">"
            + "<rim:Slot name=\"$XDSDocumentEntryPatientId\">"
            + "<rim:ValueList>"
            + "<rim:Value>"
            + PATIENT_ID
            + "</rim:Value>"
            + "</rim:ValueList>"
            + "</rim:Slot>"
            + "<rim:Slot name=\"$XDSDocumentEntryClassCode\">"
            + "<rim:ValueList>"
            + "<rim:Value>('XNHIN-CONSENT')</rim:Value>"
            + "</rim:ValueList>"
            + "</rim:Slot>"
            + "</rim:AdhocQuery>" + "</wsnt:Subscribe>";

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void patientIdParseTest() {
        System.out.println("Start patientIdParseTest");
        try {
            // Create the subscribe element
            Element subscribeElement = XmlUtility.convertXmlToElement(SUBSCRIBE_MESSAGE);
            assertNotNull("Subscribe element was null", subscribeElement);

            // XPath pattern to find the patient identifier
            String patientIdentifierLocation = "//*[local-name()='Subscribe']/*[local-name()='AdhocQuery']/*[local-name()='Slot' and @name='$XDSDocumentEntryPatientId']/*[local-name()='ValueList']/*[local-name()='Value']";

            // Extract the patient id
            PatientIdExtractor extractor = new PatientIdExtractor();
            TopicConfigurationEntry topicConfig = new TopicConfigurationEntry();
            topicConfig.setPatientIdentifierSubscribeLocation(patientIdentifierLocation);
            topicConfig.setPatientRequired(true);

            QualifiedSubjectIdentifierType patientId = extractor
                    .extractPatientIdentifier(subscribeElement, topicConfig);

            // Validate the extracted patient id
            assertNotNull("Patient id was null", patientId);
            assertEquals("Patient id was incorrect", EXPECTED_PATIENT_ID, patientId.getSubjectIdentifier());
            assertEquals("Assigning authority id incorrect", EXPECTED_ASSIGNING_AUTHORITY_ID,
                    patientId.getAssigningAuthorityIdentifier());
        } catch (Throwable t) {
            System.out.println("Error encountered running patientIdParseTest test: " + t.getMessage());
            t.printStackTrace();
            fail(t.getMessage());
        }
        System.out.println("End patientIdParseTest");
    }

}