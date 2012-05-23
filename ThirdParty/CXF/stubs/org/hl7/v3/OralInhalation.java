
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OralInhalation.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="OralInhalation">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ORINHL"/>
 *     &lt;enumeration value="ORIFINHL"/>
 *     &lt;enumeration value="REBREATH"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "OralInhalation")
@XmlEnum
public enum OralInhalation {

    ORINHL,
    ORIFINHL,
    REBREATH;

    public String value() {
        return name();
    }

    public static OralInhalation fromValue(String v) {
        return valueOf(v);
    }

}
