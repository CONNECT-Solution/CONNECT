
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActMedicalServiceCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActMedicalServiceCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ALC"/>
 *     &lt;enumeration value="CARD"/>
 *     &lt;enumeration value="CHR"/>
 *     &lt;enumeration value="DNTL"/>
 *     &lt;enumeration value="DRGRHB"/>
 *     &lt;enumeration value="GENRL"/>
 *     &lt;enumeration value="MED"/>
 *     &lt;enumeration value="OBS"/>
 *     &lt;enumeration value="ONC"/>
 *     &lt;enumeration value="PALL"/>
 *     &lt;enumeration value="PED"/>
 *     &lt;enumeration value="PHAR"/>
 *     &lt;enumeration value="PHYRHB"/>
 *     &lt;enumeration value="PSYCH"/>
 *     &lt;enumeration value="SURG"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActMedicalServiceCode")
@XmlEnum
public enum ActMedicalServiceCode {

    ALC,
    CARD,
    CHR,
    DNTL,
    DRGRHB,
    GENRL,
    MED,
    OBS,
    ONC,
    PALL,
    PED,
    PHAR,
    PHYRHB,
    PSYCH,
    SURG;

    public String value() {
        return name();
    }

    public static ActMedicalServiceCode fromValue(String v) {
        return valueOf(v);
    }

}
