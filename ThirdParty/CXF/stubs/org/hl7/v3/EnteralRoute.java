
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EnteralRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EnteralRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ENTINSTL"/>
 *     &lt;enumeration value="EFT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "EnteralRoute")
@XmlEnum
public enum EnteralRoute {

    ENTINSTL,
    EFT;

    public String value() {
        return name();
    }

    public static EnteralRoute fromValue(String v) {
        return valueOf(v);
    }

}
