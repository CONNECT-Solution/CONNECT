
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Ambulance.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Ambulance">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="AMBT"/>
 *     &lt;enumeration value="AMBAIR"/>
 *     &lt;enumeration value="AMBGRND"/>
 *     &lt;enumeration value="AMBHELO"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Ambulance")
@XmlEnum
public enum Ambulance {

    AMBT,
    AMBAIR,
    AMBGRND,
    AMBHELO;

    public String value() {
        return name();
    }

    public static Ambulance fromValue(String v) {
        return valueOf(v);
    }

}
