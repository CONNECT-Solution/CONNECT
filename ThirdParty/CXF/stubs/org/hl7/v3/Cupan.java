
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Cupan.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Cupan">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="x-CHL"/>
 *     &lt;enumeration value="x-CUP"/>
 *     &lt;enumeration value="x-LUI"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Cupan")
@XmlEnum
public enum Cupan {

    @XmlEnumValue("x-CHL")
    X_CHL("x-CHL"),
    @XmlEnumValue("x-CUP")
    X_CUP("x-CUP"),
    @XmlEnumValue("x-LUI")
    X_LUI("x-LUI");
    private final String value;

    Cupan(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Cupan fromValue(String v) {
        for (Cupan c: Cupan.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
