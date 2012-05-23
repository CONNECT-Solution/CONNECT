
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RoleClassContact.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RoleClassContact">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="CON"/>
 *     &lt;enumeration value="ECON"/>
 *     &lt;enumeration value="NOK"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "RoleClassContact")
@XmlEnum
public enum RoleClassContact {

    CON,
    ECON,
    NOK;

    public String value() {
        return name();
    }

    public static RoleClassContact fromValue(String v) {
        return valueOf(v);
    }

}
