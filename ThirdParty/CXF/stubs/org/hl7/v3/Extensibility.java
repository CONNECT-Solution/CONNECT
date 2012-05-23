
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Extensibility.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Extensibility">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="CNE"/>
 *     &lt;enumeration value="CWE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Extensibility")
@XmlEnum
public enum Extensibility {

    CNE,
    CWE;

    public String value() {
        return name();
    }

    public static Extensibility fromValue(String v) {
        return valueOf(v);
    }

}
