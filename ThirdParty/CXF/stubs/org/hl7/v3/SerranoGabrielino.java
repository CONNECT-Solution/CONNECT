
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SerranoGabrielino.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SerranoGabrielino">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="x-SER"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SerranoGabrielino")
@XmlEnum
public enum SerranoGabrielino {

    @XmlEnumValue("x-SER")
    X_SER("x-SER");
    private final String value;

    SerranoGabrielino(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SerranoGabrielino fromValue(String v) {
        for (SerranoGabrielino c: SerranoGabrielino.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
