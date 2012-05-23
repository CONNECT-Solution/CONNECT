
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NephClinPracticeSetting.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="NephClinPracticeSetting">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="NEPH"/>
 *     &lt;enumeration value="PEDNEPH"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "NephClinPracticeSetting")
@XmlEnum
public enum NephClinPracticeSetting {

    NEPH,
    PEDNEPH;

    public String value() {
        return name();
    }

    public static NephClinPracticeSetting fromValue(String v) {
        return valueOf(v);
    }

}
