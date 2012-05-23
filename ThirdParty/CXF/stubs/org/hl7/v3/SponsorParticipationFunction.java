
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SponsorParticipationFunction.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SponsorParticipationFunction">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="FULINRD"/>
 *     &lt;enumeration value="SELFINRD"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SponsorParticipationFunction")
@XmlEnum
public enum SponsorParticipationFunction {

    FULINRD,
    SELFINRD;

    public String value() {
        return name();
    }

    public static SponsorParticipationFunction fromValue(String v) {
        return valueOf(v);
    }

}
