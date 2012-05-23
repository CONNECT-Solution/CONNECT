
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CVDiagTherPracticeSetting.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CVDiagTherPracticeSetting">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="CVDX"/>
 *     &lt;enumeration value="CATH"/>
 *     &lt;enumeration value="ECHO"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CVDiagTherPracticeSetting")
@XmlEnum
public enum CVDiagTherPracticeSetting {

    CVDX,
    CATH,
    ECHO;

    public String value() {
        return name();
    }

    public static CVDiagTherPracticeSetting fromValue(String v) {
        return valueOf(v);
    }

}
