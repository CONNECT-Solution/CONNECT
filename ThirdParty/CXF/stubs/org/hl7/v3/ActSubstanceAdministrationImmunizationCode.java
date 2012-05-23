
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActSubstanceAdministrationImmunizationCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActSubstanceAdministrationImmunizationCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="IMMUNIZ"/>
 *     &lt;enumeration value="BOOSTER"/>
 *     &lt;enumeration value="INITIMMUNIZ"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActSubstanceAdministrationImmunizationCode")
@XmlEnum
public enum ActSubstanceAdministrationImmunizationCode {

    IMMUNIZ,
    BOOSTER,
    INITIMMUNIZ;

    public String value() {
        return name();
    }

    public static ActSubstanceAdministrationImmunizationCode fromValue(String v) {
        return valueOf(v);
    }

}
