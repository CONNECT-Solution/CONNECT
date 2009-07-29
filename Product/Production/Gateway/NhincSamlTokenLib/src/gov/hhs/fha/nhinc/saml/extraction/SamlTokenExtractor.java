/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.saml.extraction;

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
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.text.SimpleDateFormat;
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
    private static final String USER_ROLE_ID = "UserRole";
    private static final String PURPOSE_ROLE_ID = "PurposeForUse";
    private static final String USERNAME_ID = "UserName";
    private static final String USERORG_ID = "UserOrganization";
    private static final String CONTENTREF_ID = "ContentReference";
    private static final String CONTENT_ID = "Content";
    private static final String CONTENTTYPE_ID = "ContentType";
    private static final String CE_CODE_ID = "code";
    private static final String CE_CODESYS_ID = "codesystem";
    private static final String CE_CODESYSNAME_ID = "codeSystemName";
    private static final String CE_DISPLAYNAME_ID = "displayName";
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
                        // Currently nothing done for AuthnStatement
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
            ;
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
    private static AssertionType  initializeAssertion() {

        System.out.println("Initializing Assertion to Default: " + EMPTY_STRING);
        AssertionType assertOut = new AssertionType();

        CeType purposeCoded = new CeType();
        UserType user = new UserType();
        PersonNameType userPerson = new PersonNameType();
        CeType userRole = new CeType();
        HomeCommunityType userHc = new HomeCommunityType();
        user.setPersonName(userPerson);
        user.setOrg(userHc);
        user.setRoleCoded(userRole);
        assertOut.setUserInfo(user);
        assertOut.setPurposeOfDisclosureCoded(purposeCoded);

        userPerson.setGivenName(EMPTY_STRING);
        userPerson.setFamilyName(EMPTY_STRING);
        userPerson.setSecondNameOrInitials(EMPTY_STRING);
        userHc.setName(EMPTY_STRING);
        user.setUserName(EMPTY_STRING);
        userRole.setCode(EMPTY_STRING);
        userRole.setCodeSystem(EMPTY_STRING);
        userRole.setCodeSystemName(EMPTY_STRING);
        userRole.setDisplayName(EMPTY_STRING);

        purposeCoded.setCode(EMPTY_STRING);
        purposeCoded.setCodeSystem(EMPTY_STRING);
        purposeCoded.setCodeSystemName(EMPTY_STRING);
        purposeCoded.setDisplayName(EMPTY_STRING);

        assertOut.setDateOfSignature(EMPTY_STRING);
        assertOut.setExpirationDate(EMPTY_STRING);
        assertOut.setClaimFormRef(EMPTY_STRING);

        byte[] formRaw = EMPTY_STRING.getBytes();
        assertOut.setClaimFormRaw(formRaw);

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
     * Evidence element are: ContentReference, ContentType, and Content.
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
                        if (nameAttr.equals(USER_ROLE_ID)) {
                            assertOut.getUserInfo().setRoleCoded(parseRole(attrib, USER_ROLE_ID));
                        } else if (nameAttr.equals(PURPOSE_ROLE_ID)) {
                            assertOut.setPurposeOfDisclosureCoded(parseRole(attrib, PURPOSE_ROLE_ID));
                        } else if (nameAttr.equals(USERNAME_ID)) {
                            extractNameParts(attrib, assertOut);
                        } else if (nameAttr.equals(USERORG_ID) ||
                                nameAttr.equals(CONTENTREF_ID) ||
                                nameAttr.equals(CONTENT_ID)) {
                            List attrVals = attrib.getAttributeValue();
                            if (attrVals != null && !attrVals.isEmpty()) {
                                if (nameAttr.equals(CONTENT_ID)) {
                                    for (int idxVal = 0; idxVal < attrVals.size(); idxVal++) {
                                        Object formVal = attrVals.get(idxVal);

                                        if (formVal instanceof byte[]) {
                                            byte[] rawForm = (byte[]) attrVals.get(idxVal);
                                            log.info("Setting Claim Form to: " + rawForm.toString());
                                            assertOut.setClaimFormRaw(rawForm);
                                        } else {
                                            log.error(nameAttr + " Attribute is not recognized as base64 binary");
                                        }
                                    }
                                } else {
                                    StringBuffer strBuf = new StringBuffer();
                                    for (int idxVal = 0; idxVal < attrVals.size(); idxVal++) {
                                        strBuf.append(attrVals.get(idxVal) + " ");
                                    }
                                    if (nameAttr.equals(CONTENTREF_ID)) {
                                        log.info("Setting ContentReference to: " + strBuf.toString().trim());
                                        assertOut.setClaimFormRef(strBuf.toString().trim());
                                    } else if (nameAttr.equals(USERORG_ID)) {
                                        log.info("Setting UserOrganization to: " + strBuf.toString().trim());
                                        HomeCommunityType homeCommunity = assertOut.getUserInfo().getOrg();
                                        homeCommunity.setName(strBuf.toString().trim());
                                        assertOut.getUserInfo().setOrg(homeCommunity);
                                    }
                                }
                            } else {
                                log.warn("No values are provided for Attribute: " + nameAttr);
                            }
                        } else if (nameAttr.equals(CONTENTTYPE_ID)) {
                            log.info(nameAttr + " is set to default 'application\\pdf'.");
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
        if (attrVals != null && !attrVals.isEmpty()) {
            PersonNameType personName = assertOut.getUserInfo().getPersonName();

            for (int idxVal = 0; idxVal <
                    attrVals.size(); idxVal++) {
                if (attrVals.get(idxVal) != null) {
                    String completeName = attrVals.get(idxVal).toString();
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
                            log.info("Setting User's Given Name to:" + nameParts.get(0));

                            personName.setGivenName(nameParts.get(0));
                            nameParts.remove(0);
                        }
                    }

                    if (nameParts.size() > 0) {
                        if (!nameParts.get(nameParts.size() - 1).isEmpty()) {
                            log.info("Setting User's Family Name to: " + nameParts.get(nameParts.size() - 1));
                            personName.setFamilyName(nameParts.get(nameParts.size() - 1));
                            nameParts.remove(nameParts.size() - 1);
                        }
                    }

                    if (nameParts.size() > 0) {
                        StringBuffer midName = new StringBuffer();
                        for (String name : nameParts) {
                            midName.append(name + " ");
                        }
                        // take off last blank character
                        midName.setLength(midName.length() - 1);
                        log.info("Setting User's Middle Name to: " + midName.toString());
                        personName.setSecondNameOrInitials(midName.toString());
                    }
                    // Once found break out of the loop
                    break;
                }
            }
        } else {
            log.error("User Name attribute is empty: " + attrVals);
        }

        log.debug("Token_Rcv.extractNameParts() -- End");
    }

    /**
     * The value of the UserRole and PurposeForUse attributes are formatted 
     * according to the specifications of an nhin:CodedElement.  This method 
     * parses that expected structure to obtain the code, codeSystem, 
     * codeSystemName, and the displayName attributes of that element.
     * @param attrib The Attribute that has the UserRole or PurposeForUse as its 
     * value
     * @param assertOut The Assertion element being written to
     * @param id Identifies which coded element this is parsing
     */
    private static CeType parseRole(AttributeType attrib, String id) {
        log.debug("Entering SamlTokenExtractor.parseRole...");

        CeType ce = new CeType();
        ce.setCode(EMPTY_STRING);
        ce.setCodeSystem(EMPTY_STRING);
        ce.setCodeSystemName(EMPTY_STRING);
        ce.setDisplayName(EMPTY_STRING);

        List attrVals = attrib.getAttributeValue();

       if (attrVals != null && !attrVals.isEmpty()) {
            int numAttr = attrVals.size();
            for (int idxVal = 0; idxVal <
                    numAttr; idxVal++) {
                log.debug("Processing index:" + idxVal + " attribute value for " + id);
                if (attrVals.get(idxVal) instanceof ElementNSImpl) {
                    ElementNSImpl elem = (ElementNSImpl) attrVals.get(idxVal);
                    log.debug("Element " + elem.getNodeName());

                    NodeList nodelist = elem.getChildNodes();
                    if (nodelist != null && nodelist.getLength() > 0) {
                        int numNodes = nodelist.getLength();
                        for (int idx = 0; idx < numNodes; idx++) {
                            if (nodelist.item(idx) instanceof Node) {
                                log.debug("Processing index:" + idx + " node as " + nodelist.item(idx).getNodeName());
                                Node node = (Node) nodelist.item(idx);
                                NamedNodeMap attrMap = node.getAttributes();
                                if (attrMap != null && attrMap.getLength() > 0) {
                                    int numMapNodes = attrMap.getLength();
                                    for (int attrIdx = 0; attrIdx < numMapNodes; attrIdx++) {
                                        log.debug("Processing attribute index:" + attrIdx + " as " + attrMap.item(attrIdx));
                                        Node attrNode = attrMap.item(attrIdx);
                                        if (attrNode != null && attrNode.getNodeName() != null && !attrNode.getNodeName().isEmpty()) {
                                            if (attrNode.getNodeName().equalsIgnoreCase(CE_CODE_ID)) {
                                                if (USER_ROLE_ID.equals(id)) {
                                                    log.debug("Setting UserRole Code to: " + attrNode.getNodeValue());
                                                    ce.setCode(attrNode.getNodeValue());
                                                } else if (PURPOSE_ROLE_ID.equals(id)) {
                                                    log.debug("Setting Purpose Code to: " + attrNode.getNodeValue());
                                                    ce.setCode(attrNode.getNodeValue());
                                                } else {
                                                    log.debug("Unrecognized item: " + id + " Cannot parse " + CE_CODE_ID);
                                                }
                                            }
                                            if (attrNode.getNodeName().equalsIgnoreCase(CE_CODESYS_ID)) {
                                                if (USER_ROLE_ID.equals(id)) {
                                                    log.debug("Setting UserRole Code System to: " + attrNode.getNodeValue());
                                                    ce.setCodeSystem(attrNode.getNodeValue());
                                                } else if (PURPOSE_ROLE_ID.equals(id)) {
                                                    log.debug("Setting Purpose Code System to: " + attrNode.getNodeValue());
                                                    ce.setCodeSystem(attrNode.getNodeValue());

                                                } else {
                                                    log.debug("Unrecognized item: " + id + " Cannot parse " + CE_CODESYS_ID);
                                                }
                                            }
                                            if (attrNode.getNodeName().equalsIgnoreCase(CE_CODESYSNAME_ID)) {
                                                if (USER_ROLE_ID.equals(id)) {
                                                    log.debug("Setting UserRole Code System Name to: " + attrNode.getNodeValue());
                                                    ce.setCodeSystemName(attrNode.getNodeValue());
                                                } else if (PURPOSE_ROLE_ID.equals(id)) {
                                                    log.debug("Setting Purpose Code System Name to: " + attrNode.getNodeValue());
                                                    ce.setCodeSystemName(attrNode.getNodeValue());
                                                } else {
                                                    log.debug("Unrecognized item: " + id + " Cannot parse " + CE_CODESYSNAME_ID);
                                                }
                                            }
                                            if (attrNode.getNodeName().equalsIgnoreCase(CE_DISPLAYNAME_ID)) {
                                                if (USER_ROLE_ID.equals(id)) {
                                                    log.debug("Setting UserRole Display to: " + attrNode.getNodeValue());
                                                    ce.setDisplayName(attrNode.getNodeValue());
                                                } else if (PURPOSE_ROLE_ID.equals(id)) {
                                                    log.debug("Setting Purpose Display to: " + attrNode.getNodeValue());
                                                    ce.setDisplayName(attrNode.getNodeValue());
                                                } else {
                                                    log.debug("Unrecognized item: " + id + " Cannot parse " + CE_DISPLAYNAME_ID);
                                                }
                                            }
                                        } else {
                                          log.debug("Attribute name can not be processed");
                                        }
                                    }
                                } else {
                                    log.debug("Attribute map is null");
                                }
                            } else {
                                log.debug("Expected AttributeValue to have a Node child");
                            }
                        }
                    } else {
                        log.error("The AttributeValue for " + id + " should have a Child Node");
                    }
                } else {
                    log.error("The value for the " + id + " attribute is not a proper AttributeValue.");
                }
                // Once found break out of the loop
                break;
            }
        } else {
            log.error("Attributes for " + id + " are invalid: " + attrVals);
        }

        log.debug("Exiting SamlTokenExtractor.parseRole...");
        return ce;
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

        EvidenceType evid = authzState.getEvidence();
        List asserts = evid.getAssertionIDRefOrAssertionURIRefOrAssertion();
        if (asserts != null && !asserts.isEmpty()) {
            for (int idx = 0; idx <
                    asserts.size(); idx++) {
                if (asserts.get(idx) instanceof JAXBElement) {
                    JAXBElement evElem = (JAXBElement) asserts.get(idx);
                    if (evElem.getValue() instanceof com.sun.xml.wss.saml.internal.saml20.jaxb20.AssertionType) {
                        com.sun.xml.wss.saml.internal.saml20.jaxb20.AssertionType evAss = (com.sun.xml.wss.saml.internal.saml20.jaxb20.AssertionType) evElem.getValue();

                        extractConditionsInfo(assertOut, evAss.getConditions());

                        List statements = evAss.getStatementOrAuthnStatementOrAuthzDecisionStatement();
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
                        log.warn("Non-Assertion type element: " + evElem.getValue() + " is not processed");
                    }
                } else {
                    log.error("Evidence assertion is not recognized as a JAXB element");
                }
            }
        } else {
            log.error("Evidence assertion is empty: " + asserts);
        }
        log.debug("Exiting SamlTokenExtractor.extractDecisionInfo...");
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

        if (conditions != null) {
            SimpleDateFormat dateForm = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

            XMLGregorianCalendar beginTime = conditions.getNotBefore();
            if (beginTime != null && beginTime.toGregorianCalendar() != null && beginTime.toGregorianCalendar().getTime() != null) {
                String formBegin = dateForm.format(beginTime.toGregorianCalendar().getTime());

                if (NullChecker.isNotNullish(formBegin)) {
                    log.info("Setting Signature Date to: " + formBegin);
                    assertOut.setDateOfSignature(formBegin);
                }
            }

            XMLGregorianCalendar endTime = conditions.getNotOnOrAfter();
            if (endTime != null && endTime.toGregorianCalendar() != null && endTime.toGregorianCalendar().getTime() != null) {
                String formEnd = dateForm.format(endTime.toGregorianCalendar().getTime());

                if (NullChecker.isNotNullish(formEnd)) {
                    log.info("Setting Expires Date to: " + formEnd);
                    assertOut.setExpirationDate(formEnd);
                }
            }
        }
        log.debug("Exiting SamlTokenExtractor.extractConditionsInfo...");
    }
}
