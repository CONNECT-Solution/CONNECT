
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for x_EncounterPerformerParticipation.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="x_EncounterPerformerParticipation">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="CON"/>
 *     &lt;enumeration value="PRF"/>
 *     &lt;enumeration value="SPRF"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "x_EncounterPerformerParticipation")
@XmlEnum
public enum XEncounterPerformerParticipation {

    CON,
    PRF,
    SPRF;

    public String value() {
        return name();
    }

    public static XEncounterPerformerParticipation fromValue(String v) {
        return valueOf(v);
    }

}
