
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActNoImmunizationReason.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActNoImmunizationReason">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="IMMUNE"/>
 *     &lt;enumeration value="MEDPREC"/>
 *     &lt;enumeration value="OSTOCK"/>
 *     &lt;enumeration value="PATOBJ"/>
 *     &lt;enumeration value="PHILISOP"/>
 *     &lt;enumeration value="RELIG"/>
 *     &lt;enumeration value="VACEFF"/>
 *     &lt;enumeration value="VACSAF"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActNoImmunizationReason")
@XmlEnum
public enum ActNoImmunizationReason {

    IMMUNE,
    MEDPREC,
    OSTOCK,
    PATOBJ,
    PHILISOP,
    RELIG,
    VACEFF,
    VACSAF;

    public String value() {
        return name();
    }

    public static ActNoImmunizationReason fromValue(String v) {
        return valueOf(v);
    }

}
