
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PharmacySupplyRequestRenewalRefusalReasonCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PharmacySupplyRequestRenewalRefusalReasonCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="FAMPHYS"/>
 *     &lt;enumeration value="ONHOLD"/>
 *     &lt;enumeration value="MODIFY"/>
 *     &lt;enumeration value="ALREADYRX"/>
 *     &lt;enumeration value="NEEDAPMT"/>
 *     &lt;enumeration value="NOTPAT"/>
 *     &lt;enumeration value="NOTAVAIL"/>
 *     &lt;enumeration value="DISCONT"/>
 *     &lt;enumeration value="TOOEARLY"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PharmacySupplyRequestRenewalRefusalReasonCode")
@XmlEnum
public enum PharmacySupplyRequestRenewalRefusalReasonCode {

    FAMPHYS,
    ONHOLD,
    MODIFY,
    ALREADYRX,
    NEEDAPMT,
    NOTPAT,
    NOTAVAIL,
    DISCONT,
    TOOEARLY;

    public String value() {
        return name();
    }

    public static PharmacySupplyRequestRenewalRefusalReasonCode fromValue(String v) {
        return valueOf(v);
    }

}
