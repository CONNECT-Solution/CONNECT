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
package gov.hhs.fha.nhinc.callback;

/**
 *
 * @author Neil Webb
 */
public class SamlConstants {
    // SAML Constants

    public static final String ACTION_PROP = "action";
    public static final String RESOURCE_PROP = "resource";
    public static final String PURPOSE_CODE_PROP = "purposeOfUseRoleCode";
    public static final String PURPOSE_SYST_PROP = "purposeOfUseCodeSystem";
    public static final String PURPOSE_SYST_NAME_PROP = "purposeOfUseCodeSystemName";
    public static final String PURPOSE_DISPLAY_PROP = "purposeOfUseDisplayName";
    public static final String USER_FIRST_PROP = "userFirstName";
    public static final String USER_MIDDLE_PROP = "userMiddleName";
    public static final String USER_LAST_PROP = "userLastName";
    public static final String USER_NAME_PROP = "userName";
    public static final String USER_ORG_PROP = "userOrganization";
    public static final String USER_ORG_ID_PROP = "userOrganizationID";
    public static final String HOME_COM_PROP = "homeCommunity";
    public static final String PATIENT_ID_PROP = "patientID";
    public static final String USER_CODE_PROP = "userRoleCode";
    public static final String USER_SYST_PROP = "userRoleCodeSystem";
    public static final String USER_SYST_NAME_PROP = "userRoleCodeSystemName";
    public static final String USER_DISPLAY_PROP = "userRoleCodeDisplayName";
    public static final String URN_OASIS_NAMES = "urn:oasis:names:tc:SAML:1.1:nameid-format:entity";
    public static final String SECURITY_ASSERTION_SPPROVIDEDID = "Security/Assertion/Issuer/@SPProvidedID";
    public static final String SECURITY_ASSERTION_ISSUER_FORMAT = "SOAP header element Security/Assertion/Issuer/@Format  =";
    public static final String SECURITY_ASSERTION_NAME_QUALIFIER = "Security/Assertion/Issuer/@NameQualifier";
    public static final String SECURITY_ASSERTION_SPNAME_QUALIFIER = "Security/Assertion/Issuer/@SPNameQualifier";
    public static final String AUTHN_STATEMENT_EXISTS_PROP = "authnStatementExists";
    public static final String AUTHN_INSTANT_PROP = "authnInstant";
    public static final String AUTHN_SESSION_INDEX_PROP = "authnSessionIndex";
    public static final String AUTHN_CONTEXT_CLASS_PROP = "authnContextClass";
    public static final String SUBJECT_LOCALITY_ADDR_PROP = "subjectLocalityAddress";
    public static final String SUBJECT_LOCALITY_DNS_PROP = "subjectLocalityDNS";
    public static final String SAMLCONDITIONS_NOT_BEFORE_PROP = "samlConditionsNotBefore";
    public static final String SAMLCONDITIONS_NOT_AFTER_PROP = "samlConditionsNotAfter";
    public static final String AUTHZ_DECISION_PROP = "authzDecision";
    public static final String AUTHZ_STATEMENT_EXISTS_PROP = "authzStatementExists";
    public static final String ASSERTION_ISSUER_PROP = "assertionIssuer";
    public static final String ASSERTION_ISSUER_FORMAT_PROP = "assertionIssuerFormat";
    public static final String EVIDENCE_ID_PROP = "evidenceAssertionId";
    public static final String EVIDENCE_INSTANT_PROP = "evidenceAssertionInstant";
    public static final String EVIDENCE_VERSION_PROP = "evidenceAssertionVersion";
    public static final String EVIDENCE_ISSUER_PROP = "evidenceAssertionIssuer";
    public static final String EVIDENCE_ISSUER_FORMAT_PROP = "evidenceAssertionIssuerFormat";
    public static final String EVIDENCE_SUBJECT_PROP = "evidenceSubject";
    public static final String EVIDENCE_CONDITION_NOT_BEFORE_PROP = "evidenceConditionNotBefore";
    public static final String EVIDENCE_CONDITION_NOT_AFTER_PROP = "evidenceConditionNotAfter";
    public static final String EVIDENCE_ACCESS_CONSENT_PROP = "evidenceAccessConsent";
    public static final String EVIDENCE_INST_ACCESS_CONSENT_PROP = "evidenceInstanceAccessConsent";
    public static final String AUDIT_QUERY_ACTION = "queryAuditLog";
    public static final String NOTIFY_ACTION = "notify";
    public static final String SUBSCRIBE_ACTION = "subscribe";
    public static final String UNSUBSCRIBE_ACTION = "unsubscribe";
    public static final String DOC_QUERY_ACTION = "queryDocuments";
    public static final String DOC_RETRIEVE_ACTION = "retrieveDocuments";
    public static final String PATIENT_DISCOVERY_ACTION = "patientDiscovery";
    public static final String ADAPTER_XDR_ACTION = "adapterXDRSecured";
    public static final String ADAPTER_XDRREQUEST_SECURED_ACTION = "adapterXDRRequestSecured";
    public static final String ADAPTER_XDRRESPONSE_SECURED_ACTION = "adapterXDRResponseSecured";
    public static final String ENTITY_XDR_SECURED_RESPONSE_ACTION = "entityXDRSecuredResponse";
    public static final String AUDIT_REPO_ACTION = "auditrepository";
    public static final String POLICY_ENGINE_ACTION = "policyengine";
    public static final String PAT_CORR_ACTION = "patientcorrelation";
    public static final String ADAPTER_MPI_ACTION = "mpi";
    public static final String XDR_ACTION = "xdr";
    public static final String XDR_REQUEST_ACTION = "xdrrequest";
    public static final String XDR_RESPONSE_ACTION = "xdrresponse";
    public static final String USER_SYST_ATTR = "2.16.840.1.113883.6.96";
    public static final String USER_SYST_NAME_ATTR = "SNOMED_CT";
    public static final String PURPOSE_SYSTEM_ATTR = "2.16.840.1.113883.3.18.7.1";
    public static final String PURPOSE_SYSTEMNAME_ATTR = "nhin-purpose";
    public static final String HL7_NAMESPACE_URI = "urn:hl7-org:v3";
    public static final String HL7_PREFIX = "hl7";
    public static final String HL7_TYPE_NAMESPACE_URI = "http://www.w3.org/2001/XMLSchema-instance";
    public static final String HL7_TYPE_LOCAL_PART = "type";
    public static final String HL7_TYPE_PREFIX = "xsi";
    public static final String HL7_TYPE_KEY_VALUE = "hl7:CE";
    public static final String ATTRIBUTE_NAME_NPI = "urn:oasis:names:tc:xspa:2.0:subject:npi";
    public static final String ACCESS_CONSENT_ATTR = "AccessConsentPolicy";
    public static final String INST_ACCESS_CONSENT_ATTR = "InstanceAccessConsentPolicy";
    public static final String CE_CODE_ID = "code";
    public static final String CE_CODESYS_ID = "codeSystem";
    public static final String CE_CODESYSNAME_ID = "codeSystemName";
    public static final String CE_DISPLAYNAME_ID = "displayName";
    public static final String SAML2_ASSERTION_NS = "urn:oasis:names:tc:SAML:2.0:assertion";
    public static final String SAML2_ASSERTION_TAG = "Assertion";
    public static final String XML_SIGNATURE_NS = "http://www.w3.org/2000/09/xmldsig#";
    public static final String DIGEST_VALUE_TAG = "DigestValue";
    public static final String SIGNATURE_TAG = "Signature";
    public static final String SIGNATURE_VALUE_TAG = "SignatureValue";
    public static final String SUBJECT_CONFIRMATION = "SubjectConfirmation";
    public static final String ATTRIBUTE_NAME_XUA_ACP = "urn:ihe:iti:xua:2012:acp";
    public static final String ATTRIBUTE_NAME_XUA_IACP = "urn:ihe:iti:bppc:2007:docid";
    public static final String ACP_ATTRIBUTE_PROP = "acpAttribute";
    public static final String IACP_ATTRIBUTE_PROP = "iacpAttribute";
    public static final String ATTRIBUTE_FRIENDLY_NAME_XUA_ACP = "Patient Privacy Policy Identifier";
    public static final String ATTRIBUTE_FRIENDLY_NAME_XUA_IACP = "Patient Privacy Policy Acknowledgement Document";
    public static final String URI_NAME_FORMAT = "urn:oasis:names:tc:SAML:2.0:attrname-format:uri";
    public static final String ADMIN_AUTH_METHOD = "urn:oasis:names:tc:SAML:2.0:ac:classes:Password";
    public static final String SIGNATURE_KEY = "Signatures";
    public static final String DIGEST_KEY = "Digests";
    public static final String SIG_ALGO_PROPERTY = "saml.signatureAlgorithms";
    public static final String DIG_ALGO_PROPERTY = "saml.digestAlgorithms";
    public static final String DEFAULT_DIG_ALGO_PROPERTY = "saml.defaultDigestAlgorithm";
    public static final String DEFAULT_SIG_ALGO_PROPERTY = "saml.defaultSignatureAlgorithm";

    // Authorization Framework
    public static final String AUTH_FRWK_NAME_ID_FORMAT_EMAIL_ADDRESS = "urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress";
    public static final String AUTH_FRWK_NAME_ID_FORMAT_X509 = "urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName";
    public static final String AUTH_FRWK_NAME_ID_FORMAT_WINDOWS_NAME = "urn:oasis:names:tc:SAML:1.1:nameid-format:WindowsDomainQualifiedName";
    // SAML Constants
    public static final String TARGET_API_LEVEL = "targetAPILevel";
    public static final String ISSUE_INSTANT_PROP = "issueInstant";
    // Attribute NameID Constants
    public static final String ATTRIBUTE_NAME_SUBJECT_ID = "urn:oasis:names:tc:xacml:1.0:subject:subject-id";
    public static final String ATTRIBUTE_NAME_SUBJECT_ID_XSPA = "urn:oasis:names:tc:xspa:1.0:subject:subject-id";
    public static final String ATTRIBUTE_NAME_ORG = "urn:oasis:names:tc:xspa:1.0:subject:organization";
    public static final String ATTRIBUTE_NAME_ORG_ID = "urn:oasis:names:tc:xspa:1.0:subject:organization-id";
    public static final String ATTRIBUTE_NAME_HCID = "urn:nhin:names:saml:homeCommunityId";
    public static final String ATTRIBUTE_NAME_SUBJECT_ROLE = "urn:oasis:names:tc:xacml:2.0:subject:role";
    public static final String ATTRIBUTE_NAME_PURPOSE_OF_USE = "urn:oasis:names:tc:xspa:1.0:subject:purposeofuse";
    public static final String ATTRIBUTE_NAME_RESOURCE_ID = "urn:oasis:names:tc:xacml:2.0:resource:resource-id";
    // SAML constants
    public static final String SAML_DEFAULT_ISSUER_NAME = "CN=SAML User,OU=SU,O=SAML User,L=Los Angeles,ST=CA,C=US";
    public static final String ACTION_NAMESPACE_STRING = "urn:oasis:names:tc:SAML:1.0:action:rwedc";
    public static final String ISSUER_KEY_STRING = "org.apache.ws.security.saml.issuer.key.name";
    public static final String ISSUER_KEY_VALUE = "org.apache.ws.security.saml.issuer.key.password";
    public static final String SIGNATURE_PROPERTIES_STRING = "org.apache.ws.security.saml.issuer.cryptoProp.file";
    public static final String SIGN_ASSERTION_BOOL = "org.apache.ws.security.saml.issuer.signAssertion";
    public static final String SEND_KEYVALUE_BOOL = "org.apache.ws.security.saml.issuer.sendKeyValue";

    public static final String VALIDATE_ATTRIBUTES_PROP = "validateSAMLAttributes";

    // Flag to enable SAML Conditions element default value
    public static final String ENABLE_CONDITIONS_DEFAULT_VALUE = "enableConditionsDefaultValue";

    public static final String ALLOW_NO_SUBJECT_ASSERTION_PROP = "allowNoSubjectAssertion";
}
