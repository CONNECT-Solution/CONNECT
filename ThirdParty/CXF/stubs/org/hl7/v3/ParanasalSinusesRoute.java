
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParanasalSinusesRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ParanasalSinusesRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="PNSINJ"/>
 *     &lt;enumeration value="PNSINSTL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ParanasalSinusesRoute")
@XmlEnum
public enum ParanasalSinusesRoute {

    PNSINJ,
    PNSINSTL;

    public String value() {
        return name();
    }

    public static ParanasalSinusesRoute fromValue(String v) {
        return valueOf(v);
    }

}
