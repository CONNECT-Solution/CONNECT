/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.saml.extraction;

//import org.apache.xerces.dom.ElementNSImpl;
import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;
import com.sun.xml.wss.SubjectAccessor;
import com.sun.xml.wss.XWSSecurityException;
import com.sun.xml.wss.impl.XWSSecurityRuntimeException;
import com.sun.xml.wss.saml.SAMLAssertionFactory;
import com.sun.xml.wss.saml.SAMLException;
import com.sun.xml.wss.saml.internal.saml20.jaxb20.AttributeStatementType;
import com.sun.xml.wss.saml.internal.saml20.jaxb20.AttributeType;
import com.sun.xml.wss.saml.internal.saml20.jaxb20.ConditionsType;
import com.sun.xml.wss.saml.internal.saml20.jaxb20.EvidenceType;
import com.sun.xml.wss.saml.internal.saml20.jaxb20.AuthnStatementType;
import com.sun.xml.wss.saml.internal.saml20.jaxb20.AuthzDecisionStatementType;
import com.sun.xml.wss.saml.internal.saml20.jaxb20.NameIDType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;

import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthnStatementType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementEvidenceAssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementEvidenceType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementEvidenceConditionsType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlSignatureKeyInfoType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlSignatureType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.security.auth.Subject;
import javax.security.auth.x500.X500Principal;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.stream.XMLStreamReader;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import sun.security.x509.AVA;
import sun.security.x509.X500Name;

/**
 *
 * @author Jon Hoppesch
 */
public class SamlTokenExtractor {

    private static Log log = LogFactory.getLog(SamlTokenExtractor.class);
    private static final String X509_FORMAT = "urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName";
    private static final String EMPTY_STRING = "";

    public static AssertionType GetAssertion(WebServiceContext context) {
        log.debug("Entering SamlTokenExtractor.GetAssertion...");

        AssertionType assertion = initializeAssertion();

        try {
            Subject subj = SubjectAccessor.getRequesterSubject(context);
            Set<Object> set = subj.getPublicCredentials();
            for (Object obj : set) {
                if (obj instanceof XMLStreamReader) {
                    XMLStreamReader reader = (XMLStreamReader) obj;
                    assertion = SamlTokenExtractor.CreateAssertion(reader);
                }
            }
        } catch (XWSSecurityException ex) {
            log.error("Error extracting the SAML Token: " + ex);
            throw new XWSSecurityRuntimeException(ex);
        }

        log.debug("Exiting SamlTokenExtractor.GetAssertion...");
        return assertion;
    }

    public static AssertionType CreateAssertion(XMLStreamReader reader) {
        log.debug("Entering SamlTokenExtractor.CreateAssertion...");

        AssertionType assertion = initializeAssertion();

        try {
            SAMLAssertionFactory factory = SAMLAssertionFactory.newInstance(SAMLAssertionFactory.SAML2_0);
            com.sun.xml.wss.saml.Assertion assertIn = factory.createAssertion(reader);

            // If we know the underlying type, we can get to more of the data we are looking for.
            //------------------------------------------------------------------------------------
            if (assertIn instanceof com.sun.xml.wss.saml.internal.saml20.jaxb20.AssertionType) {
                com.sun.xml.wss.saml.internal.saml20.jaxb20.AssertionType assertType = (com.sun.xml.wss.saml.internal.saml20.jaxb20.AssertionType) assertIn;
                extractSubject(assertType, assertion);
            }

            List statements = assertIn.getStatements();
            if (statements != null && !statements.isEmpty()) {
                for (int idx = 0; idx < statements.size(); idx++) {
                    if (statements.get(idx) instanceof AttributeStatementType) {
                        AttributeStatementType statement = (AttributeStatementType) statements.get(idx);
                        extractAttributeInfo(statement, assertion);
                    } else if (statements.get(idx) instanceof AuthzDecisionStatementType) {
                        AuthzDecisionStatementType statement = (AuthzDecisionStatementType) statements.get(idx);
                        extractDecisionInfo(statement, assertion);
                    } else if (statements.get(idx) instanceof AuthnStatementType) {
                        AuthnStatementType statement = (AuthnStatementType) statements.get(idx);
                        extractAuthnStatement(statement, assertion);
                    } else {
                        log.warn("Unknown statement type: " + statements.get(idx));
                    }
                }
            } else {
                log.error("There were no statements specified in the SAML token.");
                assertion = null;
            }
        } catch (SAMLException ex) {
            log.error("SAML Exception Thrown: " + ex.getMessage());
            assertion = null;
        } catch (XWSSecurityException ex) {
            log.error("Security Exception Thrown: " + ex.getMessage());
            assertion = null;
        }

        log.debug("Exiting SamlTokenExtractor.CreateAssertion");
        return assertion;
    }

    /**
     * Initializes the assertion object to contain empty strings for all values.
     * These are overwritten in the extraction process with real values if they
     * are available
     * @param assertOut The Assertion element being written to
     */
    private static AssertionType initializeAssertion() {

        System.out.println("Initializing Assertion to Default: " + EMPTY_STRING);
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
        samlAuthzDecisionStatementAssertion.setAccessConsentPolicy(EMPTY_STRING);
        samlAuthzDecisionStatementAssertion.setInstanceAccessConsentPolicy(EMPTY_STRING);

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
     * The Subject element contains the user identification information.  It is 
     * expected to follow an X509 format, but in the case that this is not 
     * proven out it will save the entire contents as being the userid.
     * @param assertType The saml assertion type being read from
     * @param assertOut The Assertion element being written to
     */
    private static void extractSubject(com.sun.xml.wss.saml.internal.saml20.jaxb20.AssertionType assertType, AssertionType assertOut) {
        log.debug("Entering SamlTokenExtractor.extractSubject...");

        UserType userInfo = assertOut.getUserInfo();

        if (assertType.getSubject() != null) {
            List<JAXBElement<?>> contents = assertType.getSubject().getContent();
            if (contents != null && !contents.isEmpty()) {
                for (JAXBElement jaxElem : contents) {
                    if (jaxElem.getValue() instanceof NameIDType) {
                        NameIDType nameId = (NameIDType) jaxElem.getValue();
                        String format = nameId.getFormat();
                        String nameVal = nameId.getValue();

                        // For X509 format the user identifier is extracted, for others content is taken as is
                        String userIdentifier = nameVal;
                        if (NullChecker.isNotNullish(format) && NullChecker.isNotNullish(nameVal)) {
                            if (format.trim().equals(X509_FORMAT)) {
                                String extractedUID = extract509(nameVal);
                                if (NullChecker.isNotNullish(extractedUID)) {
                                    userIdentifier = extractedUID;
                                } else {
                                    log.warn("X509 Formatted user identifier cannot be extracted");
                                }
                            }
                        } else {
                            log.warn("Subject's NameId has an invalid format");
                        }
                        log.info("Setting UserName to: " + userIdentifier);
                        userInfo.setUserName(userIdentifier);
                        assertOut.setUserInfo(userInfo);
                    }
                }
            } else {
                log.warn("Expected Subject information is missing.");
            }
        } else {
            log.warn("Subject element is missing.");
        }

        log.debug("Exiting SamlTokenExtractor.extractSubject...");
    }

    /**
     * Extracts the value of the UID object identifier of the X509 formatted 
     * content.  This method uses Sun proprietary classes to determine if the 
     * X509 is properly formed and to extract the value of the UID. The current 
     * specification for the string representation of a distinguished name is 
     * defined in <a href="http://www.ietf.org/rfc/rfc2253.txt">RFC 2253</a>. 
     * @param in509 The X509 formatted string
     * @return The extracted userid value, null if not defined.
     */
    private static String extract509(String in509) {
        log.debug("Entering SamlTokenExtractor.extract509...");

        String userVal = null;

        if (NullChecker.isNotNullish(in509)) {
            try {
                X500Principal prin = new X500Principal(in509);
                X500Name name500 = X500Name.asX500Name(prin);
                for (AVA ava : name500.allAvas()) {
                    if (X500Name.userid_oid == ava.getObjectIdentifier()) {
                        userVal = ava.getValueString();
                    } else {
                        log.warn("Construction of user identifier does not use: " + ava.toString());
                    }
                }
            } catch (IllegalArgumentException iae) {
                log.error("X509 NameId is not properly formed: " + iae.getMessage());
            }
        }

        log.debug("Exiting SamlTokenExtractor.extract509...");
        return userVal;
    }

    /**
     * This method is responsible to extract the information from both the 
     * AttributeStatements as found in the main Assertion element as well as the 
     * AttributeStatements found in the Evidence element.  The permitted names 
     * of the Attributes in the Assertion element are: UserRole, PurposeForUse, 
     * UserName, UserOrganization. The permitted names of the Attributes in the 
     * Evidence element are: AccessConsentPolicy and InstanceAccessConsentPolicy
     * @param statement The attribute statement to be extracted
     * @param assertOut The Assertion element being written to
     */
    private static void extractAttributeInfo(AttributeStatementType statement, AssertionType assertOut) {
        log.debug("Entering SamlTokenExtractor.extractAttributeInfo...");

        List attribs = statement.getAttributeOrEncryptedAttribute();

        if (attribs != null && !attribs.isEmpty()) {
            for (int idx = 0; idx < attribs.size(); idx++) {
                if (attribs.get(idx) instanceof AttributeType) {
                    AttributeType attrib = (AttributeType) attribs.get(idx);
                    String nameAttr = attrib.getName();
                    if (nameAttr != null) {
                        if (nameAttr.equals(NhincConstants.USER_ROLE_ATTR)) {
                            log.debug("Extracting Assertion.userInfo.roleCoded:");
                            assertOut.getUserInfo().setRoleCoded(extractNhinCodedElement(attrib, NhincConstants.USER_ROLE_ATTR));
                        } else if (nameAttr.equals(NhincConstants.PURPOSE_ROLE_ATTR)) {
                            log.debug("Extracting Assertion.purposeOfDisclosure:");
                            assertOut.setPurposeOfDisclosureCoded(extractNhinCodedElement(attrib, NhincConstants.PURPOSE_ROLE_ATTR));
                        } else if (nameAttr.equals(NhincConstants.USERNAME_ATTR)) {
                            extractNameParts(attrib, assertOut);
                        } else if (nameAttr.equals(NhincConstants.USER_ORG_ATTR)) {
                            String sUserOrg = extractAttributeValueString(attrib);
                            assertOut.getUserInfo().getOrg().setName(sUserOrg);
                            log.debug("Assertion.userInfo.org.Name = " + sUserOrg);
                        } else if (nameAttr.equals(NhincConstants.USER_ORG_ID_ATTR)) {
                            String sUserOrgId = extractAttributeValueString(attrib);
                            assertOut.getUserInfo().getOrg().setHomeCommunityId(sUserOrgId);
                            log.debug("Assertion.userInfo.org.homeCommunityId = " + sUserOrgId);
                        } else if (nameAttr.equals(NhincConstants.HOME_COM_ID_ATTR)) {
                            String sHomeComId = extractAttributeValueString(attrib);
                            assertOut.getHomeCommunity().setHomeCommunityId(sHomeComId);
                            log.debug("Assertion.homeCommunity.homeCommunityId = " + sHomeComId);
                        } else if (nameAttr.equals(NhincConstants.PATIENT_ID_ATTR)) {
                            String sPatientId = extractAttributeValueString(attrib);
                            assertOut.getUniquePatientId().add(sPatientId);
                            log.debug("Assertion.uniquePatientId = " + sPatientId);
                        } else if (nameAttr.equals(NhincConstants.ACCESS_CONSENT_ATTR)) {
                            String sAccessConsentId = extractAttributeValueString(attrib);
                            assertOut.getSamlAuthzDecisionStatement().getEvidence().getAssertion().setAccessConsentPolicy(sAccessConsentId);
                            log.debug("Assertion.SamlAuthzDecisionStatement.Evidence.Assertion.AccessConsentPolicy = " + sAccessConsentId);
                        } else if (nameAttr.equals(NhincConstants.INST_ACCESS_CONSENT_ATTR)) {
                            String sInstAccessConsentId = extractAttributeValueString(attrib);
                            assertOut.getSamlAuthzDecisionStatement().getEvidence().getAssertion().setInstanceAccessConsentPolicy(sInstAccessConsentId);
                            log.debug("Assertion.SamlAuthzDecisionStatement.Evidence.Assertion.InstanceAccessConsentPolicy = " + sInstAccessConsentId);
                        } else {
                            log.warn("Unrecognized Name Attribute: " + nameAttr);
                        }
                    } else {
                        log.warn("Improperly formed Name Attribute: " + nameAttr);
                    }
                }
            }
        } else {
            log.error("Expected Attributes are missing.");
        }

        log.debug("Exiting SamlTokenExtractor.extractAttributeInfo...");
    }

    /**
     * This method takes an attribute and extracts the string value of the
     * attribute.  If the attribute has multiple values, then it concatenates
     * all of the values.
     *
     * @param attrib The attribute containing the string value.
     * @return The string value (or if there are multiple values, the concatenated string value.)
     */
    private static String extractAttributeValueString(AttributeType attrib) {
        String retValue = "";

        List attrVals = attrib.getAttributeValue();
        if ((attrVals != null) &&
                (attrVals.size() > 0)) {
            StringBuffer strBuf = new StringBuffer();
            for (Object o : attrVals) {
                strBuf.append(o + " ");
            }
            retValue = strBuf.toString();
        }

        return retValue.trim();

    }

    /**
     * This method takes an attribute and extracts the base64Encoded value from the first
     * attribute value.
     *
     * @param attrib The attribute containing the string value.
     * @return The string value (or if there are multiple values, the concatenated string value.)
     */
    private static byte[] extractFirstAttributeValueBase64Binary(AttributeType attrib) {
        byte[] retValue = null;

        List attrVals = attrib.getAttributeValue();
        if ((attrVals != null) &&
                (attrVals.size() > 0)) {
            if (attrVals.get(0) instanceof byte[]) {
                retValue = (byte[]) attrVals.get(0);
            }
        }

        return retValue;
    }

    /**
     * The value of the UserName attribute is assumed to be a user's name in 
     * plain text.  The name parts are extracted in this method as the first 
     * word constitutes the first name, the last word constitutes the last name 
     * and all other text in between these words constitute the middle name. 
     * @param attrib The Attribute that has the user name as its value
     * @param assertOut The Assertion element being written to
     */
    private static void extractNameParts(AttributeType attrib, AssertionType assertOut) {
        log.debug("Entering SamlTokenExtractor.extractNameParts...");

        // Assumption is that before the 1st space reflects the first name,
        // after the last space is the last name, anything between is the middle name
        List attrVals = attrib.getAttributeValue();
        if ((attrVals != null) &&
                (attrVals.size() >= 1)) {
            PersonNameType personName = assertOut.getUserInfo().getPersonName();

            // Although SAML allows for multiple attribute values, the NHIN Specification
            // states that for a name there will be one attribute value.  So we will
            // only look at the first one.  If there are more, the first is the only one
            // that will be used.
            //-----------------------------------------------------------------------------
            String completeName = attrVals.get(0).toString();
            personName.setFullName(completeName);
            log.debug("Assertion.userInfo.personName.FullName = " + completeName);

            String[] nameTokens = completeName.split("\\s");
            ArrayList<String> nameParts = new ArrayList<String>();

            //remove blank tokens
            for (String tok : nameTokens) {
                if (tok.trim() != null && tok.trim().length() > 0) {
                    nameParts.add(tok);
                }
            }

            if (nameParts.size() > 0) {
                if (!nameParts.get(0).isEmpty()) {
                    personName.setGivenName(nameParts.get(0));
                    nameParts.remove(0);
                    log.debug("Assertion.userInfo.personName.givenName = " + personName.getGivenName());
                }
            }

            if (nameParts.size() > 0) {
                if (!nameParts.get(nameParts.size() - 1).isEmpty()) {
                    personName.setFamilyName(nameParts.get(nameParts.size() - 1));
                    nameParts.remove(nameParts.size() - 1);
                    log.debug("Assertion.userInfo.personName.familyName = " + personName.getFamilyName());
                }
            }

            if (nameParts.size() > 0) {
                StringBuffer midName = new StringBuffer();
                for (String name : nameParts) {
                    midName.append(name + " ");
                }
                // take off last blank character
                midName.setLength(midName.length() - 1);
                personName.setSecondNameOrInitials(midName.toString());
                log.debug("Assertion.userInfo.personName.secondNameOrInitials = " + personName.getSecondNameOrInitials());
            }
        } else {
            log.error("User Name attribute is empty: " + attrVals);
        }

        log.debug("SamlTokenExtractor.extractNameParts() -- End");
    }

    /**
     * The value of the UserRole and PurposeForUse attributes are formatted 
     * according to the specifications of an nhin:CodedElement.  This method 
     * parses that expected structure to obtain the code, codeSystem, 
     * codeSystemName, and the displayName attributes of that element.
     * @param attrib The Attribute that has the UserRole or PurposeForUse as its 
     * value
     * @param assertOut The Assertion element being written to
     * @param codeId Identifies which coded element this is parsing
     */
    private static CeType extractNhinCodedElement(AttributeType attrib, String codeId) {
        log.debug("Entering SamlTokenExtractor.parseNhinCodedElement...");

        CeType ce = new CeType();
        ce.setCode(EMPTY_STRING);
        ce.setCodeSystem(EMPTY_STRING);
        ce.setCodeSystemName(EMPTY_STRING);
        ce.setDisplayName(EMPTY_STRING);

        List attrVals = attrib.getAttributeValue();
        //log.debug("extractNhinCodedElement: " + attrib.getName() + " has " + attrVals.size() + " values");

        if ((attrVals != null) &&
                (attrVals.size() > 0)) {
            log.debug("AttributeValue is: " + attrVals.get(0).getClass());
            // According to the NHIN specifications - there should be exactly one value.
            // If there is more than one. We will take only the first one.
            //---------------------------------------------------------------------------
            NodeList nodelist = null;
            if (attrVals.get(0) instanceof ElementNSImpl) {
                ElementNSImpl elem = (ElementNSImpl) attrVals.get(0);
                nodelist = elem.getChildNodes();
            } else if (attrVals.get(0) instanceof org.apache.xerces.dom.ElementNSImpl) {
                org.apache.xerces.dom.ElementNSImpl elem = (org.apache.xerces.dom.ElementNSImpl) attrVals.get(0);
                nodelist = elem.getChildNodes();
            } else {
                log.error("The value for the " + codeId + " attribute is a: " + attrVals.get(0).getClass() + " expected a ElementNSImpl");
            }
            if ((nodelist != null) &&
                    (nodelist.getLength() > 0)) {
                int numNodes = nodelist.getLength();
                for (int idx = 0; idx < numNodes; idx++) {
                    if (nodelist.item(idx) instanceof Node) {
                        //log.debug("Processing index:" + idx + " node as " + nodelist.item(idx).getNodeName());
                        Node node = (Node) nodelist.item(idx);
                        NamedNodeMap attrMap = node.getAttributes();
                        if ((attrMap != null) &&
                                (attrMap.getLength() > 0)) {
                            int numMapNodes = attrMap.getLength();
                            for (int attrIdx = 0; attrIdx < numMapNodes; attrIdx++) {
                                //log.debug("Processing attribute index:" + attrIdx + " as " + attrMap.item(attrIdx));
                                Node attrNode = attrMap.item(attrIdx);
                                if ((attrNode != null) &&
                                        (attrNode.getNodeName() != null) &&
                                        (!attrNode.getNodeName().isEmpty())) {
                                    if (attrNode.getNodeName().equalsIgnoreCase(NhincConstants.CE_CODE_ID)) {
                                        ce.setCode(attrNode.getNodeValue());
                                        log.debug(codeId + ": ce.Code = " + ce.getCode());
                                    }
                                    if (attrNode.getNodeName().equalsIgnoreCase(NhincConstants.CE_CODESYS_ID)) {
                                        ce.setCodeSystem(attrNode.getNodeValue());
                                        log.debug(codeId + ": ce.CodeSystem = " + ce.getCodeSystem());
                                    }
                                    if (attrNode.getNodeName().equalsIgnoreCase(NhincConstants.CE_CODESYSNAME_ID)) {
                                        ce.setCodeSystemName(attrNode.getNodeValue());
                                        log.debug(codeId + ": ce.CodeSystemName = " + ce.getCodeSystemName());
                                    }
                                    if (attrNode.getNodeName().equalsIgnoreCase(NhincConstants.CE_DISPLAYNAME_ID)) {
                                        ce.setDisplayName(attrNode.getNodeValue());
                                        log.debug(codeId + ": ce.DisplayName = " + ce.getDisplayName());
                                    }
                                } else {
                                    log.debug("Attribute name can not be processed");
                                }
                            }   // for (int attrIdx = 0; attrIdx < numMapNodes; attrIdx++) {
                            } else {
                            log.debug("Attribute map is null");
                        }
                    } else {
                        log.debug("Expected AttributeValue to have a Node child");
                    }
                }   // for (int idx = 0; idx < numNodes; idx++) {
                } else {
                log.error("The AttributeValue for " + codeId + " should have a Child Node");
            }
        } else {
            log.error("Attributes for " + codeId + " are invalid: " + attrVals);
        }

        log.debug("Exiting SamlTokenExtractor.extractNhinCodedElement...");
        return ce;
    }

    /**
     * The Authorization Decision Statement is used to convey a form authorizing
     * access to medical records.  It may embed the binary content of the
     * authorization form as well describing the conditions of its validity.
     * This method saves off all values associated with this Evidence.
     * @param authnStatement The authn statement element
     * @param assertOut The Assertion element being written to
     */
    public static void extractAuthnStatement(AuthnStatementType authnStatement, AssertionType assertOut) {
        log.debug("Entering SamlTokenExtractor.extractAuthnStatement...");

        if (authnStatement == null) {
            log.debug("authnStatement is null: ");
            return;         // nothing to do...
        }

        SamlAuthnStatementType samlAuthnStatement = assertOut.getSamlAuthnStatement();

        // AuthnInstant
        //-------------
        if (authnStatement.getAuthnInstant() != null) {
            samlAuthnStatement.setAuthInstant(authnStatement.getAuthnInstant().toXMLFormat());
            log.debug("Assertion.samlAuthnStatement.authnInstant = " + samlAuthnStatement.getAuthInstant());
        }

        // SessionIndex
        if (authnStatement.getSessionIndex() != null) {
            samlAuthnStatement.setSessionIndex(authnStatement.getSessionIndex());
            log.debug("Assertion.samlAuthnStatement.sessionIndex = " + samlAuthnStatement.getSessionIndex());
        }

        // AuthContextClassRef
        //--------------------
        if ((authnStatement.getAuthnContext() != null) &&
                (authnStatement.getAuthnContext().getContent() != null)) {
            log.debug("authnContext has " + authnStatement.getAuthnContext().getContent().size() + " content entries. ");
            List<JAXBElement<?>> contents = authnStatement.getAuthnContext().getContent();
            if ((contents != null) &&
                    (contents.size() > 0)) {
                for (JAXBElement jaxElem1 : contents) {
                    // When you look at the actual XML string, it looks as follows:
                    // 		<saml2:AuthnContext>
                    //          <saml2:AuthnContextClassRef>urn:oasis:names:tc:SAML:2.0:ac:classes:X509</saml2:AuthnContextClassRef>
                    //      </saml2:AuthnContext>
                    // This implies that we should see under the AuthnContext tag another tag.  But for some reason the
                    // unmarshaller is stripping that layer out and what we see directly under the AuthnContext is
                    // a string element that contains the value that is currently in the AuthnContextClassRef tag.  So
                    // that is why we are looking for a string data type here.
                    //----------------------------------------------------------------------------------------------------------------
                    if (jaxElem1.getValue() instanceof String) {
                        String sValue = (String) jaxElem1.getValue();
                        samlAuthnStatement.setAuthContextClassRef(sValue);
                        log.debug("Assertion.samlAuthnStatement.authContextClassRef = " + samlAuthnStatement.getAuthContextClassRef());
                    }   // if (jaxElem1.getValue() instanceof AuthnContext)
                }   // for (JAXBElement jaxElem1 : contents)
            }   // if ((contents != null) &&
        }   // if ((authnStatement.getAuthnContext() != null) &&

        // SubjectLocalityAddress
        //-----------------------
        if ((authnStatement.getSubjectLocality() != null) &&
                (authnStatement.getSubjectLocality().getAddress() != null) &&
                (authnStatement.getSubjectLocality().getAddress().length() > 0)) {
            samlAuthnStatement.setSubjectLocalityAddress(authnStatement.getSubjectLocality().getAddress());
            log.debug("Assertion.samlAuthnStatement.subjectlocalityAddress = " + samlAuthnStatement.getSubjectLocalityAddress());
        }

        // SubjectLocalityDNSName
        //------------------------
        if ((authnStatement.getSubjectLocality() != null) &&
                (authnStatement.getSubjectLocality().getDNSName() != null) &&
                (authnStatement.getSubjectLocality().getDNSName().length() > 0)) {
            samlAuthnStatement.setSubjectLocalityDNSName(authnStatement.getSubjectLocality().getDNSName());
            log.debug("Assertion.samlAuthnStatement.subjectlocalityDNSName = " + samlAuthnStatement.getSubjectLocalityDNSName());
        }

        log.debug("Exiting SamlTokenExtractor.extractAuthnStatement...");
    }

    /**
     * The Authorization Decision Statement is used to convey a form authorizing 
     * access to medical records.  It may embed the binary content of the 
     * authorization form as well describing the conditions of its validity.  
     * This method saves off all values associated with this Evidence.
     * @param authzState The authorization decision element
     * @param assertOut The Assertion element being written to
     */
    private static void extractDecisionInfo(AuthzDecisionStatementType authzState, AssertionType assertOut) {
        log.debug("Entering SamlTokenExtractor.extractDecisionInfo...");

        SamlAuthzDecisionStatementType oSamlAuthzDecision = assertOut.getSamlAuthzDecisionStatement();

        // @Decision
        //-----------
        if ((authzState.getDecision() != null) &&
                (authzState.getDecision().value() != null)) {
            oSamlAuthzDecision.setDecision(authzState.getDecision().value());
            log.debug("Assertion.SamlAuthzDecisionStatement.Decision = " + oSamlAuthzDecision.getDecision());
        }

        // @Resource
        //----------
        if (authzState.getResource() != null) {
            oSamlAuthzDecision.setResource(authzState.getResource());
            log.debug("Assertion.SamlAuthzDecisionStatement.Resource = " + oSamlAuthzDecision.getResource());
        }

        // Action
        // According to the NHIN Specifications we should see exactly one of these.  If there are more than
        // one we will only take the first one.
        //--------------------------------------------------------------------------------------------------
        if ((authzState.getAction() != null) &&
                (authzState.getAction().size() > 0) &&
                (authzState.getAction().get(0) != null) &&
                (authzState.getAction().get(0).getValue() != null)) {
            oSamlAuthzDecision.setAction(authzState.getAction().get(0).getValue());
            log.debug("Assertion.SamlAuthzDecisionStatement.Action = " + oSamlAuthzDecision.getAction());
        }

        // Evidence
        //----------
        EvidenceType evid = authzState.getEvidence();
        extractEvidence(evid, assertOut);

        log.debug("Exiting SamlTokenExtractor.extractDecisionInfo...");
    }

    /**
     * This extracts the evidence information from the authorization
     * decision statement and places the data into the assertion class.
     *
     * @param evidence The evidence that was part of the authroization decision statement.
     * @param assertOut The class where the data will be placed.
     */
    private static void extractEvidence(EvidenceType evidence, AssertionType assertOut) {
        log.debug("Entering SamlTokenExtractor.extractEvidence...");
        SamlAuthzDecisionStatementEvidenceAssertionType oSamlEvidAssert = assertOut.getSamlAuthzDecisionStatement().getEvidence().getAssertion();

        List<JAXBElement<?>> oaAsserts = evidence.getAssertionIDRefOrAssertionURIRefOrAssertion();
        if ((oaAsserts != null) &&
                (oaAsserts.size() > 0)) {
            // Loop looking for the tags we need.
            //----------------------------------------
            for (JAXBElement<?> oElement : oaAsserts) {
                // Is this an Assertion?
                //-----------------------
                if (oElement.getValue() instanceof com.sun.xml.wss.saml.internal.saml20.jaxb20.AssertionType) {

                    com.sun.xml.wss.saml.internal.saml20.jaxb20.AssertionType oAssert = (com.sun.xml.wss.saml.internal.saml20.jaxb20.AssertionType) oElement.getValue();

                    // ID
                    //----
                    if (oAssert.getID() != null) {
                        oSamlEvidAssert.setId(oAssert.getID());
                        log.debug("Assertion.SamlAuthzDecisionStatement.Evidence.Assertion.Id = " + oSamlEvidAssert.getId());
                    }

                    // Issue Instant
                    //--------------
                    if (oAssert.getIssueInstant() != null) {
                        oSamlEvidAssert.setIssueInstant(oAssert.getIssueInstant().toXMLFormat());
                        log.debug("Assertion.SamlAuthzDecisionStatement.Evidence.Assertion.IssueInstant = " + oSamlEvidAssert.getIssueInstant());
                    }

                    // Version
                    //--------
                    if (oAssert.getVersion() != null) {
                        oSamlEvidAssert.setVersion(oAssert.getVersion());
                        log.debug("Assertion.SamlAuthzDecisionStatement.Evidence.Assertion.Version = " + oSamlEvidAssert.getVersion());
                    }

                    // Issuer Format
                    //---------------
                    if ((oAssert.getIssuer() != null) &&
                            (oAssert.getIssuer().getFormat() != null)) {
                        oSamlEvidAssert.setIssuerFormat(oAssert.getIssuer().getFormat());
                        log.debug("Assertion.SamlAuthzDecisionStatement.Evidence.Assertion.IssuerFormat = " + oSamlEvidAssert.getIssuerFormat());
                    }

                    // Issuer
                    //-------
                    if ((oAssert.getIssuer() != null) &&
                            (oAssert.getIssuer().getValue() != null)) {
                        oSamlEvidAssert.setIssuer(oAssert.getIssuer().getValue());
                        log.debug("Assertion.SamlAuthzDecisionStatement.Evidence.Assertion.Issuer = " + oSamlEvidAssert.getIssuer());
                    }

                    extractConditionsInfo(assertOut, oAssert.getConditions());

                    List statements = oAssert.getStatementOrAuthnStatementOrAuthzDecisionStatement();
                    if (statements != null && !statements.isEmpty()) {
                        for (int idxState = 0; idxState <
                                statements.size(); idxState++) {
                            if (statements.get(idxState) instanceof AttributeStatementType) {
                                AttributeStatementType statement = (AttributeStatementType) statements.get(idxState);
                                extractAttributeInfo(statement, assertOut);
                            }
                        }
                    } else {
                        log.error("Evidence Statements are missing.");
                    }
                } else {
                    log.warn("Non-Assertion type element: " + oElement.getValue() + " is not processed");
                }
            }
        } else {
            log.error("Evidence assertion is empty: " + oaAsserts);
        }

        log.debug("Exiting SamlTokenExtractor.extractEvidence...");
    }

    /**
     * This method extracts the dates of validity for the Evidence's 
     * authorization form.  These dates are contained in the Conditions element 
     * and are written out to the storage file by this method.
     * @param assertOut The Assertion element being written to
     * @param conditions The Evidence's Conditions element
     */
    private static void extractConditionsInfo(AssertionType assertOut, ConditionsType conditions) {
        log.debug("Entering SamlTokenExtractor.extractConditionsInfo...");

        SamlAuthzDecisionStatementEvidenceAssertionType oSamlEvidAssert = assertOut.getSamlAuthzDecisionStatement().getEvidence().getAssertion();

        if (conditions != null) {
            //SimpleDateFormat dateForm = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

            XMLGregorianCalendar beginTime = conditions.getNotBefore();
            if (beginTime != null && beginTime.toGregorianCalendar() != null && beginTime.toGregorianCalendar().getTime() != null) {
                String formBegin = beginTime.toXMLFormat();

                if (NullChecker.isNotNullish(formBegin)) {
                    oSamlEvidAssert.getConditions().setNotBefore(formBegin);
                    log.debug("Assertion.SamlAuthzDecisionStatement.Evidence.Assertion.Conditions.NotBefore = " + oSamlEvidAssert.getConditions().getNotBefore());
                }
            }

            XMLGregorianCalendar endTime = conditions.getNotOnOrAfter();
            if (endTime != null && endTime.toGregorianCalendar() != null && endTime.toGregorianCalendar().getTime() != null) {
                String formEnd = endTime.toXMLFormat();
                //String formEnd = dateForm.format(endTime.toGregorianCalendar().getTime());

                if (NullChecker.isNotNullish(formEnd)) {
                    oSamlEvidAssert.getConditions().setNotOnOrAfter(formEnd);
                    log.debug("Assertion.SamlAuthzDecisionStatement.Evidence.Assertion.Conditions.NotOnOrAfter = " + oSamlEvidAssert.getConditions().getNotOnOrAfter());
                }
            }
        }
        log.debug("Exiting SamlTokenExtractor.extractConditionsInfo...");
    }
}
