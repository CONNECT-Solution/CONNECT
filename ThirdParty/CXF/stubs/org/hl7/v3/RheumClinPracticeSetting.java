
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RheumClinPracticeSetting.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RheumClinPracticeSetting">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="RHEUM"/>
 *     &lt;enumeration value="PEDRHEUM"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "RheumClinPracticeSetting")
@XmlEnum
public enum RheumClinPracticeSetting {

    RHEUM,
    PEDRHEUM;

    public String value() {
        return name();
    }

    public static RheumClinPracticeSetting fromValue(String v) {
        return valueOf(v);
    }

}
