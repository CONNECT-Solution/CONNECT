/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.saml.extraction;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.ArrayList;
import org.apache.xerces.dom.ElementNSImpl;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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

/**
 *
 * @author mweaver
 */
public class AttributeTypeHelper {

    private static Log log = LogFactory.getLog(AttributeTypeHelper.class);
    private static final String EMPTY_STRING = "";

    /**
     * This method takes an attribute and extracts the string value of the
     * attribute.  If the attribute has multiple values, then it concatenates
     * all of the values.
     *
     * @param attrib The attribute containing the string value.
     * @return The string value (or if there are multiple values, the concatenated string value.)
     */
    public static String extractAttributeValueString(AttributeType attrib) {
        // this method was rewritten for GATEWAY-426
        StringBuffer strBuf = new StringBuffer();

        if (attrib != null) {
            List attrVals = attrib.getAttributeValue();
            for (Object o : attrVals) {
                if (o instanceof org.w3c.dom.Element) {
                    org.w3c.dom.Element elem = (org.w3c.dom.Element) o;
                    strBuf.append(elem.getTextContent());

                    // we break here because per the nhin specification, there should only be one attribute value.
                    break;
                } else if (o instanceof String) {
                    strBuf.append((String) o + " ");

                    // we DO NOT break here despite the nhin specification because the previous algorithm for handling these Strings handled multiple values. Until I understand
                    // why the string values are treated differently I am not going to change this logic.
                }
            }
        }

        return strBuf.toString().trim();
    }

    /**
     * This method takes an attribute and extracts the base64Encoded value from the first
     * attribute value.
     *
     * @param attrib The attribute containing the string value.
     * @return The string value (or if there are multiple values, the concatenated string value.)
     */
    public static byte[] extractFirstAttributeValueBase64Binary(AttributeType attrib) {
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
    public static void extractNameParts(AttributeType attrib, AssertionType assertOut) {
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
            String completeName = extractAttributeValueString(attrib);
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
     * The value of the UserRole and PurposeOfUse attributes are formatted
     * according to the specifications of an nhin:CodedElement.  This method
     * parses that expected structure to obtain the code, codeSystem,
     * codeSystemName, and the displayName attributes of that element.
     * @param attrib The Attribute that has the UserRole or PurposeOfUse as its
     * value
     * @param assertOut The Assertion element being written to
     * @param codeId Identifies which coded element this is parsing
     */
    public static CeType extractNhinCodedElement(AttributeType attrib, String codeId) {
        log.debug("Entering SamlTokenExtractor.extractNhinCodedElement...");

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
}
