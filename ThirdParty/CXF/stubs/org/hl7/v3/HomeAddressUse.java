
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HomeAddressUse.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="HomeAddressUse">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="H"/>
 *     &lt;enumeration value="HP"/>
 *     &lt;enumeration value="HV"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "HomeAddressUse")
@XmlEnum
public enum HomeAddressUse {

    H,
    HP,
    HV;

    public String value() {
        return name();
    }

    public static HomeAddressUse fromValue(String v) {
        return valueOf(v);
    }

}
