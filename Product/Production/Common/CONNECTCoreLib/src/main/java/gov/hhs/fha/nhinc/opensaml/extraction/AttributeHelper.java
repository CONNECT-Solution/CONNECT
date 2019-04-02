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
package gov.hhs.fha.nhinc.opensaml.extraction;

import gov.hhs.fha.nhinc.callback.SamlConstants;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.schema.impl.XSAnyImpl;
import org.opensaml.core.xml.schema.impl.XSStringImpl;
import org.opensaml.saml.saml2.core.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

        if (CollectionUtils.isNotEmpty(attrVals)) {
            LOG.trace("AttributeValue is: {}", attrVals.get(0).getClass());
            // According to the NHIN specifications - there should be exactly one value.
            // If there is more than one. We will take only the first one.
            // ---------------------------------------------------------------------------
            NodeList nodelist = null;
            if (attrVals.get(0) instanceof XSAnyImpl) {
                XSAnyImpl elem = (XSAnyImpl) attrVals.get(0);

                nodelist = elem.getDOM().getChildNodes();
            } else {
                LOG.error("The value for the {} attribute is a: {} expected an XSAnyImpl", codeId,
                    attrVals.get(0).getClass());
            }
            if (nodelist != null && nodelist.getLength() > 0) {
                int numNodes = nodelist.getLength();
                for (int idx = 0; idx < numNodes; idx++) {
                    compareAttrMap(nodelist, ce, codeId, idx);
                }
            } else {
                LOG.error("The AttributeValue for {} should have a Child Node", codeId);
            }
        } else {
            LOG.error("Attributes for {} are invalid: {} ", codeId, attrVals);
        }

        LOG.debug("Exiting AttributeHelper.extractNhinCodedElement...");
        return ce;
    }

    /**
     * @param attrNode
     * @param ce
     */
    private static void compareAttrMap(NodeList nodelist, CeType ce, String codeId, int idx) {
        if (nodelist.item(idx) != null) {
            Node node = nodelist.item(idx);
            NamedNodeMap attrMap = node.getAttributes();
            if (attrMap != null && attrMap.getLength() > 0) {
                int numMapNodes = attrMap.getLength();
                for (int attrIdx = 0; attrIdx < numMapNodes; attrIdx++) {
                    Node attrNode = attrMap.item(attrIdx);
                    compareAttrNode(attrNode, ce, codeId);

                }
            } else {
                LOG.debug("Attribute map is null");
            }
        } else {
            LOG.debug("Expected AttributeValue to have a Node child");
        }
    }

    private static void compareAttrNode(Node attrNode, CeType ce, String codeId) {
        if (attrNode != null && attrNode.getNodeName() != null && !attrNode.getNodeName().isEmpty()) {
            setCode(attrNode, ce, codeId);
        } else {
            LOG.debug("Attribute name can not be processed");
        }
    }

    /**
     * @param attrNode
     * @param ce
     * @param codeId
     */
    private static void setCode(Node attrNode, CeType ce, String codeId) {
        String nodeName = attrNode.getNodeName();
        if (SamlConstants.CE_CODE_ID.equalsIgnoreCase(nodeName)) {
            ce.setCode(attrNode.getNodeValue());
            LOG.trace("{}: ce.Code = {}", codeId, ce.getCode());
        }
        if (SamlConstants.CE_CODESYS_ID.equalsIgnoreCase(nodeName)) {
            ce.setCodeSystem(attrNode.getNodeValue());
            LOG.trace("{}: ce.CodeSystem = {}", codeId, ce.getCodeSystem());
        }
        if (SamlConstants.CE_CODESYSNAME_ID.equalsIgnoreCase(nodeName)) {
            ce.setCodeSystemName(attrNode.getNodeValue());
            LOG.trace("{}: ce.CodeSystemName = {}", codeId, ce.getCodeSystemName());
        }
        if (SamlConstants.CE_DISPLAYNAME_ID.equalsIgnoreCase(nodeName)) {
            ce.setDisplayName(attrNode.getNodeValue());
            LOG.trace("{}: ce.DisplayName = {}", codeId, ce.getDisplayName());
        }
    }

    /**
     * This method takes an attribute and extracts the string value of the attribute. If the attribute has multiple
     * values, then it concatenates all of the values.
     *
     * @param attrib The attribute containing the string value.
     * @return The string value (or if there are multiple values, the concatenated string value.)
     */
    public String extractAttributeValueString(Attribute attrib) {

        StringBuilder strBuilder = new StringBuilder();

        if (attrib != null) {
            List<XMLObject> attrVals = attrib.getAttributeValues();
            for (Object o : attrVals) {
                if (o instanceof org.w3c.dom.Element) {
                    org.w3c.dom.Element elem = (org.w3c.dom.Element) o;
                    strBuilder.append(elem.getTextContent());

                    // we break here because per the nhin specification, there should only be one attribute value.
                    break;
                } else if (o instanceof String) {
                    strBuilder.append(o).append(" ");

                    // we DO NOT break here despite the nhin specification because the previous algorithm for handling
                    // these Strings handled multiple values. Until I understand
                    // why the string values are treated differently I am not going to change this logic.
                } else if (o instanceof XSStringImpl) {
                    strBuilder.append(((XSStringImpl) o).getValue());
                } else if (o instanceof XSAnyImpl) {
                    strBuilder.append(((XSAnyImpl) o).getTextContent());
                }
            }
        }
        return strBuilder.toString().trim();
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
        if (CollectionUtils.isNotEmpty(attrVals)) {

            PersonNameType personName = assertOut.getUserInfo().getPersonName();

            // Although SAML allows for multiple attribute values, the NHIN Specification
            // states that for a name there will be one attribute value. So we will
            // only look at the first one. If there are more, the first is the only one
            // that will be used.
            // -----------------------------------------------------------------------------
            String completeName = extractAttributeValueString(attrib);
            personName.setFullName(completeName);
            LOG.trace("Assertion.userInfo.personName.FullName = {}", completeName);
            String[] nameTokens = completeName.split("\\s");
            ArrayList<String> nameParts = new ArrayList<>();

            for (String tok : nameTokens) {
                if (tok.trim() != null && tok.trim().length() > 0) {
                    nameParts.add(tok.trim());
                }
            }
            // remove blank tokens
            removeTokens(nameParts, personName);

        } else {
            LOG.error("User Name attribute is empty: {}", attrVals);
        }

        LOG.debug("AttributeHelper.extractNameParts() -- End");
    }

    /**
     * @param nameParts
     * @param nameTokens
     * @param personName
     */
    private static void removeTokens(ArrayList<String> nameParts, PersonNameType personName) {

        if (!nameParts.isEmpty() && !nameParts.get(0).isEmpty()) {
            personName.setGivenName(nameParts.get(0));
            nameParts.remove(0);
            LOG.trace("Assertion.userInfo.personName.givenName = {}", personName.getGivenName());
        }

        if (!nameParts.isEmpty() && !nameParts.get(nameParts.size() - 1).isEmpty()) {
            personName.setFamilyName(nameParts.get(nameParts.size() - 1));
            nameParts.remove(nameParts.size() - 1);
            LOG.trace("Assertion.userInfo.personName.familyName = {}", personName.getFamilyName());
        }

        if (!nameParts.isEmpty()) {
            StringBuilder midName = new StringBuilder();
            for (String name : nameParts) {
                midName.append(name + " ");
            }
            // take off last blank character
            midName.setLength(midName.length() - 1);
            personName.setSecondNameOrInitials(midName.toString());
            LOG.trace("Assertion.userInfo.personName.secondNameOrInitials = {}", personName.getSecondNameOrInitials());
        }
    }
}
