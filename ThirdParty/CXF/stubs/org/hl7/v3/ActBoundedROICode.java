
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActBoundedROICode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActBoundedROICode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ROIFS"/>
 *     &lt;enumeration value="ROIPS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActBoundedROICode")
@XmlEnum
public enum ActBoundedROICode {

    ROIFS,
    ROIPS;

    public String value() {
        return name();
    }

    public static ActBoundedROICode fromValue(String v) {
        return valueOf(v);
    }

}
