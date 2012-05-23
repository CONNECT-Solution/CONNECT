
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Enema.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Enema">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ENEMA"/>
 *     &lt;enumeration value="RETENEMA"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Enema")
@XmlEnum
public enum Enema {

    ENEMA,
    RETENEMA;

    public String value() {
        return name();
    }

    public static Enema fromValue(String v) {
        return valueOf(v);
    }

}
