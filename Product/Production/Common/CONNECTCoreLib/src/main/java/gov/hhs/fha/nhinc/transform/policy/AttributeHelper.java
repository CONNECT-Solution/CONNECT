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
package gov.hhs.fha.nhinc.transform.policy;

import gov.hhs.fha.nhinc.util.Base64Coder;
import gov.hhs.fha.nhinc.util.StringUtil;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import oasis.names.tc.xacml._2_0.context.schema.os.AttributeType;
import oasis.names.tc.xacml._2_0.context.schema.os.AttributeValueType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResourceType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;
import org.hl7.v3.II;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author rayj
 */
public class AttributeHelper {

    private static final Logger LOG = LoggerFactory.getLogger(AttributeHelper.class);

    public AttributeType attributeFactory(String attributeId, String dataType, String value) {
        return attributeFactory(attributeId, dataType, (Object) value);
    }

    public AttributeType attributeFactory(String attributeId, String dataType, Object value) {
        LOG.debug("creating XACML attribute [id='" + attributeId + "'; value='" + value + "'; datatype='" + dataType
            + "']");

        // There is a problem if the value is null. If that occurs then we get a XACML Attribute outer tag
        // and no inner "<AttributeValue> tag. This causes the receiving PEP to error out because it should not occur.
        // if we return null, then the entire attribute will not be added - which is what we want. Note that an "add" to
        // a JAXB list where the value is null turns into a noop.
        // -----------------------------------------------------------------------------------------------------------------
        if (value == null) {
            LOG.debug(
                "XACML attribute [id='" + attributeId + "' was null - returning null - no atribute will be added.");
            return null;
        }

        AttributeType attribute = new AttributeType();
        attribute.setAttributeId(attributeId);
        attribute.setDataType(dataType);

        AttributeValueType atttibuteValue = new AttributeValueType();
        if (value instanceof String) {
            atttibuteValue.getContent().add(value);
        } else if (value instanceof List) {
            atttibuteValue.getContent().addAll((List<String>) value);
        } else if (value instanceof byte[]) {
            try {
                String sValue = StringUtil.convertToStringUTF8((byte[]) value); // Note that JAXB already decoded this.
                // We need to re-encode it.
                String sEncodedValue = Base64Coder.encodeString(sValue);
                atttibuteValue.getContent().add(sEncodedValue);
            } catch (UnsupportedEncodingException ex) {
                LOG.error("Error converting String to UTF8 format: {}", ex.getLocalizedMessage(), ex);
            }
        } else if (value instanceof II) {
            II iiValue = (II) value;
            try {
                Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                Element iiElement = doc.createElementNS("urn:hl7-org:v3", "hl7:PatientId");
                iiElement.setAttribute("root", iiValue.getRoot());
                iiElement.setAttribute("extension", iiValue.getExtension());
                atttibuteValue.getContent().add(iiElement);
            } catch (ParserConfigurationException | DOMException e) {
                LOG.error("Unable to add II attribute: {}", e.getLocalizedMessage(), e);
            }
        } else {
            // Note sure what to do with the rest - just put them in...
            // ----------------------------------------------------------
            atttibuteValue.getContent().add(value);
        }
        attribute.getAttributeValue().add(atttibuteValue);

        return attribute;
    }

    public AttributeType appendAttributeToParent(SubjectType subject, String attributeId, String dataType,
        String attributeValue, boolean appendIfValueNull) {
        return appendAttributeToParent(subject, attributeId, dataType, (Object) attributeValue, appendIfValueNull);
    }

    public AttributeType appendAttributeToParent(SubjectType subject, String attributeId, String dataType,
        URI attributeValue, boolean appendIfValueNull) {
        String strAttributeVal = null;
        if (attributeValue != null) {
            strAttributeVal = attributeValue.toString();
        }
        return appendAttributeToParent(subject, attributeId, dataType, (Object) strAttributeVal, appendIfValueNull);
    }

    public AttributeType appendAttributeToParent(SubjectType subject, String attributeId, String dataType,
        Object attributeValue, boolean appendIfValueNull) {
        AttributeType attribute = null;
        if (attributeValue != null) {
            attribute = attributeFactory(attributeId, dataType, attributeValue);
            subject.getAttribute().add(attribute);
        }
        return attribute;
    }

    public AttributeType appendAttributeToParent(ResourceType resource, String attributeId, String dataType,
        String attributeValue, boolean appendIfValueNull) {
        return appendAttributeToParent(resource, attributeId, dataType, (Object) attributeValue, appendIfValueNull);
    }

    public AttributeType appendAttributeToParent(ResourceType resource, String attributeId, String dataType,
        XMLGregorianCalendar attributeValue, boolean appendIfValueNull) {
        String strAttributeVal = null;
        if (attributeValue != null) {
            strAttributeVal = attributeValue.toXMLFormat();
        }
        return appendAttributeToParent(resource, attributeId, dataType, (Object) strAttributeVal, appendIfValueNull);
    }

    public AttributeType appendAttributeToParent(ResourceType resource, String attributeId, String dataType,
        URI attributeValue, boolean appendIfValueNull) {
        String strAttributeVal = null;
        if (attributeValue != null) {
            strAttributeVal = attributeValue.toString();
        }
        return appendAttributeToParent(resource, attributeId, dataType, (Object) strAttributeVal, appendIfValueNull);
    }

    public AttributeType appendAttributeToParent(ResourceType resource, String attributeId, String dataType,
        Object attributeValue, boolean appendIfValueNull) {
        AttributeType attribute = null;
        if (attributeValue != null) {
            attribute = attributeFactory(attributeId, dataType, attributeValue);
            resource.getAttribute().add(attribute);
        }
        return attribute;
    }

    public AttributeType getSingleAttribute(List<AttributeType> attributeList, String attributeID) {
        AttributeType matchingAttribute = null;
        for (AttributeType attribute : attributeList) {
            if (attribute.getAttributeId().contentEquals(attributeID)) {
                if (matchingAttribute == null) {
                    matchingAttribute = attribute;
                } else {
                    throw new IllegalArgumentException(
                        "getSingleAttribute() assumes that there is a single matching attribute in list.  List contained multiple items with attributeId='"
                            + attributeID + "'.");
                }
            }
        }
        return matchingAttribute;
    }

    public Object getSingleContentValue(AttributeType attribute) {
        // if there a multiple attribute value or contents, return error
        // if no attribute value or content, return null
        Object contentValue = null;
        if (attribute != null) {
            if (attribute.getAttributeValue().size() > 1) {
                throw new IllegalArgumentException(
                    "getSingleContentValue assumes that attribute contains a single attribute value.");
            }
            if (attribute.getAttributeValue().size() == 1) {
                AttributeValueType attributeValue = attribute.getAttributeValue().get(0);
                if (attributeValue.getContent().size() > 1) {
                    throw new IllegalArgumentException(
                        "getSingleContentValue assumes that attribute value contains a single content item.");
                }
                if (attributeValue.getContent().size() == 1) {
                    contentValue = attributeValue.getContent().get(0);
                }
            }
        }
        return contentValue;
    }
}
