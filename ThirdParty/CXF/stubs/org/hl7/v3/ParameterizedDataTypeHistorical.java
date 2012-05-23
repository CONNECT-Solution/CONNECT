
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParameterizedDataTypeHistorical.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ParameterizedDataTypeHistorical">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="HXIT&lt;T>"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ParameterizedDataTypeHistorical")
@XmlEnum
public enum ParameterizedDataTypeHistorical {

    @XmlEnumValue("HXIT<T>")
    HXIT_T("HXIT<T>");
    private final String value;

    ParameterizedDataTypeHistorical(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ParameterizedDataTypeHistorical fromValue(String v) {
        for (ParameterizedDataTypeHistorical c: ParameterizedDataTypeHistorical.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
