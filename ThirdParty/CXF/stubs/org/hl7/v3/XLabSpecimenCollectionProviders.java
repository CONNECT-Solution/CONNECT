
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for x_LabSpecimenCollectionProviders.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="x_LabSpecimenCollectionProviders">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="communityLaboratory"/>
 *     &lt;enumeration value="homeHealth"/>
 *     &lt;enumeration value="laboratory"/>
 *     &lt;enumeration value="pathologist"/>
 *     &lt;enumeration value="phlebotomist"/>
 *     &lt;enumeration value="self"/>
 *     &lt;enumeration value="thirdParty"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "x_LabSpecimenCollectionProviders")
@XmlEnum
public enum XLabSpecimenCollectionProviders {

    @XmlEnumValue("communityLaboratory")
    COMMUNITY_LABORATORY("communityLaboratory"),
    @XmlEnumValue("homeHealth")
    HOME_HEALTH("homeHealth"),
    @XmlEnumValue("laboratory")
    LABORATORY("laboratory"),
    @XmlEnumValue("pathologist")
    PATHOLOGIST("pathologist"),
    @XmlEnumValue("phlebotomist")
    PHLEBOTOMIST("phlebotomist"),
    @XmlEnumValue("self")
    SELF("self"),
    @XmlEnumValue("thirdParty")
    THIRD_PARTY("thirdParty");
    private final String value;

    XLabSpecimenCollectionProviders(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static XLabSpecimenCollectionProviders fromValue(String v) {
        for (XLabSpecimenCollectionProviders c: XLabSpecimenCollectionProviders.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
