
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OrderedListStyle.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="OrderedListStyle">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="Arabic"/>
 *     &lt;enumeration value="BigAlpha"/>
 *     &lt;enumeration value="BigRoman"/>
 *     &lt;enumeration value="LittleAlpha"/>
 *     &lt;enumeration value="LittleRoman"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "OrderedListStyle")
@XmlEnum
public enum OrderedListStyle {

    @XmlEnumValue("Arabic")
    ARABIC("Arabic"),
    @XmlEnumValue("BigAlpha")
    BIG_ALPHA("BigAlpha"),
    @XmlEnumValue("BigRoman")
    BIG_ROMAN("BigRoman"),
    @XmlEnumValue("LittleAlpha")
    LITTLE_ALPHA("LittleAlpha"),
    @XmlEnumValue("LittleRoman")
    LITTLE_ROMAN("LittleRoman");
    private final String value;

    OrderedListStyle(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static OrderedListStyle fromValue(String v) {
        for (OrderedListStyle c: OrderedListStyle.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
