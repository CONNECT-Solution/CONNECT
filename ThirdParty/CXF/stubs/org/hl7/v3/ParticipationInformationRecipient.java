
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParticipationInformationRecipient.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ParticipationInformationRecipient">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="IRCP"/>
 *     &lt;enumeration value="REFB"/>
 *     &lt;enumeration value="REFT"/>
 *     &lt;enumeration value="PRCP"/>
 *     &lt;enumeration value="TRC"/>
 *     &lt;enumeration value="NOT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ParticipationInformationRecipient")
@XmlEnum
public enum ParticipationInformationRecipient {

    IRCP,
    REFB,
    REFT,
    PRCP,
    TRC,
    NOT;

    public String value() {
        return name();
    }

    public static ParticipationInformationRecipient fromValue(String v) {
        return valueOf(v);
    }

}
