
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for KutchinHan.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="KutchinHan">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="x-HAA"/>
 *     &lt;enumeration value="x-KUC"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "KutchinHan")
@XmlEnum
public enum KutchinHan {

    @XmlEnumValue("x-HAA")
    X_HAA("x-HAA"),
    @XmlEnumValue("x-KUC")
    X_KUC("x-KUC");
    private final String value;

    KutchinHan(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static KutchinHan fromValue(String v) {
        for (KutchinHan c: KutchinHan.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
