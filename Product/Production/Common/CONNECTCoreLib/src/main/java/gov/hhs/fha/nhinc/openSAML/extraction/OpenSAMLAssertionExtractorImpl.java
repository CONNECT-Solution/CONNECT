/**
 * 
 */
package gov.hhs.fha.nhinc.openSAML.extraction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.cxf.common.util.StringUtils;
import org.apache.log4j.Logger;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.saml.ext.OpenSAMLUtil;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.AuthzDecisionStatement;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.Evidence;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Subject;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSAny;
import org.opensaml.xml.util.AttributeMap;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
import gov.hhs.fha.nhinc.cxf.extraction.SAMLExtractorDOM;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;

/**
 * @author mweaver
 * 
 */
public class OpenSAMLAssertionExtractorImpl implements SAMLExtractorDOM {
    private static final Logger log = Logger.getLogger(OpenSAMLAssertionExtractorImpl.class);
    private static final String EMPTY_STRING = "";
    private static final String X509_FORMAT = "urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName";

    /**
     * This method is used to extract the SAML assertion information.
     * 
     * @param Element element
     * @return AssertionType
     */
    public final AssertionType extractSAMLAssertion(final Element element) {

        log.debug("Executing Saml2AssertionExtractor.extractSamlAssertion()...");
        Assertion saml2Assertion = extractSaml2Assertion(element);

        AssertionType target = initializeAssertion();
        // Populate the Subject Information
        populateSubject(saml2Assertion, target);
        // Populate the Authentication Statement Information.
        populateAuthenticationStatement(saml2Assertion, target);
        // Populate the Attribute Statement Information.
        populateAttributeStatement(saml2Assertion, target);
        // Populate the Authorization Decision Statement Information
        populateAuthzDecisionStatement(saml2Assertion, target);
        log.debug("end extractSamlAssertion()");

        return target;
    }

    /**
     * This method is used extract the saml2Assertion from Context.
     * 
     * @param context context
     * @return saml2 assertion from context
     */
    @SuppressWarnings("unchecked")
    private Assertion extractSaml2Assertion(final Element element) {
        Assertion assertion = null;
        NodeList list = element.getElementsByTagNameNS(SAMLConstants.SAML20_NS, Assertion.DEFAULT_ELEMENT_LOCAL_NAME);
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
     * @param saml2Assertion saml2 assertion
     * @param target target assertion
     */
    private void populateAttributeStatement(final Assertion saml2Assertion, final AssertionType target) {

        log.debug("Executing Saml2AssertionExtractor.populateAttributeStatement()...");
        AttributeHelper helper = new AttributeHelper();

        for (AttributeStatement attributeStatement : saml2Assertion.getAttributeStatements()) {
            for (Attribute attribute : attributeStatement.getAttributes()) {

                if (NhincConstants.USER_ROLE_ATTR.equals(attribute.getName())) {
                    log.debug("Extracting Assertion.userInfo.roleCoded:");
                    target.getUserInfo().setRoleCoded(
                            helper.extractNhinCodedElement(attribute, NhincConstants.USER_ROLE_ATTR));
                }

                else if (NhincConstants.PURPOSE_ROLE_ATTR.equals(attribute.getName())) {
                    log.debug("Extracting Assertion.purposeOfDisclosure:");
                    target.setPurposeOfDisclosureCoded(helper.extractNhinCodedElement(attribute,
                            NhincConstants.PURPOSE_ROLE_ATTR));
                }

                else if (NhincConstants.USERNAME_ATTR.equals(attribute.getName())) {
                    helper.extractNameParts(attribute, target);
                }

                else if (NhincConstants.USER_ORG_ATTR.equals(attribute.getName())) {
                    String sUserOrg = helper.extractAttributeValueString(attribute);
                    target.getUserInfo().getOrg().setName(sUserOrg);
                    log.debug("Assertion.userInfo.org.Name = " + sUserOrg);
                }

                else if (NhincConstants.USER_ORG_ID_ATTR.equals(attribute.getName())) {
                    String sUserOrgId = helper.extractAttributeValueString(attribute);
                    target.getUserInfo().getOrg().setHomeCommunityId(sUserOrgId);
                    log.debug("Assertion.userInfo.org.homeCommunityId = " + sUserOrgId);
                }

                else if (NhincConstants.HOME_COM_ID_ATTR.equals(attribute.getName())) {
                    String sHomeComId = helper.extractAttributeValueString(attribute);
                    target.getHomeCommunity().setHomeCommunityId(sHomeComId);
                    log.debug("Assertion.homeCommunity.homeCommunityId = " + sHomeComId);
                }

                else if (NhincConstants.PATIENT_ID_ATTR.equals(attribute.getName())) {
                    String sPatientId = helper.extractAttributeValueString(attribute);
                    target.getUniquePatientId().add(sPatientId);
                    log.debug("Assertion.uniquePatientId = " + sPatientId);
                }

                else if (NhincConstants.ACCESS_CONSENT_ATTR.equals(attribute.getName())) {
                    List<String> sAccessConsentId = transformXMLtoString(attribute.getAttributeValues());
                    target.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getAccessConsentPolicy()
                    .addAll(sAccessConsentId);
                    log.debug("Assertion.SamlAuthzDecisionStatement.Evidence.Assertion.AccessConsentPolicy = "
                            + sAccessConsentId);
                }

                else if (NhincConstants.INST_ACCESS_CONSENT_ATTR.equals(attribute.getName())) {
                    List<String> sInstAccessConsentId = transformXMLtoString(attribute.getAttributeValues());
                    target.getSamlAuthzDecisionStatement().getEvidence().getAssertion()
                    .getInstanceAccessConsentPolicy().addAll(sInstAccessConsentId);
                    log.debug("Assertion.SamlAuthzDecisionStatement.Evidence.Assertion.InstanceAccessConsentPolicy = "
                            + sInstAccessConsentId);
                }

                else if (NhincConstants.ATTRIBUTE_NAME_ORG.equals(attribute.getName())) {
                    String orgAttribute = getAttributeValue(attribute);
                    target.getUserInfo().getOrg().setName(orgAttribute);
                }

                else if (NhincConstants.ATTRIBUTE_NAME_ORG_ID.equals(attribute.getName())) {
                    String orgIDAttribute = getAttributeValue(attribute);
                    target.getUserInfo().getOrg().setHomeCommunityId(orgIDAttribute);
                }

                else if (NhincConstants.ATTRIBUTE_NAME_HCID.equals(attribute.getName())) {
                    HomeCommunityType hcidAttribute = new HomeCommunityType();
                    hcidAttribute.setHomeCommunityId(getAttributeValue(attribute));
                    target.setHomeCommunity(hcidAttribute);
                }

                else if (NhincConstants.ATTRIBUTE_NAME_SUBJECT_ROLE.equals(attribute.getName())) {
                    populateSubjectRole(attribute, target);
                }

                else if (NhincConstants.ATTRIBUTE_NAME_PURPOSE_OF_USE.equals(attribute.getName())) {
                    populatePurposeOfUseAttribute(attribute, target);
                }

                else if (NhincConstants.ATTRIBUTE_NAME_RESOURCE_ID.equals(attribute.getName())
                        && !StringUtils.isEmpty(attribute.getDOM().getTextContent())) {
                    target.getUniquePatientId().add(getAttributeValue(attribute));
                } else {
                    log.warn("Unrecognized Name Attribute: " + attribute.getName());
                }
            }
        }
        log.debug("end populateAttributeStatement()");
    }

    /**
     * @param attributeValues
     * @return The same list, with XMLObjects converted to a String representation
     */
    private List<String> transformXMLtoString(List<XMLObject> attributeValues) {
        List<String> stringList = new ArrayList<String>();
        for(XMLObject item : attributeValues){
            stringList.add(item.toString());
        }
        return stringList;
    }

    private String getAttributeValue(Attribute attribute) {
        return attribute.getAttributeValues().get(0).getDOM()
                .getTextContent();
    }

    /**
     * This method is used to populate the Authentication Statement Information.
     * 
     * @param saml2Assertion saml2 assertion
     * @param target target assertion
     */
    private void populateAuthenticationStatement(final Assertion saml2Assertion, final AssertionType target) {

        log.debug("Executing Saml2AssertionExtractor.populateAuthenticationStatement()...");
        SamlAuthnStatementType samlAuthnStatement = new SamlAuthnStatementType();
        AuthnStatement source = saml2Assertion.getAuthnStatements().get(0);
        samlAuthnStatement.setAuthInstant(source.getAuthnInstant().toString());
        samlAuthnStatement.setSessionIndex(source.getSessionIndex());
        samlAuthnStatement.setAuthContextClassRef(source.getAuthnContext().getAuthnContextClassRef()
                .getAuthnContextClassRef().toString());

        if (source.getSubjectLocality() != null) {
            samlAuthnStatement.setSubjectLocalityDNSName(source.getSubjectLocality().getDNSName());
            samlAuthnStatement.setSubjectLocalityAddress(source.getSubjectLocality().getAddress());
        }

        target.setSamlAuthnStatement(samlAuthnStatement);
        log.debug("end populateAuthenticationStatement()");
    }

    /**
     * This method is used to populate the Subject Information into the target assertion.
     * 
     * @param saml2Assertion saml2 assertion
     * @param target target assertion
     */
    private void populateSubject(final Assertion saml2Assertion, final AssertionType target) {
        log.debug("Executing Saml2AssertionExtractor.populateSubject()...");

        Subject subject = saml2Assertion.getSubject();
        NameID name = subject.getNameID();
        if (X509_FORMAT.equals(name.getFormat())) {
            log.warn("Subject name format is not X509!");
        }
        target.getUserInfo().setUserName(name.getValue());

        log.debug("end populateSubject()");
    }

    /**
     * This method is used to populate the Authorization Decision Statement Information.
     * 
     * @param saml2Assertion saml2 assertion
     * @param target target assertion
     */
    private void populateAuthzDecisionStatement(final Assertion saml2Assertion, final AssertionType target) {

        final String ACCESS_CONSENT_POLICY_ATTRIBUTE_NAME = "AccessConsentPolicy";
        final String INSTANCE_ACCESS_CONSENT_POLICY_ATTRIBUTE_NAME = "InstanceAccessConsentPolicy";

        log.debug("Executing Saml2AssertionExtractor.populateAuthzDecisionStatement()...");

        List<AuthzDecisionStatement> saml2AuthzDecisionStatements = saml2Assertion.getAuthzDecisionStatements();
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
        List<Assertion> saml2EvidenceAssertions = saml2Evidence.getAssertions();

        SamlAuthzDecisionStatementEvidenceType targetEvidence = new SamlAuthzDecisionStatementEvidenceType();
        targetAuthzDecisionStatement.setEvidence(targetEvidence);

        // Translate Evidence Assertion
        Assertion saml2EvidenceAssertion = saml2EvidenceAssertions.get(0);

        SamlAuthzDecisionStatementEvidenceAssertionType targetEvidenceAssertion = new SamlAuthzDecisionStatementEvidenceAssertionType();
        targetEvidence.setAssertion(targetEvidenceAssertion);

        // Translate Evidence Attribute Statement
        AttributeStatement saml2EvidenceAttributeStatement = saml2EvidenceAssertion.getAttributeStatements().get(0);
        List<Attribute> saml2EvidenceAttributes = saml2EvidenceAttributeStatement.getAttributes();

        for (Attribute saml2EvidenceAttribute : saml2EvidenceAttributes) {
            if (saml2EvidenceAttribute.getName().equals(ACCESS_CONSENT_POLICY_ATTRIBUTE_NAME)) {
                XMLObject xmlObject = saml2EvidenceAttribute.getAttributeValues().get(0);
                String accessConsent = xmlObject.getDOM().getTextContent();

                targetEvidenceAssertion.getAccessConsentPolicy().add(accessConsent);
            } else if (saml2EvidenceAttribute.getName().equals(INSTANCE_ACCESS_CONSENT_POLICY_ATTRIBUTE_NAME)) {
                XMLObject xmlObject = saml2EvidenceAttribute.getAttributeValues().get(0);
                String instanceAccessConsent = xmlObject.getDOM().getTextContent();

                targetEvidenceAssertion.getInstanceAccessConsentPolicy().add(instanceAccessConsent);
            }
        }

        // Translate Evidence Conditions
        Conditions saml2EvidenceCondition = saml2EvidenceAssertion.getConditions();

        SamlAuthzDecisionStatementEvidenceConditionsType targetConditions = new SamlAuthzDecisionStatementEvidenceConditionsType();
        targetEvidenceAssertion.setConditions(targetConditions);

        targetConditions.setNotBefore(saml2EvidenceCondition.getNotBefore().toString());
        targetConditions.setNotOnOrAfter(saml2EvidenceCondition.getNotOnOrAfter().toString());

        // Translate Evidence Issuer
        Issuer saml2EvidenceIssuer = saml2EvidenceAssertion.getIssuer();

        targetEvidenceAssertion.setIssuerFormat(saml2EvidenceIssuer.getFormat());
        targetEvidenceAssertion.setIssuer(saml2EvidenceIssuer.getValue());

        log.debug("end populateAuthzDecisionStatement()");
    }

    /**
     * This method is used to construct HL7 PurposeOfUse Attribute, and adds it to the Assertion.
     * 
     * @param attribute attribute
     * @param target target assertion
     */
    private void populatePurposeOfUseAttribute(final Attribute attribute, final AssertionType target) {

        log.debug("Executing Saml2AssertionExtractor.populatePurposeOfUseAttribute...");

        XMLObject purposeOfUseAttribute = attribute.getAttributeValues().get(0);
        AttributeMap attributeMap = ((XSAny) purposeOfUseAttribute.getOrderedChildren().get(0)).getUnknownAttributes();

        String code = "";
        String codeSystem = "";
        String codeSystemName = "";
        String displayName = "";

        for (Map.Entry<QName, String> entry : attributeMap.entrySet()) {
            QName key = entry.getKey();
            if (NhincConstants.HL7_NS.equals(key.getNamespaceURI())) {
                // Validate PurposeOfUse Code
                if (key.getLocalPart().equals(NhincConstants.CE_CODE)) {
                    code = String.valueOf(entry.getValue());
                }
                // Validate the code system
                if (key.getLocalPart().equals(NhincConstants.CE_CODESYSTEM)) {
                    codeSystem = String.valueOf(entry.getValue());
                }
                // Validate the code system name
                if (key.getLocalPart().equals(NhincConstants.CE_CODESYSTEM_NAME)) {
                    codeSystemName = String.valueOf(entry.getValue());
                }
                // Validate the code system name
                if (key.getLocalPart().equals(NhincConstants.CE_DISPLAYNAME)) {
                    displayName = String.valueOf(entry.getValue());
                }
            }
        }

        CeType purposeOfUse = new CeType();
        purposeOfUse.setCode(code);
        purposeOfUse.setCodeSystem(codeSystem);
        purposeOfUse.setCodeSystemName(codeSystemName);
        purposeOfUse.setDisplayName(displayName);
        target.setPurposeOfDisclosureCoded(purposeOfUse);
        log.debug("end populatePurposeOfUseAttribute()");
    }

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
        samlAuthzDecisionStatementAssertion.getAccessConsentPolicy().clear();
        samlAuthzDecisionStatementAssertion.getInstanceAccessConsentPolicy().clear();

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

    /**
     * This method is used to construct HL7 Subject Role Attribute, and adds it to the Assertion.
     * 
     * @param attribute attribute
     * @param target target assertion
     */
    private void populateSubjectRole(final Attribute attribute, final AssertionType target) {

        log.debug("Executing Saml2AssertionExtractor.populateSubjectRole...");

        XMLObject subjRoleAttribute = attribute.getAttributeValues().get(0);
        AttributeMap attributeMap = ((XSAny) subjRoleAttribute.getOrderedChildren().get(0))
                .getUnknownAttributes();

        String code = "";
        String codeSystem = "";
        String codeSystemName = "";
        String displayName = "";

        for (Map.Entry<QName, String> entry : attributeMap.entrySet()) {
            QName key = entry.getKey();
            if (key.getNamespaceURI().equals(NhincConstants.HL7_NS)) {
                // Validate Code
                if (key.getLocalPart().equals(NhincConstants.CE_CODE)) {
                    code = String.valueOf(entry.getValue());
                }
                // Validate Code System
                if (key.getLocalPart().equals(NhincConstants.CE_CODESYSTEM)) {
                    codeSystem = String.valueOf(entry.getValue());
                }
                // Validate CodeSystem Name
                if (key.getLocalPart().equals(NhincConstants.CE_CODESYSTEM_NAME)) {
                    codeSystemName = String.valueOf(entry.getValue());
                }
                // Validate the display Name
                if (key.getLocalPart().equals(NhincConstants.CE_DISPLAYNAME)) {
                    displayName = String.valueOf(entry.getValue());
                }
            }
        }

        CeType role = target.getUserInfo().getRoleCoded();
        role.setCode(code);
        role.setCodeSystem(codeSystem);
        role.setCodeSystemName(codeSystemName);
        role.setDisplayName(displayName);

        log.debug("end populateSubjectRole()");
    }
}
