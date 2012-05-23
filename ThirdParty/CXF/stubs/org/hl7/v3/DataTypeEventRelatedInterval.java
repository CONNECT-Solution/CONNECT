
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DataTypeEventRelatedInterval.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DataTypeEventRelatedInterval">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="EIVL&lt;TS>"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DataTypeEventRelatedInterval")
@XmlEnum
public enum DataTypeEventRelatedInterval {

    @XmlEnumValue("EIVL<TS>")
    EIVL_TS("EIVL<TS>");
    private final String value;

    DataTypeEventRelatedInterval(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DataTypeEventRelatedInterval fromValue(String v) {
        for (DataTypeEventRelatedInterval c: DataTypeEventRelatedInterval.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
