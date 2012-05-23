
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ConceptPropertyId.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ConceptPropertyId">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="OID"/>
 *     &lt;enumeration value="_ValueSetPropertyId"/>
 *     &lt;enumeration value="appliesTo"/>
 *     &lt;enumeration value="howApplies"/>
 *     &lt;enumeration value="inverseRelationship"/>
 *     &lt;enumeration value="openIssue"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ConceptPropertyId")
@XmlEnum
public enum ConceptPropertyId {

    OID("OID"),
    @XmlEnumValue("_ValueSetPropertyId")
    VALUE_SET_PROPERTY_ID("_ValueSetPropertyId"),
    @XmlEnumValue("appliesTo")
    APPLIES_TO("appliesTo"),
    @XmlEnumValue("howApplies")
    HOW_APPLIES("howApplies"),
    @XmlEnumValue("inverseRelationship")
    INVERSE_RELATIONSHIP("inverseRelationship"),
    @XmlEnumValue("openIssue")
    OPEN_ISSUE("openIssue");
    private final String value;

    ConceptPropertyId(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ConceptPropertyId fromValue(String v) {
        for (ConceptPropertyId c: ConceptPropertyId.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
