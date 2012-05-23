
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PowerOfAttorney.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PowerOfAttorney">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="POWATT"/>
 *     &lt;enumeration value="DPOWATT"/>
 *     &lt;enumeration value="HPOWATT"/>
 *     &lt;enumeration value="SPOWATT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PowerOfAttorney")
@XmlEnum
public enum PowerOfAttorney {

    POWATT,
    DPOWATT,
    HPOWATT,
    SPOWATT;

    public String value() {
        return name();
    }

    public static PowerOfAttorney fromValue(String v) {
        return valueOf(v);
    }

}
