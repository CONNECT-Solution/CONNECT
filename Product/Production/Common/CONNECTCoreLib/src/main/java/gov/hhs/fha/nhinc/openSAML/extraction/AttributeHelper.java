/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.openSAML.extraction;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.impl.XSAnyImpl;
import org.opensaml.xml.schema.impl.XSStringImpl;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author mweaver
 *
 */
public class AttributeHelper {

    private static final Logger LOG = LoggerFactory.getLogger(AttributeHelper.class);

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
        LOG.debug("Entering AttributeHelper.extractNhinCodedElement...");

        CeType ce = new CeType();
        ce.setCode("");
        ce.setCodeSystem("");
        ce.setCodeSystemName("");
        ce.setDisplayName("");

        List<XMLObject> attrVals = attrib.getAttributeValues();

        if ((attrVals != null) && (attrVals.size() > 0)) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("AttributeValue is: " + attrVals.get(0).getClass());
            }
            // According to the NHIN specifications - there should be exactly one value.
            // If there is more than one. We will take only the first one.
            // ---------------------------------------------------------------------------
            NodeList nodelist = null;
            if (attrVals.get(0) instanceof XSAnyImpl) {
                XSAnyImpl elem = (XSAnyImpl) attrVals.get(0);

                nodelist = elem.getDOM().getChildNodes();
            } else {
                LOG.error("The value for the " + codeId + " attribute is a: " + attrVals.get(0).getClass()
                    + " expected an XSAnyImpl");
            }
            if ((nodelist != null) && (nodelist.getLength() > 0)) {
                int numNodes = nodelist.getLength();
                for (int idx = 0; idx < numNodes; idx++) {
                    if (nodelist.item(idx) != null) {
                        Node node = nodelist.item(idx);
                        NamedNodeMap attrMap = node.getAttributes();
                        if ((attrMap != null) && (attrMap.getLength() > 0)) {
                            int numMapNodes = attrMap.getLength();
                            for (int attrIdx = 0; attrIdx < numMapNodes; attrIdx++) {
                                Node attrNode = attrMap.item(attrIdx);
                                if ((attrNode != null) && (attrNode.getNodeName() != null)
                                    && (!attrNode.getNodeName().isEmpty())) {
                                    if (attrNode.getNodeName().equalsIgnoreCase(NhincConstants.CE_CODE_ID)) {
                                        ce.setCode(attrNode.getNodeValue());
                                        if (LOG.isTraceEnabled()) {
                                            LOG.trace(codeId + ": ce.Code = " + ce.getCode());
                                        }
                                    }
                                    if (attrNode.getNodeName().equalsIgnoreCase(NhincConstants.CE_CODESYS_ID)) {
                                        ce.setCodeSystem(attrNode.getNodeValue());
                                        if (LOG.isTraceEnabled()) {
                                            LOG.trace(codeId + ": ce.CodeSystem = " + ce.getCodeSystem());
                                        }
                                    }
                                    if (attrNode.getNodeName().equalsIgnoreCase(NhincConstants.CE_CODESYSNAME_ID)) {
                                        ce.setCodeSystemName(attrNode.getNodeValue());
                                        if (LOG.isTraceEnabled()) {
                                            LOG.trace(codeId + ": ce.CodeSystemName = " + ce.getCodeSystemName());
                                        }
                                    }
                                    if (attrNode.getNodeName().equalsIgnoreCase(NhincConstants.CE_DISPLAYNAME_ID)) {
                                        ce.setDisplayName(attrNode.getNodeValue());
                                        if (LOG.isTraceEnabled()) {
                                            LOG.trace(codeId + ": ce.DisplayName = " + ce.getDisplayName());
                                        }
                                    }
                                } else {
                                    LOG.debug("Attribute name can not be processed");
                                }
                            } // for (int attrIdx = 0; attrIdx < numMapNodes; attrIdx++) {
                        } else {
                            LOG.debug("Attribute map is null");
                        }
                    } else {
                        LOG.debug("Expected AttributeValue to have a Node child");
                    }
                } // for (int idx = 0; idx < numNodes; idx++) {
            } else {
                LOG.error("The AttributeValue for " + codeId + " should have a Child Node");
            }
        } else {
            LOG.error("Attributes for " + codeId + " are invalid: " + attrVals);
        }

        LOG.debug("Exiting AttributeHelper.extractNhinCodedElement...");
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
            List<XMLObject> attrVals = attrib.getAttributeValues();
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
                    strBuf.append(((XSStringImpl) o).getValue());
                }
            }
        }
        return strBuf.toString().trim();
    }

    /**
     * The value of the UserName attribute is assumed to be a user's name in plain text. The name parts are extracted in
     * this method as the first word constitutes the first name, the last word constitutes the last name and all other
     * text in between these words constitute the middle name.
     *
     * @param attrib The Attribute that has the user name as its value
     * @param assertOut The Assertion element being written to
     */
    public void extractNameParts(Attribute attrib, AssertionType assertOut) {
        LOG.debug("Entering AttributeHelper.extractNameParts...");

        // Assumption is that before the 1st space reflects the first name,
        // after the last space is the last name, anything between is the middle name
        List<XMLObject> attrVals = attrib.getAttributeValues();
        if ((attrVals != null) && (attrVals.size() >= 1)) {
            PersonNameType personName = assertOut.getUserInfo().getPersonName();

            // Although SAML allows for multiple attribute values, the NHIN Specification
            // states that for a name there will be one attribute value. So we will
            // only look at the first one. If there are more, the first is the only one
            // that will be used.
            // -----------------------------------------------------------------------------
            String completeName = extractAttributeValueString(attrib);
            personName.setFullName(completeName);
            if (LOG.isTraceEnabled()) {
                LOG.trace("Assertion.userInfo.personName.FullName = " + completeName);
            }

            String[] nameTokens = completeName.split("\\s");
            ArrayList<String> nameParts = new ArrayList<String>();

            // remove blank tokens
            for (String tok : nameTokens) {
                if (tok.trim() != null && tok.trim().length() > 0) {
                    nameParts.add(tok.trim());
                }
            }

            if (nameParts.size() > 0) {
                if (!nameParts.get(0).isEmpty()) {
                    personName.setGivenName(nameParts.get(0));
                    nameParts.remove(0);
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("Assertion.userInfo.personName.givenName = " + personName.getGivenName());
                    }
                }
            }

            if (nameParts.size() > 0) {
                if (!nameParts.get(nameParts.size() - 1).isEmpty()) {
                    personName.setFamilyName(nameParts.get(nameParts.size() - 1));
                    nameParts.remove(nameParts.size() - 1);
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("Assertion.userInfo.personName.familyName = " + personName.getFamilyName());
                    }
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
                if (LOG.isTraceEnabled()) {
                    LOG.trace("Assertion.userInfo.personName.secondNameOrInitials = "
                        + personName.getSecondNameOrInitials());
                }
            }
        } else {
            LOG.error("User Name attribute is empty: " + attrVals);
        }

        LOG.debug("AttributeHelper.extractNameParts() -- End");
    }
}
