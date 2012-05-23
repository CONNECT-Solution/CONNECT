
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Homeless.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Homeless">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="HL"/>
 *     &lt;enumeration value="M"/>
 *     &lt;enumeration value="T"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Homeless")
@XmlEnum
public enum Homeless {

    HL,
    M,
    T;

    public String value() {
        return name();
    }

    public static Homeless fromValue(String v) {
        return valueOf(v);
    }

}
