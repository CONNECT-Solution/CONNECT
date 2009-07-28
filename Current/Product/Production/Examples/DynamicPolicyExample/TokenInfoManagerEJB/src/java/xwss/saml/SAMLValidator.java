package xwss.saml;

import com.sun.xml.wss.XWSSecurityException;
import com.sun.xml.wss.saml.Assertion;
import com.sun.xml.wss.saml.SAMLAssertionFactory;
import com.sun.xml.wss.saml.SAMLException;
import com.sun.xml.wss.saml.internal.saml11.jaxb10.AttributeValue;
import com.sun.xml.wss.saml.internal.saml20.jaxb20.ActionType;
import com.sun.xml.wss.saml.internal.saml20.jaxb20.AssertionType;
import com.sun.xml.wss.saml.internal.saml20.jaxb20.AttributeStatementType;
import com.sun.xml.wss.saml.internal.saml20.jaxb20.AttributeType;
import com.sun.xml.wss.saml.internal.saml20.jaxb20.AuthnStatementType;
import com.sun.xml.wss.saml.internal.saml20.jaxb20.AuthzDecisionStatementType;
import com.sun.xml.wss.saml.internal.saml20.jaxb20.ConditionsType;
import com.sun.xml.wss.saml.internal.saml20.jaxb20.EvidenceType;
import com.sun.xml.wss.saml.internal.saml20.jaxb20.NameIDType;
import gov.hhs.fha.nhinc.token.InternalTokenMgr;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.*;
import java.util.PropertyResourceBundle;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.security.auth.x500.X500Principal;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import sun.security.x509.AVA;
import sun.security.x509.X500Name;

/**
 * This class implements the Callback SAMLAssertionValidator which is invoked 
 * upon receiving an incoming message.  It extracts the Assertion element and 
 * drills down into each sub-element extracting the information needed to 
 * reconstruct the Assertion object which is needed to pass along information to 
 * the Agencies or other services.  The information obtained is written out to a 
 * configured file store for later retrieval.
 */
public class SAMLValidator implements com.sun.xml.wss.impl.callback.SAMLAssertionValidator {

    private static Log log = LogFactory.getLog(SAMLValidator.class);
    private static final String USER_ROLE_ID = "UserRole";
    private static final String PURPOSE_ROLE_ID = "PurposeForUse";
    //private static final String EMAIL_FORMAT = "urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress";
    private static final String X509_FORMAT = "urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName";

    /**
     * This method is required by the SAMLAssertionValidator, but is not invoked.
     * @param arg0 The Assertion element
     * @throws com.sun.xml.wss.impl.callback.SAMLAssertionValidator.SAMLValidationException
     */
    public void validate(Element arg0) throws SAMLValidationException {
        log.info(" Entry to the validate Element block");
        log.info(" Element: " + arg0);
    }

    /**
     * This is the invoked implementation of the validation upon receipt of an 
     * incoming message needing SAML verification.  It creates / opens the 
     * storage file and manages invocation of methods to extract and write out 
     * the needed information from the various sub-elements. 
     * @param reader Reader containing the SAML Assertion elements
     * @throws com.sun.xml.wss.impl.callback.SAMLAssertionValidator.SAMLValidationException
     */
    public void validate(XMLStreamReader reader) throws SAMLValidationException {
        log.debug(" ****************************  Entering SAML ValidatorCallback ***********************");

        PropertyResourceBundle prop = (PropertyResourceBundle) PropertyResourceBundle.getBundle(InternalTokenMgr.propFileName);
        String fileName = prop.getString(InternalTokenMgr.dumpFileName);
        RandomAccessFile raFile = null;
        PrintWriter writeOut = null;

        try {
            SAMLAssertionFactory factory = SAMLAssertionFactory.newInstance(SAMLAssertionFactory.SAML2_0);
            Assertion assertIn = factory.createAssertion(reader);

            raFile = new RandomAccessFile(fileName, "rw");
            raFile.setLength(0);
            log.debug("Create: " + fileName);

            writeOut = new PrintWriter(new FileWriter(fileName));

            writeSubject(writeOut, assertIn);

            List statements = assertIn.getStatements();
            if (statements != null && !statements.isEmpty()) {
                for (int idx = 0; idx < statements.size(); idx++) {
                    if (statements.get(idx) instanceof AttributeStatementType) {
                        AttributeStatementType statement = (AttributeStatementType) statements.get(idx);
                        writeAttributeInfo(writeOut, statement);
                    } else if (statements.get(idx) instanceof AuthzDecisionStatementType) {
                        AuthzDecisionStatementType statement = (AuthzDecisionStatementType) statements.get(idx);
                        writeDecisionInfo(writeOut, statement);
                    } else if (statements.get(idx) instanceof AuthnStatementType) {
                        // Currently nothing done for AuthnStatement 
                    } else {
                        log.warn("Unknown statement type: " + statements.get(idx));
                    }
                }
            } else {
                log.warn("Expected Attribute Statements are missing.");
            }
            log.debug(fileName + " write is complete");

        } catch (SAMLException ex) {
            log.error("SAMLException in validation: " + ex.getMessage());
            ex.printStackTrace();
        } catch (XWSSecurityException ex) {
            log.error("XWSSecurityException in validation: " + ex.getMessage());
            ex.printStackTrace();
        } catch (IOException ex) {
            log.error("IOException in validation: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            // Close the file
            if (writeOut != null) {
                writeOut.close();
            }
            if (raFile != null) {
                try {
                    raFile.close();
                } catch (IOException ex) {
                }
            }
        }
        log.debug("**************************** End SAML ValidatorCallback **************************");
    }

    /**
     * The Subject element contains the user identification information.  It is 
     * expected to follow an X509 format, but in the case that this is not 
     * proven out it will save the entire contents as being the userid.
     * @param writeOut The writer for the storage file
     * @param assertIn The Assertion element
     */
    private void writeSubject(PrintWriter writeOut, Assertion assertIn) {
        log.debug("SAMLValidator.writeSubject() -- Begin");
        if (assertIn instanceof AssertionType) {
            AssertionType assertType = (AssertionType) assertIn;
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
                            if (format != null && nameVal != null) {
                                if (format.trim().equals(X509_FORMAT)) {
                                    String extractedUID = extract509(nameVal);
                                    if (extractedUID != null) {
                                        userIdentifier = extractedUID;
                                    } else {
                                        log.warn("X509 Formatted user identifier can not be extracted");
                                    }
                                }
                            } else {
                                log.warn("Subject's NameId has an invalid format");
                            }
                            log.info(InternalTokenMgr.userNameAttrName + "=" + userIdentifier);
                            writeOut.println(InternalTokenMgr.userNameAttrName + "=" + userIdentifier);
                        }
                    }
                } else {
                    log.warn("Expected Subject information is missing.");
                }
            } else {
                log.warn("Subject element is missing.");
            }
        }
        log.debug("SAMLValidator.writeSubject() -- End");
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
    private String extract509(String in509) {
        log.debug("SAMLValidator.extract509() " + in509 + " -- Begin");
        String userVal = null;
        if (in509 != null) {
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
                log.warn("X509 NameId is improperly formed: " + iae.getMessage());
            }
        }
        log.debug("SAMLValidator.extract509() " + userVal + " -- End");
        return userVal;
    }

    /**
     * This method is responsible to extract the information from both the 
     * AttributeStatements as found in the main Assertion element as well as the 
     * AttributeStatements found in the Evidence element.  The permitted names 
     * of the Attributes in the Assertion element are: UserRole, PurposeForUse, 
     * UserName, UserOrganization. The permitted names of the Attributes in the 
     * Evidence element are: ContentReference, ContentType, and Content.
     * @param writeOut The writer for the storage file
     * @param statement The attribute statement to be extracted
     */
    private void writeAttributeInfo(PrintWriter writeOut, AttributeStatementType statement) {
        log.debug("SAMLValidator.writeAttributeInfo() -- Begin");
        List attribs = statement.getAttributeOrEncryptedAttribute();
        if (attribs != null && !attribs.isEmpty()) {
            for (int idx = 0; idx < attribs.size(); idx++) {
                if (attribs.get(idx) instanceof AttributeType) {
                    StringBuffer strBuf = new StringBuffer();
                    AttributeType attrib = (AttributeType) attribs.get(idx);
                    String nameAttr = attrib.getName();
                    if (nameAttr != null) {
                        if ("UserRole".equals(nameAttr)) {
                            parseRole(attrib, strBuf, USER_ROLE_ID);
                        } else if ("PurposeForUse".equals(nameAttr)) {
                            parseRole(attrib, strBuf, PURPOSE_ROLE_ID);
                        } else if (InternalTokenMgr.userNameAttrName.equals(nameAttr)) {
                            extractNameParts(attrib, strBuf);
                        } else if (InternalTokenMgr.userOrgAttrName.equals(nameAttr) ||
                                InternalTokenMgr.claimRefAttrName.equals(nameAttr) ||
                                InternalTokenMgr.claimFormAttrName.equals(nameAttr)) {
                            List attrVals = attrib.getAttributeValue();
                            if (attrVals != null && !attrVals.isEmpty()) {
                                strBuf.append(attrib.getName() + "=");
                                if (InternalTokenMgr.claimFormAttrName.equals(nameAttr)) {
                                    for (int idxVal = 0; idxVal < attrVals.size(); idxVal++) {
                                        Object formVal = attrVals.get(idxVal);
                                        if (formVal instanceof byte[]) {
                                            byte[] rawForm = (byte[]) attrVals.get(idxVal);
                                            String strForm = new String(rawForm);
                                            strBuf.append(strForm + " ");
                                        } else {
                                            strBuf.append(" ");
                                            log.warn(nameAttr + " Attribute is not recognized as base64 binary");
                                        }
                                    }
                                } else {
                                    for (int idxVal = 0; idxVal < attrVals.size(); idxVal++) {
                                        strBuf.append(attrVals.get(idxVal) + " ");
                                    }
                                }
                            } else {
                                log.warn("No values are provided for Attribute " + nameAttr);
                            }
                        } else if (InternalTokenMgr.claimFormTypeAttrName.equals(nameAttr)) {
                            log.info(nameAttr + " is set to default 'application\\pdf'.");
                        } else {
                            log.warn("Unrecognized Name Attribute: " + nameAttr);
                        }
                    } else {
                        log.warn("Improperly formed Name Attribute: " + nameAttr);
                    }
                    if (strBuf != null && strBuf.length() > 0) {
                        writeOut.println(strBuf.toString());
                        log.info(strBuf.toString());
                    }
                }
            }
        } else {
            log.warn("Expected Attributes are missing.");
        }

        log.debug("SAMLValidator.writeAttributeInfo() -- End");
    }

    /**
     * The value of the UserName attribute is assumed to be a user's name in 
     * plain text.  The name parts are extracted in this method as the first 
     * word constitutes the first name, the last word constitutes the last name 
     * and all other text in between these words constitute the middle name. 
     * @param attrib The Attribute that has the user name as its value
     * @param strBuf The name parts are appended to this string buffer for later 
     * output to the storage file
     */
    private void extractNameParts(AttributeType attrib, StringBuffer strBuf) {
        log.debug("SAMLValidator.extractNameParts() -- Begin");
        // Assumption is that before the 1st space reflects the first name,
        // after the last space is the last name, anything between is the middle name
        List attrVals = attrib.getAttributeValue();
        if (attrVals != null && !attrVals.isEmpty()) {
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
                        strBuf.append(InternalTokenMgr.userFirstNameAttrName + "=" + nameParts.get(0) + "\n");
                        nameParts.remove(0);
                    }

                    if (nameParts.size() > 0) {
                        strBuf.append(InternalTokenMgr.userLastNameAttrName + "=" + nameParts.get(nameParts.size() - 1) + "\n");
                        nameParts.remove(nameParts.size() - 1);
                    }

                    if (nameParts.size() > 0) {
                        strBuf.append(InternalTokenMgr.userMiddleNameAttrName + "=");
                        for (String name : nameParts) {
                            strBuf.append(name + " ");
                        }
                        // take off last blank character
                        strBuf.setLength(strBuf.length() - 1);
                    }
                    // take off last return char.
                    if (strBuf.toString().endsWith("\n")) {
                        int truncIdx = strBuf.lastIndexOf("\n");
                        strBuf.setLength(truncIdx);
                    }
                    // Once found break out of the loop
                    break;
                }
            }
        } else {
            log.warn("Expected User Name attribute is empty: " + attrVals);
        }

        log.debug("SAMLValidator.extractNameParts() -- End");
    }

    /**
     * The value of the UserRole and PurposeForUse attributes are formatted 
     * according to the specifications of an nhin:CodedElement.  This method 
     * parses that expected structure to obtain the code, codeSystem, 
     * codeSystemName, and the displayName attributes of that element.
     * @param attrib The Attribute that has the UserRole or PurposeForUse as its 
     * value
     * @param strBuf The coded element's attribute parts are appended to this 
     * string buffer for later output to the storage file
     * @param id Identifies which coded element this is parsing
     */
    private void parseRole(AttributeType attrib, StringBuffer strBuf, String id) {
        log.debug("SAMLValidator.parseRole() for " + id + " -- Begin");

        List attrVals = attrib.getAttributeValue();

        if (attrVals != null && !attrVals.isEmpty()) {
            for (int idxVal = 0; idxVal <
                    attrVals.size(); idxVal++) {
                // With our current version of Metro the get(idxVal) gives us a null
                // It was supposedly fixed by 9/30/08.
                if (attrVals.get(idxVal) instanceof AttributeValue) {
                    AttributeValue attrVal = (AttributeValue) attrVals.get(idxVal);

                    String roleVal = attrVal.toString();
                    log.info("Role value: " + roleVal);
                    TreeSet<Integer> patStartRef = new TreeSet<Integer>();
                    Map<Integer, Integer> patStartEndXRef = new HashMap<Integer, Integer>();
                    Map<Integer, String> patStartNameXRef = new HashMap<Integer, String>();

                    Pattern codePat = Pattern.compile("[cC][oO][dD][eE]\\s*?=");
                    Matcher codeMatch = codePat.matcher(roleVal);
                    while (codeMatch.find()) {
                        patStartRef.add(codeMatch.start());
                        patStartEndXRef.put(codeMatch.start(), codeMatch.end());
                        if (USER_ROLE_ID.equals(id)) {
                            patStartNameXRef.put(codeMatch.start(), InternalTokenMgr.userRoleCodeAttrName + "=");
                        } else if (PURPOSE_ROLE_ID.equals(id)) {
                            patStartNameXRef.put(codeMatch.start(), InternalTokenMgr.purposeCodeAttrName + "=");
                        } else {
                            log.warn("Unrecognized item: " + id + " Can not parse 'code'.");
                        }

                    }

                    Pattern codeSysPat = Pattern.compile("[cC][oO][dD][eE][sS][yY][sS][tT][eE][mM]\\s*?=");
                    Matcher codeSysMatch = codeSysPat.matcher(roleVal);
                    while (codeSysMatch.find()) {
                        patStartRef.add(codeSysMatch.start());
                        patStartEndXRef.put(codeSysMatch.start(), codeSysMatch.end());
                        if (USER_ROLE_ID.equals(id)) {
                            patStartNameXRef.put(codeSysMatch.start(), InternalTokenMgr.userRoleCodeSystemAttrName + "=");
                        } else if (PURPOSE_ROLE_ID.equals(id)) {
                            patStartNameXRef.put(codeSysMatch.start(), InternalTokenMgr.purposeCodeSystemAttrName + "=");
                        } else {
                            log.warn("Unrecognized item: " + id + " Can not parse 'codeSystem'.");
                        }

                    }

                    Pattern codeSysNamePat = Pattern.compile("[cC][oO][dD][eE][sS][yY][sS][tT][eE][mM][nN][aA][mM][eE]\\s*?=");
                    Matcher codeSysNameMatch = codeSysNamePat.matcher(roleVal);
                    while (codeSysNameMatch.find()) {
                        patStartRef.add(codeSysNameMatch.start());
                        patStartEndXRef.put(codeSysNameMatch.start(), codeSysNameMatch.end());
                        if (USER_ROLE_ID.equals(id)) {
                            patStartNameXRef.put(codeSysNameMatch.start(), InternalTokenMgr.userRoleCodeSystemNameAttrName + "=");
                        } else if (PURPOSE_ROLE_ID.equals(id)) {
                            patStartNameXRef.put(codeSysNameMatch.start(), InternalTokenMgr.purposeCodeSystemNameAttrName + "=");
                        } else {
                            log.warn("Unrecognized item: " + id + " Can not parse 'codeSystemName'.");
                        }

                    }

                    Pattern dispNamePat = Pattern.compile("[dD][iI][sS][pP][lL][aA][yY][nN][aA][mM][eE]\\s*?=");
                    Matcher dispNameMatch = dispNamePat.matcher(roleVal);
                    while (dispNameMatch.find()) {
                        patStartRef.add(dispNameMatch.start());
                        patStartEndXRef.put(dispNameMatch.start(), dispNameMatch.end());
                        if (USER_ROLE_ID.equals(id)) {
                            patStartNameXRef.put(dispNameMatch.start(), InternalTokenMgr.userRoleDisplayAttrName + "=");
                        } else if (PURPOSE_ROLE_ID.equals(id)) {
                            patStartNameXRef.put(dispNameMatch.start(), InternalTokenMgr.purposeDisplayAttrName + "=");
                        } else {
                            log.warn("Unrecognized item: " + id + " Can not parse 'displayName'.");
                        }

                    }

                    while (!patStartRef.isEmpty()) {
                        // get substring from the end of this to the beginning of the next
                        int keyIdx = patStartRef.pollFirst();
                        int begSubIdx = patStartEndXRef.get(keyIdx);
                        int endSubIdx = roleVal.length();
                        try {
                            endSubIdx = patStartRef.first();
                        } catch (NoSuchElementException ne) {
                        }
                        String valSubstr = roleVal.substring(begSubIdx, endSubIdx).trim();

                        // value lies between the quotes
                        String extractedVal = valSubstr;
                        int valBeg = valSubstr.indexOf("\"");
                        int valEnd = valSubstr.lastIndexOf("\"");
                        if (valBeg != -1 && valEnd != -1 && valBeg <= valEnd) {
                            extractedVal = valSubstr.substring(valBeg + 1, valEnd).trim();
                        } else {
                            log.warn("Extracted Value is in an unrecognized form: " + valSubstr);
                        }

                        log.info("Parsed Value " + patStartNameXRef.get(keyIdx) + ": " + extractedVal);
                        strBuf.append(patStartNameXRef.get(keyIdx) + extractedVal + "\n");
                    }
                    // take off last return char.
                    if (strBuf.toString().endsWith("\n")) {
                        int truncIdx = strBuf.lastIndexOf("\n");
                        strBuf.setLength(truncIdx);
                    }

                } else {
                    log.warn("Value for " + id + " attribute is not a proper AttributeValue.");
                }
                // Once found break out of the loop
                break;
            }
        } else {
            log.warn("Attributes for " + id + " are invalid: " + attrVals);
        }

        log.debug("SAMLValidator.parseRole() -- End");
    }

    /**
     * The Authorization Decision Statement is used to convey a form authorizing 
     * access to medical records.  It may embed the binary content of the 
     * authorization form as well describing the conditions of its validity.  
     * This method saves off all values associated with this Evidence.
     * @param writeOut The writer for the storage file
     * @param authzState The authorization decision element
     */
    private void writeDecisionInfo(PrintWriter writeOut, AuthzDecisionStatementType authzState) {

        log.debug("SAMLValidator.writeDecisionInfo() -- Begin");
        writeOut.println(InternalTokenMgr.resourceAttrName + "=" + authzState.getResource());
        log.info(InternalTokenMgr.resourceAttrName + "=" + authzState.getResource());

        List<ActionType> actions = authzState.getAction();
        StringBuffer actSb = new StringBuffer();
        for (ActionType act : actions) {
            actSb.append(act.getValue() + " ");
        }

        writeOut.println(InternalTokenMgr.actionAttrName + "=" + actSb.toString());
        log.info(InternalTokenMgr.actionAttrName + "=" + actSb.toString());

        EvidenceType evid = authzState.getEvidence();
        List asserts = evid.getAssertionIDRefOrAssertionURIRefOrAssertion();
        if (asserts != null && !asserts.isEmpty()) {
            for (int idx = 0; idx <
                    asserts.size(); idx++) {
                if (asserts.get(idx) instanceof JAXBElement) {
                    JAXBElement evElem = (JAXBElement) asserts.get(idx);
                    if (evElem.getValue() instanceof AssertionType) {
                        AssertionType evAss = (AssertionType) evElem.getValue();

                        writeConditionsInfo(writeOut, evAss.getConditions());

                        List statements = evAss.getStatementOrAuthnStatementOrAuthzDecisionStatement();
                        if (statements != null && !statements.isEmpty()) {
                            for (int idxState = 0; idxState <
                                    statements.size(); idxState++) {
                                if (statements.get(idxState) instanceof AttributeStatementType) {
                                    AttributeStatementType statement = (AttributeStatementType) statements.get(idxState);
                                    writeAttributeInfo(writeOut, statement);
                                }

                            }
                        } else {
                            log.warn("Evidence Statements are missing.");
                        }
                    } else {
                        log.warn("Non-Assertion type element: " + evElem.getValue() + " is not processed");
                    }
                } else {
                    log.warn("Evidence assertion is not recognized as a JAXB element");
                }
            }
        } else {
            log.warn("Evidence assertion is empty: " + asserts);
        }

        log.debug("SAMLValidator.writeDecisionInfo() -- End");
    }

    /**
     * This method extracts the dates of validity for the Evidence's 
     * authorization form.  These dates are contained in the Conditions element 
     * and are written out to the storage file by this method.
     * @param writeOut The writer for the storage file
     * @param conditions The Evidence's Conditions element
     */
    private void writeConditionsInfo(PrintWriter writeOut, ConditionsType conditions) {
        log.debug("SAMLValidator.writeConditionsInfo() -- Begin");
        if (conditions != null) {
            SimpleDateFormat dateForm = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

            XMLGregorianCalendar beginTime = conditions.getNotBefore();
            if (beginTime != null && beginTime.toGregorianCalendar() != null && beginTime.toGregorianCalendar().getTime() != null) {
                String formBegin = dateForm.format(beginTime.toGregorianCalendar().getTime());
                writeOut.println(InternalTokenMgr.signDateAttrName + "=" + formBegin);
                log.info(InternalTokenMgr.signDateAttrName + "=" + formBegin);
            }

            XMLGregorianCalendar endTime = conditions.getNotOnOrAfter();
            if (endTime != null && endTime.toGregorianCalendar() != null && endTime.toGregorianCalendar().getTime() != null) {
                String formEnd = dateForm.format(endTime.toGregorianCalendar().getTime());
                writeOut.println(InternalTokenMgr.expireDateAttrName + "=" + formEnd);
                log.info(InternalTokenMgr.expireDateAttrName + "=" + formEnd);
            }

        }
        log.debug("SAMLValidator.writeConditionsInfo() -- End");
    }
}
