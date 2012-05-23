
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IntrathecalRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="IntrathecalRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="IT"/>
 *     &lt;enumeration value="ITINJ"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "IntrathecalRoute")
@XmlEnum
public enum IntrathecalRoute {

    IT,
    ITINJ;

    public String value() {
        return name();
    }

    public static IntrathecalRoute fromValue(String v) {
        return valueOf(v);
    }

}
