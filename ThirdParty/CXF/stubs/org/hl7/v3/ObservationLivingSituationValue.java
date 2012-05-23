
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ObservationLivingSituationValue.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ObservationLivingSituationValue">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="LIVSIT"/>
 *     &lt;enumeration value="ALONE"/>
 *     &lt;enumeration value="DEPCHD"/>
 *     &lt;enumeration value="DEPSPS"/>
 *     &lt;enumeration value="DEPYGCHD"/>
 *     &lt;enumeration value="FAM"/>
 *     &lt;enumeration value="RELAT"/>
 *     &lt;enumeration value="SPS"/>
 *     &lt;enumeration value="UNREL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ObservationLivingSituationValue")
@XmlEnum
public enum ObservationLivingSituationValue {

    LIVSIT,
    ALONE,
    DEPCHD,
    DEPSPS,
    DEPYGCHD,
    FAM,
    RELAT,
    SPS,
    UNREL;

    public String value() {
        return name();
    }

    public static ObservationLivingSituationValue fromValue(String v) {
        return valueOf(v);
    }

}
