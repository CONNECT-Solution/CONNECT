/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.policyengine.adapter.pip;

/**
 * This class is used to hold the CDA constants that are used when creating the CDA document.
 *
 * @author Les Westberg
 */
public class CDAConstants {
    public static final String CDA_CLASS_CODE = "DOCCLIN";
    public static final String CDA_MOOD_CODE = "EVN";
    public static final String TYPE_ID_EXTENSION_POCD_HD000040 = "POCD_HD000040";
    public static final String TYPE_ID_ROOT = "2.16.840.1.113883.1.3";
    public static final String TEMPLATE_ID_ROOT_XDS_SD_DOCUMENT = "1.3.6.1.4.1.19376.1.2.20";
    public static final String TEMPLATE_ID_ROOT_MEDICAL_DOCUMENTS = "1.3.6.1.4.1.19376.1.5.3.1.1.1";
    public static final String TEMPLATE_ID_ROOT_CONSENT_TO_SHARE = "1.3.6.1.4.1.19376.1.5.3.1.1.7";
    public static final String TEMPLATE_ID_ROOT_AUTHOR_ORIGINAL = "1.3.6.1.4.1.19376.1.2.20.1";
    public static final String TEMPLATE_ID_ROOT_AUTHOR_SCANNER = "1.3.6.1.4.1.19376.1.2.20.2";
    public static final String TEMPLATE_ID_ROOT_DATA_ENTERER = "1.3.6.1.4.1.19376.1.2.20.3";
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

    public static final String METADATA_CLASS_CODE = "57017-6"; // Access Consent Policy LOINC Code
    public static final String ADHOC_QUERY_CLASS_CODE = "('57017-6')";
    public static final String METADATA_CLASS_CODE_DISPLAY_NAME = "Privacy Policy";
    public static final String METADATA_FORMAT_CODE_SYSTEM = "1.3.6.1.4.1.19376.1.2.3";
    public static final String METADATA_FORMAT_CODE_XACML = "urn:nhin:names:acp:XACML";
    // public static final String METADATA_FORMAT_CODE_XACML_DISPLAY_NAME = "Privacy Policy";
    public static final String METADATA_FORMAT_CODE_PDF = "urn:ihe:iti:xds-sd:pdf:2008";
    public static final String METADATA_FORMAT_CODE_SCANNED_TEXT = "urn:ihe:iti:xds-sd:text:2008";
    public static final String METADATA_EVENT_CODE_SYSTEM = "N/A";
    public static final String METADATA_NOT_APPLICABLE_CODE = "385432009";
    public static final String METADATA_NOT_APPLICABLE_CODE_SYSTEM = "2.16.840.1.113883.6.96";
    public static final String METADATA_NOT_APPLICABLE_DISPLAY_NAME = "Not Applicable";
    public static final String METADATA_TYPE_CODE = "57017-6"; // Access Consent Policy LOINC Code
    public static final String METADATA_TYPE_CODE_DISPLAY_NAME = "Privacy Policy";

    public static final String ADHOC_QUERY_REQUEST_BY_DOCUMENT_ID_UUID = "urn:uuid:5c4f972b-d56b-40ac-a5fc-c8ca9b40b9d4";
    public static final String ADHOC_QUERY_REQUEST_BY_PATIENT_ID_UUID = "urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d";
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

    // To build Assertion for Notification
    public static final String PERMISSION_DATE = "NotBeforeDate";
    public static final String EXPIRATION_DATE = "NotOnOrAfterDate";
    public static final String FIRST_NAME = "UserFirstName";
    public static final String LAST_NAME = "UserLastName";
    public static final String USER_NAME = "UserName";
    public static final String ORG_NAME = "UserOrganization";
    public static final String USER_ROLE_CD = "UserRoleCode";
    public static final String USER_ROLE_CD_SYSTEM = "UserRoleCodeSystem";
    public static final String USER_ROLE_CD_SYSTEM_NAME = "UserRoleCodeSystemName";
    public static final String USER_ROLE_DISPLAY_NAME = "UserRoleDisplayName";
    public static final String PURPOSE_FOR_USE_ROLE_CD = "PurposeOfUseRoleCode";
    public static final String PURPOSE_FOR_USE_CODE_SYSTEM = "PurposeOfUseCodeSystem";
    public static final String PURPOSE_FOR_USE_CODE_SYSTEM_NAME = "PurposeOfUseCodeSystemName";
    public static final String PURPOSE_FOR_USE_DISPLAY_NAME = "PurposeOfUseDisplayName";
    public static final String ACCESS_POLICY_CONSENT = "AccessPolicyConsent";
    public static final String INSTANCE_ACCESS_POLICY_CONSENT = "InstanceAccessPolicyConsent";
    public static final String SubscribeeCommunityList_PROPFILE_NAME = "SubscribeeCommunityList";
    public static final String EBXML_RESPONSE_PATIENTID_IDENTIFICATION_SCHEME = "urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427";
    public static final String ENTITY_NOTIFICATION_CONSUMER_ENDPOINT_URL = "EntityNotificationConsumerURL";

    // Provide Register Transaction constants
    public static final String PROVIDE_REGISTER_SLOT_NAME_PATIENT_ID = "XDSDocumentEntry.patientId";
    public static final String PROVIDE_REGISTER_SLOT_NAME_DOCUMENT_ID = "XDSDocumentEntry.uniqueId";
    public static final String PROVIDE_REGISTER_SLOT_NAME_DOC_SUBMISSION_SET_PATIENT_ID = "XDSSubmissionSet.patientId";
    public static final String PROVIDE_REGISTER_SLOT_NAME_DOC_SUBMISSION_SET_DOCUMENT_ID = "XDSSubmissionSet.uniqueId";
    public static final String PROVIDE_REGISTER_SLOT_NAME_SUBMISSION_SET_SOURCE_ID = "XDSSubmissionSet.sourceId";
    public static final String PROVIDE_REGISTER_MIME_TYPE = "text/xml";
    public static final String PROVIDE_REGISTER_STATUS_APPROVED = "Approved";
    public static final String PROVIDE_REGISTER_OBJECT_TYPE = "urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1";
    public static final String SLOT_NAME_CREATION_TIME = "creationTime";
    public static final String SLOT_NAME_LANGUAGE_CODE = "languageCode";
    public static final String SLOT_NAME_LEGAL_AUTHENTICATOR = "legalAuthenticator";
    public static final String SLOT_NAME_SOURCE_PATIENT_INFO = "sourcePatientInfo";
    public static final String SLOT_NAME_SERVICE_START_TIME = "serviceStartTime";
    public static final String SLOT_NAME_SERVICE_STOP_TIME = "serviceStopTime";
    public static final String SLOT_NAME_URI = "URI";
    public static final String SLOT_NAME_SIZE = "size";
    public static final String SLOT_NAME_INTENDED_RECIPIENT = "intendedRecipient";

    public static final String CHARACTER_SET = "UTF-8";
    public static final String EXTERNAL_OBJECT_IDENTIFIER_TYPE = "urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExternalIdentifier";
    public static final String EXTERNAL_IDENTIFICATION_SCHEMA_UNIQUEID = "urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8";
    public static final String EXTERNAL_IDENTIFICATION_SCHEMA_PATIENTID = "urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446";
    public static final String EXTERNAL_IDENTIFICATION_SCHEMA_REGISTRYOBJECT = "SubmissionSet01";
    public static final String CLASSIFICATION_SCHEMA_IDENTIFIER_CDAR2 = "urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d";
    public static final String CLASSIFICATION_SCHEMA_IDENTIFIER_FORMAT_CODE = "urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d";
    public static final String CLASSIFICATION_SCHEMA_IDENTIFIER_TYPE_CODE = "urn:uuid:f0306f51-975f-434e-a61c-c59651d33983";
    public static final String NODE_REPRESENTATION_CDAR2 = "CDAR2/IHE 1.0";
    public static final String CDAR2_VALUE = "Connect-a-thon formatCodes";
    public static final String CLASSIFICATION_REGISTRY_OBJECT = "urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification";
    public static final String CLASSIFICATION_SCHEMA_CDNAME = "codingScheme";
    public static final String CLASSIFICATION_SCHEMA_AUTHOR_IDENTIFIER = "urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d";
    public static final String XDS_EVENT_CODE_LIST_CLASSIFICATION = "urn:uuid:2c6b8cb7-8b2a-4051-b291-b1ae6a575ef4";
    public static final String XDS_REGISTRY_ASSOCIATION_TYPE = "urn:oasis:names:tc:ebxml-regrep:AssociationType:HasMember";
    public static final String XDS_REGISTRY_ASSOCIATION_OBJECT_TYPE = "urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Association";
    public static final String XDS_REGISTRY_REGISTRY_PACKAGE_TYPE = "urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:RegistryPackage";
    public static final String XDS_REGISTRY_SLOT_NAME_SUBMISSION_TIME = "submissionTime";
    public static final String XDS_CLASS_CODE_SCHEMA_UUID = "urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a";
    public static final String XDS_REGISTRY_CONTENT_TYPE_UUID = "urn:uuid:aa543740-bdda-424e-8c96-df4873be8500";
    public static final String XDS_REGISTRY_ASSOCIATION_TYPE_RPLC = "urn:oasis:names:tc:ebxml-regrep:AssociationType:RPLC";
    public static final String XDS_REGISTRY_ASSOCIATION_TYPE_TARGET_OBJECT = "urn:uuid:6f5d07b7-81f4-4865-836a-c2d9134e1687";
    public static final String XDS_AUTHOR = "urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d";
    public static final String CLASSIFICATION_SLOT_AUTHOR_PERSON = "authorPerson";
    public static final String CLASSIFICATION_SLOT_AUTHOR_INSTITUTION = "authorInstitution";
    public static final String CLASSIFICATION_SLOT_AUTHOR_ROLE = "authorRole";
    public static final String CLASSIFICATION_SLOT_AUTHOR_SPECIALTY = "authorSpecialty";
    public static final String PROVIDE_REGISTER_SUBMISSION_SET_SOURCE_ID_UUID = "urn:uuid:554ac39e-e3fe-47fe-b233-965d2a147832";
    public static final String XDS_RETRIEVE_RESPONSE_STATUS_SUCCESS = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success";
    public static final String PROVIDE_REGISTER_CONFIDENTIALITY_CODE_UUID = "urn:uuid:f4f85eac-e6cb-4883-b524-f2705394840f";
    public static final String PROVIDE_REGISTER_FACILITY_TYPE_UUID = "urn:uuid:f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1";
    public static final String PROVIDE_REGISTER_PRACTICE_SETTING_CD_UUID = "urn:uuid:cccf5598-8b07-4b77-a05e-ae952c785ead";
    public static final String PROVIDE_REGISTER_SUBMISSION_SET_CLASSIFICATION_UUID = "urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd";
}
