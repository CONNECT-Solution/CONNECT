
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ObservationEnvironmentalIntoleranceType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ObservationEnvironmentalIntoleranceType">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="EINT"/>
 *     &lt;enumeration value="EALG"/>
 *     &lt;enumeration value="ENAINT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ObservationEnvironmentalIntoleranceType")
@XmlEnum
public enum ObservationEnvironmentalIntoleranceType {

    EINT,
    EALG,
    ENAINT;

    public String value() {
        return name();
    }

    public static ObservationEnvironmentalIntoleranceType fromValue(String v) {
        return valueOf(v);
    }

}
