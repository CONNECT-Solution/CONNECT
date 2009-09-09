/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.policy;

import oasis.names.tc.xacml._2_0.context.schema.os.AttributeType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;
import oasis.names.tc.xacml._2_0.context.schema.os.AttributeValueType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResourceType;

/**
 *
 * @author rayj
 */
public class AttributeHelper {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(AttributeHelper.class);

    public static AttributeType attributeFactory(String attributeId, String dataType, String value) {
        return attributeFactory(attributeId, dataType, (Object) value);
    }

    public static AttributeType attributeFactory(String attributeId, String dataType, Object value) {
        log.debug("creating XACML attribute [id='" + attributeId + "'; value='" + value + "'; datatype='" + dataType + "']");

        AttributeType attribute = new AttributeType();
        attribute.setAttributeId(attributeId);
        attribute.setDataType(dataType);

        AttributeValueType atttibuteValue = new AttributeValueType();
        atttibuteValue.getContent().add(value);
        attribute.getAttributeValue().add(atttibuteValue);

        return attribute;
    }

    public static AttributeType appendAttributeToParent(SubjectType subject, String attributeId, String dataType, String attributeValue, boolean appendIfValueNull) {
        return appendAttributeToParent(subject, attributeId, dataType, (Object) attributeValue, appendIfValueNull);
    }

    public static AttributeType appendAttributeToParent(SubjectType subject, String attributeId, String dataType, Object attributeValue, boolean appendIfValueNull) {
        AttributeType attribute = null;
        if (attributeValue != null) {
            attribute = AttributeHelper.attributeFactory(attributeId, dataType, attributeValue);
            subject.getAttribute().add(attribute);
        }
        return attribute;
    }

    public static AttributeType appendAttributeToParent(ResourceType resource, String attributeId, String dataType, String attributeValue, boolean appendIfValueNull) {
        return appendAttributeToParent(resource, attributeId, dataType, (Object) attributeValue, appendIfValueNull);
    }

    public static AttributeType appendAttributeToParent(ResourceType resource, String attributeId, String dataType, Object attributeValue, boolean appendIfValueNull) {
        AttributeType attribute = null;
        if (attributeValue != null) {
            attribute = AttributeHelper.attributeFactory(attributeId, dataType, attributeValue);
            resource.getAttribute().add(attribute);
        }
        return attribute;
    }
}
