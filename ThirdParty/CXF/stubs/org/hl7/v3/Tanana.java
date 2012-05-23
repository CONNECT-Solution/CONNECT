
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Tanana.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Tanana">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="x-TAA"/>
 *     &lt;enumeration value="x-TCB"/>
 *     &lt;enumeration value="x-TAU"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Tanana")
@XmlEnum
public enum Tanana {

    @XmlEnumValue("x-TAA")
    X_TAA("x-TAA"),
    @XmlEnumValue("x-TCB")
    X_TCB("x-TCB"),
    @XmlEnumValue("x-TAU")
    X_TAU("x-TAU");
    private final String value;

    Tanana(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Tanana fromValue(String v) {
        for (Tanana c: Tanana.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
