
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParticipationAncillary.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ParticipationAncillary">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ADM"/>
 *     &lt;enumeration value="ATND"/>
 *     &lt;enumeration value="CALLBCK"/>
 *     &lt;enumeration value="CON"/>
 *     &lt;enumeration value="DIS"/>
 *     &lt;enumeration value="ESC"/>
 *     &lt;enumeration value="REF"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ParticipationAncillary")
@XmlEnum
public enum ParticipationAncillary {

    ADM,
    ATND,
    CALLBCK,
    CON,
    DIS,
    ESC,
    REF;

    public String value() {
        return name();
    }

    public static ParticipationAncillary fromValue(String v) {
        return valueOf(v);
    }

}
