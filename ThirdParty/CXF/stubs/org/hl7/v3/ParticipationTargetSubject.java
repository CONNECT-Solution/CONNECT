
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParticipationTargetSubject.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ParticipationTargetSubject">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="SBJ"/>
 *     &lt;enumeration value="SPC"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ParticipationTargetSubject")
@XmlEnum
public enum ParticipationTargetSubject {

    SBJ,
    SPC;

    public String value() {
        return name();
    }

    public static ParticipationTargetSubject fromValue(String v) {
        return valueOf(v);
    }

}
