
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActInsurancePolicyCodeSubsidizedHealthProgramByBOT.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActInsurancePolicyCodeSubsidizedHealthProgramByBOT">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="SUBSIDIZ"/>
 *     &lt;enumeration value="SUBSIDMC"/>
 *     &lt;enumeration value="SUBSUPP"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActInsurancePolicyCodeSubsidizedHealthProgramByBOT")
@XmlEnum
public enum ActInsurancePolicyCodeSubsidizedHealthProgramByBOT {

    SUBSIDIZ,
    SUBSIDMC,
    SUBSUPP;

    public String value() {
        return name();
    }

    public static ActInsurancePolicyCodeSubsidizedHealthProgramByBOT fromValue(String v) {
        return valueOf(v);
    }

}
