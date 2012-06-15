/**
 * 
 */
package gov.hhs.fha.nhinc.openSAML.extraction;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthnStatementType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementEvidenceAssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementEvidenceConditionsType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementEvidenceType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlSignatureKeyInfoType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlSignatureType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.cxf.extraction.SAML2AssertionExtractor;
import gov.hhs.fha.nhinc.cxf.extraction.SAMLExtractorDOM;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.openSAML.extraction.AttributeHelper;

import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.saml.ext.OpenSAMLUtil;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.AuthzDecisionStatement;
import org.opensaml.saml2.core.Evidence;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSAny;
import org.opensaml.xml.util.AttributeMap;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author mweaver
 *
 */
public class OpenSAMLAssertionExtractorImpl implements SAMLExtractorDOM {
    private static final Logger log = Logger.getLogger(OpenSAMLAssertionExtractorImpl.class);
    private static final String EMPTY_STRING = "";
    
    /**
     * This method is used to extract the SAML assertion information.
     * 
     * @param Element
     *            element
     * @return AssertionType
     */
    public final AssertionType extractSAMLAssertion(final Element element) {

        log.debug("Executing Saml2AssertionExtractor.extractSamlAssertion()...");
        Assertion saml2Assertion = extractSaml2Assertion(element);

        AssertionType target = initializeAssertion();
        // Populate the Subject Information
        //populateSubject(saml2Assertion, target);
        // Populate the Authentication Statement Information.
        //populateAuthenticationStatement(saml2Assertion, target);
        // Populate the Attribute Statement Information.
        populateAttributeStatement(saml2Assertion, target);
        // Populate the Authorization Decision Statement Information
        //populateAuthzDecisionStatement(saml2Assertion, target);
        log.debug("end extractSamlAssertion()");

        return target;
    }

    /**
     * This method is used extract the saml2Assertion from Context.
     * 
     * @param context
     *            context
     * @return saml2 assertion from context
     */
    @SuppressWarnings("unchecked")
    private Assertion extractSaml2Assertion(final Element element) {
        Assertion assertion = null;
        

        NodeList list = element.getElementsByTagNameNS(SAMLConstants.SAML20_NS,
                Assertion.DEFAULT_ELEMENT_LOCAL_NAME);
        Element el = (Element) list.item(0);
        try {
            XMLObject xmlObj = OpenSAMLUtil.fromDom(el);

            if (xmlObj instanceof org.opensaml.saml2.core.Assertion) {
                assertion = (Assertion) xmlObj;
            }
        } catch (WSSecurityException e) {
            log.error("error extracting SAML assertion", e);
        }

        return assertion;
    }
    
    /**
     * This method is used to populate the Attribute Statement.
     * 
     * @param saml2Assertion
     *            saml2 assertion
     * @param target
     *            target assertion
     */
    private void populateAttributeStatement(final Assertion saml2Assertion, final AssertionType target) {

        log.debug("Executing Saml2AssertionExtractor.populateAttributeStatement()...");
        AttributeHelper helper = new AttributeHelper();

        for (AttributeStatement attributeStatement : saml2Assertion.getAttributeStatements()) {

            for (Attribute attribute : attributeStatement.getAttributes()) {

                /*if (NhincConstants.USER_ROLE_ATTR.equals(attribute.getName())) {
                    SubjectIDAttributeType subjectIDAttribute = new SubjectIDAttributeType();
                    subjectIDAttribute.setAttributeValue(attribute.getAttributeValues().get(0).getDOM()
                            .getTextContent());
                    attributeStatements.setSubjectIDAttribute(subjectIDAttribute);
                } else if (NhincConstants.PURPOSE_ROLE_ATTR.equals(attribute.getName()) {
                    log.debug("Extracting Assertion.purposeOfDisclosure:");
                    target.setPurposeOfDisclosureCoded(AttributeTypeHelper.extractNhinCodedElement(attrib,
                            NhincConstants.PURPOSE_ROLE_ATTR));
                } else if (NhincConstants.USERNAME_ATTR.equals(attribute.getName())) {
                    AttributeTypeHelper.extractNameParts(attrib, target);
                } else if (NhincConstants.USER_ORG_ATTR.equals(attribute.getName())) {
                    String sUserOrg = AttributeTypeHelper.extractAttributeValueString(attrib);
                    target.getUserInfo().getOrg().setName(sUserOrg);
                    log.debug("Assertion.userInfo.org.Name = " + sUserOrg);
                } else if (NhincConstants.USER_ORG_ID_ATTR.equals(attribute.getName())) {
                    String sUserOrgId = AttributeTypeHelper.extractAttributeValueString(attrib);
                    target.getUserInfo().getOrg().setHomeCommunityId(sUserOrgId);
                    log.debug("Assertion.userInfo.org.homeCommunityId = " + sUserOrgId);
                } else*/ if (NhincConstants.HOME_COM_ID_ATTR.equals(attribute.getName())) {
                    String sHomeComId = helper.extractAttributeValueString(attribute);
                    target.getHomeCommunity().setHomeCommunityId(sHomeComId);
                    log.debug("Assertion.homeCommunity.homeCommunityId = " + sHomeComId);
                } /*else if (NhincConstants.PATIENT_ID_ATTR.equals(attribute.getName())) {
                    String sPatientId = AttributeTypeHelper.extractAttributeValueString(attrib);
                    target.getUniquePatientId().add(sPatientId);
                    log.debug("Assertion.uniquePatientId = " + sPatientId);
                } else if (NhincConstants.ACCESS_CONSENT_ATTR.equals(attribute.getName())) {
                    List sAccessConsentId = attrib.getAttributeValue();
                    target.getSamlAuthzDecisionStatement().getEvidence().getAssertion()
                            .getAccessConsentPolicy().addAll(sAccessConsentId);
                    log.debug("Assertion.SamlAuthzDecisionStatement.Evidence.Assertion.AccessConsentPolicy = "
                            + sAccessConsentId);
                } else if (NhincConstants.INST_ACCESS_CONSENT_ATTR.equals(attribute.getName())) {
                    List sInstAccessConsentId = attrib.getAttributeValue();
                    target.getSamlAuthzDecisionStatement().getEvidence().getAssertion()
                            .getInstanceAccessConsentPolicy().addAll(sInstAccessConsentId);
                    log.debug("Assertion.SamlAuthzDecisionStatement.Evidence.Assertion.InstanceAccessConsentPolicy = "
                            + sInstAccessConsentId);
                } else {
                    log.warn("Unrecognized Name Attribute: " + nameAttr);
                }
                if (NhinSecurityConstants.ATTRIBUTE_NAME_ORG.equals(attribute.getName())) {
                    OrganizationAttributeType orgAttribute = new OrganizationAttributeType();
                    orgAttribute.setAttributeValue(attribute.getAttributeValues().get(0).getDOM().getTextContent());
                    attributeStatements.setOrganizationAttribute(orgAttribute);
                }
                if (NhinSecurityConstants.ATTRIBUTE_NAME_ORG_ID.equals(attribute.getName())) {
                    OrganizationIDAttributeType orgIDAttribute = new OrganizationIDAttributeType();
                    orgIDAttribute.setAttributeValue(attribute.getAttributeValues().get(0).getDOM().getTextContent());
                    attributeStatements.setOrganizationIDAttribute(orgIDAttribute);
                }
                if (NhinSecurityConstants.ATTRIBUTE_NAME_HCID.equals(attribute.getName())) {
                    HomeCommunityIDAttributeType hcid = new HomeCommunityIDAttributeType();
                    hcid.setAttributeValue(attribute.getAttributeValues().get(0).getDOM().getTextContent());
                    attributeStatements.setHomeCommunityIDAttribute(hcid);
                }
                if (NhinSecurityConstants.ATTRIBUTE_NAME_SUBJECT_ROLE.equals(attribute.getName())) {
                    populateSubjectRole(attribute, target);
                }
                if (NhinSecurityConstants.ATTRIBUTE_NAME_PURPOSE_OF_USE.equals(attribute.getName())) {
                    populatePurposeOfUseAttribute(attribute, target);
                }
                if (NhinSecurityConstants.ATTRIBUTE_NAME_RESOURCE_ID.equals(attribute.getName())
                        && StringUtils.isNotBlank(attribute.getDOM().getTextContent())) {
                    ResourceIDAttributeType resourceIDAttribute = new ResourceIDAttributeType();
                    resourceIDAttribute.setAttributeValue(attribute.getAttributeValues().get(0).getDOM()
                            .getTextContent());
                    attributeStatements.setResourceIDAttribute(resourceIDAttribute);
                }

                if (NhinSecurityConstants.ATTRIBUTE_NAME_NPI.equals(attribute.getName())
                        && StringUtils.isNotBlank(attribute.getDOM().getTextContent())) {
                    NPIAttributeType npi = new NPIAttributeType();
                    npi.setAttributeValue(attribute.getAttributeValues().get(0).getDOM().getTextContent());
                    attributeStatements.setNPIAttribute(npi);
                }*/
            }
        }
        log.debug("end populateAttributeStatement()");
    }

    /**
     * This method is used to populate the Authentication Statement Information.
     * 
     * @param saml2Assertion
     *            saml2 assertion
     * @param target
     *            target assertion
     */
    /*private void populateAuthenticationStatement(final Assertion saml2Assertion, final AssertionType target) {

        LOGGER.debug("Executing Saml2AssertionExtractor.populateAuthenticationStatement()...");
        AuthnStatementType authnStatement = new AuthnStatementType();
        AuthnStatement source = saml2Assertion.getAuthnStatements().get(0);

        // authnStatement.setAuthInstant(source.getAuthnInstant().toString());
        DatatypeFactory dtf = null;
        try {
            dtf = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
            throw new NhinServiceException(ErrorConstants.GENERAL_ERROR,
                    "Error during DatatypeFactory instantiation ");
        }
        authnStatement.setAuthInstant(dtf.newXMLGregorianCalendar(source.getAuthnInstant()
                .toGregorianCalendar()));

        authnStatement.setSessionIndex(source.getSessionIndex());

        authnStatement.setAuthnContextClassRef(source.getAuthnContext().getAuthnContextClassRef()
                .getAuthnContextClassRef().toString());

        if (source.getSubjectLocality() != null) {
            SubjectLocalityType subjectLocality = new SubjectLocalityType();
            subjectLocality.setDNSName(source.getSubjectLocality().getDNSName());
            subjectLocality.setAddress(source.getSubjectLocality().getAddress());
            authnStatement.setSubjectLocality(subjectLocality);
        }

        target.setAuthnStatementType(authnStatement);
        LOGGER.debug("end populateAuthenticationStatement()");
    }*/

    /**
     * This method is used to populate the Subject Information into the target
     * assertion.
     * 
     * @param saml2Assertion
     *            saml2 assertion
     * @param assertion
     *            target assertion
     */
    /*private void populateSubject(final Assertion saml2Assertion, final AssertionType assertion) {

        LOGGER.debug("Executing Saml2AssertionExtractor.populateSubject()...");
        SubjectType subject = new SubjectType();
        subject.setNameID(saml2Assertion.getSubject().getNameID().getDOM().getTextContent());
        subject.setNameIDFormat(saml2Assertion.getSubject().getNameID().getFormat());
        assertion.setSubject(subject);
        LOGGER.debug("end populateSubject()");
    }*/

    /**
     * This method is used to populate the Authorization Decision Statement
     * Information.
     * 
     * @param saml2Assertion
     *            saml2 assertion
     * @param target
     *            target assertion
     */
    /*private void populateAuthzDecisionStatement(final Assertion saml2Assertion, final AssertionType target) {

        LOGGER.debug("Executing Saml2AssertionExtractor.populateAuthzDecisionStatement()...");
        AuthzDecisionStatementType targetAuthzStmt = new AuthzDecisionStatementType();
        AuthzDecisionStatementEvidenceAssertionType targetEvidenceAssertion = new AuthzDecisionStatementEvidenceAssertionType();

        List<AuthzDecisionStatement> authzDecisionStatements = saml2Assertion.getAuthzDecisionStatements();
        // If there is no AuthzDecision Statement, return
        if (authzDecisionStatements == null || authzDecisionStatements.size() < 1) {
            return;
        }

        AuthzDecisionStatement authzDecisionStatement = authzDecisionStatements.get(0);
        targetAuthzStmt.setAction(authzDecisionStatement.getActions().get(0).getAction());
        targetAuthzStmt.setResource(authzDecisionStatement.getResource());
        targetAuthzStmt.setDecision(transformAuthzDecision(authzDecisionStatement.getDecision().toString()));

        Evidence evidence = authzDecisionStatement.getEvidence();
        InstanceAccessConsentPolicyAttributeType instanceACP = new InstanceAccessConsentPolicyAttributeType();
        AccessConsentPolicyAttributeType acp = new AccessConsentPolicyAttributeType();

        List<Assertion> assertions = evidence.getAssertions();

        for (Assertion evidenceAssertion : assertions) {
            List<AttributeStatement> attributeStatements = evidenceAssertion.getAttributeStatements();
            AttributeStatement attributeStatement = attributeStatements.get(0);
            List<Attribute> attributes = attributeStatement.getAttributes();

            for (Attribute attribute : attributes) {
                if (attribute.getName().equals(NhinSecurityConstants.ACCESS_CONSENT_POLICY_ATTRIBUTE_NAME)) {
                    XMLObject xmlObject = attribute.getAttributeValues().get(0);
                    // urn:oid:1.2.3.4
                    String accessConsent = xmlObject.getDOM().getTextContent();
                    acp.setAttributeValue(accessConsent);
                    targetEvidenceAssertion
                            .getAccessConsentPolicyAttributeOrInstanceAccessConsentPolicyAttribute().add(acp);
                } else if (attribute.getName().equals(
                        NhinSecurityConstants.INSTANCE_ACCESS_CONSENT_POLICY_ATTRIBUTE_NAME)) {
                    XMLObject xmlObject = attribute.getAttributeValues().get(0);
                    // urn:oid:1.2.3.4.123456789
                    String instanceAccessConsent = xmlObject.getDOM().getTextContent();
                    instanceACP.setAttributeValue(instanceAccessConsent);
                    targetEvidenceAssertion
                            .getAccessConsentPolicyAttributeOrInstanceAccessConsentPolicyAttribute().add(
                                    instanceACP);
                }
            }

        }
        targetAuthzStmt.getAuthzDecisionStatementEvidence().add(targetEvidenceAssertion);

        // There should only be one AuthzDecisionStatement inside the SAML 2.0
        // assertion
        target.setAuthzDecisionStatement(targetAuthzStmt);
        LOGGER.debug("end populateAuthzDecisionStatement()");
    }*/

    /**
     * Transform the authz decision value.
     * 
     * @param string
     *            decision value
     * @return decision type
     */
    /*private DecisionType transformAuthzDecision(final String string) {
        return DecisionType.fromValue(string);
    }*/
    /**
     * This method is used to construct HL7 Subject Role Attribute, and adds it
     * to the Assertion.
     * 
     * @param attribute
     *            attribute
     * @param target
     *            target assertion
     */
    /*private void populateSubjectRole(final Attribute attribute, final AssertionType target) {

        LOGGER.debug("Executing Saml2AssertionExtractor.populateSubjectRole...");
        SubjectRoleAttributeType targetSubjRoleAttribute = new SubjectRoleAttributeType();

        XMLObject subjRoleAttribute = attribute.getAttributeValues().get(0);
        AttributeMap attributeMap = ((XSAny) subjRoleAttribute.getOrderedChildren().get(0))
                .getUnknownAttributes();

        String code = "";
        String codeSystem = "";
        String codeSystemName = "";
        String displayName = "";

        for (Map.Entry<QName, String> entry : attributeMap.entrySet()) {
            QName key = (QName) entry.getKey();

            if (key.getNamespaceURI().equals(NhinSecurityConstants.HL7_NS)) {

                // Validate Code
                if (key.getLocalPart().equals(NhinSecurityConstants.CE_CODE)) {

                    code = String.valueOf(entry.getValue());

                }

                // Validate Code System
                if (key.getLocalPart().equals(NhinSecurityConstants.CE_CODESYSTEM)) {

                    codeSystem = String.valueOf(entry.getValue());

                }

                // Validate CodeSystem Name
                if (key.getLocalPart().equals(NhinSecurityConstants.CE_CODESYSTEM_NAME)) {

                    codeSystemName = String.valueOf(entry.getValue());

                }

                // Validate the display Name
                if (key.getLocalPart().equals(NhinSecurityConstants.CE_DISPLAYNAME)) {

                    displayName = String.valueOf(entry.getValue());

                }
            }
        }

        CeType role = new CeType();
        role.setCode(code);
        role.setCodeSystem(codeSystem);
        role.setCodeSystemName(codeSystemName);
        role.setDisplayName(displayName);

        targetSubjRoleAttribute.setRole(role);
        target.getAttributeStatement().setSubjectRoleAttribute(targetSubjRoleAttribute);
        LOGGER.debug("end populateSubjectRole()");
    }*/

    /**
     * This method is used to construct HL7 PurposeOfUse Attribute, and adds it
     * to the Assertion.
     * 
     * @param attribute
     *            attribute
     * @param target
     *            target assertion
     */
    /*private void populatePurposeOfUseAttribute(final Attribute attribute, final AssertionType target) {

        LOGGER.debug("Executing Saml2AssertionExtractor.populatePurposeOfUseAttribute...");
        PurposeOfUseAttributeType targetPurposeOfUseAttribute = new PurposeOfUseAttributeType();

        XMLObject purposeOfUseAttribute = attribute.getAttributeValues().get(0);
        AttributeMap attributeMap = ((XSAny) purposeOfUseAttribute.getOrderedChildren().get(0))
                .getUnknownAttributes();

        String code = "";
        String codeSystem = "";
        String codeSystemName = "";
        String displayName = "";

        for (Map.Entry<QName, String> entry : attributeMap.entrySet()) {
            QName key = (QName) entry.getKey();

            if (NhinSecurityConstants.HL7_NS.equals(key.getNamespaceURI())) {

                // Validate PurposeOfUse Code
                if (key.getLocalPart().equals(NhinSecurityConstants.CE_CODE)) {
                    code = String.valueOf(entry.getValue());

                }

                // Validate the code system
                if (key.getLocalPart().equals(NhinSecurityConstants.CE_CODESYSTEM)) {
                    codeSystem = String.valueOf(entry.getValue());

                }

                // Validate the code system name
                if (key.getLocalPart().equals(NhinSecurityConstants.CE_CODESYSTEM_NAME)) {

                    codeSystemName = String.valueOf(entry.getValue());
                }

                // Validate the code system name
                if (key.getLocalPart().equals(NhinSecurityConstants.CE_DISPLAYNAME)) {

                    displayName = String.valueOf(entry.getValue());
                }
            }
        }

        CeType purposeOfUse = new CeType();
        purposeOfUse.setCode(code);
        purposeOfUse.setCodeSystem(codeSystem);
        purposeOfUse.setCodeSystemName(codeSystemName);
        purposeOfUse.setDisplayName(displayName);
        targetPurposeOfUseAttribute.setPurposeOfUse(purposeOfUse);
        target.getAttributeStatement().setPurposeOfUseAttribute(targetPurposeOfUseAttribute);
        LOGGER.debug("end populatePurposeOfUseAttribute()");
    }*/
    /**
     * Initializes the assertion object to contain empty strings for all values. These are overwritten in the extraction
     * process with real values if they are available
     * 
     * @param assertOut The Assertion element being written to
     */
    private AssertionType initializeAssertion() {

        log.debug("Initializing Assertion to Default: " + EMPTY_STRING);
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
        // samlAuthzDecisionStatementAssertion
        // .setAccessConsentPolicy(EMPTY_STRING);
        // samlAuthzDecisionStatementAssertion
        // .setInstanceAccessConsentPolicy(EMPTY_STRING);

        samlAuthzDecisionStatementAssertion.setConditions(samlAuthzDecisionStatementEvidenceConditions);
        samlAuthzDecisionStatementEvidenceConditions.setNotBefore(EMPTY_STRING);
        samlAuthzDecisionStatementEvidenceConditions.setNotOnOrAfter(EMPTY_STRING);

        byte[] formRaw = EMPTY_STRING.getBytes();
        assertOut.setSamlSignature(samlSignature);
        samlSignature.setSignatureValue(formRaw);

        samlSignature.setKeyInfo(samlSignatureKeyInfo);
        samlSignatureKeyInfo.setRsaKeyValueExponent(formRaw);
        samlSignatureKeyInfo.setRsaKeyValueModulus(formRaw);

        return assertOut;
    }

}
