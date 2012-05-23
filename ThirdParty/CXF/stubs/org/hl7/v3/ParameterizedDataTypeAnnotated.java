
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParameterizedDataTypeAnnotated.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ParameterizedDataTypeAnnotated">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ANT&lt;T>"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ParameterizedDataTypeAnnotated")
@XmlEnum
public enum ParameterizedDataTypeAnnotated {

    @XmlEnumValue("ANT<T>")
    ANT_T("ANT<T>");
    private final String value;

    ParameterizedDataTypeAnnotated(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ParameterizedDataTypeAnnotated fromValue(String v) {
        for (ParameterizedDataTypeAnnotated c: ParameterizedDataTypeAnnotated.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
