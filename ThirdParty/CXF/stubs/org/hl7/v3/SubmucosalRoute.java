
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SubmucosalRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SubmucosalRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="SUBMUCINJ"/>
 *     &lt;enumeration value="SMUCMAB"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SubmucosalRoute")
@XmlEnum
public enum SubmucosalRoute {

    SUBMUCINJ,
    SMUCMAB;

    public String value() {
        return name();
    }

    public static SubmucosalRoute fromValue(String v) {
        return valueOf(v);
    }

}
