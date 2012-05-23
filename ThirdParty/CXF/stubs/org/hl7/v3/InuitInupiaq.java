
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for InuitInupiaq.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="InuitInupiaq">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="x-ESI"/>
 *     &lt;enumeration value="x-ESK"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "InuitInupiaq")
@XmlEnum
public enum InuitInupiaq {

    @XmlEnumValue("x-ESI")
    X_ESI("x-ESI"),
    @XmlEnumValue("x-ESK")
    X_ESK("x-ESK");
    private final String value;

    InuitInupiaq(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static InuitInupiaq fromValue(String v) {
        for (InuitInupiaq c: InuitInupiaq.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
