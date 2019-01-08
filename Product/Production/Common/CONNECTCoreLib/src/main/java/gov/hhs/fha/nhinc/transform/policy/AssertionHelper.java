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
package gov.hhs.fha.nhinc.transform.policy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.subdisc.HL7DataTransformHelper;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResourceType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hl7.v3.II;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author rayj
 */
public class AssertionHelper {

    private static final Logger LOG = LoggerFactory.getLogger(AssertionHelper.class);
    private static final boolean appendAttributesIfNull = false;

    public void appendAssertionDataToRequest(RequestType policyRequest, AssertionType assertion) {
        LOG.debug("begin appending assertion data to xacml request");

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
            appendPurposeOfUseCode(policyRequest, assertion);
            appendPurposeOfUseCodeSystem(policyRequest, assertion);
            appendPurposeOfUseCodeSystemName(policyRequest, assertion);
            appendPurposeOfUseCodeDisplayName(policyRequest, assertion);
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
            appendAttributeAccessConsent(policyRequest, assertion);
        } else {
            LOG.warn("assertion was not set - unable to extract assertion related data to send to policy engine");
        }

        LOG.debug("end appending assertion data to xacml request");
    }

    private SubjectType getSubject(RequestType policyXacmlRequest) {

        if (policyXacmlRequest == null) {
            throw new NullPointerException("policy request request is null");
        }

        SubjectType subject;
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

        ResourceType resource;
        if (policyRequest.getResource() != null && !policyRequest.getResource().isEmpty()) {
            resource = policyRequest.getResource().get(0);
        } else {
            resource = new ResourceType();
            policyRequest.getResource().add(resource);
        }

        return resource;
    }

    public String extractUserName(AssertionType assertion) {
        String username = null;
        if (assertion != null && assertion.getUserInfo() != null && assertion.getUserInfo().getUserName() != null) {
            username = assertion.getUserInfo().getUserName();
        }

        return username;
    }

    public String extractUserHomeCommunity(AssertionType assertion) {
        String username = null;
        if (assertion != null && assertion.getUserInfo() != null && assertion.getUserInfo().getOrg() != null
            && assertion.getUserInfo().getOrg().getHomeCommunityId() != null) {
            username = assertion.getUserInfo().getOrg().getHomeCommunityId();
        }

        return username;
    }

    public String extractPurpose(AssertionType assertion) {
        String purpose = null;
        if (assertion != null && assertion.getPurposeOfDisclosureCoded() != null
            && assertion.getPurposeOfDisclosureCoded().getCode() != null) {
            purpose = assertion.getPurposeOfDisclosureCoded().getCode();
        }

        return purpose;
    }

    public String extractUserRole(AssertionType assertion) {
        String userRole = null;
        if (assertion != null && assertion.getUserInfo() != null && assertion.getUserInfo().getRoleCoded() != null
            && assertion.getUserInfo().getRoleCoded().getCode() != null) {
            userRole = assertion.getUserInfo().getRoleCoded().getCode();
        }

        return userRole;
    }

    private void appendAuthnStatementAuthnInstant(RequestType policyRequest, AssertionType assertion) {
        LOG.debug("begin appending AuthnStatementAuthnInstant");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.AuthnStatementAuthnInstant;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthnStatementAuthnInstant(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        LOG.debug("end appending AuthnStatementAuthnInstant");
    }

    private String extractAuthnStatementAuthnInstant(AssertionType assertion) {
        String value = null;
        if (assertion.getSamlAuthnStatement() != null) {
            LOG.debug("Extracting AuthInstant");
            value = assertion.getSamlAuthnStatement().getAuthInstant();
            LOG.debug("Extracted AuthInstant value=" + value);
        } else {
            LOG.warn("Unable to find SamlAuthnStatement in SAML, will not be included in message to policy engine");
        }

        return value;
    }

    private void appendAuthnStatementSessionIndex(RequestType policyRequest, AssertionType assertion) {
        LOG.debug("begin appending AuthnStatementSessionIndex");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.AuthnStatementSessionIndex;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthnStatementSessionIndex(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        LOG.debug("end appending AuthnStatementSessionIndex");
    }

    private String extractAuthnStatementSessionIndex(AssertionType assertion) {
        String value = null;
        if (assertion.getSamlAuthnStatement() != null) {
            LOG.debug("Extracting SessionIndex");
            value = assertion.getSamlAuthnStatement().getSessionIndex();
            LOG.debug("Extracted SessionIndex value=" + value);
        } else {
            LOG.debug("Unable to find SamlAuthnStatement in SAML, will not be included in message to policy engine");
        }
        return value;
    }

    private void appendAuthnStatementAthnContextClassRef(RequestType policyRequest, AssertionType assertion) {
        LOG.debug("begin appending AuthnStatementAthnContextClassRef");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.AuthnStatementAthnContextClassRef;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthnStatementAthnContextClassRef(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        LOG.debug("end appending AuthnStatementAthnContextClassRef");
    }

    private String extractAuthnStatementAthnContextClassRef(AssertionType assertion) {
        String value = null;
        if (assertion.getSamlAuthnStatement() != null) {
            LOG.debug("Extracting AuthnStatementAthnContextClassRef");
            value = assertion.getSamlAuthnStatement().getAuthContextClassRef();
            LOG.debug("Extracted AuthnStatementAthnContextClassRef value=" + value);
        } else {
            LOG.warn("Unable to find SamlAuthnStatement in SAML, will not be included in message to policy engine");
        }
        return value;
    }

    private void appendAuthnStatementSubjectLocalityAddress(RequestType policyRequest, AssertionType assertion) {
        LOG.debug("begin appending AuthnStatementSubjectLocalityAddress");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.AuthnStatementSubjectLocalityAddress;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthnStatementSubjectLocalityAddress(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        LOG.debug("end appending AuthnStatementSubjectLocalityAddress");
    }

    private String extractAuthnStatementSubjectLocalityAddress(AssertionType assertion) {
        String value = null;
        if (assertion.getSamlAuthnStatement() != null) {
            LOG.debug("Extracting AuthnStatementSubjectLocalityAddress");
            value = assertion.getSamlAuthnStatement().getSubjectLocalityAddress();
            LOG.debug("Extracted AuthnStatementSubjectLocalityAddress value=" + value);
        } else {
            LOG.warn("Unable to find SamlAuthnStatement in SAML, will not be included in message to policy engine");
        }
        return value;
    }

    private void appendAuthnStatementDNSName(RequestType policyRequest, AssertionType assertion) {
        LOG.debug("begin appending AuthnStatementDNSName");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.AuthnStatementDNSName;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthnStatementDNSName(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        LOG.debug("end appending AuthnStatementDNSName");
    }

    private String extractAuthnStatementDNSName(AssertionType assertion) {
        String value = null;
        if (assertion.getSamlAuthnStatement() != null) {
            LOG.debug("Extracting SubjectLocalityDNSName");
            value = assertion.getSamlAuthnStatement().getSubjectLocalityDNSName();
            LOG.debug("Extracted SubjectLocalityDNSName value=" + value);
        } else {
            LOG.warn("Unable to find SamlAuthnStatement in SAML, will not be included in message to policy engine");
        }
        return value;
    }

    private void appendUserPersonName(RequestType policyRequest, AssertionType assertion) {
        LOG.debug("begin appending UserPersonName");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.UserPersonName;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractUserPersonName(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        LOG.debug("end appending UserPersonName");
    }

    private String extractUserPersonName(AssertionType assertion) {
        String value = null;
        if (assertion.getUserInfo() != null) {
            LOG.debug("Extracting UserPersonName");
            value = assertion.getUserInfo().getUserName();
            LOG.debug("Extracted UserPersonName value=" + value);
        } else {
            LOG.warn("Unable to find UserInfo in SAML, will not be included in message to policy engine");
        }
        return value;
    }

    private void appendUserOrganizationName(RequestType policyRequest, AssertionType assertion) {
        LOG.debug("begin appending UserOrganizationName");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.UserOrganizationName;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractUserOrganizationName(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        LOG.debug("end appending UserOrganizationName");
    }

    private String extractUserOrganizationName(AssertionType assertion) {
        String value = null;
        if (assertion.getUserInfo() != null && assertion.getUserInfo().getOrg() != null) {
            LOG.debug("Extracting UserOrganizationName");
            value = assertion.getUserInfo().getOrg().getName();
            LOG.debug("Extracted UserOrganizationName value=" + value);
        } else {
            LOG.warn("Unable to find User Organization in SAML, will not be included in message to policy engine");
        }
        return value;
    }

    private void appendUserOrganizationId(RequestType policyRequest, AssertionType assertion) {
        LOG.debug("begin appending UserOrganizationId");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.UserOrganizationId;
        String dataType = Constants.DataTypeAnyURI;
        String attributeValue = extractUserOrganizationId(assertion);
        URI orgIdURI = null;
        if (attributeValue != null) {
            try {
                orgIdURI = new URI(attributeValue);
            } catch (URISyntaxException ex) {
                LOG.warn("User org in SAML is not a valid URI, it will not be included in message to policy engine: {}",
                    ex.getLocalizedMessage(), ex);
            }
        } else {
            LOG.warn("User org in SAML is not a valid URI, it will not be included in message to policy engine");
        }
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, orgIdURI, appendAttributesIfNull);
        LOG.debug("end appending UserOrganizationId");
    }

    private String extractUserOrganizationId(AssertionType assertion) {
        String value = null;
        if (assertion.getUserInfo() != null && assertion.getUserInfo().getOrg() != null) {
            LOG.debug("Extracting UserOrganizationId");
            value = assertion.getUserInfo().getOrg().getHomeCommunityId();
            LOG.debug("Extracted UserOrganizationId value=" + value);
        } else {
            LOG.warn("Unable to find User Organization in SAML, it will not be included in message to policy engine");
        }
        return value;
    }

    private void appendHomeCommunityName(RequestType policyRequest, AssertionType assertion) {
        LOG.debug("begin appending HomeCommunityName");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.HomeCommunityName;
        String dataType = Constants.DataTypeAnyURI;
        String attributeValue = extractHomeCommunityName(assertion);
        URI orgIdURI = null;
        if (attributeValue != null) {
            try {
                orgIdURI = new URI(attributeValue);
            } catch (URISyntaxException ex) {
                LOG.warn("Home Community in SAML is not a valid URI, it will not be included in message to policy"
                    + " engine: {}", ex.getLocalizedMessage());
                LOG.trace("Home Community in SAML is not a valid URI, it will not be included in message to policy"
                    + " engine: {}", ex.getLocalizedMessage(), ex);
            }
        } else {
            LOG.warn("User Organization in SAML is not a valid URI, it will not be included in message to policy"
                + " engine");
        }
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, orgIdURI, appendAttributesIfNull);
        LOG.debug("end appending HomeCommunityName");
    }

    private String extractHomeCommunityName(AssertionType assertion) {
        String value = null;
        if (assertion.getHomeCommunity() != null) {
            LOG.debug("Extracting HomeCommunityName");
            value = assertion.getHomeCommunity().getHomeCommunityId();
            LOG.debug("Extracted HomeCommunityName value=" + value);
        } else {
            LOG.warn("Unable to find Home Community in SAML, will not be included in message to policy engine");
        }
        return value;
    }

    private void appendUniquePatientId(RequestType policyRequest, AssertionType assertion) {
        LOG.debug("begin appending UniquePatientId");
        ResourceType parent = getResource(policyRequest);
        String attributeId = XacmlAttributeId.UniquePatientId;
        String dataType = Constants.DataTypeHL7II;
        II attributeValue = extractUniquePatientId(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        LOG.debug("end appending UniquePatientId");
    }

    private II extractUniquePatientId(AssertionType assertion) {
        II iiValue = null;
        String patId = null;
        if (CollectionUtils.isNotEmpty(assertion.getUniquePatientId())) {
            LOG.debug("Extracting UniquePatientId");
            // Take first identifier found
            for (String id : assertion.getUniquePatientId()) {
                if (StringUtils.isNotEmpty(id)) {
                    patId = id;
                    break;
                }
            }
            if (patId != null) {
                String patExt = PatientIdFormatUtil.parsePatientId(patId);
                String patRoot = PatientIdFormatUtil.parseCommunityId(patId);
                LOG.debug("UniquePatientId value=" + patId + " => root:" + patRoot + " extension:" + patExt);
                if (patRoot != null && patExt != null) {
                    iiValue = HL7DataTransformHelper.IIFactory(patRoot, patExt);
                } else {
                    LOG.warn("Unique patient should be in ISO format including a root and extension");
                    LOG.warn("Found root: " + patRoot + " and extension: " + patExt
                        + " It will not be included in message to policy engine");
                }
            } else {
                LOG.warn("Unable to find Unique Patient in SAML, will not be included in message to policy engine");
            }
        } else {
            LOG.warn("Unable to find Unique Patient in SAML, will not be included in message to policy engine");
        }
        return iiValue;
    }

    private void appendUserRoleCode(RequestType policyRequest, AssertionType assertion) {
        LOG.debug("begin appending UserRoleCode");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.UserRoleCode;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractUserRoleCode(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        LOG.debug("end appending UserRoleCode");
    }

    private String extractUserRoleCode(AssertionType assertion) {
        String value = null;
        if (assertion != null && assertion.getUserInfo() != null && assertion.getUserInfo().getRoleCoded() != null) {
            value = assertion.getUserInfo().getRoleCoded().getCode();
        } else {
            LOG.warn("Unable to find user role in SAML, will not be included in message to policy engine");
        }

        return value;
    }

    private void appendUserRoleCodeSystem(RequestType policyRequest, AssertionType assertion) {
        LOG.debug("begin appending UserRoleCodeSystem");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.UserRoleCodeSystem;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractUserRoleCodeSystem(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        LOG.debug("end appending UserRoleCodeSystem");
    }

    private String extractUserRoleCodeSystem(AssertionType assertion) {
        String value = null;
        if (assertion != null && assertion.getUserInfo() != null && assertion.getUserInfo().getRoleCoded() != null) {
            value = assertion.getUserInfo().getRoleCoded().getCodeSystem();
        } else {
            LOG.warn("Unable to find user role in SAML, will not be included in message to policy engine");
        }

        return value;
    }

    private void appendUserRoleCodeSystemName(RequestType policyRequest, AssertionType assertion) {
        LOG.debug("begin appending UserRoleCodeSystemName");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.UserRoleCodeSystemName;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractUserRoleCodeSystemName(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        LOG.debug("end appending UserRoleCodeSystemName");
    }

    private String extractUserRoleCodeSystemName(AssertionType assertion) {
        String value = null;
        if (assertion != null && assertion.getUserInfo() != null && assertion.getUserInfo().getRoleCoded() != null) {
            value = assertion.getUserInfo().getRoleCoded().getCodeSystemName();
        } else {
            LOG.warn("Unable to find user role in SAML, will not be included in message to policy engine");
        }

        return value;
    }

    private void appendUserRoleCodeDiplayName(RequestType policyRequest, AssertionType assertion) {
        LOG.debug("begin appending UserRoleCodeDiplayName");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.UserRoleCodeDiplayName;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractUserRoleCodeDiplayName(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        LOG.debug("end appending UserRoleCodeDiplayName");
    }

    private String extractUserRoleCodeDiplayName(AssertionType assertion) {
        String value = null;
        if (assertion != null && assertion.getUserInfo() != null && assertion.getUserInfo().getRoleCoded() != null) {
            value = assertion.getUserInfo().getRoleCoded().getDisplayName();
        } else {
            LOG.warn("Unable to find user role in SAML, will not be included in message to policy engine");
        }

        return value;
    }

    private void appendPurposeOfUseCode(RequestType policyRequest, AssertionType assertion) {
        LOG.debug("begin appending PurposeOfUseCode");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.PurposeOfUseCode;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractPurposeOfUseCode(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        LOG.debug("end appending PurposeOfUseCode");
    }

    private String extractPurposeOfUseCode(AssertionType assertion) {
        String value = null;
        if (assertion != null && assertion.getPurposeOfDisclosureCoded() != null) {
            value = assertion.getPurposeOfDisclosureCoded().getCode();
        }

        return value;
    }

    private void appendPurposeOfUseCodeSystem(RequestType policyRequest, AssertionType assertion) {
        LOG.debug("begin appending PurposeOfUseCodeSystem");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.PurposeOfUseCodeSystem;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractPurposeOfUseCodeSystem(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        LOG.debug("end appending PurposeOfUseCodeSystem");
    }

    private String extractPurposeOfUseCodeSystem(AssertionType assertion) {
        String value = null;
        if (assertion != null && assertion.getPurposeOfDisclosureCoded() != null) {
            value = assertion.getPurposeOfDisclosureCoded().getCodeSystem();
        }

        return value;
    }

    private void appendPurposeOfUseCodeSystemName(RequestType policyRequest, AssertionType assertion) {
        LOG.debug("begin appending PurposeOfUseCodeSystemName");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.PurposeOfUseCodeSystemName;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractPurposeOfUseCodeSystemName(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        LOG.debug("end appending PurposeOfUseCodeSystemName");
    }

    private String extractPurposeOfUseCodeSystemName(AssertionType assertion) {
        String value = null;
        if (assertion != null && assertion.getPurposeOfDisclosureCoded() != null) {
            value = assertion.getPurposeOfDisclosureCoded().getCodeSystemName();
        }

        return value;
    }

    private void appendPurposeOfUseCodeDisplayName(RequestType policyRequest, AssertionType assertion) {
        LOG.debug("begin appending PurposeOfUseCodeDisplayName");
        SubjectType parent = getSubject(policyRequest);
        String attributeId = XacmlAttributeId.PurposeOfUseCodeDisplayName;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractPurposeOfUseCodeDisplayName(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        LOG.debug("end appending PurposeOfUseCodeDisplayName");
    }

    private String extractPurposeOfUseCodeDisplayName(AssertionType assertion) {
        String value = null;
        if (assertion != null && assertion.getPurposeOfDisclosureCoded() != null) {
            value = assertion.getPurposeOfDisclosureCoded().getDisplayName();
        }

        return value;
    }

    private void appendAuthzDecisionStatementDecision(RequestType policyRequest, AssertionType assertion) {
        LOG.debug("begin appending AuthzDecisionStatementDecision");
        ResourceType parent = getResource(policyRequest);
        String attributeId = XacmlAttributeId.AuthzDecisionStatementDecision;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthzDecisionStatementDecision(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        LOG.debug("end appending AuthzDecisionStatementDecision");
    }

    private String extractAuthzDecisionStatementDecision(AssertionType assertion) {
        String value = null;
        if (assertion.getSamlAuthzDecisionStatement() != null) {
            LOG.debug("Extracting AuthzDecisionStatementDecision");
            value = assertion.getSamlAuthzDecisionStatement().getDecision();
            LOG.debug("Extracted AuthzDecisionStatementDecision value=" + value);
        } else {
            LOG.warn("Unable to find AuthzDecisionStatementDecision in SAML, will not be included in message to "
                + "policy engine");
        }

        return value;
    }

    private void appendAuthzDecisionStatementResource(RequestType policyRequest, AssertionType assertion) {
        LOG.debug("begin appending AuthzDecisionStatementResource");
        ResourceType parent = getResource(policyRequest);
        String attributeId = XacmlAttributeId.AuthzDecisionStatementResource;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthzDecisionStatementResource(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        LOG.debug("end appending AuthzDecisionStatementResource");
    }

    private String extractAuthzDecisionStatementResource(AssertionType assertion) {
        String value = null;
        if (assertion.getSamlAuthzDecisionStatement() != null) {
            LOG.debug("Extracting AuthzDecisionStatementResource");
            value = assertion.getSamlAuthzDecisionStatement().getResource();
            LOG.debug("Extracted AuthzDecisionStatementResource value=" + value);
        } else {
            LOG.warn("Unable to find AuthzDecisionStatement in SAML, will not be included in message to policy engine");
        }

        return value;
    }

    private void appendAuthzDecisionStatementAction(RequestType policyRequest, AssertionType assertion) {
        LOG.debug("begin appending AuthzDecisionStatementAction");
        ResourceType parent = getResource(policyRequest);
        String attributeId = XacmlAttributeId.AuthzDecisionStatementAction;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthzDecisionStatementAction(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        LOG.debug("end appending AuthzDecisionStatementAction");
    }

    private String extractAuthzDecisionStatementAction(AssertionType assertion) {
        String value = null;
        if (assertion.getSamlAuthzDecisionStatement() != null) {
            LOG.debug("Extracting AuthzDecisionStatementAction");
            value = assertion.getSamlAuthzDecisionStatement().getAction();
            LOG.debug("Extracted AuthzDecisionStatementAction value=" + value);
        } else {
            LOG.warn("Unable to find AuthzDecisionStatement in SAML, will not be included in message to policy engine");
        }

        return value;
    }

    private void appendAuthzDecisionStatementEvidenceAssertionID(RequestType policyRequest, AssertionType assertion) {
        LOG.debug("begin appending AuthzDecisionStatementEvidenceAssertionID");
        ResourceType parent = getResource(policyRequest);
        String attributeId = XacmlAttributeId.AuthzDecisionStatementEvidenceAssertionID;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthzDecisionStatementEvidenceAssertionID(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        LOG.debug("end appending AuthzDecisionStatementEvidenceAssertionID");
    }

    private String extractAuthzDecisionStatementEvidenceAssertionID(AssertionType assertion) {
        String value = null;
        if (assertion.getSamlAuthzDecisionStatement() != null
            && assertion.getSamlAuthzDecisionStatement().getEvidence() != null
            && assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion() != null) {
            LOG.debug("Extracting AuthzDecisionStatementEvidenceAssertionID");
            value = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getId();
            LOG.debug("Extracted AuthzDecisionStatementEvidenceAssertionID value=" + value);
        } else {
            LOG.warn("Unable to find AuthzDecisionStatement Evidence Assertion in SAML, will not be included in "
                + "message to policy engine");
        }

        return value;
    }

    private void appendAuthzDecisionStatementEvidenceAssertionIssueInstant(RequestType policyRequest,
        AssertionType assertion) {
        LOG.debug("begin appending AuthzDecisionStatementEvidenceAssertionIssueInstant");
        ResourceType parent = getResource(policyRequest);
        String attributeId = XacmlAttributeId.AuthzDecisionStatementEvidenceAssertionIssueInstant;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthzDecisionStatementEvidenceAssertionIssueInstant(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        LOG.debug("end appending AuthzDecisionStatementEvidenceAssertionIssueInstant");
    }

    private String extractAuthzDecisionStatementEvidenceAssertionIssueInstant(AssertionType assertion) {
        String value = null;
        if (assertion.getSamlAuthzDecisionStatement() != null
            && assertion.getSamlAuthzDecisionStatement().getEvidence() != null
            && assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion() != null) {
            LOG.debug("Extracting AuthzDecisionStatementEvidenceAssertionIssueInstant");
            value = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getIssueInstant();
            LOG.debug("Extracted AuthzDecisionStatementEvidenceAssertionIssueInstant value=" + value);
        } else {
            LOG.debug("Unable to find AuthzDecisionStatement Evidence Assertion in SAML, will not be included in"
                + " message to policy engine");
        }

        return value;
    }

    private void appendAuthzDecisionStatementEvidenceAssertionVersion(RequestType policyRequest,
        AssertionType assertion) {
        LOG.debug("begin appending AuthzDecisionStatementEvidenceAssertionVersion");
        ResourceType parent = getResource(policyRequest);
        String attributeId = XacmlAttributeId.AuthzDecisionStatementEvidenceAssertionVersion;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthzDecisionStatementEvidenceAssertionVersion(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        LOG.debug("end appending AuthzDecisionStatementEvidenceAssertionVersion");
    }

    private String extractAuthzDecisionStatementEvidenceAssertionVersion(AssertionType assertion) {
        String value = null;
        if (assertion.getSamlAuthzDecisionStatement() != null
            && assertion.getSamlAuthzDecisionStatement().getEvidence() != null
            && assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion() != null) {
            LOG.debug("Extracting AuthzDecisionStatementEvidenceAssertionVersion");
            value = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getVersion();
            LOG.debug("Extracted AuthzDecisionStatementEvidenceAssertionVersion value=" + value);
        } else {
            LOG.warn("Unable to find AuthzDecisionStatement Evidence Assertion in SAML, will not be included in message"
                + " to policy engine");
        }

        return value;

    }

    private void appendAuthzDecisionStatementEvidenceAssertionIssuer(RequestType policyRequest,
        AssertionType assertion) {
        LOG.debug("begin appending AuthzDecisionStatementEvidenceAssertionIssuer");
        ResourceType parent = getResource(policyRequest);
        String attributeId = XacmlAttributeId.AuthzDecisionStatementEvidenceAssertionIssuer;
        String dataType = Constants.DataTypeString;
        String attributeValue = extractAuthzDecisionStatementEvidenceAssertionIssuer(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        LOG.debug("end appending AuthzDecisionStatementEvidenceAssertionIssuer");
    }

    private String extractAuthzDecisionStatementEvidenceAssertionIssuer(AssertionType assertion) {
        String value = null;
        if (assertion.getSamlAuthzDecisionStatement() != null
            && assertion.getSamlAuthzDecisionStatement().getEvidence() != null
            && assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion() != null) {
            LOG.debug("Extracting AuthzDecisionStatementEvidenceAssertionIssuer");
            value = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getIssuer();
            LOG.debug("Extracted AuthzDecisionStatementEvidenceAssertionIssuer value=" + value);
        } else {
            LOG.warn(
                "Unable to find AuthzDecisionStatement Evidence Assertion in SAML, will not be included in message to policy engine");
        }

        return value;
    }

    private String extractAuthzDecisionStatementEvidenceAssertionConditionsNotBefore(AssertionType assertion) {
        String value = null;
        if (assertion.getSamlAuthzDecisionStatement() != null
            && assertion.getSamlAuthzDecisionStatement().getEvidence() != null
            && assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion() != null
            && assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions() != null) {
            LOG.debug("Extracting AuthzDecisionStatementEvidenceAssertionConditionsNotBefore");
            value = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions()
                .getNotBefore();
            LOG.debug("Extracted AuthzDecisionStatementEvidenceAssertionConditionsNotBefore value=" + value);
        } else {
            LOG.warn("Unable to find AuthzDecisionStatement Evidence Assertion Condition in SAML, will not be included"
                + " in message to policy engine");
        }

        return value;
    }

    private void appendAuthzDecisionStatementEvidenceAssertionConditionsNotBefore(RequestType policyRequest,
        AssertionType assertion) {
        LOG.debug("begin appending AuthzDecisionStatementEvidenceAssertionConditionsNotBefore");
        ResourceType parent = getResource(policyRequest);
        String attributeId = XacmlAttributeId.AuthzDecisionStatementEvidenceAssertionConditionsNotBefore;
        String dataType = Constants.DataTypeDate;
        String attributeValue = extractAuthzDecisionStatementEvidenceAssertionConditionsNotBefore(assertion);
        XMLGregorianCalendar calValue = createCal(attributeValue);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, calValue, appendAttributesIfNull);
        LOG.debug("end appending AuthzDecisionStatementEvidenceAssertionConditionsNotBefore");
    }

    private String extractAuthzDecisionStatementEvidenceAssertionConditionsNotOnOrAfter(AssertionType assertion) {
        String value = null;
        if (assertion.getSamlAuthzDecisionStatement() != null
            && assertion.getSamlAuthzDecisionStatement().getEvidence() != null
            && assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion() != null
            && assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions() != null) {
            LOG.debug("Extracting AuthzDecisionStatementEvidenceAssertionConditionsNotOnOrAfter");
            value = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions()
                .getNotOnOrAfter();
            LOG.debug("Extracted AuthzDecisionStatementEvidenceAssertionConditionsNotOnOrAfter value=" + value);
        } else {
            LOG.warn("Unable to find AuthzDecisionStatement Evidence Assertion Condition in SAML, will not be included"
                + " in message to policy engine");
        }

        return value;

    }

    private void appendAuthzDecisionStatementEvidenceAssertionConditionsNotOnOrAfter(RequestType policyRequest,
        AssertionType assertion) {
        LOG.debug("begin appending AuthzDecisionStatementEvidenceAssertionConditionsNotOnOrAfter");
        ResourceType parent = getResource(policyRequest);
        String attributeId = XacmlAttributeId.AuthzDecisionStatementEvidenceAssertionConditionsNotOnOrAfter;
        String dataType = Constants.DataTypeDate;
        String attributeValue = extractAuthzDecisionStatementEvidenceAssertionConditionsNotOnOrAfter(assertion);
        XMLGregorianCalendar calValue = createCal(attributeValue);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, calValue, appendAttributesIfNull);
        LOG.debug("end appending AuthzDecisionStatementEvidenceAssertionConditionsNotOnOrAfter");
    }

    private XMLGregorianCalendar createCal(String time) {

        XMLGregorianCalendar xmlDate = null;
        if (time != null) {
            try {
                // times must be in UTC format as specified by the XML Schema type (dateTime)
                DatatypeFactory xmlDateFactory = DatatypeFactory.newInstance();
                xmlDate = xmlDateFactory.newXMLGregorianCalendar(time.trim());
            } catch (IllegalArgumentException iaex) {
                LOG.warn("Exception with UTC format calendar creation: {}", iaex.getLocalizedMessage(), iaex);
                try {
                    // try simple date format - backward compatibility
                    SimpleDateFormat dateForm = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    GregorianCalendar cal = new GregorianCalendar();
                    cal.setTimeZone(TimeZone.getTimeZone("GMT"));
                    cal.setTime(dateForm.parse(time));
                    DatatypeFactory xmlDateFactory = DatatypeFactory.newInstance();
                    xmlDate = xmlDateFactory.newXMLGregorianCalendar(cal);
                } catch (DatatypeConfigurationException | ParseException ex) {
                    LOG.error("Date form is expected to be in dateTime format: {}", ex.getLocalizedMessage(), ex);
                }
            } catch (DatatypeConfigurationException dtex) {
                LOG.error("Problem creating XML Date Factory, setting default date: {}", dtex.getLocalizedMessage(),
                    dtex);
            }
        }
        return xmlDate;
    }

    private List<String> extractAuthzDecisionStatementEvidenceAssertionAccessConsentPolicy(AssertionType assertion) {
        List<String> value = null;
        if (assertion.getSamlAuthzDecisionStatement() != null
            && assertion.getSamlAuthzDecisionStatement().getEvidence() != null
            && assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion() != null
            && !assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getAccessConsentPolicy()
            .isEmpty()) {
            LOG.debug("Extracting AuthzDecisionStatementEvidenceAssertionAccessConsentPolicy");
            value = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getAccessConsentPolicy();
            LOG.debug("Extracted AuthzDecisionStatementEvidenceAssertionAccessConsentPolicy value=" + value);
        } else {
            LOG.warn("Unable to find AuthzDecisionStatement Evidence Assertion in SAML, will not be included in"
                + " message to policy engine");
        }

        return value;
    }

    private void appendAuthzDecisionStatementEvidenceAssertionAccessConsentPolicy(RequestType policyRequest,
        AssertionType assertion) {
        LOG.debug("begin appending AuthzDecisionStatementEvidenceAssertionAccessConsentPolicy");
        ResourceType parent = getResource(policyRequest);
        String attributeId = XacmlAttributeId.AuthzDecisionStatementEvidenceAssertionAccessConsentPolicy;
        String dataType = Constants.DataTypeAnyURI;
        List<String> attributeValue = extractAuthzDecisionStatementEvidenceAssertionAccessConsentPolicy(assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        LOG.debug("end appending AuthzDecisionStatementEvidenceAssertionAccessConsentPolicy");
    }

    private List<String> extractAuthzDecisionStatementEvidenceAssertionInstanceAccessConsentPolicy(
        AssertionType assertion) {
        List<String> value = null;
        if (assertion.getSamlAuthzDecisionStatement() != null
            && assertion.getSamlAuthzDecisionStatement().getEvidence() != null
            && assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion() != null
            && !assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion()
            .getInstanceAccessConsentPolicy().isEmpty()) {
            LOG.debug("Extracting AuthzDecisionStatementEvidenceAssertionInstanceAccessConsentPolicy");
            value = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion()
                .getInstanceAccessConsentPolicy();
            LOG.debug("Extracted AuthzDecisionStatementEvidenceAssertionInstanceAccessConsentPolicy value=" + value);
        } else {
            LOG.warn("Unable to find AuthzDecisionStatement Evidence Assertion in SAML, will not be included in"
                + " message to policy engine");
        }

        return value;
    }

    private void appendAuthzDecisionStatementEvidenceAssertionInstanceAccessConsentPolicy(RequestType policyRequest,
        AssertionType assertion) {
        LOG.debug("begin appending AuthzDecisionStatementEvidenceAssertionInstanceAccessConsentPolicy");
        ResourceType parent = getResource(policyRequest);
        String attributeId = XacmlAttributeId.AuthzDecisionStatementEvidenceAssertionInstanceAccessConsentPolicy;
        String dataType = Constants.DataTypeAnyURI;
        List<String> attributeValue = extractAuthzDecisionStatementEvidenceAssertionInstanceAccessConsentPolicy(
            assertion);
        AttributeHelper attrHelper = new AttributeHelper();
        attrHelper.appendAttributeToParent(parent, attributeId, dataType, attributeValue, appendAttributesIfNull);
        LOG.debug("end appending AuthzDecisionStatementEvidenceAssertionInstanceAccessConsentPolicy");
    }

    private void appendAttributeAccessConsent(RequestType policyRequest, AssertionType assertion) {
        LOG.trace("begin appending AttributeAccessConsentPolicies");
        ResourceType parent = getResource(policyRequest);
        AttributeHelper attrHelper = new AttributeHelper();
        String dataType = Constants.DataTypeAnyURI;
        
        if(NullChecker.isNotNullish(assertion.getAcpAttribute())) {
            String attributeId = XacmlAttributeId.ATTRIBUTE_ACCESS_CONSENT_POLICY;           
            attrHelper.appendAttributeToParent(parent, attributeId, dataType, assertion.getAcpAttribute(), appendAttributesIfNull);
        }
        
        if(NullChecker.isNotNullish(assertion.getInstanceAcpAttribute())) {
            String attributeId = XacmlAttributeId.ATTRIBUTE_INSTANCE_ACCESS_CONSENT_POLICY;
            attrHelper.appendAttributeToParent(parent, attributeId, dataType, assertion.getInstanceAcpAttribute(), appendAttributesIfNull);
        }
      
        LOG.trace("end appending AttributeAccessConsentPolicies");
    }
}
