
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for x_ActEncounterReason.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="x_ActEncounterReason">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="MEDNEC"/>
 *     &lt;enumeration value="PAT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "x_ActEncounterReason")
@XmlEnum
public enum XActEncounterReason {

    MEDNEC,
    PAT;

    public String value() {
        return name();
    }

    public static XActEncounterReason fromValue(String v) {
        return valueOf(v);
    }

}
