
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParticipationExposureparticipation.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ParticipationExposureparticipation">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="EXPART"/>
 *     &lt;enumeration value="EXSRC"/>
 *     &lt;enumeration value="EXPTRGT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ParticipationExposureparticipation")
@XmlEnum
public enum ParticipationExposureparticipation {

    EXPART,
    EXSRC,
    EXPTRGT;

    public String value() {
        return name();
    }

    public static ParticipationExposureparticipation fromValue(String v) {
        return valueOf(v);
    }

}
