
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CalendarCycleOneLetter.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CalendarCycleOneLetter">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="D"/>
 *     &lt;enumeration value="J"/>
 *     &lt;enumeration value="H"/>
 *     &lt;enumeration value="N"/>
 *     &lt;enumeration value="M"/>
 *     &lt;enumeration value="S"/>
 *     &lt;enumeration value="W"/>
 *     &lt;enumeration value="Y"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CalendarCycleOneLetter")
@XmlEnum
public enum CalendarCycleOneLetter {

    D,
    J,
    H,
    N,
    M,
    S,
    W,
    Y;

    public String value() {
        return name();
    }

    public static CalendarCycleOneLetter fromValue(String v) {
        return valueOf(v);
    }

}
