/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
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

    public static final String ADAPTER_PROPERTY_FILE_NAME = "adapter";
    public static final String XDS_HOME_COMMUNITY_ID_PROPERTY = "XDSbHomeCommunityId";

    // CONNECT Operation
    public static final String CONNECT_DEMO_OPERATION_MODE_PROP = "connectDemoOperationMode";

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
    public static final String AUTHN_INSTANT_PROP = "authnInstant";
    public static final String AUTHN_SESSION_INDEX_PROP = "authnSessionIndex";
    public static final String AUTHN_CONTEXT_CLASS_PROP = "authnContextClass";
    public static final String SUBJECT_LOCALITY_ADDR_PROP = "subjectLocalityAddress";
    public static final String SUBJECT_LOCALITY_DNS_PROP = "subjectLocalityDNS";
    public static final String AUTHZ_DECISION_PROP = "authzDecision";
    public static final String AUTHZ_STATEMENT_EXISTS_PROP = "authzStatementExists";
    public static final String ASSERTION_ISSUER_PROP = "assertionIssuer";
    public static final String ASSERTION_ISSUER_FORMAT_PROP = "assertionIssuerFormat";
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
    public static final String ADMIN_DIST_ACTION = "administrativedistribution";
    public static final String ADAPTER_XDR_ACTION = "adapterXDRSecured";
    public static final String ADAPTER_XDRREQUEST_SECURED_ACTION = "adapterXDRRequestSecured";
    public static final String ADAPTER_XDRREQUEST_ACTION = "adapterXDRRequest";
    public static final String ADAPTER_XDRRESPONSE_SECURED_ACTION = "adapterXDRResponseSecured";
    public static final String ADAPTER_XDRRESPONSE_ACTION = "adapterXDRResponse";
    public static final String ENTITY_XDR_SECURED_RESPONSE_ACTION = "entityXDRSecuredResponse";
    public static final String AUDIT_REPO_ACTION = "auditrepository";
    public static final String POLICY_ENGINE_ACTION = "policyengine";
    public static final String POLICY_INFORMATION_POINT_ACTION = "policyinformationpoint";
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
    // Async Property Constants
    public static final String ASYNC_MESSAGE_ID_PROP = "messageId";
    public static final String ASYNC_RELATES_TO_PROP = "relatesToId";
    public static final String ASYNC_MSG_TYPE_PROP = "msgType";
    public static final String ASYNC_REQUEST_MSG_TYPE_VAL = "request";
    public static final String ASYNC_RESPONSE_MSG_TYPE_VAL = "response";
    public static final String ASYNC_DB_REC_EXP_VAL_PROP = "asyncDbRecExpValue";
    public static final String ASYNC_DB_REC_EXP_VAL_UNITS_PROP = "asyncDbRecExpUnits";
    public static final String ASYNC_DB_REC_EXP_VAL_UNITS_SEC = "seconds";
    public static final String ASYNC_DB_REC_EXP_VAL_UNITS_MIN = "minutes";
    public static final String ASYNC_DB_REC_EXP_VAL_UNITS_HOUR = "hours";
    public static final String ASYNC_DB_REC_EXP_VAL_UNITS_DAY = "days";
    public static final String NS_ADDRESSING_2005 = "http://www.w3.org/2005/08/addressing";
    public static final String HEADER_MESSAGEID = "MessageID";
    public static final String HEADER_RELATESTO = "RelatesTo";
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
    public static final String AUDIT_LOG_DEFERRED_TYPE = "Deferred";
    public static final String AUDIT_LOG_SYNC_TYPE = "Synchronous";
    public static final String AUDIT_LOG_REQUEST_PROCESS = "Request";
    public static final String AUDIT_LOG_RESPONSE_PROCESS = "Response";
    public static final String AUDIT_LOG_SERVICE_PROPERTY = "serviceAuditRepository";
    public static final String AUDIT_LOG_SERVICE_PASSTHRU_PROPERTY = "auditRepositoryPassthrough";
    public static final String AUDIT_DISABLED_ACK_MSG = "Audit Service is not enabled";
    // Policy Engine Constants
    public static final String POLICYENGINE_DTE_SERVICE_NAME = "policyenginedte";
    public static final String POLICYENGINE_SERVICE_NAME = "policyengineservice";
    public static final String POLICYENGINE_SERVICE_SECURED_NAME = "policyengineservicesecured";
    public static final String POLICYENGINE_FACADE_SERVICE_NAME = "policyenginefacade";
    public static final String ADAPTER_PIP_SERVICE_NAME = "adapterpip";
    public static final String ADAPTER_PEP_SERVICE_NAME = "adapterpep";
    public static final String ADAPTER_POLICY_ENGINE_ORCHESTRATOR_SERVICE_NAME = "adapterpolicyengineorchestrator";
    public static final String POLICYENGINE_INBOUND_DIRECTION = "Inbound";
    public static final String POLICYENGINE_OUTBOUND_DIRECTION = "Outbound";
    public static final String POLICY_PERMIT = "Permit";
    // Redaction Engine Constants
    public static final String REDACTION_ENGINE_SERVICE_NAME = "adapterredactionengine";
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
    // Adapter Component MPI constants
    public static final String ADAPTER_MPI_SERVICE_NAME = "mpi";
    public static final String ADAPTER_MPI_SECURED_SERVICE_NAME = "mpisecured";
    // Adapter Component MPI constants
    public static final String ADAPTER_COMPONENT_MPI_SERVICE_NAME = "adaptercomponentmpiservice";
    public static final String ADAPTER_COMPONENT_MPI_SECURED_SERVICE_NAME = "adaptercomponentmpisecuredservice";
    // SOAP Headers
    public static final String HTTP_REQUEST_ATTRIBUTE_SOAPMESSAGE = "SoapMessage";
    public static final String HIEM_SUBSCRIBE_SOAP_HDR_ATTR_TAG = "subscribeSoapMessage";
    public static final String HIEM_UNSUBSCRIBE_SOAP_HDR_ATTR_TAG = "unsubscribeSoapMessage";
    public static final String HIEM_NOTIFY_SOAP_HDR_ATTR_TAG = "notifySoapMessage";
    public static final String WS_ADDRESSING_URL = "http://www.w3.org/2005/08/addressing";
    public static final String WS_ADDRESSING_URL_ANONYMOUS = "http://www.w3.org/2005/08/addressing/anonymous";
    public static final String WS_SOAP_HEADER_ACTION = "Action";
    public static final String WS_RETRIEVE_DOCUMENT_ACTION = "urn:ihe:iti:2007:RetrieveDocumentSet";
    public static final String WS_PROVIDE_AND_REGISTER_DOCUMENT_ACTION = "urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b";
    public static final String WS_SOAP_HEADER_TO = "To";
    public static final String WS_SOAP_HEADER_REPLYTO = "ReplyTo";
    public static final String WS_SOAP_HEADER_ADDRESS = "Address";
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

    public static final String NHINC_DOCUMENT_QUERY_DEFERRED_REQ_SERVICE_NAME = "serviceDocumentQueryDeferredReq";
    public static final String NHINC_DOCUMENT_QUERY_DEFERRED_REQ_SERVICE_PASSTHRU_PROPERTY = "documentQueryDeferredReqPassthrough";
    public static final String NHINC_DOCUMENT_QUERY_DEFERRED_RESP_SERVICE_NAME = "serviceDocumentQueryDeferredResp";
    public static final String NHINC_DOCUMENT_QUERY_DEFERRED_RESP_SERVICE_PASSTHRU_PROPERTY = "documentQueryDeferredRespPassthrough";

    public static final String ADAPTER_DOCUMENT_QUERY_DEFERRED_REQ_SERVICE_NAME = "adapterdocquerydeferredrequest";
    public static final String ADAPTER_DOCUMENT_QUERY_DEFERRED_REQ_SECURED_SERVICE_NAME = "adapterdocquerydeferredrequestsecured";
    public static final String ADAPTER_DOCUMENT_QUERY_DEFERRED_RESP_SERVICE_NAME = "adapterdocquerydeferredresponse";
    public static final String ADAPTER_DOCUMENT_QUERY_DEFERRED_RESP_SECURED_SERVICE_NAME = "adapterdocquerydeferredresponsesecured";
    public static final String ADAPTER_COMP_DOCUMENT_QUERY_DEFERRED_REQ_SERVICE_NAME = "adaptercomponentdocquerydeferredrequest";
    public static final String ADAPTER_COMP_DOCUMENT_QUERY_DEFERRED_RESP_SERVICE_NAME = "adaptercomponentdocquerydeferredresponse";
    public static final String ADAPTER_DOCUMENT_QUERY_DEFERRED_REQ_ERROR_SERVICE_NAME = "adapterdocquerydeferredrequesterror";
    public static final String ADAPTER_DOCUMENT_QUERY_DEFERRED_REQ_ERROR_SECURED_SERVICE_NAME = "adapterdocquerydeferredrequesterrorsecured";
    public static final String ADAPTER_DOCUMENT_QUERY_DEFERRED_REQ_QUEUE_PROCESS_SERVICE_NAME = "adapterdocquerydeferredreqqueueprocess";

    public static final String NHIN_DOCUMENT_QUERY_DEFERRED_REQ_SERVICE_NAME = "QueryForDocumentsDeferredRequest";
    public static final String NHIN_DOCUMENT_QUERY_DEFERRED_RESP_SERVICE_NAME = "QueryForDocumentsDeferredResponse";
    public static final String PASSTHRU_DOCUMENT_QUERY_DEFERRED_REQ_SERVICE_NAME = "passthrudocquerydeferredrequest";
    public static final String PASSTHRU_DOCUMENT_QUERY_DEFERRED_REQ_SECURED_SERVICE_NAME = "passthrudocquerydeferredrequestsecured";
    public static final String PASSTHRU_DOCUMENT_QUERY_DEFERRED_RESP_SERVICE_NAME = "passthrudocquerydeferredresponse";
    public static final String PASSTHRU_DOCUMENT_QUERY_DEFERRED_RESP_SECURED_SERVICE_NAME = "passthrudocquerydeferredresponsesecured";

    public static final String ENTITY_DOCUMENT_QUERY_DEFERRED_REQ_SERVICE_NAME = "entitydocquerydeferredrequest";
    public static final String ENTITY_DOCUMENT_QUERY_DEFERRED_REQ_SECURED_SERVICE_NAME = "entitydocquerydeferredrequestsecured";
    public static final String ENTITY_DOCUMENT_QUERY_DEFERRED_RESP_SERVICE_NAME = "entitydocquerydeferredresponse";
    public static final String ENTITY_DOCUMENT_QUERY_DEFERRED_RESP_SECURED_SERVICE_NAME = "entitydocquerydeferredresponsesecured";
    
    public static final String DOC_QUERY_DEFERRED_REQ_ACK_STATUS_MSG = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:RequestAccepted";
    public static final String DOC_QUERY_DEFERRED_RESP_ACK_STATUS_MSG = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:ResponseAccepted";
    public static final String DOC_QUERY_DEFERRED_REQ_ACK_FAILURE_STATUS_MSG = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure";
    public static final String DOC_QUERY_DEFERRED_RESP_ACK_FAILURE_STATUS_MSG = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure";
    public static final String DOC_QUERY_DEFERRED_ACK_ERROR_AUTHORIZATION = "AuthorizationException";
    public static final String DOC_QUERY_DEFERRED_ACK_ERROR_INVALID = "InvalidRequestException";
    public static final String DOC_QUERY_DEFERRED_ACK_ERROR_SIGNATURE = "SignatureValidationException";
    public static final String DOC_QUERY_DEFERRED_ACK_ERROR_TIMEOUT = "TimeoutException";
    public static final String DOC_QUERY_DEFERRED_ACK_ERROR_UNSUPPORTED = "UnsupportedCapabilityException";

    //Document Retrieve Constants
    public static final String ADAPTER_DOC_RETRIEVE_SERVICE_NAME = "adapterdocretrieve";
    public static final String ADAPTER_DOC_RETRIEVE_SECURED_SERVICE_NAME = "adapterdocretrievesecured";
    public static final String ADAPTER_DOC_REPOSITORY_SERVICE_NAME = "adapterxdsbdocrepository";
    public static final String ADAPTER_XDS_REP_SERVICE_NAME = "adapterxdsbdocrepositorysoap12";
    public static final String ENTITY_DOC_RETRIEVE_PROXY_SERVICE_NAME = "entitydocretrieveproxy";
    public static final String ENTITY_DOC_RETRIEVE_SECURED_SERVICE_NAME = "entitydocretrievesecured";
    public static final String ENTITY_DOC_RETRIEVE_SERVICE_NAME = "entitydocretrieve";
    public static final String NHINC_PROXY_DOC_RETRIEVE_SERVICE_NAME = "nhincproxydocretrieve";
    public static final String NHINC_PROXY_DOC_RETRIEVE_SECURED_SERVICE_NAME = "nhincproxydocretrievesecured";
    public static final String DOC_RETRIEVE_SERVICE_NAME = "RetrieveDocument";
    public static final String NHINC_DOCUMENT_RETRIEVE_SERVICE_KEY = "serviceDocumentRetrieve";
    public static final String NHINC_DOCUMENT_RETRIEVE_SERVICE_PASSTHRU_PROPERTY = "documentRetrievePassthrough";
    public static final String ADAPTER_DOC_RETRIEVE_DEFERRED_REQ_QUEUE_PROCESS_SERVICE_NAME = "adapterdocretrievedeferredreqqueueprocess";

    public static final String DOC_RETRIEVE_DEFERRED_REQ_ACK_STATUS_MSG = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:RequestAccepted";
    public static final String DOC_RETRIEVE_DEFERRED_RESP_ACK_STATUS_MSG = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:ResponseAccepted";
    public static final String DOC_RETRIEVE_DEFERRED_REQ_ACK_FAILURE_STATUS_MSG = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure";
    public static final String DOC_RETRIEVE_DEFERRED_RESP_ACK_FAILURE_STATUS_MSG = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure";
    public static final String DOC_RETRIEVE_DEFERRED_ACK_ERROR_AUTHORIZATION = "AuthorizationException";
    public static final String DOC_RETRIEVE_DEFERRED_ACK_ERROR_INVALID = "InvalidRequestException";
    public static final String DOC_RETRIEVE_DEFERRED_ACK_ERROR_SIGNATURE = "SignatureValidationException";
    public static final String DOC_RETRIEVE_DEFERRED_ACK_ERROR_TIMEOUT = "TimeoutException";
    public static final String DOC_RETRIEVE_DEFERRED_ACK_ERROR_UNSUPPORTED = "UnsupportedCapabilityException";

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
    public static final String NHINC_PATIENT_DISCOVERY_ASYNC_REQ_SERVICE_NAME = "servicePatientDiscoveryAsyncReq";
    public static final String NHINC_PATIENT_DISCOVERY_ASYNC_RESP_SERVICE_NAME = "servicePatientDiscoveryAsyncResp";
    public static final String PATIENT_DISCOVERY_SERVICE_NAME = "PatientDiscovery";
    public static final String PATIENT_DISCOVERY_ASYNC_REQ_SERVICE_NAME = "PatientDiscoveryAsyncReq";
    public static final String PATIENT_DISCOVERY_ASYNC_RESP_SERVICE_NAME = "PatientDiscoveryAsyncResp";
    public static final String PATIENT_DISCOVERY_SERVICE_PASSTHRU_PROPERTY = "patientDiscoveryPassthrough";
    public static final String PATIENT_DISCOVERY_SERVICE_ASYNC_REQ_PASSTHRU_PROPERTY = "patientDiscoveryAsyncReqPassthrough";
    public static final String PATIENT_DISCOVERY_DISABLED_ACK_MSG = "Patient Discovery Service is not enabled";
    public static final String PATIENT_DISCOVERY_POLICY_FAILED_ACK_MSG = "Policy Check failed for Patient Discovery";
    public static final String PATIENT_DISCOVERY_ADAPTER_SERVICE_NAME = "adapterpatientdiscovery";
    public static final String ADAPTER_PATIENT_DISCOVERY_SECURED_SERVICE_NAME = "adapterpatientdiscoverysecured";
    public static final String PATIENT_DISCOVERY_ADAPTER_ASYNC_REQ_SERVICE_NAME = "adapterpatientdiscoveryasyncreq";
    public static final String PATIENT_DISCOVERY_ADAPTER_SECURED_ASYNC_REQ_SERVICE_NAME = "adapterpatientdiscoverysecuredasyncreq";
    public static final String PATIENT_DISCOVERY_ADAPTER_ASYNC_REQ_ERROR_SERVICE_NAME = "adapterpatientdiscoveryasyncreqerror";
    public static final String PATIENT_DISCOVERY_ADAPTER_SECURED_ASYNC_REQ_ERROR_SERVICE_NAME = "adapterpatientdiscoverysecuredasyncreqerror";
    public static final String PATIENT_DISCOVERY_ADAPTER_ASYNC_RESP_SERVICE_NAME = "adapterpatientdiscoveryasyncresp";
    public static final String PATIENT_DISCOVERY_ADAPTER_SECURED_ASYNC_RESP_SERVICE_NAME = "adapterpatientdiscoverysecuredasyncresp";
    public static final String ENTITY_PATIENT_DISCOVERY_SECURED_SERVICE_NAME = "entitypatientdiscoverysecured";
    public static final String NHINC_PASSTHRU_PATIENT_DISCOVERY_SERVICE_NAME = "nhincproxypatientdiscovery";
    public static final String NHINC_PASSTHRU_PATIENT_DISCOVERY_SECURED_SERVICE_NAME = "nhincproxypatientdiscoverysecured";
    public static final String PATIENT_DISCOVERY_PASSTHRU_ASYNC_REQ_SERVICE_NAME = "nhincproxypatientdiscoveryasyncreq";
    public static final String PATIENT_DISCOVERY_PASSTHRU_SECURED_ASYNC_REQ_SERVICE_NAME = "nhincproxypatientdiscoverysecuredasyncreq";
    public static final String PATIENT_DISCOVERY_PASSTHRU_ASYNC_RESP_SERVICE_NAME = "nhincproxypatientdiscoveryasyncresp";
    public static final String PATIENT_DISCOVERY_PASSTHRU_SECURED_ASYNC_RESP_SERVICE_NAME = "nhincproxypatientdiscoverysecuredasyncresp";
    public static final String ENTITY_PATIENT_DISCOVERY_SERVICE_NAME = "entitypatientdiscovery";
    public static final String PATIENT_DISCOVERY_ENTITY_ASYNC_REQ_SERVICE_NAME = "entitypatientdiscoveryasyncreq";
    public static final String PATIENT_DISCOVERY_ENTITY_SECURED_ASYNC_REQ_SERVICE_NAME = "entitypatientdiscoverysecuredasyncreq";
    public static final String PATIENT_DISCOVERY_ENTITY_ASYNC_RESP_SERVICE_NAME = "entitypatientdiscoveryasyncresp";
    public static final String PATIENT_DISCOVERY_ENTITY_SECURED_ASYNC_RESP_SERVICE_NAME = "entitypatientdiscoverysecuredasyncresp";

    public static final String PATIENT_DISCOVERY_ADAPTER_ASYNC_REQ_QUEUE_SERVICE_NAME = "adapterpatientdiscoveryasyncreqqueue";
    public static final String PATIENT_DISCOVERY_ADAPTER_SECURED_ASYNC_REQ_QUEUE_SERVICE_NAME = "adapterpatientdiscoverysecuredasyncreqqueue";
    //public static final String PATIENT_DISCOVERY_ENTITY_ASYNC_REQ_QUEUE_SERVICE_NAME = "entitypatientdiscoveryasyncreqqueue";
    //public static final String PATIENT_DISCOVERY_ENTITY_SECURED_ASYNC_REQ_QUEUE_SERVICE_NAME = "entitypatientdiscoverysecuredasyncreqqueue";
    public static final String PATIENT_DISCOVERY_ADAPTER_ASYNC_REQ_QUEUE_PROCESS_SERVICE_NAME = "adapterpatientdiscoverydeferredreqqueueprocess";

    // Patient Discovery Error Constants
    public static final String PATIENT_DISCOVERY_ANSWER_NOT_AVAIL_ERR_CODE = "AnswerNotAvailable";
    public static final String PATIENT_DISCOVERY_RESPONDER_BUSY_ERR_CODE = "ResponderBusy";
    public static final String PATIENT_DISCOVERY_GENDER_MORE_CODE = "LivingSubjectAdministrativeGenderRequested";
    public static final String PATIENT_DISCOVERY_ADDRESS_MORE_CODE = "PatientAdressRequested";
    public static final String PATIENT_DISCOVERY_TELCOM_MORE_CODE = "PatientTelecomRequested";
    public static final String PATIENT_DISCOVERY_BIRTH_PLACE_NAME_MORE_CODE = "LivingSubjectBirthPlaceNameRequested";
    public static final String PATIENT_DISCOVERY_BIRTH_PLACE_ADDRESS_MORE_CODE = "LivingSubjectBirthPlaceAddressRequested";
    public static final String PATIENT_DISCOVERY_MOTHERS_MAIDEN_NAME_MORE_CODE = "MothersMaidenNameRequested";
    public static final String PATIENT_DISCOVERY_SSN_MORE_CODE = "SSNRequested";

    // XDR Constants
    public static final String ENTITY_XDR_SECURED_SERVICE_NAME = "entityxdrsecured";
    public static final String ENTITY_XDR_SERVICE_NAME = "entityxdr";
    public static final String NHINC_PROXY_XDR_SERVICE_NAME = "nhincproxyxdr";
    public static final String NHINC_PROXY_XDR_SECURED_SERVICE_NAME = "nhincproxyxdrsecured";
    public static final String ADAPTER_XDR_SERVICE_NAME = "adapterxdr";
    public static final String ADAPTER_XDR_SECURED_SERVICE_NAME = "adapterxdrsecured";
    public static final String ADAPTER_COMPONENT_XDR_SERVICE_NAME = "adaptercomponentxdr";
    public static final String ADAPTER_COMPONENT_XDR_SECURED_SERVICE_NAME = "adaptercomponentxdrsecured";
    public static final String NHINC_XDR_SERVICE_NAME = "serviceXDR";
    public static final String ENTITY_XDR_REQUEST_SERVICE_NAME = "entityxdrrequest";
    public static final String ENTITY_XDR_REQUEST_SECURED_SERVICE_NAME = "entityxdrrequestsecured";
    public static final String NHINC_PROXY_XDR_REQUEST_SERVICE_NAME = "nhincproxyxdrrequest";
    public static final String NHINC_PROXY_XDR_REQUEST_SECURED_SERVICE_NAME = "nhincproxyxdrrequestsecured";
    public static final String ADAPTER_XDR_REQUEST_SERVICE_NAME = "adapterxdrrequest";
    public static final String ADAPTER_COMPONENT_XDR_REQUEST_SERVICE_NAME = "adaptercomponentxdrrequest";
    public static final String ADAPTER_XDR_REQUEST_SECURED_SERVICE_NAME = "adapterxdrrequestsecured";
    public static final String NHINC_XDR_REQUEST_SERVICE_NAME = "xdrrequest";
    public static final String ENTITY_XDR_RESPONSE_SERVICE_NAME = "entityxdrresponse";
    public static final String ENTITY_XDR_RESPONSE_SECURED_SERVICE_NAME = "entityxdrresponsesecured";
    public static final String NHINC_PROXY_XDR_RESPONSE_SERVICE_NAME = "nhincproxyxdrresponse";
    public static final String NHINC_PROXY_XDR_RESPONSE_SECURED_SERVICE_NAME = "nhincproxyxdrresponsesecured";
    public static final String ADAPTER_XDR_RESPONSE_SERVICE_NAME = "adapterxdrresponse";
    public static final String ADAPTER_XDR_RESPONSE_SECURED_SERVICE_NAME = "adapterxdrresponsesecured";
    public static final String NHINC_XDR_RESPONSE_SERVICE_NAME = "xdrresponse";
    public static final String ADAPTER_XDR_ASYNC_REQ_ERROR_SERVICE_NAME = "adapterxdrrequesterror";
    public static final String ADAPTER_XDR_SECURED_ASYNC_REQ_ERROR_SERVICE_NAME = "adapterxdrsecuredasyncreqerror";
    public static final String ADAPTER_COMPONENT_XDR_RESPONSE_SERVICE_NAME = "adaptercomponentxdrresponse";
    public static final String XDR_ACK_STATUS_MSG = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:RequestAccepted";
    public static final String XDR_RESP_ACK_STATUS_MSG = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:ResponseAccepted";
    public static final String XDR_ACK_FAILURE_STATUS_MSG = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure";

    public static final String DOC_SUBMISSION_SERVICE_PROP = "serviceDocumentSubmission";
    public static final String DOC_SUBMISSION_DEFERRED_REQ_SERVICE_PROP = "serviceDocumentSubmissionDeferredReq";
    public static final String DOC_SUBMISSION_DEFERRED_RESP_SERVICE_PROP = "serviceDocumentSubmissionDeferredResp";
    public static final String DOC_SUBMISSION_PASSTHRU_PROP = "documentSubmissionPassthrough";
    public static final String DOC_SUBMISSION_DEFERRED_REQ_PASSTHRU_PROP = "documentSubmissionDeferredReqPassthrough";
    public static final String DOC_SUBMISSION_DEFERRED_RESP_PASSTHRU_PROP = "documentSubmissionDeferredRespPassthrough";

    //Administrative Distribution Constants
    public static final String NHIN_ADMIN_DIST_SERVICE_NAME = "nhinadmindist";
    public static final String ENTITY_ADMIN_DIST_SERVICE_NAME = "entityadmindist";
    public static final String ENTITY_ADMIN_DIST_SECURED_SERVICE_NAME = "entityadmindistsecured";
    public static final String ADAPTER_ADMIN_DIST_SERVICE_NAME = "adapteradmindist";
    public static final String ADAPTER_ADMIN_DIST_SECURED_SERVICE_NAME = "adapteradmindistsecured";
    public static final String NHINC_ADMIN_DIST_SERVICE_NAME = "nhincadmindist";
    public static final String NHINC_ADMIN_DIST_SECURED_SERVICE_NAME = "nhincadmindistsecured";
    public static final String NHIN_ADMIN_DIST_SERVICE_ENABLED = "serviceAdministrativeDistribution";
    public static final String NHIN_ADMIN_DIST_SERVICE_PASSTHRU_PROPERTY = "administrativeDistributionPassthrough";
    public static final String NHIN_ADMIN_DIST_SERVICE_SLEEP_PROPERTY = "administrativeDistributionPassthrough";

    // Hibernate Config Files
    public static final String HIBERNATE_AUDIT_REPOSITORY = "auditrepo.hibernate.cfg.xml";
    public static final String HIBERNATE_ASSIGNING_AUTHORITY = "assignauthority.hibernate.cfg.xml";
    public static final String HIBERNATE_PATIENT_CORRELATION = "CorrelatedIdentifers.hibernate.cfg.xml";
    public static final String HIBERNATE_DOCUMENT_REPOSITORY = "docrepo.hibernate.cfg.xml";
    public static final String HIBERNATE_ASYNCMSGS_REPOSITORY = "AsyncMsgs.hibernate.cfg.xml";
    public static final String HIBERNATE_LIFTMESSAGE_REPOSITORY = "GatewayLiftMsg.hibernate.cfg.xml";
    public static final String HIBERNATE_PATIENTDB_REPOSITORY = "patientdb.hibernate.cfg.xml";
    public static final String HIBERNATE_PERFREPO_REPOSITORY = "perfrepo.hibernate.cfg.xml";
    public static final String HIBERNATE_HIEMSUBREP_REPOSITORY = "HiemSubRepHibernate.cfg.xml";
    public static final String HIBERNATE_AGGREGATOR_REPOSITORY = "aggregator.cfg.xml";

    //LiFT Constants
    public static final String LIFT_MANAGER_SERVICE_NAME = "nhincliftmanager";
    public static final String LIFT_ENABLED_PROPERTY_NAME = "liftEnabled";
    public static final String LIFT_TRANSPORT_SERVICE_SLOT_NAME = "transportService";
    public static final String LIFT_TRANSPORT_SERVICE_SLOT_VALUE = "LIFT";
    public static final String LIFT_TRANSPORT_SERVICE_PROTOCOL_SLOT_NAME = "transportServiceProtocol";
    public static final String LIFT_TRANSPORT_SERVICE_PROTOCOL_SLOT_VALUE = "HTTPS";
    public static final String LIFT_CLIENT_IP = "ClientManagerControllerIP";
    public static final String LIFT_CLIENT_PORT = "ClientManagerControllerPort";
    public static final String LIFT_KEYSTORE = "LiftKeyStore";
    public static final String LIFT_KEYSTOREPASS = "LiftKeyStorePass";
    public static final String LIFT_KEYALIAS = "LiftKeyAlias";
    public static final String LIFT_TRUSTSTORE = "LiftTrustStore";
    public static final String LIFT_TRUSTSTOREPASS = "LiftTrustStorePass";
    public static final String LIFT_FILEDROP = "DefaultFileDest";
    public static final String LIFT_PROXY_ADDRESS = "ProxyAddress";
    public static final String LIFT_PROXY_PORT = "ProxyPort";
    public static final String LIFT_FILESERVER_IP = "FileServerIP";
    public static final String LIFT_FILESERVER_PORT = "FileServerPort";
    public static final String LIFT_BASE_FILE_SERVER_DIR_PROP_NAME = "BaseOutboundDir";
    public static final String LIFT_CIPHER_SUITES = "CipherSuites";
    public static final String LIFT_SERVER_NSS_CONFIG = "LiftServerNSSConfig";
    public static final String LIFT_CLIENT_NSS_CONFIG = "LiftClientNSSConfig";
    public static final String LIFT_TRANSFER_DB_STATE_ENTERED = "ENTERED";
    public static final String LIFT_TRANSFER_DB_STATE_PROCESSING = "PROCESSING";
    public static final String LIFT_GATEWAY_MESSAGE_DB_STATE_ENTERED = "ENTERED";
    public static final String LIFT_GATEWAY_MESSAGE_DB_STATE_PROCESSING = "PROCESSING";
    public static final String LIFT_GATEWAY_MESSAGE_DB_TYPE_DOC_SUB = "DEFERRED_DOCUMENT_SUBMISSION";

    public static final String AGGREGATOR_LARGE_RESP_DIR_PROP = "aggregatorLargeResponseDir";
    public static final String AGGREGATOR_LARGE_RESP_SIZE_PROP = "aggregatorMaxDbResponseSize";

    /* -- Begin Document Retrieve deferred Service Name -- */
    public static final String DOCRETRIEVE_DEFERRED_ACTION = "docretrievedeferred";
    public static final String NHINC_DOCUMENT_RETRIEVE_DEFERRED_REQUEST_SERVICE_KEY = "serviceDocumentRetrieveDeferredReq";
    public static final String NHINC_DOCUMENT_RETRIEVE_DEFERRED_REQUEST_SERVICE_PASSTHRU_PROPERTY = "documentRetrieveDeferredReqPassthrough";
    public static final String NHINC_DOCUMENT_RETRIEVE_DEFERRED_RESPONSE_SERVICE_KEY = "serviceDocumentRetrieveDeferredResp";
    public static final String NHINC_DOCUMENT_RETRIEVE_DEFERRED_RESPONSE_SERVICE_PASSTHRU_PROPERTY = "documentRetrieveDeferredRespPassthrough";
    public static final String ENTITY_DOCRETRIEVE_DEFERRED_UNSECURED_REQUEST = "entitydocretrievedeferredrequest";
    public static final String ENTITY_DOCRETRIEVE_DEFERRED_SECURED_REQUEST = "entitydocretrievedeferredrequestsecured";
    public static final String NHINCPROXY_DOCRETRIEVE_DEFERRED_UNSECURED_REQUEST = "nhincproxydocretrievedeferredrequest";
    public static final String NHINCPROXY_DOCRETRIEVE_DEFERRED_SECURED_REQUEST = "nhincproxydocretrievedeferredrequestsecured";
    public static final String ENTITY_DOCRETRIEVE_DEFERRED_UNSECURED_RESPONSE = "entitydocretrievedeferredresponse";
    public static final String ENTITY_DOCRETRIEVE_DEFERRED_SECURED_RESPONSE = "entitydocretrievedeferredresponsesecured";
    public static final String NHINCPROXY_DOCRETRIEVE_DEFERRED_UNSECURED_RESPONSE = "nhincproxydocretrievedeferredresponse";
    public static final String NHINCPROXY_DOCRETRIEVE_DEFERRED_SECURED_RESPONSE = "nhincproxydocretrievedeferredresponsesecured";
    public static final String NHIN_DOCRETRIEVE_DEFERRED_REQUEST = "nhindocretrievedeferredrequest";
    public static final String NHIN_DOCRETRIEVE_DEFERRED_RESPONSE = "nhindocretrievedeferredresponse";
    public static final String DOCRETRIEVEDEFERRED_REQUEST_ACTION = "docretrievedeferredrequest";
    public static final String DOCRETRIEVEDEFERRED_RESPONSE_ACTION = "docretrievedeferredresponse";
    public static final String ADAPTER_COMPONENT_DOC_RETRIEVE_DEFERRED_REQUEST_SERVICE_NAME = "adaptercomponentdocretrievedeferredrequest";
    public static final String ADAPTER_DOC_RETRIEVE_DEFERRED_REQUEST_SERVICE_NAME = "adapterdocretrievedeferredrequest";
    public static final String ADAPTER_DOC_RETRIEVE_DEFERRED_REQUEST_SECURED_SERVICE_NAME = "adapterdocretrievedeferredrequestsecured";
    public static final String ADAPTER_DOC_RETRIEVE_DEFERRED_REQUEST_ERROR_SERVICE_NAME = "adapterdocretrievedeferredrequesterror";
    public static final String ADAPTER_DOC_RETRIEVE_DEFERRED_REQUEST_ERROR_SECURED_SERVICE_NAME = "adapterdocretrievedeferredrequesterrorsecured";
    public static final String ADAPTER_COMPONENT_DOC_RETRIEVE_DEFERRED_RESPONSE_SERVICE_NAME = "adaptercomponentdocretrievedeferredresponse";
    public static final String ADAPTER_DOC_RETRIEVE_DEFERRED_RESPONSE_SERVICE_NAME = "adapterdocretrievedeferredresponse";
    public static final String ADAPTER_DOC_RETRIEVE_DEFERRED_RESPONSE_SECURED_SERVICE_NAME = "adapterdocretrievedeferredresponsesecured";
    /* -- End Document Retrieve deferred Service Name -- */

    private NhincConstants() {
    }
}
