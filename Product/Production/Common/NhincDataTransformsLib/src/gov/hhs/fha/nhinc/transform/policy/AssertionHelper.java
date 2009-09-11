/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.policy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
//import gov.hhs.fha.nhinc.common.nhinccommonadapter.RequestType;
import gov.hhs.fha.nhinc.transform.marshallers.CheckPolicyRequestMarshaller;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import oasis.names.tc.xacml._2_0.context.schema.os.ActionType;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResourceType;
import oasis.names.tc.xacml._2_0.context.schema.os.AttributeType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;

/**
 *
 * @author rayj
 */
public class AssertionHelper {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(AssertionHelper.class);
    private static final boolean appendAttributesIfNull = false;

    public static void appendAssertionDataToRequest(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending assertion data to xacml request");

//        if (log.isDebugEnabled()) {
//            CheckPolicyRequestMarshaller marshaller = new CheckPolicyRequestMarshaller();
//            String message = XmlUtility.serializeElementIgnoreFaults(marshaller.marshalCheckPolicyRequest(policyRequest));
//            log.debug("begin transformAdhocQueryToCheckPolicy [" + message + "]");
//        }


        PurposeForUseHelper.appendPurposeForUse(policyRequest, assertion);

        if (assertion != null) {
            appendAuthnStatementAuthnInstant(policyRequest, assertion);
            appendAuthnStatementSessionIndex(policyRequest, assertion);
            appendAuthnStatementAthnContextClassRef(policyRequest, assertion);
            appendAuthnStatementSubjectLocalityAddress(policyRequest, assertion);
            appendAuthnStatementDNSName(policyRequest, assertion);
            appendUserPersonName(policyRequest, assertion);
            appendUserOrganizationName(policyRequest, assertion);
            appendUserRoleCode(policyRequest, assertion);
            appendUserRoleCodeSystem(policyRequest, assertion);
            appendUserRoleCodeSystemName(policyRequest, assertion);
            appendUserRoleCodeDiplayName(policyRequest, assertion);
            appendPurposeForUseCode(policyRequest, assertion);
            appendPurposeForUseCodeSystem(policyRequest, assertion);
            appendPurposeForUseCodeSystemName(policyRequest, assertion);
            appendPurposeForUseCodeDisplayName(policyRequest, assertion);
            appendAuthzDecisionStatementDecision(policyRequest, assertion);
            appendAuthzDecisionStatementResource(policyRequest, assertion);
            appendAuthzDecisionStatementAction(policyRequest, assertion);
            appendAuthzDecisionStatementEvidenceAssertionID(policyRequest, assertion);
            appendAuthzDecisionStatementEvidenceAssertionIssueInstant(policyRequest, assertion);
            appendAuthzDecisionStatementEvidenceAssertionVersion(policyRequest, assertion);
            appendAuthzDecisionStatementEvidenceAssertionIssuer(policyRequest, assertion);
            appendAuthzDecisionStatementEvidenceAssertionConditionsNotBefore(policyRequest, assertion);
            appendAuthzDecisionStatementEvidenceAssertionConditionsNotOnOrAfter(policyRequest, assertion);
            appendAuthzDecisionStatementEvidenceAssertionContentReference(policyRequest, assertion);
            appendAuthzDecisionStatementEvidenceAssertionContentType(policyRequest, assertion);
            appendAuthzDecisionStatementEvidenceAssertionContent(policyRequest, assertion);
//            appendSignatureKeyModulus(policyRequest, assertion);
//            appendSignatureKeyExponent(policyRequest, assertion);
//            appendSignatureValue(policyRequest, assertion);
        } else {
            log.warn("assertion was not set - unable to extract assertion related data to send to policy engine");
        }

        log.debug("end appending assertion data to xacml request");
    }

//    private static void appendAuthInstant(RequestType policyRequest, AssertionType assertion) {
//        log.debug("begin appending auth instant");
//        SubjectType subject = getSubject(policyRequest);
//        String attributeId = XacmlAttributeId.AuthnStatementAuthnInstant;
//        String dataType = Constants.DataTypeString;
//        String attributeValue = extractAuthId(assertion);
//        AttributeHelper.appendAttributeToParent(subject, attributeId, dataType, attributeValue);
//        log.debug("end appending auth instant");
//    }

//    private static String extractAuthId(AssertionType assertion) {
//        String value = null;
//        if (assertion.getSamlAuthnStatement() != null) {
//            value = assertion.getSamlAuthnStatement().getAuthInstant();
//        }
//        return value;
//    }
    private static ActionType getAction(RequestType policyRequest) {
        if (policyRequest == null) {
            throw new NullPointerException("policy request is null");
        }

        ActionType action = policyRequest.getAction();
        if (action == null) {
            action = new ActionType();
            policyRequest.setAction(action);
        }

        return action;
    }

    private static SubjectType getSubject(RequestType policyXacmlRequest) {

        if (policyXacmlRequest == null) {
            throw new NullPointerException("policy request request is null");
        }

        SubjectType subject = policyXacmlRequest.getSubject().get(0);
        if (subject == null) {
            subject = new SubjectType();
            policyXacmlRequest.getSubject().add(subject);
        }

        return subject;
    }

    private static ResourceType getResource(RequestType policyRequest) {
        if (policyRequest == null) {
            throw new NullPointerException("policy request is null");
        }

        ResourceType resource = policyRequest.getResource().get(0);
        if (resource == null) {
            resource = new ResourceType();
            policyRequest.getResource().add(resource);
        }

        return resource;
    }

    private static boolean doesAtributeExist(ActionType action, String attributeName) {
        boolean found = false;
        for (AttributeType attribute : action.getAttribute()) {
            if (attributeName.contentEquals(attribute.getAttributeId())) {
                found = true;
                break;

            }
        }
        return found;
    }

    public static String extractUserName(
            AssertionType assertion) {
        String username = null;
        if ((assertion != null) && (assertion.getUserInfo() != null) && (assertion.getUserInfo().getUserName() != null)) {
            username = assertion.getUserInfo().getUserName();
        }

        return username;
    }

    public static String extractUserHomeCommunity(
            AssertionType assertion) {
        String username = null;
        if ((assertion != null) && (assertion.getUserInfo() != null) && (assertion.getUserInfo().getOrg() != null) && (assertion.getUserInfo().getOrg().getHomeCommunityId() != null)) {
            username = assertion.getUserInfo().getOrg().getHomeCommunityId();
        }

        return username;
    }

    public static String extractPurpose(
            AssertionType assertion) {
        String purpose = null;
        if ((assertion != null) && (assertion.getPurposeOfDisclosureCoded() != null) && (assertion.getPurposeOfDisclosureCoded().getCode() != null)) {
            purpose = assertion.getPurposeOfDisclosureCoded().getCode();
        }

        return purpose;
    }

    public static String extractUserRole(
            AssertionType assertion) {
        String userRole = null;
        if ((assertion != null) && (assertion.getUserInfo() != null) && (assertion.getUserInfo().getRoleCoded() != null) && (assertion.getUserInfo().getRoleCoded().getCode() != null)) {
            userRole = assertion.getUserInfo().getRoleCoded().getCode();
        }

        return userRole;
    }

    private static void appendAuthnStatementAuthnInstant(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending AuthnStatementAuthnInstant");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.AuthnStatementAuthnInstant;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthnStatementAuthnInstant(assertion);
        AttributeHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending AuthnStatementAuthnInstant");
    }

    private static String extractAuthnStatementAuthnInstant(AssertionType assertion) {
        String value = null;
        if (assertion.getSamlAuthnStatement() != null) {
            log.debug("Extracting AuthInstant");
            value = assertion.getSamlAuthnStatement().getAuthInstant();
            log.debug("Extracted AuthInstant value=" + value);
        } else {
            log.debug("Unable to find SamlAuthnStatement in SAML, will not be included in message to policy engine");
        }

        return value;
    }

    private static void appendAuthnStatementSessionIndex(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending AuthnStatementSessionIndex");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.AuthnStatementSessionIndex;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthnStatementSessionIndex(assertion);
        AttributeHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending AuthnStatementSessionIndex");
    }

    private static String extractAuthnStatementSessionIndex(AssertionType assertion) {
        String value = null;
        if (assertion.getSamlAuthnStatement() != null) {
            log.debug("Extracting SessionIndex");
            value = assertion.getSamlAuthnStatement().getSessionIndex();
            log.debug("Extracted SessionIndex value=" + value);
        } else {
            log.debug("Unable to find SamlAuthnStatement in SAML, will not be included in message to policy engine");
        }
        return value;
    }

    private static void appendAuthnStatementAthnContextClassRef(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending AuthnStatementAthnContextClassRef");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.AuthnStatementAthnContextClassRef;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthnStatementAthnContextClassRef(assertion);
        AttributeHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending AuthnStatementAthnContextClassRef");
    }

    private static String extractAuthnStatementAthnContextClassRef(AssertionType assertion) {
        String value = null;
        if (assertion.getSamlAuthnStatement() != null) {
            log.debug("Extracting AuthnStatementAthnContextClassRef");
            value = assertion.getSamlAuthnStatement().getAuthContextClassRef();
            log.debug("Extracted AuthnStatementAthnContextClassRef value=" + value);
        } else {
            log.debug("Unable to find SamlAuthnStatement in SAML, will not be included in message to policy engine");
        }
        return value;
    }

    private static void appendAuthnStatementSubjectLocalityAddress(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending AuthnStatementSubjectLocalityAddress");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.AuthnStatementSubjectLocalityAddress;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthnStatementSubjectLocalityAddress(assertion);
        AttributeHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending AuthnStatementSubjectLocalityAddress");
    }

    private static String extractAuthnStatementSubjectLocalityAddress(AssertionType assertion) {
        String value = null;
        if (assertion.getSamlAuthnStatement() != null) {
            log.debug("Extracting AuthnStatementSubjectLocalityAddress");
            value = assertion.getSamlAuthnStatement().getSubjectLocalityAddress();
            log.debug("Extracted AuthnStatementSubjectLocalityAddress value=" + value);
        } else {
            log.debug("Unable to find SamlAuthnStatement in SAML, will not be included in message to policy engine");
        }
        return value;
    }

    private static void appendAuthnStatementDNSName(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending AuthnStatementDNSName");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.AuthnStatementDNSName;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthnStatementDNSName(assertion);
        AttributeHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending AuthnStatementDNSName");
    }

    private static String extractAuthnStatementDNSName(AssertionType assertion) {
        String value = null;
        if (assertion.getSamlAuthnStatement() != null) {
            log.debug("Extracting SubjectLocalityDNSName");
            value = assertion.getSamlAuthnStatement().getSubjectLocalityDNSName();
            log.debug("Extracted SubjectLocalityDNSName value=" + value);
        } else {
            log.debug("Unable to find SamlAuthnStatement in SAML, will not be included in message to policy engine");
        }
        return value;
    }

    private static void appendUserPersonName(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending UserPersonName");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.UserPersonName;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractUserPersonName(assertion);
        AttributeHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending UserPersonName");
    }

    private static String extractUserPersonName(AssertionType assertion) {
        String value = null;
        if (assertion.getUserInfo() != null) {
            log.debug("Extracting UserPersonName");
            value = assertion.getUserInfo().getUserName();
            log.debug("Extracted UserPersonName value=" + value);
        } else {
            log.debug("Unable to find UserInfo in SAML, will not be included in message to policy engine");
        }
        return value;
    }

    private static void appendUserOrganizationName(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending UserOrganizationName");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.UserOrganizationName;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractUserOrganizationName(assertion);
        AttributeHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending UserOrganizationName");
    }

    private static String extractUserOrganizationName(AssertionType assertion) {
        String value = null;
        if ((assertion.getUserInfo() != null) && (assertion.getUserInfo().getOrg() != null)) {
            log.debug("Extracting UserOrganizationName");
            value = assertion.getUserInfo().getOrg().getName();
            log.debug("Extracted UserOrganizationName value=" + value);
        } else {
            log.debug("Unable to find User Organization in SAML, will not be included in message to policy engine");
        }
        return value;
    }

    private static void appendUserRoleCode(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending UserRoleCode");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.UserRoleCode;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractUserRoleCode(assertion);
        AttributeHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending UserRoleCode");
    }

    private static String extractUserRoleCode(AssertionType assertion) {
        String value = null;
        if ((assertion != null) && (assertion.getUserInfo() != null) && (assertion.getUserInfo().getRoleCoded() != null)) {
            value = assertion.getUserInfo().getRoleCoded().getCode();
        } else {
            log.debug("Unable to find user role in SAML, will not be included in message to policy engine");
        }

        return value;
    }

    private static void appendUserRoleCodeSystem(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending UserRoleCodeSystem");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.UserRoleCodeSystem;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractUserRoleCodeSystem(assertion);
        AttributeHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending UserRoleCodeSystem");
    }

    private static String extractUserRoleCodeSystem(AssertionType assertion) {
        String value = null;
        if ((assertion != null) && (assertion.getUserInfo() != null) && (assertion.getUserInfo().getRoleCoded() != null)) {
            value = assertion.getUserInfo().getRoleCoded().getCodeSystem();
        } else {
            log.debug("Unable to find user role in SAML, will not be included in message to policy engine");
        }

        return value;
    }

    private static void appendUserRoleCodeSystemName(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending UserRoleCodeSystemName");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.UserRoleCodeSystemName;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractUserRoleCodeSystemName(assertion);
        AttributeHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending UserRoleCodeSystemName");
    }

    private static String extractUserRoleCodeSystemName(AssertionType assertion) {
        String value = null;
        if ((assertion != null) && (assertion.getUserInfo() != null) && (assertion.getUserInfo().getRoleCoded() != null)) {
            value = assertion.getUserInfo().getRoleCoded().getCodeSystemName();
        } else {
            log.debug("Unable to find user role in SAML, will not be included in message to policy engine");
        }

        return value;
    }

    private static void appendUserRoleCodeDiplayName(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending UserRoleCodeDiplayName");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.UserRoleCodeDiplayName;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractUserRoleCodeDiplayName(assertion);
        AttributeHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending UserRoleCodeDiplayName");
    }

    private static String extractUserRoleCodeDiplayName(AssertionType assertion) {
        String value = null;
        if ((assertion != null) && (assertion.getUserInfo() != null) && (assertion.getUserInfo().getRoleCoded() != null)) {
            value = assertion.getUserInfo().getRoleCoded().getDisplayName();
        } else {
            log.debug("Unable to find user role in SAML, will not be included in message to policy engine");
        }

        return value;
    }

    private static void appendPurposeForUseCode(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending PurposeForUseCode");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.PurposeForUseCode;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractPurposeForUseCode(assertion);
        AttributeHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending PurposeForUseCode");
    }

    private static String extractPurposeForUseCode(AssertionType assertion) {
        String value = null;
        if ((assertion != null) && (assertion.getPurposeOfDisclosureCoded() != null)) {
            value = assertion.getPurposeOfDisclosureCoded().getCode();
        }

        return value;
    }

    private static void appendPurposeForUseCodeSystem(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending PurposeForUseCodeSystem");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.PurposeForUseCodeSystem;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractPurposeForUseCodeSystem(assertion);
        AttributeHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending PurposeForUseCodeSystem");
    }

    private static String extractPurposeForUseCodeSystem(AssertionType assertion) {
        String value = null;
        if ((assertion != null) && (assertion.getPurposeOfDisclosureCoded() != null)) {
            value = assertion.getPurposeOfDisclosureCoded().getCodeSystem();
        }

        return value;
    }

    private static void appendPurposeForUseCodeSystemName(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending PurposeForUseCodeSystemName");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.PurposeForUseCodeSystemName;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractPurposeForUseCodeSystemName(assertion);
        AttributeHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending PurposeForUseCodeSystemName");
    }

    private static String extractPurposeForUseCodeSystemName(AssertionType assertion) {
        String value = null;
        if ((assertion != null) && (assertion.getPurposeOfDisclosureCoded() != null)) {
            value = assertion.getPurposeOfDisclosureCoded().getCodeSystemName();
        }

        return value;
    }

    private static void appendPurposeForUseCodeDisplayName(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending PurposeForUseCodeDisplayName");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.PurposeForUseCodeDisplayName;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractPurposeForUseCodeDisplayName(assertion);
        AttributeHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending PurposeForUseCodeDisplayName");
    }

    private static String extractPurposeForUseCodeDisplayName(AssertionType assertion) {
        String value = null;
        if ((assertion != null) && (assertion.getPurposeOfDisclosureCoded() != null)) {
            value = assertion.getPurposeOfDisclosureCoded().getDisplayName();
        }

        return value;
    }

    private static void appendAuthzDecisionStatementDecision(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending AuthzDecisionStatementDecision");
        ResourceType parent = getResource(policyRequest);
        String attributeId = XacmlAttributeId.AuthzDecisionStatementDecision;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthzDecisionStatementDecision(assertion);
        AttributeHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending AuthzDecisionStatementDecision");
    }

    private static String extractAuthzDecisionStatementDecision(AssertionType assertion) {
        String value = null;
        if (assertion.getSamlAuthzDecisionStatement() != null) {
            log.debug("Extracting AuthzDecisionStatementDecision");
            value = assertion.getSamlAuthzDecisionStatement().getDecision();
            log.debug("Extracted AuthzDecisionStatementDecision value=" + value);
        } else {
            log.debug("Unable to find AuthzDecisionStatementDecision in SAML, will not be included in message to policy engine");
        }

        return value;
    }

    private static void appendAuthzDecisionStatementResource(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending AuthzDecisionStatementResource");
        ResourceType parent = getResource(policyRequest);
        String attributeId = XacmlAttributeId.AuthzDecisionStatementResource;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthzDecisionStatementResource(assertion);
        AttributeHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending AuthzDecisionStatementResource");
    }

    private static String extractAuthzDecisionStatementResource(AssertionType assertion) {
        String value = null;
        if (assertion.getSamlAuthzDecisionStatement() != null) {
            log.debug("Extracting AuthzDecisionStatementResource");
            value = assertion.getSamlAuthzDecisionStatement().getResource();
            log.debug("Extracted AuthzDecisionStatementResource value=" + value);
        } else {
            log.debug("Unable to find AuthzDecisionStatement in SAML, will not be included in message to policy engine");
        }

        return value;
    }

    private static void appendAuthzDecisionStatementAction(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending AuthzDecisionStatementAction");
        ResourceType parent = getResource(policyRequest);
        String attributeId = XacmlAttributeId.AuthzDecisionStatementAction;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthzDecisionStatementAction(assertion);
        AttributeHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending AuthzDecisionStatementAction");
    }

    private static String extractAuthzDecisionStatementAction(AssertionType assertion) {
        String value = null;
        if (assertion.getSamlAuthzDecisionStatement() != null) {
            log.debug("Extracting AuthzDecisionStatementAction");
            value = assertion.getSamlAuthzDecisionStatement().getAction();
            log.debug("Extracted AuthzDecisionStatementAction value=" + value);
        } else {
            log.debug("Unable to find AuthzDecisionStatement in SAML, will not be included in message to policy engine");
        }

        return value;
    }

    private static void appendAuthzDecisionStatementEvidenceAssertionID(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending AuthzDecisionStatementEvidenceAssertionID");
        ResourceType parent = getResource(policyRequest);
        String attributeId = XacmlAttributeId.AuthzDecisionStatementEvidenceAssertionID;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthzDecisionStatementEvidenceAssertionID(assertion);
        AttributeHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending AuthzDecisionStatementEvidenceAssertionID");
    }

    private static String extractAuthzDecisionStatementEvidenceAssertionID(AssertionType assertion) {
        String value = null;
        if ((assertion.getSamlAuthzDecisionStatement() != null) && (assertion.getSamlAuthzDecisionStatement().getEvidence() != null) && (assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion() != null)) {
            log.debug("Extracting AuthzDecisionStatementEvidenceAssertionID");
            value = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getId();
            log.debug("Extracted AuthzDecisionStatementEvidenceAssertionID value=" + value);
        } else {
            log.debug("Unable to find AuthzDecisionStatement Evidence Assertion in SAML, will not be included in message to policy engine");
        }

        return value;
    }

    private static void appendAuthzDecisionStatementEvidenceAssertionIssueInstant(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending AuthzDecisionStatementEvidenceAssertionIssueInstant");
        ResourceType parent = getResource(policyRequest);
        String attributeId = XacmlAttributeId.AuthzDecisionStatementEvidenceAssertionIssueInstant;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthzDecisionStatementEvidenceAssertionIssueInstant(assertion);
        AttributeHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending AuthzDecisionStatementEvidenceAssertionIssueInstant");
    }

    private static String extractAuthzDecisionStatementEvidenceAssertionIssueInstant(AssertionType assertion) {
        String value = null;
        if ((assertion.getSamlAuthzDecisionStatement() != null) && (assertion.getSamlAuthzDecisionStatement().getEvidence() != null) && (assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion() != null)) {
            log.debug("Extracting AuthzDecisionStatementEvidenceAssertionIssueInstant");
            value = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getIssueInstant();
            log.debug("Extracted AuthzDecisionStatementEvidenceAssertionIssueInstant value=" + value);
        } else {
            log.debug("Unable to find AuthzDecisionStatement Evidence Assertion in SAML, will not be included in message to policy engine");
        }

        return value;
    }

    private static void appendAuthzDecisionStatementEvidenceAssertionVersion(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending AuthzDecisionStatementEvidenceAssertionVersion");
        ResourceType parent = getResource(policyRequest);
        String attributeId = XacmlAttributeId.AuthzDecisionStatementEvidenceAssertionVersion;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthzDecisionStatementEvidenceAssertionVersion(assertion);
        AttributeHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending AuthzDecisionStatementEvidenceAssertionVersion");
    }

    private static String extractAuthzDecisionStatementEvidenceAssertionVersion(AssertionType assertion) {
        String value = null;
        if ((assertion.getSamlAuthzDecisionStatement() != null) && (assertion.getSamlAuthzDecisionStatement().getEvidence() != null) && (assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion() != null)) {
            log.debug("Extracting AuthzDecisionStatementEvidenceAssertionVersion");
            value = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getVersion();
            log.debug("Extracted AuthzDecisionStatementEvidenceAssertionVersion value=" + value);
        } else {
            log.debug("Unable to find AuthzDecisionStatement Evidence Assertion in SAML, will not be included in message to policy engine");
        }

        return value;

    }

    private static void appendAuthzDecisionStatementEvidenceAssertionIssuer(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending AuthzDecisionStatementEvidenceAssertionIssuer");
        ResourceType parent = getResource(policyRequest);
        String attributeId = XacmlAttributeId.AuthzDecisionStatementEvidenceAssertionIssuer;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthzDecisionStatementEvidenceAssertionIssuer(assertion);
        AttributeHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending AuthzDecisionStatementEvidenceAssertionIssuer");
    }

    private static String extractAuthzDecisionStatementEvidenceAssertionIssuer(AssertionType assertion) {
        String value = null;
        if ((assertion.getSamlAuthzDecisionStatement() != null) && (assertion.getSamlAuthzDecisionStatement().getEvidence() != null) && (assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion() != null)) {
            log.debug("Extracting AuthzDecisionStatementEvidenceAssertionIssuer");
            value = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getIssuer();
            log.debug("Extracted AuthzDecisionStatementEvidenceAssertionIssuer value=" + value);
        } else {
            log.debug("Unable to find AuthzDecisionStatement Evidence Assertion in SAML, will not be included in message to policy engine");
        }

        return value;
    }

    private static String extractAuthzDecisionStatementEvidenceAssertionConditionsNotBefore(AssertionType assertion) {
        String value = null;
        if ((assertion.getSamlAuthzDecisionStatement() != null) && (assertion.getSamlAuthzDecisionStatement().getEvidence() != null) && (assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion() != null) && (assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions() != null)) {
            log.debug("Extracting AuthzDecisionStatementEvidenceAssertionConditionsNotBefore");
            value = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().getNotBefore();
            log.debug("Extracted AuthzDecisionStatementEvidenceAssertionConditionsNotBefore value=" + value);
        } else {
            log.debug("Unable to find AuthzDecisionStatement Evidence Assertion Condition in SAML, will not be included in message to policy engine");
        }

        return value;
    }

    private static void appendAuthzDecisionStatementEvidenceAssertionConditionsNotBefore(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending AuthzDecisionStatementEvidenceAssertionConditionsNotBefore");
        ResourceType parent = getResource(policyRequest);
        String attributeId = XacmlAttributeId.AuthzDecisionStatementEvidenceAssertionConditionsNotBefore;
        String dataType = Constants.DataTypeDate;
        String attributeValue = extractAuthzDecisionStatementEvidenceAssertionConditionsNotBefore(assertion);
        AttributeHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending AuthzDecisionStatementEvidenceAssertionConditionsNotBefore");
    }

    private static String extractAuthzDecisionStatementEvidenceAssertionConditionsNotOnOrAfter(AssertionType assertion) {
        String value = null;
        if ((assertion.getSamlAuthzDecisionStatement() != null) && (assertion.getSamlAuthzDecisionStatement().getEvidence() != null) && (assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion() != null) && (assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions() != null)) {
            log.debug("Extracting AuthzDecisionStatementEvidenceAssertionConditionsNotOnOrAfter");
            value = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().getNotOnOrAfter();
            log.debug("Extracted AuthzDecisionStatementEvidenceAssertionConditionsNotOnOrAfter value=" + value);
        } else {
            log.debug("Unable to find AuthzDecisionStatement Evidence Assertion Condition in SAML, will not be included in message to policy engine");
        }

        return value;

    }

    private static void appendAuthzDecisionStatementEvidenceAssertionConditionsNotOnOrAfter(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending AuthzDecisionStatementEvidenceAssertionConditionsNotOnOrAfter");
        ResourceType parent = getResource(policyRequest);
        String attributeId = XacmlAttributeId.AuthzDecisionStatementEvidenceAssertionConditionsNotOnOrAfter;
        String dataType = Constants.DataTypeDate;
        String attributeValue = extractAuthzDecisionStatementEvidenceAssertionConditionsNotOnOrAfter(assertion);
        AttributeHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending AuthzDecisionStatementEvidenceAssertionConditionsNotOnOrAfter");
    }

    private static String extractAuthzDecisionStatementEvidenceAssertionContentReference(AssertionType assertion) {
        String value = null;
        if ((assertion.getSamlAuthzDecisionStatement() != null) && (assertion.getSamlAuthzDecisionStatement().getEvidence() != null) && (assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion() != null)) {
            log.debug("Extracting AuthzDecisionStatementEvidenceAssertionContentReference");
            value = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getContentReference();
            log.debug("Extracted AuthzDecisionStatementEvidenceAssertionContentReference value=" + value);
        } else {
            log.debug("Unable to find AuthzDecisionStatement Evidence Assertion in SAML, will not be included in message to policy engine");
        }

        return value;
    }

    private static void appendAuthzDecisionStatementEvidenceAssertionContentReference(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending AuthzDecisionStatementEvidenceAssertionContentReference");
        ResourceType parent = getResource(policyRequest);
        String attributeId = XacmlAttributeId.AuthzDecisionStatementEvidenceAssertionContentReference;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthzDecisionStatementEvidenceAssertionContentReference(assertion);
        AttributeHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending AuthzDecisionStatementEvidenceAssertionContentReference");
    }

    private static void appendAuthzDecisionStatementEvidenceAssertionContentType(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending AuthzDecisionStatementEvidenceAssertionContentType");
        ResourceType parent = getResource(policyRequest);
        String attributeId = XacmlAttributeId.AuthzDecisionStatementEvidenceAssertionContentType;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthzDecisionStatementEvidenceAssertionContentType(assertion);
        AttributeHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending AuthzDecisionStatementEvidenceAssertionContentType");
    }

    private static String extractAuthzDecisionStatementEvidenceAssertionContentType(AssertionType assertion) {
        String value = null;
        if ((assertion.getSamlAuthzDecisionStatement() != null) && (assertion.getSamlAuthzDecisionStatement().getEvidence() != null) && (assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion() != null)) {
            log.debug("Extracting AuthzDecisionStatementEvidenceAssertionContentType");
            value = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getContentType();
            log.debug("Extracted AuthzDecisionStatementEvidenceAssertionContentType value=" + value);
        } else {
            log.debug("Unable to find AuthzDecisionStatement Evidence Assertion in SAML, will not be included in message to policy engine");
        }

        return value;

    }

    private static byte[] extractAuthzDecisionStatementEvidenceAssertionContent(AssertionType assertion) {
        byte[] value = null;
        if ((assertion.getSamlAuthzDecisionStatement() != null) && (assertion.getSamlAuthzDecisionStatement().getEvidence() != null) && (assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion() != null)) {
            log.debug("Extracting AuthzDecisionStatementEvidenceAssertionContent");
            value = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getContent();
            log.debug("Extracted AuthzDecisionStatementEvidenceAssertionContent value=" + value);
        } else {
            log.debug("Unable to find AuthzDecisionStatement Evidence Assertion in SAML, will not be included in message to policy engine");
        }

        return value;
    }

    private static void appendAuthzDecisionStatementEvidenceAssertionContent(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending AuthzDecisionStatementEvidenceAssertionContent");
        ResourceType parent = getResource(policyRequest);
        String attributeId = XacmlAttributeId.AuthzDecisionStatementEvidenceAssertionContent;
        String dataType = Constants.DataTypeBinary;

        byte[] attributeValueAsByteArray = extractAuthzDecisionStatementEvidenceAssertionContent(assertion);

        AttributeHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValueAsByteArray, appendAttributesIfNull);

        //String attributeValue  = new String(attributeValueAsByteArray) ;
        //AttributeHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);

        log.debug("end appending AuthzDecisionStatementEvidenceAssertionContent");
    }

    private static void appendSignatureKeyModulus(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending SignatureKeyModulus");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.SignatureKeyModulus;
        String dataType = Constants.DataTypeBinary;
        String attributeValue = extractSignatureKeyModulus(assertion);
        AttributeHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending SignatureKeyModulus");
    }

    private static String extractSignatureKeyModulus(AssertionType assertion) {
        String value = null;
        if ((assertion != null)) {
            value = null; //Signature/KeyInfo/KeyValue/RSAKeyValue/Modulus
        }

        return value;
    }

    private static void appendSignatureKeyExponent(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending SignatureKeyExponent");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.SignatureKeyExponent;
        String dataType = Constants.DataTypeBinary;
        String attributeValue = extractSignatureKeyExponent(assertion);
        AttributeHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending SignatureKeyExponent");
    }

    private static String extractSignatureKeyExponent(AssertionType assertion) {
        String value = null;
        if ((assertion != null)) {
            value = null; //Signature/KeyInfo/KeyValue/RSAKeyValue/Exponent
        }

        return value;
    }

    private static void appendSignatureValue(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending SignatureValue");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.SignatureValue;
        String dataType = Constants.DataTypeBinary;
        String attributeValue = extractSignatureValue(assertion);
        AttributeHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending SignatureValue");
    }

    private static String extractSignatureValue(AssertionType assertion) {
        String value = null;
        if ((assertion != null)) {
            value = null; //Signature/SignatureValue
        }

        return value;
    }
}