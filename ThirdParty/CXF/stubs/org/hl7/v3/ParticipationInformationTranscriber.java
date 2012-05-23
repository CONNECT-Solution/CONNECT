
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParticipationInformationTranscriber.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ParticipationInformationTranscriber">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="TRANS"/>
 *     &lt;enumeration value="ENT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ParticipationInformationTranscriber")
@XmlEnum
public enum ParticipationInformationTranscriber {

    TRANS,
    ENT;

    public String value() {
        return name();
    }

    public static ParticipationInformationTranscriber fromValue(String v) {
        return valueOf(v);
    }

}
