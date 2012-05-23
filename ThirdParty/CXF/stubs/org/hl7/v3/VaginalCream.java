
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VaginalCream.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="VaginalCream">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="VAGCRM"/>
 *     &lt;enumeration value="VAGCRMAPL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "VaginalCream")
@XmlEnum
public enum VaginalCream {

    VAGCRM,
    VAGCRMAPL;

    public String value() {
        return name();
    }

    public static VaginalCream fromValue(String v) {
        return valueOf(v);
    }

}
