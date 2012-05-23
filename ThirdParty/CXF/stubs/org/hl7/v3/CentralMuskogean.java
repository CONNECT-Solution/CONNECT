
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CentralMuskogean.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CentralMuskogean">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="x-AKZ"/>
 *     &lt;enumeration value="x-CKU"/>
 *     &lt;enumeration value="x-MIK"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CentralMuskogean")
@XmlEnum
public enum CentralMuskogean {

    @XmlEnumValue("x-AKZ")
    X_AKZ("x-AKZ"),
    @XmlEnumValue("x-CKU")
    X_CKU("x-CKU"),
    @XmlEnumValue("x-MIK")
    X_MIK("x-MIK");
    private final String value;

    CentralMuskogean(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CentralMuskogean fromValue(String v) {
        for (CentralMuskogean c: CentralMuskogean.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
