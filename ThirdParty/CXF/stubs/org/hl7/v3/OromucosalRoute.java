
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OromucosalRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="OromucosalRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="GARGLE"/>
 *     &lt;enumeration value="SUCK"/>
 *     &lt;enumeration value="SWISHSPIT"/>
 *     &lt;enumeration value="SWISHSWAL"/>
 *     &lt;enumeration value="ORMUC"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "OromucosalRoute")
@XmlEnum
public enum OromucosalRoute {

    GARGLE,
    SUCK,
    SWISHSPIT,
    SWISHSWAL,
    ORMUC;

    public String value() {
        return name();
    }

    public static OromucosalRoute fromValue(String v) {
        return valueOf(v);
    }

}
