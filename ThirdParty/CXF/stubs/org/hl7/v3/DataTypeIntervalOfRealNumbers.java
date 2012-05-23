
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DataTypeIntervalOfRealNumbers.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DataTypeIntervalOfRealNumbers">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="IVL&lt;REAL>"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DataTypeIntervalOfRealNumbers")
@XmlEnum
public enum DataTypeIntervalOfRealNumbers {

    @XmlEnumValue("IVL<REAL>")
    IVL_REAL("IVL<REAL>");
    private final String value;

    DataTypeIntervalOfRealNumbers(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DataTypeIntervalOfRealNumbers fromValue(String v) {
        for (DataTypeIntervalOfRealNumbers c: DataTypeIntervalOfRealNumbers.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
