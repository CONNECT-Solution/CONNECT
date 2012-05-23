
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SubstanceAdminSubstitutionReason.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SubstanceAdminSubstitutionReason">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="CT"/>
 *     &lt;enumeration value="FP"/>
 *     &lt;enumeration value="OS"/>
 *     &lt;enumeration value="RR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SubstanceAdminSubstitutionReason")
@XmlEnum
public enum SubstanceAdminSubstitutionReason {

    CT,
    FP,
    OS,
    RR;

    public String value() {
        return name();
    }

    public static SubstanceAdminSubstitutionReason fromValue(String v) {
        return valueOf(v);
    }

}
