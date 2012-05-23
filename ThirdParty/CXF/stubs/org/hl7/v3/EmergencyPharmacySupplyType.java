
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EmergencyPharmacySupplyType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EmergencyPharmacySupplyType">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="EM"/>
 *     &lt;enumeration value="SO"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "EmergencyPharmacySupplyType")
@XmlEnum
public enum EmergencyPharmacySupplyType {

    EM,
    SO;

    public String value() {
        return name();
    }

    public static EmergencyPharmacySupplyType fromValue(String v) {
        return valueOf(v);
    }

}
