package gov.hhs.fha.nhinc.document;

import org.junit.Ignore;

/**
 * This class is used to hold the CDA constants that are used when creating the
 * CDA document.
 *
 * @author Les Westberg
 */
@Ignore
public class CDAConstants
{
    public static final String TYPE_ID_EXTENSION_POCD_HD000040 = "POCD_HD000040";
    public static final String TYPE_ID_ROOT = "2.16.840.1.113883.1.3";
    public static final String TEMPLATE_ID_ROOT_MEDICAL_DOCUMENTS = "1.3.6.1.4.1.19376.1.5.3.1.1.1";
    public static final String TEMPLATE_ID_ROOT_CONSENT_TO_SHARE = "1.3.6.1.4.1.19376.1.5.3.1.1.7";
    public static final String CODE_SYSTEM_LOINC_OID = "2.16.840.1.113883.6.1";
    public static final String CODE_SYSTEM_NAME_LOINC = "LOINC";
    public static final String TITLE_TAG = "title";
    public static final String TEXT_TAG = "text";
    public static final String TITLE = "Consent to Share Information";
    public static final String CONFIDENTIALITY_CODE_NORMAL = "N";
    public static final String CONFIDENTIALITY_CODE_NORMAL_DISPLAY_NAME = "Normal";
    public static final String CONFIDENTIALITY_CODE_SYSTEM = "2.16.840.1.113883.5.25";
    public static final String CONFIDENTIALITY_CODE_SYSTEM_DISPLAY_NAME = "Confidentiality";
    public static final String LANGUAGE_CODE_ENGLISH = "en-US";
    public static final String DOCUMENTATION_OF_TYPE_CODE = "DOC";
    public static final String SERVICE_EVENT_CLASS_CODE_ACT = "ACT";
    public static final String SERVICE_EVENT_MOOD_CODE_EVENT = "EVN";
    public static final String SERVICE_EVENT_TEMPLATE_ID_ROOT = "1.3.6.1.4.1.19376.1.5.3.1.2.6";
    public static final String CONSENT_CODE_YES = "417370002";
    public static final String CONSENT_CODE_YES_DISPLAY_NAME = "Consent Given for Upload to National Shared Electronic Record";
    public static final String CONSENT_CODE_NO = "416308001";
    public static final String CONSENT_CODE_NO_DISPLAY_NAME = "Refused Consent for Upload to National Shared Electronic Record";
    public static final String SNOMED_CT_CODE_SYSTEM = "2.16.840.1.113883.6.96";
    public static final String SNOMED_CT_CODE_SYSTEM_DISPLAY_NAME = "SNOMED CT";
    public static final String STATUS_APPROVED_QUERY_VALUE = "('urn:oasis:names:tc:ebxml-regrep:StatusType:Approved')";
    public static final String STATUS_APPROVED_STORE_VALUE = "urn:oasis:names:tc:ebxml-regrep:StatusType:Approved";

    public static final String METADATA_CLASS_CODE = "Consent";
    public static final String METADATA_CLASS_CODE_DISPLAY_NAME = "Consent";
    public static final String METADATA_FORMAT_CODE = "urn:ihe:iti:bppc:2007";
    public static final String METADATA_FORMAT_CODE_SYSTEM = "1.3.6.1.4.1.19376.1.2.3";

    public static final String ADHOC_QUERY_REQUEST_UUID = "urn:uuid:5c4f972b-d56b-40ac-a5fc-c8ca9b40b9d4";
    public static final String ADHOC_QUERY_REQUEST_LEAF_CLASS = "LeafClass";
    public static final String ADHOC_QUERY_REQUEST_SLOT_NAME_CPP_PATIENT_ID = "$XDSDocumentEntryPatientId";
    public static final String XDS_PROVIDE_REGISTER_SLOT_NAME_PATIENT_ID = "$XDSSubmissionSetPatientId";
    public static final String ADHOC_QUERY_REQUEST_SLOT_NAME_DOCUMENT_CLASS_CODE = "$XDSDocumentEntryClassCode";
    public static final String ADHOC_QUERY_REQUEST_SLOT_NAME_STATUS = "$XDSDocumentEntryStatus";

    public static final String SLOT_NAME_REPOSITORY_UNIQUE_ID = "repositoryUniqueId";
    public static final String DOCUMENT_ID_IDENT_SCHEME = "urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab";
    public static final String SLOT_NAME_DOC_RETRIEVE_REPOSITORY_UNIQUE_ID = "$XDSRepositoryUniqueId";
    public static final String SLOT_NAME_DOC_RETRIEVE_DOCUMENT_ID = "$XDSDocumentEntryUniqueId";
    public static final String SLOT_NAME_DOC_SUBMISSION_SET_DOCUMENT_ID = "$XDSSubmissionSetUniqueId";
    public static final String SLOT_NAME_DOC_ENTRY_EVENT_CODE_LIST = "$XDSDocumentEntryEventCodeList";
    public static final String SLOT_NAME_SOURCE_PATIENT_ID = "sourcePatientId";

    public static final String DOC_REGISTRY_SERVICE_NAME = "adapterxdsbdocregistry";
    public static final String DOC_REPOSITORY_SERVICE_NAME = "adapterxdsbdocrepository";

    public static final String DOC_REGISTRY_SERVICE_DEFAULT_URL = "http://localhost:8080/DocumentRegistry_Service";
    public static final String DOC_REPOSITORY_SERVICE_DEFAULT_URL = "http://localhost:8080/DocumentRepository_Service";

    //To build Assertion for Notification
    public static final String DATE_OF_SIGNATURE = "SignDate";
    public static final String EXPIRATION_DATE = "ExpirationDate";
    public static final String FIRST_NAME = "UserFirstName";
    public static final String LAST_NAME = "UserLastName";
    public static final String USER_NAME = "UserName";
    public static final String ORG_NAME = "UserOrganization";
    public static final String USER_ROLE_CD = "UserRoleCode";
    public static final String USER_ROLE_CD_SYSTEM = "UserRoleCodeSystem";
    public static final String USER_ROLE_CD_SYSTEM_NAME = "UserRoleCodeSystemName";
    public static final String USER_ROLE_DISPLAY_NAME = "UserRoleDisplayName";
    public static final String PURPOSE_FOR_USE_ROLE_CD = "PurposeForUseRoleCode";
    public static final String PURPOSE_FOR_USE_CODE_SYSTEM = "PurposeForUseCodeSystem";
    public static final String PURPOSE_FOR_USE_CODE_SYSTEM_NAME = "PurposeForUseCodeSystemName";
    public static final String PURPOSE_FOR_USE_DISPLAY_NAME = "PurposeForUseDisplayName";
    public static final String CONTENT_REFERENCE = "ContentReference";
    public static final String CONTENT = "Content";
    public static final String SubscribeeCommunityList_PROPFILE_NAME = "SubscribeeCommunityList";
    public static final String EBXML_RESPONSE_PATIENTID_IDENTIFICATION_SCHEME = "urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427";
    public static final String ENTITY_NOTIFICATION_CONSUMER_ENDPOINT_URL ="EntityNotificationConsumerURL";
    
    //Provide Register Transaction constants
    public static final String PROVIDE_REGISTER_MIME_TYPE = "text/xml";
    public static final String PROVIDE_REGISTER_OBJECT_TYPE = "urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1";
    public static final String SLOT_NAME_CREATION_TIME = "creationTime";
    public static final String SLOT_NAME_LANGUAGE_CODE = "languageCode";
    public static final String CHARACTER_SET = "UTF-8";
    public static final String EXTERNAL_OBJECT_IDENTIFIER_TYPE = "urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExternalIdentifier";
    public static final String EXTERNAL_IDENTIFICATION_SCHEMA_UNIQUEID = "urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8";
    public static final String EXTERNAL_IDENTIFICATION_SCHEMA_PATIENTID = "urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446";
    public static final String EXTERNAL_IDENTIFICATION_SCHEMA_REGISTRYOBJECT = "SubmissionSet01";
    public static final String CLASSIFICATION_SCHEMA_IDENTIFIER_CDAR2 = "urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d";
    public static final String NODE_REPRESENTATION_CDAR2 = "CDAR2/IHE 1.0";
    public static final String CDAR2_VALUE = "Connect-a-thon formatCodes";
    public static final String CLASSIFICATION_REGISTRY_OBJECT = "urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification";
    public static final String CLASSIFICATION_SCHEMA_CDNAME = "codingScheme";
    public static final String CLASSIFICATION_SCHEMA_AUTHOR_IDENTIFIER = "urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d";
    //public static final String XDS_EVENT_CODE_LIST_CLASSIFICATION = "urn:uuid:2c6b8cb7-8b2a-4051-b291-b1ae6a575ef4";
    public static final String XDS_REGISTRY_ASSOCIATION_TYPE = "urn:oasis:names:tc:ebxml-regrep:AssociationType:HasMember";
    public static final String XDS_REGISTRY_ASSOCIATION_OBJECT_TYPE = "urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Association";
    public static final String XDS_REGISTRY_REGISTRY_PACKAGE_TYPE = "urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:RegistryPackage";
    public static final String XDS_REGISTRY_SLOT_NAME_SUBMISSION_TIME = "submissionTime";
    public static final String XDS_CLASS_CODE_SCHEMA_UUID="urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a";
    public static final String XDS_CLASS_CODE_CLASSIFICATION_ID_UUID="urn:uuid:f332b08a-0c99-4bf5-8af5-b770f";
    public static final String XDS_REGISTRY_CONTENT_TYPE_UUID = "urn:uuid:aa543740-bdda-424e-8c96-df4873be8500";
    public static final String XDS_REGISTRY_ASSOCIATION_TYPE_RPLC = "urn:oasis:names:tc:ebxml-regrep:AssociationType:RPLC";
    public static final String XDS_REGISTRY_ASSOCIATION_TYPE_TARGET_OBJECT = "urn:uuid:6f5d07b7-81f4-4865-836a-c2d9134e1687";
}
