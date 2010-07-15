package gov.hhs.fha.nhinc.gateway.aggregator.document;

/**
 * This class contains constants used when looking at Document related
 * NHIN messages.
 * 
 * @author Les Westberg
 */
public class DocumentConstants 
{
    public static final String EBXML_DOCENTRY_PATIENT_ID = "$XDSDocumentEntryPatientId";
    public static final String EBXML_DOCENTRY_CLASS_CODE = "$XDSDocumentEntryClassCode";
    public static final String EBXML_DOCENTRY_CLASS_CODE_SCHEME = "$XDSDocumentEntryClassCodeScheme";
    public static final String EBXML_DOCENTRY_PRACTICE_SETTING_CODE = "$XDSDocumentEntryPracticeSettingCode";
    public static final String EBXML_DOCENTRY_PRACTICE_SETTING_CODE_SCHEME = "$XDSDocumentEntryPracticeSettingCodeScheme";
    public static final String EBXML_DOCENTRY_CREATION_TIME_FROM = "$XDSDocumentEntryCreationTimeFrom";
    public static final String EBXML_DOCENTRY_CREATION_TIME_TO = "$XDSDocumentEntryCreationTimeTo";
    public static final String EBXML_DOCENTRY_SERVICE_START_TIME_FROM = "$XDSDocumentEntryServiceStartTimeFrom";
    public static final String EBXML_DOCENTRY_SERVICE_START_TIME_TO = "$XDSDocumentEntryServiceStartTimeTo";
    public static final String EBXML_DOCENTRY_SERVICE_STOP_TIME_FROM = "$XDSDocumentEntryServiceStopTimeFrom";
    public static final String EBXML_DOCENTRY_SERVICE_STOP_TIME_TO = "$XDSDocumentEntryServiceStopTimeTo";
    public static final String EBXML_DOCENTRY_HEALTHCARE_FACILITY_TYPE_CODE = "$XDSDocumentEntryHealthcareFacilityTypeCode";
    public static final String EBXML_DOCENTRY_HEALTHCARE_FACILITY_TYPE_CODE_SCHEME = "$XDSDocumentEntryHealthcareFacilityTypeCodeScheme";
    public static final String EBXML_DOCENTRY_EVENT_CODE_LIST = "$XDSDocumentEntryEventCodeList";
    public static final String EBXML_DOCENTRY_EVENT_CODE_LIST_SCHEME = "$XDSDocumentEntryEventCodeListScheme";
    public static final String EBXML_DOCENTRY_CONFIDENTIALITY_CODE = "$XDSDocumentEntryConfidentialityCode";
    public static final String EBXML_DOCENTRY_CONFIDENTIALITY_CODE_SCHEME = "$XDSDocumentEntryConfidentialityCodeScheme";
    public static final String EBXML_DOCENTRY_FORMAT_CODE = "$XDSDocumentEntryFormatCode";
    public static final String EBXML_DOCENTRY_STATUS = "$XDSDocumentEntryStatus";

    public static final String XDS_QUERY_ID_FIND_DOCUMENTS = "urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d";
    
    public static final String XDS_QUERY_RESPONSE_OPTION_RETURN_TYPE_OBJECT_REF = "ObjectRef";
    public static final String XDS_QUERY_RESPONSE_OPTION_RETURN_TYPE_LEAF_CLASS = "LeafClass";
    
    public static final String EBXML_RESPONSE_REPOSITORY_UNIQUE_ID_SLOTNAME = "repositoryUniqueId";
    public static final String EBXML_RESPONSE_DOCID_IDENTIFICATION_SCHEME = "urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab";
    public static final String EBXML_RESPONSE_DOCID_NAME = "XDSDocumentEntry.uniqueId";
    public static final String EBXML_RESPONSE_PATIENTID_IDENTIFICATION_SCHEME = "urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427";
    public static final String EBXML_RESPONSE_PATIENTID_NAME = "XDSDocumentEntry.patientId";
    public static final String EBXML_RESPONSE_AUTHOR_CLASS_SCHEME = "urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d";
    public static final String EBXML_RESPONSE_AUTHOR_PERSON_SLOTNAME = "authorPerson";
    public static final String EBXML_RESPONSE_AUTHOR_INSTITUTION_SLOTNAME = "authorInstitution";
    public static final String EBXML_RESPONSE_AUTHOR_ROLE_SLOTNAME = "authorRole";
    public static final String EBXML_RESPONSE_AUTHOR_SPECIALTY_SLOTNAME = "authorSpecialty";
    public static final String EBXML_RESPONSE_CLASSCODE_CLASS_SCHEME = "urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a";
    public static final String EBXML_RESPONSE_CONFIDENTIALITYCODE_CLASS_SCHEME = "urn:uuid:f4f85eac-e6cb-4883-b524-f2705394840f";
    public static final String EBXML_RESPONSE_EVENTCODE_CLASS_SCHEME = "urn:uuid:2c6b8cb7-8b2a-4051-b291-b1ae6a575ef4";
    public static final String EBXML_RESPONSE_FORMATCODE_CLASS_SCHEME = "urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d";
    public static final String EBXML_RESPONSE_HEALTHCAREFACILITYTYPE_CLASS_SCHEME = "urn:uuid:f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1";
    public static final String EBXML_RESPONSE_PRACTICESETTING_CLASS_SCHEME = "urn:uuid:cccf5598-8b07-4b77-a05e-ae952c785ead";
    public static final String EBXML_RESPONSE_TYPECODE_CLASS_SCHEME = "urn:uuid:f0306f51-975f-434e-a61c-c59651d33983";
    public static final String EBXML_RESPONSE_CODE_CODESCHEME_SLOTNAME = "codingScheme";
    public static final String EBXML_RESPONSE_CREATIONTIME_SLOTNAME = "creationTime";
    public static final String EBXML_RESPONSE_HASH_SLOTNAME = "hash";
    public static final String EBXML_RESPONSE_INTENDEDRECIPIENTS_SLOTNAME = "intendedRecipient";
    public static final String EBXML_RESPONSE_LANGUAGECODE_SLOTNAME = "languageCode";
    public static final String EBXML_RESPONSE_LEGALAUTHENTICATOR_SLOTNAME = "legalAuthenticator";
    public static final String EBXML_RESPONSE_SERVICESTARTTIME_SLOTNAME = "serviceStartTime";
    public static final String EBXML_RESPONSE_SERVICESTOPTIME_SLOTNAME = "serviceStopTime";
    public static final String EBXML_RESPONSE_SIZE_SLOTNAME = "size";
    public static final String EBXML_RESPONSE_SOURCEPATIENTID_SLOTNAME = "sourcePatientId";
    public static final String EBXML_RESPONSE_SOURCEPATIENTINFO_SLOTNAME = "sourcePatientInfo";
    public static final String EBXML_RESPONSE_URI_SLOTNAME = "URI";
    public static final int EBXML_RESPONSE_URI_LINE_LENGTH = 128;
    
    public static final String XDS_QUERY_RESPONSE_EXTRINSIC_OBJCECT_OBJECT_TYPE = "urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1";
    
    public static final String XDS_QUERY_RESPONSE_STATUS_SUCCESS = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success";
    public static final String XDS_QUERY_RESPONSE_STATUS_FAILURE = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure";
    public static final String XDS_RETRIEVE_RESPONSE_STATUS_SUCCESS = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success";
    public static final String XDS_RETRIEVE_RESPONSE_STATUS_FAILURE = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure";
    
    public static final String XDS_RETRIEVE_ERRORCODE_REGISTRY_ERROR = "XDSRegistryError";
    public static final String XDS_RETRIEVE_CODECONTEXT_TIMEDOUT_MSG = "We did not recieve a response to a document query message from this NHIN gateway.";
    public static final String XDS_RETRIEVE_ERRORCODE_REPOSITORY_ERROR = "XDSRepositoryError";

    public static final String FAIL_TEXT = "FAIL";
    public static final String SUCCESS_TEXT = "SUCCESS";
    public static final String COMPLETE_TEXT = "COMPLETE";
    public static final String PENDING_TEXT = "PENDING";
    
}
