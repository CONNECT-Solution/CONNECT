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
package gov.hhs.fha.nhinc.opensaml.extraction;

import gov.hhs.fha.nhinc.callback.SamlConstants;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthnStatementType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementEvidenceAssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementEvidenceConditionsType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementEvidenceType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlConditionsType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlIssuerType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlSignatureKeyInfoType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlSignatureType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlSubjectConfirmationType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.cxf.extraction.SAMLExtractorDOM;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.util.StringUtil;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.apache.commons.collections.CollectionUtils;
import org.apache.cxf.common.util.StringUtils;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.common.saml.OpenSAMLUtil;
import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.schema.XSAny;
import org.opensaml.core.xml.util.AttributeMap;
import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.saml.saml2.core.Attribute;
import org.opensaml.saml.saml2.core.AttributeStatement;
import org.opensaml.saml.saml2.core.AuthnStatement;
import org.opensaml.saml.saml2.core.AuthzDecisionStatement;
import org.opensaml.saml.saml2.core.Conditions;
import org.opensaml.saml.saml2.core.Evidence;
import org.opensaml.saml.saml2.core.Issuer;
import org.opensaml.saml.saml2.core.NameID;
import org.opensaml.saml.saml2.core.Subject;
import org.opensaml.saml.saml2.core.SubjectConfirmation;
import org.opensaml.saml.saml2.core.SubjectConfirmationData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author mweaver
 *
 */
public class OpenSAMLAssertionExtractorImpl implements SAMLExtractorDOM {

    private static final Logger LOG = LoggerFactory.getLogger(OpenSAMLAssertionExtractorImpl.class);
    private static final String EMPTY_STRING = "";
    private static final String X509_FORMAT = "urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName";
    private static final String ACCESS_CONSENT_POLICY_ATTRIBUTE_NAME = "AccessConsentPolicy";
    private static final String INSTANCE_ACCESS_CONSENT_POLICY_ATTRIBUTE_NAME = "InstanceAccessConsentPolicy";

    /**
     * This method is used to extract the SAML assertion information.
     *
     * @param element
     * @return AssertionType
     */
    @Override
    public final AssertionType extractSAMLAssertion(final Element element) {

        LOG.debug("Executing Saml2AssertionExtractor.extractSamlAssertion()...");
        if (null == element) {
            return null;
        }
        Assertion saml2Assertion = extractSaml2Assertion(element);

        AssertionType target = initializeAssertion();
        // Populate Issuer
        if (saml2Assertion != null) {
            populateIssuer(saml2Assertion, target);
            // Populate the Subject Information
            populateSubject(saml2Assertion, target);
            // Populate the Authentication Statement Information.
            populateAuthenticationStatement(saml2Assertion, target);
            // Populate the Attribute Statement Information.
            populateAttributeStatement(saml2Assertion, target);
            // Populate the Authorization Decision Statement Information
            populateAuthzDecisionStatement(saml2Assertion, target);
        }

        LOG.debug("end extractSamlAssertion()");

        return target;
    }

    /**
     * Extract subject multiple confirmation into assertionType
     *
     * @param saml2Assertion source
     * @param target
     */
    private static void populateSubjectConfirmation(Assertion saml2Assertion, AssertionType target) {
        List<SamlSubjectConfirmationType> samlTargetSubjects = target.getSamlSubjectConfirmations();
        List<SubjectConfirmation> subjectConfirmations = saml2Assertion.getSubject().getSubjectConfirmations();
        for (SubjectConfirmation subConfirmation : subjectConfirmations) {
            SamlSubjectConfirmationType samlSubjConfTypeTarget = new SamlSubjectConfirmationType();
            samlSubjConfTypeTarget.setMethod(subConfirmation.getMethod());
            SubjectConfirmationData subjectConfirmationData = subConfirmation.getSubjectConfirmationData();
            if (subjectConfirmationData != null) {
                samlSubjConfTypeTarget.setAddress(subjectConfirmationData.getAddress());
                samlSubjConfTypeTarget.setInResponseTo(subjectConfirmationData.getInResponseTo());
                samlSubjConfTypeTarget.setRecipient(subjectConfirmationData.getRecipient());
                SamlConditionsType samlConditionTarget = new SamlConditionsType();
                if (subjectConfirmationData.getNotBefore() != null) {
                    samlConditionTarget.setNotBefore(subjectConfirmationData.getNotBefore().toString());
                }
                if (subjectConfirmationData.getNotOnOrAfter() != null) {
                    samlConditionTarget.setNotOnOrAfter(subjectConfirmationData.getNotOnOrAfter().toString());
                }
                samlSubjConfTypeTarget.setSubjectCondition(samlConditionTarget);
            }
            samlTargetSubjects.add(samlSubjConfTypeTarget);
        }
    }

    /**
     * This method will return the first Assertion encountered in the passed in element.
     *
     * @param element the xml element to extract the assertion from
     * @return The first encountered Assertion object in the element
     */
    private static Assertion extractSaml2Assertion(final Element element) {
        if (element.getNamespaceURI().equals(SamlConstants.SAML2_ASSERTION_NS)
            && element.getLocalName().equals(SamlConstants.SAML2_ASSERTION_TAG)) {

            return convertToAssertion(element);
        }

        return extractSaml2AssertionFromDescendants(element);
    }

    private static Assertion extractSaml2AssertionFromDescendants(final Element element) {
        NodeList assertionNodes = element.getElementsByTagNameNS(SamlConstants.SAML2_ASSERTION_NS,
            SamlConstants.SAML2_ASSERTION_TAG);

        if (assertionNodes.getLength() > 0) {
            Node assertionNode = assertionNodes.item(0);
            if (assertionNode instanceof Element) {
                return convertToAssertion((Element) assertionNode);
            }
        }

        return null;
    }

    private static Assertion convertToAssertion(final Element element) {
        try {
            org.opensaml.core.xml.XMLObject xmlObject = OpenSAMLUtil.fromDom(element);
            if (xmlObject instanceof org.opensaml.saml.saml2.core.Assertion) {
                return (Assertion) xmlObject;
            }
        } catch (WSSecurityException e) {
            LOG.error("error extracting SAML assertion", e);
        }

        return null;
    }

    private static void populateIssuer(final Assertion saml2Assertion, final AssertionType target) {
        target.setSamlIssuer(new SamlIssuerType());
        target.getSamlIssuer().setIssuer(saml2Assertion.getIssuer().getValue());
        target.getSamlIssuer().setIssuerFormat(saml2Assertion.getIssuer().getFormat());
    }

    /**
     * This method is used to populate the Attribute Statement.
     *
     * @param saml2Assertion saml2 assertion
     * @param target target assertion
     */
    private void populateAttributeStatement(final Assertion saml2Assertion, final AssertionType target) {

        LOG.trace("Executing Saml2AssertionExtractor.populateAttributeStatement()...");
        AttributeHelper helper = new AttributeHelper();

        for (AttributeStatement attributeStatement : saml2Assertion.getAttributeStatements()) {
            for (Attribute attribute : attributeStatement.getAttributes()) {
                compareAttribute(attribute, target, helper);
            }
        }
        LOG.trace("end populateAttributeStatement()");
    }

    /**
     * @param attribute
     * @param target
     * @param attrName
     */
    private void compareAttribute(Attribute attribute, AssertionType target, AttributeHelper helper) {

        if (attribute.getName().equals(NhincConstants.ATTRIBUTE_NAME_SUBJECT_ROLE)) {
            LOG.debug("Extracting Assertion.userInfo.roleCoded:");
            populateSubjectRole(attribute, target);

        } else if (attribute.getName().equals(NhincConstants.ATTRIBUTE_NAME_PURPOSE_OF_USE)) {
            LOG.debug("Extracting Assertion.purposeOfDisclosure:");
            populatePurposeOfUseAttribute(attribute, target);

        } else if (attribute.getName().equals(SamlConstants.USERNAME_ATTR)) {
            helper.extractNameParts(attribute, target);

        } else if (attribute.getName().equals(SamlConstants.USER_ORG_ATTR)) {
            String orgAttribute = getAttributeValue(attribute);
            target.getUserInfo().getOrg().setName(orgAttribute);
            LOG.debug("Assertion.userInfo.org.Name = {}", orgAttribute);

        } else if (attribute.getName().equals(SamlConstants.USER_ORG_ID_ATTR)) {
            String orgIDAttribute = getAttributeValue(attribute);
            target.getUserInfo().getOrg().setHomeCommunityId(orgIDAttribute);
            LOG.debug("Assertion.userInfo.org.homeCommunityId = {}", orgIDAttribute);

        } else if (attribute.getName().equals(NhincConstants.ATTRIBUTE_NAME_HCID)) {
            String homeCommunityId = getAttributeValue(attribute);
            target.getHomeCommunity().setHomeCommunityId(homeCommunityId);
            LOG.debug("Assertion.homeCommunity.homeCommunityId = {}", homeCommunityId);

        } else if (attribute.getName().equals(SamlConstants.ACCESS_CONSENT_ATTR)) {
            List<String> accessConsentId = transformXMLtoString(attribute.getAttributeValues());
            target.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getAccessConsentPolicy()
                .addAll(accessConsentId);
            LOG.debug("Assertion.SamlAuthzDecisionStatement.Evidence.Assertion.AccessConsentPolicy = {}",
                accessConsentId);

        } else if (attribute.getName().equals(SamlConstants.INST_ACCESS_CONSENT_ATTR)) {
            List<String> instAccessConsentId = transformXMLtoString(attribute.getAttributeValues());
            target.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getInstanceAccessConsentPolicy()
                .addAll(instAccessConsentId);
            LOG.debug("Assertion.SamlAuthzDecisionStatement.Evidence.Assertion.InstanceAccessConsentPolicy = {}",
                instAccessConsentId);
        } else if (attribute.getName().equals(SamlConstants.ATTRIBUTE_NAME_XUA_ACP)) {
            String acpValue = getAttributeValue(attribute);
            if (org.apache.commons.lang.StringUtils.isNotEmpty(acpValue)) {
                target.setAcpAttribute(acpValue);
            }
        } else if (attribute.getName().equals(SamlConstants.ATTRIBUTE_NAME_XUA_IACP)) {
            String iacpValue = getAttributeValue(attribute);
            if (org.apache.commons.lang.StringUtils.isNotEmpty(iacpValue)) {
                target.setInstanceAcpAttribute(iacpValue);
            }
        } else if (!addProviderPatientID(attribute, target)) {
            LOG.warn("Unrecognized Name Attribute: {}", attribute.getName());

        }
    }

    /**
     * @param target
     * @param attribute
     * @return
     */
    private static boolean addProviderPatientID(Attribute attribute, AssertionType target) {
        if (attribute.getName().equals(NhincConstants.ATTRIBUTE_NAME_RESOURCE_ID)) {
            if (!StringUtils.isEmpty(attribute.getDOM().getTextContent())) {
                String patientId = getAttributeValue(attribute);
                target.getUniquePatientId().add(patientId);
                LOG.debug("Assertion.uniquePatientId = {}", patientId);
            }
            return true;

        } else if (attribute.getName().equals(NhincConstants.ATTRIBUTE_NAME_NPI)) {
            String nationalProviderId = getAttributeValue(attribute);
            target.setNationalProviderId(nationalProviderId);
            LOG.debug("Assertion.nationalProviderId = {}", nationalProviderId);
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param attributeValues
     * @return The same list, with XMLObjects converted to a String representation
     */
    private List<String> transformXMLtoString(List<XMLObject> attributeValues) {
        List<String> stringList = new ArrayList<>();
        for (XMLObject item : attributeValues) {
            stringList.add(item.toString());
        }
        return stringList;
    }

    private static String getAttributeValue(Attribute attribute) {
        if (!CollectionUtils.isEmpty(attribute.getAttributeValues()) && attribute.getAttributeValues().get(0) != null
            && attribute.getAttributeValues().get(0).getDOM() != null
            && attribute.getAttributeValues().get(0).getDOM().getTextContent() != null) {
            return attribute.getAttributeValues().get(0).getDOM().getTextContent().trim();
        } else {
            return null;
        }
    }

    /**
     * This method is used to populate the Authentication Statement Information.
     *
     * @param saml2Assertion saml2 assertion
     * @param target target assertion
     */
    private static void populateAuthenticationStatement(final Assertion saml2Assertion, final AssertionType target) {

        LOG.debug("Executing Saml2AssertionExtractor.populateAuthenticationStatement()...");
        SamlAuthnStatementType samlAuthnStatement = new SamlAuthnStatementType();
        if (null == saml2Assertion.getAuthnStatements() || saml2Assertion.getAuthnStatements().isEmpty()) {
            return;
        }
        AuthnStatement source = saml2Assertion.getAuthnStatements().get(0);
        samlAuthnStatement.setAuthInstant(source.getAuthnInstant().toString());
        samlAuthnStatement.setSessionIndex(source.getSessionIndex());
        samlAuthnStatement
            .setAuthContextClassRef(source.getAuthnContext().getAuthnContextClassRef().getAuthnContextClassRef());

        if (source.getSubjectLocality() != null) {
            samlAuthnStatement.setSubjectLocalityDNSName(source.getSubjectLocality().getDNSName());
            samlAuthnStatement.setSubjectLocalityAddress(source.getSubjectLocality().getAddress());
        }

        target.setSamlAuthnStatement(samlAuthnStatement);
        LOG.debug("end populateAuthenticationStatement()");
    }

    /**
     * This method is used to populate the Subject Information into the target assertion.
     *
     * @param saml2Assertion saml2 assertion
     * @param target target assertion
     */
    private static void populateSubject(final Assertion saml2Assertion, final AssertionType target) {
        LOG.debug("Executing Saml2AssertionExtractor.populateSubject()...");

        Subject subject = saml2Assertion.getSubject();
        if (subject == null) {
            return;
        }
        NameID name = subject.getNameID();

        if (name != null) {

            if (X509_FORMAT.equals(name.getFormat())) {
                LOG.warn("Subject name format is not X509!");
            }
            target.getUserInfo().setUserName(name.getValue());
        }
        populateSubjectConfirmation(saml2Assertion, target);
        LOG.debug("end populateSubject()");
    }

    /**
     * This method is used to populate the Authorization Decision Statement Information.
     *
     * @param saml2Assertion saml2 assertion
     * @param target target assertion
     */
    private static void populateAuthzDecisionStatement(final Assertion saml2Assertion, final AssertionType target) {

        LOG.debug("Executing Saml2AssertionExtractor.populateAuthzDecisionStatement()...");

        List<AuthzDecisionStatement> saml2AuthzDecisionStatements = saml2Assertion.getAuthzDecisionStatements();
        if (saml2AuthzDecisionStatements == null || saml2AuthzDecisionStatements.isEmpty()) {
            target.setSamlAuthzDecisionStatement(null);
            return;
        }

        AuthzDecisionStatement saml2AuthzDecisionStatement = saml2AuthzDecisionStatements.get(0);

        SamlAuthzDecisionStatementType targetAuthzDecisionStatement = new SamlAuthzDecisionStatementType();
        target.setSamlAuthzDecisionStatement(targetAuthzDecisionStatement);

        // Translate attributes (Decision and Resource)
        targetAuthzDecisionStatement.setDecision(saml2AuthzDecisionStatement.getDecision().toString());
        targetAuthzDecisionStatement.setResource(saml2AuthzDecisionStatement.getResource());

        // Translate action
        targetAuthzDecisionStatement.setAction(saml2AuthzDecisionStatement.getActions().get(0).getAction());

        // Translate evidence
        Evidence saml2Evidence = saml2AuthzDecisionStatement.getEvidence();
        if (saml2Evidence != null) {
            SamlAuthzDecisionStatementEvidenceType targetEvidence = new SamlAuthzDecisionStatementEvidenceType();
            targetAuthzDecisionStatement.setEvidence(targetEvidence);
            translateEvidenceAssertions(targetEvidence, saml2Evidence.getAssertions());
        }
        LOG.debug("end populateAuthzDecisionStatement()");
    }

    private static void translateEvidenceAssertions(SamlAuthzDecisionStatementEvidenceType targetEvidence,
        List<Assertion> saml2EvidenceAssertions) {

        if (CollectionUtils.isEmpty(saml2EvidenceAssertions)) {
            LOG.trace("Empty/null assertion list");
            return;
        }

        Assertion saml2EvidenceAssertion = saml2EvidenceAssertions.get(0);

        SamlAuthzDecisionStatementEvidenceAssertionType targetEvidenceAssertion = new SamlAuthzDecisionStatementEvidenceAssertionType();
        targetEvidence.setAssertion(targetEvidenceAssertion);

        // Translate Evidence Assertion Attributes
        targetEvidenceAssertion.setId(saml2EvidenceAssertion.getID());
        targetEvidenceAssertion.setIssueInstant(saml2EvidenceAssertion.getIssueInstant().toString());
        targetEvidenceAssertion.setVersion(saml2EvidenceAssertion.getVersion().toString());

        // Translate Evidence Attribute Statement
        addConsent(saml2EvidenceAssertion, targetEvidenceAssertion);

        // Only create the Conditions if NotBefore and/or NotOnOrAfter is present
        if (saml2EvidenceAssertion.getConditions() != null
            && (saml2EvidenceAssertion.getConditions().getNotBefore() != null
                || saml2EvidenceAssertion.getConditions().getNotOnOrAfter() != null)) {
            // Translate Evidence Conditions
            Conditions saml2EvidenceCondition = saml2EvidenceAssertion.getConditions();
            SamlAuthzDecisionStatementEvidenceConditionsType targetConditions = new SamlAuthzDecisionStatementEvidenceConditionsType();
            targetEvidenceAssertion.setConditions(targetConditions);
            if (saml2EvidenceCondition.getNotBefore() != null) {
                targetConditions.setNotBefore(saml2EvidenceCondition.getNotBefore().toString());
            }
            if (saml2EvidenceCondition.getNotOnOrAfter() != null) {
                targetConditions.setNotOnOrAfter(saml2EvidenceCondition.getNotOnOrAfter().toString());
            }
        }

        // Translate Evidence Issuer
        Issuer saml2EvidenceIssuer = saml2EvidenceAssertion.getIssuer();

        targetEvidenceAssertion.setIssuerFormat(saml2EvidenceIssuer.getFormat());
        targetEvidenceAssertion.setIssuer(saml2EvidenceIssuer.getValue());
    }

    /**
     * @param targetEvidenceAssertion
     * @param saml2EvidenceAssertion
     *
     */
    private static void addConsent(Assertion saml2EvidenceAssertion,
        SamlAuthzDecisionStatementEvidenceAssertionType targetEvidenceAssertion) {
        for (AttributeStatement saml2EvidenceAttributeStatement : saml2EvidenceAssertion.getAttributeStatements()) {
            List<Attribute> saml2EvidenceAttributes = saml2EvidenceAttributeStatement.getAttributes();

            for (Attribute saml2EvidenceAttribute : saml2EvidenceAttributes) {
                if (saml2EvidenceAttribute.getName().equals(ACCESS_CONSENT_POLICY_ATTRIBUTE_NAME)
                    && !saml2EvidenceAttribute.getAttributeValues().isEmpty()) {
                    XMLObject xmlObject = saml2EvidenceAttribute.getAttributeValues().get(0);
                    String accessConsent = xmlObject.getDOM().getTextContent();

                    targetEvidenceAssertion.getAccessConsentPolicy().add(accessConsent);
                } else if (saml2EvidenceAttribute.getName().equals(INSTANCE_ACCESS_CONSENT_POLICY_ATTRIBUTE_NAME)
                    && !saml2EvidenceAttribute.getAttributeValues().isEmpty()) {
                    XMLObject xmlObject = saml2EvidenceAttribute.getAttributeValues().get(0);
                    String instanceAccessConsent = xmlObject.getDOM().getTextContent();

                    targetEvidenceAssertion.getInstanceAccessConsentPolicy().add(instanceAccessConsent);
                }
            }
        }

    }

    /**
     * This method is used to construct HL7 PurposeOfUse Attribute, and adds it to the Assertion.
     *
     * @param attribute attribute
     * @param target target assertion
     */
    private static void populatePurposeOfUseAttribute(final Attribute attribute, final AssertionType target) {

        LOG.trace("Executing Saml2AssertionExtractor.populatePurposeOfUseAttribute...");

        if (!CollectionUtils.isEmpty(attribute.getAttributeValues()) && attribute.getAttributeValues().get(0) != null
            && !CollectionUtils.isEmpty(attribute.getAttributeValues().get(0).getOrderedChildren())) {

            CeType purposeOfUse = new CeType();
            XMLObject purposeOfUseAttribute = attribute.getAttributeValues().get(0);
            XMLObject purposeOfUseElement = purposeOfUseAttribute.getOrderedChildren().get(0);

            populateCeType((XSAny) purposeOfUseElement, purposeOfUse);
            target.setPurposeOfDisclosureCoded(purposeOfUse);
        }

        LOG.trace("end populatePurposeOfUseAttribute()");
    }

    /**
     * Initializes the assertion object to contain empty strings for all values. These are overwritten in the extraction
     * process with real values if they are available
     *
     * @param assertOut The Assertion element being written to
     */
    private static AssertionType initializeAssertion() {

        LOG.debug("Initializing Assertion to Default: ");
        AssertionType assertOut = new AssertionType();

        CeType purposeCoded = new CeType();
        UserType user = new UserType();
        PersonNameType userPerson = new PersonNameType();
        CeType userRole = new CeType();
        HomeCommunityType userHc = new HomeCommunityType();
        HomeCommunityType homeCom = new HomeCommunityType();
        SamlAuthnStatementType samlAuthnStatement = new SamlAuthnStatementType();
        SamlAuthzDecisionStatementType samlAuthzDecisionStatement = new SamlAuthzDecisionStatementType();
        SamlAuthzDecisionStatementEvidenceType samlAuthzDecisionStatementEvidence = new SamlAuthzDecisionStatementEvidenceType();
        SamlAuthzDecisionStatementEvidenceAssertionType samlAuthzDecisionStatementAssertion = new SamlAuthzDecisionStatementEvidenceAssertionType();
        SamlAuthzDecisionStatementEvidenceConditionsType samlAuthzDecisionStatementEvidenceConditions = new SamlAuthzDecisionStatementEvidenceConditionsType();
        SamlSignatureType samlSignature = new SamlSignatureType();
        SamlSignatureKeyInfoType samlSignatureKeyInfo = new SamlSignatureKeyInfoType();

        assertOut.setHomeCommunity(homeCom);
        homeCom.setHomeCommunityId(EMPTY_STRING);

        assertOut.getUniquePatientId().clear();

        user.setPersonName(userPerson);
        user.setOrg(userHc);
        user.setRoleCoded(userRole);
        assertOut.setUserInfo(user);
        assertOut.setPurposeOfDisclosureCoded(purposeCoded);

        userPerson.setGivenName(EMPTY_STRING);
        userPerson.setFamilyName(EMPTY_STRING);
        userPerson.setSecondNameOrInitials(EMPTY_STRING);
        userPerson.setFullName(EMPTY_STRING);

        userHc.setName(EMPTY_STRING);
        userHc.setHomeCommunityId(EMPTY_STRING);
        user.setUserName(EMPTY_STRING);
        userRole.setCode(EMPTY_STRING);
        userRole.setCodeSystem(EMPTY_STRING);
        userRole.setCodeSystemName(EMPTY_STRING);
        userRole.setDisplayName(EMPTY_STRING);

        purposeCoded.setCode(EMPTY_STRING);
        purposeCoded.setCodeSystem(EMPTY_STRING);
        purposeCoded.setCodeSystemName(EMPTY_STRING);
        purposeCoded.setDisplayName(EMPTY_STRING);

        assertOut.setSamlAuthnStatement(samlAuthnStatement);
        samlAuthnStatement.setAuthInstant(EMPTY_STRING);
        samlAuthnStatement.setSessionIndex(EMPTY_STRING);
        samlAuthnStatement.setAuthContextClassRef(EMPTY_STRING);
        samlAuthnStatement.setSubjectLocalityAddress(EMPTY_STRING);
        samlAuthnStatement.setSubjectLocalityDNSName(EMPTY_STRING);

        assertOut.setSamlAuthzDecisionStatement(samlAuthzDecisionStatement);
        samlAuthzDecisionStatement.setDecision(EMPTY_STRING);
        samlAuthzDecisionStatement.setResource(EMPTY_STRING);
        samlAuthzDecisionStatement.setAction(EMPTY_STRING);

        samlAuthzDecisionStatement.setEvidence(samlAuthzDecisionStatementEvidence);

        samlAuthzDecisionStatementEvidence.setAssertion(samlAuthzDecisionStatementAssertion);
        samlAuthzDecisionStatementAssertion.setId(EMPTY_STRING);
        samlAuthzDecisionStatementAssertion.setIssueInstant(EMPTY_STRING);
        samlAuthzDecisionStatementAssertion.setVersion(EMPTY_STRING);
        samlAuthzDecisionStatementAssertion.setIssuer(EMPTY_STRING);
        samlAuthzDecisionStatementAssertion.getAccessConsentPolicy().clear();
        samlAuthzDecisionStatementAssertion.getInstanceAccessConsentPolicy().clear();

        samlAuthzDecisionStatementAssertion.setConditions(samlAuthzDecisionStatementEvidenceConditions);
        samlAuthzDecisionStatementEvidenceConditions.setNotBefore(EMPTY_STRING);
        samlAuthzDecisionStatementEvidenceConditions.setNotOnOrAfter(EMPTY_STRING);

        byte[] formRaw = null;
        try {
            formRaw = EMPTY_STRING.getBytes(StringUtil.UTF8_CHARSET);
        } catch (UnsupportedEncodingException ex) {
            LOG.error("Error converting String to UTF8 format: {}", ex.getLocalizedMessage(), ex);
        }
        assertOut.setSamlSignature(samlSignature);
        samlSignature.setSignatureValue(formRaw);

        samlSignature.setKeyInfo(samlSignatureKeyInfo);
        samlSignatureKeyInfo.setRsaKeyValueExponent(formRaw);
        samlSignatureKeyInfo.setRsaKeyValueModulus(formRaw);

        return assertOut;
    }

    /**
     * This method is used to construct HL7 Subject Role Attribute, and adds it to the Assertion.
     *
     * @param attribute attribute
     * @param target target assertion
     */
    private static void populateSubjectRole(final Attribute attribute, final AssertionType target) {
        LOG.trace("Executing Saml2AssertionExtractor.populateSubjectRole...");

        if (!CollectionUtils.isEmpty(attribute.getAttributeValues()) && attribute.getAttributeValues().get(0) != null
            && !CollectionUtils.isEmpty(attribute.getAttributeValues().get(0).getOrderedChildren())) {
            XMLObject subjRoleAttribute = attribute.getAttributeValues().get(0);
            XMLObject roleElement = subjRoleAttribute.getOrderedChildren().get(0);

            populateCeType((XSAny) roleElement, target.getUserInfo().getRoleCoded());

        }
        LOG.trace("end populateSubjectRole()");
    }

    private static void populateCeType(XSAny samlAttrValElement, CeType ceType) {
        ceType.setCode("");
        ceType.setCodeSystem("");
        ceType.setCodeSystemName("");
        ceType.setDisplayName("");

        // check namespace and break out on mismatch...
        if (!samlAttrValElement.getElementQName().getNamespaceURI().equals(NhincConstants.HL7_NS)) {
            return;
        }

        AttributeMap attributeMap = samlAttrValElement.getUnknownAttributes();
        for (Map.Entry<QName, String> entry : attributeMap.entrySet()) {

            QName key = entry.getKey();

            if (key.getLocalPart().equals(NhincConstants.CE_CODE)) {
                ceType.setCode(String.valueOf(entry.getValue()));

            } else if (key.getLocalPart().equals(NhincConstants.CE_CODESYSTEM)) {
                ceType.setCodeSystem(String.valueOf(entry.getValue()));

            } else if (key.getLocalPart().equals(NhincConstants.CE_CODESYSTEM_NAME)) {
                ceType.setCodeSystemName(String.valueOf(entry.getValue()));

            } else if (key.getLocalPart().equals(NhincConstants.CE_DISPLAYNAME)) {
                ceType.setDisplayName(String.valueOf(entry.getValue()));
            }
        }
    }
}
