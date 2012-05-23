
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IntrathoracicRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="IntrathoracicRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ITHORINJ"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "IntrathoracicRoute")
@XmlEnum
public enum IntrathoracicRoute {

    ITHORINJ;

    public String value() {
        return name();
    }

    public static IntrathoracicRoute fromValue(String v) {
        return valueOf(v);
    }

}
