
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActMoodCriterion.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActMoodCriterion">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="CRT"/>
 *     &lt;enumeration value="EVN.CRT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActMoodCriterion")
@XmlEnum
public enum ActMoodCriterion {

    CRT("CRT"),
    @XmlEnumValue("EVN.CRT")
    EVN_CRT("EVN.CRT");
    private final String value;

    ActMoodCriterion(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ActMoodCriterion fromValue(String v) {
        for (ActMoodCriterion c: ActMoodCriterion.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
