
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Nootkan.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Nootkan">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="x-MYH"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Nootkan")
@XmlEnum
public enum Nootkan {

    @XmlEnumValue("x-MYH")
    X_MYH("x-MYH");
    private final String value;

    Nootkan(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Nootkan fromValue(String v) {
        for (Nootkan c: Nootkan.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
