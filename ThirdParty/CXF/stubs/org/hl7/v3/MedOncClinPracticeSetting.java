
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MedOncClinPracticeSetting.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MedOncClinPracticeSetting">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ONCL"/>
 *     &lt;enumeration value="PEDHO"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "MedOncClinPracticeSetting")
@XmlEnum
public enum MedOncClinPracticeSetting {

    ONCL,
    PEDHO;

    public String value() {
        return name();
    }

    public static MedOncClinPracticeSetting fromValue(String v) {
        return valueOf(v);
    }

}
