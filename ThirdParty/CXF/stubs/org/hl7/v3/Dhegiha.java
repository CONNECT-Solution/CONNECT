
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Dhegiha.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Dhegiha">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="x-KAA"/>
 *     &lt;enumeration value="x-OMA"/>
 *     &lt;enumeration value="x-OSA"/>
 *     &lt;enumeration value="x-QUA"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Dhegiha")
@XmlEnum
public enum Dhegiha {

    @XmlEnumValue("x-KAA")
    X_KAA("x-KAA"),
    @XmlEnumValue("x-OMA")
    X_OMA("x-OMA"),
    @XmlEnumValue("x-OSA")
    X_OSA("x-OSA"),
    @XmlEnumValue("x-QUA")
    X_QUA("x-QUA");
    private final String value;

    Dhegiha(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Dhegiha fromValue(String v) {
        for (Dhegiha c: Dhegiha.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
