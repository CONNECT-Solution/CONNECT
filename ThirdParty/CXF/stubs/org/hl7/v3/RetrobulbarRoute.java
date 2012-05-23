
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RetrobulbarRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RetrobulbarRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="RBINJ"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "RetrobulbarRoute")
@XmlEnum
public enum RetrobulbarRoute {

    RBINJ;

    public String value() {
        return name();
    }

    public static RetrobulbarRoute fromValue(String v) {
        return valueOf(v);
    }

}
