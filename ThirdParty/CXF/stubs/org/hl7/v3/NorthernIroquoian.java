
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NorthernIroquoian.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="NorthernIroquoian">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="x-CAY"/>
 *     &lt;enumeration value="x-MOH"/>
 *     &lt;enumeration value="x-ONE"/>
 *     &lt;enumeration value="x-ONO"/>
 *     &lt;enumeration value="x-SEE"/>
 *     &lt;enumeration value="x-TUS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "NorthernIroquoian")
@XmlEnum
public enum NorthernIroquoian {

    @XmlEnumValue("x-CAY")
    X_CAY("x-CAY"),
    @XmlEnumValue("x-MOH")
    X_MOH("x-MOH"),
    @XmlEnumValue("x-ONE")
    X_ONE("x-ONE"),
    @XmlEnumValue("x-ONO")
    X_ONO("x-ONO"),
    @XmlEnumValue("x-SEE")
    X_SEE("x-SEE"),
    @XmlEnumValue("x-TUS")
    X_TUS("x-TUS");
    private final String value;

    NorthernIroquoian(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static NorthernIroquoian fromValue(String v) {
        for (NorthernIroquoian c: NorthernIroquoian.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
