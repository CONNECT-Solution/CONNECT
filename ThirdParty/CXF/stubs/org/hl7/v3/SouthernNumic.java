
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SouthernNumic.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SouthernNumic">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="x-KAW"/>
 *     &lt;enumeration value="x-UTE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SouthernNumic")
@XmlEnum
public enum SouthernNumic {

    @XmlEnumValue("x-KAW")
    X_KAW("x-KAW"),
    @XmlEnumValue("x-UTE")
    X_UTE("x-UTE");
    private final String value;

    SouthernNumic(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SouthernNumic fromValue(String v) {
        for (SouthernNumic c: SouthernNumic.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
