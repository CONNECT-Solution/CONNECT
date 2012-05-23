
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DataTypeSetOfCodedValue.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DataTypeSetOfCodedValue">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="SET&lt;CV>"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DataTypeSetOfCodedValue")
@XmlEnum
public enum DataTypeSetOfCodedValue {

    @XmlEnumValue("SET<CV>")
    SET_CV("SET<CV>");
    private final String value;

    DataTypeSetOfCodedValue(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DataTypeSetOfCodedValue fromValue(String v) {
        for (DataTypeSetOfCodedValue c: DataTypeSetOfCodedValue.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
