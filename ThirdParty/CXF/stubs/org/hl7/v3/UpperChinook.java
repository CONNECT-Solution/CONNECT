
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UpperChinook.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="UpperChinook">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="x-WAC"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "UpperChinook")
@XmlEnum
public enum UpperChinook {

    @XmlEnumValue("x-WAC")
    X_WAC("x-WAC");
    private final String value;

    UpperChinook(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static UpperChinook fromValue(String v) {
        for (UpperChinook c: UpperChinook.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
