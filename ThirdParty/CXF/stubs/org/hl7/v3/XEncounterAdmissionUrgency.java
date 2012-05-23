
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for x_EncounterAdmissionUrgency.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="x_EncounterAdmissionUrgency">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="EL"/>
 *     &lt;enumeration value="EM"/>
 *     &lt;enumeration value="R"/>
 *     &lt;enumeration value="UR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "x_EncounterAdmissionUrgency")
@XmlEnum
public enum XEncounterAdmissionUrgency {

    EL,
    EM,
    R,
    UR;

    public String value() {
        return name();
    }

    public static XEncounterAdmissionUrgency fromValue(String v) {
        return valueOf(v);
    }

}
