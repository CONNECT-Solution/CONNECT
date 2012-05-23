
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ObservationAllergyType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ObservationAllergyType">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ALG"/>
 *     &lt;enumeration value="DALG"/>
 *     &lt;enumeration value="EALG"/>
 *     &lt;enumeration value="FALG"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ObservationAllergyType")
@XmlEnum
public enum ObservationAllergyType {

    ALG,
    DALG,
    EALG,
    FALG;

    public String value() {
        return name();
    }

    public static ObservationAllergyType fromValue(String v) {
        return valueOf(v);
    }

}
