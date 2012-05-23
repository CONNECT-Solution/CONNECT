
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SoftTissueRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SoftTissueRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="SOFTISINJ"/>
 *     &lt;enumeration value="SOFTISINSTIL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SoftTissueRoute")
@XmlEnum
public enum SoftTissueRoute {

    SOFTISINJ,
    SOFTISINSTIL;

    public String value() {
        return name();
    }

    public static SoftTissueRoute fromValue(String v) {
        return valueOf(v);
    }

}
