
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SubstanceAdminSubstitutionNotAllowedReason.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SubstanceAdminSubstitutionNotAllowedReason">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="PAT"/>
 *     &lt;enumeration value="ALGINT"/>
 *     &lt;enumeration value="TRIAL"/>
 *     &lt;enumeration value="COMPCON"/>
 *     &lt;enumeration value="THERCHAR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SubstanceAdminSubstitutionNotAllowedReason")
@XmlEnum
public enum SubstanceAdminSubstitutionNotAllowedReason {

    PAT,
    ALGINT,
    TRIAL,
    COMPCON,
    THERCHAR;

    public String value() {
        return name();
    }

    public static SubstanceAdminSubstitutionNotAllowedReason fromValue(String v) {
        return valueOf(v);
    }

}
