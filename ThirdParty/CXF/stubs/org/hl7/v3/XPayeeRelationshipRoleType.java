
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for x_PayeeRelationshipRoleType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="x_PayeeRelationshipRoleType">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="FM"/>
 *     &lt;enumeration value="GT"/>
 *     &lt;enumeration value="PT"/>
 *     &lt;enumeration value="PH"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "x_PayeeRelationshipRoleType")
@XmlEnum
public enum XPayeeRelationshipRoleType {

    FM,
    GT,
    PT,
    PH;

    public String value() {
        return name();
    }

    public static XPayeeRelationshipRoleType fromValue(String v) {
        return valueOf(v);
    }

}
