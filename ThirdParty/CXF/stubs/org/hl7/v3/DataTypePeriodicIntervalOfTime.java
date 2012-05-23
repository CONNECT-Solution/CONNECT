
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DataTypePeriodicIntervalOfTime.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DataTypePeriodicIntervalOfTime">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="PIVL&lt;TS>"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DataTypePeriodicIntervalOfTime")
@XmlEnum
public enum DataTypePeriodicIntervalOfTime {

    @XmlEnumValue("PIVL<TS>")
    PIVL_TS("PIVL<TS>");
    private final String value;

    DataTypePeriodicIntervalOfTime(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DataTypePeriodicIntervalOfTime fromValue(String v) {
        for (DataTypePeriodicIntervalOfTime c: DataTypePeriodicIntervalOfTime.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
