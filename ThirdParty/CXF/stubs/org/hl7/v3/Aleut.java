
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Aleut.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Aleut">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="x-ALW"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Aleut")
@XmlEnum
public enum Aleut {

    @XmlEnumValue("x-ALW")
    X_ALW("x-ALW");
    private final String value;

    Aleut(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Aleut fromValue(String v) {
        for (Aleut c: Aleut.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
