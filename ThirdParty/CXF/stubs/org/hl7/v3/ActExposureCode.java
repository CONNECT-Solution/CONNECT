
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActExposureCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActExposureCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="HOMECARE"/>
 *     &lt;enumeration value="CONVEYNC"/>
 *     &lt;enumeration value="PLACE"/>
 *     &lt;enumeration value="SUBSTNCE"/>
 *     &lt;enumeration value="TRAVINT"/>
 *     &lt;enumeration value="CHLDCARE"/>
 *     &lt;enumeration value="HLTHCARE"/>
 *     &lt;enumeration value="PTNTCARE"/>
 *     &lt;enumeration value="HOSPPTNT"/>
 *     &lt;enumeration value="HOSPVSTR"/>
 *     &lt;enumeration value="HOUSEHLD"/>
 *     &lt;enumeration value="INMATE"/>
 *     &lt;enumeration value="INTIMATE"/>
 *     &lt;enumeration value="LTRMCARE"/>
 *     &lt;enumeration value="SCHOOL2"/>
 *     &lt;enumeration value="SOCIAL2"/>
 *     &lt;enumeration value="WORK2"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActExposureCode")
@XmlEnum
public enum ActExposureCode {

    HOMECARE("HOMECARE"),
    CONVEYNC("CONVEYNC"),
    PLACE("PLACE"),
    SUBSTNCE("SUBSTNCE"),
    TRAVINT("TRAVINT"),
    CHLDCARE("CHLDCARE"),
    HLTHCARE("HLTHCARE"),
    PTNTCARE("PTNTCARE"),
    HOSPPTNT("HOSPPTNT"),
    HOSPVSTR("HOSPVSTR"),
    HOUSEHLD("HOUSEHLD"),
    INMATE("INMATE"),
    INTIMATE("INTIMATE"),
    LTRMCARE("LTRMCARE"),
    @XmlEnumValue("SCHOOL2")
    SCHOOL_2("SCHOOL2"),
    @XmlEnumValue("SOCIAL2")
    SOCIAL_2("SOCIAL2"),
    @XmlEnumValue("WORK2")
    WORK_2("WORK2");
    private final String value;

    ActExposureCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ActExposureCode fromValue(String v) {
        for (ActExposureCode c: ActExposureCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
