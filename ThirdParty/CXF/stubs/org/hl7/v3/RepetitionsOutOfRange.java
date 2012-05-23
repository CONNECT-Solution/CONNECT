
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RepetitionsOutOfRange.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RepetitionsOutOfRange">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="REP_RANGE"/>
 *     &lt;enumeration value="MAXOCCURS"/>
 *     &lt;enumeration value="MINOCCURS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "RepetitionsOutOfRange")
@XmlEnum
public enum RepetitionsOutOfRange {

    REP_RANGE,
    MAXOCCURS,
    MINOCCURS;

    public String value() {
        return name();
    }

    public static RepetitionsOutOfRange fromValue(String v) {
        return valueOf(v);
    }

}
