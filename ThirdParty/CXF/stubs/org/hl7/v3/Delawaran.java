
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Delawaran.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Delawaran">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="x-DEL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Delawaran")
@XmlEnum
public enum Delawaran {

    @XmlEnumValue("x-DEL")
    X_DEL("x-DEL");
    private final String value;

    Delawaran(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Delawaran fromValue(String v) {
        for (Delawaran c: Delawaran.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
