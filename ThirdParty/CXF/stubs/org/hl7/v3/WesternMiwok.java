
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for WesternMiwok.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="WesternMiwok">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="x-CSI"/>
 *     &lt;enumeration value="x-LMW"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "WesternMiwok")
@XmlEnum
public enum WesternMiwok {

    @XmlEnumValue("x-CSI")
    X_CSI("x-CSI"),
    @XmlEnumValue("x-LMW")
    X_LMW("x-LMW");
    private final String value;

    WesternMiwok(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static WesternMiwok fromValue(String v) {
        for (WesternMiwok c: WesternMiwok.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
