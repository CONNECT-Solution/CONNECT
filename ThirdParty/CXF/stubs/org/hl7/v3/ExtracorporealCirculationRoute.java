
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ExtracorporealCirculationRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ExtracorporealCirculationRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="EXTCORPDIF"/>
 *     &lt;enumeration value="EXTCORPINJ"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ExtracorporealCirculationRoute")
@XmlEnum
public enum ExtracorporealCirculationRoute {

    EXTCORPDIF,
    EXTCORPINJ;

    public String value() {
        return name();
    }

    public static ExtracorporealCirculationRoute fromValue(String v) {
        return valueOf(v);
    }

}
