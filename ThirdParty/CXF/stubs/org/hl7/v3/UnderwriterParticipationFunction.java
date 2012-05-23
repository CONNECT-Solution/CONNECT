
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UnderwriterParticipationFunction.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="UnderwriterParticipationFunction">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="PAYORCNTR"/>
 *     &lt;enumeration value="REINS"/>
 *     &lt;enumeration value="RETROCES"/>
 *     &lt;enumeration value="SUBCTRT"/>
 *     &lt;enumeration value="UNDERWRTNG"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "UnderwriterParticipationFunction")
@XmlEnum
public enum UnderwriterParticipationFunction {

    PAYORCNTR,
    REINS,
    RETROCES,
    SUBCTRT,
    UNDERWRTNG;

    public String value() {
        return name();
    }

    public static UnderwriterParticipationFunction fromValue(String v) {
        return valueOf(v);
    }

}
