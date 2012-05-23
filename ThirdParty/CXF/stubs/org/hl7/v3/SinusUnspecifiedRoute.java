
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SinusUnspecifiedRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SinusUnspecifiedRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ENDOSININJ"/>
 *     &lt;enumeration value="SININSTIL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SinusUnspecifiedRoute")
@XmlEnum
public enum SinusUnspecifiedRoute {

    ENDOSININJ,
    SININSTIL;

    public String value() {
        return name();
    }

    public static SinusUnspecifiedRoute fromValue(String v) {
        return valueOf(v);
    }

}
