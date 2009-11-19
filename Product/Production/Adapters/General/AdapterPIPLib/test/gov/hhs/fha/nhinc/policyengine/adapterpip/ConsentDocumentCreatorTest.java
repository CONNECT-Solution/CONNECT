package gov.hhs.fha.nhinc.policyengine.adapterpip;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType;
import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.xacml._2_0.policy.schema.os.PolicyType;
import org.apache.commons.logging.Log;
import org.hl7.v3.ActClassClinicalDocument;
import org.hl7.v3.POCDMT000040Author;
import org.hl7.v3.POCDMT000040ClinicalDocument;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.runner.RunWith;

/**
 * Test case for the ConsentDocumentCreator class
 *
 * @author Neil Webb
 */
@RunWith(JMock.class)
public class ConsentDocumentCreatorTest
{
    Mockery context = new JUnit4Mockery();

    public ConsentDocumentCreatorTest()
    {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * This test exercises consent document creation using complete input
     * data. Some provided values are expected to be overrided but most
     * values will be populated from the values provided in the source
     * document.
     */
    @Test
    public void testCreatePatientConsentBuilderFullDoc()
    {
        try
        {
            // Create Mock objects
            final Log mockLog = context.mock(Log.class);


            StorePtConsentRequestType storeConsentRequest = loadDocument(STORE_CONSENT_REQUEST_FULL);
            assertNotNull("Deserialized doc was null", storeConsentRequest);

            ConsentDocumentCreator docCreator = new ConsentDocumentCreator()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
            };

            // Set expectations
            context.checking(new Expectations(){{
                allowing (mockLog).isDebugEnabled();
                allowing (mockLog).debug(with(any(String.class)));
            }});

            // Create consent XACML
            PolicyType oConsentXACML = null;
            XACMLCreator oCreator = new XACMLCreator();
            oConsentXACML = oCreator.createConsentXACMLDoc(storeConsentRequest.getPatientPreferences());
            XACMLSerializer oSerializer = new XACMLSerializer();
            String sConsentXACML = oSerializer.serializeConsentXACMLDoc(oConsentXACML);

            POCDMT000040ClinicalDocument clinDoc = docCreator.createConsentCDADoc(storeConsentRequest.getPatientPreferences(), sConsentXACML);
            assertNotNull("Created doc was null", clinDoc);

            // Class code - hard coded value used - override from doc
            assertEquals("Class code", ActClassClinicalDocument.DOCCLIN, clinDoc.getClassCode());

            // Document title - value from document used
            assertEquals("Document title", "document_title", (String) clinDoc.getTitle().getContent().get(0));

        }
        catch(Throwable t)
        {
            t.printStackTrace();
            fail(t.getMessage());
        }
    }

    /**
     * This test exercises consent document creation using minimal input
     * data. Some provided values are expected to be overrided and some
     * values will be populated with default data as they are not provided
     * in the source document.
     */
    @Test
    public void testCreatePatientConsentBuilderMinimalDoc()
    {
        try
        {
            // Create Mock objects
            final Log mockLog = context.mock(Log.class);

            StorePtConsentRequestType storeConsentRequest = loadDocument(STORE_CONSENT_REQUEST_MINIMAL);
            assertNotNull("Deserialized doc was null", storeConsentRequest);

            ConsentDocumentCreator docCreator = new ConsentDocumentCreator()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
            };

            // Set expectations
            context.checking(new Expectations(){{
                allowing (mockLog).isDebugEnabled();
                allowing (mockLog).debug(with(any(String.class)));
            }});

            // Create consent XACML
            PolicyType oConsentXACML = null;
            XACMLCreator oCreator = new XACMLCreator();
            oConsentXACML = oCreator.createConsentXACMLDoc(storeConsentRequest.getPatientPreferences());
            XACMLSerializer oSerializer = new XACMLSerializer();
            String sConsentXACML = oSerializer.serializeConsentXACMLDoc(oConsentXACML);

            POCDMT000040ClinicalDocument clinDoc = docCreator.createConsentCDADoc(storeConsentRequest.getPatientPreferences(), sConsentXACML);
            assertNotNull("Created doc was null", clinDoc);

            // Document title - Default value used
            assertEquals("Document title", CDAConstants.TITLE, (String) clinDoc.getTitle().getContent().get(0));

            // Class code - hard coded value used - override from doc
            assertEquals("Class code", ActClassClinicalDocument.DOCCLIN, clinDoc.getClassCode());
        }
        catch(Throwable t)
        {
            t.printStackTrace();
            fail(t.getMessage());
        }
    }

    private StorePtConsentRequestType loadDocument(String serializedDoc)
    {
        StorePtConsentRequestType request = null;
        String contextPath = "gov.hhs.fha.nhinc.common.nhinccommonadapter";
        Object unmarshalledObject = null;
        try
        {
            JAXBContext jc = JAXBContext.newInstance(contextPath);
            javax.xml.bind.Unmarshaller unmarshaller = jc.createUnmarshaller();
            StringReader stringReader = new StringReader(serializedDoc);
            unmarshalledObject = unmarshaller.unmarshal(stringReader);
            if (unmarshalledObject instanceof JAXBElement)
            {
                JAXBElement jaxb = (JAXBElement) unmarshalledObject;
                unmarshalledObject = jaxb.getValue();
            }
        } catch (Exception e)
        {
            unmarshalledObject = null;
            e.printStackTrace();
            fail("Exception unmarshalling patient preferences: " + e.getMessage());
        }
        if(unmarshalledObject instanceof StorePtConsentRequestType)
        {
            request = (StorePtConsentRequestType)unmarshalledObject;
        }
        else
        {
            fail("Unmarshalled object is not a StorePtConsentRequestType. Is: " + ((unmarshalledObject == null) ? "null" : unmarshalledObject.getClass().getName()));
        }
        return request;

    }

    private static final String STORE_CONSENT_REQUEST_FULL =
        "<urn:StorePtConsentRequest  xmlns:urn=\"urn:gov:hhs:fha:nhinc:common:nhinccommonadapter\">" +
        " <urn:patientPreferences>" +
        "	<urn:patientId>ADPTPIPTST98769876Z</urn:patientId>" +
        "	<urn:assigningAuthority>1.1</urn:assigningAuthority>" +
        "	<urn:optIn>false</urn:optIn>" +
        "	<urn:fineGrainedPolicyMetadata>" +
        "	   <urn:policyOID>policy_oid</urn:policyOID>" +
        "	   <urn:authorPerson>author_person</urn:authorPerson>" +
        "	   <urn:authorInstitution>author_institution</urn:authorInstitution>" +
        "	   <urn:authorRole>author_role</urn:authorRole>" +
        "	   <urn:authorSpecialty>author_specialty</urn:authorSpecialty>" +
        "	   <urn:availabilityStatus>Approved</urn:availabilityStatus>" +
        "	   <urn:classCode>class_code</urn:classCode>" +
        "	   <urn:classCodeScheme>class_code_scheme</urn:classCodeScheme>" +
        "	   <urn:classCodeDisplayName>class_code_display_name</urn:classCodeDisplayName>" +
        "	   <urn:comments>le_comments</urn:comments>" +
        "	   <urn:confidentialityCode>confidentiality_code</urn:confidentialityCode>" +
        "	   <urn:confidentialityCodeScheme>confidentiality_code_scheme</urn:confidentialityCodeScheme>" +
        "	   <urn:confidentialityCodeDisplayName>confidentiality_code_display_name</urn:confidentialityCodeDisplayName>" +
        "	   <urn:creationTime>2009-09-23T21:14:27Z</urn:creationTime>" +
        "	   <urn:formatCode>format_code</urn:formatCode>" +
        "	   <urn:formatCodeScheme>format_code_scheme</urn:formatCodeScheme>" +
        "	   <urn:formatCodeDisplayName>format_code_display_name</urn:formatCodeDisplayName>" +
        "	   <urn:hash>le_hash</urn:hash>" +
        "	   <urn:healthcareFacilityCode>healthcare_facility_code</urn:healthcareFacilityCode>" +
        "	   <urn:healthcareFacilityCodeScheme>healthcare_facility_code_scheme</urn:healthcareFacilityCodeScheme>" +
        "	   <urn:healthcareFacilityCodeDisplayName>healthcare_facility_code_display_name</urn:healthcareFacilityCodeDisplayName>" +
        "	   <urn:languageCode>language_code</urn:languageCode>" +
        "	   <urn:legalAuthenticator>legal_authenticator</urn:legalAuthenticator>" +
        "	   <urn:mimeType>mime_type</urn:mimeType>" +
        "	   <urn:practiceSettingCode>practice_setting_code</urn:practiceSettingCode>" +
        "	   <urn:practiceSettingCodeScheme>practice_setting_code_scheme</urn:practiceSettingCodeScheme>" +
        "	   <urn:practiceSettingCodeDisplayName>practice_setting_code_display_name</urn:practiceSettingCodeDisplayName>" +
        "	   <urn:serviceStartTime>2009-09-23T21:14:27Z</urn:serviceStartTime>" +
        "	   <urn:serviceStopTime>2009-09-23T21:14:27Z</urn:serviceStopTime>" +
        "	   <urn:size>123</urn:size>" +
        "	   <urn:sourcePatientId>source_patient_id</urn:sourcePatientId>" +
        "	   <urn:Pid3>pid3_value</urn:Pid3>" +
        "	   <urn:Pid5>pid5_value</urn:Pid5>" +
        "	   <urn:Pid7>pid7_value</urn:Pid7>" +
        "	   <urn:Pid8>pid8_value</urn:Pid8>" +
        "	   <urn:Pid11>pid11_value</urn:Pid11>" +
        "	   <urn:documentTitle>document_title</urn:documentTitle>" +
        "	   <urn:typeCode>type_code</urn:typeCode>" +
        "	   <urn:typeCodeScheme>type_code_scheme</urn:typeCodeScheme>" +
        "	   <urn:typeCodeDisplayName>type_code_display_name</urn:typeCodeDisplayName>" +
        "	   <urn:documentUniqueId>document_unique_id</urn:documentUniqueId>" +
        "	   <urn:documentURI>document_uri</urn:documentURI>" +
        "	   <urn:eventCodes>" +
        "		  <urn:code>event_code_1</urn:code>" +
        "		  <urn:codingScheme>event_code_1_scheme</urn:codingScheme>" +
        "		  <urn:codeDisplayName>event_code_1_display_name</urn:codeDisplayName>" +
        "	   </urn:eventCodes>" +
        "	   <urn:eventCodes>" +
        "		  <urn:code>event_code_2</urn:code>" +
        "		  <urn:codingScheme>event_code_2_scheme</urn:codingScheme>" +
        "		  <urn:codeDisplayName>event_code_2_display_name</urn:codeDisplayName>" +
        "	   </urn:eventCodes>" +
        "	</urn:fineGrainedPolicyMetadata>" +
        " </urn:patientPreferences>" +
        "</urn:StorePtConsentRequest>";


    private static final String STORE_CONSENT_REQUEST_MINIMAL =
        "<urn:StorePtConsentRequest  xmlns:urn=\"urn:gov:hhs:fha:nhinc:common:nhinccommonadapter\">" +
        " <urn:patientPreferences>" +
        "	<urn:patientId>ADPTPIPTST98769876Z</urn:patientId>" +
        "	<urn:assigningAuthority>1.1</urn:assigningAuthority>" +
        "	<urn:optIn>false</urn:optIn>" +
        "	<urn:fineGrainedPolicyMetadata>" +
        "	   <urn:policyOID>policy_oid</urn:policyOID>" +
        "	   <urn:authorPerson>author_person</urn:authorPerson>" +
        "	   <urn:authorInstitution>author_institution</urn:authorInstitution>" +
        "	   <urn:authorRole>author_role</urn:authorRole>" +
        "	   <urn:authorSpecialty>author_specialty</urn:authorSpecialty>" +
        "	   <urn:availabilityStatus>Approved</urn:availabilityStatus>" +
        "	   <urn:classCode>class_code</urn:classCode>" +
        "	   <urn:classCodeScheme>class_code_scheme</urn:classCodeScheme>" +
        "	   <urn:classCodeDisplayName>class_code_display_name</urn:classCodeDisplayName>" +
        "	   <urn:comments>le_comments</urn:comments>" +
        "	   <urn:confidentialityCode>confidentiality_code</urn:confidentialityCode>" +
        "	   <urn:confidentialityCodeScheme>confidentiality_code_scheme</urn:confidentialityCodeScheme>" +
        "	   <urn:confidentialityCodeDisplayName>confidentiality_code_display_name</urn:confidentialityCodeDisplayName>" +
        "	   <urn:creationTime>2009-09-23T21:14:27Z</urn:creationTime>" +
        "	   <urn:formatCode>format_code</urn:formatCode>" +
        "	   <urn:formatCodeScheme>format_code_scheme</urn:formatCodeScheme>" +
        "	   <urn:formatCodeDisplayName>format_code_display_name</urn:formatCodeDisplayName>" +
        "	   <urn:hash>le_hash</urn:hash>" +
        "	   <urn:healthcareFacilityCode>healthcare_facility_code</urn:healthcareFacilityCode>" +
        "	   <urn:healthcareFacilityCodeScheme>healthcare_facility_code_scheme</urn:healthcareFacilityCodeScheme>" +
        "	   <urn:healthcareFacilityCodeDisplayName>healthcare_facility_code_display_name</urn:healthcareFacilityCodeDisplayName>" +
        "	   <urn:languageCode>language_code</urn:languageCode>" +
        "	   <urn:legalAuthenticator>legal_authenticator</urn:legalAuthenticator>" +
        "	   <urn:mimeType>mime_type</urn:mimeType>" +
        "	   <urn:practiceSettingCode>practice_setting_code</urn:practiceSettingCode>" +
        "	   <urn:practiceSettingCodeScheme>practice_setting_code_scheme</urn:practiceSettingCodeScheme>" +
        "	   <urn:practiceSettingCodeDisplayName>practice_setting_code_display_name</urn:practiceSettingCodeDisplayName>" +
        "	   <urn:serviceStartTime>2009-09-23T21:14:27Z</urn:serviceStartTime>" +
        "	   <urn:serviceStopTime>2009-09-23T21:14:27Z</urn:serviceStopTime>" +
        "	   <urn:size>123</urn:size>" +
        "	   <urn:sourcePatientId>source_patient_id</urn:sourcePatientId>" +
        "	   <urn:Pid3>pid3_value</urn:Pid3>" +
        "	   <urn:Pid5>pid5_value</urn:Pid5>" +
        "	   <urn:Pid7>pid7_value</urn:Pid7>" +
        "	   <urn:Pid8>pid8_value</urn:Pid8>" +
        "	   <urn:Pid11>pid11_value</urn:Pid11>" +
        "	   <urn:typeCode>type_code</urn:typeCode>" +
        "	   <urn:typeCodeScheme>type_code_scheme</urn:typeCodeScheme>" +
        "	   <urn:typeCodeDisplayName>type_code_display_name</urn:typeCodeDisplayName>" +
        "	   <urn:documentUniqueId>document_unique_id</urn:documentUniqueId>" +
        "	   <urn:documentURI>document_uri</urn:documentURI>" +
        "	   <urn:eventCodes>" +
        "		  <urn:code>event_code_1</urn:code>" +
        "		  <urn:codingScheme>event_code_1_scheme</urn:codingScheme>" +
        "		  <urn:codeDisplayName>event_code_1_display_name</urn:codeDisplayName>" +
        "	   </urn:eventCodes>" +
        "	   <urn:eventCodes>" +
        "		  <urn:code>event_code_2</urn:code>" +
        "		  <urn:codingScheme>event_code_2_scheme</urn:codingScheme>" +
        "		  <urn:codeDisplayName>event_code_2_display_name</urn:codeDisplayName>" +
        "	   </urn:eventCodes>" +
        "	</urn:fineGrainedPolicyMetadata>" +
        " </urn:patientPreferences>" +
        "</urn:StorePtConsentRequest>";

}