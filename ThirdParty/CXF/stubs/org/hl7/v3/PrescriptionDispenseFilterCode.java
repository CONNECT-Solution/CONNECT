
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PrescriptionDispenseFilterCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PrescriptionDispenseFilterCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="C"/>
 *     &lt;enumeration value="R"/>
 *     &lt;enumeration value="N"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PrescriptionDispenseFilterCode")
@XmlEnum
public enum PrescriptionDispenseFilterCode {

    C,
    R,
    N;

    public String value() {
        return name();
    }

    public static PrescriptionDispenseFilterCode fromValue(String v) {
        return valueOf(v);
    }

}
