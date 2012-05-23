
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MedicationCap.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MedicationCap">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="CHILD"/>
 *     &lt;enumeration value="EASY"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "MedicationCap")
@XmlEnum
public enum MedicationCap {

    CHILD,
    EASY;

    public String value() {
        return name();
    }

    public static MedicationCap fromValue(String v) {
        return valueOf(v);
    }

}
