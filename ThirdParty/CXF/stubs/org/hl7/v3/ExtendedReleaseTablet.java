
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ExtendedReleaseTablet.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ExtendedReleaseTablet">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ERTAB"/>
 *     &lt;enumeration value="ERTAB12"/>
 *     &lt;enumeration value="ERTAB24"/>
 *     &lt;enumeration value="ERECTAB"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ExtendedReleaseTablet")
@XmlEnum
public enum ExtendedReleaseTablet {

    ERTAB("ERTAB"),
    @XmlEnumValue("ERTAB12")
    ERTAB_12("ERTAB12"),
    @XmlEnumValue("ERTAB24")
    ERTAB_24("ERTAB24"),
    ERECTAB("ERECTAB");
    private final String value;

    ExtendedReleaseTablet(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ExtendedReleaseTablet fromValue(String v) {
        for (ExtendedReleaseTablet c: ExtendedReleaseTablet.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
