
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ObservationLivingExpenseValue.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ObservationLivingExpenseValue">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="LIVEXP"/>
 *     &lt;enumeration value="CLOTH"/>
 *     &lt;enumeration value="FOOD"/>
 *     &lt;enumeration value="HEALTH"/>
 *     &lt;enumeration value="HOUSE"/>
 *     &lt;enumeration value="LEGAL"/>
 *     &lt;enumeration value="MORTG"/>
 *     &lt;enumeration value="RENT"/>
 *     &lt;enumeration value="SUNDRY"/>
 *     &lt;enumeration value="TRANS"/>
 *     &lt;enumeration value="UTIL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ObservationLivingExpenseValue")
@XmlEnum
public enum ObservationLivingExpenseValue {

    LIVEXP,
    CLOTH,
    FOOD,
    HEALTH,
    HOUSE,
    LEGAL,
    MORTG,
    RENT,
    SUNDRY,
    TRANS,
    UTIL;

    public String value() {
        return name();
    }

    public static ObservationLivingExpenseValue fromValue(String v) {
        return valueOf(v);
    }

}
