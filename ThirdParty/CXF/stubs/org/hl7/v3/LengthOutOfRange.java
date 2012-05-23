
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LengthOutOfRange.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="LengthOutOfRange">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="LEN_RANGE"/>
 *     &lt;enumeration value="LEN_LONG"/>
 *     &lt;enumeration value="LEN_SHORT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "LengthOutOfRange")
@XmlEnum
public enum LengthOutOfRange {

    LEN_RANGE,
    LEN_LONG,
    LEN_SHORT;

    public String value() {
        return name();
    }

    public static LengthOutOfRange fromValue(String v) {
        return valueOf(v);
    }

}
