/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.policy;

import java.net.URI;
import oasis.names.tc.xacml._2_0.context.schema.os.AttributeType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;
import oasis.names.tc.xacml._2_0.context.schema.os.AttributeValueType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResourceType;
import gov.hhs.fha.nhinc.util.Base64Coder;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilderFactory;
import org.hl7.v3.II;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author rayj
 */
public class AttributeHelper {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(AttributeHelper.class);

    public AttributeType attributeFactory(String attributeId, String dataType, String value) {
        return attributeFactory(attributeId, dataType, (Object) value);
    }

    public AttributeType attributeFactory(String attributeId, String dataType, Object value) {
        log.debug("creating XACML attribute [id='" + attributeId + "'; value='" + value + "'; datatype='" + dataType + "']");

        // There is a problem if the value is null.  If that occurs then we get a XACML Attribute outer tag
        // and no inner "<AttributeValue> tag.  This causes the receiving PEP to error out because it should not occur.
        // if we return null, then the entire attribute will not be added - which is what we want.  Note that an "add" to
        // a JAXB list where the value is null turns into a noop.
        //-----------------------------------------------------------------------------------------------------------------
        if (value == null) {
            log.debug("XACML attribute [id='" + attributeId + "' was null - returning null - no atribute will be added.");
            return null;
        }

        AttributeType attribute = new AttributeType();
        attribute.setAttributeId(attributeId);
        attribute.setDataType(dataType);

        AttributeValueType atttibuteValue = new AttributeValueType();
        if (value instanceof String) {
            atttibuteValue.getContent().add(value);
        } else if (value instanceof byte[]) {
            String sValue = new String((byte[]) value);     // Note that JAXB already decoded this.  We need to re-encode it.
            String sEncodedValue = Base64Coder.encodeString(sValue);
            atttibuteValue.getContent().add(sEncodedValue);
        } else if (value instanceof II) {
            II iiValue = (II) value;
            try {
                Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                Element iiElement = doc.createElementNS("urn:hl7-org:v3", "hl7:PatientId");
                iiElement.setAttribute("root", iiValue.getRoot());
                iiElement.setAttribute("extension", iiValue.getExtension());
                atttibuteValue.getContent().add(iiElement);
            } catch (Exception e) {
                log.error("Unable to add II attribute " + e.getMessage());
            }
        } else {
            // Note sure what to do with the rest - just put them in...
            //----------------------------------------------------------
            atttibuteValue.getContent().add(value);
        }
        attribute.getAttributeValue().add(atttibuteValue);

        return attribute;
    }

    public AttributeType appendAttributeToParent(SubjectType subject, String attributeId, String dataType, String attributeValue, boolean appendIfValueNull) {
        return appendAttributeToParent(subject, attributeId, dataType, (Object) attributeValue, appendIfValueNull);
    }

    public AttributeType appendAttributeToParent(SubjectType subject, String attributeId, String dataType, URI attributeValue, boolean appendIfValueNull) {
        String strAttributeVal = null;
        if(attributeValue != null){
            strAttributeVal = attributeValue.toString();
        }
        return appendAttributeToParent(subject, attributeId, dataType, (Object) strAttributeVal, appendIfValueNull);
    }

    public AttributeType appendAttributeToParent(SubjectType subject, String attributeId, String dataType, Object attributeValue, boolean appendIfValueNull) {
        AttributeType attribute = null;
        if (attributeValue != null) {
            attribute = attributeFactory(attributeId, dataType, attributeValue);
            subject.getAttribute().add(attribute);
        }
        return attribute;
    }

    public AttributeType appendAttributeToParent(ResourceType resource, String attributeId, String dataType, String attributeValue, boolean appendIfValueNull) {
        return appendAttributeToParent(resource, attributeId, dataType, (Object) attributeValue, appendIfValueNull);
    }

    public AttributeType appendAttributeToParent(ResourceType resource, String attributeId, String dataType, XMLGregorianCalendar attributeValue, boolean appendIfValueNull) {
        String strAttributeVal = null;
        if(attributeValue != null){
            strAttributeVal = attributeValue.toXMLFormat();
        }
        return appendAttributeToParent(resource, attributeId, dataType, (Object) strAttributeVal, appendIfValueNull);
    }

    public AttributeType appendAttributeToParent(ResourceType resource, String attributeId, String dataType, URI attributeValue, boolean appendIfValueNull) {
        String strAttributeVal = null;
        if(attributeValue != null){
            strAttributeVal = attributeValue.toString();
        }
        return appendAttributeToParent(resource, attributeId, dataType, (Object) strAttributeVal, appendIfValueNull);
    }

    public AttributeType appendAttributeToParent(ResourceType resource, String attributeId, String dataType, Object attributeValue, boolean appendIfValueNull) {
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
                    throw new IllegalArgumentException("getSingleAttribute() assumes that there is a single matching attribute in list.  List contained multiple items with attributeId='" + attributeID + "'.");
                }
            }
        }
        return matchingAttribute;
    }

    public Object getSingleContentValue(AttributeType attribute) {
        //returns attribute.getAttributeValue().get(0).getContent().get(0);
        //if there a multiple attribute value or contents, return error
        //if no attribute value or content, return null
        Object contentValue = null;
        if (attribute != null) {
            if (attribute.getAttributeValue().size() > 1) {
                throw new IllegalArgumentException("getSingleContentValue assumes that attribute contains a single attribute value.");
            }
            if (attribute.getAttributeValue().size() == 1) {
                AttributeValueType attributeValue = attribute.getAttributeValue().get(0);
                if (attributeValue.getContent().size() > 1) {
                    throw new IllegalArgumentException("getSingleContentValue assumes that attribute value contains a single content item.");
                }
                if (attributeValue.getContent().size() == 1) {
                    contentValue = attributeValue.getContent().get(0);
                }
            }
        }
        return contentValue;
    }
}
