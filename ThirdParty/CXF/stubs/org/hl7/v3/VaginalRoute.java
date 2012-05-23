
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VaginalRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="VaginalRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="DOUCHE"/>
 *     &lt;enumeration value="VAGINSI"/>
 *     &lt;enumeration value="VAGINS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "VaginalRoute")
@XmlEnum
public enum VaginalRoute {

    DOUCHE,
    VAGINSI,
    VAGINS;

    public String value() {
        return name();
    }

    public static VaginalRoute fromValue(String v) {
        return valueOf(v);
    }

}
