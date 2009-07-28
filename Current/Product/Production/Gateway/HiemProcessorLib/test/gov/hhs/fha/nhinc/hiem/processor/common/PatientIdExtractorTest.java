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
public class PatientIdExtractorTest
{
    private static final String EXPECTED_PATIENT_ID = "222498764";
    private static final String EXPECTED_ASSIGNING_AUTHORITY_ID = "2.16.840.1.113883.3.18.103";
    private static final String PATIENT_ID = "222498764^^^&amp;2.16.840.1.113883.3.18.103&amp;ISO";

    private static final String SUBSCRIBE_MESSAGE =
        "<wsnt:Subscribe xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\" >" +
        "<wsnt:ConsumerReference>" +
        "<wsa:Address>https://localhost:8181/NotificationConsumerService/HiemNotifyTestHelper</wsa:Address>" +
        "</wsnt:ConsumerReference>" +
        "<rim:AdhocQuery id=\"urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d\" xmlns:rim=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0\">" +
        "<rim:Slot name=\"$XDSDocumentEntryPatientId\">" +
        "<rim:ValueList>" +
        "<rim:Value>" + PATIENT_ID + "</rim:Value>" +
        "</rim:ValueList>" +
        "</rim:Slot>" +
        "<rim:Slot name=\"$XDSDocumentEntryClassCode\">" +
        "<rim:ValueList>" +
        "<rim:Value>('XNHIN-CONSENT')</rim:Value>" +
        "</rim:ValueList>" +
        "</rim:Slot>" +
        "</rim:AdhocQuery>" +
        "</wsnt:Subscribe>";

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }


    @Test
    public void patientIdParseTest()
    {
        System.out.println("Start patientIdParseTest");
        try
        {
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

            QualifiedSubjectIdentifierType patientId = extractor.extractPatientIdentifier(subscribeElement, topicConfig);
            
            // Validate the extracted patient id
            assertNotNull("Patient id was null", patientId);
            assertEquals("Patient id was incorrect", EXPECTED_PATIENT_ID, patientId.getSubjectIdentifier());
            assertEquals("Assigning authority id incorrect", EXPECTED_ASSIGNING_AUTHORITY_ID, patientId.getAssigningAuthorityIdentifier());
        }
        catch(Throwable t)
        {
            System.out.println("Error encountered running patientIdParseTest test: " + t.getMessage());
            t.printStackTrace();
            fail(t.getMessage());
        }
        System.out.println("End patientIdParseTest");
    }

}