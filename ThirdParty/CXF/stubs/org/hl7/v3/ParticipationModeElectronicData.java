
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParticipationModeElectronicData.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ParticipationModeElectronicData">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ELECTRONIC"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ParticipationModeElectronicData")
@XmlEnum
public enum ParticipationModeElectronicData {

    ELECTRONIC;

    public String value() {
        return name();
    }

    public static ParticipationModeElectronicData fromValue(String v) {
        return valueOf(v);
    }

}
