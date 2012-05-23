
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VitreousHumourRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="VitreousHumourRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="IVITIMPLNT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "VitreousHumourRoute")
@XmlEnum
public enum VitreousHumourRoute {

    IVITIMPLNT;

    public String value() {
        return name();
    }

    public static VitreousHumourRoute fromValue(String v) {
        return valueOf(v);
    }

}
