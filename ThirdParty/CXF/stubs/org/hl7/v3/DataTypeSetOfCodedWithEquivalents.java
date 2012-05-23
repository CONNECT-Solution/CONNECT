
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DataTypeSetOfCodedWithEquivalents.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DataTypeSetOfCodedWithEquivalents">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="SET&lt;CE>"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DataTypeSetOfCodedWithEquivalents")
@XmlEnum
public enum DataTypeSetOfCodedWithEquivalents {

    @XmlEnumValue("SET<CE>")
    SET_CE("SET<CE>");
    private final String value;

    DataTypeSetOfCodedWithEquivalents(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DataTypeSetOfCodedWithEquivalents fromValue(String v) {
        for (DataTypeSetOfCodedWithEquivalents c: DataTypeSetOfCodedWithEquivalents.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
