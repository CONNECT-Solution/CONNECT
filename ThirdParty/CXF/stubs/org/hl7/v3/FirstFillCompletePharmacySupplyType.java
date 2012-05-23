
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FirstFillCompletePharmacySupplyType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="FirstFillCompletePharmacySupplyType">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="FFC"/>
 *     &lt;enumeration value="FFCS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "FirstFillCompletePharmacySupplyType")
@XmlEnum
public enum FirstFillCompletePharmacySupplyType {

    FFC,
    FFCS;

    public String value() {
        return name();
    }

    public static FirstFillCompletePharmacySupplyType fromValue(String v) {
        return valueOf(v);
    }

}
