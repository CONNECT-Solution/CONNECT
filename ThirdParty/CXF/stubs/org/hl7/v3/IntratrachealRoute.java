
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IntratrachealRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="IntratrachealRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ITRACHINSTIL"/>
 *     &lt;enumeration value="ITRACHMAB"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "IntratrachealRoute")
@XmlEnum
public enum IntratrachealRoute {

    ITRACHINSTIL,
    ITRACHMAB;

    public String value() {
        return name();
    }

    public static IntratrachealRoute fromValue(String v) {
        return valueOf(v);
    }

}
