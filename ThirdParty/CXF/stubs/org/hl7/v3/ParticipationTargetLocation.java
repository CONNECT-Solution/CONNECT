
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParticipationTargetLocation.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ParticipationTargetLocation">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="LOC"/>
 *     &lt;enumeration value="DST"/>
 *     &lt;enumeration value="ELOC"/>
 *     &lt;enumeration value="ORG"/>
 *     &lt;enumeration value="RML"/>
 *     &lt;enumeration value="VIA"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ParticipationTargetLocation")
@XmlEnum
public enum ParticipationTargetLocation {

    LOC,
    DST,
    ELOC,
    ORG,
    RML,
    VIA;

    public String value() {
        return name();
    }

    public static ParticipationTargetLocation fromValue(String v) {
        return valueOf(v);
    }

}
