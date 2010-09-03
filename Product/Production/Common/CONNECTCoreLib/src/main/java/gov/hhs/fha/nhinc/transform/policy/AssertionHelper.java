/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.transform.policy;

//import com.sun.xml.internal.bind.v2.schemagen.xmlschema.AttributeType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.transform.subdisc.HL7DataTransformHelper;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import oasis.names.tc.xacml._2_0.context.schema.os.ActionType;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResourceType;
import oasis.names.tc.xacml._2_0.context.schema.os.AttributeType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.II;

/**
 *
 * @author rayj
 */
public class AssertionHelper {

    private Log log = null;
    private static final boolean appendAttributesIfNull = false;

    public AssertionHelper() {
        log = createLogger();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    public void appendAssertionDataToRequest(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending assertion data to xacml request");

//        if (log.isDebugEnabled()) {
//            CheckPolicyRequestMarshaller marshaller = new CheckPolicyRequestMarshaller();
//            String message = XmlUtility.serializeElementIgnoreFaults(marshaller.marshalCheckPolicyRequest(policyRequest));
//            log.debug("begin transformAdhocQueryToCheckPolicy [" + message + "]");
//        }


        //PurposeForUseHelper.appendPurposeForUse(policyRequest, assertion);

        if (assertion != null) {
            appendAuthnStatementAuthnInstant(policyRequest, assertion);
            appendAuthnStatementSessionIndex(policyRequest, assertion);
            appendAuthnStatementAthnContextClassRef(policyRequest, assertion);
            appendAuthnStatementSubjectLocalityAddress(policyRequest, assertion);
            appendAuthnStatementDNSName(policyRequest, assertion);
            appendUserPersonName(policyRequest, assertion);
            appendUserOrganizationName(policyRequest, assertion);
            appendUserOrganizationId(policyRequest, assertion);
            appendHomeCommunityName(policyRequest, assertion);
            appendUniquePatientId(policyRequest, assertion);
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
            appendAuthzDecisionStatementEvidenceAssertionAccessConsentPolicy(policyRequest, assertion);
            appendAuthzDecisionStatementEvidenceAssertionInstanceAccessConsentPolicy(policyRequest, assertion);
        } else {
            log.warn("assertion was not set - unable to extract assertion related data to send to policy engine");
        }

        log.debug("end appending assertion data to xacml request");
    }

    private ActionType getAction(RequestType policyRequest) {
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

    private SubjectType getSubject(RequestType policyXacmlRequest) {

        if (policyXacmlRequest == null) {
            throw new NullPointerException("policy request request is null");
        }

        SubjectType subject = null;
        if (policyXacmlRequest.getSubject() != null && !policyXacmlRequest.getSubject().isEmpty()) {
            subject = policyXacmlRequest.getSubject().get(0);
        } else {
            subject = new SubjectType();
            policyXacmlRequest.getSubject().add(subject);
        }

        return subject;
    }

    private ResourceType getResource(RequestType policyRequest) {
        if (policyRequest == null) {
            throw new NullPointerException("policy request is null");
        }

        ResourceType resource = null;
        if (policyRequest.getResource() != null && !policyRequest.getResource().isEmpty()) {
            resource = policyRequest.getResource().get(0);
        } else {
            resource = new ResourceType();
            policyRequest.getResource().add(resource);
        }

        return resource;
    }

    private boolean doesAtributeExist(ActionType action, String attributeName) {
        boolean found = false;
        for (AttributeType attribute : action.getAttribute()) {
            if (attributeName.contentEquals(attribute.getAttributeId())) {
                found = true;
                break;

            }
        }
        return found;
    }

    public String extractUserName(
            AssertionType assertion) {
        String username = null;
        if ((assertion != null) && (assertion.getUserInfo() != null) && (assertion.getUserInfo().getUserName() != null)) {
            username = assertion.getUserInfo().getUserName();
        }

        return username;
    }

    public String extractUserHomeCommunity(
            AssertionType assertion) {
        String username = null;
        if ((assertion != null) && (assertion.getUserInfo() != null) && (assertion.getUserInfo().getOrg() != null) && (assertion.getUserInfo().getOrg().getHomeCommunityId() != null)) {
            username = assertion.getUserInfo().getOrg().getHomeCommunityId();
        }

        return username;
    }

    public String extractPurpose(
            AssertionType assertion) {
        String purpose = null;
        if ((assertion != null) && (assertion.getPurposeOfDisclosureCoded() != null) && (assertion.getPurposeOfDisclosureCoded().getCode() != null)) {
            purpose = assertion.getPurposeOfDisclosureCoded().getCode();
        }

        return purpose;
    }

    public String extractUserRole(
            AssertionType assertion) {
        String userRole = null;
        if ((assertion != null) && (assertion.getUserInfo() != null) && (assertion.getUserInfo().getRoleCoded() != null) && (assertion.getUserInfo().getRoleCoded().getCode() != null)) {
            userRole = assertion.getUserInfo().getRoleCoded().getCode();
        }

        return userRole;
    }

    private void appendAuthnStatementAuthnInstant(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending AuthnStatementAuthnInstant");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.AuthnStatementAuthnInstant;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthnStatementAuthnInstant(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending AuthnStatementAuthnInstant");
    }

    private String extractAuthnStatementAuthnInstant(AssertionType assertion) {
        String value = null;
        if (assertion.getSamlAuthnStatement() != null) {
            log.debug("Extracting AuthInstant");
            value = assertion.getSamlAuthnStatement().getAuthInstant();
            log.debug("Extracted AuthInstant value=" + value);
        } else {
            log.warn("Unable to find SamlAuthnStatement in SAML, will not be included in message to policy engine");
        }

        return value;
    }

    private void appendAuthnStatementSessionIndex(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending AuthnStatementSessionIndex");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.AuthnStatementSessionIndex;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthnStatementSessionIndex(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending AuthnStatementSessionIndex");
    }

    private String extractAuthnStatementSessionIndex(AssertionType assertion) {
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

    private void appendAuthnStatementAthnContextClassRef(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending AuthnStatementAthnContextClassRef");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.AuthnStatementAthnContextClassRef;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthnStatementAthnContextClassRef(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending AuthnStatementAthnContextClassRef");
    }

    private String extractAuthnStatementAthnContextClassRef(AssertionType assertion) {
        String value = null;
        if (assertion.getSamlAuthnStatement() != null) {
            log.debug("Extracting AuthnStatementAthnContextClassRef");
            value = assertion.getSamlAuthnStatement().getAuthContextClassRef();
            log.debug("Extracted AuthnStatementAthnContextClassRef value=" + value);
        } else {
            log.warn("Unable to find SamlAuthnStatement in SAML, will not be included in message to policy engine");
        }
        return value;
    }

    private void appendAuthnStatementSubjectLocalityAddress(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending AuthnStatementSubjectLocalityAddress");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.AuthnStatementSubjectLocalityAddress;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthnStatementSubjectLocalityAddress(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending AuthnStatementSubjectLocalityAddress");
    }

    private String extractAuthnStatementSubjectLocalityAddress(AssertionType assertion) {
        String value = null;
        if (assertion.getSamlAuthnStatement() != null) {
            log.debug("Extracting AuthnStatementSubjectLocalityAddress");
            value = assertion.getSamlAuthnStatement().getSubjectLocalityAddress();
            log.debug("Extracted AuthnStatementSubjectLocalityAddress value=" + value);
        } else {
            log.warn("Unable to find SamlAuthnStatement in SAML, will not be included in message to policy engine");
        }
        return value;
    }

    private void appendAuthnStatementDNSName(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending AuthnStatementDNSName");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.AuthnStatementDNSName;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthnStatementDNSName(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending AuthnStatementDNSName");
    }

    private String extractAuthnStatementDNSName(AssertionType assertion) {
        String value = null;
        if (assertion.getSamlAuthnStatement() != null) {
            log.debug("Extracting SubjectLocalityDNSName");
            value = assertion.getSamlAuthnStatement().getSubjectLocalityDNSName();
            log.debug("Extracted SubjectLocalityDNSName value=" + value);
        } else {
            log.warn("Unable to find SamlAuthnStatement in SAML, will not be included in message to policy engine");
        }
        return value;
    }

    private void appendUserPersonName(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending UserPersonName");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.UserPersonName;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractUserPersonName(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending UserPersonName");
    }

    private String extractUserPersonName(AssertionType assertion) {
        String value = null;
        if (assertion.getUserInfo() != null) {
            log.debug("Extracting UserPersonName");
            value = assertion.getUserInfo().getUserName();
            log.debug("Extracted UserPersonName value=" + value);
        } else {
            log.warn("Unable to find UserInfo in SAML, will not be included in message to policy engine");
        }
        return value;
    }

    private void appendUserOrganizationName(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending UserOrganizationName");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.UserOrganizationName;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractUserOrganizationName(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending UserOrganizationName");
    }

    private String extractUserOrganizationName(AssertionType assertion) {
        String value = null;
        if ((assertion.getUserInfo() != null) && (assertion.getUserInfo().getOrg() != null)) {
            log.debug("Extracting UserOrganizationName");
            value = assertion.getUserInfo().getOrg().getName();
            log.debug("Extracted UserOrganizationName value=" + value);
        } else {
            log.warn("Unable to find User Organization in SAML, will not be included in message to policy engine");
        }
        return value;
    }

    private void appendUserOrganizationId(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending UserOrganizationId");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.UserOrganizationId;
        String dataType = Constants.DataTypeAnyURI;
        String attributeValue = extractUserOrganizationId(assertion);
        URI orgIdURI = null;
        if (attributeValue != null) {
            try {
                orgIdURI = new URI(attributeValue);
            } catch (URISyntaxException ex) {
                log.warn("User Organization in SAML is not a valid URI, it will not be included in message to policy engine");
            }
        } else {
            log.warn("User Organization in SAML is not a valid URI, it will not be included in message to policy engine");
        }
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, orgIdURI, appendAttributesIfNull);
        log.debug("end appending UserOrganizationId");
    }

    private String extractUserOrganizationId(AssertionType assertion) {
        String value = null;
        if ((assertion.getUserInfo() != null) && (assertion.getUserInfo().getOrg() != null)) {
            log.debug("Extracting UserOrganizationId");
            value = assertion.getUserInfo().getOrg().getHomeCommunityId();
            log.debug("Extracted UserOrganizationId value=" + value);
        } else {
            log.warn("Unable to find User Organization in SAML, it will not be included in message to policy engine");
        }
        return value;
    }

    private void appendHomeCommunityName(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending HomeCommunityName");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.HomeCommunityName;
        String dataType = Constants.DataTypeAnyURI;
        String attributeValue = extractHomeCommunityName(assertion);
        URI orgIdURI = null;
        if (attributeValue != null) {
            try {
                orgIdURI = new URI(attributeValue);
            } catch (URISyntaxException ex) {
                log.warn("Home Community in SAML is not a valid URI, it will not be included in message to policy engine");
            }
        } else {
            log.warn("User Organization in SAML is not a valid URI, it will not be included in message to policy engine");
        }
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, orgIdURI, appendAttributesIfNull);
        log.debug("end appending HomeCommunityName");
    }

    private String extractHomeCommunityName(AssertionType assertion) {
        String value = null;
        if ((assertion.getHomeCommunity() != null)) {
            log.debug("Extracting HomeCommunityName");
            value = assertion.getHomeCommunity().getHomeCommunityId();
            log.debug("Extracted HomeCommunityName value=" + value);
        } else {
            log.warn("Unable to find Home Community in SAML, will not be included in message to policy engine");
        }
        return value;
    }

    private void appendUniquePatientId(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending UniquePatientId");
        ResourceType parent = getResource(policyRequest);
        String attributeId = XacmlAttributeId.UniquePatientId;
        String dataType = Constants.DataTypeHL7II;
        II attributeValue = extractUniquePatientId(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending UniquePatientId");
    }

    private II extractUniquePatientId(AssertionType assertion) {
        II iiValue = null;
        String patId = null;
        if ((assertion.getUniquePatientId() != null && assertion.getUniquePatientId().size() > 0)) {
            log.debug("Extracting UniquePatientId");
            // Take first identifier found
            for (String id : assertion.getUniquePatientId()) {
                if (id != null && !id.isEmpty()) {
                    patId = id;
                    break;
                }
            }
            if (patId != null) {
                String patExt = PatientIdFormatUtil.parsePatientId(patId);
                String patRoot = PatientIdFormatUtil.parseCommunityId(patId);
                log.debug("UniquePatientId value=" + patId + " => root:" + patRoot + " extension:" + patExt);
                if (patRoot != null && patExt != null) {
                    iiValue = HL7DataTransformHelper.IIFactory(patRoot, patExt);
                } else {
                    log.warn("Unique patient should be in ISO format including a root and extension");
                    log.warn("Found root: " + patRoot + " and extension: " + patExt + " It will not be included in message to policy engine");
                }
            } else {
                log.warn("Unable to find Unique Patient in SAML, will not be included in message to policy engine");
            }
        } else {
            log.warn("Unable to find Unique Patient in SAML, will not be included in message to policy engine");
        }
        return iiValue;
    }

    private void appendUserRoleCode(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending UserRoleCode");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.UserRoleCode;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractUserRoleCode(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending UserRoleCode");
    }

    private String extractUserRoleCode(AssertionType assertion) {
        String value = null;
        if ((assertion != null) && (assertion.getUserInfo() != null) && (assertion.getUserInfo().getRoleCoded() != null)) {
            value = assertion.getUserInfo().getRoleCoded().getCode();
        } else {
            log.warn("Unable to find user role in SAML, will not be included in message to policy engine");
        }

        return value;
    }

    private void appendUserRoleCodeSystem(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending UserRoleCodeSystem");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.UserRoleCodeSystem;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractUserRoleCodeSystem(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending UserRoleCodeSystem");
    }

    private String extractUserRoleCodeSystem(AssertionType assertion) {
        String value = null;
        if ((assertion != null) && (assertion.getUserInfo() != null) && (assertion.getUserInfo().getRoleCoded() != null)) {
            value = assertion.getUserInfo().getRoleCoded().getCodeSystem();
        } else {
            log.warn("Unable to find user role in SAML, will not be included in message to policy engine");
        }

        return value;
    }

    private void appendUserRoleCodeSystemName(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending UserRoleCodeSystemName");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.UserRoleCodeSystemName;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractUserRoleCodeSystemName(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending UserRoleCodeSystemName");
    }

    private String extractUserRoleCodeSystemName(AssertionType assertion) {
        String value = null;
        if ((assertion != null) && (assertion.getUserInfo() != null) && (assertion.getUserInfo().getRoleCoded() != null)) {
            value = assertion.getUserInfo().getRoleCoded().getCodeSystemName();
        } else {
            log.warn("Unable to find user role in SAML, will not be included in message to policy engine");
        }

        return value;
    }

    private void appendUserRoleCodeDiplayName(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending UserRoleCodeDiplayName");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.UserRoleCodeDiplayName;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractUserRoleCodeDiplayName(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending UserRoleCodeDiplayName");
    }

    private String extractUserRoleCodeDiplayName(AssertionType assertion) {
        String value = null;
        if ((assertion != null) && (assertion.getUserInfo() != null) && (assertion.getUserInfo().getRoleCoded() != null)) {
            value = assertion.getUserInfo().getRoleCoded().getDisplayName();
        } else {
            log.warn("Unable to find user role in SAML, will not be included in message to policy engine");
        }

        return value;
    }

    private void appendPurposeForUseCode(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending PurposeForUseCode");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.PurposeForUseCode;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractPurposeForUseCode(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending PurposeForUseCode");
    }

    private String extractPurposeForUseCode(AssertionType assertion) {
        String value = null;
        if ((assertion != null) && (assertion.getPurposeOfDisclosureCoded() != null)) {
            value = assertion.getPurposeOfDisclosureCoded().getCode();
        }

        return value;
    }

    private void appendPurposeForUseCodeSystem(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending PurposeForUseCodeSystem");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.PurposeForUseCodeSystem;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractPurposeForUseCodeSystem(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending PurposeForUseCodeSystem");
    }

    private String extractPurposeForUseCodeSystem(AssertionType assertion) {
        String value = null;
        if ((assertion != null) && (assertion.getPurposeOfDisclosureCoded() != null)) {
            value = assertion.getPurposeOfDisclosureCoded().getCodeSystem();
        }

        return value;
    }

    private void appendPurposeForUseCodeSystemName(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending PurposeForUseCodeSystemName");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.PurposeForUseCodeSystemName;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractPurposeForUseCodeSystemName(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending PurposeForUseCodeSystemName");
    }

    private String extractPurposeForUseCodeSystemName(AssertionType assertion) {
        String value = null;
        if ((assertion != null) && (assertion.getPurposeOfDisclosureCoded() != null)) {
            value = assertion.getPurposeOfDisclosureCoded().getCodeSystemName();
        }

        return value;
    }

    private void appendPurposeForUseCodeDisplayName(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending PurposeForUseCodeDisplayName");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.PurposeForUseCodeDisplayName;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractPurposeForUseCodeDisplayName(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending PurposeForUseCodeDisplayName");
    }

    private String extractPurposeForUseCodeDisplayName(AssertionType assertion) {
        String value = null;
        if ((assertion != null) && (assertion.getPurposeOfDisclosureCoded() != null)) {
            value = assertion.getPurposeOfDisclosureCoded().getDisplayName();
        }

        return value;
    }

    private void appendAuthzDecisionStatementDecision(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending AuthzDecisionStatementDecision");
        ResourceType parent = getResource(policyRequest);
        String attributeId = XacmlAttributeId.AuthzDecisionStatementDecision;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthzDecisionStatementDecision(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending AuthzDecisionStatementDecision");
    }

    private String extractAuthzDecisionStatementDecision(AssertionType assertion) {
        String value = null;
        if (assertion.getSamlAuthzDecisionStatement() != null) {
            log.debug("Extracting AuthzDecisionStatementDecision");
            value = assertion.getSamlAuthzDecisionStatement().getDecision();
            log.debug("Extracted AuthzDecisionStatementDecision value=" + value);
        } else {
            log.warn("Unable to find AuthzDecisionStatementDecision in SAML, will not be included in message to policy engine");
        }

        return value;
    }

    private void appendAuthzDecisionStatementResource(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending AuthzDecisionStatementResource");
        ResourceType parent = getResource(policyRequest);
        String attributeId = XacmlAttributeId.AuthzDecisionStatementResource;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthzDecisionStatementResource(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending AuthzDecisionStatementResource");
    }

    private String extractAuthzDecisionStatementResource(AssertionType assertion) {
        String value = null;
        if (assertion.getSamlAuthzDecisionStatement() != null) {
            log.debug("Extracting AuthzDecisionStatementResource");
            value = assertion.getSamlAuthzDecisionStatement().getResource();
            log.debug("Extracted AuthzDecisionStatementResource value=" + value);
        } else {
            log.warn("Unable to find AuthzDecisionStatement in SAML, will not be included in message to policy engine");
        }

        return value;
    }

    private void appendAuthzDecisionStatementAction(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending AuthzDecisionStatementAction");
        ResourceType parent = getResource(policyRequest);
        String attributeId = XacmlAttributeId.AuthzDecisionStatementAction;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthzDecisionStatementAction(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending AuthzDecisionStatementAction");
    }

    private String extractAuthzDecisionStatementAction(AssertionType assertion) {
        String value = null;
        if (assertion.getSamlAuthzDecisionStatement() != null) {
            log.debug("Extracting AuthzDecisionStatementAction");
            value = assertion.getSamlAuthzDecisionStatement().getAction();
            log.debug("Extracted AuthzDecisionStatementAction value=" + value);
        } else {
            log.warn("Unable to find AuthzDecisionStatement in SAML, will not be included in message to policy engine");
        }

        return value;
    }

    private void appendAuthzDecisionStatementEvidenceAssertionID(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending AuthzDecisionStatementEvidenceAssertionID");
        ResourceType parent = getResource(policyRequest);
        String attributeId = XacmlAttributeId.AuthzDecisionStatementEvidenceAssertionID;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthzDecisionStatementEvidenceAssertionID(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending AuthzDecisionStatementEvidenceAssertionID");
    }

    private String extractAuthzDecisionStatementEvidenceAssertionID(AssertionType assertion) {
        String value = null;
        if ((assertion.getSamlAuthzDecisionStatement() != null) && (assertion.getSamlAuthzDecisionStatement().getEvidence() != null) && (assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion() != null)) {
            log.debug("Extracting AuthzDecisionStatementEvidenceAssertionID");
            value = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getId();
            log.debug("Extracted AuthzDecisionStatementEvidenceAssertionID value=" + value);
        } else {
            log.warn("Unable to find AuthzDecisionStatement Evidence Assertion in SAML, will not be included in message to policy engine");
        }

        return value;
    }

    private void appendAuthzDecisionStatementEvidenceAssertionIssueInstant(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending AuthzDecisionStatementEvidenceAssertionIssueInstant");
        ResourceType parent = getResource(policyRequest);
        String attributeId = XacmlAttributeId.AuthzDecisionStatementEvidenceAssertionIssueInstant;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthzDecisionStatementEvidenceAssertionIssueInstant(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending AuthzDecisionStatementEvidenceAssertionIssueInstant");
    }

    private String extractAuthzDecisionStatementEvidenceAssertionIssueInstant(AssertionType assertion) {
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

    private void appendAuthzDecisionStatementEvidenceAssertionVersion(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending AuthzDecisionStatementEvidenceAssertionVersion");
        ResourceType parent = getResource(policyRequest);
        String attributeId = XacmlAttributeId.AuthzDecisionStatementEvidenceAssertionVersion;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthzDecisionStatementEvidenceAssertionVersion(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending AuthzDecisionStatementEvidenceAssertionVersion");
    }

    private String extractAuthzDecisionStatementEvidenceAssertionVersion(AssertionType assertion) {
        String value = null;
        if ((assertion.getSamlAuthzDecisionStatement() != null) && (assertion.getSamlAuthzDecisionStatement().getEvidence() != null) && (assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion() != null)) {
            log.debug("Extracting AuthzDecisionStatementEvidenceAssertionVersion");
            value = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getVersion();
            log.debug("Extracted AuthzDecisionStatementEvidenceAssertionVersion value=" + value);
        } else {
            log.warn("Unable to find AuthzDecisionStatement Evidence Assertion in SAML, will not be included in message to policy engine");
        }

        return value;

    }

    private void appendAuthzDecisionStatementEvidenceAssertionIssuer(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending AuthzDecisionStatementEvidenceAssertionIssuer");
        ResourceType parent = getResource(policyRequest);
        String attributeId = XacmlAttributeId.AuthzDecisionStatementEvidenceAssertionIssuer;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthzDecisionStatementEvidenceAssertionIssuer(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending AuthzDecisionStatementEvidenceAssertionIssuer");
    }

    private String extractAuthzDecisionStatementEvidenceAssertionIssuer(AssertionType assertion) {
        String value = null;
        if ((assertion.getSamlAuthzDecisionStatement() != null) && (assertion.getSamlAuthzDecisionStatement().getEvidence() != null) && (assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion() != null)) {
            log.debug("Extracting AuthzDecisionStatementEvidenceAssertionIssuer");
            value = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getIssuer();
            log.debug("Extracted AuthzDecisionStatementEvidenceAssertionIssuer value=" + value);
        } else {
            log.warn("Unable to find AuthzDecisionStatement Evidence Assertion in SAML, will not be included in message to policy engine");
        }

        return value;
    }

    private String extractAuthzDecisionStatementEvidenceAssertionConditionsNotBefore(AssertionType assertion) {
        String value = null;
        if ((assertion.getSamlAuthzDecisionStatement() != null) && (assertion.getSamlAuthzDecisionStatement().getEvidence() != null) && (assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion() != null) && (assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions() != null)) {
            log.debug("Extracting AuthzDecisionStatementEvidenceAssertionConditionsNotBefore");
            value = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().getNotBefore();
            log.debug("Extracted AuthzDecisionStatementEvidenceAssertionConditionsNotBefore value=" + value);
        } else {
            log.warn("Unable to find AuthzDecisionStatement Evidence Assertion Condition in SAML, will not be included in message to policy engine");
        }

        return value;
    }

    private void appendAuthzDecisionStatementEvidenceAssertionConditionsNotBefore(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending AuthzDecisionStatementEvidenceAssertionConditionsNotBefore");
        ResourceType parent = getResource(policyRequest);
        String attributeId = XacmlAttributeId.AuthzDecisionStatementEvidenceAssertionConditionsNotBefore;
        String dataType = Constants.DataTypeDate;
        String attributeValue = extractAuthzDecisionStatementEvidenceAssertionConditionsNotBefore(assertion);
        XMLGregorianCalendar calValue = createCal(attributeValue);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, calValue, appendAttributesIfNull);
        log.debug("end appending AuthzDecisionStatementEvidenceAssertionConditionsNotBefore");
    }

    private String extractAuthzDecisionStatementEvidenceAssertionConditionsNotOnOrAfter(AssertionType assertion) {
        String value = null;
        if ((assertion.getSamlAuthzDecisionStatement() != null) && (assertion.getSamlAuthzDecisionStatement().getEvidence() != null) && (assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion() != null) && (assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions() != null)) {
            log.debug("Extracting AuthzDecisionStatementEvidenceAssertionConditionsNotOnOrAfter");
            value = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().getNotOnOrAfter();
            log.debug("Extracted AuthzDecisionStatementEvidenceAssertionConditionsNotOnOrAfter value=" + value);
        } else {
            log.warn("Unable to find AuthzDecisionStatement Evidence Assertion Condition in SAML, will not be included in message to policy engine");
        }

        return value;

    }

    private void appendAuthzDecisionStatementEvidenceAssertionConditionsNotOnOrAfter(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending AuthzDecisionStatementEvidenceAssertionConditionsNotOnOrAfter");
        ResourceType parent = getResource(policyRequest);
        String attributeId = XacmlAttributeId.AuthzDecisionStatementEvidenceAssertionConditionsNotOnOrAfter;
        String dataType = Constants.DataTypeDate;
        String attributeValue = extractAuthzDecisionStatementEvidenceAssertionConditionsNotOnOrAfter(assertion);
        XMLGregorianCalendar calValue = createCal(attributeValue);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, calValue, appendAttributesIfNull);
        log.debug("end appending AuthzDecisionStatementEvidenceAssertionConditionsNotOnOrAfter");
    }

    private XMLGregorianCalendar createCal(String time) {

        XMLGregorianCalendar xmlDate = null;
        if (time != null) {
            try {
                //times must be in UTC format as specified by the XML Schema type (dateTime)
                DatatypeFactory xmlDateFactory = DatatypeFactory.newInstance();
                xmlDate = xmlDateFactory.newXMLGregorianCalendar(time.trim());
            } catch (IllegalArgumentException iaex) {
                try {
                    // try simple date format - backward compatibility
                    SimpleDateFormat dateForm = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    GregorianCalendar cal = new GregorianCalendar();
                    cal.setTimeZone(TimeZone.getTimeZone("GMT"));
                    cal.setTime(dateForm.parse(time));
                    DatatypeFactory xmlDateFactory = DatatypeFactory.newInstance();
                    xmlDate = xmlDateFactory.newXMLGregorianCalendar(cal);
                } catch (DatatypeConfigurationException ex) {
                    log.error("Date form is expected to be in dateTime format");
                } catch (ParseException ex) {
                    log.error("Date form is expected to be in dateTime format");
                }
            } catch (DatatypeConfigurationException dtex) {
                log.error("Problem in creating XML Date Factory. Setting default date");
            }
        }
        return xmlDate;
    }

    private String extractAuthzDecisionStatementEvidenceAssertionAccessConsentPolicy(AssertionType assertion) {
        String value = null;
        if ((assertion.getSamlAuthzDecisionStatement() != null) && (assertion.getSamlAuthzDecisionStatement().getEvidence() != null) && (assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion() != null)) {
            log.debug("Extracting AuthzDecisionStatementEvidenceAssertionAccessConsentPolicy");
            value = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getAccessConsentPolicy();
            log.debug("Extracted AuthzDecisionStatementEvidenceAssertionAccessConsentPolicy value=" + value);
        } else {
            log.warn("Unable to find AuthzDecisionStatement Evidence Assertion in SAML, will not be included in message to policy engine");
        }

        return value;
    }

    private void appendAuthzDecisionStatementEvidenceAssertionAccessConsentPolicy(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending AuthzDecisionStatementEvidenceAssertionAccessConsentPolicy");
        ResourceType parent = getResource(policyRequest);
        String attributeId = XacmlAttributeId.AuthzDecisionStatementEvidenceAssertionAccessConsentPolicy;
        String dataType = Constants.DataTypeAnyURI;
        String attributeValue = extractAuthzDecisionStatementEvidenceAssertionAccessConsentPolicy(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending AuthzDecisionStatementEvidenceAssertionAccessConsentPolicy");
    }

    private String extractAuthzDecisionStatementEvidenceAssertionInstanceAccessConsentPolicy(AssertionType assertion) {
        String value = null;
        if ((assertion.getSamlAuthzDecisionStatement() != null) && (assertion.getSamlAuthzDecisionStatement().getEvidence() != null) && (assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion() != null)) {
            log.debug("Extracting AuthzDecisionStatementEvidenceAssertionInstanceAccessConsentPolicy");
            value = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getInstanceAccessConsentPolicy();
            log.debug("Extracted AuthzDecisionStatementEvidenceAssertionInstanceAccessConsentPolicy value=" + value);
        } else {
            log.warn("Unable to find AuthzDecisionStatement Evidence Assertion in SAML, will not be included in message to policy engine");
        }

        return value;
    }

    private void appendAuthzDecisionStatementEvidenceAssertionInstanceAccessConsentPolicy(RequestType policyRequest, AssertionType assertion) {
        log.debug("begin appending AuthzDecisionStatementEvidenceAssertionInstanceAccessConsentPolicy");
        ResourceType parent = getResource(policyRequest);
        String attributeId = XacmlAttributeId.AuthzDecisionStatementEvidenceAssertionInstanceAccessConsentPolicy;
        String dataType = Constants.DataTypeAnyURI;
        String attributeValue = extractAuthzDecisionStatementEvidenceAssertionInstanceAccessConsentPolicy(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        log.debug("end appending AuthzDecisionStatementEvidenceAssertionInstanceAccessConsentPolicy");
    }
}
