
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for x_ParticipationVrfRespSprfWit.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="x_ParticipationVrfRespSprfWit">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="RESP"/>
 *     &lt;enumeration value="SPRF"/>
 *     &lt;enumeration value="VRF"/>
 *     &lt;enumeration value="WIT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "x_ParticipationVrfRespSprfWit")
@XmlEnum
public enum XParticipationVrfRespSprfWit {

    RESP,
    SPRF,
    VRF,
    WIT;

    public String value() {
        return name();
    }

    public static XParticipationVrfRespSprfWit fromValue(String v) {
        return valueOf(v);
    }

}
