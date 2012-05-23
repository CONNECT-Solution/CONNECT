
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MilitaryRoleType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MilitaryRoleType">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="MIL"/>
 *     &lt;enumeration value="ACTMIL"/>
 *     &lt;enumeration value="RETMIL"/>
 *     &lt;enumeration value="VET"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "MilitaryRoleType")
@XmlEnum
public enum MilitaryRoleType {

    MIL,
    ACTMIL,
    RETMIL,
    VET;

    public String value() {
        return name();
    }

    public static MilitaryRoleType fromValue(String v) {
        return valueOf(v);
    }

}
