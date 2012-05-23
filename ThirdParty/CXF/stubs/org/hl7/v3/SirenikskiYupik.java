
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SirenikskiYupik.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SirenikskiYupik">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="x-ESU"/>
 *     &lt;enumeration value="x-ESS"/>
 *     &lt;enumeration value="x-EMS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SirenikskiYupik")
@XmlEnum
public enum SirenikskiYupik {

    @XmlEnumValue("x-ESU")
    X_ESU("x-ESU"),
    @XmlEnumValue("x-ESS")
    X_ESS("x-ESS"),
    @XmlEnumValue("x-EMS")
    X_EMS("x-EMS");
    private final String value;

    SirenikskiYupik(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SirenikskiYupik fromValue(String v) {
        for (SirenikskiYupik c: SirenikskiYupik.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
