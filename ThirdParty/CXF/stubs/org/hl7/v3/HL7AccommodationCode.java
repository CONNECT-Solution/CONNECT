
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HL7AccommodationCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="HL7AccommodationCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="I"/>
 *     &lt;enumeration value="P"/>
 *     &lt;enumeration value="SP"/>
 *     &lt;enumeration value="S"/>
 *     &lt;enumeration value="W"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "HL7AccommodationCode")
@XmlEnum
public enum HL7AccommodationCode {

    I,
    P,
    SP,
    S,
    W;

    public String value() {
        return name();
    }

    public static HL7AccommodationCode fromValue(String v) {
        return valueOf(v);
    }

}
