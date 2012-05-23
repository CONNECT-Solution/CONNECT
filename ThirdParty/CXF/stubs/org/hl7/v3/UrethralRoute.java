
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UrethralRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="UrethralRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="URETHINJ"/>
 *     &lt;enumeration value="URETHINS"/>
 *     &lt;enumeration value="URETHSUP"/>
 *     &lt;enumeration value="URETHINSTL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "UrethralRoute")
@XmlEnum
public enum UrethralRoute {

    URETHINJ,
    URETHINS,
    URETHSUP,
    URETHINSTL;

    public String value() {
        return name();
    }

    public static UrethralRoute fromValue(String v) {
        return valueOf(v);
    }

}
