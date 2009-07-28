/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.transform.policy;

import oasis.names.tc.xacml._2_0.context.schema.os.AttributeType;
import oasis.names.tc.xacml._2_0.context.schema.os.AttributeValueType;

/**
 *
 * @author rayj
 */
public class AttributeHelper {
    public static AttributeType attributeFactory(String attributeId, String dataType, String attributeValue) {
        AttributeType attribute = new AttributeType();
        attribute.setAttributeId(attributeId);
        attribute.setDataType(dataType);

        AttributeValueType atttibuteValue = new AttributeValueType();
        atttibuteValue.getContent().add(attributeValue);
        attribute.getAttributeValue().add(atttibuteValue);

        return attribute;
    }

}
