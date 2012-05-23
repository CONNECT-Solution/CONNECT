
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SubconjunctivalRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SubconjunctivalRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="SCINJ"/>
 *     &lt;enumeration value="SUBCONJTA"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SubconjunctivalRoute")
@XmlEnum
public enum SubconjunctivalRoute {

    SCINJ,
    SUBCONJTA;

    public String value() {
        return name();
    }

    public static SubconjunctivalRoute fromValue(String v) {
        return valueOf(v);
    }

}
