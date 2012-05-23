
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PayorParticipationFunction.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PayorParticipationFunction">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="CLMADJ"/>
 *     &lt;enumeration value="ENROLL"/>
 *     &lt;enumeration value="FFSMGT"/>
 *     &lt;enumeration value="MCMGT"/>
 *     &lt;enumeration value="PROVMGT"/>
 *     &lt;enumeration value="UMGT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PayorParticipationFunction")
@XmlEnum
public enum PayorParticipationFunction {

    CLMADJ,
    ENROLL,
    FFSMGT,
    MCMGT,
    PROVMGT,
    UMGT;

    public String value() {
        return name();
    }

    public static PayorParticipationFunction fromValue(String v) {
        return valueOf(v);
    }

}
