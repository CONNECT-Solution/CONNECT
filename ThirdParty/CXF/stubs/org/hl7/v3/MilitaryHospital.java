
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MilitaryHospital.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MilitaryHospital">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="MHSP"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "MilitaryHospital")
@XmlEnum
public enum MilitaryHospital {

    MHSP;

    public String value() {
        return name();
    }

    public static MilitaryHospital fromValue(String v) {
        return valueOf(v);
    }

}
