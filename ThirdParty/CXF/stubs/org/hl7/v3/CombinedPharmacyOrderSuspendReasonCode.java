
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CombinedPharmacyOrderSuspendReasonCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CombinedPharmacyOrderSuspendReasonCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="HOSPADM"/>
 *     &lt;enumeration value="SALG"/>
 *     &lt;enumeration value="SDDI"/>
 *     &lt;enumeration value="DRUGHIGH"/>
 *     &lt;enumeration value="SDUPTHER"/>
 *     &lt;enumeration value="SINTOL"/>
 *     &lt;enumeration value="LABINT"/>
 *     &lt;enumeration value="PREG"/>
 *     &lt;enumeration value="NON-AVAIL"/>
 *     &lt;enumeration value="SURG"/>
 *     &lt;enumeration value="CLARIF"/>
 *     &lt;enumeration value="ALTCHOICE"/>
 *     &lt;enumeration value="WASHOUT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CombinedPharmacyOrderSuspendReasonCode")
@XmlEnum
public enum CombinedPharmacyOrderSuspendReasonCode {

    HOSPADM("HOSPADM"),
    SALG("SALG"),
    SDDI("SDDI"),
    DRUGHIGH("DRUGHIGH"),
    SDUPTHER("SDUPTHER"),
    SINTOL("SINTOL"),
    LABINT("LABINT"),
    PREG("PREG"),
    @XmlEnumValue("NON-AVAIL")
    NON_AVAIL("NON-AVAIL"),
    SURG("SURG"),
    CLARIF("CLARIF"),
    ALTCHOICE("ALTCHOICE"),
    WASHOUT("WASHOUT");
    private final String value;

    CombinedPharmacyOrderSuspendReasonCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CombinedPharmacyOrderSuspendReasonCode fromValue(String v) {
        for (CombinedPharmacyOrderSuspendReasonCode c: CombinedPharmacyOrderSuspendReasonCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
