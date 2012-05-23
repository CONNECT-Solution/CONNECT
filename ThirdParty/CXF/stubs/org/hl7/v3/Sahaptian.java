
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Sahaptian.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Sahaptian">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="x-WAR"/>
 *     &lt;enumeration value="x-UMA"/>
 *     &lt;enumeration value="x-WAA"/>
 *     &lt;enumeration value="x-YAK"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Sahaptian")
@XmlEnum
public enum Sahaptian {

    @XmlEnumValue("x-WAR")
    X_WAR("x-WAR"),
    @XmlEnumValue("x-UMA")
    X_UMA("x-UMA"),
    @XmlEnumValue("x-WAA")
    X_WAA("x-WAA"),
    @XmlEnumValue("x-YAK")
    X_YAK("x-YAK");
    private final String value;

    Sahaptian(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Sahaptian fromValue(String v) {
        for (Sahaptian c: Sahaptian.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
