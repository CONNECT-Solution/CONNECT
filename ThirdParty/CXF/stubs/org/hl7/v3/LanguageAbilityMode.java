
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LanguageAbilityMode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="LanguageAbilityMode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ESGN"/>
 *     &lt;enumeration value="ESP"/>
 *     &lt;enumeration value="EWR"/>
 *     &lt;enumeration value="RSGN"/>
 *     &lt;enumeration value="RSP"/>
 *     &lt;enumeration value="RWR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "LanguageAbilityMode")
@XmlEnum
public enum LanguageAbilityMode {

    ESGN,
    ESP,
    EWR,
    RSGN,
    RSP,
    RWR;

    public String value() {
        return name();
    }

    public static LanguageAbilityMode fromValue(String v) {
        return valueOf(v);
    }

}
