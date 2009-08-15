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
    public static final String USER_CODE_PROP = "userRoleCode";
    public static final String USER_SYST_PROP = "userRoleCodeSystem";
    public static final String USER_SYST_NAME_PROP = "userRoleCodeSystemName";
    public static final String USER_DISPLAY_PROP = "userRoleCodeDisplayName";
    public static final String EXPIRE_PROP = "expirationDate";
    public static final String SIGN_PROP = "signDate";
    public static final String CONTENT_REF_PROP = "contentReference";
    public static final String CONTENT_PROP = "content";
    public static final String AUDIT_QUERY_ACTION = "queryAuditLog";
    public static final String NOTIFY_ACTION = "notify";
    public static final String SUBSCRIBE_ACTION = "subscribe";
    public static final String UNSUBSCRIBE_ACTION = "unsubscribe";
    public static final String DOC_QUERY_ACTION = "queryDocuments";
    public static final String SUBJECT_DISCOVERY_ACTION = "subjectDiscovery";

    public static final String PAT_CORR_ACTION = "patientcorrelation";
    

    // Audit Logging Constants
    public static final String AUDIT_REPO_SERVICE_NAME = "auditrepository";
    public static final String AUDIT_LOG_SERVICE_NAME = "auditlog";
    public static final String AUDIT_QUERY_SERVICE_NAME = "auditquery";
    public static final String AUDIT_LOG_ADAPTER_SERVICE_NAME = "adapterauditquery";
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

    // HIEM - adapter interface
    public static final String HIEM_SUBSCRIBE_ADAPTER_SERVICE_NAME = "adapternotificationproducer";
    public static final String HIEM_UNSUBSCRIBE_ADAPTER_SERVICE_NAME = "adaptersubscriptionmanager";
    public static final String HIEM_NOTIFY_ADAPTER_SERVICE_NAME = "adapternotificationconsumer";
    
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

    // SOAP Headers
    public static final String HTTP_REQUEST_ATTRIBUTE_SOAPMESSAGE = "SoapMessage";
    public static final String HIEM_SUBSCRIBE_SOAP_HDR_ATTR_TAG = "subscribeSoapMessage";
    public static final String HIEM_UNSUBSCRIBE_SOAP_HDR_ATTR_TAG = "unsubscribeSoapMessage";
    public static final String HIEM_NOTIFY_SOAP_HDR_ATTR_TAG = "notifySoapMessage";

    //Document Query Constants
    public static final String ADAPTER_DOC_QUERY_SERVICE_NAME = "adapterdocquery";
    public static final String ADAPTER_DOC_QUERY_SECURED_SERVICE_NAME = "adapterdocquerysecured";
    public static final String ADAPTER_DOC_REGISTRY_SERVICE_NAME = "adapterxdsbdocregistry";
    public static final String DOC_QUERY_XDS_PATIENT_ID_SLOT_NAME = "$XDSDocumentEntryPatientId";
    public static final String NHINC_DOCUMENT_QUERY_SERVICE_NAME = "serviceDocumentQuery";
    public static final String DOC_QUERY_SELF_PROPERTY_NAME = "documentQueryQuerySelf";
    public static final String NHINC_DOCUMENT_QUERY_SERVICE_PASSTHRU_PROPERTY = "documentQueryPassthrough";
    public static final String DOC_QUERY_SERVICE_NAME = "documentquery";
    public static final String ENTITY_DOC_QUERY_PROXY_SERVICE_NAME = "entitydocqueryproxy";
    public static final String ENTITY_DOC_QUERY_SECURED_SERVICE_NAME = "entitydocquerysecured";
    public static final String NHINC_ADHOC_QUERY_SUCCESS_RESPONSE = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success";
    public static final BigInteger NHINC_ADHOC_QUERY_NO_RESULT_COUNT = BigInteger.valueOf(0L);
    public static final String NHINC_PROXY_DOC_QUERY_SERVICE_NAME = "nhincproxydocquery";
    public static final String NHINC_PROXY_DOC_QUERY_SECURED_SERVICE_NAME = "nhincproxydocquerysecured";

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
    public static final String SUBJECT_DISCOVERY_ADAPTER_SERVICE_NAME = "adaptersubdiscovery";

    private NhincConstants() {
    }
}
