
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArapahoGrosVentre.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ArapahoGrosVentre">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="x-ARP"/>
 *     &lt;enumeration value="x-ATS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ArapahoGrosVentre")
@XmlEnum
public enum ArapahoGrosVentre {

    @XmlEnumValue("x-ARP")
    X_ARP("x-ARP"),
    @XmlEnumValue("x-ATS")
    X_ATS("x-ATS");
    private final String value;

    ArapahoGrosVentre(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ArapahoGrosVentre fromValue(String v) {
        for (ArapahoGrosVentre c: ArapahoGrosVentre.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
