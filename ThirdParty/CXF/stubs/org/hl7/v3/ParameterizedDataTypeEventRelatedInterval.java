
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParameterizedDataTypeEventRelatedInterval.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ParameterizedDataTypeEventRelatedInterval">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="EIVL&lt;T>"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ParameterizedDataTypeEventRelatedInterval")
@XmlEnum
public enum ParameterizedDataTypeEventRelatedInterval {

    @XmlEnumValue("EIVL<T>")
    EIVL_T("EIVL<T>");
    private final String value;

    ParameterizedDataTypeEventRelatedInterval(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ParameterizedDataTypeEventRelatedInterval fromValue(String v) {
        for (ParameterizedDataTypeEventRelatedInterval c: ParameterizedDataTypeEventRelatedInterval.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
