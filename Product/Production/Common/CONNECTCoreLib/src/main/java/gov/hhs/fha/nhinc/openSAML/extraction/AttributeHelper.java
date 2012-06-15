/**
 * 
 */
package gov.hhs.fha.nhinc.openSAML.extraction;

import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.xerces.dom.ElementNSImpl;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.xml.schema.impl.XSStringImpl;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author mweaver
 *
 */
public class AttributeHelper {
    private static final Logger log = Logger.getLogger(AttributeHelper.class);
    /**
     * The value of the UserRole and PurposeOfUse attributes are formatted according to the specifications of an
     * nhin:CodedElement. This method parses that expected structure to obtain the code, codeSystem, codeSystemName, and
     * the displayName attributes of that element.
     * 
     * @param attrib The Attribute that has the UserRole or PurposeOfUse as its value
     * @param assertOut The Assertion element being written to
     * @param codeId Identifies which coded element this is parsing
     */
    public CeType extractNhinCodedElement(Attribute attrib, String codeId) {
        log.debug("Entering SamlTokenExtractor.extractNhinCodedElement...");

        CeType ce = new CeType();
        ce.setCode("");
        ce.setCodeSystem("");
        ce.setCodeSystemName("");
        ce.setDisplayName("");

        List attrVals = attrib.getAttributeValues();
        // log.debug("extractNhinCodedElement: " + attrib.getName() + " has " + attrVals.size() + " values");

        if ((attrVals != null) && (attrVals.size() > 0)) {
            log.debug("AttributeValue is: " + attrVals.get(0).getClass());
            // According to the NHIN specifications - there should be exactly one value.
            // If there is more than one. We will take only the first one.
            // ---------------------------------------------------------------------------
            NodeList nodelist = null;
            if (attrVals.get(0) instanceof ElementNSImpl) {
                ElementNSImpl elem = (ElementNSImpl) attrVals.get(0);
                nodelist = elem.getChildNodes();
            } else {
                log.error("The value for the " + codeId + " attribute is a: " + attrVals.get(0).getClass()
                        + " expected a ElementNSImpl");
            }
            if ((nodelist != null) && (nodelist.getLength() > 0)) {
                int numNodes = nodelist.getLength();
                for (int idx = 0; idx < numNodes; idx++) {
                    if (nodelist.item(idx) instanceof Node) {
                        // log.debug("Processing index:" + idx + " node as " + nodelist.item(idx).getNodeName());
                        Node node = (Node) nodelist.item(idx);
                        NamedNodeMap attrMap = node.getAttributes();
                        if ((attrMap != null) && (attrMap.getLength() > 0)) {
                            int numMapNodes = attrMap.getLength();
                            for (int attrIdx = 0; attrIdx < numMapNodes; attrIdx++) {
                                // log.debug("Processing attribute index:" + attrIdx + " as " + attrMap.item(attrIdx));
                                Node attrNode = attrMap.item(attrIdx);
                                if ((attrNode != null) && (attrNode.getNodeName() != null)
                                        && (!attrNode.getNodeName().isEmpty())) {
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
                            } // for (int attrIdx = 0; attrIdx < numMapNodes; attrIdx++) {
                        } else {
                            log.debug("Attribute map is null");
                        }
                    } else {
                        log.debug("Expected AttributeValue to have a Node child");
                    }
                } // for (int idx = 0; idx < numNodes; idx++) {
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
     * This method takes an attribute and extracts the string value of the attribute. If the attribute has multiple
     * values, then it concatenates all of the values.
     * 
     * @param attrib The attribute containing the string value.
     * @return The string value (or if there are multiple values, the concatenated string value.)
     */
    public String extractAttributeValueString(Attribute attrib) {
        // this method was rewritten for GATEWAY-426
        StringBuffer strBuf = new StringBuffer();

        if (attrib != null) {
            List attrVals = attrib.getAttributeValues();
            for (Object o : attrVals) {
                if (o instanceof org.w3c.dom.Element) {
                    org.w3c.dom.Element elem = (org.w3c.dom.Element) o;
                    strBuf.append(elem.getTextContent());

                    // we break here because per the nhin specification, there should only be one attribute value.
                    break;
                } else if (o instanceof String) {
                    strBuf.append((String) o + " ");

                    // we DO NOT break here despite the nhin specification because the previous algorithm for handling
                    // these Strings handled multiple values. Until I understand
                    // why the string values are treated differently I am not going to change this logic.
                } else if (o instanceof XSStringImpl) {
                    strBuf.append(((XSStringImpl)o).getValue());
                }
            }
        }

        return strBuf.toString().trim();
    }
}
