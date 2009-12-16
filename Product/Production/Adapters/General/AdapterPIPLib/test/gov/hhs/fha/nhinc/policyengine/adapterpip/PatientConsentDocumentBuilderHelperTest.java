package gov.hhs.fha.nhinc.policyengine.adapterpip;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryPackageType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import org.apache.commons.logging.Log;
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
 *
 * @author webbn
 */
@RunWith(JMock.class)
public class PatientConsentDocumentBuilderHelperTest {
    Mockery context = new JUnit4Mockery();

    public PatientConsentDocumentBuilderHelperTest() {
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

    @Test
    public void testCreateSubmitObjectRequestFull()
    {
        try
        {
            // Create Mock objects
            final Log mockLog = context.mock(Log.class);
            final String propertiesPath = "";
            final String documentUniqueId = "999.200.20.54";
            final String submissionSetUniqueId = "888.100.10.42";

            PatientConsentDocumentBuilderHelper builder = new PatientConsentDocumentBuilderHelper()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected String getPropertiesFilePath()
                {
                    return propertiesPath;
                }
                @Override
                protected synchronized String getOidFromProperty(String sPropertyName)
                {
                    if("documentuniqueid".equals(sPropertyName))
                    {
                        return documentUniqueId;
                    }
                    else if("submissionsetuniqueid".equals(sPropertyName))
                    {
                        return submissionSetUniqueId;
                    }
                    else
                    {
                        throw new IllegalArgumentException("Unexpected property key");
                    }
                }
            };

            // Set expectations
            context.checking(new Expectations(){{
                allowing (mockLog).isDebugEnabled();
                allowing (mockLog).isInfoEnabled();
                allowing (mockLog).debug(with(any(String.class)));
                allowing (mockLog).info(with(any(String.class)));
            }});

            String targetObject = ""; // If not empty/null, creates an association to this document id
            String sHomeCommunityId = "1.1"; // Used if patient id from patient preferences is not already HL7 2.x encoded
            String existingDocumentUniqueId = "123abc";
            PatientPreferencesType patientPreferences = loadPatientPreferences(STORE_CONSENT_REQUEST_FULL);
            SubmitObjectsRequest request = builder.createSubmitObjectRequest(targetObject, sHomeCommunityId, existingDocumentUniqueId, null, patientPreferences);

            assertNotNull("Generated request was null", request);
            RegistryObjectListType registryObjectList = request.getRegistryObjectList();
            assertNotNull("RegistryObjectList", registryObjectList);
            ExtrinsicObjectType extObject = getExtrinsicObject(registryObjectList);
            assertNotNull("Extrinsic object was null", extObject);
            // Document unique id
            assertEquals("Document unique id", existingDocumentUniqueId, extObject.getId());
            // Home community
            assertEquals("Home community id", sHomeCommunityId, extObject.getHome());
            // MIME type
            assertEquals("MIME type", "mime_type", extObject.getMimeType());
            // Creation time (time zone set in message)
            //validateSlotValue(extObject, CDAConstants.SLOT_NAME_CREATION_TIME, "200909266151427");
            // Language code
            validateSlotValue(extObject, CDAConstants.SLOT_NAME_LANGUAGE_CODE, "language_code");
            // Source patient id
            validateSlotValue(extObject, CDAConstants.SLOT_NAME_SOURCE_PATIENT_ID, "source_patient_id");
            // Source patient info
            validateSlotValue(extObject, CDAConstants.SLOT_NAME_SOURCE_PATIENT_INFO, "PID-3|pid3_value");
            validateSlotValue(extObject, CDAConstants.SLOT_NAME_SOURCE_PATIENT_INFO, "PID-5|pid5_value");
            validateSlotValue(extObject, CDAConstants.SLOT_NAME_SOURCE_PATIENT_INFO, "PID-7|pid7_value");
            validateSlotValue(extObject, CDAConstants.SLOT_NAME_SOURCE_PATIENT_INFO, "PID-8|pid8_value");
            validateSlotValue(extObject, CDAConstants.SLOT_NAME_SOURCE_PATIENT_INFO, "PID-11|pid11_value");
            // Document title
            assertFalse("Document title value list empty", extObject.getName().getLocalizedString().isEmpty());
            assertEquals("Document title", "document_title", extObject.getName().getLocalizedString().get(0).getValue());
            // Class code
            validateClassification("Class code", extObject, CDAConstants.XDS_CLASS_CODE_SCHEMA_UUID, CDAConstants.CLASSIFICATION_SCHEMA_CDNAME, CDAConstants.METADATA_CLASS_CODE, "2.16.840.1.113883.6.1", CDAConstants.METADATA_CLASS_CODE_DISPLAY_NAME);
            // Confidentiality code
            validateClassification("Confidentiality code", extObject, CDAConstants.PROVIDE_REGISTER_CONFIDENTIALITY_CODE_UUID, CDAConstants.CLASSIFICATION_SCHEMA_CDNAME, "confidentiality_code", "confidentiality_code_scheme", "confidentiality_code_display_name");
            // Healthcare facility type
            validateClassification("Healthcare facility type code", extObject, CDAConstants.PROVIDE_REGISTER_FACILITY_TYPE_UUID, CDAConstants.CLASSIFICATION_SCHEMA_CDNAME, CDAConstants.METADATA_NOT_APPLICABLE_CODE, CDAConstants.METADATA_NOT_APPLICABLE_CODE_SYSTEM, CDAConstants.METADATA_NOT_APPLICABLE_DISPLAY_NAME);
            // Practice setting
            validateClassification("Practice setting", extObject, CDAConstants.PROVIDE_REGISTER_PRACTICE_SETTING_CD_UUID, CDAConstants.CLASSIFICATION_SCHEMA_CDNAME, CDAConstants.METADATA_NOT_APPLICABLE_CODE, CDAConstants.METADATA_NOT_APPLICABLE_CODE_SYSTEM, CDAConstants.METADATA_NOT_APPLICABLE_DISPLAY_NAME);
            // Patient id
            validateExternalIdentifier("Patient id", extObject, CDAConstants.EBXML_RESPONSE_PATIENTID_IDENTIFICATION_SCHEME, "ADPTPIPTST98769876Z^^^&1.1&ISO");
            // Document unique id
            validateExternalIdentifier("Document unique id", extObject, CDAConstants.DOCUMENT_ID_IDENT_SCHEME, existingDocumentUniqueId);
            // Home community id
            validateExternalIdentifier("Home community id", registryObjectList, CDAConstants.PROVIDE_REGISTER_SUBMISSION_SET_SOURCE_ID_UUID, sHomeCommunityId);
            // Author
            validateClassification("Author person", extObject, CDAConstants.XDS_AUTHOR, CDAConstants.CLASSIFICATION_SLOT_AUTHOR_PERSON, "Author", "author_person", "Author");
            validateClassification("Author institution", extObject, CDAConstants.XDS_AUTHOR, CDAConstants.CLASSIFICATION_SLOT_AUTHOR_INSTITUTION, "Author", "author_institution", "Author");
            validateClassification("Author role", extObject, CDAConstants.XDS_AUTHOR, CDAConstants.CLASSIFICATION_SLOT_AUTHOR_ROLE, "Author", "author_role", "Author");
            validateClassification("Author specialty", extObject, CDAConstants.XDS_AUTHOR, CDAConstants.CLASSIFICATION_SLOT_AUTHOR_SPECIALTY, "Author", "author_specialty", "Author");
            // Event Code
            validateClassification("Event code 1", extObject, CDAConstants.XDS_EVENT_CODE_LIST_CLASSIFICATION, CDAConstants.CLASSIFICATION_SCHEMA_CDNAME, "event_code_1", "N/A", "");
            validateClassification("Event code 2", extObject, CDAConstants.XDS_EVENT_CODE_LIST_CLASSIFICATION, CDAConstants.CLASSIFICATION_SCHEMA_CDNAME, "event_code_2", "N/A", "");
            // Language code
            validateSlotValue(extObject, CDAConstants.SLOT_NAME_LANGUAGE_CODE, "language_code");
            // Legal authenticator
            validateSlotValue(extObject, CDAConstants.SLOT_NAME_LEGAL_AUTHENTICATOR, "legal_authenticator");
            // Service start time
            //validateSlotValue(extObject, CDAConstants.SLOT_NAME_SERVICE_START_TIME, "200909266151427");
            // Service stop time
            //validateSlotValue(extObject, CDAConstants.SLOT_NAME_SERVICE_STOP_TIME, "200909266151427");
            // Document URI
            validateSlotValue(extObject, CDAConstants.SLOT_NAME_URI, existingDocumentUniqueId);
            // Comments
            assertNotNull("Comments null", extObject.getDescription());
            assertFalse("Comments list empty", extObject.getDescription().getLocalizedString().isEmpty());
            assertEquals("Comments", "le_comments", extObject.getDescription().getLocalizedString().get(0).getValue());
            // Type code
            validateClassification("Type code", extObject, CDAConstants.CLASSIFICATION_SCHEMA_IDENTIFIER_TYPE_CODE, CDAConstants.CLASSIFICATION_SCHEMA_CDNAME, CDAConstants.METADATA_TYPE_CODE, CDAConstants.CODE_SYSTEM_LOINC_OID, CDAConstants.METADATA_TYPE_CODE_DISPLAY_NAME);
        }
        catch(Throwable t)
        {
            t.printStackTrace();
            fail(t.getMessage());
        }
    }

    @Test
    public void testCreateSubmitObjectRequestMinimal()
    {
        try
        {
            // Create Mock objects
            final Log mockLog = context.mock(Log.class);
            final String propertiesPath = "";
            final String documentUniqueId = "999.200.20.54";
            final String submissionSetUniqueId = "888.100.10.42";

            PatientConsentDocumentBuilderHelper builder = new PatientConsentDocumentBuilderHelper()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected String getPropertiesFilePath()
                {
                    return propertiesPath;
                }
                @Override
                protected synchronized String getOidFromProperty(String sPropertyName)
                {
                    if("documentuniqueid".equals(sPropertyName))
                    {
                        return documentUniqueId;
                    }
                    else if("submissionsetuniqueid".equals(sPropertyName))
                    {
                        return submissionSetUniqueId;
                    }
                    else
                    {
                        throw new IllegalArgumentException("Unexpected property key");
                    }
                }
            };

            // Set expectations
            context.checking(new Expectations(){{
                allowing (mockLog).isDebugEnabled();
                allowing (mockLog).isInfoEnabled();
                allowing (mockLog).debug(with(any(String.class)));
                allowing (mockLog).info(with(any(String.class)));
            }});

            String targetObject = ""; // If not empty/null, creates an association to this document id
            String sHomeCommunityId = "1.1"; // Used if patient id from patient preferences is not already HL7 2.x encoded
            String existingDocumentUniqueId = "123abc";
            PatientPreferencesType patientPreferences = loadPatientPreferences(STORE_CONSENT_REQUEST_MINIMAL);
            SubmitObjectsRequest request = builder.createSubmitObjectRequest(targetObject, sHomeCommunityId, existingDocumentUniqueId, null, patientPreferences);

            assertNotNull("Generated request was null", request);
            RegistryObjectListType registryObjectList = request.getRegistryObjectList();
            assertNotNull("RegistryObjectList", registryObjectList);
            ExtrinsicObjectType extObject = getExtrinsicObject(registryObjectList);
            assertNotNull("Extrinsic object was null", extObject);
            // Document unique id
            assertEquals("Document unique id", existingDocumentUniqueId, extObject.getId());
            // Home community
            assertEquals("Home community id", sHomeCommunityId, extObject.getHome());
            // MIME type
            assertEquals("MIME type", CDAConstants.PROVIDE_REGISTER_MIME_TYPE, extObject.getMimeType());
            // Creation time (set to current date by default)
            validateSlotHasValue(extObject, CDAConstants.SLOT_NAME_CREATION_TIME);
            // Language code
            validateSlotValue(extObject, CDAConstants.SLOT_NAME_LANGUAGE_CODE, CDAConstants.LANGUAGE_CODE_ENGLISH);
            // Source patient id (patient id and home community are used as default)
            validateSlotValue(extObject, CDAConstants.SLOT_NAME_SOURCE_PATIENT_ID, "ADPTPIPTST98769876Z^^^&1.1&ISO");
            // Document title
            assertFalse("Document title value list empty", extObject.getName().getLocalizedString().isEmpty());
            assertEquals("Document title", CDAConstants.TITLE, extObject.getName().getLocalizedString().get(0).getValue());
            // Class code
            validateClassification("Class code", extObject, CDAConstants.XDS_CLASS_CODE_SCHEMA_UUID, CDAConstants.CLASSIFICATION_SCHEMA_CDNAME, CDAConstants.METADATA_CLASS_CODE, "2.16.840.1.113883.6.1", CDAConstants.METADATA_CLASS_CODE_DISPLAY_NAME);
            // Confidentiality code
            validateClassification("Confidentiality code", extObject, CDAConstants.PROVIDE_REGISTER_CONFIDENTIALITY_CODE_UUID, CDAConstants.CLASSIFICATION_SCHEMA_CDNAME, "R", "2.16.840.1.113883.5.25", "Restricted");
            // Healthcare facility type
            validateClassification("Healthcare facility type code", extObject, CDAConstants.PROVIDE_REGISTER_FACILITY_TYPE_UUID, CDAConstants.CLASSIFICATION_SCHEMA_CDNAME, CDAConstants.METADATA_NOT_APPLICABLE_CODE, CDAConstants.METADATA_NOT_APPLICABLE_CODE_SYSTEM, CDAConstants.METADATA_NOT_APPLICABLE_DISPLAY_NAME);
            // Practice setting
            validateClassification("Practice setting", extObject, CDAConstants.PROVIDE_REGISTER_PRACTICE_SETTING_CD_UUID, CDAConstants.CLASSIFICATION_SCHEMA_CDNAME, CDAConstants.METADATA_NOT_APPLICABLE_CODE, CDAConstants.METADATA_NOT_APPLICABLE_CODE_SYSTEM, CDAConstants.METADATA_NOT_APPLICABLE_DISPLAY_NAME);
            // Patient id
            validateExternalIdentifier("Patient id", extObject, CDAConstants.EBXML_RESPONSE_PATIENTID_IDENTIFICATION_SCHEME, "ADPTPIPTST98769876Z^^^&1.1&ISO");
            // Document unique id
            validateExternalIdentifier("Document unique id", extObject, CDAConstants.DOCUMENT_ID_IDENT_SCHEME, existingDocumentUniqueId);
            // Home community id
            validateExternalIdentifier("Home community id", registryObjectList, CDAConstants.PROVIDE_REGISTER_SUBMISSION_SET_SOURCE_ID_UUID, sHomeCommunityId);
            // Document URI
            validateSlotValue(extObject, CDAConstants.SLOT_NAME_URI, existingDocumentUniqueId);
            // Comments
            assertTrue("Comments list not null or empty", ((extObject.getDescription() == null) || (extObject.getDescription().getLocalizedString().isEmpty())));
            // Type code
            validateClassification("Type code", extObject, CDAConstants.CLASSIFICATION_SCHEMA_IDENTIFIER_TYPE_CODE, CDAConstants.CLASSIFICATION_SCHEMA_CDNAME, CDAConstants.METADATA_TYPE_CODE, CDAConstants.CODE_SYSTEM_LOINC_OID, CDAConstants.METADATA_TYPE_CODE_DISPLAY_NAME);
        }
        catch(Throwable t)
        {
            t.printStackTrace();
            fail(t.getMessage());
        }
    }

    @Test
    public void testCreateSubmitObjectRequestNoMetadata()
    {
        try
        {
            // Create Mock objects
            final Log mockLog = context.mock(Log.class);
            final String propertiesPath = "";
            final String documentUniqueId = "999.200.20.54";
            final String submissionSetUniqueId = "888.100.10.42";

            PatientConsentDocumentBuilderHelper builder = new PatientConsentDocumentBuilderHelper()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected String getPropertiesFilePath()
                {
                    return propertiesPath;
                }
                @Override
                protected synchronized String getOidFromProperty(String sPropertyName)
                {
                    if("documentuniqueid".equals(sPropertyName))
                    {
                        return documentUniqueId;
                    }
                    else if("submissionsetuniqueid".equals(sPropertyName))
                    {
                        return submissionSetUniqueId;
                    }
                    else
                    {
                        throw new IllegalArgumentException("Unexpected property key");
                    }
                }
            };

            // Set expectations
            context.checking(new Expectations(){{
                allowing (mockLog).isDebugEnabled();
                allowing (mockLog).isInfoEnabled();
                allowing (mockLog).debug(with(any(String.class)));
                allowing (mockLog).info(with(any(String.class)));
            }});

            String targetObject = ""; // If not empty/null, creates an association to this document id
            String sHomeCommunityId = "1.1"; // Used if patient id from patient preferences is not already HL7 2.x encoded
            String existingDocumentUniqueId = "123abc";
            PatientPreferencesType patientPreferences = loadPatientPreferences(STORE_CONSENT_REQUEST_NO_METADATA);
            SubmitObjectsRequest request = builder.createSubmitObjectRequest(targetObject, sHomeCommunityId, existingDocumentUniqueId, null, patientPreferences);

            assertNotNull("Generated request was null", request);
            RegistryObjectListType registryObjectList = request.getRegistryObjectList();
            assertNotNull("RegistryObjectList", registryObjectList);
            ExtrinsicObjectType extObject = getExtrinsicObject(registryObjectList);
            assertNotNull("Extrinsic object was null", extObject);
            // Home community
            assertEquals("Home community id", sHomeCommunityId, extObject.getHome());
        }
        catch(Throwable t)
        {
            t.printStackTrace();
            fail(t.getMessage());
        }
    }

    private void validateSlotValue(ExtrinsicObjectType extObject, String slotName, String expectedValue)
    {
        boolean matchFound = false;
        String sLastResult = "<no values>";
        for(SlotType1 slot : extObject.getSlot())
        {
            if(slotName.equals(slot.getName()))
            {
                assertNotNull("Slot value list for " + slotName + " was null", slot.getValueList());
                for(String sValue : slot.getValueList().getValue())
                {
                    if(expectedValue.equals(sValue))
                    {
                        matchFound = true;
                        break;
                    }
                    else
                    {
                        sLastResult = sValue;
                    }
                }
            }
        }
        if(!matchFound)
        {
            fail("Slot (" + slotName + ") value (" + expectedValue + ") not found - last found: " + sLastResult);
        }
    }

    private void validateSlotHasValue(ExtrinsicObjectType extObject, String slotName)
    {
        for(SlotType1 slot : extObject.getSlot())
        {
            if(slotName.equals(slot.getName()))
            {
                assertNotNull("Slot value list for " + slotName + " was null", slot.getValueList());
                boolean resultFound = false;
                for(String sValue : slot.getValueList().getValue())
                {
                    if(NullChecker.isNotNullish(sValue))
                    {
                        resultFound = true;
                        break;
                    }
                }
                if(!resultFound)
                {
                    fail("Slot (" + slotName + ") had no values");
                }
            }
        }
    }

    private void validateClassification(String sItemName, ExtrinsicObjectType extObject, String sSchemaUUID, String sSlotName, String sCode, String sCodeScheme, String sDisplayName)
    {
        for(ClassificationType oClassification : extObject.getClassification())
        {
            if(sSchemaUUID.equals(oClassification.getClassificationScheme()))
            {
                if(sCode.equals(oClassification.getNodeRepresentation()))
                {
                    boolean foundValue = false;
                    String sLastValue = "<no value>";
                    for(SlotType1 slot : oClassification.getSlot())
                    {
                        if(sSlotName.equals(slot.getName()))
                        {
                            if(slot.getValueList() != null)
                            {
                                for(String sValue : slot.getValueList().getValue())
                                {
                                    if(sCodeScheme.equals(sValue))
                                    {
                                        foundValue = true;
                                        break;
                                    }
                                    else
                                    {
                                        sLastValue = sValue;
                                    }
                                }
                            }
                            if(foundValue)
                            {
                                break;
                            }
                        }
                    }
                    if(!foundValue)
                    {
                        fail("Failed to find " + sItemName + " code scheme (" + sCodeScheme + ") - last value: " + sLastValue);
                    }
                    // Display name
                    assertFalse(sItemName + " display name list empty", oClassification.getName().getLocalizedString().isEmpty());
                    assertEquals(sItemName + " display name", sDisplayName, oClassification.getName().getLocalizedString().get(0).getValue());
                    return;
                }
            }
        }
        fail("Failed to find " + sItemName + " classification");

    }

    private void validateExternalIdentifier(String sItemName, ExtrinsicObjectType extObject, String sScheme, String sValue)
    {
        for(ExternalIdentifierType externalIdentifier : extObject.getExternalIdentifier())
        {
            if(sScheme.equals(externalIdentifier.getIdentificationScheme()))
            {
                assertEquals(sItemName + " value", sValue, externalIdentifier.getValue());
                return;
            }
        }
        fail(sItemName + " - did not find external identifier");
    }

    private void validateExternalIdentifier(String sItemName, RegistryObjectListType registryObjectList, String sScheme, String sValue)
    {
        RegistryPackageType oRegistryPackage = getRegistryPackage(registryObjectList);
        for(ExternalIdentifierType externalIdentifier : oRegistryPackage.getExternalIdentifier())
        {
            if(sScheme.equals(externalIdentifier.getIdentificationScheme()))
            {
                assertEquals(sItemName + " value", sValue, externalIdentifier.getValue());
                return;
            }
        }
        fail(sItemName + " - did not find external identifier");
    }

    private ExtrinsicObjectType getExtrinsicObject(RegistryObjectListType registryObjectList)
    {
        ExtrinsicObjectType extObj = null;
        if(registryObjectList != null)
        {

            for(JAXBElement<? extends IdentifiableType> oIdentifiable : registryObjectList.getIdentifiable())
            {
                if(oIdentifiable.getValue() instanceof ExtrinsicObjectType)
                {
                    extObj = (ExtrinsicObjectType)oIdentifiable.getValue();
                    break;
                }
            }
        }
        return extObj;
    }

    private RegistryPackageType getRegistryPackage(RegistryObjectListType registryObjectList)
    {
        RegistryPackageType regPkg = null;
        if(registryObjectList != null)
        {

            for(JAXBElement<? extends IdentifiableType> oIdentifiable : registryObjectList.getIdentifiable())
            {
                if(oIdentifiable.getValue() instanceof RegistryPackageType)
                {
                    regPkg = (RegistryPackageType)oIdentifiable.getValue();
                    break;
                }
            }
        }
        return regPkg;
    }

    private PatientPreferencesType loadPatientPreferences(String serializedDoc)
    {
        PatientPreferencesType patientPreferences = null;
        String contextPath = "gov.hhs.fha.nhinc.common.nhinccommonadapter";
        Object unmarshalledObject = null;
        try
        {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext(contextPath);
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
            StorePtConsentRequestType request = (StorePtConsentRequestType)unmarshalledObject;
            patientPreferences = request.getPatientPreferences();
        }
        else
        {
            fail("Unmarshalled object is not a StorePtConsentRequestType. Is: " + ((unmarshalledObject == null) ? "null" : unmarshalledObject.getClass().getName()));
        }
        return patientPreferences;

    }

    private static final String STORE_CONSENT_REQUEST_FULL =
        "<urn:StorePtConsentRequest  xmlns:urn=\"urn:gov:hhs:fha:nhinc:common:nhinccommonadapter\" xmlns:urn1=\"urn:gov:hhs:fha:nhinc:common:nhinccommon\">>" +
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
        "	   <urn:creationTime>20090923211427</urn:creationTime>" +
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
        "	   <urn:serviceStartTime>20090923211427</urn:serviceStartTime>" +
        "	   <urn:serviceStopTime>20090923211427</urn:serviceStopTime>" +
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
        "		  <urn1:code>event_code_1</urn1:code>" +
        "		  <urn1:codeSystem>event_code_1_scheme</urn1:codeSystem>" +
        "		  <urn1:displayName>event_code_1_display_name</urn1:displayName>" +
        "	   </urn:eventCodes>" +
        "	   <urn:eventCodes>" +
        "		  <urn1:code>event_code_2</urn1:code>" +
        "		  <urn1:codeSystem>event_code_2_scheme</urn1:codeSystem>" +
        "		  <urn1:displayName>event_code_2_display_name</urn1:displayName>" +
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
        "	   <urn:availabilityStatus>Approved</urn:availabilityStatus>" +
        "	   <urn:classCode>class_code</urn:classCode>" +
        "	   <urn:classCodeScheme>class_code_scheme</urn:classCodeScheme>" +
        "	   <urn:classCodeDisplayName>class_code_display_name</urn:classCodeDisplayName>" +
        "	   <urn:formatCode>format_code</urn:formatCode>" +
        "	   <urn:formatCodeScheme>format_code_scheme</urn:formatCodeScheme>" +
        "	   <urn:formatCodeDisplayName>format_code_display_name</urn:formatCodeDisplayName>" +
        "	   <urn:documentUniqueId>document_unique_id</urn:documentUniqueId>" +
        "	</urn:fineGrainedPolicyMetadata>" +
        " </urn:patientPreferences>" +
        "</urn:StorePtConsentRequest>";

    private static final String STORE_CONSENT_REQUEST_NO_METADATA =
        "<urn:StorePtConsentRequest  xmlns:urn=\"urn:gov:hhs:fha:nhinc:common:nhinccommonadapter\">" +
        " <urn:patientPreferences>" +
        "	<urn:patientId>ADPTPIPTST98769876Z</urn:patientId>" +
        "	<urn:assigningAuthority>1.1</urn:assigningAuthority>" +
        "	<urn:optIn>false</urn:optIn>" +
        " </urn:patientPreferences>" +
        "</urn:StorePtConsentRequest>";
}