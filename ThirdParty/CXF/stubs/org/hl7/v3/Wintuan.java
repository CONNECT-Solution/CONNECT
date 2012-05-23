
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Wintuan.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Wintuan">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="x-WIT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Wintuan")
@XmlEnum
public enum Wintuan {

    @XmlEnumValue("x-WIT")
    X_WIT("x-WIT");
    private final String value;

    Wintuan(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Wintuan fromValue(String v) {
        for (Wintuan c: Wintuan.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
