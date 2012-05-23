
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EntericCoatedTablet.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EntericCoatedTablet">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ECTAB"/>
 *     &lt;enumeration value="ERECTAB"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "EntericCoatedTablet")
@XmlEnum
public enum EntericCoatedTablet {

    ECTAB,
    ERECTAB;

    public String value() {
        return name();
    }

    public static EntericCoatedTablet fromValue(String v) {
        return valueOf(v);
    }

}
