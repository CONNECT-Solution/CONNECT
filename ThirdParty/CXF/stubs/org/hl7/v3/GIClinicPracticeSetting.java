
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GIClinicPracticeSetting.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="GIClinicPracticeSetting">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="GI"/>
 *     &lt;enumeration value="PEDGI"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "GIClinicPracticeSetting")
@XmlEnum
public enum GIClinicPracticeSetting {

    GI,
    PEDGI;

    public String value() {
        return name();
    }

    public static GIClinicPracticeSetting fromValue(String v) {
        return valueOf(v);
    }

}
