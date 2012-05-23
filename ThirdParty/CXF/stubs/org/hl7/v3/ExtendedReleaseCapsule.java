
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ExtendedReleaseCapsule.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ExtendedReleaseCapsule">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ERCAP"/>
 *     &lt;enumeration value="ERCAP12"/>
 *     &lt;enumeration value="ERCAP24"/>
 *     &lt;enumeration value="ERECCAP"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ExtendedReleaseCapsule")
@XmlEnum
public enum ExtendedReleaseCapsule {

    ERCAP("ERCAP"),
    @XmlEnumValue("ERCAP12")
    ERCAP_12("ERCAP12"),
    @XmlEnumValue("ERCAP24")
    ERCAP_24("ERCAP24"),
    ERECCAP("ERECCAP");
    private final String value;

    ExtendedReleaseCapsule(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ExtendedReleaseCapsule fromValue(String v) {
        for (ExtendedReleaseCapsule c: ExtendedReleaseCapsule.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
