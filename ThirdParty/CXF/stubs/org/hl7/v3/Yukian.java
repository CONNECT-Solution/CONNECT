
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Yukian.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Yukian">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="x-WAO"/>
 *     &lt;enumeration value="x-YUK"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Yukian")
@XmlEnum
public enum Yukian {

    @XmlEnumValue("x-WAO")
    X_WAO("x-WAO"),
    @XmlEnumValue("x-YUK")
    X_YUK("x-YUK");
    private final String value;

    Yukian(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Yukian fromValue(String v) {
        for (Yukian c: Yukian.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
