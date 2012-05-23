
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for WesternApachean.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="WesternApachean">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="x-NAV"/>
 *     &lt;enumeration value="x-APM"/>
 *     &lt;enumeration value="x-APW"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "WesternApachean")
@XmlEnum
public enum WesternApachean {

    @XmlEnumValue("x-NAV")
    X_NAV("x-NAV"),
    @XmlEnumValue("x-APM")
    X_APM("x-APM"),
    @XmlEnumValue("x-APW")
    X_APW("x-APW");
    private final String value;

    WesternApachean(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static WesternApachean fromValue(String v) {
        for (WesternApachean c: WesternApachean.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
