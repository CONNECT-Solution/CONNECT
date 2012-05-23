
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SubarachnoidRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SubarachnoidRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="SUBARACHINJ"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SubarachnoidRoute")
@XmlEnum
public enum SubarachnoidRoute {

    SUBARACHINJ;

    public String value() {
        return name();
    }

    public static SubarachnoidRoute fromValue(String v) {
        return valueOf(v);
    }

}
