
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParticipationModeVerbal.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ParticipationModeVerbal">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="VERBAL"/>
 *     &lt;enumeration value="DICTATE"/>
 *     &lt;enumeration value="FACE"/>
 *     &lt;enumeration value="PHONE"/>
 *     &lt;enumeration value="VIDEOCONF"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ParticipationModeVerbal")
@XmlEnum
public enum ParticipationModeVerbal {

    VERBAL,
    DICTATE,
    FACE,
    PHONE,
    VIDEOCONF;

    public String value() {
        return name();
    }

    public static ParticipationModeVerbal fromValue(String v) {
        return valueOf(v);
    }

}
