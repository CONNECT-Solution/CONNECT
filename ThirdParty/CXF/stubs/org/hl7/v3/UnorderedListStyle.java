
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UnorderedListStyle.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="UnorderedListStyle">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="Circle"/>
 *     &lt;enumeration value="Disc"/>
 *     &lt;enumeration value="Square"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "UnorderedListStyle")
@XmlEnum
public enum UnorderedListStyle {

    @XmlEnumValue("Circle")
    CIRCLE("Circle"),
    @XmlEnumValue("Disc")
    DISC("Disc"),
    @XmlEnumValue("Square")
    SQUARE("Square");
    private final String value;

    UnorderedListStyle(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static UnorderedListStyle fromValue(String v) {
        for (UnorderedListStyle c: UnorderedListStyle.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
