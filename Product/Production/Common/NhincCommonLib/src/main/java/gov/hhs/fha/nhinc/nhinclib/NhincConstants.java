/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.nhinclib;

import java.math.BigInteger;

/**
 *
 * @author Jon Hoppesch
 */
public class NhincConstants {
    // Property File Constants

    public static final String GATEWAY_PROPERTY_FILE = "gateway";
    public static final String HOME_COMMUNITY_ID_PROPERTY = "localHomeCommunityId";
    public static final String NHINC_PROPERTIES_DIR = "NHINC_PROPERTIES_DIR";
    public static final String SPRING_CONFIG_FILE = "ComponentProxyConfig.xml";

    // SAML Constants
    public static final String ACTION_PROP = "action";
    public static final String RESOURCE_PROP = "resource";
    public static final String PURPOSE_CODE_PROP = "purposeForUseRoleCode";
    public static final String PURPOSE_SYST_PROP = "purposeForUseCodeSystem";
    public static final String PURPOSE_SYST_NAME_PROP = "purposeForUseCodeSystemName";
    public static final String PURPOSE_DISPLAY_PROP = "purposeForUseDisplayName";
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
    public static final String AUTHN_INSTANT_PROP = "authnInstant";
    public static final String AUTHN_SESSION_INDEX_PROP = "authnSessionIndex";
    public static final String AUTHN_CONTEXT_CLASS_PROP = "authnContextClass";
    public static final String SUBJECT_LOCALITY_ADDR_PROP = "subjectLocalityAddress";
    public static final String SUBJECT_LOCALITY_DNS_PROP = "subjectLocalityDNS";
    public static final String AUTHZ_DECISION_PROP = "authzDecision";
    public static final String EVIDENCE_ID_PROP = "evidenceAssertionId";
    public static final String EVIDENCE_INSTANT_PROP = "evidenceAssertionInstant";
    public static final String EVIDENCE_VERSION_PROP = "evidenceAssertionVersion";
    public static final String EVIDENCE_ISSUER_PROP = "evidenceAssertionIssuer";
    public static final String EVIDENCE_ISSUER_FORMAT_PROP = "evidenceAssertionIssuerFormat";
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
    public static final String SUBJECT_DISCOVERY_ACTION = "subjectDiscovery";
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
    public static final String USERNAME_ATTR = "urn:oasis:names:tc:xspa:1.0:subject:subject-id";
    public static final String USER_ORG_ATTR = "urn:oasis:names:tc:xspa:1.0:subject:organization";
    public static final String USER_ORG_ID_ATTR = "urn:oasis:names:tc:xspa:1.0:subject:organization-id";
    public static final String HOME_COM_ID_ATTR = "urn:nhin:names:saml:homeCommunityId";
    public static final String USER_ROLE_ATTR = "urn:oasis:names:tc:xacml:2.0:subject:role";
    public static final String PURPOSE_ROLE_ATTR = "urn:oasis:names:tc:xspa:1.0:subject:purposeofuse";
    public static final String PATIENT_ID_ATTR = "urn:oasis:names:tc:xacml:2.0:resource:resource-id";
    public static final String ACCESS_CONSENT_ATTR = "AccessConsentPolicy";
    public static final String INST_ACCESS_CONSENT_ATTR = "InstanceAccessConsentPolicy";
    public static final String CE_CODE_ID = "code";
    public static final String CE_CODESYS_ID = "codeSystem";
    public static final String CE_CODESYSNAME_ID = "codeSystemName";
    public static final String CE_DISPLAYNAME_ID = "displayName";


    // Audit Logging Constants
    public static final String AUDIT_REPO_SERVICE_NAME = "auditrepository";
    public static final String AUDIT_REPO_SECURE_SERVICE_NAME = "auditrepositorysecured";
    public static final String AUDIT_LOG_SERVICE_NAME = "auditlog";
    public static final String AUDIT_QUERY_SERVICE_NAME = "auditquery";
    public static final String AUDIT_LOG_ADAPTER_SERVICE_NAME = "adapterauditquery";
    public static final String ADAPTER_AUDIT_QUERY_SECURED_SERVICE_NAME = "adapterauditquerysecured";
    public static final String ENTITY_AUDIT_QUERY_SECURED_SERVICE_NAME = "entityauditquerysecured";
    public static final String NHINC_PROXY_AUDIT_QUERY_SERVICE_NAME = "nhincproxyauditquery";
    public static final String NHINC_PROXY_AUDIT_QUERY_SECURED_SERVICE_NAME = "nhincproxyauditquerysecured";

    public static final String AUDIT_LOG_ADAPTER_SERVICE_MANAGER_NAME = "auditrepositorymanager";
    public static final String AUDIT_LOG_INBOUND_DIRECTION = "Inbound";
    public static final String AUDIT_LOG_OUTBOUND_DIRECTION = "Outbound";
    public static final String AUDIT_LOG_ENTITY_INTERFACE = "Entity";
    public static final String AUDIT_LOG_NHIN_INTERFACE = "Nhin";
    public static final String AUDIT_LOG_ADAPTER_INTERFACE = "Adapter";
    public static final String AUDIT_LOG_SERVICE_PROPERTY = "serviceAuditRepository";
    public static final String AUDIT_LOG_SERVICE_PASSTHRU_PROPERTY = "auditRepositoryPassthrough";
    public static final String AUDIT_DISABLED_ACK_MSG = "Audit Service is not enabled";

    // Policy Engine Constants
    public static final String POLICYENGINE_DTE_SERVICE_NAME = "policyenginedte";
    public static final String POLICYENGINE_SERVICE_NAME = "policyengineservice";
    public static final String POLICYENGINE_SERVICE_SECURED_NAME = "policyengineservicesecured";
    public static final String POLICYENGINE_FACADE_SERVICE_NAME = "policyenginefacade";
    public static final String POLICYENGINE_INBOUND_DIRECTION = "Inbound";
    public static final String POLICYENGINE_OUTBOUND_DIRECTION = "Outbound";
    public static final String POLICY_PERMIT = "Permit";

    // HIEM - NHIN interface
    public static final String HIEM_SUBSCRIPTION_MANAGER_SERVICE_NAME = "subscriptionmanager";
    public static final String HIEM_SUBSCRIBE_SERVICE_NAME = "notificationproducer";
    public static final String HIEM_NOTIFY_SERVICE_NAME = "notificationconsumer";

    // HIEM - entity interface
    public static final String HIEM_SUBSCRIBE_ENTITY_SERVICE_NAME = "entitynotificationproducer";
    public static final String HIEM_UNSUBSCRIBE_ENTITY_SERVICE_NAME = "entitysubscriptionmanager";
    public static final String HIEM_NOTIFY_ENTITY_SERVICE_NAME = "entitynotificationconsumer";

    // HIEM - entity interface secured
    public static final String HIEM_SUBSCRIBE_ENTITY_SERVICE_NAME_SECURED = "entitynotificationproducersecured";
    public static final String HIEM_UNSUBSCRIBE_ENTITY_SERVICE_NAME_SECURED = "entitysubscriptionmanagersecured";
    public static final String HIEM_NOTIFY_ENTITY_SERVICE_NAME_SECURED = "entitynotificationconsumersecured";

    // HIEM - proxy interface secured
    public static final String HIEM_SUBSCRIBE_PROXY_SERVICE_NAME_SECURED = "nhincproxynotificationproducersecured";
    public static final String HIEM_UNSUBSCRIBE_PROXY_SERVICE_NAME_SECURED = "nhincproxysubscriptionmanagersecured";
    public static final String HIEM_NOTIFY_PROXY_SERVICE_NAME_SECURED = "nhincproxynotificationconsumersecured";

    // HIEM - adapter interface
    public static final String HIEM_SUBSCRIBE_ADAPTER_SERVICE_NAME = "adapternotificationproducer";
    public static final String HIEM_UNSUBSCRIBE_ADAPTER_SERVICE_NAME = "adaptersubscriptionmanager";
    public static final String HIEM_NOTIFY_ADAPTER_SERVICE_NAME = "adapternotificationconsumer";

    // HIEM - adapter interface secured
    public static final String HIEM_SUBSCRIBE_ADAPTER_SECURED_SERVICE_NAME = "adapternotificationproducersecured";
    public static final String HIEM_UNSUBSCRIBE_ADAPTER_SERVICE_SECURED_NAME = "adaptersubscriptionmanagersecured";
    public static final String HIEM_NOTIFY_ADAPTER_SERVICE_SECURED_NAME = "adapternotificationconsumersecured";

    public static final String HIEM_SUBSCRIPTION_SERVICE_PROPERTY = "serviceSubscription";
    public static final String HIEM_SUBSCRIPTION_SERVICE_PASSTHRU_PROPERTY = "subscriptionPassthrough";
    public static final String HIEM_NOTIFY_SERVICE_PROPERTY = "serviceNotify";
    public static final String HIEM_NOTIFY_SERVICE_PASSTHRU_PROPERTY = "notifyPassthrough";
    public static final String HIEM_ADAPTER_SUBSCRIPTION_MODE_PROPERTY = "hiem.AdapterSubscriptionMode";
    public static final String HIEM_ADAPTER_SUBSCRIPTION_MODE_CREATE_CHILD_SUBSCRIPTIONS = "createchildsubscription";
    public static final String HIEM_ADAPTER_SUBSCRIPTION_MODE_CREATE_CHILD_FORWARD = "forward";
    public static final String HIEM_ADAPTER_SUBSCRIPTION_MODE_CREATE_CHILD_DISABLED = "disabled";

    // MPI constants
    public static final String ADAPTER_MPI_SERVICE_NAME = "adaptercomponentmpiservice";
    public static final String ADAPTER_MPI_SECURED_SERVICE_NAME = "adaptercomponentmpisecuredservice";
    public static final String ADAPTER_MPI_PROXY_SERVICE_NAME = "mpi";
    public static final String ADAPTER_MPI_PROXY_SECURED_SERVICE_NAME = "mpisecured";


    // SOAP Headers
    public static final String HTTP_REQUEST_ATTRIBUTE_SOAPMESSAGE = "SoapMessage";
    public static final String HIEM_SUBSCRIBE_SOAP_HDR_ATTR_TAG = "subscribeSoapMessage";
    public static final String HIEM_UNSUBSCRIBE_SOAP_HDR_ATTR_TAG = "unsubscribeSoapMessage";
    public static final String HIEM_NOTIFY_SOAP_HDR_ATTR_TAG = "notifySoapMessage";
    public static final String WS_ADDRESSING_URL = "http://www.w3.org/2005/08/addressing";
    public static final String WS_SOAP_HEADER_ACTION = "Action";
    public static final String WS_RETRIEVE_DOCUMENT_ACTION = "urn:ihe:iti:2007:RetrieveDocumentSet";
    public static final String WS_PROVIDE_AND_REGISTER_DOCUMENT_ACTION = "urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b";
    public static final String WS_SOAP_HEADER_TO = "To";
    public static final String WS_SOAP_HEADER_MESSAGE_ID = "MessageID";
    public static final String WS_SOAP_HEADER_MESSAGE_ID_PREFIX = "urn:uuid:";

    //Document Query Constants
    public static final String ADAPTER_DOC_QUERY_SERVICE_NAME = "adapterdocquery";
    public static final String ADAPTER_DOC_QUERY_SECURED_SERVICE_NAME = "adapterdocquerysecured";
    public static final String ADAPTER_DOC_REGISTRY_SERVICE_NAME = "adapterxdsbdocregistry";
    public static final String DOC_QUERY_XDS_PATIENT_ID_SLOT_NAME = "$XDSDocumentEntryPatientId";
    public static final String NHINC_DOCUMENT_QUERY_SERVICE_NAME = "serviceDocumentQuery";
    public static final String DOC_QUERY_SELF_PROPERTY_NAME = "documentQueryQuerySelf";
    public static final String NHINC_DOCUMENT_QUERY_SERVICE_PASSTHRU_PROPERTY = "documentQueryPassthrough";
    public static final String DOC_QUERY_SERVICE_NAME = "QueryForDocument";
    public static final String ENTITY_DOC_QUERY_PROXY_SERVICE_NAME = "entitydocqueryproxy";
    public static final String ENTITY_DOC_QUERY_SECURED_SERVICE_NAME = "entitydocquerysecured";
    public static final String NHINC_ADHOC_QUERY_SUCCESS_RESPONSE = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success";
    public static final BigInteger NHINC_ADHOC_QUERY_NO_RESULT_COUNT = BigInteger.valueOf(0L);
    public static final String NHINC_PROXY_DOC_QUERY_SERVICE_NAME = "nhincproxydocquery";
    public static final String NHINC_PROXY_DOC_QUERY_SECURED_SERVICE_NAME = "nhincproxydocquerysecured";

    //Document Retrieve Constants
    public static final String ADAPTER_DOC_RETRIEVE_SERVICE_NAME = "adapterdocretrieve";
    public static final String ADAPTER_DOC_RETRIEVE_SECURED_SERVICE_NAME = "adapterdocretrievesecured";
    public static final String ADAPTER_DOC_REPOSITORY_SERVICE_NAME = "adapterxdsbdocrepository";
    public static final String ADAPTER_XDS_REP_SERVICE_NAME = "adapterxdsbdocrepositorysoap12";
    public static final String ENTITY_DOC_RETRIEVE_PROXY_SERVICE_NAME = "entitydocretrieveproxy";
    public static final String ENTITY_DOC_RETRIEVE_SECURED_SERVICE_NAME = "entitydocretrievesecured";
    public static final String NHINC_PROXY_DOC_RETRIEVE_SERVICE_NAME = "nhincproxydocretrieve";
    public static final String NHINC_PROXY_DOC_RETRIEVE_SECURED_SERVICE_NAME = "nhincproxydocretrievesecured";
    public static final String DOC_RETRIEVE_SERVICE_NAME = "RetrieveDocument";
    public static final String NHINC_DOCUMENT_RETRIEVE_SERVICE_KEY = "serviceDocumentRetrieve";
    public static final String NHINC_DOCUMENT_RETRIEVE_SERVICE_PASSTHRU_PROPERTY = "documentRetrievePassthrough";

    //Patient Correlation Constants
    public static final String PATIENT_CORRELATION_SERVICE_NAME = "patientcorrelation";
    public static final String PATIENT_CORRELATION_SECURED_SERVICE_NAME = "patientcorrelationsecured";

    // Subject Discovery Constants
    public static final String NHINC_SUBJECT_DISCOVERY_SERVICE_NAME = "serviceSubjectDiscovery";
    public static final String SUBJECT_DISCOVERY_SERVICE_NAME = "subjectdiscovery";
    public static final String SUBJECT_DISCOVERY_SERVICE_PASSTHRU_PROPERTY = "subjectDiscoveryPassthrough";
    public static final String SUBJECT_DISCOVERY_DISABLED_ACK_MSG = "Subject Discovery Service is not enabled";
    public static final String SUBJECT_DISCOVERY_POLICY_FAILED_ACK_MSG = "Policy Check failed for Subject Discovery";
    public static final String SUBJECT_DISCOVERY_REIDENT_SERVICE_NAME = "adapterreidentificationservice";
    public static final String SUBJECT_DISCOVERY_ADAPTER_SERVICE_NAME = "adaptersubjectdiscovery";
    public static final String ADAPTER_SUBJECT_DISCOVERY_SECURED_SERVICE_NAME = "adaptersubjectdiscoverysecured";
    public static final String ENTITY_SUBJECT_DISCOVERY_SECURED_SERVICE_NAME = "entitysubjectdiscoverysecured";
    public static final String NHINC_PROXY_SUBJECT_DISCOVERY_SERVICE_NAME = "nhincproxysubjectdiscovery";
    public static final String NHINC_PROXY_SUBJECT_DISCOVERY_SECURED_SERVICE_NAME = "nhincproxysubjectdiscoverysecured";

    // Patient Discovery Constants
    public static final String NHINC_PATIENT_DISCOVERY_SERVICE_NAME = "servicePatientDiscovery";
    public static final String PATIENT_DISCOVERY_SERVICE_NAME = "PatientDiscovery";
    public static final String PATIENT_DISCOVERY_SERVICE_PASSTHRU_PROPERTY = "patientDiscoveryPassthrough";
    public static final String PATIENT_DISCOVERY_DISABLED_ACK_MSG = "Patient Discovery Service is not enabled";
    public static final String PATIENT_DISCOVERY_POLICY_FAILED_ACK_MSG = "Policy Check failed for Patient Discovery";
    public static final String PATIENT_DISCOVERY_ADAPTER_SERVICE_NAME = "adapterpatientdiscovery";
    public static final String ADAPTER_PATIENT_DISCOVERY_SECURED_SERVICE_NAME = "adapterpatientdiscoverysecured";
    public static final String ENTITY_PATIENT_DISCOVERY_SECURED_SERVICE_NAME = "entitypatientdiscoverysecured";
    public static final String NHINC_PROXY_PATIENT_DISCOVERY_SERVICE_NAME = "nhincproxypatientdiscovery";
    public static final String NHINC_PROXY_PATIENT_DISCOVERY_SECURED_SERVICE_NAME = "nhincproxypatientdiscoverysecured";
    public static final String ENTITY_PATIENT_DISCOVERY_SERVICE_NAME = "entitypatientdiscovery";

    // Patient Discovery Error Constants
    public static final String PATIENT_DISCOVERY_ANSWER_NOT_AVAIL_ERR_CODE = "AnswerNotAvailable";

    // XDR Constants
    public static final String ENTITY_XDR_SECURED_SERVICE_NAME = "entityxdrsecured";
    public static final String NHINC_PROXY_XDR_SERVICE_NAME = "nhincproxyxdr";
    public static final String NHINC_PROXY_XDR_SECURED_SERVICE_NAME = "nhincproxyxdrsecured";
    public static final String ADAPTER_XDR_SERVICE_NAME = "adapterxdr";
    public static final String ADAPTER_XDR_SECURED_SERVICE_NAME = "adapterxdrsecured";
    public static final String NHINC_XDR_SERVICE_NAME = "serviceXDR";

    public static final String ENTITY_XDR_REQUEST_SERVICE_NAME = "entityxdrrequest";
    public static final String ENTITY_XDR_REQUEST_SECURED_SERVICE_NAME = "entityxdrrequestsecured";
    public static final String NHINC_PROXY_XDR_REQUEST_SERVICE_NAME = "nhincproxyxdrrequest";
    public static final String NHINC_PROXY_XDR_REQUEST_SECURED_SERVICE_NAME = "nhincproxyxdrrequestsecured";
    public static final String ADAPTER_XDR_REQUEST_SERVICE_NAME = "adapterxdrrequest";
    public static final String ADAPTER_XDR_REQUEST_SECURED_SERVICE_NAME = "adapterxdrrequestsecured";
    public static final String NHINC_XDR_REQUEST_SERVICE_NAME = "xdrrequest";

    public static final String ENTITY_XDR_RESPONSE_SERVICE_NAME = "entityxdrresponse";
    public static final String ENTITY_XDR_RESPONSE_SECURED_SERVICE_NAME = "entityxdrresponsesecured";
    public static final String NHINC_PROXY_XDR_RESPONSE_SERVICE_NAME = "nhincproxyxdrresponse";
    public static final String NHINC_PROXY_XDR_RESPONSE_SECURED_SERVICE_NAME = "nhincproxyxdrresponsesecured";
    public static final String ADAPTER_XDR_RESPONSE_SERVICE_NAME = "adapterxdrresponse";
    public static final String ADAPTER_XDR_RESPONSE_SECURED_SERVICE_NAME = "adapterxdrresponsesecured";
    public static final String NHINC_XDR_RESPONSE_SERVICE_NAME = "xdrresponse";

    // Hibernate Config Files
    public static final String HIBERNATE_AUDIT_REPOSITORY = "auditrepo.hibernate.cfg.xml";
    public static final String HIBERNATE_ASSIGNING_AUTHORITY = "assignauthority.hibernate.cfg.xml";
    public static final String HIBERNATE_PATIENT_CORRELATION = "CorrelatedIdentifers.hibernate.cfg.xml";
    public static final String HIBERNATE_DOCUMENT_REPOSITORY = "docrepo.hibernate.cfg.xml";

    private NhincConstants() {
    }
}
