/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.policy;

import oasis.names.tc.xacml._2_0.context.schema.os.AttributeType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;
import oasis.names.tc.xacml._2_0.context.schema.os.AttributeValueType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResourceType;
import gov.hhs.fha.nhinc.callback.Base64Coder;

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
        
        // There is a problem if the value is null.  If that occurs then we get a XACML Attribute outer tag
        // and no inner "<AttributeValue> tag.  This causes the receiving PEP to error out because it should not occur.
        // if we return null, then the entire attribute will not be added - which is what we want.  Note that an "add" to
        // a JAXB list where the value is null turns into a noop.
        //-----------------------------------------------------------------------------------------------------------------
        if (value == null)
        {
            log.debug("XACML attribute [id='" + attributeId + "' was null - returning null - no atribute will be added.");
            return null;
        }

        AttributeType attribute = new AttributeType();
        attribute.setAttributeId(attributeId);
        attribute.setDataType(dataType);

        AttributeValueType atttibuteValue = new AttributeValueType();
        if (value instanceof String) {
            atttibuteValue.getContent().add(value);
        }
        else if (value  instanceof byte[]) {
            String sValue = new String((byte[]) value);     // Note that JAXB already decoded this.  We need to re-encode it.
            String sEncodedValue = Base64Coder.encodeString(sValue);
            atttibuteValue.getContent().add(sEncodedValue);
        }
        else {
            // Note sure what to do with the rest - just put them in...
            //----------------------------------------------------------
            atttibuteValue.getContent().add(value);
        }
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
