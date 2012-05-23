
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Tsamosan.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Tsamosan">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="x-COW"/>
 *     &lt;enumeration value="x-CEA"/>
 *     &lt;enumeration value="x-QUN"/>
 *     &lt;enumeration value="x-CJH"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Tsamosan")
@XmlEnum
public enum Tsamosan {

    @XmlEnumValue("x-COW")
    X_COW("x-COW"),
    @XmlEnumValue("x-CEA")
    X_CEA("x-CEA"),
    @XmlEnumValue("x-QUN")
    X_QUN("x-QUN"),
    @XmlEnumValue("x-CJH")
    X_CJH("x-CJH");
    private final String value;

    Tsamosan(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Tsamosan fromValue(String v) {
        for (Tsamosan c: Tsamosan.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
