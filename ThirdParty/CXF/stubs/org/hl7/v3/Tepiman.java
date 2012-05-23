
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Tepiman.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Tepiman">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="x-PAP"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Tepiman")
@XmlEnum
public enum Tepiman {

    @XmlEnumValue("x-PAP")
    X_PAP("x-PAP");
    private final String value;

    Tepiman(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Tepiman fromValue(String v) {
        for (Tepiman c: Tepiman.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
