
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RoleClassEquivalentEntity.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RoleClassEquivalentEntity">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="SAME"/>
 *     &lt;enumeration value="SUBY"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "RoleClassEquivalentEntity")
@XmlEnum
public enum RoleClassEquivalentEntity {

    SAME,
    SUBY;

    public String value() {
        return name();
    }

    public static RoleClassEquivalentEntity fromValue(String v) {
        return valueOf(v);
    }

}
