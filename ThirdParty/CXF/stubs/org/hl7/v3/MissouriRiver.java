
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MissouriRiver.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MissouriRiver">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="x-CRO"/>
 *     &lt;enumeration value="x-HID"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "MissouriRiver")
@XmlEnum
public enum MissouriRiver {

    @XmlEnumValue("x-CRO")
    X_CRO("x-CRO"),
    @XmlEnumValue("x-HID")
    X_HID("x-HID");
    private final String value;

    MissouriRiver(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MissouriRiver fromValue(String v) {
        for (MissouriRiver c: MissouriRiver.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
