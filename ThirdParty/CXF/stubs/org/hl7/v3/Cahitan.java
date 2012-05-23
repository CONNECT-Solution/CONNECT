
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Cahitan.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Cahitan">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="x-YAQ"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Cahitan")
@XmlEnum
public enum Cahitan {

    @XmlEnumValue("x-YAQ")
    X_YAQ("x-YAQ");
    private final String value;

    Cahitan(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Cahitan fromValue(String v) {
        for (Cahitan c: Cahitan.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
