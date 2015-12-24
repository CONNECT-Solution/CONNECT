/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.policyengine.adapter.pep;

import com.sun.identity.saml2.common.SAML2Exception;
import com.sun.identity.xacml.common.XACMLException;
import com.sun.identity.xacml.context.Action;
import com.sun.identity.xacml.context.Attribute;
import com.sun.identity.xacml.context.ContextFactory;
import com.sun.identity.xacml.context.Decision;
import com.sun.identity.xacml.context.Environment;
import com.sun.identity.xacml.context.Request;
import com.sun.identity.xacml.context.Resource;
import com.sun.identity.xacml.context.Response;
import com.sun.identity.xacml.context.Result;
import com.sun.identity.xacml.context.Subject;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
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
import java.util.Iterator;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements the policy engine PEP (Policy Enforcement Point).
 */
public class AdapterPEPImpl {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterPEPImpl.class);
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
    private static final String[] xacmlActionDef = {"PatientDiscoveryIn", "PatientDiscoveryOut", "DocumentQueryIn",
        "DocumentQueryOut", "DocumentRetrieveIn", "DocumentRetrieveOut", "AuditLogQueryIn", "AuditLogQueryOut",
        "HIEMSubscriptionRequestIn", "HIEMSubscriptionRequestOut", "HIEMSubscriptionCancelIn",
        "HIEMSubscriptionCancelOut", "HIEMNotifyIn", "HIEMNotifyOut", "XDRIn", "XDROut"};
    // These define the xspa actions corresponding to the xacml definitions
    // Correlation is order driven
    private static final String[] xspaActionDef = {"create", "create", "read", "read", "read", "read", "read", "read",
        "create", "create", "delete", "delete", "update", "update", "read", "read", "create", "create"};
    // Mapping of the NHIN actions to the cooresponding XSPA action
    private static Map<String, String> actionMap = new HashMap<String, String>();

    static {
        int numActions = xacmlActionDef.length;
        for (int actionIdx = 0; actionIdx < numActions; actionIdx++) {
            actionMap.put(xacmlActionDef[actionIdx], xspaActionDef[actionIdx]);
        }
    }

    /**
     * Given a request to check the access policy, this service will interface with the PDP to determine if access is to
     * be granted or denied.
     *
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

                LOG.debug("PDP Response: " + pdpResponse.toXMLString());

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
                LOG.error("PropertyAccessException thrown from XACMLRequestProcessor: {}", pex.getLocalizedMessage(),
                    pex);
            } catch (XACMLException xex) {
                checkPolicyResp = createResponse(DecisionType.DENY);
                try {
                    if (pdpRequest != null) {
                        LOG.error("Unable to process PDP request: " + pdpRequest.toXMLString());
                    }
                } catch (XACMLException ex) {
                    LOG.error("Failed to conver PDP request to XML string: {}", ex.getLocalizedMessage(), ex);
                }
                LOG.error("XACMLException thrown from XACMLRequestProcessor: {}", xex.getLocalizedMessage(), xex);
            } catch (SAML2Exception samlex) {
                checkPolicyResp = createResponse(DecisionType.DENY);
                LOG.error("SAML2Exception thrown by XACMLRequestProcessor: {}" + samlex.getLocalizedMessage(), samlex);
            }
        } else {
            checkPolicyResp = createResponse(DecisionType.DENY);
        }

        return checkPolicyResp;
    }

    /**
     * Creates a check policy response message containing the decision declaration
     *
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
     * The PDP (Policy Decision Point) request document is a XACML XML document based on the XSPA profile.
     *
     * @param checkPolicyRequest The xacml request to check defined policy
     * @return The PDP compatible request document
     */
    public Request createPdpRequest(CheckPolicyRequestType checkPolicyRequest, AssertionType assertion) {

        Request request = ContextFactory.getInstance().createRequest();

        try {
            /*
             * Create Subject has a subject id, subject organization, subject organization id, home community id, an
             * optional user role, and a purpose of use
             */
            Subject subject = ContextFactory.getInstance().createSubject();
            List<Attribute> subjAttrList = new ArrayList<Attribute>();

            // Subject id must be present it is extracted from the Subject of the request
            List<Attribute> subjIdList = createSubjAttrs(checkPolicyRequest, XACML_SUBJECT_ID, XSPA_SUBJECT_ID, null);
            removeEmptyItems(subjIdList);
            if (subjIdList.isEmpty()) {
                LOG.debug(XSPA_SUBJECT_ID + " Attribute is empty");
            }
            subjAttrList.addAll(subjIdList);

            // Subject organization must be present it is extracted from the Subject of the request
            List<Attribute> subjOrgList = createSubjAttrs(checkPolicyRequest, XACML_SUBJECT_ORG, XSPA_SUBJECT_ORG,
                null);
            removeEmptyItems(subjOrgList);
            if (subjOrgList.isEmpty()) {
                LOG.debug(XSPA_SUBJECT_ORG + " Attribute is empty");
            }
            subjAttrList.addAll(subjOrgList);

            // Subject organization id must be present it is extracted from the Subject of the request
            List<Attribute> subjOrgIdList = createSubjAttrs(checkPolicyRequest, XACML_SUBJECT_ORG_ID,
                XSPA_SUBJECT_ORG_ID, null);
            removeEmptyItems(subjOrgIdList);
            if (subjOrgIdList.isEmpty()) {
                LOG.debug(XSPA_SUBJECT_ID + " Attribute is empty");
            }
            subjAttrList.addAll(subjOrgIdList);

            // // Home community id must be present
            // // In inbound messages it is extracted from the Subject of the request
            // // In outbound messages it is looked up in our gateway properties
            // List<Attribute> subjHomeCommunityList = createSubjAttrs(checkPolicyRequest, XACML_HOME_COMMUNITY,
            // XSPA_ENVIRONMENT_LOCALITY, null);
            // if (subjHomeCommunityList.isEmpty()) {
            // log.debug("Sender community is assumed to be this gateway");
            // subjHomeCommunityList.addAll(createSubjLocAttrs());
            // }
            // if (subjHomeCommunityList.isEmpty()) {
            // log.debug(XSPA_ENVIRONMENT_LOCALITY + " Attribute is empty");
            // }
            // subjAttrList.addAll(subjHomeCommunityList);
            // User role is optional
            List<String> extractedUserRoles = new ArrayList<String>();
            List<Attribute> subjUserRoleList = createSubjAttrs(checkPolicyRequest, XACML_SUBJECT_ROLE,
                XSPA_SUBJECT_ROLE, extractedUserRoles);
            removeEmptyItems(subjUserRoleList);
            if (subjUserRoleList.isEmpty()) {
                LOG.debug(XSPA_SUBJECT_ROLE + " Attribute is empty");
            }
            subjAttrList.addAll(subjUserRoleList);

            // Purpose of use must be present
            List<String> extractedPurpose = new ArrayList<String>();
            List<Attribute> subjPurposeList = createSubjAttrs(checkPolicyRequest, XACML_SUBJECT_PURPOSE,
                XSPA_SUBJECT_PURPOSE, extractedPurpose);
            removeEmptyItems(subjPurposeList);
            if (subjPurposeList.isEmpty()) {
                LOG.debug("Create a default " + XSPA_SUBJECT_PURPOSE + " Attribute");
                subjPurposeList.addAll(createDefaultAttrs(XSPA_SUBJECT_PURPOSE, DEFAULT_PURPOSE_TEXT));
            }
            subjAttrList.addAll(subjPurposeList);

            // Add the Subject into the PDP request
            subject.setAttributes(subjAttrList);

            List<Subject> subjectList = new ArrayList<Subject>();
            subjectList.add(subject);
            request.setSubjects(subjectList);

            /*
             * Create Resource has a patient id, patient home community, service type, and patient opt-in/opt-out
             */
            Resource resource = ContextFactory.getInstance().createResource();
            List<Attribute> resourceAttrList = new ArrayList<Attribute>();
            List<Attribute> resourcePatientOptInList = new ArrayList<Attribute>();

            // Service type must be present - identifies the NHIN service inbound or outbound
            // It is extracted from the Action attribute of the request
            List<String> extractedServices = new ArrayList<String>();
            List<Attribute> resourceServiceList = createActionAttrs(checkPolicyRequest, XACML_ACTION,
                XSPA_SERVICE_TYPE, extractedServices);
            removeEmptyItems(resourceServiceList);
            if (resourceServiceList.isEmpty()) {
                LOG.debug(XSPA_SERVICE_TYPE + " Attribute is empty");
            }
            resourceAttrList.addAll(resourceServiceList);

            // Resource id is only present if the request is patient specific
            List<String> extractedResourceIds = new ArrayList<String>();
            List<Attribute> resourceIdList = createResourceAttrs(checkPolicyRequest, XACML_RESOURCE_ID,
                XSPA_RESOURCE_ID, extractedResourceIds);
            removeEmptyItems(resourceIdList);
            if (resourceIdList.isEmpty()) {
                LOG.debug(XSPA_RESOURCE_ID + " Attribute is empty");
            }
            resourceAttrList.addAll(resourceIdList);
            if (!extractedResourceIds.isEmpty()) {
                LOG.debug("createPdpRequest - extractedResourceIds not empty");
                // If request is patient specific then get the home community
                List<String> extractedCommunityIds = new ArrayList<String>();
                List<Attribute> resourceHomeCommunityList = createResourceAttrs(checkPolicyRequest,
                    XACML_ASSIGING_AUTH, XSPA_ASSIGNING_AUTH, extractedCommunityIds);
                if (resourceHomeCommunityList.isEmpty()) {
                    LOG.debug(XSPA_ASSIGNING_AUTH + " Attribute is empty");
                }
                resourceAttrList.addAll(resourceHomeCommunityList);

                if (!extractedResourceIds.isEmpty() && !extractedCommunityIds.isEmpty()) {
                    // The existance of a patient identifier in the request indicates that
                    // the policy engine needs to check the patient opt-in status (Yes or No)
                    resourcePatientOptInList.addAll(createPatientOptStatusAttrs(extractedResourceIds,
                        extractedCommunityIds, assertion));
                    // Patient Opt-In or Opt-Out is optional - assume opt-out (No) if missing
                    if (resourcePatientOptInList.isEmpty()) {
                        LOG.debug("Create a default " + XSPA_PATIENT_OPT_IN + " Attribute with value Opt-Out");
                        resourcePatientOptInList.addAll(createDefaultAttrs(XSPA_PATIENT_OPT_IN, "No"));
                    }
                } else {
                    LOG.debug("Create a default " + XSPA_PATIENT_OPT_IN + " Attribute with value Opt-Out");
                    resourcePatientOptInList.addAll(createDefaultAttrs(XSPA_PATIENT_OPT_IN, "No"));
                }
            } else {
                // If message is not patient-centric it may be document-centric or subscription based
                // If document-centric then the patient opt-in status is checked by document-id
                LOG.debug("createPdpRequest - extractedResourceIds is empty");
                List<String> extractedDocIds = new ArrayList<String>();
                List<String> extractedCommunityIds = new ArrayList<String>();
                List<String> extractedRepositoryIds = new ArrayList<String>();
                // Note that these are not used in the construction of the request

                createResourceAttrs(checkPolicyRequest, XACML_DOCUMENT_ID, XSPA_RESOURCE_ID, extractedDocIds);
                createResourceAttrs(checkPolicyRequest, XACML_DOC_COMMUNITY_ID, XSPA_RESOURCE_ID,
                    extractedCommunityIds);
                createResourceAttrs(checkPolicyRequest, XACML_DOC_REPOSITORY_ID, XSPA_RESOURCE_ID,
                    extractedRepositoryIds);

                if (!extractedDocIds.isEmpty()) {
                    LOG.debug("createPdpRequest - extractedDocIds.size: " + extractedDocIds.size());
                    for (String docId : extractedDocIds) {
                        LOG.debug("createPdpRequest - docId: " + docId);
                    }

                    try {
                        Attribute docResAttr = ContextFactory.getInstance().createAttribute();
                        docResAttr.setAttributeId(new URI(XACML_RESOURCE_ID));
                        docResAttr.setDataType(new URI(XACML_DATATYPE));
                        docResAttr.setAttributeStringValues(extractedDocIds);
                        resourcePatientOptInList.add(docResAttr);
                    } catch (URISyntaxException | XACMLException ex) {
                        LOG.error("Error in setting attribute value for documentid resource-id: {}",
                            ex.getLocalizedMessage(), ex);
                    }

                    // Patient Opt-In or Opt-Out is optional - assume opt-out (No) if missing
                    // The existance of a document identifier in the request indicates that
                    // the policy engine needs to check the patient opt-in status (Yes or No)
                    resourcePatientOptInList.addAll(createDocumentOptStatusAttrs(extractedDocIds,
                        extractedCommunityIds, extractedRepositoryIds, assertion));
                    if (resourcePatientOptInList.isEmpty()) {
                        LOG.debug("Create a default " + XSPA_PATIENT_OPT_IN + " Attribute with value Opt-Out");
                        resourcePatientOptInList.addAll(createDefaultAttrs(XSPA_PATIENT_OPT_IN, "No"));
                    }
                } else {
                    LOG.debug("createPdpRequest - extractedDocIds is empty");
                    // If it is not patient specific nor document specific then look for subscription
                    List<String> extractedSubscription = new ArrayList<String>();
                    createResourceAttrs(checkPolicyRequest, XACML_SUBSCRIPTION_ID, XSPA_RESOURCE_ID,
                        extractedSubscription);
                    if (!extractedSubscription.isEmpty()) {
                        // Subscription requests are automatically opted-In
                        LOG.debug("Subscription request assigns " + XSPA_PATIENT_OPT_IN
                            + " Attribute with value Opt-In");
                        resourcePatientOptInList.addAll(createDefaultAttrs(XSPA_PATIENT_OPT_IN, "Yes"));
                    } else {
                        // This message type is not identifiable as patient specific, document specific, or subscription
                        // based
                        // The only other valid type that is known is AuditLogQuery
                        if (extractedServices.contains("AuditLogQueryIn")
                            || extractedServices.contains("AuditLogQueryOut")) {
                            LOG.debug("Audit Log Query request assigns " + XSPA_PATIENT_OPT_IN
                                + " Attribute with value Opt-In");
                            resourcePatientOptInList.addAll(createDefaultAttrs(XSPA_PATIENT_OPT_IN, "Yes"));
                        } else if (extractedServices.contains("HIEMNotifyIn")
                            || extractedServices.contains("HIEMNotifyOut")) {
                            LOG.debug("HIEM Notify request assigns " + XSPA_PATIENT_OPT_IN
                                + " Attribute with value Opt-In");
                            resourcePatientOptInList.addAll(createDefaultAttrs(XSPA_PATIENT_OPT_IN, "Yes"));
                        } else {
                            LOG.debug("Unknown message type assign " + XSPA_PATIENT_OPT_IN
                                + " Attribute with value Opt-Out");
                            resourcePatientOptInList.addAll(createDefaultAttrs(XSPA_PATIENT_OPT_IN, "No"));
                        }
                    }
                }
            }

            removeEmptyItems(resourcePatientOptInList);
            if (resourcePatientOptInList.isEmpty()) {
                LOG.debug("Create a default " + XSPA_PATIENT_OPT_IN + " Attribute with value Opt-Out");
                resourcePatientOptInList.addAll(createDefaultAttrs(XSPA_PATIENT_OPT_IN, "No"));
            }
            resourceAttrList.addAll(resourcePatientOptInList);

            // Add the Resource into the PDP request
            resource.setAttributes(resourceAttrList);

            List<Resource> resourceList = new ArrayList<Resource>();
            resourceList.add(resource);
            request.setResources(resourceList);

            /*
             * Create Action recognized values are: create, read, update, delete, execute, suspend.
             */
            Action action = ContextFactory.getInstance().createAction();
            List<Attribute> actionAttrList = new ArrayList<Attribute>();

            // Action must be present, depends on the action in the request.
            List<Attribute> actionIdList = createActionAttrs(checkPolicyRequest, XACML_ACTION, XSPA_ACTION, null);
            removeEmptyItems(actionIdList);
            if (actionIdList.isEmpty()) {
                LOG.debug(XSPA_ACTION + " Attribute is empty");
                actionIdList.addAll(createDefaultAttrs(XSPA_ACTION, "Unknown"));
            }
            actionAttrList.addAll(actionIdList);

            // Add the Action into the PDP request
            action.setAttributes(actionAttrList);
            request.setAction(action);

            // Environnment, required but not used
            Environment environment = ContextFactory.getInstance().createEnvironment();
            List<Attribute> envAttrList = new ArrayList<Attribute>();

            // Home community id must be present
            // In inbound messages it is extracted from the Subject of the request
            // In outbound messages it is looked up in our gateway properties
            List<Attribute> envHomeCommunityList = createSubjAttrs(checkPolicyRequest, XACML_HOME_COMMUNITY,
                XSPA_ENVIRONMENT_LOCALITY, null);
            removeEmptyItems(envHomeCommunityList);
            if (envHomeCommunityList.isEmpty()) {
                LOG.debug(XSPA_ENVIRONMENT_LOCALITY + " Attribute is empty");
                LOG.debug("Sender community is assumed to be this gateway");
                envHomeCommunityList.addAll(createEnvLocAttrs());
            }
            envAttrList.addAll(envHomeCommunityList);

            // Add the Environment into the PDP request
            environment.setAttributes(envAttrList);
            request.setEnvironment(environment);

            LOG.debug("AdapterPEPImpl.createPdpRequest with PDP request: \n" + request.toXMLString());
        } catch (XACMLException ex) {
            LOG.error("Error in AdapterPEPImpl.createPdpRequest: {}", ex.getLocalizedMessage(), ex);
        }
        return request;
    }

    private List<Attribute> removeEmptyItems(List<Attribute> attrs) {
        if (attrs == null) {
            return attrs;
        }
        for (Iterator<Attribute> i = attrs.iterator(); i.hasNext();) {
            Attribute attr = i.next();
            if (attr != null) {
                if (attr.getAttributeValues() == null) {
                    i.remove(); // Remove item with empty values
                }
            } else {
                i.remove(); // Remove null item
            }
        }
        return attrs;
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
    private List<Attribute> createSubjAttrs(CheckPolicyRequestType checkPolicyRequest, String xacmlId, String xspaId,
        List<String> extractedVals) {

        List<Attribute> retSubjList = new ArrayList<Attribute>();

        if (checkPolicyRequest != null && checkPolicyRequest.getRequest() != null
            && checkPolicyRequest.getRequest().getSubject() != null
            && !checkPolicyRequest.getRequest().getSubject().isEmpty()) {
            for (SubjectType subjItem : checkPolicyRequest.getRequest().getSubject()) {
                if (subjItem != null && subjItem.getAttribute() != null && !subjItem.getAttribute().isEmpty()) {

                    List<Attribute> xspaAttrs = extractAttrs(subjItem.getAttribute(), xacmlId, xspaId, extractedVals);
                    if (xspaAttrs.isEmpty()) {
                        LOG.debug("Subject Attributes for check policy request do not include values for " + xacmlId);
                    } else {
                        retSubjList.addAll(xspaAttrs);
                    }

                } else {
                    LOG.debug("No Subject Attribute elements found in the check policy request");
                }

            }
        } else {
            LOG.debug("No Subject element found in the check policy request");
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
    private List<Attribute> createResourceAttrs(CheckPolicyRequestType checkPolicyRequest, String xacmlId,
        String xspaId, List<String> extractedVals) {
        LOG.debug("Begin createResourceAttrs()..");
        List<Attribute> retResourceList = new ArrayList<Attribute>();

        if (checkPolicyRequest != null && checkPolicyRequest.getRequest() != null
            && checkPolicyRequest.getRequest().getResource() != null
            && !checkPolicyRequest.getRequest().getResource().isEmpty()) {
            for (ResourceType resourceItem : checkPolicyRequest.getRequest().getResource()) {
                if (resourceItem != null && resourceItem.getAttribute() != null
                    && !resourceItem.getAttribute().isEmpty()) {
                    List<Attribute> xspaAttrs = extractAttrs(resourceItem.getAttribute(), xacmlId, xspaId,
                        extractedVals);
                    if (xspaAttrs.isEmpty()) {
                        LOG.debug("Resource Attributes for check policy request do not include values for " + xacmlId);
                    } else {
                        retResourceList.addAll(xspaAttrs);
                    }

                } else {
                    LOG.debug("No Resource Attribute elements found in the check policy request");
                }
            }
        } else {
            LOG.debug("No Resource element found in the check policy request");
        }
        LOG.debug("End createResourceAttrs()..");
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
    private List<Attribute> createActionAttrs(CheckPolicyRequestType checkPolicyRequest, String xacmlId, String xspaId,
        List<String> extractedVals) {

        List<Attribute> retActionList = new ArrayList<Attribute>();

        if (checkPolicyRequest != null && checkPolicyRequest.getRequest() != null
            && checkPolicyRequest.getRequest().getAction() != null) {
            ActionType actionItem = checkPolicyRequest.getRequest().getAction();
            if (actionItem != null && actionItem.getAttribute() != null && !actionItem.getAttribute().isEmpty()) {

                List<Attribute> xspaAttrs = extractAttrs(actionItem.getAttribute(), xacmlId, xspaId, extractedVals);
                if (xspaAttrs.isEmpty()) {
                    LOG.debug("Action Attributes for check policy request do not include values for " + xacmlId);
                } else {
                    retActionList.addAll(xspaAttrs);
                }

            } else {
                LOG.debug("No Action Attribute elements found in the check policy request");
            }

        } else {
            LOG.debug("No Action element found in the check policy request");
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
    private List<Attribute> extractAttrs(List<AttributeType> xacmlAttrs, String xacmlId, String xspaId,
        List<String> extractedVals) {
        LOG.debug("Begin extractAttrs()..");
        List<Attribute> xspaAttrs = new ArrayList<Attribute>();

        for (AttributeType xacmlAttr : xacmlAttrs) {
            if (xacmlAttr.getAttributeId().equals(xacmlId)) {
                try {
                    Attribute xspaAttr = ContextFactory.getInstance().createAttribute();
                    xspaAttr.setAttributeId(new URI(xspaId));
                    xspaAttr.setDataType(new URI(XACML_DATATYPE));
                    List<String> extractedContent = new ArrayList<String>();
                    for (AttributeValueType attrVal : xacmlAttr.getAttributeValue()) {
                        for (Object values : attrVal.getContent()) {
                            extractedContent.add(values.toString().trim());
                        }
                    }
                    if (extractedContent.isEmpty()) {
                        LOG.debug("The attribute in the check policy request has no values for " + xacmlId);
                    } else {
                        if (extractedVals != null) {
                            extractedVals.addAll(extractedContent);
                        }

                        if (XSPA_ACTION.equals(xspaId)) {
                            List<String> xspaActionVals = determineXSPAAction(extractedContent);
                            for (String xspaActionVal : xspaActionVals) {
                                LOG.debug("Adding attribute value: " + xspaActionVal + " for " + xspaId);
                            }

                            xspaAttr.setAttributeStringValues(xspaActionVals);
                        } else {
                            for (String extractedVal : extractedContent) {
                                LOG.debug("Adding attribute value: " + extractedVal + " for " + xspaId);
                            }

                            xspaAttr.setAttributeStringValues(extractedContent);
                        }

                    }
                    xspaAttrs.add(xspaAttr);
                } catch (URISyntaxException | XACMLException ex) {
                    LOG.error("Error in extracting {} values: ", xacmlId, ex.getLocalizedMessage(), ex);
                }
            }
        }
        LOG.debug("End extractAttrs()..");
        return xspaAttrs;
    }

    /**
     * Creates the XSPA Attributes for the environment locality based upon a lookup of the gateway property for home
     * community
     *
     * @return The XSPA Attributes containing the determined home community
     */
    private List<Attribute> createEnvLocAttrs() {
        LOG.debug("Begin createEnvLocAttrs()..");
        List<Attribute> xspaAttrs = new ArrayList<Attribute>();

        try {
            Attribute xspaAttr = ContextFactory.getInstance().createAttribute();
            xspaAttr.setAttributeId(new URI(XSPA_ENVIRONMENT_LOCALITY));
            xspaAttr.setDataType(new URI(XACML_DATATYPE));

            List<String> homeCommunityVals = new ArrayList<String>();
            String homeCommunityId = PropertyAccessor.getInstance().getProperty(PROPERTY_FILE_NAME_GATEWAY,
                PROPERTY_FILE_KEY_HOME_COMMUNITY);
            LOG.debug("Adding hcid attribute value for " + XSPA_ENVIRONMENT_LOCALITY);
            homeCommunityVals.add(homeCommunityId);

            xspaAttr.setAttributeStringValues(homeCommunityVals);
            xspaAttrs.add(xspaAttr);
        } catch (PropertyAccessException | URISyntaxException | XACMLException ex) {
            LOG.error("Error in extracting Home Community values: {}", ex.getLocalizedMessage(), ex);
        }

        LOG.debug("End createEnvLocAttrs()..");
        return xspaAttrs;
    }

    /**
     * Creates the XSPA Attributes for the document opt-in status
     *
     * @param documentIdList The listing of document ids used to determine opt-in status
     * @param communityIds The listing of community ids used to determine opt-in status
     * @param repositoryIds The listing of repository ids used to determine opt-in status
     * @return The XSPA Attributes containing the determined consent status (Yes - optIn, No -optOut)
     */
    private List<Attribute> createDocumentOptStatusAttrs(List<String> documentIdList, List<String> communityIds,
        List<String> repositoryIds, AssertionType assertion) {
        LOG.debug("Begin createDocumentOptStatusAttrs()..");
        List<Attribute> xspaAttrs = new ArrayList<Attribute>();

        try {
            Attribute xspaAttr = ContextFactory.getInstance().createAttribute();
            xspaAttr.setAttributeId(new URI(XSPA_PATIENT_OPT_IN));
            xspaAttr.setDataType(new URI(XACML_DATATYPE));

            List<String> optStatusVals = determineDocumentOptStatus(documentIdList, communityIds, repositoryIds,
                assertion);
            for (String optStatusVal : optStatusVals) {
                LOG.debug("Adding attribute value: " + optStatusVal + " for " + XSPA_PATIENT_OPT_IN);
            }

            xspaAttr.setAttributeStringValues(optStatusVals);
            xspaAttrs.add(xspaAttr);
        } catch (URISyntaxException | XACMLException | RuntimeException ex) {
            LOG.error("Error in extracting PatientOptStatus values: {}", ex.getLocalizedMessage(), ex);
        }
        LOG.debug("End createDocumentOptStatusAttrs()..");
        return xspaAttrs;
    }

    /**
     * Determines the opt-in/opt-out status as identified by the documet ids
     *
     * @param documentIds The listing of document ids used to determine opt-in status
     * @param communityIds The listing of community ids used to determine opt-in status
     * @param repositoryIds The listing of repository ids used to determine opt-in status
     * @return The listing of matching consent status (Yes - optIn, No -optOut)
     */
    protected List<String> determineDocumentOptStatus(List<String> documentIds, List<String> communityIds,
        List<String> repositoryIds, AssertionType assertion) {
        LOG.debug("Begin determineDocumentOptStatus()..");
        List<String> optStatus = new ArrayList<String>();
        int numDocIdAttr = documentIds.size();
        int numCommunityIdAttr = communityIds.size();
        int numRepoIdAttr = repositoryIds.size();
        if (numDocIdAttr != numCommunityIdAttr || numDocIdAttr != numRepoIdAttr || numCommunityIdAttr
            != numRepoIdAttr) {
            LOG.error("Error in extracting DocumentOptInStatus values: "
                + "Number of DocumentId Attributes should match number of Community and Repository Attributes");
        } else {
            for (int idx = 0; idx < numDocIdAttr; idx++) {
                String documentId = documentIds.get(idx).trim();
                String communityId = communityIds.get(idx).trim();
                String repositoryId = repositoryIds.get(idx).trim();

                LOG.debug("Process document id: " + documentId + " for community: " + communityId + " in repository: "
                    + repositoryId);

                AdapterPIPProxyObjectFactory factory = new AdapterPIPProxyObjectFactory();
                AdapterPIPProxy adapterPIPProxy = factory.getAdapterPIPProxy();

                RetrievePtConsentByPtDocIdRequestType consentReq = new RetrievePtConsentByPtDocIdRequestType();
                consentReq.setDocumentId(documentId);
                consentReq.setHomeCommunityId(communityId);
                consentReq.setRepositoryId(repositoryId);
                RetrievePtConsentByPtDocIdResponseType consentResp = adapterPIPProxy.retrievePtConsentByPtDocId(
                    consentReq, assertion);
                if (consentResp.getPatientPreferences().isOptIn()) {
                    optStatus.add("Yes");
                    LOG.debug("Determined Patient Opt-In Status as: Yes");
                } else {
                    LOG.debug("Determined Patient Opt-In Status as: No");
                    optStatus.add("No");
                }
            }
        }
        LOG.debug("End determineDocumentOptStatus()..");
        return optStatus;
    }

    /**
     * Creates the XSPA Attributes for the patient status
     *
     * @param resourceIdList The listing of patient ids
     * @param assigningAuthList The listing of matching assigning authorities
     * @param assertion Assertion
     * @return The XSPA Attributes containing the determined consent status (Yes - optIn, No -optOut)
     */
    private List<Attribute> createPatientOptStatusAttrs(List<String> resourceIdList, List<String> assigningAuthList,
        AssertionType assertion) {
        LOG.debug("Begin createPatientOptStatusAttrs()..");
        List<Attribute> xspaAttrs = new ArrayList<Attribute>();

        try {
            Attribute xspaAttr = ContextFactory.getInstance().createAttribute();
            xspaAttr.setAttributeId(new URI(XSPA_PATIENT_OPT_IN));
            xspaAttr.setDataType(new URI(XACML_DATATYPE));

            List<String> optStatusVals = determinePatientOptStatus(resourceIdList, assigningAuthList, assertion);
            for (String optStatusVal : optStatusVals) {
                LOG.debug("Adding attribute value: " + optStatusVal + " for " + XSPA_PATIENT_OPT_IN);
            }

            xspaAttr.setAttributeStringValues(optStatusVals);
            xspaAttrs.add(xspaAttr);
        } catch (URISyntaxException | XACMLException ex) {
            LOG.error("Error in extracting PatientOptStatus values: {}", ex.getLocalizedMessage(), ex);
        }
        LOG.debug("End createPatientOptStatusAttrs()..");
        return xspaAttrs;
    }

    /**
     * Determines the opt-in/opt-out status of these patients as identified by their patient ids
     *
     * @param resourceIds The listing of patient ids
     * @param assigningAuths The listing of matching assigning authorities
     * @param assertion Assertion
     * @return The listing of matching consent status (Yes - optIn, No -optOut)
     */
    protected List<String> determinePatientOptStatus(List<String> resourceIds, List<String> assigningAuths,
        AssertionType assertion) {
        LOG.debug("Begin determinePatientOptStatus()..");
        List<String> optStatus = new ArrayList<String>();
        int numIdAttr = resourceIds.size();
        int numAuthAttr = assigningAuths.size();
        if (numIdAttr != numAuthAttr) {
            LOG.error("Error in extracting PatientOptStatus values: "
                + "Number of Patient Id Attributes should match number of Assigning Authority Attributes");
        } else {
            for (int idx = 0; idx < numIdAttr; idx++) {
                String patientId = resourceIds.get(idx).trim();
                String authId = assigningAuths.get(idx).trim();
                LOG.debug("Process id: " + patientId + " Authority: " + authId);

                AdapterPIPProxyObjectFactory factory = new AdapterPIPProxyObjectFactory();
                AdapterPIPProxy adapterPIPProxy = factory.getAdapterPIPProxy();
                RetrievePtConsentByPtIdRequestType consentReq = new RetrievePtConsentByPtIdRequestType();
                consentReq.setPatientId(patientId);
                consentReq.setAssigningAuthority(authId);
                consentReq.setAssertion(assertion);
                RetrievePtConsentByPtIdResponseType consentResp = adapterPIPProxy.retrievePtConsentByPtId(consentReq,
                    assertion);
                if (consentResp.getPatientPreferences().isOptIn()) {
                    optStatus.add("Yes");
                    LOG.debug("Determined Patient Opt-In Status as: Yes");
                } else {
                    LOG.debug("Determined Patient Opt-In Status as: No");
                    optStatus.add("No");
                }
            }
        }
        LOG.debug("End determinePatientOptStatus()..");
        return optStatus;
    }

    /**
     * Determines the action to give to the XSPA action attribute as mapped from the XACML Action Attribute
     *
     * @param extractedVals The listing of the XACML actions
     * @return The listing of the matching XSPA actions
     */
    private List<String> determineXSPAAction(List<String> extractedVals) {
        List<String> translatedActions = new ArrayList<String>();
        // Action values in the request message reflect the NHIN service and
        // the direction being inbound or outbound
        for (String actionValue : extractedVals) {
            translatedActions.add(actionMap.get(actionValue.trim()));
            LOG.debug("Translated Action: " + actionValue.trim() + " to " + actionMap.get(actionValue.trim()));
        }

        return translatedActions;
    }

    /**
     * This method creates a default Attribute for the given XSPA identifier.
     *
     * @param xspaId The Attribute Id to give to the generated XSPA attribute
     * @param value The value to set within the default attribute created
     * @return The listing of the generated XSPA attributes
     */
    private List<Attribute> createDefaultAttrs(String xspaId, String value) {
        LOG.debug("Begin createDefaultAttrs()..");
        List<Attribute> xspaAttrs = new ArrayList<Attribute>();

        try {
            Attribute xspaAttr = ContextFactory.getInstance().createAttribute();
            xspaAttr.setAttributeId(new URI(xspaId));
            xspaAttr.setDataType(new URI(XACML_DATATYPE));
            List<String> valList = new ArrayList<String>();
            valList.add(value);

            xspaAttr.setAttributeStringValues(valList);
            xspaAttrs.add(xspaAttr);

        } catch (URISyntaxException | XACMLException ex) {
            LOG.error("Error in creating default values: {}", ex.getLocalizedMessage(), ex);
        }

        return xspaAttrs;
    }
}
