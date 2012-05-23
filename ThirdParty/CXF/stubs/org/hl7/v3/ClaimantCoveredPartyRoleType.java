
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ClaimantCoveredPartyRoleType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ClaimantCoveredPartyRoleType">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="CRIMEVIC"/>
 *     &lt;enumeration value="INJ"/>
 *     &lt;enumeration value="INJWKR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ClaimantCoveredPartyRoleType")
@XmlEnum
public enum ClaimantCoveredPartyRoleType {

    CRIMEVIC,
    INJ,
    INJWKR;

    public String value() {
        return name();
    }

    public static ClaimantCoveredPartyRoleType fromValue(String v) {
        return valueOf(v);
    }

}
