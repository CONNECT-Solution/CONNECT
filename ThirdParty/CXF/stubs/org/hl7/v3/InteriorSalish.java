
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for InteriorSalish.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="InteriorSalish">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="x-CRD"/>
 *     &lt;enumeration value="x-COL"/>
 *     &lt;enumeration value="x-FLA"/>
 *     &lt;enumeration value="x-OKA"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "InteriorSalish")
@XmlEnum
public enum InteriorSalish {

    @XmlEnumValue("x-CRD")
    X_CRD("x-CRD"),
    @XmlEnumValue("x-COL")
    X_COL("x-COL"),
    @XmlEnumValue("x-FLA")
    X_FLA("x-FLA"),
    @XmlEnumValue("x-OKA")
    X_OKA("x-OKA");
    private final String value;

    InteriorSalish(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static InteriorSalish fromValue(String v) {
        for (InteriorSalish c: InteriorSalish.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
