
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PayorRoleType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PayorRoleType">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ENROLBKR"/>
 *     &lt;enumeration value="TPA"/>
 *     &lt;enumeration value="UMO"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PayorRoleType")
@XmlEnum
public enum PayorRoleType {

    ENROLBKR,
    TPA,
    UMO;

    public String value() {
        return name();
    }

    public static PayorRoleType fromValue(String v) {
        return valueOf(v);
    }

}
