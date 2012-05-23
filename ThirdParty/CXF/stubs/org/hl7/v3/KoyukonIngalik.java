
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for KoyukonIngalik.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="KoyukonIngalik">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="x-ING"/>
 *     &lt;enumeration value="x-HOI"/>
 *     &lt;enumeration value="x-KOY"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "KoyukonIngalik")
@XmlEnum
public enum KoyukonIngalik {

    @XmlEnumValue("x-ING")
    X_ING("x-ING"),
    @XmlEnumValue("x-HOI")
    X_HOI("x-HOI"),
    @XmlEnumValue("x-KOY")
    X_KOY("x-KOY");
    private final String value;

    KoyukonIngalik(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static KoyukonIngalik fromValue(String v) {
        for (KoyukonIngalik c: KoyukonIngalik.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
