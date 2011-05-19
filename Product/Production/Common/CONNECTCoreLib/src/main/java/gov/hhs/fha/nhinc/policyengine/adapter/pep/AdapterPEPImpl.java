/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.policyengine.adapter.pep;

import com.sun.identity.saml2.common.SAML2Exception;
import com.sun.identity.xacml.common.XACMLException;
import com.sun.identity.xacml.context.Action;
import com.sun.identity.xacml.context.Attribute;
import com.sun.identity.xacml.context.ContextFactory;
import com.sun.identity.xacml.context.Decision;
import com.sun.identity.xacml.context.Environment;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import com.sun.identity.xacml.context.Request;
import com.sun.identity.xacml.context.Resource;
import com.sun.identity.xacml.context.Response;
import com.sun.identity.xacml.context.Result;
import com.sun.identity.xacml.context.Subject;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdResponseType;
import gov.hhs.fha.nhinc.policyengine.adapter.pdp.proxy.AdapterPDPProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.pdp.proxy.AdapterPDPProxyObjectFactory;
import gov.hhs.fha.nhinc.policyengine.adapter.pip.proxy.AdapterPIPProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.pip.proxy.AdapterPIPProxyObjectFactory;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oasis.names.tc.xacml._2_0.context.schema.os.ActionType;
import oasis.names.tc.xacml._2_0.context.schema.os.AttributeType;
import oasis.names.tc.xacml._2_0.context.schema.os.AttributeValueType;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResourceType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResponseType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResultType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  This class implements the policy engine PEP (Policy Enforcement Point).
 */
public class AdapterPEPImpl {

    private static Log log = LogFactory.getLog(AdapterPEPImpl.class);
    private static final String PROPERTY_FILE_NAME_GATEWAY = "gateway";
    private static final String PROPERTY_FILE_KEY_HOME_COMMUNITY = "localHomeCommunityId";
    private static final String DEFAULT_PURPOSE_TEXT = "Purpose for Use code not provided";
    private static final String VALID_PURPOSE = "PUBLICHEALTH";
    private static final String VALID_USER_ROLE_CODE = "307969004";
    private static final String XSPA_SUBJECT_ID = "urn:oasis:names:tc:xacml:1.0:subject:subject-id";
    private static final String XSPA_SUBJECT_ORG = "urn:oasis:names:tc:xspa:1.0:subject:organization";
    private static final String XSPA_SUBJECT_ORG_ID = "urn:oasis:names:tc:xspa:1.0:subject:organization-id";
    private static final String XSPA_SUBJECT_ROLE = "urn:oasis:names:tc:xacml:2.0:subject:role";
    private static final String XSPA_SUBJECT_PURPOSE = "urn:oasis:names:tc:xspa:1.0:subject:purposeofuse";
    private static final String XSPA_RESOURCE_ID = "urn:oasis:names:tc:xacml:1.0:resource:resource-id";
    private static final String XSPA_ENVIRONMENT_LOCALITY = "urn:oasis:names:tc:xspa:1.0:environment:locality";
    private static final String XSPA_PATIENT_OPT_IN = "urn:gov:hhs:fha:nhinc:patient-opt-in";
    private static final String XSPA_ASSIGNING_AUTH = "urn:gov:hhs:fha:nhinc:assigning-authority-id";
    private static final String XSPA_SERVICE_TYPE = "urn:gov:hhs:fha:nhinc:service-type";
    private static final String XSPA_ACTION = "urn:oasis:names:tc:xacml:1.0:action:action-id";
    private static final String XACML_DATATYPE = "http://www.w3.org/2001/XMLSchema#string";
    private static final String XACML_SUBJECT_ID = "urn:oasis:names:tc:xacml:1.0:subject:subject-id";
    private static final String XACML_SUBJECT_ORG = "urn:gov:hhs:fha:nhinc:user-organization-name";
    private static final String XACML_SUBJECT_ORG_ID = "urn:oasis:names:tc:xspa:1.0:subject:organization-id";
    private static final String XACML_SUBJECT_ROLE = "urn:oasis:names:tc:xacml:2.0:subject:role";
    private static final String XACML_SUBJECT_PURPOSE = "urn:oasis:names:tc:xspa:1.0:subject:purposeofuse";
    private static final String XACML_HOME_COMMUNITY = "urn:gov:hhs:fha:nhinc:home-community-id";
    private static final String XACML_RESOURCE_ID = "urn:oasis:names:tc:xacml:1.0:resource:resource-id";
    private static final String XACML_ASSIGING_AUTH = "urn:gov:hhs:fha:nhinc:assigning-authority-id";
    private static final String XACML_DOCUMENT_ID = "urn:gov:hhs:fha:nhinc:document-id";
    private static final String XACML_DOC_COMMUNITY_ID = "urn:gov:hhs:fha:nhinc:home-community-id";
    private static final String XACML_DOC_REPOSITORY_ID = "urn:gov:hhs:fha:nhinc:document-repository-id";
    private static final String XACML_SUBSCRIPTION_ID = "urn:gov:hhs:fha:nhinc:subscription-topic";
    private static final String XACML_ACTION = "urn:oasis:names:tc:xacml:1.0:action:action-id";
    // These define the possible xacml definitions of our services
    // There must be a one-to-one correspondance with the xspa action definitions
    private static final String[] xacmlActionDef = {"PatientDiscoveryIn",
        "PatientDiscoveryOut",
        "DocumentQueryIn",
        "DocumentQueryOut",
        "DocumentRetrieveIn",
        "DocumentRetrieveOut",
        "AuditLogQueryIn",
        "AuditLogQueryOut",
        "HIEMSubscriptionRequestIn",
        "HIEMSubscriptionRequestOut",
        "HIEMSubscriptionCancelIn",
        "HIEMSubscriptionCancelOut",
        "HIEMNotifyIn",
        "HIEMNotifyOut",
        "SubjectDiscoveryReidentificationIn",
        "SubjectDiscoveryReidentificationOut",
        "XDRIn",
        "XDROut"};
    // These define the xspa actions corresponding to the xacml definitions
    // Correlation is order driven
    private static final String[] xspaActionDef = {"create",
        "create",
        "read",
        "read",
        "read",
        "read",
        "read",
        "read",
        "create",
        "create",
        "delete",
        "delete",
        "update",
        "update",
        "read",
        "read",
        "create",
        "create"};
    // Mapping of the NHIN actions to the cooresponding XSPA action
    private static Map<String, String> actionMap = new HashMap<String, String>();

    static {
        int numActions = xacmlActionDef.length;
        for (int actionIdx = 0; actionIdx < numActions; actionIdx++) {
            actionMap.put(xacmlActionDef[actionIdx], xspaActionDef[actionIdx]);
        }
    }

    /**
     * Given a request to check the access policy, this service will interface
     * with the PDP to determine if access is to be granted or denied.
     * @param checkPolicyRequest The xacml request to check defined policy
     * @return The xacml response which contains the access decision
     */
    public CheckPolicyResponseType checkPolicy(CheckPolicyRequestType checkPolicyRequest, AssertionType assertion) {
        CheckPolicyResponseType checkPolicyResp = new CheckPolicyResponseType();

        if (checkPolicyRequest != null) {
            Request pdpRequest = null;
            try {
                pdpRequest = createPdpRequest(checkPolicyRequest, assertion);

                AdapterPDPProxyObjectFactory pdpProxyFactory = new AdapterPDPProxyObjectFactory();
                AdapterPDPProxy pdpProxy = pdpProxyFactory.getAdapterPDPProxy();
                Response pdpResponse = pdpProxy.processPDPRequest(pdpRequest);

                log.debug("PDP Response: " + pdpResponse.toXMLString());

                boolean isPermitted = false;
                for (Object pdpRetObj : pdpResponse.getResults()) {
                    if (pdpRetObj instanceof Result) {
                        Result pdpResult = (Result) pdpRetObj;
                        Decision pdpDecision = pdpResult.getDecision();
                        if ("Permit".equalsIgnoreCase(pdpDecision.getValue())) {
                            isPermitted = true;
                        }
                    }
                }
                if (isPermitted) {
                    checkPolicyResp = createResponse(DecisionType.PERMIT);
                } else {
                    checkPolicyResp = createResponse(DecisionType.DENY);
                }

            } catch (PropertyAccessException pex) {
                checkPolicyResp = createResponse(DecisionType.DENY);
                log.error("PropertyAccessException thrown from XACMLRequestProcessor: " + pex.getMessage());
            } catch (XACMLException xex) {
                checkPolicyResp = createResponse(DecisionType.DENY);
                try {
                    if (pdpRequest != null) {
                        log.error("Unable to process PDP request: " + pdpRequest.toXMLString());
                    }
                } catch (XACMLException ex) {
                    //already in handling
                }
                log.error("XACMLException thrown from XACMLRequestProcessor: " + xex.getMessage());
            } catch (SAML2Exception samlex) {
                checkPolicyResp = createResponse(DecisionType.DENY);
                log.error("SAML2Exception thrown from XACMLRequestProcessor: " + samlex.getMessage());
            }
        } else {
            checkPolicyResp = createResponse(DecisionType.DENY);
        }

        return checkPolicyResp;
    }

    /**
     * Creates a check policy response message containing the decision declaration
     * @param value The declared decision, should be an enumerattion provided through DecisionType
     * @return The generated check policy response message
     */
    private CheckPolicyResponseType createResponse(DecisionType value) {
        CheckPolicyResponseType policyResponse = new CheckPolicyResponseType();
        ResponseType response = new ResponseType();
        ResultType result = new ResultType();
        result.setDecision(value);
        response.getResult().add(result);
        policyResponse.setResponse(response);
        return policyResponse;
    }

    /**
     * The PDP (Policy Decision Point) request document is a XACML XML document
     * based on the XSPA profile
     * @param checkPolicyRequest The xacml request to check defined policy
     * @return The PDP compatible request document
     */
    public Request createPdpRequest(CheckPolicyRequestType checkPolicyRequest, AssertionType assertion) {

        Request request = ContextFactory.getInstance().createRequest();

        try {
            /*
             * Create Subject
             * has a subject id, subject organization, subject organization id,
             * home community id, an optional user role, and a purpose of use
             */
            Subject subject = ContextFactory.getInstance().createSubject();
            List subjAttrList = new ArrayList();

            // Subject id must be present it is extracted from the Subject of the request
            List<Attribute> subjIdList = createSubjAttrs(checkPolicyRequest, XACML_SUBJECT_ID, XSPA_SUBJECT_ID, null);
            if (subjIdList.isEmpty()) {
                log.debug(XSPA_SUBJECT_ID + " Attribute is empty");
            }
            subjAttrList.addAll(subjIdList);

            // Subject organization must be present it is extracted from the Subject of the request
            List<Attribute> subjOrgList = createSubjAttrs(checkPolicyRequest, XACML_SUBJECT_ORG, XSPA_SUBJECT_ORG, null);
            if (subjOrgList.isEmpty()) {
                log.debug(XSPA_SUBJECT_ORG + " Attribute is empty");
            }
            subjAttrList.addAll(subjOrgList);

            // Subject organization id must be present it is extracted from the Subject of the request
            List<Attribute> subjOrgIdList = createSubjAttrs(checkPolicyRequest, XACML_SUBJECT_ORG_ID, XSPA_SUBJECT_ORG_ID, null);
            if (subjOrgIdList.isEmpty()) {
                log.debug(XSPA_SUBJECT_ID + " Attribute is empty");
            }
            subjAttrList.addAll(subjOrgIdList);

//            // Home community id must be present
//            // In inbound messages it is extracted from the Subject of the request
//            // In outbound messages it is looked up in our gateway properties
//            List<Attribute> subjHomeCommunityList = createSubjAttrs(checkPolicyRequest, XACML_HOME_COMMUNITY, XSPA_ENVIRONMENT_LOCALITY, null);
//            if (subjHomeCommunityList.isEmpty()) {
//                log.debug("Sender community is assumed to be this gateway");
//                subjHomeCommunityList.addAll(createSubjLocAttrs());
//            }
//            if (subjHomeCommunityList.isEmpty()) {
//                log.debug(XSPA_ENVIRONMENT_LOCALITY + " Attribute is empty");
//            }
//            subjAttrList.addAll(subjHomeCommunityList);

            // User role is optional
            List<String> extractedUserRoles = new ArrayList<String>();
            List<Attribute> subjUserRoleList = createSubjAttrs(checkPolicyRequest, XACML_SUBJECT_ROLE, XSPA_SUBJECT_ROLE, extractedUserRoles);
            if (subjUserRoleList.isEmpty()) {
                log.debug(XSPA_SUBJECT_ROLE + " Attribute is empty");
            }
            subjAttrList.addAll(subjUserRoleList);

            //Purpose of use must be present
            List<String> extractedPurpose = new ArrayList<String>();
            List<Attribute> subjPurposeList = createSubjAttrs(checkPolicyRequest, XACML_SUBJECT_PURPOSE, XSPA_SUBJECT_PURPOSE, extractedPurpose);
            if (subjPurposeList.isEmpty()) {
                log.debug("Create a default " + XSPA_SUBJECT_PURPOSE + " Attribute");
                subjPurposeList.addAll(createDefaultAttrs(XSPA_SUBJECT_PURPOSE, DEFAULT_PURPOSE_TEXT));
            }
            subjAttrList.addAll(subjPurposeList);

            // Add the Subject into the PDP request
            subject.setAttributes(subjAttrList);

            List subjectList = new ArrayList();
            subjectList.add(subject);
            request.setSubjects(subjectList);

            /*
             * Create Resource
             * has a patient id, patient home community, service type, and patient opt-in/opt-out
             */
            Resource resource = ContextFactory.getInstance().createResource();
            List resourceAttrList = new ArrayList();
            List<Attribute> resourcePatientOptInList = new ArrayList<Attribute>();

            // Service type must be present - identifies the NHIN service inbound or outbound
            // It is extracted from the Action attribute of the request
            List<String> extractedServices = new ArrayList<String>();
            List<Attribute> resourceServiceList = createActionAttrs(checkPolicyRequest, XACML_ACTION, XSPA_SERVICE_TYPE, extractedServices);
            if (resourceServiceList.isEmpty()) {
                log.debug(XSPA_SERVICE_TYPE + " Attribute is empty");
            }
            resourceAttrList.addAll(resourceServiceList);

            // Resource id is only present if the request is patient specific
            List<String> extractedResourceIds = new ArrayList<String>();
            List<Attribute> resourceIdList = createResourceAttrs(checkPolicyRequest, XACML_RESOURCE_ID, XSPA_RESOURCE_ID, extractedResourceIds);
            if (resourceIdList.isEmpty()) {
                log.debug(XSPA_RESOURCE_ID + " Attribute is empty");
            }
            resourceAttrList.addAll(resourceIdList);
            if (!extractedResourceIds.isEmpty()) {
                log.debug("createPdpRequest - extractedResourceIds not empty");
                // If request is patient specific then get the home community
                List<String> extractedCommunityIds = new ArrayList<String>();
                List<Attribute> resourceHomeCommunityList = createResourceAttrs(checkPolicyRequest, XACML_ASSIGING_AUTH, XSPA_ASSIGNING_AUTH, extractedCommunityIds);
                if (resourceHomeCommunityList.isEmpty()) {
                    log.debug(XSPA_ASSIGNING_AUTH + " Attribute is empty");
                }
                resourceAttrList.addAll(resourceHomeCommunityList);

                if (!extractedResourceIds.isEmpty() && !extractedCommunityIds.isEmpty()) {
                    // The policy for the Subject Discovery Reidentification is based on the
                    // user role code = "307969004" and the purpose for use = "PUBLICHEALTH"
                    // The patient identifiers passed in are the Pseudonyms not the real ones.
                    if (extractedServices.contains("SubjectDiscoveryReidentificationIn") ||
                            extractedServices.contains("SubjectDiscoveryReidentificationOut")) {
                        if (!extractedUserRoles.isEmpty() && !extractedPurpose.isEmpty()) {
                            resourcePatientOptInList.addAll(createReidentOptStatusAttrs(extractedUserRoles, extractedPurpose));
                        }
                    } else {
                        // The existance of a patient identifier in the request indicates that
                        // the policy engine needs to check the patient opt-in status (Yes or No)
                        resourcePatientOptInList.addAll(createPatientOptStatusAttrs(extractedResourceIds, extractedCommunityIds, assertion));
                    }
                    // Patient Opt-In or Opt-Out is optional - assume opt-out (No) if missing
                    if (resourcePatientOptInList.isEmpty()) {
                        log.debug("Create a default " + XSPA_PATIENT_OPT_IN + " Attribute with value Opt-Out");
                        resourcePatientOptInList.addAll(createDefaultAttrs(XSPA_PATIENT_OPT_IN, "No"));
                    }
                } else {
                    log.debug("Create a default " + XSPA_PATIENT_OPT_IN + " Attribute with value Opt-Out");
                    resourcePatientOptInList.addAll(createDefaultAttrs(XSPA_PATIENT_OPT_IN, "No"));
                }
            } else {
                // If message is not patient-centric it may be document-centric or subscription based
                // If document-centric then the patient opt-in status is checked by document-id
                log.debug("createPdpRequest - extractedResourceIds is empty");
                List<String> extractedDocIds = new ArrayList<String>();
                List<String> extractedCommunityIds = new ArrayList<String>();
                List<String> extractedRepositoryIds = new ArrayList<String>();
                //Note that these are not used in the construction of the request

                createResourceAttrs(checkPolicyRequest, XACML_DOCUMENT_ID, XSPA_RESOURCE_ID, extractedDocIds);
                createResourceAttrs(checkPolicyRequest, XACML_DOC_COMMUNITY_ID, XSPA_RESOURCE_ID, extractedCommunityIds);
                createResourceAttrs(checkPolicyRequest, XACML_DOC_REPOSITORY_ID, XSPA_RESOURCE_ID, extractedRepositoryIds);

                if (!extractedDocIds.isEmpty()) {
                    log.debug("createPdpRequest - extractedDocIds.size: " + extractedDocIds.size());
                    for (String docId : extractedDocIds) {
                        log.debug("createPdpRequest - docId: " + docId);
                    }

                    try {
                        Attribute docResAttr = ContextFactory.getInstance().createAttribute();
                        docResAttr.setAttributeId(new URI(XACML_RESOURCE_ID));
                        docResAttr.setDataType(new URI(XACML_DATATYPE));
                        docResAttr.setAttributeStringValues(extractedDocIds);
                        resourcePatientOptInList.add(docResAttr);
                    } catch (URISyntaxException uriex) {
                        log.error("Error in setting  attriute value for documentid resource-id: " + uriex.getMessage());
                    } catch (XACMLException xacmlex) {
                        log.error("Error in setting  attriute value for documentid resource-id: " + xacmlex.getMessage());
                    }

                    // Patient Opt-In or Opt-Out is optional - assume opt-out (No) if missing
                    // The existance of a document identifier in the request indicates that
                    // the policy engine needs to check the patient opt-in status (Yes or No)
                    resourcePatientOptInList.addAll(createDocumentOptStatusAttrs(extractedDocIds, extractedCommunityIds, extractedRepositoryIds, assertion));
                    if (resourcePatientOptInList.isEmpty()) {
                        log.debug("Create a default " + XSPA_PATIENT_OPT_IN + " Attribute with value Opt-Out");
                        resourcePatientOptInList.addAll(createDefaultAttrs(XSPA_PATIENT_OPT_IN, "No"));
                    }
                } else {
                    log.debug("createPdpRequest - extractedDocIds is empty");
                    // If it is not patient specific nor document specific then look for subscription
                    List<String> extractedSubscription = new ArrayList<String>();
                    createResourceAttrs(checkPolicyRequest, XACML_SUBSCRIPTION_ID, XSPA_RESOURCE_ID, extractedSubscription);
                    if (!extractedSubscription.isEmpty()) {
                        // Subscription requests are automatically opted-In
                        log.debug("Subscription request assigns " + XSPA_PATIENT_OPT_IN + " Attribute with value Opt-In");
                        resourcePatientOptInList.addAll(createDefaultAttrs(XSPA_PATIENT_OPT_IN, "Yes"));
                    } else {
                        //This message type is not identifiable as patient specific, document specific, or subscription based
                        // The only other valid type that is known is AuditLogQuery
                        if (extractedServices.contains("AuditLogQueryIn") ||
                                extractedServices.contains("AuditLogQueryOut")) {
                            log.debug("Audit Log Query request assigns " + XSPA_PATIENT_OPT_IN + " Attribute with value Opt-In");
                            resourcePatientOptInList.addAll(createDefaultAttrs(XSPA_PATIENT_OPT_IN, "Yes"));
                        } else if (extractedServices.contains("HIEMNotifyIn") ||
                                extractedServices.contains("HIEMNotifyOut")) {
                            log.debug("HIEM Notify request assigns " + XSPA_PATIENT_OPT_IN + " Attribute with value Opt-In");
                            resourcePatientOptInList.addAll(createDefaultAttrs(XSPA_PATIENT_OPT_IN, "Yes"));
                        } else {
                            log.debug("Unknown message type assign " + XSPA_PATIENT_OPT_IN + " Attribute with value Opt-Out");
                            resourcePatientOptInList.addAll(createDefaultAttrs(XSPA_PATIENT_OPT_IN, "No"));
                        }
                    }
                }
            }

            if (resourcePatientOptInList.isEmpty()) {
                log.debug("Create a default " + XSPA_PATIENT_OPT_IN + " Attribute with value Opt-Out");
                resourcePatientOptInList.addAll(createDefaultAttrs(XSPA_PATIENT_OPT_IN, "No"));
            }
            resourceAttrList.addAll(resourcePatientOptInList);

            // Add the Resource into the PDP request
            resource.setAttributes(resourceAttrList);

            List resourceList = new ArrayList();
            resourceList.add(resource);
            request.setResources(resourceList);

            /*
             * Create Action
             * recognized values are: create, read, update, delete, execute, suspend.
             */
            Action action = ContextFactory.getInstance().createAction();
            List actionAttrList = new ArrayList();

            // Action must be present, depends on the action in the request.
            List<Attribute> actionIdList = createActionAttrs(checkPolicyRequest, XACML_ACTION, XSPA_ACTION, null);
            if (actionIdList.isEmpty()) {
                log.debug(XSPA_ACTION + " Attribute is empty");
                actionIdList.addAll(createDefaultAttrs(XSPA_ACTION, "Unknown"));
            }
            actionAttrList.addAll(actionIdList);

            // Add the Action into the PDP request
            action.setAttributes(actionAttrList);
            request.setAction(action);


            //Environnment, required but not used
            Environment environment = ContextFactory.getInstance().createEnvironment();
            List envAttrList = new ArrayList();

            // Home community id must be present
            // In inbound messages it is extracted from the Subject of the request
            // In outbound messages it is looked up in our gateway properties
            List<Attribute> envHomeCommunityList = createSubjAttrs(checkPolicyRequest, XACML_HOME_COMMUNITY, XSPA_ENVIRONMENT_LOCALITY, null);
            if (envHomeCommunityList.isEmpty()) {
                log.debug("Sender community is assumed to be this gateway");
                envHomeCommunityList.addAll(createEnvLocAttrs());
            } else {
                for (Attribute attr : envHomeCommunityList) {
                    log.debug("Environment attr: " + attr.toXMLString());
                }
            }
            if (envHomeCommunityList.isEmpty()) {
                log.debug(XSPA_ENVIRONMENT_LOCALITY + " Attribute is empty");
            }
            envAttrList.addAll(envHomeCommunityList);

            // Add the Environment into the PDP request
            environment.setAttributes(envAttrList);
            request.setEnvironment(environment);

            log.debug("AdapterPEPImpl.createPdpRequest with PDP request: \n" + request.toXMLString());
        } catch (XACMLException ex) {
            log.error("Error in AdapterPEPImpl.createPdpRequest " + ex.getMessage());
        }
        return request;
    }

    /**
     * Creates the new XSPA attributes which originate from the incoming XACML request Subject attributes
     *
     * @param checkPolicyRequest The incoming XACML request
     * @param xacmlId The Attribute Id for the XACML request Subject attribute
     * @param xspaId The Attribute Id to give to the generated XSPA attribute
     * @param extractedVals The list of extracted values, may be null if not required
     * @return The listing of the generated XSPA attributes
     */
    private List<Attribute> createSubjAttrs(CheckPolicyRequestType checkPolicyRequest, String xacmlId, String xspaId, List<String> extractedVals) {

        List<Attribute> retSubjList = new ArrayList<Attribute>();

        if (checkPolicyRequest != null && checkPolicyRequest.getRequest() != null && checkPolicyRequest.getRequest().getSubject() != null && !checkPolicyRequest.getRequest().getSubject().isEmpty()) {
            for (SubjectType subjItem : checkPolicyRequest.getRequest().getSubject()) {
                if (subjItem != null && subjItem.getAttribute() != null &&
                        !subjItem.getAttribute().isEmpty()) {

                    List<Attribute> xspaAttrs = extractAttrs(subjItem.getAttribute(), xacmlId, xspaId, extractedVals);
                    if (xspaAttrs.isEmpty()) {
                        log.debug("Subject Attributes for check policy request do not include values for " + xacmlId);
                    } else {
                        retSubjList.addAll(xspaAttrs);
                    }

                } else {
                    log.debug("No Subject Attribute elements found in the check policy request");
                }

            }
        } else {
            log.debug("No Subject element found in the check policy request");
        }

        return retSubjList;
    }

    /**
     * Creates the new XSPA attributes which originate from the incoming XACML request Resource attributes
     *
     * @param checkPolicyRequest The incoming XACML request
     * @param xacmlId The Attribute Id for the XACML request Resource attribute
     * @param xspaId The Attribute Id to give to the generated XSPA attribute
     * @param extractedVals The list of extracted values, may be null if not required
     * @return The listing of the generated XSPA attributes
     */
    private List<Attribute> createResourceAttrs(CheckPolicyRequestType checkPolicyRequest, String xacmlId, String xspaId, List<String> extractedVals) {
        log.debug("Begin createResourceAttrs()..");
        List<Attribute> retResourceList = new ArrayList<Attribute>();

        if (checkPolicyRequest != null && checkPolicyRequest.getRequest() != null && checkPolicyRequest.getRequest().getResource() != null && !checkPolicyRequest.getRequest().getResource().isEmpty()) {
            for (ResourceType resourceItem : checkPolicyRequest.getRequest().getResource()) {
                if (resourceItem != null && resourceItem.getAttribute() != null &&
                        !resourceItem.getAttribute().isEmpty()) {
                    List<Attribute> xspaAttrs = extractAttrs(resourceItem.getAttribute(), xacmlId, xspaId, extractedVals);
                    if (xspaAttrs.isEmpty()) {
                        log.debug("Resource Attributes for check policy request do not include values for " + xacmlId);
                    } else {
                        retResourceList.addAll(xspaAttrs);
                    }

                } else {
                    log.debug("No Resource Attribute elements found in the check policy request");
                }
            }
        } else {
            log.debug("No Resource element found in the check policy request");
        }
        log.debug("End createResourceAttrs()..");
        return retResourceList;
    }

    /**
     * Creates the new XSPA attributes which originate from the incoming XACML request Action attributes
     *
     * @param checkPolicyRequest The incoming XACML request
     * @param xacmlId The Attribute Id for the XACML request Action attribute
     * @param xspaId The Attribute Id to give to the generated XSPA attribute
     * @param extractedVals The list of extracted values, may be null if not required
     * @return The listing of the generated XSPA attributes
     */
    private List<Attribute> createActionAttrs(CheckPolicyRequestType checkPolicyRequest, String xacmlId, String xspaId, List<String> extractedVals) {

        List<Attribute> retActionList = new ArrayList<Attribute>();

        if (checkPolicyRequest != null && checkPolicyRequest.getRequest() != null && checkPolicyRequest.getRequest().getAction() != null) {
            ActionType actionItem = checkPolicyRequest.getRequest().getAction();
            if (actionItem != null && actionItem.getAttribute() != null &&
                    !actionItem.getAttribute().isEmpty()) {

                List<Attribute> xspaAttrs = extractAttrs(actionItem.getAttribute(), xacmlId, xspaId, extractedVals);
                if (xspaAttrs.isEmpty()) {
                    log.debug("Action Attributes for check policy request do not include values for " + xacmlId);
                } else {
                    retActionList.addAll(xspaAttrs);
                }

            } else {
                log.debug("No Action Attribute elements found in the check policy request");
            }

        } else {
            log.debug("No Action element found in the check policy request");
        }

        return retActionList;
    }

    /**
     * Creates the new XSPA attributes by extracting the information from the XACML atributes
     *
     * @param xacmlAttrs The Attributes from the incoming XACML request
     * @param xacmlId The Attribute Id for the XACML request attribute
     * @param xspaId The Attribute Id to give to the generated XSPA attribute
     * @param extractedVals The list of extracted values, may be null if not required
     * @return The listing of the generated XSPA attributes
     */
    private List<Attribute> extractAttrs(List<AttributeType> xacmlAttrs, String xacmlId, String xspaId, List<String> extractedVals) {
        log.debug("Begin extractAttrs()..");
        List<Attribute> xspaAttrs = new ArrayList<Attribute>();

        for (AttributeType xacmlAttr : xacmlAttrs) {
            if (xacmlAttr.getAttributeId().equals(xacmlId)) {
                try {
                    Attribute xspaAttr = ContextFactory.getInstance().createAttribute();
                    xspaAttr.setAttributeId(new URI(xspaId));
                    xspaAttr.setDataType(new URI(XACML_DATATYPE));
                    List<String> extractedContent = new ArrayList();
                    for (AttributeValueType attrVal : xacmlAttr.getAttributeValue()) {
                        for (Object values : attrVal.getContent()) {
                            extractedContent.add(values.toString().trim());
                        }
                    }
                    if (extractedContent.isEmpty()) {
                        log.debug("The attribute in the check policy request has no values for " + xacmlId);
                    } else {
                        if (extractedVals != null) {
                            extractedVals.addAll(extractedContent);
                        }

                        if (XSPA_ACTION.equals(xspaId)) {
                            List<String> xspaActionVals = determineXSPAAction(extractedContent);
                            for (String xspaActionVal : xspaActionVals) {
                                log.debug("Adding attribute value: " + xspaActionVal + " for " + xspaId);
                            }

                            xspaAttr.setAttributeStringValues(xspaActionVals);
                        } else {
                            for (String extractedVal : extractedContent) {
                                log.debug("Adding attribute value: " + extractedVal + " for " + xspaId);
                            }

                            xspaAttr.setAttributeStringValues(extractedContent);
                        }

                    }
                    xspaAttrs.add(xspaAttr);
                } catch (URISyntaxException uriex) {
                    log.error("Error in extracting " + xacmlId + " values: " + uriex.getMessage());
                } catch (XACMLException xacmlex) {
                    log.error("Error in extracting " + xacmlId + " values: " + xacmlex.getMessage());
                }

            }
        }
        log.debug("End extractAttrs()..");
        return xspaAttrs;
    }

    /**
     * Creates the XSPA Attributes for the environment locality based upon a lookup
     * of the gateway property for home community
     * @return The XSPA Attributes containing the determined home community
     */
    private List<Attribute> createEnvLocAttrs() {
        log.debug("Begin createEnvLocAttrs()..");
        List<Attribute> xspaAttrs = new ArrayList<Attribute>();

        try {
            Attribute xspaAttr = ContextFactory.getInstance().createAttribute();
            xspaAttr.setAttributeId(new URI(XSPA_ENVIRONMENT_LOCALITY));
            xspaAttr.setDataType(new URI(XACML_DATATYPE));

            List<String> homeCommunityVals = new ArrayList<String>();
            String homeCommunityId = PropertyAccessor.getProperty(PROPERTY_FILE_NAME_GATEWAY, PROPERTY_FILE_KEY_HOME_COMMUNITY);
            log.debug("Adding attribute value: " + homeCommunityId + " for " + XSPA_ENVIRONMENT_LOCALITY);
            homeCommunityVals.add(homeCommunityId);

            xspaAttr.setAttributeStringValues(homeCommunityVals);
            xspaAttrs.add(xspaAttr);
        } catch (PropertyAccessException pex) {
            log.error("Error in extracting Home Community values: " + pex.getMessage());
        } catch (URISyntaxException uriex) {
            log.error("Error in extracting Home Community values: " + uriex.getMessage());
        } catch (XACMLException xacmlex) {
            log.error("Error in extracting Home Community  values: " + xacmlex.getMessage());
        }

        log.debug("End createEnvLocAttrs()..");
        return xspaAttrs;
    }

    /**
     * Creates the XSPA Attributes for the patient status
     * @param userRoleList The listing of user roles
     * @param purposeList The listing of purpose for use codes
     * @return The XSPA Attributes containing the determined consent status (Yes - optIn, No -optOut)
     */
    private List<Attribute> createReidentOptStatusAttrs(List<String> userRoleList, List<String> purposeList) {
        log.debug("Begin createReidentOptStatusAttrs()..");
        List<Attribute> xspaAttrs = new ArrayList<Attribute>();

        try {
            Attribute xspaAttr = ContextFactory.getInstance().createAttribute();
            xspaAttr.setAttributeId(new URI(XSPA_PATIENT_OPT_IN));
            xspaAttr.setDataType(new URI(XACML_DATATYPE));

            List<String> optStatusVals = determineReidentOptStatus(userRoleList, purposeList);
            for (String optStatusVal : optStatusVals) {
                log.debug("Adding attribute value: " + optStatusVal + " for " + XSPA_PATIENT_OPT_IN);
            }

            xspaAttr.setAttributeStringValues(optStatusVals);
            xspaAttrs.add(xspaAttr);
        } catch (URISyntaxException uriex) {
            log.error("Error in extracting PatientOptStatus values: " + uriex.getMessage());
        } catch (XACMLException xacmlex) {
            log.error("Error in extracting PatientOptStatus values: " + xacmlex.getMessage());
        }
        log.debug("End createReidentOptStatusAttrs()..");
        return xspaAttrs;
    }

    /**
     * Determines the opt-in/opt-out status of these patients as identified by
     * the user's role code and purpose for use
     * @param userRoleList The listing of user roles
     * @param purposeList The listing of purpose for use codes
     * @return The listing of matching consent status (Yes - optIn, No -optOut)
     */
    private List<String> determineReidentOptStatus(List<String> userRoleList, List<String> purposeList) {
        List<String> optStatus = new ArrayList<String>();
        int numRoleAttr = userRoleList.size();
        int numPurposeAttr = purposeList.size();
        if (numRoleAttr != numPurposeAttr) {
            log.error("Error in extracting ReidentificationOptStatus values: " +
                    "Number of User Role Attributes should match number of Purpose For Use Attributes");
        } else {
            for (int idx = 0; idx <
                    numRoleAttr; idx++) {
                String userRole = userRoleList.get(idx).trim();
                String purpose = purposeList.get(idx).trim();
                log.debug("Process role: " + userRole + " Purpose: " + purpose);
                if (purpose.toUpperCase().contains(VALID_PURPOSE)) {
                    if (userRole.contains(VALID_USER_ROLE_CODE)) {
                        optStatus.add("Yes");
                        log.debug("Determined Reidentification Opt-In Status as: Yes");
                    } else {
                        log.debug("User role is not valid set Reidentification Opt-In Status: No");
                        optStatus.add("No");
                    }
                } else {
                    log.debug("Purpose for Use is not valid set Reidentification Opt-In Status: No");
                    optStatus.add("No");
                }
            }
        }
        return optStatus;
    }

    /**
     * Creates the XSPA Attributes for the document opt-in status
     * @param documentIdList The listing of document ids used to determine opt-in status
     * @param communityIds The listing of community ids used to determine opt-in status
     * @param repositoryIds The listing of repository ids used to determine opt-in status
     * @return The XSPA Attributes containing the determined consent status (Yes - optIn, No -optOut)
     */
    private List<Attribute> createDocumentOptStatusAttrs(List<String> documentIdList, List<String> communityIds, List<String> repositoryIds, AssertionType assertion) {
        log.debug("Begin createDocumentOptStatusAttrs()..");
        List<Attribute> xspaAttrs = new ArrayList<Attribute>();

        try {
            Attribute xspaAttr = ContextFactory.getInstance().createAttribute();
            xspaAttr.setAttributeId(new URI(XSPA_PATIENT_OPT_IN));
            xspaAttr.setDataType(new URI(XACML_DATATYPE));

            List<String> optStatusVals = determineDocumentOptStatus(documentIdList, communityIds, repositoryIds, assertion);
            for (String optStatusVal : optStatusVals) {
                log.debug("Adding attribute value: " + optStatusVal + " for " + XSPA_PATIENT_OPT_IN);
            }

            xspaAttr.setAttributeStringValues(optStatusVals);
            xspaAttrs.add(xspaAttr);
        } catch (URISyntaxException uriex) {
            log.error("Error in extracting PatientOptStatus values: " + uriex.getMessage());
        } catch (XACMLException xacmlex) {
            log.error("Error in extracting PatientOptStatus values: " + xacmlex.getMessage());
        } catch (RuntimeException rex) {
            log.error("Error in extracting PatientOptStatus values: " + rex.getMessage());
        }
        log.debug("End createDocumentOptStatusAttrs()..");
        return xspaAttrs;
    }

    /**
     * Determines the opt-in/opt-out status as identified by the documet ids
     * @param documentIds The listing of document ids used to determine opt-in status
     * @param communityIds The listing of community ids used to determine opt-in status
     * @param repositoryIds The listing of repository ids used to determine opt-in status
     * @return The listing of matching consent status (Yes - optIn, No -optOut)
     */
    protected List<String> determineDocumentOptStatus(List<String> documentIds, List<String> communityIds, List<String> repositoryIds, AssertionType assertion) {
        log.debug("Begin determineDocumentOptStatus()..");
        List<String> optStatus = new ArrayList<String>();
        int numDocIdAttr = documentIds.size();
        int numCommunityIdAttr = communityIds.size();
        int numRepoIdAttr = repositoryIds.size();
        if (numDocIdAttr != numCommunityIdAttr || numDocIdAttr != numRepoIdAttr || numCommunityIdAttr != numRepoIdAttr) {
            log.error("Error in extracting DocumentOptInStatus values: " +
                    "Number of DocumentId Attributes should match number of Community and Repository Attributes");
        } else {
            for (int idx = 0; idx <
                    numDocIdAttr; idx++) {
                String documentId = documentIds.get(idx).trim();
                String communityId = communityIds.get(idx).trim();
                String repositoryId = repositoryIds.get(idx).trim();

                log.debug("Process document id: " + documentId + " for community: " + communityId + " in repository: " + repositoryId);

                AdapterPIPProxyObjectFactory factory = new AdapterPIPProxyObjectFactory();
                AdapterPIPProxy adapterPIPProxy = factory.getAdapterPIPProxy();

                RetrievePtConsentByPtDocIdRequestType consentReq = new RetrievePtConsentByPtDocIdRequestType();
                consentReq.setDocumentId(documentId);
                consentReq.setHomeCommunityId(communityId);
                consentReq.setRepositoryId(repositoryId);
                RetrievePtConsentByPtDocIdResponseType consentResp = adapterPIPProxy.retrievePtConsentByPtDocId(consentReq, assertion);
                if (consentResp.getPatientPreferences().isOptIn()) {
                    optStatus.add("Yes");
                    log.debug("Determined Patient Opt-In Status as: Yes");
                } else {
                    log.debug("Determined Patient Opt-In Status as: No");
                    optStatus.add("No");
                }
            }
        }
        log.debug("End determineDocumentOptStatus()..");
        return optStatus;
    }

    /**
     * Creates the XSPA Attributes for the patient status
     * @param resourceIdList The listing of patient ids
     * @param assigningAuthList The listing of matching assigning authorities
     * @param assertion Assertion
     * @return The XSPA Attributes containing the determined consent status (Yes - optIn, No -optOut)
     */
    private List<Attribute> createPatientOptStatusAttrs(List<String> resourceIdList, List<String> assigningAuthList, AssertionType assertion) {
        log.debug("Begin createPatientOptStatusAttrs()..");
        List<Attribute> xspaAttrs = new ArrayList<Attribute>();

        try {
            Attribute xspaAttr = ContextFactory.getInstance().createAttribute();
            xspaAttr.setAttributeId(new URI(XSPA_PATIENT_OPT_IN));
            xspaAttr.setDataType(new URI(XACML_DATATYPE));

            List<String> optStatusVals = determinePatientOptStatus(resourceIdList, assigningAuthList, assertion);
            for (String optStatusVal : optStatusVals) {
                log.debug("Adding attribute value: " + optStatusVal + " for " + XSPA_PATIENT_OPT_IN);
            }

            xspaAttr.setAttributeStringValues(optStatusVals);
            xspaAttrs.add(xspaAttr);
        } catch (URISyntaxException uriex) {
            log.error("Error in extracting PatientOptStatus values: " + uriex.getMessage());
        } catch (XACMLException xacmlex) {
            log.error("Error in extracting PatientOptStatus values: " + xacmlex.getMessage());
        }
        log.debug("End createPatientOptStatusAttrs()..");
        return xspaAttrs;
    }

    /**
     * Determines the opt-in/opt-out status of these patients as identified by their patient ids
     * @param resourceIds The listing of patient ids
     * @param assigningAuths The listing of matching assigning authorities
     * @param assertion Assertion
     * @return The listing of matching consent status (Yes - optIn, No -optOut)
     */
    protected List<String> determinePatientOptStatus(List<String> resourceIds, List<String> assigningAuths, AssertionType assertion) {
        log.debug("Begin determinePatientOptStatus()..");
        List<String> optStatus = new ArrayList<String>();
        int numIdAttr = resourceIds.size();
        int numAuthAttr = assigningAuths.size();
        if (numIdAttr != numAuthAttr) {
            log.error("Error in extracting PatientOptStatus values: " +
                    "Number of Patient Id Attributes should match number of Assigning Authority Attributes");
        } else {
            for (int idx = 0; idx <
                    numIdAttr; idx++) {
                String patientId = resourceIds.get(idx).trim();
                String authId = assigningAuths.get(idx).trim();
                log.debug("Process id: " + patientId + " Authority: " + authId);

                AdapterPIPProxyObjectFactory factory = new AdapterPIPProxyObjectFactory();
                AdapterPIPProxy adapterPIPProxy = factory.getAdapterPIPProxy();
                RetrievePtConsentByPtIdRequestType consentReq = new RetrievePtConsentByPtIdRequestType();
                consentReq.setPatientId(patientId);
                consentReq.setAssigningAuthority(authId);
                consentReq.setAssertion(assertion);
                RetrievePtConsentByPtIdResponseType consentResp = adapterPIPProxy.retrievePtConsentByPtId(consentReq, assertion);
                if (consentResp.getPatientPreferences().isOptIn()) {
                    optStatus.add("Yes");
                    log.debug("Determined Patient Opt-In Status as: Yes");
                } else {
                    log.debug("Determined Patient Opt-In Status as: No");
                    optStatus.add("No");
                }
            }
        }
        log.debug("End determinePatientOptStatus()..");
        return optStatus;
    }

    /**
     * Determines the action to give to the XSPA action attribute as mapped
     * from the XACML Action Attribute
     * @param extractedVals The listing of the XACML actions
     * @return The listing of the matching XSPA actions
     */
    private List<String> determineXSPAAction(List<String> extractedVals) {
        List<String> translatedActions = new ArrayList<String>();
        // Action values in the request message reflect the NHIN service and
        // the direction being inbound or outbound
        for (String actionValue : extractedVals) {
            translatedActions.add(actionMap.get(actionValue.trim()));
            log.debug("Translated Action: " + actionValue.trim() + " to " + actionMap.get(actionValue.trim()));
        }

        return translatedActions;
    }

    /**
     * This method creates a default Attribute for the given XSPA identifier.
     * @param xspaId The Attribute Id to give to the generated XSPA attribute
     * @param value The value to set within the default attribute created
     * @return The listing of the generated XSPA attributes
     */
    private List<Attribute> createDefaultAttrs(String xspaId, String value) {
        log.debug("Begin createDefaultAttrs()..");
        List<Attribute> xspaAttrs = new ArrayList<Attribute>();

        try {
            Attribute xspaAttr = ContextFactory.getInstance().createAttribute();
            xspaAttr.setAttributeId(new URI(xspaId));
            xspaAttr.setDataType(new URI(XACML_DATATYPE));
            List valList = new ArrayList();
            valList.add(value);

            xspaAttr.setAttributeStringValues(valList);
            xspaAttrs.add(xspaAttr);

        } catch (URISyntaxException uriex) {
            log.error("Error in creating default values: " + uriex.getMessage());
        } catch (XACMLException xacmlex) {
            log.error("Error in creating default values: " + xacmlex.getMessage());
        }

        return xspaAttrs;
    }
}
