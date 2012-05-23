
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LineAccessMedicalDevice.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="LineAccessMedicalDevice">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="LINE"/>
 *     &lt;enumeration value="IALINE"/>
 *     &lt;enumeration value="IVLINE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "LineAccessMedicalDevice")
@XmlEnum
public enum LineAccessMedicalDevice {

    LINE,
    IALINE,
    IVLINE;

    public String value() {
        return name();
    }

    public static LineAccessMedicalDevice fromValue(String v) {
        return valueOf(v);
    }

}
