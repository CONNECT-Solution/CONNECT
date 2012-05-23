
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParameterizedDataTypeInterval.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ParameterizedDataTypeInterval">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="IVL&lt;T>"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ParameterizedDataTypeInterval")
@XmlEnum
public enum ParameterizedDataTypeInterval {

    @XmlEnumValue("IVL<T>")
    IVL_T("IVL<T>");
    private final String value;

    ParameterizedDataTypeInterval(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ParameterizedDataTypeInterval fromValue(String v) {
        for (ParameterizedDataTypeInterval c: ParameterizedDataTypeInterval.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
